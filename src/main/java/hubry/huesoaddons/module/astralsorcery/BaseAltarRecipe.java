package hubry.huesoaddons.module.astralsorcery;

import hellfirepvp.astralsorcery.common.crafting.ItemHandle;
import hellfirepvp.astralsorcery.common.crafting.altar.AbstractAltarRecipe;
import hellfirepvp.astralsorcery.common.crafting.altar.AltarRecipeRegistry;
import hellfirepvp.astralsorcery.common.crafting.altar.recipes.AttunementRecipe;
import hellfirepvp.astralsorcery.common.crafting.altar.recipes.ConstellationRecipe;
import hellfirepvp.astralsorcery.common.crafting.altar.recipes.TraitRecipe;
import hellfirepvp.astralsorcery.common.crafting.helper.AccessibleRecipe;
import hellfirepvp.astralsorcery.common.crafting.helper.ShapedRecipeSlot;
import hellfirepvp.astralsorcery.common.lib.ItemsAS;
import hellfirepvp.astralsorcery.common.tile.TileAltar;
import hubry.huesoaddons.common.recipe.BasicRecipe;
import hubry.huesoaddons.common.recipe.LinkBuilder;
import hubry.huesoaddons.common.recipe.ParamList;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class BaseAltarRecipe extends BasicRecipe<AbstractAltarRecipe> {
	private final TileAltar.AltarLevel level;

	BaseAltarRecipe(String name, TileAltar.AltarLevel level) {
		super(name);
		this.level = level;
	}

	@Nonnull
	@Override
	public List<AbstractAltarRecipe> gatherRecipes(ItemStack stack) {
		List<AbstractAltarRecipe> list = new ArrayList<>();
		for (AbstractAltarRecipe recipe : AltarRecipeRegistry.recipes.get(level)) {
			if (recipe.getOutputForMatching().isItemEqual(stack)) {
				list.add(recipe);
			}
		}
		return list;
	}

	@Override
	protected ParamList getParameters(AbstractAltarRecipe recipe) {
		ParamList list = new ParamList();

		AccessibleRecipe r = recipe.getNativeRecipe();
		for (ShapedRecipeSlot slot : ShapedRecipeSlot.values()) {
			ItemHandle handle = r.getExpectedStackHandle(slot);
			addHandle(list, slot.getSlotID(), handle);
		}

		if (recipe instanceof AttunementRecipe) {
			for (AttunementRecipe.AttunementAltarSlot slot : AttunementRecipe.AttunementAltarSlot.values()) {
				ItemHandle handle = ((AttunementRecipe) recipe).getAttItemHandle(slot);
				addHandle(list, slot.getSlotId(), handle);
			}
		}

		if (recipe instanceof ConstellationRecipe) {
			for (ConstellationRecipe.ConstellationAtlarSlot slot : ConstellationRecipe.ConstellationAtlarSlot.values()) {
				ItemHandle handle = ((ConstellationRecipe) recipe).getCstItemHandle(slot);
				addHandle(list, slot.getSlotId(), handle);
			}
		}

		if (recipe instanceof TraitRecipe) {
			TraitRecipe traitRecipe = (TraitRecipe) recipe;
			for (TraitRecipe.TraitRecipeSlot slot : TraitRecipe.TraitRecipeSlot.values()) {
				ItemHandle handle = traitRecipe.getInnerTraitItemHandle(slot);
				addHandle(list, slot.getSlotId(), handle);
			}
			List<ItemHandle> traitItemHandles = traitRecipe.getTraitItemHandles();
			for (int i = 0; i < traitItemHandles.size(); i++) {
				ItemHandle handle = traitItemHandles.get(i);
				list.add("R" + (i + 1), AstralModule.outputItemHandle(handle));
			}
			if (traitRecipe.getRequiredConstellation() != null) {
				list.add("focus", new LinkBuilder(new ItemStack(ItemsAS.tunedCelestialCrystal))
						.setDesc("&7Tuned to " + I18n.format(traitRecipe.getRequiredConstellation().getUnlocalizedName()))
						.toString());
			}
		}

		list.output("O", recipe.getOutputForMatching());
		list.add("starlight", String.valueOf(recipe.getPassiveStarlightRequired()));
		return list;
	}

	private static void addHandle(ParamList list, int slotID, @Nullable ItemHandle handle) {
		if (handle != null && handle != ItemHandle.EMPTY)
			list.add(String.valueOf(slotID + 1), AstralModule.outputItemHandle(handle));
	}

}
