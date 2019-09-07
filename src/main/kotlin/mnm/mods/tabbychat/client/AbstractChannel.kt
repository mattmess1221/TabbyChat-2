package mnm.mods.tabbychat.client;

import com.google.common.base.Strings;
import mnm.mods.tabbychat.api.Channel;
import net.minecraft.util.StringUtils;

public abstract class AbstractChannel implements Channel {

    private String alias;

    private String prefix = "";
    private boolean prefixHidden = false;

    private String command = "";

    public AbstractChannel(String name) {
        this.alias = name;
    }

    /**
     * Gets the alias that is displayed on the channel tab.
     *
     * @return The alias
     */
    public String getAlias() {
        return this.alias;
    }

    /**
     * Sets the alias that is displayed on the channel tab.
     *
     * @param alias The new alias
     */
    public void setAlias(String alias) {
        this.alias = alias;
    }

    /**
     * Gets the prefix that is inserted before the input.
     *
     * @return The prefix
     */
    public String getPrefix() {
        return Strings.nullToEmpty(this.prefix);
    }

    /**
     * Sets the prefix that is inserted before the input.
     *
     * @param prefix The new prefix
     */
    public void setPrefix(String prefix) {
        this.prefix = StringUtils.stripControlCodes(prefix);
    }

    /**
     * Gets whether the prefix is hidden or not. A hidden prefix will not be
     * displayed while typing. Defaults to false.
     *
     * @return True if the prefix is hidden
     */
    public boolean isPrefixHidden() {
        return this.prefixHidden;
    }

    /**
     * Sets whether the prefix is hidden or not. A hidden prefix will not be
     * displayed while typing.
     *
     * @param hidden Whether to hide the prefix
     */
    public void setPrefixHidden(boolean hidden) {
        this.prefixHidden = hidden;
    }

    public String getCommand() {
        return Strings.nullToEmpty(command);
    }

    public void setCommand(String command) {
        this.command = command;
    }

}
