package pl.lambda.scpcore.utils.config;

import ninja.leaping.configurate.ConfigurationNode;
import pl.lambda.scpcore.SCPCore;

public class LambdaConfig
{
    private String botToken;
    private String invitation;
    private long guildID;
    private long syncChannelID;

    private long level0role;
    private long level1role;
    private long level2role;
    private long level3role;
    private long level4role;
    private long level5role;
    private long mutedRole;

    private long kitCooldownInSeconds;

    public LambdaConfig loadConfig()
    {
        ConfigData configData = SCPCore.getInstance().getConfigData();
        ConfigurationNode data = configData.getNode();

        this.botToken = data.getNode("botToken").getString();
        this.invitation = data.getNode("invitation").getString();
        this.guildID = data.getNode("guildID").getLong();
        this.syncChannelID = data.getNode("syncChannelID").getLong();

        this.level0role = data.getNode("level0role").getLong();
        this.level1role = data.getNode("level1role").getLong();
        this.level2role = data.getNode("level2role").getLong();
        this.level3role = data.getNode("level3role").getLong();
        this.level4role = data.getNode("level4role").getLong();
        this.level5role = data.getNode("level5role").getLong();
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

    public long getLevel0role() {
        return level0role;
    }

    public long getLevel1role() {
        return level1role;
    }

    public long getLevel2role() {
        return level2role;
    }

    public long getLevel3role() {
        return level3role;
    }

    public long getLevel4role() {
        return level4role;
    }

    public long getLevel5role() {
        return level5role;
    }

    public long getMutedRole() { return mutedRole; }

    public long getKitCooldownInSeconds() {
        return kitCooldownInSeconds;
    }
}
