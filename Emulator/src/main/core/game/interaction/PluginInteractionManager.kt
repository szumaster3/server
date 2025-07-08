package core.game.interaction

import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.node.scenery.Scenery

/**
 * Handles registration and dispatching of entity interaction plugins.
 */
object PluginInteractionManager {
    private val npcInteractions = HashMap<Int, PluginInteraction>()
    private val objectInteractions = HashMap<Int, PluginInteraction>()
    private val useWithInteractions = HashMap<Int, PluginInteraction>()
    private val groundItemInteractions = HashMap<Int, PluginInteraction>()

    /**
     * Registers a new interaction with the specified type.
     *
     * @param interaction The interaction to register.
     * @param type        The type of interaction.
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
     * Handles interaction with a scenery object.
     *
     * @param player The player initiating the interaction.
     * @param object The scenery object being interacted with.
     * @return true if the interaction was handled, false otherwise.
     */
    fun handle(player: Player?, `object`: Scenery): Boolean {
        val i = objectInteractions[`object`.id]
        return i != null && i.handle(player, `object`)
    }

    /**
     * Handles interaction with an item being used on another entity.
     *
     * @param player The player initiating the interaction.
     * @param event  The event containing usage details.
     * @return true if the interaction was handled, false otherwise.
     */
    fun handle(player: Player?, event: NodeUsageEvent): Boolean {
        val i = useWithInteractions[event.used.asItem().id]
        return i != null && i.handle(player, event)
    }

    /**
     * Handles interaction with an NPC.
     *
     * @param player The player initiating the interaction.
     * @param npc    The NPC being interacted with.
     * @param option The interaction option chosen.
     * @return true if the interaction was handled, false otherwise.
     */
    fun handle(player: Player?, npc: NPC, option: Option?): Boolean {
        val i = npcInteractions[npc.id]
        return i != null && i.handle(player, npc, option)
    }

    /**
     * Handles interaction with a ground item.
     *
     * @param player The player initiating the interaction.
     * @param item   The item being interacted with.
     * @param option The interaction option chosen.
     * @return true if the interaction was handled, false otherwise.
     */
    fun handle(player: Player?, item: Item, option: Option?): Boolean {
        val i = groundItemInteractions[item.id]
        return i != null && i.handle(player, item, option)
    }

    /**
     * Defines the types of interactions that can be registered.
     */
    enum class InteractionType {
        NPC, OBJECT, USE_WITH, ITEM
    }
}