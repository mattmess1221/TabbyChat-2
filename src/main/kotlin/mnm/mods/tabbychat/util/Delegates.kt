package mnm.mods.tabbychat.util

import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KMutableProperty0
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty0

private open class ReadOnlyPropertyDelegate<T>(private var property: KProperty0<T>) : ReadOnlyProperty<Any, T> {
    override fun getValue(thisRef: Any, property: KProperty<*>) = this.property.get()
}

private class ReadWritePropertyDelegate<T>(private var property: KMutableProperty0<T>) : ReadOnlyPropertyDelegate<T>(property), ReadWriteProperty<Any, T> {
    override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
        this.property.set(value)
    }
}

fun <T> property(property: KProperty0<T>): ReadOnlyProperty<Any, T> = ReadOnlyPropertyDelegate(property)
fun <T> property(property: KMutableProperty0<T>): ReadWriteProperty<Any, T> = ReadWritePropertyDelegate(property)