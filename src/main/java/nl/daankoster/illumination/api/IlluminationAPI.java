package nl.daankoster.illumination.api;

import nl.daankoster.illumination.api.implementation.ImplV1;
import nl.daankoster.illumination.api.implementation.Implementation;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class IlluminationAPI {
    private final Plugin plugin;
    private final PlayerListener playerListener;
    private final Map<Byte, Implementation> implementations = new HashMap<>();
    private final Map<Player, Implementation> players = new HashMap<>();
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final Lock readLock = lock.readLock();
    private final Lock writeLock = lock.writeLock();
    private final NetworkInterface defaultNetwork;
    public static ImplV1 implementation;

    public IlluminationAPI(Plugin plugin) {
        this.plugin = plugin;
        this.playerListener = new PlayerListener(this);
        this.defaultNetwork = new DefaultNetworkInterface(plugin);
        implementation = new ImplV1(plugin);
    }

    public void registerImplementation(Implementation implementation) {
        writeLock.lock();
        try {
            implementations.put(implementation.getVersion(), implementation);
        } finally {
            writeLock.unlock();
        }
    }

    public Plugin getPlugin() {
        return plugin;
    }

    Map<Byte, Implementation> getImplementations() {
        readLock.lock();
        try {
            return new HashMap<>(implementations);
        } finally {
            readLock.unlock();
        }
    }

    void setImplementation(Player player, Implementation implementation) {
        writeLock.lock();
        try {
            if (implementation != null) {
                players.put(player, implementation);
            } else {
                players.remove(player);
            }
        } finally {
            writeLock.unlock();
        }
    }

    public boolean isEnabled(Player player) {
        readLock.lock();
        try {
            return players.containsKey(player);
        } finally {
            readLock.unlock();
        }
    }

    public boolean sendLights(NetworkInterface network, Player player, Integer[] lights, int red, int green, int blue, int brightness) {
        if (network == null) {
            network = defaultNetwork;
        }

        readLock.lock();
        try {
            Implementation implementation = players.get(player);
            if (implementation == null) {
                return false;
            }

            implementation.sendLights(network, player, lights, red, green, blue, brightness);
            return true;
        } finally {
            readLock.unlock();
        }
    }

    public void unregister() {
        writeLock.lock();
        try {
            implementations.clear();
            playerListener.unregister();
        } finally {
            writeLock.unlock();
        }
    }
}
