package nl.daankoster.illumination.api;

import nl.daankoster.illumination.api.event.PlayerIlluminationHandshakeEvent;
import nl.daankoster.illumination.api.implementation.Implementation;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRegisterChannelEvent;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.nio.ByteBuffer;
import java.util.Map;

public class PlayerListener implements Listener, PluginMessageListener {
    private static final String CHANNEL = "illumination:hs";
    private final IlluminationAPI api;



    public PlayerListener(IlluminationAPI api) {
        this.api = api;

        Bukkit.getPluginManager().registerEvents(this, api.getPlugin());
        Bukkit.getMessenger().registerIncomingPluginChannel(api.getPlugin(), CHANNEL, this);
        Bukkit.getMessenger().registerOutgoingPluginChannel(api.getPlugin(), CHANNEL);
    }

    @EventHandler
    public void onPlayerRegisterChannel(PlayerRegisterChannelEvent event) {
        if (!event.getChannel().equals(CHANNEL)) {
            return;
        }

        byte[] message = new byte[]{1, 4};

        event.getPlayer().sendPluginMessage(api.getPlugin(), CHANNEL, message);
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] payload) {
        if (!channel.equals(CHANNEL) || payload.length < 1) {
            return;
        }

        Implementation implementation = IlluminationAPI.implementation;

        api.setImplementation(player, implementation);

        if (implementation != null) {
            Bukkit.getPluginManager().callEvent(new PlayerIlluminationHandshakeEvent(player, implementation, "Latest"));
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        api.setImplementation(event.getPlayer(), null);
    }

    public void unregister() {
        HandlerList.unregisterAll(this);
        Bukkit.getMessenger().unregisterIncomingPluginChannel(api.getPlugin(), CHANNEL, this);
    }
}
