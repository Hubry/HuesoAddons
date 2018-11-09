package hubry.huesoaddons.common.config;

import hubry.huesoaddons.HuesoAddons;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.IModGuiFactory;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@SuppressWarnings("unused") //Referenced in @Mod on main class
public class GuiFactory implements IModGuiFactory {
	@Override
	public void initialize(Minecraft minecraftInstance) {
	}

	@Override
	public boolean hasConfigGui() {
		return true;
	}

	@Override
	public GuiScreen createConfigGui(GuiScreen parentScreen) {
		return new ConfigGui(parentScreen);
	}

	@Override
	public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
		return null;
	}

	private static class ConfigGui extends GuiConfig {
		ConfigGui(GuiScreen parent) {
			super(parent, getConfigElements(), HuesoAddons.MODID, false, false, "HuesoAddons Config");
		}

		private static List<IConfigElement> getConfigElements() {
			Configuration c = Config.cfg;
			return c.getCategoryNames().stream()
					.filter(name -> !c.getCategory(name).isChild())
					.map(name -> new ConfigElement(c.getCategory(name).setLanguageKey(HuesoAddons.MODID + ".config." + name)))
					.collect(Collectors.toList());
		}
	}
}
