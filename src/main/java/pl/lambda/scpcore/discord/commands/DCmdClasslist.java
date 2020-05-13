package pl.lambda.scpcore.discord.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import pl.lambda.scpcore.SCPCore;
import pl.lambda.scpcore.discord.DiscordModule;
import pl.lambda.scpcore.utils.classes.LambdaClass;

import java.util.HashMap;
import java.util.Map;

public class DCmdClasslist extends ListenerAdapter
{
    DiscordModule discordModule;
    public DCmdClasslist(DiscordModule discordModule)
    {
        this.discordModule = discordModule;
    }

    public void onMessageReceived(MessageReceivedEvent e)
    {
        String[] args = e.getMessage().getContentRaw().split(" ");
        if(args[0].equalsIgnoreCase("+classlist"))
        {
            if(!e.getMember().hasPermission(Permission.ADMINISTRATOR))
            {
                e.getTextChannel().sendMessage("**Error!** You don't have permission to use that command!").queue();
                return;
            }

            HashMap<String, LambdaClass> classes = SCPCore.getInstance().getLambdaClasses();
            e.getTextChannel().sendMessage("**List of current active classes:**").queue();
            for(Map.Entry<String, LambdaClass> entry : classes.entrySet())
            {
                e.getTextChannel().sendMessage("Name: " + entry.getValue().getName() + "\n" +
                        "Prefix: " + entry.getValue().getPrefix() + "\n" +
                        "Discord ID: " + entry.getValue().getDiscordID() + "\n" +
                        "DyeColor: " + entry.getValue().getDyeColor().getName() + "\n" +
                        "TextColor: " + entry.getValue().getTextColor().getName() + "\n\n")
                .queue();
            }
            e.getTextChannel().sendMessage("**That's all!**").queue();
        }
    }
}
