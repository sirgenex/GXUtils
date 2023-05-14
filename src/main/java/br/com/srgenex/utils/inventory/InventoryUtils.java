package br.com.srgenex.utils.inventory;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SuppressWarnings("unused")
public class InventoryUtils {

    public static void addItem(Player p, ItemStack item){
        p.getInventory().addItem(item).values().forEach(it -> p.getWorld().dropItemNaturally(p.getLocation(), it));
    }

    public static void addItem(Player p, ItemStack... item){
        for (ItemStack itemStack : item) {
            addItem(p, itemStack);
        }
    }

    public static void addItem(Player p, List<ItemStack> item){
        item.forEach(it -> addItem(p, it));
    }

    public static Integer getRandomSlot(Inventory inventory){
        if(inventory.firstEmpty() == -1) return -1;
        List<Integer> slots = getAvailableSlots(inventory);
        if(slots.size() == 1) return slots.get(0);
        return slots.get(new Random().nextInt(slots.size())-1);
    }

    public static List<Integer> getAvailableSlots(Inventory inventory){
        List<Integer> slots = new ArrayList<>();
        for(int slot = 0 ; slot < inventory.getSize() ; slot++) {
            ItemStack item = inventory.getItem(slot);
            if(item == null || item.getType() == Material.AIR) slots.add(slot);
        }
        return slots;
    }

}
