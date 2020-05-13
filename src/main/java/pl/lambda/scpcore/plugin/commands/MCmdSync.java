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
import pl.lambda.scpcore.SCPCore;
import pl.lambda.scpcore.utils.players.LambdaPlayer;
import pl.lambda.scpcore.utils.syncdata.SyncManager;

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

        SyncManager syncManager = SCPCore.getInstance().getSyncManager();
        String code = args.<String>getOne(Text.of("code")).get();

        if(!syncManager.verify(((Player) src).getUniqueId(), code))
        {
            src.sendMessage(Text.of(TextColors.RED, "Provided sync code is incorrect! Try again."));
            return CommandResult.empty();
        }

        long discordID = syncManager.getDiscordID(((Player) src).getUniqueId());
        LambdaPlayer lambdaPlayer = SCPCore.getInstance().getLambdaPlayers().get(((Player) src).getUniqueId());
        lambdaPlayer.setDiscordID(discordID);
        lambdaPlayer.savePlayer();

        syncManager.endSync(((Player) src).getUniqueId());

        src.sendMessage(Text.of(TextColors.GREEN, "Your Discord account has been synced successfully!"));

        return CommandResult.success();
    }

    public static CommandSpec sync = CommandSpec.builder()
            .executor(new MCmdSync())
            .arguments(
                    GenericArguments.optional(GenericArguments.string(Text.of("code")))
            )
            .build();
}
