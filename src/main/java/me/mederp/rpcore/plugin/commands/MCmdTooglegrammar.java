package me.mederp.rpcore.plugin.commands;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import me.mederp.rpcore.RPCore;
import me.mederp.rpcore.utils.players.LambdaPlayer;

public class MCmdTooglegrammar implements CommandExecutor
{
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException
    {
        if(!(src instanceof Player)) return CommandResult.empty();

        if(!src.hasPermission("lambda.scpcore.togglegrammar"))
        {
            src.sendMessage(Text.of(TextColors.RED, "You are not allowed to use that command!"));
            return CommandResult.empty();
        }

        LambdaPlayer lambdaPlayer = RPCore.getInstance().getLambdaPlayers().get(((Player) src).getUniqueId());
        if(lambdaPlayer.isGrammarMode())
        {
            lambdaPlayer.setGrammarMode(false);
            src.sendMessage(Text.of(TextColors.RED, "You disabled grammar mode!"));
        }
        else
        {
            lambdaPlayer.setGrammarMode(true);
            src.sendMessage(Text.of(TextColors.GREEN, "You enabled grammar mode!"));
        }

        return CommandResult.success();
    }

    public static CommandSpec tooglegrammar = CommandSpec.builder()
            .executor(new MCmdTooglegrammar()).build();
}
