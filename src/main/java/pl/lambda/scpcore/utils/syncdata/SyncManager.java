package pl.lambda.scpcore.utils.syncdata;

import pl.lambda.scpcore.SCPCore;

import java.util.UUID;

public class SyncManager
{
    public void startSync(UUID uuid, long discordID, String code)
    {
        SyncDataStorage syncDataStorage = SCPCore.getInstance().getSyncDataStorage();
        syncDataStorage.getNode().getNode("sync", uuid.toString(), "discordID").setValue(discordID);
        syncDataStorage.getNode().getNode("sync", uuid.toString(), "code").setValue(code);
        syncDataStorage.save();
    }

    public boolean verify(UUID uuid, String input)
    {
        SyncDataStorage syncDataStorage = SCPCore.getInstance().getSyncDataStorage();
        String code = syncDataStorage.getNode().getNode("sync", uuid.toString(), "code").getString();
        return input.equals(code);
    }

    public long getDiscordID(UUID uuid)
    {
        SyncDataStorage syncDataStorage = SCPCore.getInstance().getSyncDataStorage();
        return syncDataStorage.getNode().getNode("sync", uuid.toString(), "discordID").getLong();
    }

    public void endSync(UUID uuid)
    {
        SyncDataStorage syncDataStorage = SCPCore.getInstance().getSyncDataStorage();
        syncDataStorage.getNode().getNode("sync", uuid.toString()).setValue(null);
        syncDataStorage.save();
    }

    public void endSync()
    {
        SyncDataStorage syncDataStorage = SCPCore.getInstance().getSyncDataStorage();
        syncDataStorage.getNode().getNode("sync").setValue(null);
        syncDataStorage.save();
    }
}
