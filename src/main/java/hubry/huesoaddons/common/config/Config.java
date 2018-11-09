package hubry.huesoaddons.common.config;

import hubry.huesoaddons.HuesoAddons;
import hubry.huesoaddons.Mods;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.File;
import java.util.HashSet;

public class Config {
	public static Configuration cfg;

	private static final int CONFIG_VERSION = 0;
	private static HashSet<String> enabledMods = new HashSet<>();

	public static boolean isEnabled(String modid) {
		return enabledMods.contains(modid);
	}

	public static void initConfig(File file) {
		cfg = new Configuration(file, String.valueOf(CONFIG_VERSION));

		loadConfig(true);
		MinecraftForge.EVENT_BUS.register(Config.class);
	}

	private static void loadConfig(boolean loading) {
		cfg.getCategory("_modules").setRequiresMcRestart(true)
				.setComment("Disabling a module on this list will prevent it from loading entirely");

		for (Mods mod : Mods.values()) {
			if (cfg.get("_modules", mod.modid, true).getBoolean()) {
				enabledMods.add(mod.modid);

				if (loading && Loader.isModLoaded(mod.modid)) {
					HuesoAddons.addModule(mod.getModule());
				}
			}
		}
		HuesoAddons.configureModules();

		if (cfg.hasChanged()) {
			cfg.save();
		}
	}

	@SubscribeEvent
	public static void onConfigChange(ConfigChangedEvent.PostConfigChangedEvent event) {
		if (HuesoAddons.MODID.equals(event.getModID())) {
			loadConfig(false);
		}
	}
}
