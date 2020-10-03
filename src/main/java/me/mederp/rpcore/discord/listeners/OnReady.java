package me.mederp.rpcore.discord.listeners;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.spongepowered.api.Sponge;
import me.mederp.rpcore.RPCore;
import me.mederp.rpcore.discord.DiscordModule;
import me.mederp.rpcore.utils.config.LambdaConfig;
import me.mederp.rpcore.utils.exceptions.GuildIsNullException;
import me.mederp.rpcore.utils.exceptions.SyncChannelIsNullException;

public class OnReady extends ListenerAdapter
{
    private DiscordModule module;

    public OnReady(DiscordModule module)
    {
        this.module = module;
    }

    public void onReady(ReadyEvent e)
    {
        LambdaConfig lambdaConfig = RPCore.getInstance().getLambdaConfig();

        try
        {
            Guild guild = module.getJDA().getGuildById(lambdaConfig.getGuildID());

            if(guild == null)
            {
                throw new GuildIsNullException();
            }

            TextChannel textChannel = module.getJDA().getTextChannelById(lambdaConfig.getSyncChannelID());

            if(textChannel == null)
            {
                throw new SyncChannelIsNullException();
            }

            module.setGuild(guild);
            module.setSyncChannel(textChannel);

            textChannel.sendMessage("**Server is on!** What are you waiting for? Start Technic and join!").queue();
        }
        catch (NullPointerException | GuildIsNullException | SyncChannelIsNullException ex)
        {
            ex.printStackTrace();
            System.err.println("COULDN'T GET GUILD ID/SYNC CHANNEL ID. DISABLING SERVER...");
            Sponge.getServer().shutdown();
        }
    }
}
