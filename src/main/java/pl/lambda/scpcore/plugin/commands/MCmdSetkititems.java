package pl.lambda.scpcore.plugin.commands;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.data.type.HandType;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.filter.data.Has;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import pl.lambda.scpcore.utils.classes.LambdaClass;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class MCmdSetkititems implements CommandExecutor
{
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException
    {
        if(!(src instanceof Player))
        {
            src.sendMessage(Text.of("This command is only for players!"));
            return CommandResult.empty();
        }

        if(!src.hasPermission("lambda.scpcore.setkititems"))
        {
            src.sendMessage(Text.of(TextColors.DARK_RED, "You don't have permission to use that command!"));
            return CommandResult.empty();
        }

        Optional<String> argument = args.getOne(Text.of("className"));
        if(!argument.isPresent())
        {
            src.sendMessage(Text.of(TextColors.RED, "Incorrect usage! Correct: /setkititems <class name>. REMEMBER! All your inventory items will be added to kit! Be sure that there are only items that you expect to be that class kit!"));
            return CommandResult.empty();
        }

        String className = argument.get();

        LambdaClass lambdaClass = LambdaClass.getLambdaClassByName(className);
        if(lambdaClass == null)
        {
            src.sendMessage(Text.of(TextColors.RED, "Class with provided name not exist! REMEMBER! All your inventory items will be added to kit! Be sure that there are only items that you expect to be that class kit!"));
            return CommandResult.empty();
        }

        Player player = (Player) src;
        Set<ItemStack> kitItems = new HashSet<>();
        for(Inventory slot : player.getInventory())
        {
            slot.peek().ifPresent(kitItems::add);
        }

        lambdaClass.getClassKit().clear();
        lambdaClass.getClassKit().addAll(kitItems);
        lambdaClass.saveClass();

        src.sendMessage(Text.of(TextColors.GREEN, "All your inventory has been added to " + lambdaClass.getName() + " class!"));

        return CommandResult.success();
    }

    public static CommandSpec setkititems = CommandSpec.builder()
            .executor(new MCmdSetkititems())
            .arguments(
                    GenericArguments.remainingJoinedStrings(Text.of("className"))
            )
            .build();
}
