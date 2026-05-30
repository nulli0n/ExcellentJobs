package su.nightexpress.excellentjobs.contract.data;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentjobs.contract.ContractSettings;
import su.nightexpress.excellentjobs.data.Database;
import su.nightexpress.nightcore.NightPlugin;
import su.nightexpress.nightcore.db.data.DataConstants;
import su.nightexpress.nightcore.db.data.DataSettings;
import su.nightexpress.nightcore.db.data.layer.AbstractTwoLevelDataManager;
import su.nightexpress.nightcore.db.statement.condition.Operator;
import su.nightexpress.nightcore.db.statement.condition.Wheres;
import su.nightexpress.nightcore.db.table.Table;
import su.nightexpress.nightcore.userdata.UserLoginListener;
import su.nightexpress.nightcore.util.Players;

@NullMarked
public class ContractDataManager extends AbstractTwoLevelDataManager<UUID, String, ContractData> implements UserLoginListener {

    private final Database         dataHandler;
    private final ContractSettings settings;

    private final Table contractsTable;

    public ContractDataManager(NightPlugin plugin, Database dataHandler, ContractSettings settings) {
        super(plugin, ContractData::getPlayerId, ContractData::getJobId);
        this.dataHandler = dataHandler;
        this.settings = settings;

        this.contractsTable = Table.builder(dataHandler.getTablePrefix() + "_contracts")
            .withColumn(ContractDataQueries.PLAYER_ID)
            .withColumn(ContractDataQueries.JOB_ID)
            .withColumn(ContractDataQueries.ACTIVE)
            .withColumn(ContractDataQueries.COMPLETED_IDS)
            .withColumn(ContractDataQueries.CONTRACT_ID)
            .withColumn(ContractDataQueries.CONTRACT_POINTS)
            .withColumn(ContractDataQueries.CONTRACT_LEAVE_COOLDOWN)
            .withColumn(ContractDataQueries.PROMOTION_STARTED)
            .withColumn(ContractDataQueries.PROMOTION_DEADLINE)
            .withColumn(ContractDataQueries.PROMOTION_COMPLETED)
            .withColumn(ContractDataQueries.PROMOTION_COOLDOWN)
            .build();
    }

    @Override
    public DataSettings getSettings() {
        return this.settings;
    }

    @Override
    protected void initialize() {
        this.dataHandler.createTable(this.contractsTable);
        this.addSync();
        this.initOnline();
    }

    private void addSync() {
        this.dataHandler.addTableSync(this.contractsTable, resultSet -> {
            try {
                ContractData data = ContractDataQueries.DATA_ROW_MAPPER.map(resultSet);
                if (data == null) return;

                this.cacheNewData(data);
            }
            catch (SQLException exception) {
                exception.printStackTrace();
            }
        });
    }

    private void initOnline() {
        Players.getOnline().forEach(player -> {
            UUID playerId = player.getUniqueId();
            this.selectByKey(playerId).forEach(this::cachePermanently);
        });
    }

    @Override
    protected ContractData createData(UUID playerId, String jobId) {
        return new ContractData(playerId, jobId);
    }

    @Override
    public UUID getGlobalKey() {
        return DataConstants.GLOBAL_ID;
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
    protected List<ContractData> selectAll() {
        return this.dataHandler.selectAny(this.contractsTable, ContractDataQueries.DATA_SELECT);
    }

    @Override
    protected List<ContractData> selectByKey(UUID playerId) {
        Wheres<Object> wheres = Wheres.whereUUID(ContractDataQueries.PLAYER_ID, o -> playerId);

        return this.dataHandler.selectWhere(this.contractsTable, ContractDataQueries.DATA_SELECT, wheres);
    }

    @Override
    protected @Nullable ContractData selectByKeyAndId(UUID playerId, String jobId) {
        Wheres<Object> wheres = Wheres
            .whereUUID(ContractDataQueries.PLAYER_ID, o -> playerId)
            .and(ContractDataQueries.JOB_ID, Operator.EQUALS_IGNORE_CASE, o -> jobId);

        return this.dataHandler.selectFirst(this.contractsTable, ContractDataQueries.DATA_SELECT, wheres).orElse(null);
    }

    @Override
    protected void upsertData(Collection<ContractData> datas) {
        this.dataHandler.insert(this.contractsTable, ContractDataQueries.DATA_INSERT, datas);
    }

    @Override
    protected void deleteData(Collection<ContractData> datas) {
        Wheres<ContractData> wheres = Wheres
            .whereUUID(ContractDataQueries.PLAYER_ID, ContractData::getPlayerId)
            .and(ContractDataQueries.JOB_ID, Operator.EQUALS_IGNORE_CASE, ContractData::getJobId);

        this.dataHandler.delete(this.contractsTable, datas, wheres);
    }
}
