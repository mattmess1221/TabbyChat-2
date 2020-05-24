package mnm.mods.tabbychat.util.config

@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class Comment(val value: String)