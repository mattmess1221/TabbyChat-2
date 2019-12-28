package mnm.mods.tabbychat.util.config

import com.electronwill.nightconfig.core.Config
import com.electronwill.nightconfig.core.ConfigSpec
import com.electronwill.nightconfig.core.EnumGetMethod
import com.electronwill.nightconfig.core.file.CommentedFileConfig
import com.electronwill.nightconfig.core.file.FileConfig
import com.google.common.collect.ImmutableList
import java.nio.file.Files
import java.nio.file.Path
import kotlin.reflect.KProperty

abstract class AbstractConfigView(val prefix: List<String>) {
    abstract val file: FileConfigView

    internal fun <T : Any> defining(default: T, builder: Spec<T>.() -> Unit = {}) = DelegateProvider { _, prop ->
        val path = prefix.toMutableList().apply { add(prop.name) }
        val spec = Spec(file, path, default)
        spec.builder()
        file.configSpec.define(path, default, spec.validator)
        spec.value = default
        Delegate { _, _ -> spec }
    }

    internal fun <T : Any> definingInList(default: T, vararg acceptable: T, builder: Spec<T>.() -> Unit) = defining(default) {
        validator = { it in acceptable }
        builder()
    }

    internal fun <V : Comparable<V>> definingInRange(default: V, min: V, max: V, builder: Spec<V>.() -> Unit) = let {
        check(min <= max) {
            "The minimum must be less than the maximum"
        }
        defining(default) {
            validator = {
                @Suppress("UNCHECKED_CAST")
                (it as? Comparable<V>)?.let { c ->
                    c > min && c < max
                } == true
            }
            builder()
        }
    }

    internal fun <T, L : List<T>> definingList(default: L, elementValidator: (Any?) -> Boolean = { true }, builder: Spec<L>.() -> Unit = {}) = defining(default) {
        validator = { (it as? List<*>)?.all(elementValidator) == true }
        builder()
    }

    internal inline fun <reified T : Enum<T>> definingEnum(default: T, method: EnumGetMethod = EnumGetMethod.NAME, noinline builder: Spec<T>.() -> Unit = {}) = defining(default) {
        validator = { method.validate(it, T::class.java) }
        getter = { c, p -> c.getEnum(p, T::class.java) }
        builder()
    }

    internal inline fun <reified T : Enum<T>> definingRestrictedEnum(default: T, acceptable: Collection<T>, method: EnumGetMethod = EnumGetMethod.NAME, noinline builder: Spec<T>.() -> Unit = {}) = defining(default) {
        validator = { method.validate(it, T::class.java) && method.get(it, T::class.java) in acceptable }
        getter = { c, p -> c.getEnum(p, T::class.java) }
        builder()
    }

    internal fun <T : ConfigView> child(supplier: (config: FileConfigView, prefix: List<String>) -> T, comment: () -> String? = { null }) = DelegateProvider { _, prop ->
        val prefix = prefix.toMutableList().apply { add(prop.name) }
        file.config.setComment(prefix, comment())
        val s = supplier(file, prefix)
        Delegate { _, _ -> s }
    }

    internal fun <T : Any> mapChild(getter: (Config, String) -> T = {c, p -> c.get<T>(p)}) = DelegateProvider { _, prop ->
        val default = file.config.createSubConfig()
        val path = prefix.toMutableList().also {it.add(prop.name)}
        file.config.set<Config>(path, default)
        val view = MappedConfigView(file, path, getter)
        Delegate { _, _ -> view }
    }
}

abstract class ConfigView(override val file: FileConfigView, prefix: List<String>) : AbstractConfigView(prefix)

class MappedConfigView<T : Any>(file: FileConfigView, prefix: List<String>, val getter: (Config, String) -> T) : ConfigView(file, prefix) {
    val config get() = file.config.get<Config>(prefix)

    operator fun get(path: String) = getter(config, path)

    operator fun set(path: String, value: T) {
        config.set<T>(path, value)
    }
}

abstract class FileConfigView(path: Path) : AbstractConfigView(listOf()) {

    override val file: FileConfigView get() = this

    val config: CommentedFileConfig = CommentedFileConfig.of(path)
    val configSpec = ConfigSpec()

    fun save() {
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

class Spec<T : Any>(val file: FileConfigView, path: List<String>, val default: T) {

    val path: List<String> = ImmutableList.copyOf(path)

    var validator: (Any?) -> Boolean = {
        it != null && default.javaClass.isAssignableFrom(it.javaClass)
    }

    var getter: (FileConfig, List<String>) -> T = { config, path ->
        config.get(path) ?: default
    }
    var setter: (FileConfig, List<String>, value: T) -> Unit = { config, path, value ->
        config.set<T>(path, value)
    }
    var comment: String?
        get() = file.config.getComment(path)
        set(value) {
            file.config.setComment(path, value)
        }

    var value: T
        get() = getter(file.config, path)
        set(value) {
            check(file.configSpec.isCorrect(path, value)) {
                "config ${path.joinToString(".")} check failed."
            }
            setter(file.config, path, value)
            listeners.forEach { it(value) }
        }

    private val listeners = mutableListOf<(T) -> Unit>()

    fun listen(listener: (T) -> Unit) {
        listeners += listener
    }

    operator fun getValue(thisRef: Any, prop: KProperty<*>): T {
        return value
    }

    operator fun setValue(thisRef: Any, prop: KProperty<*>, value: T) {
        this.value = value
    }
}

class DelegateProvider<R>(private val provider: (thisRef: Any, prop: KProperty<*>) -> Delegate<R>) {
    operator fun provideDelegate(thisRef: Any, prop: KProperty<*>) = provider(thisRef, prop)
}

class Delegate<R>(private val delegate: (thisRef: Any, prop: KProperty<*>) -> R) {
    operator fun getValue(thisRef: Any, prop: KProperty<*>) = delegate(thisRef, prop)
}
