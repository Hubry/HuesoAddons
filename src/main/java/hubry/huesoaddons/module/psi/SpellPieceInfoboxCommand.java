package hubry.huesoaddons.module.psi;

import com.google.common.base.Stopwatch;
import hubry.huesoaddons.HuesoAddons;
import hubry.huesoaddons.common.recipe.ParamList;
import net.minecraft.client.resources.I18n;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellPiece;
import xbony2.huesodewiki.Utils;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class SpellPieceInfoboxCommand extends CommandBase {
	@Override
	public String getName() {
		return "spellinfobox";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "commands.huesoaddons.spellinfobox.usage";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (args.length != 1) {
			throw new WrongUsageException(getUsage(sender));
		}

		Class<? extends SpellPiece> spellClass = PsiAPI.spellPieceRegistry.getObject(args[0]);
		if (spellClass == null) {
			throw new WrongUsageException(getUsage(sender));
		}

		Stopwatch stopwatch = Stopwatch.createStarted();
		ParamList params = new ParamList();

		try {
			SpellPiece piece = SpellPiece.create(spellClass, PieceCostCalculator.mockSpell);

			String modid = PsiAPI.pieceMods.getOrDefault(spellClass, "psi");
			String pieceName = I18n.format(piece.getUnlocalizedName());

			params.add("name", pieceName);
			params.add("imageicon", "{{Gc|mod=" + Utils.getModAbbrevation(modid) + "|link=none|no-bg=true|" + pieceName + "}}");
			params.add("mod", Utils.getModName(modid));
			params.add("type", piece.getPieceType().name().toLowerCase(Locale.ROOT));
			params.add("group", I18n.format(PsiAPI.groupsForPiece.get(spellClass).getUnlocalizedName()));

			Class<?> evaluationType = piece.getEvaluationType();
			if (evaluationType != SpellPiece.Null.class) {
				params.add("returns", piece.getEvaluationTypeString());
			}

			PieceCostCalculator.Result costs = PieceCostCalculator.calculate(piece.copy());
			costs.fillParams(params);
			if (costs.hasFailed()) {
				params.add("CHECK MOD SOURCE CODE FOR CALCULATIONS! Class name: " + spellClass.getName());
			}

			if (!piece.params.isEmpty()) {
				int i = 1;
				for (SpellParam param : piece.params.values()) {
					String paramName = I18n.format(param.name);
					if (param.canDisable)
						paramName += " (optional)";
					params.add("param" + i + "name", paramName);
					params.add("param" + i + "type", param.getRequiredTypeString());
					i++;
				}
			}
			params.add("registryname", piece.registryKey);
			Utils.copyString("{{Infobox spellpiece\n" + params.toString() + "}}\n");
			String url = "https://ftb.gamepedia.com/index.php?title=" + pieceName.replace(' ', '_') + "&action=edit";

			ITextComponent link = new TextComponentString(pieceName);
			link.setStyle(new Style().setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url)).setColor(TextFormatting.BLUE).setUnderlined(true));

			HuesoAddons.LOGGER.info("Created infobox in {}", stopwatch);
			sender.sendMessage(new TextComponentTranslation("commands.huesoaddons.spellinfobox.success", link));
			if (costs.hasFailed()) {
				sender.sendMessage(new TextComponentTranslation("commands.huesoaddons.spellinfobox.placeholders").setStyle(new Style().setColor(TextFormatting.GREEN)));
			}
		} catch (Exception e) {
			HuesoAddons.LOGGER.catching(e);
			throw new CommandException("commands.huesoaddons.spellinfobox.error");
		}
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
		if (args.length == 1) {
			return CommandBase.getListOfStringsMatchingLastWord(args, PsiAPI.spellPieceRegistry.getKeys());
		}
		return Collections.emptyList();
	}
}
