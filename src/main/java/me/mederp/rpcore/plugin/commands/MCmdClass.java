package me.mederp.rpcore.plugin.commands;

import io.github.nucleuspowered.nucleus.api.NucleusAPI;
import org.spongepowered.api.Sponge;
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
import org.spongepowered.api.entity.living.player.gamemode.GameMode;
import org.spongepowered.api.entity.living.player.tab.TabListEntry;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.InventoryArchetypes;
import org.spongepowered.api.item.inventory.InventoryProperty;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.property.InventoryDimension;
import org.spongepowered.api.item.inventory.property.InventoryTitle;
import org.spongepowered.api.scoreboard.Scoreboard;
import org.spongepowered.api.scoreboard.Team;
import org.spongepowered.api.scoreboard.Visibilities;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import me.mederp.rpcore.RPCore;
import me.mederp.rpcore.utils.classes.LambdaClass;
import me.mederp.rpcore.utils.permissions.PermissionsManager;
import me.mederp.rpcore.utils.players.LambdaPlayer;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class MCmdClass implements CommandExecutor
{
    private Inventory inventory;

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException
    {
        if(!(src instanceof Player)) return CommandResult.empty();

        HashMap<String, LambdaClass> registeredClasses = RPCore.getInstance().getLambdaClasses();
        LambdaPlayer lambdaPlayer = RPCore.getInstance().getLambdaPlayers().get(((Player) src).getUniqueId());
        List<LambdaClass> playerClasses = lambdaPlayer.getAvailableClasses();

        if(!args.getOne(Text.of("classID")).isPresent())
        {
            //argument is empty, generate GUI
            inventory = Inventory.builder().of(InventoryArchetypes.CHEST)
                    .property("inventorydimension", (InventoryProperty) new InventoryDimension(9, 3))
                    .property("inventorytitle", (InventoryProperty) new InventoryTitle(Text.of("Choose department...")))
                    .build(RPCore.getInstance());

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

                PermissionsManager permissionsManager = RPCore.getInstance().getPermissionsManager();
                permissionsManager.clearPermissions((Player) src);

                for(String permission : selectedClass.getPermissions())
                {
                    permissionsManager.addPermission((Player) src, permission);
                }

                setName((Player)src, selectedClass);

                ((Player)src).offer(Keys.DISPLAY_NAME, Text.of(selectedClass.getTextColor(), selectedClass.getPrefix(), TextColors.WHITE, " | ", lambdaPlayer.getNickname()));
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
                //e.set(item);
                e.offer(item);
                continue;
            }
            i++;
        }
    }

    public void setName(Player player, LambdaClass lambdaClass)
    {
        Team team;
        Scoreboard playerScoreboard = player.getScoreboard();
        Optional<Team> optionalTeam = playerScoreboard.getTeam(player.getName());
        if (optionalTeam.isPresent())
        {
            team = optionalTeam.get();
        }
        else
        {
            team = Team.builder().name(player.getName()).build();
            playerScoreboard.registerTeam(team);
        }
        team.addMember(Text.of(player.getName()));
        team.setSuffix(Text.of(lambdaClass.getTextColor(), ' ', '[', lambdaClass.getPrefix(), ']'));

        team.setNameTagVisibility(Visibilities.ALWAYS);
        for (Player onlinePlayers : Sponge.getServer().getOnlinePlayers())
        {
            if (!onlinePlayers.getTabList().getEntry(player.getUniqueId()).isPresent())
            {
                Text name = NucleusAPI.getNicknameService().isPresent() ? ((NucleusAPI.getNicknameService().get()).getNickname(player).isPresent() ? (NucleusAPI.getNicknameService().get()).getNickname(player).get() : Text.of(player.getName())) : Text.of(player.getName());
                onlinePlayers.getTabList().addEntry(TabListEntry.builder().list(onlinePlayers.getTabList())
                        .displayName(Text.of((player.getScoreboard().getTeam(player.getName()).get()).getPrefix(), lambdaClass.getTextColor(), name, ((Team)player
                                .getScoreboard().getTeam(player.getName()).get()).getSuffix())).profile(player.getProfile()).gameMode((GameMode)player.gameMode().get()).build());
            }
        }
    }

    public static CommandSpec classCmd = CommandSpec.builder()
            .executor(new MCmdClass())
            .arguments(
                    GenericArguments.optional(GenericArguments.string(Text.of("classID")))
            )
            .build();
}
