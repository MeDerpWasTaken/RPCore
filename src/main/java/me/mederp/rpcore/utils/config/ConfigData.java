package me.mederp.rpcore.utils.config;

import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;

import java.io.File;
import java.io.IOException;

public class ConfigData
{
    public static ConfigData instance;

    private File dir, file;
    private String fileName = "config.conf";
    private ConfigurationLoader<CommentedConfigurationNode> loader;
    private CommentedConfigurationNode node;

    public void setup()
    {
        dir = new File("config/scpCore/");
        file = new File("config/scpCore/" + fileName);
        if (!dir.exists())
        {
            dir.mkdirs();
        }

        boolean fileExisted = true;

        if (!file.exists())
        {
            try
            {
                fileExisted = false;
                file.createNewFile();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        this.loader = HoconConfigurationLoader.builder().setFile(file).build();

        try
        {
            this.node = this.loader.load();
        }
        catch (IOException e)
        {
            this.node = this.loader.createEmptyNode();
            e.printStackTrace();
        }

        instance = this;

        if(!fileExisted)
        {
            loadDefaultConfig();
        }
    }

    public void reload()
    {
        try
        {
            node = loader.load();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void save()
    {
        try
        {
            this.loader.save(this.node);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void loadDefaultConfig()
    {
        node.getNode("botToken").setValue("example-bot-token");
        node.getNode("invitation").setValue("https://discord.gg/example");
        node.getNode("guildID").setValue(0000000000L);
        node.getNode("syncChannelID").setValue(0000000000L);

        node.getNode("level0role").setValue(0000000000L);
        node.getNode("level1role").setValue(0000000000L);
        node.getNode("level2role").setValue(0000000000L);
        node.getNode("level3role").setValue(0000000000L);
        node.getNode("level4role").setValue(0000000000L);
        node.getNode("level5role").setValue(0000000000L);
        node.getNode("mutedRole").setValue(0000000000L);

        node.getNode("kitCooldownInSeconds").setValue(21600);
        save();
    }

    public static ConfigData getInstance()
    {
        return instance;
    }

    public CommentedConfigurationNode getNode() {
        return node;
    }
}
