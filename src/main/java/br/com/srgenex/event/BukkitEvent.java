package br.com.srgenex.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public abstract class BukkitEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

}