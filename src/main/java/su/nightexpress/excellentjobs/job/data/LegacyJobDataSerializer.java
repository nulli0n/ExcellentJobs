package su.nightexpress.excellentjobs.job.data;

import java.lang.reflect.Type;
import java.util.Set;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;

public class LegacyJobDataSerializer implements JsonDeserializer<LegacyJobData>, JsonSerializer<LegacyJobData> {

    @Override
    public LegacyJobData deserialize(JsonElement json, Type type,
                                     JsonDeserializationContext contex) throws JsonParseException {
        JsonObject object = json.getAsJsonObject();

        String jobState = object.get("state").getAsString();
        boolean inactive = jobState.equalsIgnoreCase("inactive");
        int level = object.get("level").getAsInt();
        int xp = object.get("xp").getAsInt();
        long cooldown = object.get("cooldown") == null ? 0L : object.get("cooldown").getAsLong();

        Set<Integer> obtainedLevelRewards = contex.deserialize(object.get("obtainedLevelRewards"),
            new TypeToken<Set<Integer>>() {
            }.getType());

        return new LegacyJobData(inactive, level, xp, cooldown, obtainedLevelRewards);
    }

    @Override
    public JsonElement serialize(LegacyJobData data, Type type, JsonSerializationContext contex) {
        return new JsonObject();
    }
}
