package su.nightexpress.excellentjobs.stats.data;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentjobs.JobsPlugin;
import su.nightexpress.excellentjobs.data.Database;
import su.nightexpress.excellentjobs.stats.core.StatsSettings;
import su.nightexpress.excellentjobs.stats.model.JobStats;
import su.nightexpress.nightcore.db.data.DataConstants;
import su.nightexpress.nightcore.db.data.DataSettings;
import su.nightexpress.nightcore.db.data.layer.AbstractTwoLevelDataManager;
import su.nightexpress.nightcore.db.statement.condition.Operator;
import su.nightexpress.nightcore.db.statement.condition.Wheres;
import su.nightexpress.nightcore.db.table.Table;
import su.nightexpress.nightcore.userdata.UserLoginListener;
import su.nightexpress.nightcore.util.Players;

@NullMarked
public class StatsDataManager extends AbstractTwoLevelDataManager<UUID, String, JobStats> implements UserLoginListener {

    private final Database      database;
    private final StatsSettings settings;

    private final Table table;

    public StatsDataManager(JobsPlugin plugin, Database database, StatsSettings settings) {
        super(plugin, JobStats::getPlayerId, JobStats::getJobId);
        this.database = database;
        this.settings = settings;

        this.table = Table.builder(database.getTablePrefix() + settings.getDataTableName())
            .withColumn(StatsDataQueries.PLAYER_ID_COLUMN)
            .withColumn(StatsDataQueries.JOB_ID_COLUMN)
            .withColumn(StatsDataQueries.INCOME_COLUMN)
            .build();
    }

    @Override
    public DataSettings getSettings() {
        return this.settings;
    }

    @Override
    public UUID getGlobalKey() {
        return DataConstants.GLOBAL_ID;
    }

    @Override
    protected void initialize() {
        this.createTable();
        this.addSync();
        this.initOnline();
    }

    private void createTable() {
        this.database.createTable(this.table);
    }

    private void initOnline() {
        Players.getOnline().forEach(player -> {
            this.selectByKey(player.getUniqueId()).forEach(this::cachePermanently);
        });
    }

    private void addSync() {
        this.database.addTableSync(this.table, resultSet -> {
            try {
                JobStats stats = StatsDataQueries.JOB_STATS_ROW_MAPPER.map(resultSet);
                if (stats != null) this.cacheNewData(stats);
            }
            catch (SQLException exception) {
                exception.printStackTrace();
            }
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
    protected JobStats createData(UUID parentKey, String dataKey) {
        return new JobStats(parentKey, dataKey);
    }


    @Override
    protected List<JobStats> selectAll() {
        return this.database.selectAny(this.table, StatsDataQueries.SELECT_JOB_STATS);
    }

    @Override
    protected List<JobStats> selectByKey(UUID playerId) {
        Wheres<Object> wheres = Wheres.whereUUID(StatsDataQueries.JOB_ID_COLUMN, o -> playerId);

        return this.database.selectWhere(this.table, StatsDataQueries.SELECT_JOB_STATS, wheres);
    }

    @Override
    protected @Nullable JobStats selectByKeyAndId(UUID playerId, String jobId) {
        Wheres<Object> wheres = Wheres.whereUUID(StatsDataQueries.JOB_ID_COLUMN, o -> playerId)
            .and(StatsDataQueries.JOB_ID_COLUMN, Operator.EQUALS_IGNORE_CASE, o -> jobId);

        return this.database.selectFirst(this.table, StatsDataQueries.SELECT_JOB_STATS, wheres).orElse(null);
    }

    @Override
    protected void deleteData(Collection<JobStats> datas) {
        Wheres<JobStats> wheres = Wheres.whereUUID(StatsDataQueries.JOB_ID_COLUMN, JobStats::getPlayerId)
            .and(StatsDataQueries.JOB_ID_COLUMN, Operator.EQUALS_IGNORE_CASE, JobStats::getJobId);

        this.database.delete(this.table, datas, wheres);

    }

    @Override
    protected void upsertData(Collection<JobStats> datas) {
        this.database.insert(this.table, StatsDataQueries.INSERT_OR_UPDATE_JOB_STATS, datas);
    }
}
