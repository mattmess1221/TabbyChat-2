package mnm.mods.tabbychat.util

import com.electronwill.nightconfig.core.CommentedConfig
import com.electronwill.nightconfig.core.Config
import com.electronwill.nightconfig.core.ConfigSpec
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
    private val configSpec = ConfigSpec()

    protected fun <T : Any> defining(default: T, builder: Spec<T>.() -> Unit = {}) = DelegateProvider { _, prop ->
        val spec = Spec(config, prop, default)
        spec.builder()
        configSpec.define(prop.name, default, spec.validator)
        spec
    }

    protected fun <T : ConfigView> definingChildList(factory: (Config) -> T, builder: Spec<List<T>>.() -> Unit = {}) = definingList<T> {
        getter = { c, p ->
            (c.get<List<Config>>(p) ?: listOf()).map(factory)
        }
        setter = { c, p, v ->
            c.set(p, v.map { it.config })
        }
        builder()
    }

    protected fun <T> definingList(elementValidator: (Any?) -> Boolean = { true }, builder: Spec<List<T>>.() -> Unit = {}) = defining(listOf<T>()) {
        validator = { (it as? List<*>)?.all(elementValidator) == true }
        builder()
    }

    protected inline fun <reified T : Enum<T>> definingEnum(default: T, method: EnumGetMethod = EnumGetMethod.NAME, noinline builder: Spec<T>.() -> Unit = {}) = defining(default) {
        validator = { method.validate(it, T::class.java) }
        getter = { c, p -> c.getEnum(p, T::class.java) ?: default }
        builder()
    }

    protected inline fun <reified T : Enum<T>> definingRestrictedEnum(default: T, acceptable: Collection<T>, method: EnumGetMethod = EnumGetMethod.NAME, noinline builder: Spec<T>.() -> Unit = {}) = defining(default) {
        validator = { method.validate(it, T::class.java) && method.get(it, T::class.java) in acceptable }
        getter = { c, p -> c.getEnum(p, T::class.java) ?: default}
        builder()
    }

    protected fun <T : ConfigView> child(supplier: (config: Config) -> T, comment: () -> String? = { null }): DelegateProvider<ReadOnlyProperty<Any, T>> = DelegateProvider { _, prop ->
        (config as? CommentedFileConfig)?.setComment(prop.name, comment())
        val s = supplier(config)
        object : ReadOnlyProperty<Any, T> {
            override operator fun getValue(thisRef: Any, property: KProperty<*>) = s
        }
    }
}

abstract class ConfigView(override val config: Config) : AbstractConfigView()

abstract class FileConfigView(path: Path) : AbstractConfigView() {

    override val config: CommentedFileConfig = CommentedFileConfig.of(path)
    private val configSpec = ConfigSpec()

    fun save() {
        Files.createDirectories(config.nioPath.parent)
        config.save()
    }

    fun load() {
        if (Files.exists(config.nioPath)) {
            config.load()
            configSpec.correct(config)
        }
        save()
    }
}

class Spec<T : Any>(private val config: Config, private val property: KProperty<*>, private val default: T) : ReadWriteProperty<Any, T> {

    private val commentedConfig get() = config as? CommentedConfig
    val name get() = property.name

    var validator: (Any?) -> Boolean = {
        it != null && default.javaClass.isAssignableFrom(it.javaClass)
    }

    var getter: (Config, String) -> T = { config, path ->
        config.get(path) ?: default
    }
    var setter: (Config, String, value: T) -> Unit = { config, path, value ->
        config.set<T>(path, value)
    }
    var comment: String?
        get() = commentedConfig?.getComment(name)
        set(value) {
            commentedConfig?.setComment(name, value)
        }

    var value: T
        get() = getter(config, name)
        set(value) {
            check(validator(value)) {
                "config $name check failed."
            }
            setter(config, name, value)
            MinecraftForge.EVENT_BUS.post(ConfigEvent(property))
        }

    override operator fun getValue(thisRef: Any, property: KProperty<*>): T {
        return value
    }

    override operator fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
        this.value = value
    }
}

class ConfigEvent(val prop: KProperty<*>) : Event()

class DelegateProvider<R>(private val provider: (thisRef: Any, prop: KProperty<*>) -> R) {
    operator fun provideDelegate(thisRef: Any, prop: KProperty<*>) = provider(thisRef, prop)
}
