package core.game.interaction

import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.node.scenery.Scenery

/**
 * Manages and dispatches entity interaction plugins.
 */
object PluginInteractionManager {
    private val npcInteractions = HashMap<Int, PluginInteraction>()
    private val objectInteractions = HashMap<Int, PluginInteraction>()
    private val useWithInteractions = HashMap<Int, PluginInteraction>()
    private val groundItemInteractions = HashMap<Int, PluginInteraction>()

    /**
     * Registers an interaction plugin for the given type.
     */
    @JvmStatic
    fun register(interaction: PluginInteraction, type: InteractionType) {
        when (type) {
            InteractionType.OBJECT -> for (id in interaction.ids) {
                objectInteractions.putIfAbsent(id, interaction)
            }
            InteractionType.USE_WITH -> for (id in interaction.ids) {
                useWithInteractions.putIfAbsent(id, interaction)
            }
            InteractionType.NPC -> for (id in interaction.ids) {
                npcInteractions.putIfAbsent(id, interaction)
            }
            InteractionType.ITEM -> for (id in interaction.ids) {
                groundItemInteractions.putIfAbsent(id, interaction)
            }
        }
    }

    /**
     * Handles object interaction.
     */
    fun handle(player: Player?, `object`: Scenery): Boolean {
        val i = objectInteractions[`object`.id]
        return i != null && i.handle(player, `object`)
    }

    /**
     * Handles "use with" interaction.
     */
    fun handle(player: Player?, event: NodeUsageEvent): Boolean {
        val i = useWithInteractions[event.used.asItem().id]
        return i != null && i.handle(player, event)
    }

    /**
     * Handles NPC interaction.
     */
    fun handle(player: Player?, npc: NPC, option: Option?): Boolean {
        val i = npcInteractions[npc.id]
        return i != null && i.handle(player, npc, option)
    }

    /**
     * Handles ground item interaction.
     */
    fun handle(player: Player?, item: Item, option: Option?): Boolean {
        val i = groundItemInteractions[item.id]
        return i != null && i.handle(player, item, option)
    }

    /**
     * Interaction types that can be registered.
     */
    enum class InteractionType {
        NPC, OBJECT, USE_WITH, ITEM
    }
}