package nl.daankoster.illumination.api.implementation;

import nl.daankoster.illumination.api.Feature;
import nl.daankoster.illumination.api.NetworkInterface;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.nio.ByteBuffer;
import java.util.*;

public class ImplV1 implements Implementation {

    protected static final String LIGHTS = "illumination:light";
    private final Plugin plugin;

    private final EnumSet<Feature> features = EnumSet.of(
            Feature.LIGHTS
    );

    public ImplV1(Plugin plugin) {
        this.plugin = plugin;
        plugin.getServer().getMessenger().registerOutgoingPluginChannel(plugin, LIGHTS);
    }

    @Override
    public EnumSet<Feature> getFeatures() {
        return features;
    }

    @Override
    public byte getVersion() {
        return 4;
    }

    @Override
    public void sendLights(NetworkInterface network, Player player, Integer[] lights, int red, int green, int blue, int brightness) {
        ByteBuffer buffer = ByteBuffer.allocate(20);
        List<Integer> intList = new ArrayList<>(Arrays.asList(lights));

        buffer.put((byte) (intList.contains(1) ? 1 : 0));
        buffer.put((byte) (intList.contains(2) ? 1 : 0));
        buffer.put((byte) (intList.contains(3) ? 1 : 0));
        buffer.putInt(red);
        buffer.putInt(green);
        buffer.putInt(blue);
        buffer.putInt(brightness);

        buffer.rewind();
        byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes);

        network.sendMessage(player, LIGHTS, bytes);
    }


}
