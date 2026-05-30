package su.nightexpress.excellentjobs.zone.command.argument;

import java.util.List;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentjobs.zone.ZoneManager;
import su.nightexpress.excellentjobs.zone.core.ZoneLang;
import su.nightexpress.excellentjobs.zone.model.Zone;
import su.nightexpress.nightcore.commands.SuggestionsProvider;
import su.nightexpress.nightcore.commands.argument.ArgumentReader;
import su.nightexpress.nightcore.commands.argument.ArgumentType;
import su.nightexpress.nightcore.commands.context.CommandContext;
import su.nightexpress.nightcore.commands.context.CommandContextBuilder;
import su.nightexpress.nightcore.commands.exceptions.CommandSyntaxException;

@NullMarked
public class ZoneArgumentType implements ArgumentType<Zone>, SuggestionsProvider {

    private final ZoneManager manager;

    public ZoneArgumentType(ZoneManager manager) {
        this.manager = manager;
    }

    @Override
    public Zone parse(CommandContextBuilder contextBuilder, String string) throws CommandSyntaxException {
        Zone zone = this.manager.getZoneById(string);
        if (zone == null) throw CommandSyntaxException.custom(ZoneLang.ERROR_COMMAND_INVALID_ZONE_ARGUMENT);

        return zone;
    }

    @Override
    public List<String> suggest(ArgumentReader reader, CommandContext context) {
        return this.manager.getZoneIds();
    }
}
