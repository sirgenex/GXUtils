package br.com.srgenex.utils;

import br.com.srgenex.utils.entity.listener.EntityListener;
import br.com.srgenex.utils.inventory.listener.ToolingHandler;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

@SuppressWarnings("unused")
public class GXUtils {

    @Getter private static Plugin instance;

    public static void load(Plugin plugin){
        Bukkit.getPluginManager().registerEvents(new ToolingHandler(), plugin);
        Bukkit.getPluginManager().registerEvents(new EntityListener(), plugin);
        instance = plugin;
    }

}
