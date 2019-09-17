package mnm.mods.tabbychat.util.config

import com.google.common.reflect.TypeToken
import kotlin.properties.Delegates
import kotlin.reflect.KProperty

sealed class AbstractValue<T> {
    abstract val type: TypeToken<T>?
    abstract val value: T

    override fun toString(): String {
        return value.toString()
    }
}

class Value<T>(
        override val type: TypeToken<T>,
        val default: () -> T) : AbstractValue<T>() {

    private val listeners = mutableListOf<(T) -> Unit>()

    override var value by Delegates.observable(default()) { _, _, newValue ->
        listeners.forEach { it(newValue) }
    }

    fun listen(callback: (T) -> Unit) = listeners.add(callback)

    fun reset() {
        value = default()
    }
}

class ValueList<T>(
        override val type: TypeToken<MutableList<T>>,
        override val value: MutableList<T> = mutableListOf()
) : AbstractValue<MutableList<T>>(), MutableList<T> by value

class ValueMap<T>(
        override val type: TypeToken<MutableMap<String, T>>,
        override val value: MutableMap<String, T> = mutableMapOf()
) : AbstractValue<MutableMap<String, T>>(), MutableMap<String, T> by value

abstract class ValueObject<T : ValueObject<T>> : AbstractValue<T>() {
    @Suppress("UNCHECKED_CAST")
    override val type: TypeToken<T> = TypeToken.of(this.javaClass) as TypeToken<T>
    @Suppress("UNCHECKED_CAST")
    override val value: T
        get() = this as T

    internal val properties: MutableMap<String, AbstractValue<out Any>> = mutableMapOf()

    protected inline fun <reified T : Any> value(type: TypeToken<T> = typeToken(), noinline default: () -> T) = ValueProvider {
        Value(type, default)
    }

    protected inline fun <reified T : Any> list(type: TypeToken<MutableList<T>>) = ValueProvider {
        ValueList(type)
    }

    protected inline fun <reified T> map(type: TypeToken<MutableMap<String, T>>) = ValueProvider {
        ValueMap(type)
    }

    protected inline fun <reified T : ValueObject<T>> obj(noinline default: () -> T) = ValueProvider(default)

    protected inline fun <reified T> typeToken() = object : TypeToken<T>() {}
}

class ValueProvider<V : AbstractValue<T>, T : Any>(val factory: () -> V) {
    operator fun provideDelegate(thisRef: ValueObject<*>, prop: KProperty<*>): ValueDelegate<V, T> {
        thisRef.properties.getOrPut(prop.name) {
            factory()
        }
        return ValueDelegate()
    }

}


class ValueDelegate<V : AbstractValue<T>, T : Any> {

    @Suppress("UNCHECKED_CAST")
    operator fun getValue(thisRef: ValueObject<*>, property: KProperty<*>): V {
        return thisRef.properties[property.name] as V
    }
}
