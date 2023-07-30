package de.md5lukas.waypoints.lang

import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import org.jetbrains.annotations.VisibleForTesting

class Translation(
    private val translationLoader: TranslationLoader,
    private val key: String,
    private val prefix: Translation? = null,
    private val miniMessage: MiniMessage = MiniMessage.miniMessage()
) : AbstractTranslation {

  init {
    translationLoader.registerTranslationWrapper(this)
  }

  val text: Component
    get() = staticComponent()

  val rawText: String
    get() = translationLoader[key]

  private var staticMessage: Component? = null

  private fun staticComponent(): Component {
    if (staticMessage === null) {
      staticMessage = prependPrefix(miniMessage.deserialize(rawText))
    }
    return staticMessage!!
  }

  fun withReplacements(vararg resolvers: TagResolver): Component =
      if (resolvers.isEmpty()) {
        staticComponent()
      } else {
        prependPrefix(miniMessage.deserialize(rawText, *resolvers))
      }

  fun send(audience: Audience) {
    audience.sendMessage(staticComponent())
  }

  fun send(audience: Audience, vararg resolvers: TagResolver) {
    audience.sendMessage(withReplacements(*resolvers))
  }

  private fun prependPrefix(component: Component) =
      if (prefix === null) {
        component
      } else {
        prefix.text.append(component)
      }

  override fun reset() {
    staticMessage = null
  }

  @VisibleForTesting override fun getKeys() = arrayOf(key)
}
