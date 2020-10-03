package me.mederp.rpcore.plugin.listeners;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import me.mederp.rpcore.RPCore;

public class OnClientConnectionDisconnect
{
    @Listener
    public void onClientConnectionDisconnect(ClientConnectionEvent.Disconnect e, @First Player p)
    {
        RPCore.getInstance().getLambdaPlayers().remove(p.getUniqueId());
        e.setMessage(Text.of(TextColors.GOLD, p.getName(), TextColors.RED, " has left the game."));
        RPCore.getInstance().getDiscordModule().getSyncChannel().sendMessage("**Player left!** " + p.getName() + " has left the game.").queue();
    }
}
