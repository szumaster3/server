package content.region.desert.quest.deserttreasure.dialogue

import core.api.addItemOrDrop
import core.api.inInventory
import core.api.openDialogue
import core.api.removeItem
import core.game.dialogue.Dialogue
import core.game.dialogue.DialogueBuilder
import core.game.dialogue.DialogueBuilderFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class RuantunDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        openDialogue(player!!, RuantunDialogueFile(), npc)
        return false
    }

    override fun newInstance(player: Player?): Dialogue {
        return RuantunDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.RUANTUN_1916)
    }
}

class RuantunDialogueFile : DialogueBuilderFile() {
    override fun create(b: DialogueBuilder) {
        b
            .onQuestStages(Quests.DESERT_TREASURE, 0, 1, 2, 3, 4, 5, 6, 7, 8)
            .playerl("Hello.")
            .npcl(FaceAnim.OLD_NORMAL, "You ssshould not be down here...")
            .options()
            .let { optionBuilder ->
                optionBuilder
                    .option("Who are you?")
                    .playerl("Who are you?")
                    .npcl(
                        FaceAnim.OLD_NORMAL,
                        "My name isss unimportant... I live only to ssserve my massster.",
                    ).playerl("Um... Okay then.")
                    .end()

                optionBuilder
                    .option("Why are you down here?")
                    .playerl("Why are you down here?")
                    .npcl(
                        FaceAnim.OLD_NORMAL,
                        "Thisss isss where I belong... Beingsss sssuch as myssself cannot abide in the light... It is in the darknesss where we find our homesss...",
                    ).playerl("Uh... Okay then.")
                    .end()

                optionBuilder
                    .option("Can I use your anvil?")
                    .playerl("Can I use your anvil?")
                    .npcl(
                        FaceAnim.OLD_NORMAL,
                        "Of courssse you may... I have very little ussse for it nowadaysss...",
                    ).playerl("Uh... Thanks, I guess.")
                    .end()
            }

        b
            .onQuestStages(Quests.DESERT_TREASURE, 9, 10, 100)
            .playerl("Hello.")
            .npcl(FaceAnim.OLD_NORMAL, "You sshould not be down here...")
            .playerl("Are you an assistant to Count Draynor?")
            .npc(FaceAnim.OLD_NORMAL, "I usssed to have that honour...", "Why do you ssseek me?")
            .branch { player ->
                return@branch if (inInventory(player, Items.SILVER_BAR_2355)) {
                    1
                } else {
                    0
                }
            }.let { branch ->
                branch
                    .onValue(1)
                    .playerl(
                        "I have a silver bar with me, I was wondering if you could make it into a 'sacrificial offering pot' for me?",
                    ).betweenStage { _, player, _, _ ->
                        if (removeItem(player, Items.SILVER_BAR_2355)) {
                            addItemOrDrop(player, Items.SILVER_POT_4658)
                        }
                    }.npc(
                        FaceAnim.OLD_NORMAL,
                        "Yesss, of courssse...",
                        "There you are, put it to good usssse...",
                    ).end()

                branch
                    .onValue(0)
                    .playerl(
                        "I understand that you can make me a 'sacrificial offering pot' if I bring you a bar of silver?",
                    ).npcl(FaceAnim.OLD_NORMAL, "And where did you hear thisss?")
                    .playerl("It was from Malak in Canifis.")
                    .npc(
                        FaceAnim.OLD_NORMAL,
                        "Ah, I sssee...",
                        "Yesss, I know how to make sssuch an item...",
                        "It has been many yearsss sssince I have needed to however...",
                    ).npcl(
                        FaceAnim.OLD_NORMAL,
                        "It is not my wisssh to quessstion your desssire for sssuch an item, I wasss merely sssurprisssed that one sssuch as you would make sssuch a requessst...",
                    ).npcl(
                        FaceAnim.OLD_NORMAL,
                        "I will happily make you thisss pot, but you mussst bring me a bar of sssilver... Alasss, I can no longer collect my own ingredientsss, and mussst remain here...",
                    ).end()
            }
    }
}
