package me.mederp.rpcore.utils.classes;

import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.type.DyeColor;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.text.format.TextColor;
import org.spongepowered.api.util.TypeTokens;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import me.mederp.rpcore.RPCore;
import me.mederp.rpcore.utils.Utils;

import java.util.*;

public class LambdaClass
{
    private String classID;
    private String name;
    private String prefix;
    private long discordID;
    private boolean itemsDisappear;
    private TextColor textColor;
    private DyeColor dyeColor;
    private Location<World> spawnLocation;
    private List<String> permissions;
    private List<ItemStack> classKit;

    public LambdaClass(String classID, String name, String prefix, long discordID, boolean itemsDisappear, TextColor textColor, DyeColor dyeColor, Location<World> spawnLocation, List<String> permissions) {
        this.classID = classID;
        this.name = name;
        this.prefix = prefix;
        this.discordID = discordID;
        this.itemsDisappear = itemsDisappear;
        this.textColor = textColor;
        this.dyeColor = dyeColor;
        this.spawnLocation = spawnLocation;
        this.permissions = permissions;
        this.classKit = loadKitItems();
    }

    public String getClassID() {
        return classID;
    }

    public String getName() {
        return name;
    }

    public String getPrefix() {
        return prefix;
    }

    public long getDiscordID() {
        return discordID;
    }

    public TextColor getTextColor() {
        return textColor;
    }

    public DyeColor getDyeColor() {
        return dyeColor;
    }

    public Location<World> getSpawnLocation()
    {
        if(spawnLocation.getBlockX() == 0 && spawnLocation.getBlockY() == 0 && spawnLocation.getBlockZ() == 0)
        {
            return null;
        }
        return spawnLocation;
    }

    public boolean itemsDisappear() {
        return itemsDisappear;
    }

    public void setSpawnLocation(Location<World> spawnLocation) {
        this.spawnLocation = spawnLocation;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }

    public List<ItemStack> getClassKit() {
        return classKit;
    }

    public void setItemsDisappear(boolean itemsDisappear) {
        this.itemsDisappear = itemsDisappear;
    }

    public static LambdaClass createLambdaClass(String classID, String name, String prefix, long discordID, TextColor textColor, DyeColor dyeColor)
    {
        String worldName = Sponge.getServer().getDefaultWorldName();
        World world;

        if(Sponge.getServer().getWorld(worldName).isPresent())
        {
            world = Sponge.getServer().getWorld(worldName).get();
        }
        else
        {
            world = (World) Sponge.getServer().getWorlds().toArray()[0];
        }

        Location<World> spawnLocation = new Location<>(world, 0, 0, 0);

        return new LambdaClass(classID, name, prefix, discordID,false, textColor, dyeColor, spawnLocation, new ArrayList<>());
    }

    public static boolean classExist(String classID)
    {
        ClassDataStorage classDataStorage = RPCore.getInstance().getClassDataStorage();
        return !classDataStorage.getNode().getNode("classes", classID, "discordID").isVirtual();
    }

    public void deleteClass()
    {
        ClassDataStorage classDataStorage = RPCore.getInstance().getClassDataStorage();
        classDataStorage.getNode().getNode("classes", this.classID).setValue(null);
        classDataStorage.save();
    }

    public static LambdaClass getLambdaClassByName(String name)
    {
        HashMap<String, LambdaClass> registeredClasses = RPCore.getInstance().getLambdaClasses();

        for(Map.Entry<String, LambdaClass> loopedClass : registeredClasses.entrySet())
        {
            if(loopedClass.getValue().getName().equalsIgnoreCase(name))
            {
                return loopedClass.getValue();
            }
        }
        return null;
    }

    public static void loadClasses()
    {
        ClassDataStorage classDataStorage = RPCore.getInstance().getClassDataStorage();
        Map<Object, ? extends CommentedConfigurationNode> classes = classDataStorage.getNode().getNode("classes").getChildrenMap();

        if(classes.isEmpty())
        {
            RPCore.getInstance().getLogger().warn("There is no registered classes. {" + classes.toString() + "}");
            return;
        }

        RPCore.getInstance().getLogger().debug("Loading registered classes...");

        for(CommentedConfigurationNode loopedClass : classes.values())
        {
            try
            {
                String name = loopedClass.getNode("name").getString();
                String prefix = loopedClass.getNode("prefix").getString();
                long discordID = loopedClass.getNode("discordID").getLong();
                boolean itemsDisappear = loopedClass.getNode("itemsDisappear").getBoolean();
                TextColor textColor = Utils.getTextColor(loopedClass.getNode("textColor").getString());
                DyeColor dyeColor = Utils.getDyeColor(loopedClass.getNode("dyeColor").getString());
                int x = loopedClass.getNode("spawnLocation", "x").getInt();
                int y = loopedClass.getNode("spawnLocation", "y").getInt();
                int z = loopedClass.getNode("spawnLocation", "z").getInt();
                String worldName = loopedClass.getNode("spawnLocation", "w").getString();
                Optional<World> world = Sponge.getServer().getWorld(worldName);
                World w = world.get();

                List<String> permissions = loopedClass.getNode("permissions").getList(TypeToken.of(String.class));
                LambdaClass lambdaClass = new LambdaClass(loopedClass.getKey().toString(), name, prefix, discordID, itemsDisappear, textColor, dyeColor, new Location<World>(w, x, y, z), permissions);

                RPCore.getInstance().getLogger().info("Class loaded: " + lambdaClass.toString());
                RPCore.getInstance().getLambdaClasses().put(loopedClass.getKey().toString(), lambdaClass);
            }
            catch (Exception e)
            {
                RPCore.getInstance().getLogger().error("One of class is wrong configurated - " + loopedClass.toString());
            }
        }

        RPCore.getInstance().getLogger().debug("All classes have been loaded.");
    }

    public void saveClass()
    {
        ClassDataStorage classDataStorage = RPCore.getInstance().getClassDataStorage();
        ConfigurationNode node = classDataStorage.getNode();

        List<String> kitItemIDs = new ArrayList<>();
        for(ItemStack item : this.classKit)
        {
            kitItemIDs.add(item.getType().getId());
        }

        node.getNode("classes", this.classID, "name").setValue(this.name);
        node.getNode("classes", this.classID, "prefix").setValue(this.prefix);
        node.getNode("classes", this.classID, "discordID").setValue(this.discordID);
        node.getNode("classes", this.classID, "textColor").setValue(this.textColor.getName());
        node.getNode("classes", this.classID, "itemsDisappear").setValue(this.itemsDisappear);
        node.getNode("classes", this.classID, "dyeColor").setValue(this.dyeColor.getName());
        node.getNode("classes", this.classID, "spawnLocation", "x").setValue(this.spawnLocation.getBlockX());
        node.getNode("classes", this.classID, "spawnLocation", "y").setValue(this.spawnLocation.getBlockY());
        node.getNode("classes", this.classID, "spawnLocation", "z").setValue(this.spawnLocation.getBlockZ());
        node.getNode("classes", this.classID, "spawnLocation", "w").setValue(this.spawnLocation.getExtent().getName());
        node.getNode("classes", this.classID, "permissions").setValue(this.permissions);

        int i = 0;
        try
        {
            for(ItemStack kitItem : this.classKit)
            {
                ItemStackSnapshot snapshot = kitItem.createSnapshot();
                node.getNode("classes", this.classID, "kit", i).setValue(TypeTokens.ITEM_SNAPSHOT_TOKEN, snapshot);
                i++;
            }
        }
        catch (ObjectMappingException e)
        {
            e.printStackTrace();
        }
        classDataStorage.save();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LambdaClass that = (LambdaClass) o;
        return discordID == that.discordID &&
                classID.equals(that.classID) &&
                name.equals(that.name) &&
                prefix.equals(that.prefix) &&
                textColor.equals(that.textColor) &&
                dyeColor.equals(that.dyeColor) &&
                Objects.equals(spawnLocation, that.spawnLocation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(classID, name, prefix, discordID, textColor, dyeColor, spawnLocation);
    }

    @Override
    public String toString() {
        return "LambdaClass{" +
                "classID='" + classID + '\'' +
                ", name='" + name + '\'' +
                ", prefix='" + prefix + '\'' +
                ", discordID=" + discordID +
                ", itemsDisappear=" + itemsDisappear +
                ", textColor=" + textColor.getName() +
                ", dyeColor=" + dyeColor.getName() +
                ", permissions=" + permissions.toString() +
                '}';
    }

    private List<ItemStack> loadKitItems()
    {
        ClassDataStorage classDataStorage = RPCore.getInstance().getClassDataStorage();
        List<ItemStack> kit = new ArrayList<>();

        if(!classExist(this.classID)) return new ArrayList<>();

        Map<Object, ? extends CommentedConfigurationNode> kitItems = classDataStorage.getNode().getNode("classes", this.classID, "kit").getChildrenMap();

        if(kitItems.isEmpty()) { return new ArrayList<>(); }

        for(CommentedConfigurationNode loopedKit : kitItems.values())
        {
            try
            {
                ItemStackSnapshot kitItem = loopedKit.getNode().getValue(TypeTokens.ITEM_SNAPSHOT_TOKEN);
                if (kitItem != null)
                {
                    kit.add(kitItem.createStack());
                }
            }
            catch (ObjectMappingException e)
            {
                e.printStackTrace();
            }

        }
        
        return kit;
    }
}
