package hubry.huesoaddons.module.psi;

import javax.annotation.Nullable;
import java.util.Arrays;

public class Formula {

	/**
	 * Try to determine the formula for a single argument function
	 * Note: doesn't really work where doubles are used, because spell meta can use doubles for calculations,
	 * but the precision loss is hard to reverse.
	 *
	 * @param ints      Values for the function, offset by 1 (i.e. ints[0] is the value for f(1))
	 * @param usedParam Name for the parameter
	 * @return Formula in string form, formatted for wiki use
	 */
	@Nullable
	static String calc(int[] ints, String usedParam) {
		for (Unary formula : Unary.values()) {
			String result = formula.maybeMap(ints, usedParam);
			if (result != null) {
				return result;
			}
		}
		return null;
	}

	public enum Unary {
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
		/** ax + b */
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
				int a = ints[1] - ints[0];
				int b = ints[0] - a;
				String ret = a + "&times;" + paramName;
				if (a == 1) {
					ret = paramName;
				}
				if (b == 0) {
					return ret;
				} else {
					return ret + " + " + b;
				}
			}
		},
		/** ax^2 + b */
		QUADRATIC_SIMPLE {
			@Override
			public boolean canApply(int[] ints) {
				return maybeMap(ints, "") != null;
			}

			@Override
			public String map(int[] ints, String paramName) {
				//noinspection ConstantConditions
				return maybeMap(ints, paramName);
			}

			@Nullable
			@Override
			public String maybeMap(int[] ints, String paramName) {
				int a = (ints[1] - ints[0]) / 3;
				int b = ints[0] - a;
				for (int i = 0; i < ints.length; i++) {
					if (a * (i + 1) * (i + 1) + b != ints[i]) {
						return null;
					}
				}
				StringBuilder builder = new StringBuilder();
				if (a != 1)
					builder.append(a).append("&times;");
				builder.append(paramName).append("<sup>2</sup>");
				if (b != 0)
					builder.append(" + ").append(b);
				return builder.toString();
			}
		};

		public abstract boolean canApply(int[] ints);

		public abstract String map(int[] ints, String paramName);

		@Nullable
		public String maybeMap(int[] ints, String paramName) {
			if (canApply(ints)) {
				return map(ints, paramName);
			}
			return null;
		}
	}
}
