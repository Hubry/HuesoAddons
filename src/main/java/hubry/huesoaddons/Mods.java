package hubry.huesoaddons;

import hubry.huesoaddons.common.config.Config;
import hubry.huesoaddons.module.astralsorcery.AstralModule;
import hubry.huesoaddons.module.botania.BotaniaModule;
import hubry.huesoaddons.module.immeng.ImmEngModule;
import hubry.huesoaddons.module.jei.HuesoJEIPlugin;
import hubry.huesoaddons.module.mekanism.MekanismModule;
import hubry.huesoaddons.module.projecte.ProjectEModule;
import hubry.huesoaddons.module.psi.PsiModule;
import hubry.huesoaddons.module.thaumcraft.ThaumcraftModule;
import net.minecraftforge.fml.common.Loader;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * A list of modules to make this easier and cleaner to manage.
 * JEI is handled elsewhere, see {@link HuesoJEIPlugin}
 */

public enum Mods {
	ASTRAL("astralsorcery", AstralModule::new),
	BOTANIA("botania", BotaniaModule::new),
	//ENDERIO("enderio", EnderIOModule::new),
	IMMENG("immersiveengineering", ImmEngModule::new),
	MEKANISM("mekanism", MekanismModule::new),
	PROJECTE("projecte", ProjectEModule::new),
	PSI("psi", PsiModule::new),
	THAUMCRAFT("thaumcraft", ThaumcraftModule::new),
	//THERMALEXP("thermalexpansion", ThermalExpModule::new),
	;

	public final String modid;
	private final Supplier<IModule> factory;
	private IModule module;

	Mods(String modid, Supplier<IModule> factory) {
		this.modid = modid;
		this.factory = factory;
	}

	private boolean isEnabled() {
		return Loader.isModLoaded(modid) && Config.isEnabled(modid);
	}

	public IModule getModule() {
		if (module == null && isEnabled()) {
			module = factory.get();
		}
		return module;
	}

	public static List<IModule> getAllModules() {
		List<IModule> list = new ArrayList<>();
		for (Mods m : values()) {
			if (m.isEnabled()) {
				list.add(m.getModule());
			}
		}
		return list;
	}
}
