package me.mederp.rpcore.utils.syncdata;

import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;

import java.io.File;
import java.io.IOException;

public class SyncDataStorage
{
    public static SyncDataStorage instance;

    private File dir, file;
    private String fileName = "syncData.conf";
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

        if (!file.exists())
        {
            try
            {
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

    public void save()
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

    public static SyncDataStorage getInstance()
    {
        return instance;
    }

    public CommentedConfigurationNode getNode() {
        return node;
    }
}
