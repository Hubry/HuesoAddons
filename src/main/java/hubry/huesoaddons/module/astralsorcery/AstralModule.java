package hubry.huesoaddons.module.astralsorcery;

import hellfirepvp.astralsorcery.common.crafting.ItemHandle;
import hellfirepvp.astralsorcery.common.tile.TileAltar;
import hubry.huesoaddons.IModule;
import hubry.huesoaddons.common.util.HuesoHooks;
import net.minecraft.item.ItemStack;
import xbony2.huesodewiki.Utils;

import javax.annotation.Nullable;

public class AstralModule implements IModule {
	@Override
	public void postInit() {
		HuesoHooks.addRecipes(
				new BasicGrindstoneRecipe(),
				new StarlightInfuserRecipe(),
				new BaseAltarRecipe("Luminous Crafting Table", TileAltar.AltarLevel.DISCOVERY),
				new BaseAltarRecipe("Starlight Crafting Altar", TileAltar.AltarLevel.ATTUNEMENT),
				new BaseAltarRecipe("Celestial Altar", TileAltar.AltarLevel.CONSTELLATION_CRAFT),
				new BaseAltarRecipe("Iridescent Altar", TileAltar.AltarLevel.TRAIT_CRAFT)
		);
	}

	static String outputItemHandle(@Nullable ItemHandle handle) {
		if (handle == null || handle == ItemHandle.EMPTY) {
			return "";
		}
		if (handle.handleType == ItemHandle.Type.OREDICT) {
			return "{{O|" + handle.getOreDictName() + "}}";
		}
		StringBuilder builder = new StringBuilder();
		for (ItemStack stack : handle.getApplicableItems()) {
			builder.append(Utils.outputItem(stack));
		}
		return builder.toString();
	}
}
