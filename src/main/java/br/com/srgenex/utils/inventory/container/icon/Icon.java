package br.com.srgenex.utils.inventory.container.icon;

import br.com.srgenex.utils.inventory.container.Container;
import br.com.srgenex.utils.inventory.container.holder.ContainerHolder;
import br.com.srgenex.utils.item.ItemBuilder;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

@SuppressWarnings("unused")
@Getter
@Setter
public class Icon {

    public static final String MENU_KEY = "GENEX_CONTAINERS";

    public static Builder of(int position) {
        return new Builder(position);
    }

    public static Builder of(ItemBuilder item) {
        return new Builder(item);
    }

    public static Builder of(ItemBuilder item, String... replacements) {
        return new Builder(item, replacements);
    }

    private int position;

    private final Supplier<ItemStack> current;
    private final BiConsumer<Icon, InventoryClickEvent> consumer;
    private final boolean closeClick;

    protected Icon(int position, Supplier<ItemStack> stack, BiConsumer<Icon, InventoryClickEvent> event, boolean closeClick) {
        this.position = position;
        this.current = stack;
        this.consumer = event;
        this.closeClick = closeClick;
    }

    public void update(Inventory inventory) {
        inventory.setItem(position, current.get());
    }

    public static class Builder {

        private List<Integer> position;
        private BiConsumer<Icon, InventoryClickEvent> consumer;
        private Supplier<ItemStack> itemStack;
        private ItemBuilder itemBuilder;
        private boolean closeClick = false;

        public Builder(ItemBuilder item) {
            this.itemStack = item::wrap;
            if(item.getSlots() != null && item.getSlots().isEmpty()) {
                this.position = Collections.singletonList(item.getSlot());
            }else{
                this.position = item.getSlots();
            }
            this.itemBuilder = item;
        }

        public Builder(ItemBuilder item, String... replacements) {
            this.itemStack = () -> item.wrap(replacements);
            if(item.getSlots() != null && item.getSlots().isEmpty()) {
                this.position = Collections.singletonList(item.getSlot());
            }else{
                this.position = item.getSlots();
            }
            this.itemBuilder = item;
        }

        public Builder(int position) {
            this.position = Collections.singletonList(position);
        }

        public Builder(List<Integer> position) {
            this.position = position;
        }

        public Builder position(int position) {
            this.position = Collections.singletonList(position);
            return this;
        }

        public Builder position(List<Integer> position) {
            this.position = position;
            return this;
        }

        public Builder handle(BiConsumer<Icon, InventoryClickEvent> consumer) {
            this.consumer = consumer;
            return this;
        }

        public Builder setCloseClick(boolean b) {
            this.closeClick = b;
            return this;
        }

        public Builder stack(Supplier<ItemStack> itemStack) {
            this.itemStack = itemStack;
            return this;
        }

        public void build(Container container) {
            if (position.isEmpty()) return;
            ItemBuilder item = new ItemBuilder(itemStack.get());
            ContainerHolder holder = container.getHolder();
            Inventory inventory = holder.getInventory();
            position.forEach(slot -> {
                Icon icon = new Icon(slot, item::wrap, consumer, closeClick);
                container.put(slot, icon);
                inventory.setItem(slot, icon.getCurrent().get());
            });
        }

    }

}
