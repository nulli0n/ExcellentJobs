package su.nightexpress.excellentjobs.job.data;

import java.util.Map;
import java.util.UUID;

import org.jspecify.annotations.NullMarked;

@NullMarked
public record LegacyUserJobData(UUID playerId, Map<String, LegacyJobData> dataMap) {

}
