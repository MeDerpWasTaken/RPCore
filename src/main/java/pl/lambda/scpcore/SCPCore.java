package pl.lambda.scpcore;

import com.google.inject.Inject;
import org.apache.logging.log4j.core.Appender;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.game.state.*;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import pl.lambda.scpcore.discord.DiscordModule;
import pl.lambda.scpcore.plugin.commands.*;
import pl.lambda.scpcore.plugin.listeners.*;
import pl.lambda.scpcore.utils.classes.ClassDataStorage;
import pl.lambda.scpcore.utils.classes.LambdaClass;
import pl.lambda.scpcore.utils.config.ConfigData;
import pl.lambda.scpcore.utils.config.LambdaConfig;
import pl.lambda.scpcore.utils.permissions.PermissionsManager;
import pl.lambda.scpcore.utils.players.LambdaPlayer;
import pl.lambda.scpcore.utils.players.PlayerDataStorage;
import pl.lambda.scpcore.utils.syncdata.SyncDataStorage;
import pl.lambda.scpcore.utils.syncdata.SyncManager;

import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

@Plugin(
        id = "scpcore",
        name = "SCPCore",
        version = "1.0",
        description = "Plugin to Minecraft Sponge engine originally designed for Site-21 server. That adds some SCP Foundation stuff to game - departments, clearance levels, etc. Discord synchronization required.",
        authors = {
                "Lambda"
        }
)
public class SCPCore
{
    private static SCPCore instance;

    private ConfigData configData;
    private LambdaConfig lambdaConfig;
    private PlayerDataStorage playerDataStorage;
    private SyncDataStorage syncDataStorage;
    private SyncManager syncManager;
    private ClassDataStorage classDataStorage;
    private DiscordModule discordModule;
    private PermissionsManager permissionsManager;

    private HashMap<UUID, Date> kitCooldown = new HashMap<>();
    private HashMap<UUID, LambdaPlayer> lambdaPlayers = new HashMap<>();
    private HashMap<String, LambdaClass> lambdaClasses = new HashMap<>();

    @Inject
    private Logger logger;

    @Listener
    public void onPreGameInit(GamePreInitializationEvent event)
    {
        //configs & instance
        instance = this;
        hookConsoleLogs();

        lambdaConfig = new LambdaConfig();
        syncDataStorage = new SyncDataStorage();
        classDataStorage = new ClassDataStorage();
        configData = new ConfigData();
        playerDataStorage = new PlayerDataStorage();
        syncManager = new SyncManager();
        permissionsManager = new PermissionsManager();

        configData.setup();
        lambdaConfig.loadConfig();
        playerDataStorage.setup();
        syncDataStorage.setup();
        classDataStorage.setup();
    }

    @Listener
    public void onGameInit(GameInitializationEvent event)
    {
        //listeners
        Sponge.getEventManager().registerListeners(this, new OnClientConnectionDisconnect());
        Sponge.getEventManager().registerListeners(this, new OnClientConnectionJoin());
        Sponge.getEventManager().registerListeners(this, new OnMessageChannelChat());
        Sponge.getEventManager().registerListeners(this, new OnClickInventory());
        Sponge.getEventManager().registerListeners(this, new OnEntityDeath());
        Sponge.getEventManager().registerListeners(this, new OnInteractBlock());

        //commands
        Sponge.getCommandManager().register(this, MCmdChattype.chattype, "chattype", "switchchannel", "changechattype");
        Sponge.getCommandManager().register(this, MCmdNick.nick, "nick", "setnick", "changenick");
        Sponge.getCommandManager().register(this, MCmdSpy.spy, "spy", "spymode", "meetthespy");
        Sponge.getCommandManager().register(this, MCmdClass.classCmd, "class", "depts", "chooserole");
        Sponge.getCommandManager().register(this, MCmdSync.sync, "sync", "verify", "synchronize");
        Sponge.getCommandManager().register(this, MCmdResetclasslocation.resetclasslocation, "resetclasslocation", "deleteclasslocation", "clearclasslocation");
        Sponge.getCommandManager().register(this, MCmdSetupclasslocation.setupclasslocation, "setupclasslocation", "setclasslocation", "changeclasslocation");
        Sponge.getCommandManager().register(this, MCmdRealname.realname, "realname", "shownicknames", "truenickname");
        Sponge.getCommandManager().register(this, MCmdLambdareport.lambdareport, "lambdareport", "issuereport", "errorreport");
        Sponge.getCommandManager().register(this, MCmdClearclasskit.clearclasskit, "clearclasskit", "deleteclasskit", "classkitremove");
        Sponge.getCommandManager().register(this, MCmdSetkititems.setkititems, "setkititems", "additemtokit", "pushitemstokit");
        Sponge.getCommandManager().register(this, MCmdKit.kit, "kit", "takekit", "receivekit");
        Sponge.getCommandManager().register(this, MCmdResetkitcooldown.resetkitcooldown, "resetkitcooldown", "clearkitcooldown", "deletekitcooldown");
        Sponge.getCommandManager().register(this, MCmdTooglegrammar.tooglegrammar, "togglegrammar", "autogrammar", "toggleautogrammar");
        Sponge.getCommandManager().register(this, MCmdKeycard.keycard, "keycard", "receivekeycard", "kcard");
    }

    @Listener
    public void onServerStart(GameStartedServerEvent event)
    {
        //register LambdaClasses
        LambdaClass.loadClasses();

        //discord bot
        discordModule = new DiscordModule();
        discordModule.start();
    }

    @Listener
    public void onServerStop(GameStoppingServerEvent event)
    {
        for(Player p : Sponge.getServer().getOnlinePlayers())
        {
            SCPCore.getInstance().getDiscordModule().getSyncChannel().sendMessage("**Player left!** " + p.getName() + " has left the game.").queue();
        }

        discordModule.getSyncChannel().sendMessage("**Server off!** Server has been turned off. Let's hope that it will be restated soon!").queue();
        syncManager.endSync();
        lambdaPlayers.clear();
        lambdaClasses.clear();

        discordModule.getJDA().shutdown();
    }

    private void hookConsoleLogs()
    {

    }

    public static SCPCore getInstance()
    {
        return instance;
    }

    public Logger getLogger()
    {
        return logger;
    }

    public PlayerDataStorage getPlayerDataStorage() {
        return playerDataStorage;
    }

    public SyncDataStorage getSyncDataStorage() {
        return syncDataStorage;
    }

    public ClassDataStorage getClassDataStorage() {
        return classDataStorage;
    }

    public ConfigData getConfigData() {
        return configData;
    }

    public HashMap<UUID, LambdaPlayer> getLambdaPlayers() {
        return lambdaPlayers;
    }

    public HashMap<String, LambdaClass> getLambdaClasses() {
        return lambdaClasses;
    }

    public LambdaConfig getLambdaConfig() {
        return lambdaConfig;
    }

    public SyncManager getSyncManager() {
        return syncManager;
    }

    public PermissionsManager getPermissionsManager() {
        return permissionsManager;
    }

    public DiscordModule getDiscordModule() {
        return discordModule;
    }

    public HashMap<UUID, Date> getKitCooldown() {
        return kitCooldown;
    }
}
