package br.com.srgenex.utils.entity;

import net.minecraft.server.v1_8_R3.PathfinderGoalSelector;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;

import java.lang.reflect.Field;
import java.util.Set;

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

    public static void clearGoals(PathfinderGoalSelector selector) {
        try {
            Field bField = PathfinderGoalSelector.class.getDeclaredField("b");
            Field cField = PathfinderGoalSelector.class.getDeclaredField("c");

            bField.setAccessible(true);
            cField.setAccessible(true);

            ((Set<?>) bField.get(selector)).clear();
            ((Set<?>) cField.get(selector)).clear();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
