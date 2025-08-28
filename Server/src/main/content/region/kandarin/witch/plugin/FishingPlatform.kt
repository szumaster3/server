package content.region.kandarin.witch.plugin

import core.api.*
import core.api.getQuestStage
import core.api.isQuestComplete
import core.game.dialogue.FaceAnim
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.TeleportManager
import core.game.world.map.Location
import core.game.world.map.zone.ZoneBorders
import core.game.world.repository.Repository
import shared.consts.*
import kotlin.random.Random

/**
 * Represents the fishing platform area.
 */
class FishingPlatform : InteractionListener, MapArea {

    override fun defineAreaBorders(): Array<ZoneBorders> = arrayOf(
        ZoneBorders(2761, 3275, 2789, 3296, 1, false)
    )

    override fun entityStep(entity: Entity, location: Location, lastLocation: Location) {
        if (entity !is Player) return
        val player = entity.asPlayer()

        if (isQuestComplete(player, Quests.SEA_SLUG)) return

        if (getQuestStage(player, Quests.SEA_SLUG) >= 20) {
            val fisherman = Repository.findNPC(NPCs.FISHERMAN_703) ?: return

            if (withinDistance(player, fisherman.location, 1)) {
                if (Random.nextInt(5) == 0) {
                    if (inInventory(player, Items.LIT_TORCH_594)) {
                        sendMessage(player, "The fishermen seem afraid of your torch.")
                    } else {
                        fishermanAttack(player)
                    }
                }
            }
        }
    }

    /**
     * Handles the fishermen attack.
     */
    private fun fishermanAttack(player: Player) {
        player.lock()
        playAudio(player, Sounds.SLUG_FISHERMAN_ATTACK_3022)
        sendMessage(player, "The fishermen approach you...")
        sendMessage(player, "and smack you on the head with a fishing rod!")
        openInterface(player, Components.FADE_TO_BLACK_115)
        teleport(player, Location.create(2784, 3287, 0), TeleportManager.TeleportType.INSTANT, 3)
        queueScript(player, 6, QueueStrength.NORMAL) { _: Int ->
            player.unlock()
            sendChat(player, "Ouch!")
            openInterface(player, Components.FADE_FROM_BLACK_170)
            return@queueScript stopExecuting(player)
        }
    }

    override fun defineListeners() {
        on(NPCs.KIMBERLY_7168, IntType.NPC, "talk-to") { player, node ->
            sendNPCDialogue(player, node.id, "Hello there.", FaceAnim.CHILD_SAD)
            return@on true
        }

        on(NPCs.MAYOR_HOBB_4874, IntType.NPC, "talk-to") { player, node ->
            sendNPCDialogue(player, node.id, "Well hello there; welcome to our little village. Pray, stay awhile.")
            return@on true
        }

        on(NPCs.BROTHER_MALEDICT_4878, IntType.NPC, "talk-to") { player, node ->
            sendNPCDialogue(player, node.id, "The blessings of Saradomin be with you child.")
            return@on true
        }
    }
}
