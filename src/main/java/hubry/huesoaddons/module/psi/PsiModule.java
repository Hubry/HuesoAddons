package hubry.huesoaddons.module.psi;

import hubry.huesoaddons.IModule;
import net.minecraftforge.client.ClientCommandHandler;

public class PsiModule implements IModule {
	@Override
	public void postInit() {
		ClientCommandHandler.instance.registerCommand(new SpellPieceInfoboxCommand());
	}
}
