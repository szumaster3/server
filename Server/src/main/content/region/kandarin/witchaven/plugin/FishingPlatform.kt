package content.region.kandarin.witchaven.plugin

import core.api.*
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
import core.tools.RandomFunction
import shared.consts.*

/**
 * Represents the fishing platform area.
 */
class FishingPlatform : InteractionListener, MapArea {

    override fun defineAreaBorders(): Array<ZoneBorders> = arrayOf(
        ZoneBorders(2761, 3275, 2789, 3296, 1, false)
    )

    override fun entityStep(entity: Entity, location: Location, lastLocation: Location) {
        if (entity !is Player) return
        val player = entity

        if (isQuestComplete(player, Quests.SEA_SLUG)) return
        if (getQuestStage(player, Quests.SEA_SLUG) < 20) return

        val fisherman = Repository.findNPC(NPCs.FISHERMAN_703) ?: return
        if (!withinDistance(player, fisherman.location, 1)) return

        if (RandomFunction.roll(5)) { // 20% chances to get smack.
            if (inInventory(player, Items.LIT_TORCH_594)) {
                sendMessage(player, "The fishermen seem afraid of your torch.")
            } else {
                fishermanAttack(player)
            }
        }
    }

    /**
     * Handles the fishermen attack.
     */
    private fun fishermanAttack(player: Player) {
        player.lock()
        player.impactHandler.disabledTicks = 6
        playAudio(player, Sounds.SLUG_FISHERMAN_ATTACK_3022)
        openInterface(player, Components.FADE_TO_BLACK_115)
        sendMessage(player, "The fishermen approach you...")
        sendMessage(player, "and smack you on the head with a fishing rod!")
        teleport(player, Location.create(2784, 3287, 0), TeleportManager.TeleportType.INSTANT, 3)
        queueScript(player, 6, QueueStrength.SOFT) { _: Int ->
            openInterface(player, Components.FADE_FROM_BLACK_170)
            sendChat(player, "Ouch!")
            player.unlock()
            return@queueScript stopExecuting(player)
        }
    }

    override fun defineListeners() {
        val npcDialogues = mapOf(
            NPCs.KIMBERLY_7168 to Pair("Hello there.", FaceAnim.CHILD_SAD),
            NPCs.MAYOR_HOBB_4874 to Pair("Well hello there; welcome to our little village. Pray, stay awhile.", null),
            NPCs.BROTHER_MALEDICT_4878 to Pair("The blessings of Saradomin be with you child.", null)
        )

        for ((npcId, dialogue) in npcDialogues) {
            on(npcId, IntType.NPC, "talk-to") { player, node ->
                if (dialogue.second != null) {
                    sendNPCDialogue(player, node.id, dialogue.first, dialogue.second!!)
                } else {
                    sendNPCDialogue(player, node.id, dialogue.first)
                }
                return@on true
            }
        }
    }
}

