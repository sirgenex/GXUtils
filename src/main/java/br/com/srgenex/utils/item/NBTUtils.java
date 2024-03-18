package br.com.srgenex.utils.item;

import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

@SuppressWarnings("unused")
public class NBTUtils {

    public static boolean exists(ItemStack item, String key) {
        if (item == null || item.getType() == Material.AIR) return false;
        net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        if (nmsItem.hasTag()) {
            net.minecraft.server.v1_8_R3.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
            NBTTagCompound compound = nmsStack.hasTag() ? nmsStack.getTag() : new NBTTagCompound();
            return compound.hasKey(key);
        }
        return false;
    }

    public static ItemStack setString(ItemStack item, String key, String value) {
        if (item == null || item.getType() == Material.AIR) return item;
        net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        NBTTagCompound compound = (nmsItem.hasTag()) ? nmsItem.getTag() : new NBTTagCompound();
        compound.setString(key, value);
        nmsItem.setTag(compound);
        return CraftItemStack.asBukkitCopy(nmsItem);
    }

    public static ItemStack setInteger(ItemStack item, String key, Integer value) {
        if (item == null || item.getType() == Material.AIR) return item;
        net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        NBTTagCompound compound = (nmsItem.hasTag()) ? nmsItem.getTag() : new NBTTagCompound();
        compound.setInt(key, value);
        nmsItem.setTag(compound);
        return CraftItemStack.asBukkitCopy(nmsItem);
    }

    public static ItemStack setDouble(ItemStack item, String key, Double value) {
        if (item == null || item.getType() == Material.AIR) return item;
        net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        NBTTagCompound compound = (nmsItem.hasTag()) ? nmsItem.getTag() : new NBTTagCompound();
        compound.setDouble(key, value);
        nmsItem.setTag(compound);
        return CraftItemStack.asBukkitCopy(nmsItem);
    }

    public static String getString(ItemStack item, String key) {
        if (item == null || item.getType() == Material.AIR) return null;
        net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        NBTTagCompound tag = nmsItem.getTag();
        return tag.getString(key);
    }

    public static Integer getInteger(ItemStack item, String key) {
        if (item == null || item.getType() == Material.AIR) return null;
        net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        NBTTagCompound tag = nmsItem.getTag();
        return tag.getInt(key);
    }

    public static Double getDouble(ItemStack item, String key) {
        if (item == null || item.getType() == Material.AIR) return null;
        net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        NBTTagCompound tag = nmsItem.getTag();
        return tag.getDouble(key);
    }

}
