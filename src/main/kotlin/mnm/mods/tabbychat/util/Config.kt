package mnm.mods.tabbychat.util

import com.electronwill.nightconfig.core.CommentedConfig
import com.electronwill.nightconfig.core.Config
import com.electronwill.nightconfig.core.EnumGetMethod
import com.electronwill.nightconfig.core.file.CommentedFileConfig
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.eventbus.api.Event
import java.nio.file.Files
import java.nio.file.Path
import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

abstract class AbstractConfigView {
    abstract val config: Config
    abstract val path: List<String>

    private val specs = ArrayList<AbstractSpec<*, *>>()

    internal fun getRaw(path: String) = getRaw(listOf(path))
    internal fun getRaw(path: List<String>): Any? = config.get<Any?>(this.path + path)
    internal fun setRaw(path: String, value: Any?) = this.setRaw(listOf(path), value)
    internal fun setRaw(path: List<String>, value: Any?) = config.set<Any?>(this.path + path, value)

    private fun <T : Any, R : Any> spec(default: T, builder: Spec<T, R>.() -> Unit = {}): DelegateProvider<Spec<T, R>> = DelegateProvider { _, prop ->
        Spec<T, R>(this, prop, default).apply(builder).also { specs.add(it) }
    }

    protected fun <T : Any> defining(default: T, builder: Spec<T, T>.() -> Unit = {}) = spec(default, builder)

    protected fun <T : ConfigView> childList(factory: (Config) -> T) = DelegateProvider { _, prop ->
        ConfigListSpec(this, prop, factory)
    }

    protected fun <T : Any> definingList(elementValidator: (Any?) -> Boolean = { true }, builder: Spec<List<T>, List<T>>.() -> Unit = {}) = defining(listOf<T>()) {
        validator = {
            (it as? List<*>)?.all(elementValidator) == true
        }
        builder()
    }

    protected fun <T : Enum<T>> definingEnum(default: T, cls: Class<T>, method: EnumGetMethod = EnumGetMethod.NAME, builder: Spec<T, String>.() -> Unit = {}) = spec<T, String>(default) {
        validator = {
            method.validate(it, cls)
        }
        serialize = {
            it.name
        }
        deserialize = {
            method.get(it, cls)
        }
        builder()
    }

    protected fun <T : Enum<T>> definingRestrictedEnum(default: T, cls: Class<T>, acceptable: Collection<T>, method: EnumGetMethod = EnumGetMethod.NAME, builder: Spec<T, String>.() -> Unit = {}) = spec<T, String>(default) {
        validator = {
            method.validate(it, cls) && method.get(it, cls) in acceptable
        }
        serialize = {
            it.name
        }
        deserialize = {
            method.get(it, cls)
        }
        builder()
    }

    protected fun <T : ConfigView> child(factory: (AbstractConfigView, List<String>) -> T, init: ChildSpec<T>.() -> Unit = {}) = DelegateProvider { _, prop ->
        ChildSpec(this, prop, factory).apply(init).also { specs.add(it) }
    }

    fun populateDefaults() {
        this.specs.forEach {
            it.populateDefaults()
        }
    }
}

abstract class ConfigView(override val config: Config, override val path: List<String> = emptyList()) : AbstractConfigView() {
    constructor(parent: AbstractConfigView, path: List<String>) : this(parent.config, path)
}

abstract class FileConfigView(path: Path) : AbstractConfigView() {

    override val config: CommentedFileConfig = CommentedFileConfig.of(path)
    override val path: List<String> = emptyList()

    fun save() {
        Files.createDirectories(config.nioPath.parent)
        config.save()
    }

    fun load() {
        if (Files.exists(config.nioPath)) {
            config.load()
        }
        this.populateDefaults()
        save()
    }
}

sealed class AbstractSpec<T : Any, R : Any>(
        protected val view: AbstractConfigView,
        protected val property: KProperty<*>)
    : ReadOnlyProperty<Any, T> {
    private val commentedConfig get() = view as? CommentedConfig
    val path get() = listOf(property.name)

    var comment: String?
        get() = commentedConfig?.getComment(path)
        set(value) {
            commentedConfig?.setComment(path, value)
        }

    abstract val value: T

    open fun populateDefaults() = Unit

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
            MinecraftForge.EVENT_BUS.post(ConfigEvent(property))
        }

    override fun populateDefaults() {
        if (this.getRaw() == null) {
            this.value = this.default
        }
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

class ConfigEvent(val prop: KProperty<*>) : Event()

class DelegateProvider<R>(private val provider: (thisRef: Any, prop: KProperty<*>) -> R) {
    operator fun provideDelegate(thisRef: Any, prop: KProperty<*>) = provider(thisRef, prop)
}
