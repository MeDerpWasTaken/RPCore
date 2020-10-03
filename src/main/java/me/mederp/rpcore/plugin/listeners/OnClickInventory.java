package me.mederp.rpcore.plugin.listeners;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;
import org.spongepowered.api.item.inventory.Container;
import org.spongepowered.api.item.inventory.property.InventoryTitle;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import me.mederp.rpcore.RPCore;
import me.mederp.rpcore.utils.classes.LambdaClass;

import java.util.Collection;

public class OnClickInventory
{
    @Listener
    public void onClickInventory(ClickInventoryEvent e, @First Player p)
    {
        Container clickedInventory = e.getTargetInventory();

        if(!clickedInventory.getProperties(InventoryTitle.class).isEmpty())
        {
            String inventoryName = clickedInventory.getProperties(InventoryTitle.class).toArray()[0].toString();
            RPCore.getInstance().getLogger().debug(inventoryName);

            Collection<InventoryTitle> properties = clickedInventory.getProperties(InventoryTitle.class);
            if(properties.contains(new InventoryTitle(Text.of("Choose department..."))))
            {
                e.setCancelled(true);
                p.closeInventory();
                if(e.getCursorTransaction().isValid() && (e.getCursorTransaction().getFinal()).get(Keys.DISPLAY_NAME).isPresent())
                {
                    Text displayName = (e.getCursorTransaction().getFinal()).get(Keys.DISPLAY_NAME).get();
                    String lambdaClassID = getLambdaClassByText(displayName);
                    if(lambdaClassID == null)
                    {
                        p.sendMessage(Text.of(TextColors.RED, "Something went wrong. If that continues, contact Lambda#2594!"));
                        return;
                    }

                    Sponge.getCommandManager().process(p, "class " + lambdaClassID);
                }
            }
        }
    }

    private String getLambdaClassByText(Text input)
    {
        String stringName = input.toPlain();
        LambdaClass result = LambdaClass.getLambdaClassByName(stringName);
        if(result != null) return result.getClassID();
        return null;
    }
}
