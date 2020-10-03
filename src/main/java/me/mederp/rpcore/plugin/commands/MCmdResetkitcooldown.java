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

import java.util.Optional;

public class MCmdResetkitcooldown implements CommandExecutor
{
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException
    {
        if(!src.hasPermission("lambda.scpcore.resetkitcooldown"))
        {
            src.sendMessage(Text.of(TextColors.DARK_RED, "You don't have permission to use this command!"));
            return CommandResult.empty();
        }

        Optional<String> name = args.<String>getOne(Text.of("nickname"));
        if(name.isPresent())
        {
            Optional<Player> optionalPlayer = Sponge.getServer().getPlayer(name.get());
            if(optionalPlayer.isPresent())
            {
                RPCore.getInstance().getKitCooldown().remove(optionalPlayer.get().getUniqueId());
                src.sendMessage(Text.of(TextColors.GREEN, "Kit cooldown for ", optionalPlayer.get().getName(), " has been reseted."));
            }
            else
            {
                src.sendMessage(Text.of(TextColors.RED, "Player is offline! You can't reset kit cooldown for them!"));
            }
        }
        else
        {
            src.sendMessage(Text.of(TextColors.RED, "Incorrect usage! Correct: /resetkitcooldown <nickname>!"));
        }

        return CommandResult.success();
    }

    public static CommandSpec resetkitcooldown = CommandSpec.builder()
            .executor(new MCmdResetkitcooldown())
            .arguments(
                    GenericArguments.onlyOne(GenericArguments.string(Text.of("nickname")))
            )
            .build();
}
