package hubry.huesoaddons.module.projecte;

import moze_intel.projecte.api.ProjectEAPI;
import moze_intel.projecte.api.proxy.IEMCProxy;
import net.minecraft.item.ItemStack;
import xbony2.huesodewiki.api.infobox.IInfoboxParameter;

public class EMCParameter implements IInfoboxParameter {
	private static final IEMCProxy emcProxy = ProjectEAPI.getEMCProxy();

	@Override
	public boolean canAdd(ItemStack itemStack) {
		return emcProxy.hasValue(itemStack);
	}

	@Override
	public String getParameterName() {
		return "emc";
	}

	@Override
	public String getParameterText(ItemStack itemStack) {
		return String.valueOf(emcProxy.getValue(itemStack));
	}
}
