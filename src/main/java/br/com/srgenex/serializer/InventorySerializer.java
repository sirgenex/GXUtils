package br.com.srgenex.serializer;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

@SuppressWarnings("unused")
public class InventorySerializer {

    public static String serialize(Inventory inventory) {
        try {
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            BukkitObjectOutputStream out = new BukkitObjectOutputStream(bout);
            out.writeInt(inventory.getSize());
            for (int i = 0; i < inventory.getSize(); i++)
                out.writeObject(inventory.getItem(i));
            out.flush();
            out.close();
            return Base64Coder.encodeLines(bout.toByteArray());
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static Inventory deserialize(String data) {
        try {
            ByteArrayInputStream bin = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream in = new BukkitObjectInputStream(bin);
            int size = in.readInt();
            Inventory inv = Bukkit.getServer().createInventory(null, 6*9, "Unnamed");
            for (int i = 0; i < size; i++)
                inv.setItem(i, (ItemStack) in.readObject());
            in.close();
            return inv;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

}