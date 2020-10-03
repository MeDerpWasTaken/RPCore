package me.mederp.rpcore.plugin.commands;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import me.mederp.rpcore.RPCore;

public class MCmdNick implements CommandExecutor
{
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException
    {
        if(!(src instanceof Player)) return CommandResult.empty();

        if(!src.hasPermission("lambda.scpcore.nick"))
        {
            src.sendMessage(Text.of(TextColors.DARK_RED, "You don't have permission to use that command!"));
            return CommandResult.empty();
        }

        String newNickname = args.<String>getOne(Text.of("nickname")).get();
        if(newNickname.length() > 16)
        {
            src.sendMessage(Text.of(TextColors.RED, "Nickname cannot be longer than 16 characters!"));
            return CommandResult.empty();
        }

        newNickname = newNickname.replace('%', ' ');
        RPCore.getInstance().getLambdaPlayers().get(((Player) src).getUniqueId()).setNickname(newNickname);
        ((Player) src).offer(Keys.DISPLAY_NAME, Text.of(newNickname));
        src.sendMessage(Text.of(TextColors.GREEN, "Your nickname has been changed successfully to: " + newNickname + "!"));

        return CommandResult.success();
    }

    public static CommandSpec nick = CommandSpec.builder()
            .executor(new MCmdNick())
            .arguments(
                    GenericArguments.onlyOne(GenericArguments.string(Text.of("nickname")))
            ).build();
}
