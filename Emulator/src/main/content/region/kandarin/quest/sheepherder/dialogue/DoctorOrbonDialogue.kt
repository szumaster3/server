package content.region.kandarin.quest.sheepherder.dialogue

import core.api.*
import core.api.quest.getQuestStage
import core.game.dialogue.Dialogue
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class DoctorOrbonDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if (getQuestStage(player, Quests.SHEEP_HERDER) == 10) {
            player(
                "Hello doctor. I need to acquire some protective clothing",
                "so that I can dispose of some escaped sheep infected",
                "with the plague.",
            )
        } else {
            sendDialogue(player, "He doesn't seem interested in talking with you right now.").also {
                stage = END_DIALOGUE
            }
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                npc(
                    "Protective clothing? I'm afraid I only have the one suit",
                    "which I made myself to prevent infection from the",
                    "contaminated patients I treat.",
                ).also {
                    stage++
                }
            1 ->
                npc(
                    "I suppose I could sell you this one and make myself",
                    "another, but it would cost you at least 100 gold so that",
                    "I could afford a replacement.",
                ).also {
                    stage++
                }
            2 -> options("Sorry doc, that's too much.", "Ok, I'll take it.").also { stage++ }
            3 ->
                when (buttonId) {
                    1 -> player("Sorry doc, that's too much.").also { stage = 6 }
                    2 -> player("Ok, I'll take it.").also { stage++ }
                }

            4 -> {
                if (!removeItem(player, Item(Items.COINS_995, 100), Container.INVENTORY)) {
                    player("I would love to, but I don't have enough..")
                    stage = 120
                } else {
                    if (!player.inventory.add(
                            content.region.kandarin.quest.sheepherder.SheepHerder.PLAGUE_BOTTOM,
                            content.region.kandarin.quest.sheepherder.SheepHerder.PLAGUE_TOP,
                        )
                    ) {
                        GroundItemManager.create(
                            content.region.kandarin.quest.sheepherder.SheepHerder.PLAGUE_TOP,
                            player.location,
                            player,
                        )
                        GroundItemManager.create(
                            content.region.kandarin.quest.sheepherder.SheepHerder.PLAGUE_BOTTOM,
                            player.location,
                            player,
                        )
                    }
                    sendDialogueLines(
                        player,
                        "You give Doctor Orbon 100 coins. Doctor Orbon hands over a",
                        "protective suit.",
                    ).also {
                        stage++
                    }
                }
            }

            5 -> npc("These should protect you from infection.").also { stage = END_DIALOGUE }
            6 -> npc("That's unfortunate.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.DOCTOR_ORBON_290)
    }
}
