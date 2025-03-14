package content.global.handlers.item.withnpc

import content.data.Meat
import content.data.MeatState
import content.global.skill.prayer.Bones
import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import org.rs.consts.Animations
import org.rs.consts.Items
import org.rs.consts.NPCs

class FoodOnSheepDogListener : InteractionListener {
    companion object {
        private const val SHEEP_DOG_NPC = NPCs.SHEEPDOG_2311
        private val FEED_ANIMATION = Animations.HUMAN_BURYING_BONES_827
        private val CONSUMABLE_BONES = Bones.array.filter { it != Items.BURNT_BONES_528 }.toHashSet()
        private val CONSUMABLE_MEATS =
            Meat
                .values()
                .filter { it.state == MeatState.INEDIBLE_RAW || it.state == MeatState.EDIBLE_COOKED }
                .map { it.id }
                .toHashSet()
    }

    override fun defineListeners() {
        onUseAnyWith(IntType.NPC, SHEEP_DOG_NPC) { player, used, with ->
            if (CONSUMABLE_BONES.contains(used.id)) {
                if (!removeItem(player, used.asItem())) {
                    return@onUseAnyWith true
                }
                sendDialogue(player, "You give the dog some nice ${used.name.lowercase()}. It happily gnaws on them.")
            } else if (CONSUMABLE_MEATS.contains(used.id)) {
                if (!removeItem(player, used.asItem())) {
                    return@onUseAnyWith true
                }
                sendDialogue(player, "You give the dog a nice piece of meat. It gobbles it up.")
            } else {
                sendMessage(player, "The dog doesn't seem interested in that.")
                sendChat(with.asNpc(), "Grrrr!")
                return@onUseAnyWith true
            }

            animate(player, FEED_ANIMATION)
            sendChat(with.asNpc(), "Woof woof!")
            return@onUseAnyWith true
        }
    }
}
