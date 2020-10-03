package me.mederp.rpcore.plugin.commands;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
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
import me.mederp.rpcore.discord.DiscordModule;

import java.util.Optional;

public class MCmdLambdareport implements CommandExecutor
{
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException
    {
        Optional<String> argument = args.<String>getOne(Text.of("report"));
        if(!argument.isPresent())
        {
            src.sendMessage(Text.of(TextColors.RED + "Incorrect usage! Correct: /lambdareport <bug or error message, tell me what and how happened>!"));
            return CommandResult.empty();
        }

        String reportMessage = argument.get();

        DiscordModule module = RPCore.getInstance().getDiscordModule();
        long discordID;

        if(src instanceof Player)
        {
            discordID = RPCore.getInstance().getLambdaPlayers().get(((Player) src).getUniqueId()).getDiscordID();
        }
        else
        {
            discordID = 0;
        }

        boolean lambdaFound = false;
        for(Member member : module.getGuild().getMembers())
        {
            if(member.getIdLong() == 374554474712137728L)
            {
                User user = member.getUser();
                user.openPrivateChannel().queue((channel -> {
                    channel.sendMessage("Lambda Report from SCPCore version 1.0:\nFrom: " + src.getName() + "\n" +
                            "Discord ID: " + discordID + "\nMessage: " + reportMessage).queue();
                }));
                lambdaFound = true;
            }
        }

        if(!lambdaFound)
        {
            src.sendMessage(Text.of(TextColors.RED, "Lambda has not been found o synced Discord! Contact him on your own, Lambda#2594!"));
        }
        else
        {
            src.sendMessage(Text.of(TextColors.GREEN, "Lambda has received your report! Problem should be solved as soon as possible!"));
        }

        return CommandResult.success();
    }

    public static CommandSpec lambdareport = CommandSpec.builder()
            .executor(new MCmdLambdareport())
            .arguments(
                    GenericArguments.remainingJoinedStrings(Text.of("report"))
            )
            .build();
}
