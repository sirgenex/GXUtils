package br.com.srgenex;

import br.com.srgenex.inventory.listener.ToolingHandler;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class GXUtils {

    @Getter public static Plugin instance;

    public static void load(Plugin plugin){
        Bukkit.getPluginManager().registerEvents(new ToolingHandler(), plugin);
        instance = plugin;
    }

}
