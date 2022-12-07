package nl.daankoster.illumination.api.event;

import nl.daankoster.illumination.api.implementation.Implementation;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class PlayerIlluminationHandshakeEvent extends PlayerEvent {
    private static final HandlerList handlerList = new HandlerList();
    private final Implementation implementation;
    private final String version;

    public PlayerIlluminationHandshakeEvent(Player who, Implementation implementation, String version) {
        super(who);
        this.implementation = implementation;
        this.version = version;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

    public byte getImplementationVersion() {
        return implementation.getVersion();
    }

    public String getVersion() {
        return version;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }
}
