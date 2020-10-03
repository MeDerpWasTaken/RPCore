package me.mederp.rpcore.discord;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.spongepowered.api.Sponge;
import me.mederp.rpcore.RPCore;
import me.mederp.rpcore.discord.commands.*;
import me.mederp.rpcore.discord.listeners.OnMessageReceived;
import me.mederp.rpcore.discord.listeners.OnReady;
import me.mederp.rpcore.utils.config.LambdaConfig;

import javax.security.auth.login.LoginException;

public class DiscordModule
{
    private JDA jda;
    private Guild guild;
    private TextChannel syncChannel;

    public void start()
    {
        LambdaConfig config = RPCore.getInstance().getLambdaConfig();
        if(this.jda != null) return;

        try
        {
            this.jda = JDABuilder.create(config.getBotToken(), GatewayIntent.getIntents(GatewayIntent.ALL_INTENTS))
                    .addEventListeners(new OnReady(this), new OnMessageReceived(this))
                    .addEventListeners(
                            new DCmdSetupclass(this), new DCmdClasslist(this),
                            new DCmdVerify(this), new DCmdDeleteclass(this),
                            new DCmdAddclasspermission(), new DCmdRemoveclasspermission()
                    )
                    .build();
        }
        catch (LoginException | NullPointerException e)
        {
            e.printStackTrace();
            System.err.println("COULDN'T LOGIN TO DISCORD BOT. DISABLING SERVER...");
            Sponge.getServer().shutdown();
        }
    }

    public JDA getJDA() {
        return jda;
    }

    public Guild getGuild() {
        return guild;
    }

    public TextChannel getSyncChannel() {
        return syncChannel;
    }

    public void setGuild(Guild guild) {
        this.guild = guild;
    }

    public void setSyncChannel(TextChannel syncChannel) {
        this.syncChannel = syncChannel;
    }
}
