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
import pl.lambda.scpcore.utils.enums.ChatType;
import pl.lambda.scpcore.utils.players.LambdaPlayer;

public class MCmdChattype implements CommandExecutor
{
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException
    {
        if(!(src instanceof Player)) return CommandResult.empty();

        LambdaPlayer lambdaPlayer = SCPCore.getInstance().getLambdaPlayers().get(((Player) src).getUniqueId());
        if(lambdaPlayer.getChatType() == ChatType.GLOBAL)
        {
            lambdaPlayer.setChatType(ChatType.LOCAL);
            src.sendMessage(Text.of(TextColors.GOLD, "Your current chat channel: ", TextColors.GREEN, "local ", TextColors.GOLD, "!"));
        }
        else
        {
            lambdaPlayer.setChatType(ChatType.GLOBAL);
            src.sendMessage(Text.of(TextColors.GOLD, "Your current chat channel: ", TextColors.DARK_PURPLE, "global ", TextColors.GOLD, "!"));
        }

        return CommandResult.empty();
    }

    public static CommandSpec chattype = CommandSpec.builder()
            .executor(new MCmdChattype())
            .build();
}
