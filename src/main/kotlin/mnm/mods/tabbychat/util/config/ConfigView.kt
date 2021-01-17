package mnm.mods.tabbychat.util.config

import com.electronwill.nightconfig.core.Config
import com.electronwill.nightconfig.core.EnumGetMethod
import kotlin.reflect.KProperty

abstract class AbstractConfigView {
    abstract val config: Config
    abstract val path: List<String>

    private val specs = ArrayList<AbstractSpec<*, *>>()

    internal fun getRaw(path: String) = getRaw(listOf(path))
    internal fun getRaw(path: List<String>): Any? = config.get(this.path + path)
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

class DelegateProvider<R>(private val provider: (thisRef: Any, prop: KProperty<*>) -> R) {
    operator fun provideDelegate(thisRef: Any, prop: KProperty<*>) = provider(thisRef, prop)
}
