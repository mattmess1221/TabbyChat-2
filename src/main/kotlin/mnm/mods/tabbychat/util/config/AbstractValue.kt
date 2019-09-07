package mnm.mods.tabbychat.util.config

/**
 * A wrapper that saves a default value.
 *
 * @param <T> The type to wrap
 */
abstract class AbstractValue<T> {

    abstract val value: T

    override fun toString(): String {
        return value.toString()
    }
}

class Value<T>(override var value: T) : AbstractValue<T>()

class ValueList<T> internal constructor(
        override val value: MutableList<T> = mutableListOf()
) : AbstractValue<MutableList<T>>(), MutableList<T> by value

class ValueMap<T> internal constructor(
        override val value: MutableMap<String, T> = mutableMapOf()
) : AbstractValue<Map<String, T>>(), MutableMap<String, T> by value

abstract class ValueObject
