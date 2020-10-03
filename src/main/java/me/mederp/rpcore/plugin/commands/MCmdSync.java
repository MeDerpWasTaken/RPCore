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
import me.mederp.rpcore.RPCore;
import me.mederp.rpcore.utils.players.LambdaPlayer;
import me.mederp.rpcore.utils.syncdata.SyncManager;

public class MCmdSync implements CommandExecutor
{
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException
    {
        if(!(src instanceof Player)) return CommandResult.empty();

        if(!args.getOne(Text.of("code")).isPresent())
        {
            src.sendMessage(Text.of(TextColors.RED, "Incorrect usage! Correct: /sync <sync code>!"));
            return CommandResult.empty();
        }

        SyncManager syncManager = RPCore.getInstance().getSyncManager();
        String code = args.<String>getOne(Text.of("code")).get();

        if(!syncManager.verify(((Player) src).getUniqueId(), code))
        {
            src.sendMessage(Text.of(TextColors.RED, "Provided sync code is incorrect! Try again."));
            return CommandResult.empty();
        }

        long discordID = syncManager.getDiscordID(((Player) src).getUniqueId());
        LambdaPlayer lambdaPlayer = RPCore.getInstance().getLambdaPlayers().get(((Player) src).getUniqueId());
        lambdaPlayer.setDiscordID(discordID);
        lambdaPlayer.savePlayer();

        syncManager.endSync(((Player) src).getUniqueId());

        ((Player) src).kick(Text.of(TextColors.GREEN, "Account synced successfully! Rejoin server and enjoy!"));

        return CommandResult.success();
    }

    public static CommandSpec sync = CommandSpec.builder()
            .executor(new MCmdSync())
            .arguments(
                    GenericArguments.optional(GenericArguments.string(Text.of("code")))
            )
            .build();
}
