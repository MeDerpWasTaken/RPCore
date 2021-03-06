package me.mederp.rpcore.plugin.listeners;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.message.MessageChannelEvent;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import me.mederp.rpcore.RPCore;
import me.mederp.rpcore.discord.DiscordModule;
import me.mederp.rpcore.utils.classes.LambdaClass;
import me.mederp.rpcore.utils.config.LambdaConfig;
import me.mederp.rpcore.utils.enums.ChatType;
import me.mederp.rpcore.utils.players.LambdaPlayer;

import java.util.HashMap;
import java.util.Map;

public class OnMessageChannelChat
{
    @Listener
    public void onMessageChannelChat(MessageChannelEvent.Chat e, @First Player p)
    {
        e.setCancelled(true);
        LambdaPlayer lambdaPlayer = RPCore.getInstance().getLambdaPlayers().get(p.getUniqueId());
        LambdaClass currentClass = lambdaPlayer.getChosenClass();

        Text message = formatGrammar(e.getRawMessage(), lambdaPlayer);

        if(currentClass == null)
        {
            sendMessageToAllPlayers(Text.of(TextColors.WHITE, "[NONE] ", TextColors.GRAY, p.getName(), ": ", TextColors.WHITE, message));
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
                LambdaPlayer loopedLambdaPlayer = RPCore.getInstance().getLambdaPlayers().get(loopedPlayer.getUniqueId());
                if(lambdaPlayer.getChatType() == ChatType.GLOBAL)
                {
                    if(loopedLambdaPlayer.getRealName())
                    {
                        loopedPlayer.sendMessage(Text.of(TextColors.DARK_PURPLE, "(Global) ", currentClass.getTextColor(), currentClass.getPrefix(), ' ', TextColors.GRAY, p.getName(), ": ", TextColors.WHITE, message));
                    }
                    else if(loopedLambdaPlayer.isSpyMode())
                    {
                        loopedPlayer.sendMessage(Text.of(TextColors.RED, "(Spy) ", TextColors.DARK_PURPLE, "(Global) ", currentClass.getTextColor(), currentClass.getPrefix(), ' ', TextColors.GRAY, p.getName(), ": ", TextColors.WHITE, message));
                    }
                    else
                    {
                        loopedPlayer.sendMessage(Text.of(TextColors.DARK_PURPLE, "(Global) ", currentClass.getTextColor(), currentClass.getPrefix(), ' ', TextColors.GRAY, lambdaPlayer.getNickname(), ": ", TextColors.WHITE, message));
                    }

                    RPCore.getInstance().getDiscordModule().getSyncChannel().sendMessage("**" + p.getName() + ":** " + e.getMessage()).queue();
                }
                else
                {
                    Text nickname;
                    if(loopedLambdaPlayer.getRealName()) nickname = Text.of(p.getName());
                    else nickname = Text.of(loopedLambdaPlayer.getNickname());

                    if(loopedPlayer.getPosition().distanceSquared(p.getPosition()) < 625D)
                    {
                        loopedPlayer.sendMessage(Text.of(TextColors.GREEN, "(Local) ", currentClass.getTextColor(), currentClass.getPrefix(), ' ', TextColors.GRAY, nickname, ": ", TextColors.WHITE, message));
                    }
                    else
                    {
                        if(loopedLambdaPlayer.getRealName())
                        {
                            loopedPlayer.sendMessage(Text.of(currentClass.getTextColor(), currentClass.getPrefix(), ' ', TextColors.GRAY, p.getName(), ": ", TextColors.WHITE, message));
                        }
                        else if(loopedLambdaPlayer.isSpyMode())
                        {
                            loopedPlayer.sendMessage(Text.of(TextColors.RED, "(Spy) ", currentClass.getTextColor(), currentClass.getPrefix(), ' ', TextColors.GRAY, nickname, ": ", TextColors.WHITE, message));
                        }
                    }
                }
            }
        }
    }

    private Text formatGrammar(Text text, LambdaPlayer lambdaPlayer)
    {
        if(!lambdaPlayer.isGrammarMode()) return text;

        HashMap<String, String> errorsAndFixes = new HashMap<>();
        errorsAndFixes.put("doesnt", "doesn't");
        errorsAndFixes.put("cant", "can't");
        errorsAndFixes.put("wont", "won't");
        errorsAndFixes.put("dont", "don't");
        errorsAndFixes.put("Ive", "I've");
        errorsAndFixes.put("Id", "I'd");
        errorsAndFixes.put("Im", "I'm");
        errorsAndFixes.put("Ill", "I'll");
        errorsAndFixes.put("shes", "she's");
        errorsAndFixes.put("hes", "he's");
        errorsAndFixes.put("its", "it's");
        errorsAndFixes.put("theres", "there's");
        errorsAndFixes.put("theyre", "they're");
        errorsAndFixes.put("youve", "you've");
        errorsAndFixes.put("couldnt", "couldn't");
        errorsAndFixes.put("shouldnt", "shouldn't");
        errorsAndFixes.put("wouldnt", "wouldn't");

        String rawString = text.toPlain();
        String[] spitedString = rawString.split(" ");
        char firstChar = rawString.charAt(0);
        if(!Character.isUpperCase(firstChar))
        {
            String withoutFirstLetter = rawString.substring(1);
            rawString = Character.toUpperCase(firstChar) + withoutFirstLetter;
        }

        for(String part : spitedString)
        {
            for(Map.Entry<String, String> grammarError : errorsAndFixes.entrySet())
            {
                if(grammarError.getKey().equalsIgnoreCase(part))
                {
                    part = grammarError.getValue();
                }
            }
        }

        rawString = String.join(" ", rawString);

        char lastChar = rawString.charAt(rawString.length() - 1);
        if(!(lastChar == '.' || lastChar == '?' || lastChar == '!' || lastChar == ':'))
        {
            lastChar = '.';
            rawString = rawString + lastChar;
        }

        return Text.of(rawString);
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
        LambdaConfig lambdaConfig = RPCore.getInstance().getLambdaConfig();
        DiscordModule discordModule = RPCore.getInstance().getDiscordModule();
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
