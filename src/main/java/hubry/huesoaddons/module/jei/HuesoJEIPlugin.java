package hubry.huesoaddons.module.jei;

import hubry.huesoaddons.HuesoAddons;
import hubry.huesoaddons.IModule;
import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JEIPlugin;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.client.event.GuiScreenEvent.KeyboardInputEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
import xbony2.huesodewiki.HuesoDeWiki;
import xbony2.huesodewiki.PageCreator;
import xbony2.huesodewiki.Utils;
import xbony2.huesodewiki.recipe.RecipeCreator;

@JEIPlugin
public class HuesoJEIPlugin implements IModPlugin, IModule {
	private IJeiRuntime runtime;

	public HuesoJEIPlugin() {
		HuesoAddons.addModule(this);
	}

	@Override
	public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
		this.runtime = jeiRuntime;
	}

	@Override
	public void postInit() {
		MinecraftForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent
	public void buttonPressed(KeyboardInputEvent.Post event) {
		Minecraft mc = Minecraft.getMinecraft();
		int eventKey = Keyboard.getEventKey();
		if (mc.world == null || !(mc.currentScreen instanceof GuiContainer)
				|| !(HuesoDeWiki.copyPageKey.isActiveAndMatches(eventKey) || HuesoDeWiki.copyNameKey.isActiveAndMatches(eventKey))
				|| !Keyboard.getEventKeyState() || Keyboard.isRepeatEvent())
			return;

		Object o = runtime.getIngredientListOverlay().getIngredientUnderMouse();
		if (o instanceof ItemStack) {
			ItemStack itemstack = (ItemStack) o;

			if (itemstack.isEmpty())
				return;

			if (eventKey == HuesoDeWiki.copyNameKey.getKeyCode()) {
				Utils.copyString(itemstack.getDisplayName());
				return;
			}

			if (GuiScreen.isCtrlKeyDown()) {
				Utils.copyString(RecipeCreator.createRecipes(itemstack));
				Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new TextComponentTranslation("msg.copiedrecipe", itemstack.getDisplayName()));
			} else {
				Utils.copyString(PageCreator.createPage(itemstack));
				Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new TextComponentTranslation("msg.copiedpage", itemstack.getDisplayName()));
			}
		}
	}
}
