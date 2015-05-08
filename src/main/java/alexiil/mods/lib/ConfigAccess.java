package alexiil.mods.lib;

import java.io.File;
import java.util.Map;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.google.common.collect.Maps;

public class ConfigAccess {
    private static final String CATEGORY = Configuration.CATEGORY_GENERAL;
    private static final Map<String, ConfigAccess> configs = Maps.newHashMap();

    private final Configuration cfg;
    private boolean isOpen = false;
    private AlexIILMod mod;

    public static ConfigAccess get(File file, AlexIILMod mod) {
        if (configs.containsKey(file.getAbsolutePath())) {
            ConfigAccess ca = configs.get(file);
            if (ca.mod == null)
                ca.mod = mod;
            return ca;
        }
        ConfigAccess ca = new ConfigAccess(file, mod);
        configs.put(file.getAbsolutePath(), ca);
        return ca;
    }

    private ConfigAccess(File file, AlexIILMod mod) {
        cfg = new Configuration(file);
        this.mod = mod;
        FMLCommonHandler.instance().bus().register(this);
    }

    @SubscribeEvent
    public void onConfig(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (isOpen && mod.meta.modId.equals(event.modID))
            saveAll();
    }

    public void saveAll() {
        if (isOpen && cfg.hasChanged())
            cfg.save();
    }

    public Configuration cfg() {
        if (!isOpen) {
            cfg.load();
            isOpen = true;
        }
        return cfg;
    }

    public Property getProp(String key, boolean defaultValue) {
        return cfg().get(CATEGORY, key, defaultValue);
    }

    public Property getProp(String key, int defaultValue) {
        return cfg().get(CATEGORY, key, defaultValue);
    }

    public Property getProp(String key, byte defaultValue) {
        return cfg().get(CATEGORY, key, defaultValue);
    }

    public Property getProp(String key, double defaultValue) {
        return cfg().get(CATEGORY, key, defaultValue);
    }

    public Property getProp(String key, short defaultValue) {
        return cfg().get(CATEGORY, key, defaultValue);
    }

    public Property getProp(String key, String defaultValue) {
        return cfg().get(CATEGORY, key, defaultValue);
    }
}
