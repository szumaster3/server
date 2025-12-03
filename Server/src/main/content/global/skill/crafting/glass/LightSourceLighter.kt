package content.global.skill.crafting.glass

import content.global.skill.crafting.CraftingDefinition
import core.api.*
import core.game.event.LitLightSourceEvent
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.tools.Log
import shared.consts.Items
import shared.consts.Regions

class LightSourceLighter : InteractionListener {

    override fun defineListeners() {
        /*
         * Handles using a tinderbox on any lightable light-source.
         */

        onUseWith(IntType.ITEM, Items.TINDERBOX_590, *CraftingDefinition.LIGHTABLE_ITEM_IDS) { player, _, with ->
            val item = with.asItem()
            val light = CraftingDefinition.LightSources.forId(item.id) ?: return@onUseWith true

            if (!light(player, item, light)) {
                sendMessage(player, "You need a Firemaking level of at least ${light.level} to light this.")
            }

            return@onUseWith true
        }

        /*
         * Handles using the “extinguish” option on lit light-sources.
         */

        on(IntType.ITEM, "extinguish") { player, node ->
            val lightSources = CraftingDefinition.LightSources.forLitId(node.id)

            lightSources ?: return@on false.also {
                log(this::class.java, Log.WARN, "UNHANDLED EXTINGUISH OPTION: ID = ${node.id}")
            }

            replaceSlot(player, node.asItem().slot, Item(lightSources.fullId))
            return@on true
        }
    }

    private fun light(player: Player, item: Item, data: CraftingDefinition.LightSources): Boolean {
        val requiredLevel = data.level
        val playerLevel = getStatLevel(player, Skills.FIREMAKING)

        if (playerLevel < requiredLevel) return false

        // Fishing platform exception.
        if (player.location.isInRegion(Regions.FISHING_PLATFORM_11059)) {
            sendMessage(player, "Your tinderbox is damp from the sea crossing. It won't light here.")
            return true
        }

        // Already lit.
        if (item.id == data.litId) return true

        // Normal lighting.
        playAudio(player, data.sfxId)
        replaceSlot(player, item.slot, Item(data.litId))
        player.dispatch(LitLightSourceEvent(data.litId))
        sendMessage(player, "You light the ${getItemName(data.litId).lowercase()}.")

        return true
    }
}
