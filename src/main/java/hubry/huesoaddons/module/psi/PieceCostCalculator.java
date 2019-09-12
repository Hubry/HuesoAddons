package hubry.huesoaddons.module.psi;

import hubry.huesoaddons.HuesoAddons;
import hubry.huesoaddons.common.recipe.ParamList;
import net.minecraft.client.resources.I18n;
import vazkii.psi.api.spell.EnumSpellStat;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellCompilationException;
import vazkii.psi.api.spell.SpellMetadata;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellPiece;
import vazkii.psi.common.spell.constant.PieceConstantNumber;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

/**
 * A somewhat dumb way to mock and determine spell cost calculations.
 * It does not account for params other than numbers, exclusive params (like in the
 * {@linkplain vazkii.psi.common.spell.trick.PieceTrickSwitchTargetSlot target slot switching trick}),
 * or tricks with quite limited input values.
 * At this moment it can only work with constant values and linear increases dependent on one argument.
 */
public final class PieceCostCalculator {

	static final Spell mockSpell = new Spell();

	/**
	 * Number parameters set up around the tested spell piece.
	 * The ordering is the same as in {@link SpellParam.Side#DIRECTIONS} - top, bottom, left, right.
	 */
	private static final PieceConstantNumber[] numberParams = new PieceConstantNumber[4];

	static {
		mockSpell.name = "Mock spell";
		for (int i = 0; i < 4; i++) {
			SpellParam.Side side = SpellParam.Side.DIRECTIONS[i];

			PieceConstantNumber number = new PieceConstantNumber(mockSpell);
			number.isInGrid = true; // for good measure

			mockSpell.grid.gridData[1 + side.offx][1 + side.offy] = number;
			numberParams[i] = number;
		}
		reset();
	}

	/**
	 * Calculates the spell stats as much as possible. If it fails early due to spell
	 * compilation errors, it will return an unknown result.
	 *
	 * @param piece Spell piece to calculate the stats of
	 * @return Object with calculations formatted for the wiki.
	 */
	public static Result calculate(SpellPiece piece) {
		mockSpell.grid.gridData[1][1] = piece;
		piece.x = piece.y = 1;

		try {
			int index = 0;
			for (SpellParam value : piece.params.values()) {
				if (value.canAccept(numberParams[index])) {
					piece.paramSides.put(value, SpellParam.Side.DIRECTIONS[index]);
				}
			}
			Set<SpellParam.Side> usedSides = EnumSet.noneOf(SpellParam.Side.class);

			SpellMetadata initialMeta = new SpellMetadata();
			piece.addToMetadata(initialMeta);
			Map<EnumSpellStat, Integer> initialEntries = initialMeta.stats;

			sides:
			for (SpellParam.Side side : piece.paramSides.values()) {
				if (side == SpellParam.Side.OFF)
					continue;
				PieceConstantNumber number = getNumberParam(side);

				for (int i = 1; i < 5; i++) {
					setNumber(number, i);
					SpellMetadata meta = new SpellMetadata();
					piece.addToMetadata(meta);
					reset();

					for (Map.Entry<EnumSpellStat, Integer> entry : meta.stats.entrySet()) {
						if (entry.getValue().intValue() != initialEntries.get(entry.getKey())) {
							usedSides.add(side);
							continue sides;
						}
					}
				}
			}
			switch (usedSides.size()) {
				case 0:
					int complexity = initialEntries.get(EnumSpellStat.COMPLEXITY);
					int potency = initialEntries.get(EnumSpellStat.POTENCY);
					int cost = initialEntries.get(EnumSpellStat.COST);
					return new Result(complexity, potency, cost);
				case 1:
					return calculateOne(piece, usedSides.iterator().next());
				default:
					return Result.fail();
			}

		} catch (Exception e) { // It's not *that* important to print out the stacktrace for this
			HuesoAddons.LOGGER.warn("Failed to calculate spell stats\n\t{}", e.toString());
		} finally {
			mockSpell.grid.gridData[1][1] = null;
		}
		return Result.fail();
	}

	private static Result calculateOne(SpellPiece piece, SpellParam.Side side) throws SpellCompilationException {
		PieceConstantNumber number = getNumberParam(side);
		SpellMetadata[] metas = new SpellMetadata[5];
		for (int i = 1; i <= 5; i++) {
			setNumber(number, i);
			SpellMetadata meta = new SpellMetadata();
			metas[i - 1] = meta;
			piece.addToMetadata(meta);
		}
		SpellParam param = null;
		for (Map.Entry<SpellParam, SpellParam.Side> entry : piece.paramSides.entrySet()) {
			if (entry.getValue() == side) {
				param = entry.getKey();
				break;
			}
		}
		if (param == null) throw new NullPointerException("Param is null?");
		String name = I18n.format(param.name);
		String complexity = calcValue(Arrays.stream(metas).mapToInt(m -> m.stats.get(EnumSpellStat.COMPLEXITY)).toArray(), name);
		String potency = calcValue(Arrays.stream(metas).mapToInt(m -> m.stats.get(EnumSpellStat.POTENCY)).toArray(), name);
		String cost = calcValue(Arrays.stream(metas).mapToInt(m -> m.stats.get(EnumSpellStat.COST)).toArray(), name);

		return new Result(complexity, potency, cost);
	}

	@Nullable
	private static String calcValue(int[] ints, String usedParam) {
		for (Formula1 formula : Formula1.values()) {
			if (formula.canApply(ints))
				return formula.map(ints, usedParam);
		}
		return null;
	}

	private static String resultWrap(int value) {
		return value == 0 ? "" : String.valueOf(value);
	}

	private static PieceConstantNumber getNumberParam(SpellParam.Side side) {
		return numberParams[side.asInt() - 1];
	}

	// 1 is probably the safest value as some spells don't like zeroes.
	private static void reset() {
		for (PieceConstantNumber number : numberParams) {
			setNumber(number, 1);
		}
	}

	private static void setNumber(PieceConstantNumber piece, int value) {
		piece.valueStr = String.valueOf(value);
	}

	/**
	 * A basic checker for mathematical formulas with one argument.
	 */
	//TODO quadratic?
	public enum Formula1 {
		CONSTANT {
			@Override
			public boolean canApply(int[] ints) {
				int i = ints[0];
				return Arrays.stream(ints).allMatch(j -> j == i);
			}

			@Override
			public String map(int[] ints, String paramName) {
				return String.valueOf(ints[0]);
			}
		},
		LINEAR {
			@Override
			public boolean canApply(int[] ints) {
				int step = ints[1] - ints[0];
				for (int i = 2; i < ints.length; i++) {
					if (ints[i] - ints[i - 1] != step)
						return false;
				}
				return true;
			}

			@Override
			public String map(int[] ints, String paramName) {
				int step = ints[1] - ints[0];
				int remainder = ints[0] - step;
				String ret = step + "&times;" + paramName;
				if (step == 1) {
					ret = paramName;
				}
				if (remainder == 0) {
					return ret;
				} else {
					return ret + " + " + remainder;
				}
			}
		};

		public abstract boolean canApply(int[] ints);

		public abstract String map(int[] ints, String paramName);
	}

	public static class Result {
		private static final String FAIL_MESSAGE = " Failed to calculate spell stat!";

		final String complexity;
		final String potency;
		final String cost;

		Result(int complexity, int potency, int cost) {
			this(resultWrap(complexity), resultWrap(potency), resultWrap(cost));
		}

		/**
		 * If param is null, it failed. If a param if empty, it's a 0.
		 */
		Result(@Nullable String complexity, @Nullable String potency, @Nullable String cost) {
			this.complexity = complexity;
			this.potency = potency;
			this.cost = cost;
		}

		static Result fail() {
			return new Result(null, null, null);
		}

		public boolean hasFailed() {
			return complexity == null
					|| potency == null
					|| cost == null;
		}

		public void fillParams(ParamList list) {
			putParam(list, complexity, "complexity");
			putParam(list, potency, "potency");
			putParam(list, cost, "cost");
		}

		private static void putParam(ParamList list, @Nullable String value, String name) {
			if (!"".equals(value))
				list.add(name, value != null ? value : FAIL_MESSAGE);
		}

	}
}
