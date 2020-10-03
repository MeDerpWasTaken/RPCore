package me.mederp.rpcore.plugin.commands;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import me.mederp.rpcore.RPCore;
import me.mederp.rpcore.utils.classes.LambdaClass;
import me.mederp.rpcore.utils.config.LambdaConfig;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MCmdKit implements CommandExecutor
{
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException
    {
        if(!(src instanceof Player)) return CommandResult.empty();

        LambdaClass currentClass = RPCore.getInstance().getLambdaPlayers().get(((Player) src).getUniqueId()).getChosenClass();
        if(currentClass == null)
        {
            src.sendMessage(Text.of(TextColors.RED, "You need to choose class to receive kit!"));
            return CommandResult.empty();
        }

        if(currentClass.getClassKit().isEmpty())
        {
            src.sendMessage(Text.of(TextColors.RED, "That class haven't configured kit!"));
            return CommandResult.empty();
        }

        Player player = (Player) src;
        Date today = new Date();
        Date cooldown = RPCore.getInstance().getKitCooldown().getOrDefault(player.getUniqueId(), null);

        if(cooldown != null && today.before(cooldown))
        {
            DateFormat format = new SimpleDateFormat("dd MM hh:mm");
            src.sendMessage(Text.of(TextColors.RED, "You can't take any kit until " + format.format(cooldown) + "!"));
            return CommandResult.empty();
        }
        else
        {
            RPCore.getInstance().getKitCooldown().remove(player.getUniqueId());
        }

        LambdaConfig config = RPCore.getInstance().getLambdaConfig();
        for(ItemStack kitItem : currentClass.getClassKit())
        {
            //Sponge.getRegistry().getType(ItemType.class, "minecraft:diamond")
            player.getInventory().offer(kitItem);
        }

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, (int) config.getKitCooldownInSeconds());
        Date newCooldown = calendar.getTime();

        RPCore.getInstance().getKitCooldown().putIfAbsent(player.getUniqueId(), newCooldown);
        src.sendMessage(Text.of(TextColors.GREEN, "You have received " + currentClass.getName() + "'s kit successfully!"));

        return CommandResult.success();
    }

    public static CommandSpec kit = CommandSpec.builder()
            .executor(new MCmdKit())
            .build();
}
