package hubry.huesoaddons.module.thaumcraft;

import net.minecraft.item.ItemStack;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectHelper;
import thaumcraft.api.aspects.AspectList;
import xbony2.huesodewiki.api.infobox.IInfoboxParameter;

public class ItemAspectParameter implements IInfoboxParameter {
	@Override
	public boolean canAdd(ItemStack stack) {
		if (!ThaumcraftModule.itemAspectsEnabled)
			return false;

		AspectList list = AspectHelper.getObjectAspects(stack);
		return list != null && list.visSize() > 0;
	}

	@Override
	public String getParameterName() {
		return "tc6aspects";
	}

	@Override
	public String getParameterText(ItemStack stack) {
		AspectList list = AspectHelper.getObjectAspects(stack);
		StringBuilder builder = new StringBuilder();
		for (Aspect aspect : list.getAspectsSortedByAmount()) {
			builder.append(ThaumcraftModule.outputAspect(aspect, list.getAmount(aspect)));
		}
		return builder.toString();
	}
}
