package br.com.srgenex.inventory.container.holder;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import br.com.srgenex.inventory.container.Container;
import br.com.srgenex.inventory.container.size.Size;

public class ContainerHolder implements InventoryHolder {

    private final Container container;
    private final Inventory inventory;

    public ContainerHolder(Container container) {
        final Size size = container.getSize();
        inventory = Bukkit.createInventory(this, size.getAmount(), container.getName());
        this.container = container;
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    public Container getContainer() {
        return container;
    }
}
