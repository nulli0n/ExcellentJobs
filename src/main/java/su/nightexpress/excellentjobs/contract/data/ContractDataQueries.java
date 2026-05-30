package su.nightexpress.excellentjobs.contract.data;

import java.util.Set;
import java.util.UUID;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import su.nightexpress.excellentjobs.JobsConstants;
import su.nightexpress.nightcore.db.column.Column;
import su.nightexpress.nightcore.db.statement.RowMapper;
import su.nightexpress.nightcore.db.statement.template.InsertStatement;
import su.nightexpress.nightcore.db.statement.template.SelectStatement;

public class ContractDataQueries {

    private ContractDataQueries() {
    }

    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static final Column<UUID> PLAYER_ID = Column.uuidType("player_id").primaryKey().build();

    public static final Column<String> JOB_ID = Column.stringType("job_id", JobsConstants.MAX_MODEL_ID_LENGTH)
        .primaryKey().build();

    public static final Column<Boolean> ACTIVE = Column.booleanType("active").defaultValue(false).build();

    public static final Column<Set<String>> COMPLETED_IDS = Column.jsonSet("completed_ids", GSON,
        String.class)
        .defaultValue("[]")
        .build();

    public static final Column<String> CONTRACT_ID = Column.stringType("contract_id", JobsConstants.MAX_MODEL_ID_LENGTH)
        .nullable()
        .build();

    public static final Column<Double> CONTRACT_POINTS = Column.doubleType("contract_points")
        .defaultValue(0D).build();

    public static final Column<Long> CONTRACT_LEAVE_COOLDOWN = Column.longType("leave_cooldown")
        .defaultValue(0L).build();

    public static final Column<Boolean> PROMOTION_STARTED = Column.booleanType("promotion_started")
        .defaultValue(false).build();

    public static final Column<Long> PROMOTION_DEADLINE = Column.longType("promotion_deadline")
        .defaultValue(0L).build();

    public static final Column<Boolean> PROMOTION_COMPLETED = Column.booleanType("promotion_completed")
        .defaultValue(false).build();

    public static final Column<Long> PROMOTION_COOLDOWN = Column.longType("promotion_cooldown")
        .defaultValue(0L).build();

    public static final RowMapper<ContractData> DATA_ROW_MAPPER = resultSet -> {
        UUID playerId = PLAYER_ID.readOrThrow(resultSet);
        String jobId = JOB_ID.readOrThrow(resultSet);

        boolean active = ACTIVE.readOrThrow(resultSet);
        Set<String> completedIds = COMPLETED_IDS.readOrThrow(resultSet);

        String contractId = CONTRACT_ID.read(resultSet).orElse(null);
        double contractPoints = CONTRACT_POINTS.readOrThrow(resultSet);

        long leaveCooldown = CONTRACT_LEAVE_COOLDOWN.readOrThrow(resultSet);

        boolean promotionStarted = PROMOTION_STARTED.readOrThrow(resultSet);
        long promotionDeadline = PROMOTION_DEADLINE.readOrThrow(resultSet);
        boolean promotionCompleted = PROMOTION_COMPLETED.readOrThrow(resultSet);
        long promotionCooldown = PROMOTION_COOLDOWN.read(resultSet).orElse(0L);

        ContractData data = new ContractData(playerId, jobId);

        data.setActive(active);
        data.setCompletedIds(completedIds);
        data.setContractId(contractId);
        data.setContractPoints(contractPoints);
        data.setLeaveCooldown(leaveCooldown);
        data.setPromotionStarted(promotionStarted);
        data.setPromotionDeadline(promotionDeadline);
        data.setPromotionCompleted(promotionCompleted);
        data.setPromotionCooldown(promotionCooldown);

        return data;
    };

    public static final SelectStatement<ContractData> DATA_SELECT = SelectStatement
        .builder(DATA_ROW_MAPPER)
        .build();

    public static final InsertStatement<ContractData> DATA_INSERT = InsertStatement.builder(ContractData.class)
        .updateOnConflict()
        .setUUID(PLAYER_ID, ContractData::getPlayerId)
        .setString(JOB_ID, ContractData::getJobId)
        .setBoolean(ACTIVE, ContractData::isActive)
        .setString(COMPLETED_IDS, data -> GSON.toJson(data.getCompletedIds()))
        .setString(CONTRACT_ID, ContractData::getContractId)
        .setDouble(CONTRACT_POINTS, ContractData::getContractPoints)
        .setLong(CONTRACT_LEAVE_COOLDOWN, ContractData::getLeaveCooldown)
        .setBoolean(PROMOTION_STARTED, ContractData::isPromotionStarted)
        .setLong(PROMOTION_DEADLINE, ContractData::getPromotionDeadline)
        .setBoolean(PROMOTION_COMPLETED, ContractData::isPromotionCompleted)
        .setLong(PROMOTION_COOLDOWN, ContractData::getPromotionCooldown)
        .build();
}
