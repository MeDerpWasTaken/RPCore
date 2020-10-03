package me.mederp.rpcore.plugin.commands;

import org.spongepowered.api.Sponge;
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
import me.mederp.rpcore.RPCore;
import me.mederp.rpcore.utils.players.LambdaPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

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

        Optional<String> argument = args.getOne(Text.of("nickname"));
        if(!argument.isPresent())
        {
            src.sendMessage(Text.of(TextColors.RED, "Incorrect usage! Correct: /realname <fake nickname>!"));
            return CommandResult.success();
        }

        String nickname = argument.get().replace('%', ' ');
        HashMap<UUID, LambdaPlayer> lambdaPlayers = RPCore.getInstance().getLambdaPlayers();
        for(Map.Entry<UUID, LambdaPlayer> loopedLambdaPlayer : lambdaPlayers.entrySet())
        {
            if(loopedLambdaPlayer.getValue().getNickname().equalsIgnoreCase(nickname))
            {
                String realName = Sponge.getServer().getPlayer(loopedLambdaPlayer.getKey()).get().getName();
                src.sendMessage(Text.of(TextColors.GREEN, "Real nickname of " + nickname + ": " + realName + "!"));
                return CommandResult.success();
            }
        }

        src.sendMessage(Text.of(TextColors.RED, "Player with this nickname isn't online (% = space)!"));
        return CommandResult.success();
    }

    public static CommandSpec realname = CommandSpec.builder()
            .executor(new MCmdRealname())
            .arguments(
                    GenericArguments.string(Text.of("nickname"))
            )
            .build();
}
