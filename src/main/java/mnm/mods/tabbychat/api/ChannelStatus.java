package mnm.mods.tabbychat.api;

/**
 * An enum for chanel statuses. A null value means the channel is currently
 * inactive. Only one status can be in effect at a time.
 * <p>
 * Setting a status with a lower priority than the current one will not take
 * effect. Priority is determined by the ordinal; a lower ordinal has a higher
 * priority. e.g. {@link #ACTIVE} has the highest priority while {@link #JOINED}
 * has the lowest.
 * <p>
 * {@code null} is special and has both highest and lowest priorities. The
 * status will always be set when the old value or new value is null.
 */
public enum ChannelStatus {

    /**
     * For the active channel.
     */
    ACTIVE,
    /**
     * For when your name is mentioned.
     */
    PINGED,
    /**
     * For unread messages.
     */
    UNREAD,
    /**
     * For when players join or leave the channel.
     */
    JOINED
}
