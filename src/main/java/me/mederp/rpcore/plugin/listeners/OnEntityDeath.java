package me.mederp.rpcore.plugin.listeners;

import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.DestructEntityEvent;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import me.mederp.rpcore.RPCore;
import me.mederp.rpcore.utils.classes.LambdaClass;
import me.mederp.rpcore.utils.players.LambdaPlayer;

public class OnEntityDeath
{
    @Listener
    public void onEntityDeath(DestructEntityEvent.Death e)
    {
        Entity entity = e.getTargetEntity();
        if(!(entity instanceof Player)) return;
        Player p = (Player) entity;

        LambdaPlayer potentialPlayer = RPCore.getInstance().getLambdaPlayers().get(p.getUniqueId());
        LambdaClass lambdaClass = potentialPlayer.getChosenClass();
        if(lambdaClass != null)
        {
            if(lambdaClass.itemsDisappear())
            {
                p.getInventory().clear();
                p.sendMessage(Text.of(TextColors.RED, "You were in " + lambdaClass.getName() + " class, and you have lost your items after death."));
            }
        }
    }
}
