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

public class DCmdAddclasspermission extends ListenerAdapter
{
    private HashMap<Member, String> pendingRequests = new HashMap<>();

    public void onMessageReceived(MessageReceivedEvent e)
    {
        if(pendingRequests.containsKey(e.getMember()))
        {
            String classID = pendingRequests.get(e.getMember());
            String permission = e.getMessage().getContentRaw();

            if(permission.equalsIgnoreCase("stop"))
            {
                pendingRequests.remove(e.getMember());
                e.getTextChannel().sendMessage("**Success!** Adding permissions is now stopped.").queue();
                return;
            }

            LambdaClass lambdaClass = SCPCore.getInstance().getLambdaClasses().getOrDefault(classID, null);
            if(lambdaClass == null)
            {
                SCPCore.getInstance().getLogger().error("Issue DACP1: LambdaClass is null. classID=" + classID);
                e.getTextChannel().sendMessage("**Error!** Something went wrong. Error code: DACP1.").queue();
                pendingRequests.remove(e.getMember());
                return;
            }


            List<String> gettedPermissions = lambdaClass.getPermissions();
            List<String> classPermissions = new ArrayList<>(gettedPermissions);

            if(classPermissions.contains(permission))
            {
                e.getTextChannel().sendMessage("**Error!** That class has this permission!").queue();
                return;
            }

            classPermissions.add(permission);
            lambdaClass.setPermissions(classPermissions);

            lambdaClass.saveClass();
            LambdaClass.loadClasses();

            e.getTextChannel().sendMessage("**Success!** Permission " + permission + " added to: " + lambdaClass.getName() + "! To add next one, just type it below. If you added every permission, type stop.").queue();
            return;
        }

        String[] args = e.getMessage().getContentRaw().split(" ");
        if(args[0].equalsIgnoreCase("+addclasspermission"))
        {
            if(!e.getMember().hasPermission(Permission.ADMINISTRATOR))
            {
                e.getTextChannel().sendMessage("**No permission!** You have no permission to use that command!").queue();
                return;
            }

            if(args.length == 1)
            {
                e.getTextChannel().sendMessage("**Error!** Wrong usage. Correct: +addclasspermission <class name...>!").queue();
                return;
            }

            String className = Utils.argsBuilder(args, 2);
            LambdaClass lambdaClass = LambdaClass.getLambdaClassByName(className);
            if(lambdaClass == null)
            {
                e.getTextChannel().sendMessage("**Error!** Class with that name not exist!").queue();
                return;
            }

            System.out.println(lambdaClass.getClassID());
            pendingRequests.put(e.getMember(), lambdaClass.getClassID());
            e.getTextChannel().sendMessage("**Success!** Now type permissions that you want to add. If you added every permission that you wanted, type stop.").queue();
        }
    }
}
