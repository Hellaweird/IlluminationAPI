package nl.daankoster.illumination.api.implementation;

import nl.daankoster.illumination.api.Feature;
import nl.daankoster.illumination.api.NetworkInterface;
import org.bukkit.entity.Player;

import java.util.EnumSet;

public interface Implementation {
    default boolean isSupported(Feature feature) {
        return getFeatures().contains(feature);
    }

    EnumSet<Feature> getFeatures();

    byte getVersion();

    default void sendLights(NetworkInterface network, Player player, Integer[] lights, int red, int green, int blue, int brightness) {
        throw new UnsupportedOperationException();
    }

}
