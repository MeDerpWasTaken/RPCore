package me.mederp.rpcore.discord.listeners;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import me.mederp.rpcore.discord.DiscordModule;

public class OnMessageReceived extends ListenerAdapter
{
    private DiscordModule module;

    public OnMessageReceived(DiscordModule module)
    {
        this.module = module;
    }

    public void onMessageReceived(MessageReceivedEvent e)
    {
        if(e.getTextChannel().getIdLong() == module.getSyncChannel().getIdLong())
        {
            if(e.getAuthor().isBot()) return;
            for(Player p : Sponge.getServer().getOnlinePlayers())
            {
                p.sendMessage(Text.of(TextColors.BLUE, "[Discord] ", TextColors.GRAY, e.getAuthor().getName(), ": ", TextColors.WHITE, e.getMessage().getContentRaw()));
            }
        }
    }
}
