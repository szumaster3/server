package content.region.kandarin.seers.quest.fishingcompo.dialogue

import core.api.*
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import shared.consts.Items
import shared.consts.NPCs
import shared.consts.Quests
import shared.consts.Vars

/**
 * Represents the Morris dialogue.
 *
 * # Relations
 * - [Fishing Contest][content.region.kandarin.quest.fishingcompo.FishingContest]
 * - [Land of the Goblins][content.region.misthalin.quest.lotg.LandOfTheGoblins]
 */
@Initializable
class MorrisDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        /*
         * Attempting to enter Hemenster without a fishing pass.
         */
        if (!inInventory(player, Items.FISHING_PASS_27, 1)) {
            npc("Competition pass please.")
            stage = 0
        }
        /*
         * After Fishing contest quest.
         */
        else if(isQuestComplete(player, Quests.FISHING_CONTEST) && getQuestStage(player, Quests.LAND_OF_THE_GOBLINS) >= 1) {
            player("I need to catch a Hemenster whitefish.")
            stage = 8
        }
        /*
         * Attempting to enter Hemenster with a fishing pass.
         */
        else {
            player("What are you sitting around here for?")
            stage = 4
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> {
                player(FaceAnim.HALF_GUILTY, "I don't have one of them.")
                stage++
            }

            1 -> {
                npcl(FaceAnim.NEUTRAL, "Oh well. I can't let you past then.")
                stage++
            }

            2 -> {
                player("What do I need that for?")
                stage++
            }

            3 -> {
                npcl(FaceAnim.HALF_GUILTY, "This is the entrance to the Hemenster fishing competition. It's a high class competition. Invitation only.")
                stage = 100
            }

            4 -> {
                npc(FaceAnim.HALF_GUILTY, "I'm making sure only those with a competition pass enter", "the fishing contest.")
                stage++
            }

            5 -> {
                player(FaceAnim.HAPPY, "I have one here.")
                stage++
            }

            6 -> {
                sendItemDialogue(player!!, Items.FISHING_PASS_27, "You show Morris your pass.")
                stage++
            }

            7 -> {
                npc("Move on through. Talk to Bonzo", "to enter the competition.")
                removeItem(player, Item(Items.FISHING_PASS_27, 1), Container.INVENTORY)
                setVarbit(player,  Vars.VARBIT_FISHING_CONTEST_PASS_SHOWN_2053, 1, true)
                stage = 100
            }

            8 -> {
                npcl(FaceAnim.HALF_GUILTY, "The whitefish, eh? Okay, since you've won the competition before then I'll let you in.")
                stage = 100
            }

            100 -> end()
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.MORRIS_227)
}
