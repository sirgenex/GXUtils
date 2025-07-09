package br.com.srgenex.utils.entity;

import lombok.Getter;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.PathfinderGoalSelector;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.entity.Entity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

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

    public static void disableAI(Entity entity) {
        net.minecraft.server.v1_8_R3.Entity nmsEnt = ((CraftEntity) entity).getHandle();
        NBTTagCompound tag = nmsEnt.getNBTTag();

        if(tag == null) {
            tag = new NBTTagCompound();
        }

        nmsEnt.c(tag);
        tag.setInt("NoAI", 1);
        nmsEnt.f(tag);
    }

    @Getter
    public static ArrayList<UUID> invulnerable = new ArrayList<>();

    public static void setInvulnerable(Entity entity) {
        invulnerable.add(entity.getUniqueId());
    }

}
