package hubry.huesoaddons.module.projecte;

import moze_intel.projecte.api.item.IItemEmc;
import net.minecraft.item.ItemStack;
import xbony2.huesodewiki.api.infobox.IInfoboxParameter;

public class EMCStorageParameter implements IInfoboxParameter {
	@Override
	public boolean canAdd(ItemStack itemStack) {
		return itemStack.getItem() instanceof IItemEmc;
	}

	@Override
	public String getParameterName() {
		return "emcstorage";
	}

	@Override
	public String getParameterText(ItemStack itemStack) {
		return String.valueOf(((IItemEmc) itemStack.getItem()).getMaximumEmc(itemStack));
	}
}
