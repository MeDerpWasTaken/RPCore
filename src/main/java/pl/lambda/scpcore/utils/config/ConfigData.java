package pl.lambda.scpcore.utils.config;

import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

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
        node.getNode("guildID").setValue(1234567890);
        node.getNode("syncChannelID").setValue(987654321L);

        node.getNode("level0role").setValue(1098765432L);
        node.getNode("level1role").setValue(2098765432L);
        node.getNode("level2role").setValue(3098765432L);
        node.getNode("level3role").setValue(4098765432L);
        node.getNode("level4role").setValue(5098765432L);
        node.getNode("level5role").setValue(6098765432L);
        node.getNode("mutedRole").setValue(48594739345L);

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
