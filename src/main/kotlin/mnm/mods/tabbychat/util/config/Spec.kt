package mnm.mods.tabbychat.util.config

import com.electronwill.nightconfig.core.CommentedConfig
import com.electronwill.nightconfig.core.Config
import thedarkcolour.kotlinforforge.forge.FORGE_BUS
import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty
import kotlin.reflect.full.findAnnotation

sealed class AbstractSpec<T : Any, R : Any>(
        protected val view: AbstractConfigView,
        protected val property: KProperty<*>)
    : ReadOnlyProperty<Any, T> {
    val path get() = listOf(property.name)

    abstract val value: T

    open fun populateDefaults() {
        val config = view.config as? CommentedConfig
        if (config != null) {
            val comment = property.findAnnotation<Comment>()?.value
            config.setComment(view.path + path, comment)
        }
    }

    @Suppress("UNCHECKED_CAST")
    protected fun getRaw(): R? {
        return view.getRaw(this.path) as R?
    }

    protected fun setRaw(value: R?) {
        view.setRaw(this.path, value)
    }

    override operator fun getValue(thisRef: Any, property: KProperty<*>): T {
        return value
    }
}

open class Spec<T : Any, R : Any>(
        config: AbstractConfigView,
        property: KProperty<*>,
        val default: T)
    : AbstractSpec<T, R>(config, property), ReadWriteProperty<Any, T> {

    var validator: (R?) -> Boolean = { it != null && default.javaClass.isAssignableFrom(it.javaClass) }

    @Suppress("UNCHECKED_CAST")
    var deserialize: (R?) -> T = { it as? T ?: default }

    @Suppress("UNCHECKED_CAST")
    var serialize: (T) -> R? = { it as R? }

    override var value: T
        get() = deserialize(getRaw())
        set(value) {
            setRaw(serialize(value))
            FORGE_BUS.post(ConfigEvent(property))
        }

    override fun populateDefaults() {
        if (this.getRaw() == null) {
            this.value = this.default
        }
        super.populateDefaults()
    }

    override operator fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
        this.value = value
    }
}

class ChildSpec<T : ConfigView>(
        config: AbstractConfigView,
        property: KProperty<*>,
        private val factory: (AbstractConfigView, List<String>) -> T)
    : AbstractSpec<T, Config>(config, property) {

    override val value: T get() = factory(view, view.path + this.path)

    override fun populateDefaults() {
        value.populateDefaults()
        super.populateDefaults()
    }
}

class ConfigListSpec<T : ConfigView>(
        config: AbstractConfigView,
        property: KProperty<*>,
        private val factory: (Config) -> T)
    : AbstractSpec<MutableList<T>, MutableList<Config>>(config, property) {

    override val value: MutableList<T>
        get() {
            val raw = getRaw() ?: emptyList<Any>()
            val list = raw.filterIsInstance<Config>()
            if (raw.size != list.size) {
                setRaw(list.toMutableList())
            }
            return ConfigListImpl(getRaw() ?: arrayListOf())
        }

    private inner class ConfigListImpl(val list: MutableList<Config>) : AbstractMutableList<T>() {

        override val size: Int
            get() = list.size

        override fun get(index: Int) = factory(list[index]).also { setRaw(list) }
        override fun add(index: Int, element: T) = list.add(index, element.config).also { setRaw(list) }
        override fun set(index: Int, element: T) = factory(list.set(index, element.config)).also { setRaw(list) }
        override fun removeAt(index: Int) = factory(list.removeAt(index)).also { setRaw(list) }

    }
}