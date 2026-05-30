package su.nightexpress.excellentjobs.stats.data;

import java.util.UUID;

import su.nightexpress.excellentjobs.JobsConstants;
import su.nightexpress.excellentjobs.stats.model.JobStats;
import su.nightexpress.nightcore.db.column.Column;
import su.nightexpress.nightcore.db.statement.RowMapper;
import su.nightexpress.nightcore.db.statement.template.InsertStatement;
import su.nightexpress.nightcore.db.statement.template.SelectStatement;

public class StatsDataQueries {

    private StatsDataQueries() {
    }

    public static final Column<UUID> PLAYER_ID_COLUMN = Column.uuidType("player_id")
        .primaryKey().build();

    public static final Column<String> JOB_ID_COLUMN = Column.stringType("job_id", JobsConstants.MAX_MODEL_ID_LENGTH)
        .primaryKey().build();

    public static final Column<Double> INCOME_COLUMN = Column.doubleType("income").defaultValue(0D).build();

    public static final RowMapper<JobStats> JOB_STATS_ROW_MAPPER = resultSet -> {
        UUID playerId = PLAYER_ID_COLUMN.readOrThrow(resultSet);
        String jobId = JOB_ID_COLUMN.readOrThrow(resultSet);
        double income = INCOME_COLUMN.readOrThrow(resultSet);

        JobStats stats = new JobStats(playerId, jobId);
        stats.setIncome(income);
        return stats;
    };

    public static final SelectStatement<JobStats> SELECT_JOB_STATS = SelectStatement
        .builder(JOB_STATS_ROW_MAPPER)
        .build();

    public static final InsertStatement<JobStats> INSERT_OR_UPDATE_JOB_STATS = InsertStatement.builder(JobStats.class)
        .updateOnConflict()
        .setUUID(PLAYER_ID_COLUMN, JobStats::getPlayerId)
        .setString(JOB_ID_COLUMN, JobStats::getJobId)
        .setDouble(INCOME_COLUMN, JobStats::getIncome)
        .build();
}
