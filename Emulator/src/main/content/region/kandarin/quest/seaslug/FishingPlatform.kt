package content.region.kandarin.quest.seaslug

import core.api.*
import core.api.quest.getQuestStage
import core.api.quest.isQuestComplete
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.game.world.map.zone.ZoneBorders
import core.game.world.repository.Repository
import org.rs.consts.*
import kotlin.random.Random

/**
 * Represents Fishing platform area.
 */
class FishingPlatform : MapArea {
    override fun defineAreaBorders(): Array<ZoneBorders> = arrayOf(
        ZoneBorders(2761, 3275, 2789, 3296, 1, false)
    )

    override fun entityStep(entity: Entity, location: Location, lastLocation: Location) {
        if (entity is Player) {
            val player = entity.asPlayer()
            // If player has completed the quest, skip.
            if(isQuestComplete(player, Quests.SEA_SLUG)) return
            if (getQuestStage(player, Quests.SEA_SLUG) >= 20) {
                val fisherMan = Repository.findNPC(NPCs.FISHERMAN_703)
                if (withinDistance(player, fisherMan!!.location, 1)) {
                    if (Random.nextInt(5) == 0) {// I added this, so it doesn't spam so much.
                        if (!inInventory(player, Items.LIT_TORCH_594)) {
                            fishermanAttack(player)
                        } else {
                            sendMessage(player, "The fishermen seem afraid of your torch.")
                        }
                    }
                }
            }
        }
    }

    private fun fishermanAttack (player : Player) {
        playAudio(player, Sounds.SLUG_FISHERMAN_ATTACK_3022)
        sendMessage(player, "The fishermen approach you...")
        sendMessage(player, "and smack you on the head with a fishing rod!")
        sendChat(player, "Ouch!")
        openInterface(player, Components.FADE_TO_BLACK_115)
        runTask(player, 3) {
            openInterface(player, Components.FADE_FROM_BLACK_170)
            teleport(player, Location.create(2784, 3287, 0))
        }
    }

}