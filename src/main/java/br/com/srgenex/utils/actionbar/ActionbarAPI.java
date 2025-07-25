package br.com.srgenex.utils.actionbar;

import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

@SuppressWarnings("unused")
public class ActionbarAPI {

    public static void send(Player p, String text) {
        ((CraftPlayer)p).getHandle().playerConnection.sendPacket(new PacketPlayOutChat(ChatSerializer.a("{\"text\":\"" + ChatColor.translateAlternateColorCodes('&', text) + "\"}"), (byte) 2));
    }

    public static void broadcast(String text) {
        Bukkit.getOnlinePlayers().forEach(player -> send(player, text));
    }

}