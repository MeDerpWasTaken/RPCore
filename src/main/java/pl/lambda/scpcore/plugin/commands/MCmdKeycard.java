package pl.lambda.scpcore.plugin.commands;

import org.spongepowered.api.Sponge;
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

public class MCmdKeycard implements CommandExecutor
{
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException
    {
        if(!(src instanceof Player)) return CommandResult.empty();

        int clearance = SCPCore.getInstance().getLambdaPlayers().get(((Player) src).getUniqueId()).getClearance();
        if(clearance == -1 || clearance == 0)
        {
            src.sendMessage(Text.of(TextColors.RED, "You are not allowed to receive keycard!"));
            return CommandResult.success();
        }

        Sponge.getCommandManager().process(Sponge.getServer().getConsole(), "give " + src.getName() + " scp:level " + clearance + "card");
        src.sendMessage(Text.of(TextColors.GREEN, "You received your command successfully!"));

        return CommandResult.success();
    }

    public static CommandSpec keycard = CommandSpec.builder()
            .executor(new MCmdKeycard()).build();
}
