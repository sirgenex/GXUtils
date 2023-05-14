package br.com.srgenex.utils.message;

import br.com.srgenex.utils.placeholder.ReplacementPlaceholder;
import lombok.Data;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@SuppressWarnings({"unused"})
@Data
public class Message {

    private final List<String> message;

    public static HashMap<String, HashMap<String, List<String>>> messages = new HashMap<>();

    public static void load(Plugin plugin, FileConfiguration c, String path) {
        HashMap<String, List<String>> messages = new HashMap<>();
        Objects.requireNonNull(c.getConfigurationSection(path)).getKeys(true).forEach(s ->
                messages.put(s, c.getStringList(path + "." + s)));
        Message.messages.put(plugin.getName(), messages);
    }

    public static void load(Plugin plugin, FileConfiguration c) {
        load(plugin, c, "");
    }

    public static Message of(Plugin plugin, String path) {
        return new Message(messages.getOrDefault(plugin.getName(), new HashMap<>())
                .getOrDefault(path, Collections.singletonList(
                        "&cError! Message " + path + " of " + plugin.getName() + " doesnt exists. Contact an admin."
                )));
    }

    public void send(CommandSender sender, List<ReplacementPlaceholder> replacements) {
        if (sender instanceof Player) {
            message.forEach(msg -> {
                for (ReplacementPlaceholder replacement : replacements)
                    msg = msg.replace(replacement.getPlaceholder(), replacement.getReplacement());
                sender.sendMessage(translate(msg.trim()));
            });
        }
    }

    public void send(CommandSender sender, String... replacements) {
        if (sender instanceof Player) {
            message.forEach(msg -> {
                for (String replacement : replacements) {
                    String[] s = replacement.split(", ");
                    msg = msg.replace(s[0], s[1]);
                }
                sender.sendMessage(translate(msg.trim()));
            });
        }
    }

    public void send(Player p, String... replacements) {
        send((CommandSender) p, replacements);
    }

    public void send(OfflinePlayer sender, String... replacements) {
        if (sender.isOnline()) send(sender.getPlayer(), replacements);
    }

    public void broadcast(String... replacements) {
        Bukkit.getOnlinePlayers().forEach(player -> send(player, replacements));
    }

    public String translate(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

}
