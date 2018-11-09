package hubry.huesoaddons.common.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import xbony2.huesodewiki.Utils;

public class LinkBuilder {
	private String displayName;
	private String mod;
	private String overrideTitle;
	private String overrideLink;
	private int amount = 1;
	private boolean dis = false;
	private String description;

	public LinkBuilder(ItemStack stack) {
		this.displayName = stack.getDisplayName();
		this.mod = Utils.getModAbbrevation(stack);
		setAmount(stack.getCount());
	}

	public LinkBuilder(String displayName, String modid) {
		this.displayName = displayName;
		this.mod = Utils.getModAbbrevation(Utils.getModName(modid));
	}

	public LinkBuilder setAmount(int amount) {
		this.amount = amount;
		return this;
	}

	public LinkBuilder setOverrideLink(String overrideLink) {
		this.overrideLink = overrideLink;
		return this;
	}

	public LinkBuilder setTitle(String overrideTitle) {
		this.overrideTitle = overrideTitle;
		return this;
	}

	public LinkBuilder setDis(boolean dis) {
		this.dis = dis;
		return this;
	}

	public LinkBuilder setDesc(String description) {
		this.description = description;
		return this;
	}

	public LinkBuilder setDesc(TextFormatting style, String description) {
		return setDesc("&" + style.toString().substring(1) + description);
	}

	public LinkBuilder setNoLink() {
		return setOverrideLink("none");
	}

	@Override
	public String toString() {
		StringBuilder b = new StringBuilder("{{Gc");
		b.append("|mod=").append(mod);
		b.append('|').append(displayName);

		if (amount > 1)
			b.append('|').append(amount);
		if (overrideTitle != null)
			b.append("|name=").append(overrideTitle);
		if (description != null)
			b.append("|desc=").append(description);

		if (overrideLink != null)
			b.append("|link=").append(overrideLink);
		else if (!dis)
			b.append("|dis=false");

		return b.append("}}").toString();
	}
}
