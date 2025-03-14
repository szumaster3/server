package content.global.ame.freakyforest

import content.data.GameAttributes
import content.data.RandomEvent
import core.api.*
import core.game.node.entity.player.Player
import core.game.world.map.zone.ZoneBorders
import core.tools.RandomFunction
import org.rs.consts.Items
import org.rs.consts.NPCs

object FreakyForesterUtils {
    const val FREAK_NPC = NPCs.FREAKY_FORESTER_2458
    val freakArea = ZoneBorders(2587, 4758, 2616, 4788)

    fun giveFreakTask(player: Player) {
        when (RandomFunction.getRandom(4)) {
            0 -> setAttribute(player, GameAttributes.RE_FREAK_TASK, NPCs.PHEASANT_2459)
            1 -> setAttribute(player, GameAttributes.RE_FREAK_TASK, NPCs.PHEASANT_2460)
            2 -> setAttribute(player, GameAttributes.RE_FREAK_TASK, NPCs.PHEASANT_2461)
            3 -> setAttribute(player, GameAttributes.RE_FREAK_TASK, NPCs.PHEASANT_2462)
            else -> setAttribute(player, GameAttributes.RE_FREAK_TASK, NPCs.PHEASANT_2459)
        }
        player.dialogueInterpreter.open(FreakyForesterDialogue(), FREAK_NPC)
    }

    fun cleanup(player: Player) {
        player.locks.unlockTeleport()
        player.properties.teleportLocation = getAttribute(player, RandomEvent.save(), null)
        removeAttributes(
            player,
            RandomEvent.save(),
            GameAttributes.RE_FREAK_TASK,
            GameAttributes.RE_FREAK_COMPLETE,
            GameAttributes.RE_FREAK_KILLS,
        )
        removeAll(player, Items.RAW_PHEASANT_6178)
        removeAll(player, Items.RAW_PHEASANT_6178, Container.BANK)
        removeAll(player, Items.RAW_PHEASANT_6179)
        removeAll(player, Items.RAW_PHEASANT_6179, Container.BANK)
    }

    fun reward(player: Player) {
        val hasHat = hasAnItem(player, Items.LEDERHOSEN_HAT_6182).container != null
        val hasTop = hasAnItem(player, Items.LEDERHOSEN_TOP_6180).container != null
        val hasShort = hasAnItem(player, Items.LEDERHOSEN_SHORTS_6181).container != null
        sendNPCDialogue(player, FREAK_NPC, "You get a lederhosen item as a reward for your help, many thanks!")
        when {
            (!hasHat) -> addItemOrDrop(player, Items.LEDERHOSEN_HAT_6182, 1)
            (!hasTop) -> addItemOrDrop(player, Items.LEDERHOSEN_TOP_6180, 1)
            (!hasShort) -> addItemOrDrop(player, Items.LEDERHOSEN_SHORTS_6181, 1)
            else -> {
                sendNPCDialogue(player, FREAK_NPC, "You get some money for your help, many thanks!")
                addItemOrDrop(player, Items.COINS_995, 500)
            }
        }
    }
}
