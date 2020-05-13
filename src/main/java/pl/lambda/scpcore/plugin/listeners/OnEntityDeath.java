package pl.lambda.scpcore.plugin.listeners;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.DestructEntityEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import pl.lambda.scpcore.SCPCore;
import pl.lambda.scpcore.utils.classes.LambdaClass;
import pl.lambda.scpcore.utils.players.LambdaPlayer;

public class OnEntityDeath
{
    @Listener
    public void onEntityDeath(DestructEntityEvent e, @First Player p)
    {
        LambdaPlayer potentialPlayer = SCPCore.getInstance().getLambdaPlayers().get(p.getUniqueId());
        LambdaClass lambdaClass = potentialPlayer.getChosenClass();
        if(lambdaClass != null)
        {
            if(lambdaClass.itemsDisappear())
            {
                p.getInventory().clear();
                p.sendMessage(Text.of(TextColors.RED, "You have lost your items!"));
            }
        }
    }
}
