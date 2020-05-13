package pl.lambda.scpcore.discord.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.spongepowered.api.data.type.DyeColor;
import org.spongepowered.api.text.format.TextColor;
import pl.lambda.scpcore.SCPCore;
import pl.lambda.scpcore.discord.DiscordModule;
import pl.lambda.scpcore.utils.Utils;
import pl.lambda.scpcore.utils.classes.LambdaClass;

import java.util.List;
import java.util.UUID;

public class DCmdSetupclass extends ListenerAdapter
{
    DiscordModule discordModule;
    public DCmdSetupclass(DiscordModule discordModule)
    {
        this.discordModule = discordModule;
    }

    public void onMessageReceived(MessageReceivedEvent e)
    {
        String[] args = e.getMessage().getContentRaw().split(" ");
        if(args[0].equalsIgnoreCase("+setupclass"))
        {
            if(!e.getMember().hasPermission(Permission.ADMINISTRATOR))
            {
                e.getTextChannel().sendMessage("**Error!** You don't have permission to use that command!").queue();
                return;
            }

            if(!(args.length >= 7))
            {
                e.getTextChannel().sendMessage("**Error!** Incorrect usage! Correct: +setupclass <prefix> <textColor> <dyeColor> <should items disappear after death> <@ping department role> <name...>").queue();
                return;
            }

            String prefix = args[1];
            TextColor textColor = Utils.getTextColor(args[2]);
            DyeColor dyeColor = Utils.getDyeColor(args[3]);
            String itemsDisappear = args[4];
            List<Role> mentionedRoles = e.getMessage().getMentionedRoles();
            String name = Utils.argsBuilder(args, 7);
            String classID = UUID.randomUUID().toString();

            if(prefix.length() > 6)
            {
                e.getTextChannel().sendMessage("**Error!** Prefix length can't be longer than 6 characters!").queue();
                return;
            }

            if(textColor == null)
            {
                e.getTextChannel().sendMessage("**Error!** Wrong text color value! Correct ones: aqua, dark_aqua, " +
                        "red, dark_red, black, white, green, dark_green, gray, dark_gray, blue, dark_blue, light_purple, " +
                        "dark_purple, yellow, gold!").queue();
                return;
            }

            if(dyeColor == null)
            {
                e.getTextChannel().sendMessage("**Error!** Wrong dye color value! Correct ones: black, blue, brown, " +
                        "cyan, gray, green, light_blue, lime, magenta, orange, pink, purple, red, silver, yellow, white!").queue();
                return;
            }

            if(!(itemsDisappear.equalsIgnoreCase("true") || itemsDisappear.equalsIgnoreCase("false")))
            {
                e.getTextChannel().sendMessage("**Error!** Value itemsDisappear can be only true or false!").queue();
                return;
            }

            if(mentionedRoles.isEmpty())
            {
                e.getTextChannel().sendMessage("**Error!** Discord role has not been loaded successfully!").queue();
                return;
            }

            if(name.length() > 25)
            {
                e.getTextChannel().sendMessage("**Error!** Class name can't be longer than 25 characters!").queue();
                return;
            }

            /*
            if(LambdaClass.classExist(classID))
            {
                e.getTextChannel().sendMessage("**Error!** Unexpected error. Try again. If that continues, contact Lambda#2495!").queue();
                return;
            }
             */

            LambdaClass createdClass = LambdaClass.createLambdaClass(classID, name, prefix, mentionedRoles.get(0).getIdLong(), textColor, dyeColor);
            createdClass.setItemsDisappear(Boolean.parseBoolean(itemsDisappear.toLowerCase()));
            createdClass.saveClass();
            LambdaClass.loadClasses();
            e.getTextChannel().sendMessage("**Success!** Class " + name + " has been created successfully!").queue();
        }
    }
}

