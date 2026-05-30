package su.nightexpress.excellentjobs.api.stats;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.nightcore.userdata.UserData;

@NullMarked
public record TopEntry(UserData user, String score, int position) {

}
