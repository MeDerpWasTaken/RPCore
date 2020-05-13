package pl.lambda.scpcore.discord.commands;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import pl.lambda.scpcore.SCPCore;
import pl.lambda.scpcore.discord.DiscordModule;
import pl.lambda.scpcore.utils.Utils;
import pl.lambda.scpcore.utils.syncdata.SyncManager;

public class DCmdVerify extends ListenerAdapter
{
    DiscordModule discordModule;
    public DCmdVerify(DiscordModule discordModule)
    {
        this.discordModule = discordModule;
    }

    public void onMessageReceived(MessageReceivedEvent e)
    {
        String[] args = e.getMessage().getContentRaw().split(" ");
        if(args[0].equalsIgnoreCase("+verify"))
        {
            if(args.length != 2)
            {
                e.getTextChannel().sendMessage("**Error!** Incorrect usage! Correct: +verify <Minecraft nickname>!").queue();
                return;
            }

            String nickname = args[1];
            if(!Sponge.getServer().getPlayer(nickname).isPresent())
            {
                e.getTextChannel().sendMessage("**Error!** You are not online! Join server and try again.").queue();
                return;
            }

            Player target = Sponge.getServer().getPlayer(nickname).get();
            SyncManager syncManager = SCPCore.getInstance().getSyncManager();
            String code = Utils.randomString(4);

            syncManager.startSync(target.getUniqueId(), e.getAuthor().getIdLong(), code);

            e.getAuthor().openPrivateChannel().queue(channel -> {
                channel.sendMessage("**Your synchronization code is: " + code + ". Now type in-game command /verify " + code + " to finish procedure.").queue();
            });

            e.getTextChannel().sendMessage("**Success!** You have started sync procedure. Follow procedures that were send on your DM's. If you have disabled them, activate them and start procedure again.").queue();
        }
    }
}
