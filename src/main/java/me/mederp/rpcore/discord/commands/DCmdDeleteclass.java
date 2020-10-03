package me.mederp.rpcore.discord.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import me.mederp.rpcore.RPCore;
import me.mederp.rpcore.discord.DiscordModule;
import me.mederp.rpcore.utils.Utils;
import me.mederp.rpcore.utils.classes.LambdaClass;

public class DCmdDeleteclass extends ListenerAdapter
{
    DiscordModule discordModule;
    public DCmdDeleteclass(DiscordModule discordModule)
    {
        this.discordModule = discordModule;
    }

    public void onMessageReceived(MessageReceivedEvent e)
    {
        String[] args = e.getMessage().getContentRaw().split(" ");
        if(args[0].equalsIgnoreCase("+deleteclass"))
        {
            if(!e.getMember().hasPermission(Permission.ADMINISTRATOR))
            {
                e.getTextChannel().sendMessage("**Error!** You don't have permission to use that command!").queue();
                return;
            }

            if(!(args.length >= 2))
            {
                e.getTextChannel().sendMessage("**Error!** Incorrect usage! Correct: +deleteclass <name...>.").queue();
                return;
            }

            String name = Utils.argsBuilder(args, 2);
            LambdaClass check = LambdaClass.getLambdaClassByName(name);
            if(check == null)
            {
                e.getTextChannel().sendMessage("**Error!** Class with that name not exist!").queue();
                return;
            }

            check.deleteClass();
            RPCore.getInstance().getLambdaClasses().remove(check.getClassID());
            LambdaClass.loadClasses();
            e.getTextChannel().sendMessage("**Success!** Class has been deleted successfully!").queue();
        }
    }
}
