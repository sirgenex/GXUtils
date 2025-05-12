package br.com.srgenex.utils.entity;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;

public class EntityUtils {

    public static Entity getEntityById(int id) {
        for (World world : Bukkit.getWorlds()) {
            for (Entity entity : world.getEntities()) {
                if (entity.getEntityId() == id) {
                    return entity;
                }
            }
        }
        return null;
    }

}
