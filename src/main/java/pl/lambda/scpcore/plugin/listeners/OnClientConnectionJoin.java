package pl.lambda.scpcore.plugin.listeners;

import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import pl.lambda.scpcore.SCPCore;
import pl.lambda.scpcore.utils.players.LambdaPlayer;

public class OnClientConnectionJoin
{
    @Listener
    public void onClientConnectionJoin(ClientConnectionEvent.Join e, @First Player p)
    {
        //todo
        SCPCore.getInstance().getLambdaPlayers().putIfAbsent(p.getUniqueId(), LambdaPlayer.getOrCreateLambdaPlayer(p.getUniqueId(), p.getName()));
        e.setMessage(Text.of(TextColors.GOLD, p.getName(), TextColors.GREEN, " has joined the game!"));
        SCPCore.getInstance().getDiscordModule().getSyncChannel().sendMessage("**Player joined!** " + p.getName() + " has joined the game.").queue();
        p.offer(Keys.DISPLAY_NAME, Text.of(p.getName()));
    }
}
