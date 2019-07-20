package hubry.huesoaddons.module.projecte;

import hubry.huesoaddons.IModule;
import hubry.huesoaddons.common.util.HuesoHooks;

public class ProjectEModule implements IModule {
	@Override
	public void postInit() {
		HuesoHooks.addInfoboxParam(new EMCParameter());
		HuesoHooks.addInfoboxParam(new EMCStorageParameter());
	}
}
