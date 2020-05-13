package pl.lambda.scpcore.plugin.listeners;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.message.MessageChannelEvent;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColor;
import org.spongepowered.api.text.format.TextColors;
import pl.lambda.scpcore.SCPCore;
import pl.lambda.scpcore.discord.DiscordModule;
import pl.lambda.scpcore.utils.classes.LambdaClass;
import pl.lambda.scpcore.utils.config.LambdaConfig;
import pl.lambda.scpcore.utils.discordutils.DiscordRequest;
import pl.lambda.scpcore.utils.enums.ChatType;
import pl.lambda.scpcore.utils.players.LambdaPlayer;

public class OnMessageChannelChat
{
    @Listener
    public void onMessageChannelChat(MessageChannelEvent.Chat e, @First Player p)
    {
        e.setCancelled(true);
        LambdaPlayer lambdaPlayer = SCPCore.getInstance().getLambdaPlayers().get(p.getUniqueId());
        LambdaClass currentClass = lambdaPlayer.getChosenClass();
        if(currentClass == null)
        {
            sendMessageToAllPlayers(Text.of(TextColors.WHITE, "[NONE] ", TextColors.GRAY, p.getName(), ": ", TextColors.WHITE, e.getRawMessage()));
        }
        else
        {
            if(isMuted(lambdaPlayer.getDiscordID()))
            {
                p.sendMessage(Text.of(TextColors.RED, "You are muted (Discord mutes are synchronized with Minecraft)!"));
                return;
            }

            for(Player loopedPlayer : Sponge.getServer().getOnlinePlayers())
            {
                LambdaPlayer loopedLambdaPlayer = SCPCore.getInstance().getLambdaPlayers().get(loopedPlayer.getUniqueId());
                if(lambdaPlayer.getChatType() == ChatType.GLOBAL)
                {
                    if(loopedLambdaPlayer.getRealName())
                    {
                        loopedPlayer.sendMessage(Text.of(TextColors.DARK_PURPLE, "(Global) ", currentClass.getTextColor(), currentClass.getPrefix(), TextColors.GRAY, p.getName(), ": ", TextColors.WHITE, e.getRawMessage()));
                    }
                    else
                    {
                        loopedPlayer.sendMessage(Text.of(TextColors.DARK_PURPLE, "(Global) ", currentClass.getTextColor(), currentClass.getPrefix(), TextColors.GRAY, lambdaPlayer.getNickname(), ": ", TextColors.WHITE, e.getRawMessage()));
                    }
                }
                else
                {
                    Text nickname;
                    if(loopedLambdaPlayer.getRealName()) nickname = Text.of(p.getName());
                    else nickname = Text.of(loopedLambdaPlayer.getNickname());

                    if(loopedPlayer.getPosition().distanceSquared(p.getPosition()) < 625D)
                    {
                        loopedPlayer.sendMessage(Text.of(TextColors.GREEN, "(Local) ", currentClass.getTextColor(), currentClass.getPrefix(), TextColors.GRAY, nickname, ": ", TextColors.WHITE, e.getRawMessage()));
                    }
                    else
                    {
                        if(loopedLambdaPlayer.isSpyMode())
                        {
                            loopedPlayer.sendMessage(Text.of(TextColors.RED, "(Spy) ", currentClass.getTextColor(), currentClass.getPrefix(), TextColors.GRAY, nickname, ": ", TextColors.WHITE, e.getRawMessage()));
                        }
                    }
                }
            }
        }
    }

    private void sendMessageToAllPlayers(Text message)
    {
        for(Player player : Sponge.getServer().getOnlinePlayers())
        {
            player.sendMessage(message);
        }
    }

    private boolean isMuted(long discordID)
    {
        LambdaConfig lambdaConfig = SCPCore.getInstance().getLambdaConfig();
        DiscordModule discordModule = SCPCore.getInstance().getDiscordModule();
        Guild guild = discordModule.getGuild();

        Member member = guild.getMemberById(discordID);
        if(member == null)
        {
            return false;
        }

        Role mutedRole = guild.getRoleById(lambdaConfig.getMutedRole());
        if(mutedRole == null) return false;

        return member.getRoles().contains(mutedRole);
    }
}
