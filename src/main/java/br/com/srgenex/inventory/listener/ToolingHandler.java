package br.com.srgenex.inventory.listener;

import br.com.srgenex.GXUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import br.com.srgenex.inventory.container.Container;
import br.com.srgenex.inventory.container.holder.ContainerHolder;
import br.com.srgenex.inventory.container.icon.Icon;

public class ToolingHandler implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    private void onClick(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        InventoryHolder holder = inventory.getHolder();
        if (!(holder instanceof ContainerHolder)) return;
        ContainerHolder containerHolder = (ContainerHolder) holder;
        if (event.getCurrentItem() == null) return;
        event.setCancelled(true);
        Container container = containerHolder.getContainer();
        Icon icon = container.with(event.getRawSlot());
        if(container.getConsumer() != null) container.getConsumer().accept(event);
        if (icon != null) {
            if (icon.isCloseClick()) event.getWhoClicked().closeInventory();
            if (icon.getConsumer() != null) icon.getConsumer().accept(icon, event);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    private void onClose(InventoryCloseEvent event) {
        Player p = (Player)event.getPlayer();
        Inventory inventory = event.getInventory();
        InventoryHolder holder = inventory.getHolder();
        if (!(holder instanceof ContainerHolder)) return;
        ContainerHolder containerHolder = (ContainerHolder) holder;
        Container container = containerHolder.getContainer();
        if(!container.isCloseable()){
            Bukkit.getScheduler().runTaskLater(GXUtils.getInstance(), () -> container.open(p), 5L);
        }
        if(container.getCloseConsumer() != null) container.getCloseConsumer().accept(event);
    }

}
