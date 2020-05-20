package pl.lambda.scpcore.plugin.commands;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import pl.lambda.scpcore.SCPCore;
import pl.lambda.scpcore.utils.players.LambdaPlayer;

public class MCmdRealname implements CommandExecutor
{
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException
    {
        if(!(src instanceof Player)) return CommandResult.empty();

        if(!src.hasPermission("lambda.scpcore.realname"))
        {
            src.sendMessage(Text.of(TextColors.RED, "You are not allowed to use that command!"));
            return CommandResult.empty();
        }

        LambdaPlayer lambdaPlayer = SCPCore.getInstance().getLambdaPlayers().get(((Player) src).getUniqueId());
        if(lambdaPlayer.getRealName())
        {
            lambdaPlayer.setRealName(false);
            src.sendMessage(Text.of(TextColors.RED, "You disabled real name mode!"));
        }
        else
        {
            lambdaPlayer.setRealName(true);
            src.sendMessage(Text.of(TextColors.GREEN, "You enabled real name mode!"));
        }

        return CommandResult.success();
    }

    public static CommandSpec realname = CommandSpec.builder()
            .executor(new MCmdRealname()).build();
}
