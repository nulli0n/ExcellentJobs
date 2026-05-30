package su.nightexpress.excellentjobs.job.data;

import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentjobs.data.Database;
import su.nightexpress.excellentjobs.job.core.JobSettings;
import su.nightexpress.nightcore.NightPlugin;
import su.nightexpress.nightcore.db.data.DataConstants;
import su.nightexpress.nightcore.db.data.DataSettings;
import su.nightexpress.nightcore.db.data.layer.AbstractTwoLevelDataManager;
import su.nightexpress.nightcore.db.statement.SQLStatements;
import su.nightexpress.nightcore.db.statement.condition.Operator;
import su.nightexpress.nightcore.db.statement.condition.Wheres;
import su.nightexpress.nightcore.db.statement.template.SelectStatement;
import su.nightexpress.nightcore.db.table.Table;
import su.nightexpress.nightcore.userdata.UserLoginListener;
import su.nightexpress.nightcore.util.Players;

@NullMarked
public class JobDataManager extends AbstractTwoLevelDataManager<UUID, String, JobData> implements UserLoginListener {

    private final Database    database;
    private final JobSettings settings;
    private final Table       table;

    private String legacyUserTable;

    public JobDataManager(NightPlugin plugin, Database database, JobSettings settings) {
        super(plugin, JobData::getPlayerId, JobData::getJobId);
        this.database = database;
        this.settings = new JobSettings();

        this.table = Table.builder(this.database.getTablePrefix() + settings.getTableName())
            .withColumn(JobDataQueries.PLAYER_ID_COLUMN)
            .withColumn(JobDataQueries.JOB_ID_COLUMN)
            .withColumn(JobDataQueries.ACTIVE_COLUMN)
            .withColumn(JobDataQueries.LEAVE_COOLDOWN_COLUMN)
            .withColumn(JobDataQueries.XP_COLUMN)
            .withColumn(JobDataQueries.LEVEL_COLUMN)
            .withColumn(JobDataQueries.CLAIMED_REWARDS_COLUMN)
            .withColumn(JobDataQueries.GRIND_LIMIT_PROGRESS_COLUMN)
            .withColumn(JobDataQueries.GRIND_LIMIT_TIMESTAMP_COLUMN)
            .build();

        this.legacyUserTable = database.getTablePrefix() + "_userdata";
    }

    @Override
    public DataSettings getSettings() {
        return this.settings;
    }

    @Override
    protected void initialize() {
        this.createTable();
        this.updateLegacyData();

        this.initOnline();
        this.addSync();
    }

    private void createTable() {
        this.database.createTable(this.table);
    }

    private void updateLegacyData() {
        if (!SQLStatements.hasTable(this.database.getConnector(), this.legacyUserTable)) return;
        if (!SQLStatements.hasColumn(this.database.getConnector(), this.legacyUserTable, "data")) return;

        SelectStatement<LegacyUserJobData> statement = SelectStatement
            .builder(JobDataQueries.LEGACY_DATA_MAPPER)
            .build();

        Set<JobData> newDatas = new HashSet<>();

        SQLStatements.selectAny(this.database.getConnector(), this.legacyUserTable, statement).forEach(legacyData -> {
            UUID playerId = legacyData.playerId();
            Map<String, LegacyJobData> dataMap = legacyData.dataMap();

            dataMap.forEach((jobId, data) -> {
                if (data == null) return;

                JobData jobData = new JobData.Builder(playerId, jobId)
                    .setActive(!data.isInactive())
                    .setClaimedLevelRewards(data.getClaimedLevelRewards())
                    .setLevel(data.getLevel())
                    .setXP(data.getXP())
                    .setLeaveCooldown(data.getCooldown())
                    .build();
                newDatas.add(jobData);
            });
        });

        this.upsertData(newDatas);

        SQLStatements.executeUpdate(this.database.getConnector(), "ALTER TABLE `" + this.legacyUserTable +
            "` DROP COLUMN `data`;");
    }

    private void addSync() {
        this.database.addTableSync(this.table, resultSet -> {
            try {
                JobData data = JobDataQueries.JOB_DATA_MAPPER.map(resultSet);
                if (data != null) this.cacheNewData(data);
            }
            catch (SQLException exception) {
                exception.printStackTrace();
            }
        });
    }

    private void initOnline() {
        Players.getOnline().forEach(player -> {
            this.selectByKey(player.getUniqueId()).forEach(this::cachePermanently);
        });
    }

    @Override
    public void onJoin(PlayerJoinEvent event) {
        this.activate(event.getPlayer().getUniqueId());
    }

    @Override
    public void onPreLogin(AsyncPlayerPreLoginEvent event) {
        UUID playerId = event.getUniqueId();

        this.selectByKey(playerId).forEach(this::cacheTemporary);
    }

    @Override
    public void onQuit(PlayerQuitEvent event) {
        this.deactivate(event.getPlayer().getUniqueId());
    }

    @Override
    protected JobData createData(UUID parentKey, String dataKey) {
        return new JobData(parentKey, dataKey);
    }

    @Override
    public UUID getGlobalKey() {
        return DataConstants.GLOBAL_ID;
    }

    public Map<String, Integer> countEmployees() {
        Map<String, Integer> jobCount = new HashMap<>();

        Wheres<Object> wheres = Wheres.where(JobDataQueries.ACTIVE_COLUMN, Operator.EQUALS, o -> true);

        SelectStatement<Object> statement = SelectStatement.builder(resultSet -> {
            String jobId = JobDataQueries.JOB_ID_COLUMN.readOrThrow(resultSet);
            int total = jobCount.getOrDefault(jobId, 0);
            jobCount.put(jobId, total + 1);

            return new Object();
        })
            .column(JobDataQueries.JOB_ID_COLUMN)
            .column(JobDataQueries.ACTIVE_COLUMN)
            .build();

        this.database.selectWhere(this.table, statement, wheres);

        return jobCount;
    }

    public Map<String, Map<UUID, Integer>> getJobLevels() {
        Map<String, Map<UUID, Integer>> levelMap = new HashMap<>();

        Wheres<Object> wheres = Wheres.where(JobDataQueries.ACTIVE_COLUMN, Operator.EQUALS, o -> true);

        SelectStatement<Object> statement = SelectStatement.builder(resultSet -> {
            UUID playerId = JobDataQueries.PLAYER_ID_COLUMN.readOrThrow(resultSet);
            String jobId = JobDataQueries.JOB_ID_COLUMN.readOrThrow(resultSet);
            int level = JobDataQueries.LEVEL_COLUMN.readOrThrow(resultSet);

            levelMap.computeIfAbsent(jobId, k -> new HashMap<>()).put(playerId, level);

            return new Object();
        })
            .column(JobDataQueries.PLAYER_ID_COLUMN)
            .column(JobDataQueries.JOB_ID_COLUMN)
            .column(JobDataQueries.LEVEL_COLUMN)
            .build();

        this.database.selectWhere(this.table, statement, wheres);

        return levelMap;
    }

    @Override
    protected List<JobData> selectAll() {
        return this.database.selectAny(this.table, JobDataQueries.SELECT_DATA_STATEMENT);
    }

    @Override
    protected List<JobData> selectByKey(UUID playerId) {
        Wheres<Object> wheres = Wheres.whereUUID(JobDataQueries.PLAYER_ID_COLUMN, o -> playerId);

        return this.database.selectWhere(this.table, JobDataQueries.SELECT_DATA_STATEMENT, wheres);
    }

    @Override
    protected @Nullable JobData selectByKeyAndId(UUID playerId, String jobId) {
        Wheres<Object> wheres = Wheres.whereUUID(JobDataQueries.PLAYER_ID_COLUMN, o -> playerId)
            .and(JobDataQueries.JOB_ID_COLUMN, Operator.EQUALS_IGNORE_CASE, o -> jobId);

        return this.database.selectWhere(this.table, JobDataQueries.SELECT_DATA_STATEMENT, wheres).stream()
            .findFirst()
            .orElse(null);
    }

    @Override
    protected void deleteData(Collection<JobData> datas) {
        Wheres<JobData> wheres = Wheres.whereUUID(JobDataQueries.PLAYER_ID_COLUMN, JobData::getPlayerId)
            .and(JobDataQueries.JOB_ID_COLUMN, Operator.EQUALS_IGNORE_CASE, JobData::getJobId);

        this.database.delete(this.table, wheres);
    }

    @Override
    protected void upsertData(Collection<JobData> datas) {
        this.database.insert(this.table, JobDataQueries.INSERT_OR_UPDATE_STATEMENT, datas);
    }
}
