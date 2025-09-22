package content.region.kandarin.ardougne.plugin

import content.global.travel.ship.CharterShip
import content.region.kandarin.ardougne.east.dialogue.CaptainBarnabyDialogue
import content.region.kandarin.camelot.quest.grail.dialogue.GalahadDialogue
import core.api.*
import core.api.hasRequirement
import core.game.dialogue.FaceAnim
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.item.Item
import shared.consts.Items
import shared.consts.NPCs
import shared.consts.Quests

class ArdougnePlugin : InteractionListener {

    override fun defineListeners() {
        on(NPCs.GALAHAD_218, IntType.NPC, "talk-to") { player, _ ->
            openDialogue(player, GalahadDialogue())
            return@on true
        }

        on(NPCs.CAPTAIN_BARNABY_4974, IntType.NPC, "talk-to") { player, npc ->
            openDialogue(player, CaptainBarnabyDialogue(), npc)
            return@on true
        }

        on(NPCs.CAPTAIN_BARNABY_4974, IntType.NPC, "pay-fare") { player, _ ->
            val hasCharosRing = inEquipment(player, Items.RING_OF_CHAROSA_6465)
            val fareAmount = if (isDiaryComplete(player, DiaryType.KARAMJA, 0)) 15 else 30

            if (hasCharosRing || removeItem(player, Item(Items.COINS_995, fareAmount))) {
                if (hasCharosRing) {
                    sendMessage(player, "You board the ship.")
                } else {
                    sendMessage(player, "You pay $fareAmount coins and board the ship.")
                }
                CharterShip.ARDOUGNE_TO_BRIMHAVEN.sail(player)
            } else {
                sendMessage(player, "You cannot afford that.")
            }

            return@on true
        }

        onUseWith(IntType.NPC, Items.FERRET_10092, NPCs.CHARLIE_5138) { player, _, npc ->
            if (!hasRequirement(player, Quests.EAGLES_PEAK)) return@onUseWith true
            sendPlayerDialogue(player, "Hey, I've got another ferret if you're interested?")
            addDialogueAction(player) { _, _ ->
                sendNPCDialogue(player, npc.id, "Er, oh! Well that's very kind of you, but we don't really need another ferret at the moment, I'm afraid. We're having enough trouble taming the one we've got.", FaceAnim.NEUTRAL)
            }
            return@onUseWith true
        }
    }
}
