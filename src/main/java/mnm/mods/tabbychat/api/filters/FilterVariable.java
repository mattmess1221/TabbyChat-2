package mnm.mods.tabbychat.api.filters;

import java.util.regex.Pattern;

/**
 * Interface for getting a variable for use in a {@link Filter}'s pattern. It
 * can be used in a pattern by putting the name between <code>${</code> and
 * <code>}</code>. If the variable does not exist, an empty string is used
 * instead.
 * <p>
 * <b>Example:</b> <code>${player}</code> - the current player's name.
 */
public interface FilterVariable {

    /**
     * A FilterVariable that returns a constant empty string.
     */
    public static final FilterVariable NULL = new FilterConstant("");

    /**
     * Gets the value for this variable.
     *
     * @return A regex-compatible variable value.
     */
    String getVar();

    /**
     * A FilterVariable that returns a constant value.
     */
    public static final class FilterConstant implements FilterVariable {

        private final String constant;

        public FilterConstant(String constant) {
            this.constant = Pattern.quote(constant);
        }

        @Override
        public String getVar() {
            return constant;
        }
    }

}
