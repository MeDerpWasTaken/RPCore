package pl.lambda.scpcore.discord.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import pl.lambda.scpcore.SCPCore;
import pl.lambda.scpcore.utils.Utils;
import pl.lambda.scpcore.utils.classes.LambdaClass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DCmdRemoveclasspermission extends ListenerAdapter
{
    private HashMap<Member, String> pendingRequests = new HashMap<>();

    public void onMessageReceived(MessageReceivedEvent e)
    {
        if(pendingRequests.containsKey(e.getMember()))
        {
            String classID = pendingRequests.get(e.getMember());
            String permission = e.getMessage().getContentRaw();

            LambdaClass lambdaClass = SCPCore.getInstance().getLambdaClasses().getOrDefault(classID, null);
            if(lambdaClass == null)
            {
                e.getTextChannel().sendMessage("**Error!** Something went wrong. Error code: DRCP1.").queue();
                pendingRequests.remove(e.getMember());
                return;
            }

            List<String> classPermissions = lambdaClass.getPermissions();

            if(classPermissions == null)
            {
                classPermissions = new ArrayList<>();
            }

            if(!classPermissions.contains(permission))
            {
                e.getTextChannel().sendMessage("**Error!** That class doesn't have that permission!").queue();
                pendingRequests.remove(e.getMember());
                return;
            }

            classPermissions.remove(permission);
            lambdaClass.setPermissions(classPermissions);

            lambdaClass.saveClass();
            LambdaClass.loadClasses();

            e.getTextChannel().sendMessage("**Success!** Permission " + permission + " removed from: " + lambdaClass.getName() + "!").queue();

            pendingRequests.remove(e.getMember());
            return;
        }

        String[] args = e.getMessage().getContentRaw().split(" ");
        if(args[0].equalsIgnoreCase("+removeclasspermission"))
        {
            if(!e.getMember().hasPermission(Permission.ADMINISTRATOR))
            {
                e.getTextChannel().sendMessage("**No permission!** You have no permission to use that command!").queue();
                return;
            }

            if(args.length == 1)
            {
                e.getTextChannel().sendMessage("**Error!** Wrong usage. Correct: +removeclasspermission <class name...>!").queue();
                return;
            }

            String className = Utils.argsBuilder(args, 2);
            LambdaClass lambdaClass = LambdaClass.getLambdaClassByName(className);
            if(lambdaClass == null)
            {
                e.getTextChannel().sendMessage("**Error!** Class with that name not exist!").queue();
                return;
            }

            pendingRequests.put(e.getMember(), lambdaClass.getClassID());
            e.getTextChannel().sendMessage("**Success!** Now type permission that you want to remove.").queue();
        }
    }
}
