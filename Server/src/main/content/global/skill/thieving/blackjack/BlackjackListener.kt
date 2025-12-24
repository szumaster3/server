package content.global.skill.thieving.blackjack

import content.global.skill.thieving.blackjack.timer.BlackjackUnconsciousTimer
import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.info.Rights
import shared.consts.Quests

class BlackjackListener : InteractionListener {
    val npcs = BlackjackNPC.values().map { it.npcIds }.toIntArray()
    override fun defineListeners() {
        on(npcs, IntType.NPC, "lure") { player, node ->
            val isAdmin = player.rights == Rights.ADMINISTRATOR
            val npc = node.asNpc()
            val opt = getUsedOption(player)
            if (!isAdmin && !hasRequirement(player, Quests.THE_FEUD)) return@on false

            if(opt != "lure") {
                sendMessage(player, "You can't do that right now.")
                return@on true
            }

            BlackjackService.lure(player, npc)
            return@on true
        }

        on(npcs, IntType.NPC, "knock-out", "talk-to") { player, node ->
            val isAdmin = player.rights == Rights.ADMINISTRATOR
            val npc = node.asNpc()
            val opt = getUsedOption(player)
            if (!isAdmin && !hasRequirement(player, Quests.THE_FEUD)) return@on false

            BlackjackService.notify(player, npc)

            if(hasTimerActive<BlackjackUnconsciousTimer>(npc)) {
                return@on true
            }

            if(opt != "knock-out") { // prevent the rotation of the npc when lying on the ground.
                sendMessage(player, "You can't do that right now.")
                return@on true
            }

            BlackjackService.knockOut(player, npc)
            return@on true
        }
    }
}