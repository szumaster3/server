package content.global.plugins.item.withnpc

import content.global.skill.summoning.pets.Pet
import core.api.animate
import core.api.removeItem
import core.api.sendChat
import core.api.sendMessage
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.item.Item
import shared.consts.Animations
import shared.consts.Items
import shared.consts.NPCs

class MilkOnHellcatPlugin : InteractionListener {

    private val HELL_CATS = intArrayOf(
        NPCs.HELL_KITTEN_3505,
        NPCs.HELLCAT_3504,
        NPCs.OVERGROWN_HELLCAT_3503,
        NPCs.LAZY_HELLCAT_3506,
        NPCs.WILY_HELLCAT_3507
    )
    private val MILK = Items.BUCKET_OF_MILK_1927
    private val FEED_ANIMATION = Animations.HUMAN_BURYING_BONES_827

    override fun defineListeners() {

        onUseWith(IntType.NPC, MILK, *HELL_CATS) { player, used, with ->
            val npc = with.asNpc()
            if (!HELL_CATS.contains(npc.id)) {
                return@onUseWith false
            }
            if (used.id != MILK) {
                sendMessage(player, "The cat doesn't want that.")
                return@onUseWith true
            }

            if (!removeItem(player, used.asItem())) {
                return@onUseWith true
            }

            animate(player, FEED_ANIMATION)
            sendChat(npc, "Meeow!")

            val familiar = player.familiarManager.familiar
            if (familiar is Pet) {
                player.familiarManager.morphPet(
                    Item(familiar.itemId),
                    false,
                    player.location
                )
                player.sendMessage("Your hell-cat transforms into an ordinary cat.")
            }
            return@onUseWith true

        }
    }
}
