package me.mederp.rpcore.utils;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.DyeColor;
import org.spongepowered.api.data.type.DyeColors;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.text.format.TextColor;
import org.spongepowered.api.text.format.TextColors;

import java.util.Optional;
import java.util.Random;

public class Utils
{
    public static DyeColor getDyeColor(String s) {
        switch (s.toLowerCase()) {
            case "black":
                return DyeColors.BLACK;
            case "blue":
                return DyeColors.BLUE;
            case "brown":
                return DyeColors.BROWN;
            case "cyan":
                return DyeColors.CYAN;
            case "gray":
                return DyeColors.GRAY;
            case "green":
                return DyeColors.GREEN;
            case "light_blue":
                return DyeColors.LIGHT_BLUE;
            case "lime":
                return DyeColors.LIME;
            case "magenta":
                return DyeColors.MAGENTA;
            case "orange":
                return DyeColors.ORANGE;
            case "pink":
                return DyeColors.PINK;
            case "purple":
                return DyeColors.PURPLE;
            case "red":
                return DyeColors.RED;
            case "silver":
                return DyeColors.SILVER;
            case "yellow":
                return DyeColors.YELLOW;
            case "white":
                return DyeColors.WHITE;
        }
        return null;
    }

    public static TextColor getTextColor(String s) {
        switch (s.toLowerCase()) {
            case "aqua":
                return TextColors.AQUA;
            case "dark_aqua":
                return TextColors.DARK_AQUA;
            case "red":
                return TextColors.RED;
            case "dark_red":
                return TextColors.DARK_RED;
            case "black":
                return TextColors.BLACK;
            case "white":
                return TextColors.WHITE;
            case "green":
                return TextColors.GREEN;
            case "dark_green":
                return TextColors.DARK_GREEN;
            case "gray":
                return TextColors.GRAY;
            case "dark_gray":
                return TextColors.DARK_GRAY;
            case "blue":
                return TextColors.BLUE;
            case "dark_blue":
                return TextColors.DARK_BLUE;
            case "light_purple":
                return TextColors.LIGHT_PURPLE;
            case "dark_purple":
                return TextColors.DARK_PURPLE;
            case "yellow":
                return TextColors.YELLOW;
            case "gold":
                return TextColors.GOLD;
        }
        return null;
    }

    public static String argsBuilder(String[] args, int countFromArgument)
    {
        int argument = countFromArgument - 1;
        StringBuilder result = new StringBuilder();
        for(int i = argument; i < args.length; i++)
        {
            result.append(args[i]).append(" ");
        }
        if(result.toString().endsWith(" "))
        {
            return result.toString().substring(0, result.toString().length() - 1);
        }
        return result.toString();
    }

    public static String randomString(int length)
    {

        int leftLimit = 97;
        int rightLimit = 122;
        Random random = new Random();

        StringBuilder buffer = new StringBuilder(length);
        for (int i = 0; i < length; i++)
        {
            int randomLimitedInt = leftLimit + (int) (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }

        return buffer.toString();
    }

    public static ItemStack loadItemFromString(String itemID, int count)
    {
        Optional<ItemType> optionalItemType = Sponge.getRegistry().getType(ItemType.class, itemID);
        if(!optionalItemType.isPresent()) return null;
        ItemType type = optionalItemType.get();
        ItemStackSnapshot itemSnapshot = type.getTemplate();
        ItemStack item = itemSnapshot.createStack();
        item.setQuantity(count);

        return item;
    }

    public static boolean isSneaking(Player player)
    {
        return player.get(Keys.IS_SNEAKING).orElse(false);
    }
}
