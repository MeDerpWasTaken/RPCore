package pl.lambda.scpcore.plugin.listeners;

import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.text.Text;
import pl.lambda.scpcore.utils.Utils;

public class OnInteractBlock
{
    @Listener
    public void onPlayerBlockInteract(InteractBlockEvent.Primary e, @First Player p)
    {
        BlockSnapshot clickedBlock = e.getTargetBlock();
        String name = clickedBlock.getExtendedState().getName();
        //p.sendMessage(Text.of(name));
        if(Utils.isSneaking(p))
        {
            if(name.startsWith("blockcraftery:"))
            {
                e.setCancelled(true);
                //https://gyazo.com/ebdc5e4ce33f1212cca7dc499051b916
            }
        }
    }
}
