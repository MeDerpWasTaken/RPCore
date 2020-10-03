package me.mederp.rpcore.plugin.commands;

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
import me.mederp.rpcore.utils.classes.LambdaClass;

import java.util.Optional;

public class MCmdClearclasskit implements CommandExecutor
{
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException
    {
        if(!(src instanceof Player))
        {
            src.sendMessage(Text.of("This command is only for players!"));
            return CommandResult.empty();
        }

        if(!src.hasPermission("lambda.scpcore.clearclasskit"))
        {
            src.sendMessage(Text.of(TextColors.DARK_RED, "You don't have permission to use that command!"));
            return CommandResult.empty();
        }

        Optional<String> argument = args.getOne(Text.of("className"));
        if(!argument.isPresent())
        {
            src.sendMessage(Text.of(TextColors.RED, "Incorrect usage! Correct: /clearclasskit <class name>!"));
            return CommandResult.empty();
        }

        String className = argument.get();

        LambdaClass lambdaClass = LambdaClass.getLambdaClassByName(className);
        if(lambdaClass == null)
        {
            src.sendMessage(Text.of(TextColors.RED, "Class with that name not exist!"));
            return CommandResult.empty();
        }

        lambdaClass.getClassKit().clear();
        lambdaClass.saveClass();

        src.sendMessage(Text.of(TextColors.GREEN, "Class with name: " + lambdaClass.getName() + " no-longer has assigned kit!"));

        return CommandResult.success();
    }

    public static CommandSpec clearclasskit = CommandSpec.builder()
            .executor(new MCmdClearclasskit())
            .arguments(
                    GenericArguments.remainingJoinedStrings(Text.of("className"))
            ).build();
}
