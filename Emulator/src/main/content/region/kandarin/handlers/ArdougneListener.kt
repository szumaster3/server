package content.region.kandarin.handlers

import content.global.travel.charter.Charter
import content.region.kandarin.dialogue.ardougne.CaptainBarnabyDialogue
import content.region.kandarin.quest.grail.dialogue.GalahadDialogue
import core.api.*
import core.api.quest.hasRequirement
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.item.Item
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

class ArdougneListener : InteractionListener {
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
            var amount = if (isDiaryComplete(player, DiaryType.KARAMJA, 0)) 15 else 30
            if (!removeItem(player, Item(Items.COINS_995, amount))) {
                sendMessage(player, "You can not afford that.")
            } else {
                sendMessage(player, "You pay $amount coins and board the ship.")
                playJingle(player, 171)
                sendDialogue(player, "The ship arrives at Brimhaven.")
                Charter.ARDOUGNE_TO_BRIMHAVEN.sail(player)
            }
            return@on true
        }

        onUseWith(IntType.NPC, Items.FERRET_10092, NPCs.CHARLIE_5138) { player, _, _ ->
            if (!hasRequirement(player, Quests.EAGLES_PEAK)) return@onUseWith true
            openDialogue(
                player,
                object : DialogueFile() {
                    override fun handle(
                        componentID: Int,
                        buttonID: Int,
                    ) {
                        npc = NPC(NPCs.CHARLIE_5138)
                        when (stage) {
                            START_DIALOGUE ->
                                playerl(
                                    FaceAnim.FRIENDLY,
                                    "Hey, I've got another ferret if you're interested?",
                                ).also { stage++ }

                            1 ->
                                npcl(
                                    FaceAnim.NEUTRAL,
                                    "Er, oh! Well that's very kind of you, but we don't really need another ferret at the moment, ",
                                ).also { stage++ }

                            2 ->
                                npcl(
                                    FaceAnim.HALF_GUILTY,
                                    "I'm afraid. We're having enough trouble taming the one we've got.",
                                ).also { stage = END_DIALOGUE }
                        }
                    }
                },
            )
            return@onUseWith true
        }
    }
}
