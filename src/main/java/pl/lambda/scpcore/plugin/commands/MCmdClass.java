package pl.lambda.scpcore.plugin.commands;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.DyeColor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.InventoryArchetypes;
import org.spongepowered.api.item.inventory.InventoryProperty;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.property.InventoryDimension;
import org.spongepowered.api.item.inventory.property.InventoryTitle;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import pl.lambda.scpcore.SCPCore;
import pl.lambda.scpcore.utils.classes.LambdaClass;
import pl.lambda.scpcore.utils.permissions.PermissionsManager;
import pl.lambda.scpcore.utils.players.LambdaPlayer;

import java.util.HashMap;
import java.util.List;

public class MCmdClass implements CommandExecutor
{
    private Inventory inventory;

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException
    {
        if(!(src instanceof Player)) return CommandResult.empty();

        HashMap<String, LambdaClass> registeredClasses = SCPCore.getInstance().getLambdaClasses();
        LambdaPlayer lambdaPlayer = SCPCore.getInstance().getLambdaPlayers().get(((Player) src).getUniqueId());
        List<LambdaClass> playerClasses = lambdaPlayer.getAvailableClasses();

        if(!args.getOne(Text.of("classID")).isPresent())
        {
            //argument is empty, generate GUI
            inventory = Inventory.builder().of(InventoryArchetypes.CHEST)
                    .property("inventorydimension", (InventoryProperty) new InventoryDimension(9, 3))
                    .property("inventorytitle", (InventoryProperty) new InventoryTitle(Text.of("Choose department...")))
                    .build(this);

            int inventorySlot = 0;
            for(LambdaClass playerClass : playerClasses)
            {
                DyeColor classColor = playerClass.getDyeColor();
                String className = playerClass.getName();

                ItemStack item = ItemStack.builder().itemType(ItemTypes.WOOL)
                        .add(Keys.DYE_COLOR, classColor).quantity(1)
                        .add(Keys.DISPLAY_NAME, Text.of(className))
                        .build();

                addToSlot(inventory, item, inventorySlot);
                inventorySlot++;
            }

            ((Player) src).closeInventory();
            ((Player) src).openInventory(inventory);
        }
        else
        {
            //argument is set, check if exist & player owns it
            String lambdaClassID = args.<String>getOne(Text.of("classID")).get();
            LambdaClass selectedClass = registeredClasses.getOrDefault(lambdaClassID, null);

            if(selectedClass == null)
            {
                src.sendMessage(Text.of(TextColors.RED, "Class with provided ID not exist!"));
                return CommandResult.empty();
            }

            boolean success = false;
            for(LambdaClass playerClass : lambdaPlayer.getAvailableClasses())
            {
                if(playerClass.equals(selectedClass))
                {
                    success = true;
                }
            }

            if(success)
            {
                lambdaPlayer.setChosenClass(selectedClass);

                if(selectedClass.getSpawnLocation() != null)
                {
                    ((Player) src).setLocation(selectedClass.getSpawnLocation());
                }

                PermissionsManager permissionsManager = SCPCore.getInstance().getPermissionsManager();
                permissionsManager.clearPermissions((Player) src);

                for(String permission : selectedClass.getPermissions())
                {
                    permissionsManager.addPermission((Player) src, permission);
                }

                src.sendMessage(Text.of(TextColors.GREEN, "Your current class is now: " + selectedClass.getName() + "!"));
            }
            else
            {
                src.sendMessage(Text.of(TextColors.RED, "You don't have that class!"));
                return CommandResult.empty();
            }
        }

        return CommandResult.success();
    }

    public void addToSlot(Inventory inv, ItemStack item, int slotNumber)
    {
        int i = 0;
        for (Inventory e : inv.slots())
        {
            if (i == slotNumber)
            {
                e.set(item);
                continue;
            }
            i++;
        }
    }

    public static CommandSpec classCmd = CommandSpec.builder()
            .executor(new MCmdClass())
            .arguments(
                    GenericArguments.optional(GenericArguments.string(Text.of("classID")))
            )
            .build();
}