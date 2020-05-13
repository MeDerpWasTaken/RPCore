package pl.lambda.scpcore.plugin.commands;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import pl.lambda.scpcore.utils.classes.LambdaClass;

public class MCmdResetclasslocation implements CommandExecutor
{
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException
    {
        if(!(src instanceof Player)) return CommandResult.empty();

        if(!src.hasPermission("lambda.scpcore.resetspawnlocation"))
        {
            src.sendMessage(Text.of(TextColors.DARK_RED, "You don't have permission to use that command!"));
            return CommandResult.empty();
        }

        if(!args.getOne(Text.of("name")).isPresent())
        {
            src.sendMessage(Text.of(TextColors.RED, "Wrong usage! Correct: /resetclasslocation <name...>."));
            return CommandResult.empty();
        }

        String name = args.<String>getOne(Text.of("name")).get();

        LambdaClass lambdaClass = LambdaClass.getLambdaClassByName(name);
        if(lambdaClass == null)
        {
            src.sendMessage(Text.of(TextColors.RED, "Class with provided name not exist!"));
            return CommandResult.empty();
        }

        Location<World> emptyLocation = new Location<>(((Player) src).getWorld(), 0, 0, 0);
        lambdaClass.setSpawnLocation(emptyLocation);

        src.sendMessage(Text.of(TextColors.GREEN, name + "'s location has been cleared successfully!"));

        return CommandResult.success();
    }

    public static CommandSpec resetclasslocation = CommandSpec.builder()
            .executor(new MCmdResetclasslocation())
            .arguments(
                    GenericArguments.optional(GenericArguments.string(Text.of("name")))
            )
            .build();
}
