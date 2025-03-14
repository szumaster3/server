package content.region.asgarnia.quest.dorics.dialogue

import core.api.addItemOrDrop
import core.api.inInventory
import core.api.quest.finishQuest
import core.api.quest.startQuest
import core.api.removeItem
import core.api.sendItemDialogue
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.dialogue.Topic
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.Quests

class DoricDialogue(
    private val questStage: Int,
) : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        when (questStage) {
            10 -> handleQuestStartDialogue(player, false)
            20 -> handleQuestStartDialogue(player, true)
            30 -> handleGiveDoricItemsDialogue(player)
        }
    }

    private fun handleQuestStartDialogue(
        player: Player?,
        isWhetstone: Boolean,
    ) {
        player ?: return
        when (stage) {
            0 -> {
                if (!isWhetstone) {
                    npcl(
                        FaceAnim.OLD_NORMAL,
                        "My anvils get enough work with my own use. I make pickaxes, and it takes a lot of hard work. If you could get me some more materials, then I could let you use them.",
                    )
                    stage = 30
                } else {
                    npcl(
                        FaceAnim.OLD_NORMAL,
                        "The whetstone is for more advanced smithing, but I could let you use it as well as my anvils if you could get me some more materials.",
                    )
                    stage = 30
                }
            }

            30 ->
                showTopics(
                    Topic(FaceAnim.FRIENDLY, "Yes I will get you the materials.", 40),
                    Topic(FaceAnim.HALF_GUILTY, "No, hitting rocks is for the boring people, sorry.", 100),
                )

            40 ->
                npc(
                    FaceAnim.OLD_NORMAL,
                    "Clay is what I use more than anything, to make casts.",
                    "Could you get me 6 clay, 4 copper ore, and 2 iron ore,",
                    "please? I could pay a little, and let you use my anvils.",
                    "Take this pickaxe with you just in case you need it.",
                ).also {
                    stage++
                }

            41 -> {
                playerl(FaceAnim.FRIENDLY, "Certainly, I'll be right back!")
                startQuest(player, Quests.DORICS_QUEST)
                if (!inInventory(player, Items.BRONZE_PICKAXE_1265)) addItemOrDrop(player, Items.BRONZE_PICKAXE_1265)
                stage = END_DIALOGUE
            }

            100 ->
                npcl(FaceAnim.OLD_NORMAL, "That is your choice. Nice to meet you anyway.").also {
                    stage = END_DIALOGUE
                }
        }
    }

    private fun handleGiveDoricItemsDialogue(player: Player?) {
        player ?: return
        when (stage) {
            0 -> npcl(FaceAnim.OLD_NORMAL, "Have you got my materials yet, traveller?").also { stage++ }
            1 -> {
                if (inInventory(player, Items.CLAY_434, 6) &&
                    inInventory(
                        player,
                        Items.COPPER_ORE_436,
                        4,
                    ) &&
                    inInventory(player, Items.IRON_ORE_440, 2)
                ) {
                    playerl(FaceAnim.HAPPY, "I have everything you need.")
                    stage++
                } else {
                    playerl(FaceAnim.HALF_GUILTY, "Sorry, I don't have them all yet.")
                    stage = 50
                }
            }

            2 ->
                npcl(
                    FaceAnim.OLD_NORMAL,
                    "Many thanks. Pass them here, please. I can spare you some coins for your trouble, and please use my anvils any time you want.",
                ).also {
                    stage++
                }

            3 -> {
                if (removeItem(player, Item(Items.CLAY_434, 6)) &&
                    removeItem(player, Item(Items.COPPER_ORE_436, 4)) &&
                    removeItem(player, Item(Items.IRON_ORE_440, 2))
                ) {
                    sendItemDialogue(player, Items.COPPER_ORE_436, "You hand the clay, copper, and iron to Doric.")
                    finishQuest(player, Quests.DORICS_QUEST)
                    stage = END_DIALOGUE
                }
            }

            50 ->
                npcl(
                    FaceAnim.OLD_NORMAL,
                    "Not to worry, stick at it. Remember, I need 6 clay, 4 copper ore, and 2 iron ore.",
                ).also {
                    stage++
                }

            51 ->
                showTopics(
                    Topic(FaceAnim.HALF_ASKING, "Where can I find those?", 52),
                    Topic(FaceAnim.HAPPY, "Certainly, I'll be right back.", END_DIALOGUE),
                )

            52 ->
                npcl(
                    FaceAnim.OLD_NORMAL,
                    "You'll be able to find all those ores in the rocks just inside the Dwarven Mine. Head east from here and you'll find the entrance in the side of Ice Mountain.",
                ).also { stage = END_DIALOGUE }
        }
    }
}
