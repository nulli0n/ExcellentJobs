package su.nightexpress.excellentjobs.job.data;

import java.sql.Timestamp;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import su.nightexpress.excellentjobs.api.grind.GrindObjectiveProperty;
import su.nightexpress.nightcore.db.column.Column;
import su.nightexpress.nightcore.db.statement.RowMapper;
import su.nightexpress.nightcore.db.statement.template.InsertStatement;
import su.nightexpress.nightcore.db.statement.template.SelectStatement;

public class JobDataQueries {

    private JobDataQueries() {
    }

    private static final Gson GSON = new GsonBuilder()
        .setPrettyPrinting()
        .registerTypeAdapter(LegacyJobData.class, new LegacyJobDataSerializer())
        .create();

    public static final Column<UUID>   PLAYER_ID_COLUMN = Column.uuidType("playerId").primaryKey().build();
    public static final Column<String> JOB_ID_COLUMN    = Column.stringType("jobId", 50)
        .primaryKey()
        .build();

    public static final Column<Boolean> ACTIVE_COLUMN         = Column.booleanType("active").build();
    public static final Column<Integer> LEVEL_COLUMN          = Column.intType("level").build();
    public static final Column<Double>  XP_COLUMN             = Column.doubleType("xp").build();
    public static final Column<Long>    LEAVE_COOLDOWN_COLUMN = Column.longType("leaveCooldown").build();

    public static final Column<Map<GrindObjectiveProperty, Double>> GRIND_LIMIT_PROGRESS_COLUMN = Column
        .jsonMap("grind_limit_progress", GSON, GrindObjectiveProperty.class, Double.class)
        .defaultValue("{}")
        .build();

    public static final Column<Timestamp> GRIND_LIMIT_TIMESTAMP_COLUMN = Column.timestamp("grind_limit_timestamp")
        .defaultValue(new Timestamp(0L).toString())
        .build();

    public static final Column<Set<Integer>> CLAIMED_REWARDS_COLUMN = Column
        .jsonSet("claimed_level_rewards", GSON, Integer.class)
        .defaultValue("[]")
        .build();

    public static final Column<Map<String, LegacyJobData>> LEGACY_DATA_COLUMN = Column.jsonMap("data", GSON,
        String.class,
        LegacyJobData.class)
        .build();

    public static final RowMapper<LegacyUserJobData> LEGACY_DATA_MAPPER = resultSet -> {
        UUID playerId = PLAYER_ID_COLUMN.readOrThrow(resultSet);
        Map<String, LegacyJobData> jobData = LEGACY_DATA_COLUMN.readOrThrow(resultSet);

        return new LegacyUserJobData(playerId, jobData);
    };

    public static final RowMapper<JobData> JOB_DATA_MAPPER = resultSet -> {
        UUID playerId = PLAYER_ID_COLUMN.readOrThrow(resultSet);
        String jobId = JOB_ID_COLUMN.readOrThrow(resultSet);
        boolean active = ACTIVE_COLUMN.readOrThrow(resultSet);
        long leaveCooldown = LEAVE_COOLDOWN_COLUMN.readOrThrow(resultSet);

        double xp = XP_COLUMN.readOrThrow(resultSet);
        int level = LEVEL_COLUMN.readOrThrow(resultSet);
        Set<Integer> claimedRewards = CLAIMED_REWARDS_COLUMN.readOrThrow(resultSet);

        Map<GrindObjectiveProperty, Double> limitProgress = GRIND_LIMIT_PROGRESS_COLUMN.readOrThrow(resultSet);
        long limiTimestamp = GRIND_LIMIT_TIMESTAMP_COLUMN.readOrThrow(resultSet).getTime();

        return new JobData.Builder(playerId, jobId)
            .setActive(active)
            .setLeaveCooldown(leaveCooldown)
            .setXP(xp)
            .setLevel(level)
            .setClaimedLevelRewards(claimedRewards)
            .setLimitProgress(limitProgress)
            .setLimitTimestamp(limiTimestamp)
            .build();
    };

    public static final SelectStatement<JobData> SELECT_DATA_STATEMENT = SelectStatement
        .builder(JOB_DATA_MAPPER)
        .build();

    public static final InsertStatement<JobData> INSERT_OR_UPDATE_STATEMENT = InsertStatement
        .builder(JobData.class)
        .updateOnConflict()
        .setUUID(PLAYER_ID_COLUMN, JobData::getPlayerId)
        .setString(JOB_ID_COLUMN, JobData::getJobId)
        .setBoolean(ACTIVE_COLUMN, JobData::isActive)
        .setLong(LEAVE_COOLDOWN_COLUMN, JobData::getLeaveCooldown)
        .setDouble(XP_COLUMN, JobData::getXP)
        .setInt(LEVEL_COLUMN, JobData::getLevel)
        .setString(CLAIMED_REWARDS_COLUMN, data -> GSON.toJson(data.getClaimedLevelRewards()))
        .setString(GRIND_LIMIT_PROGRESS_COLUMN, data -> GSON.toJson(data.getLimitProgress()))
        .setTimestamp(GRIND_LIMIT_TIMESTAMP_COLUMN, data -> new Timestamp(data.getLimitTimestamp()))
        .build();
}
