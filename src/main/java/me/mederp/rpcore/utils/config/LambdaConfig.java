package me.mederp.rpcore.utils.config;

import ninja.leaping.configurate.ConfigurationNode;
import me.mederp.rpcore.RPCore;

public class LambdaConfig
{
    private String botToken;
    private String invitation;
    private long guildID;
    private long syncChannelID;

    private long mutedRole;

    private long kitCooldownInSeconds;

    public LambdaConfig loadConfig()
    {
        ConfigData configData = RPCore.getInstance().getConfigData();
        ConfigurationNode data = configData.getNode();

        this.botToken = data.getNode("botToken").getString();
        this.invitation = data.getNode("invitation").getString();
        this.guildID = data.getNode("guildID").getLong();
        this.syncChannelID = data.getNode("syncChannelID").getLong();

        this.mutedRole = data.getNode("mutedRole").getLong();

        this.kitCooldownInSeconds = data.getNode("kitCooldownInSeconds").getLong();

        return this;
    }

    public String getBotToken() {
        return botToken;
    }

    public String getInvitation() {
        return invitation;
    }

    public long getGuildID() {
        return guildID;
    }

    public long getSyncChannelID() {
        return syncChannelID;
    }

    public long getMutedRole() { return mutedRole; }

    public long getKitCooldownInSeconds() {
        return kitCooldownInSeconds;
    }
}
