package me.mederp.rpcore.plugin.listeners;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import me.mederp.rpcore.RPCore;
import me.mederp.rpcore.utils.players.LambdaPlayer;

public class OnClientConnectionJoin
{
    @Listener
    public void onClientConnectionJoin(ClientConnectionEvent.Join e, @First Player p)
    {
        //todo
        RPCore.getInstance().getLambdaPlayers().putIfAbsent(p.getUniqueId(), LambdaPlayer.getOrCreateLambdaPlayer(p.getUniqueId(), p.getName()));
        e.setMessage(Text.of(TextColors.GOLD, p.getName(), TextColors.GREEN, " has joined the game!"));
        RPCore.getInstance().getDiscordModule().getSyncChannel().sendMessage("**Player joined!** " + p.getName() + " has joined the game.").queue();
        p.offer(Keys.DISPLAY_NAME, Text.of(p.getName()));

        if(p.getName().equalsIgnoreCase("REDACTED_LAMBDA"))
        {
            Sponge.getServer().getBroadcastChannel().send(Text.of(TextColors.GREEN, "Plugin creator Lambda has joined server! Say hello ;)"));
        }


    }
}
