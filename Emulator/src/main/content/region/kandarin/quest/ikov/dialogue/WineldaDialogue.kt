package content.region.kandarin.quest.ikov.dialogue

import content.data.GameAttributes
import core.api.*
import core.api.quest.getQuestStage
import core.api.quest.setQuestStage
import core.game.dialogue.Dialogue
import core.game.dialogue.DialogueBuilder
import core.game.dialogue.DialogueBuilderFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.world.map.Location
import core.plugin.Initializable
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class WineldaDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun newInstance(player: Player): Dialogue {
        return WineldaDialogue(player)
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        openDialogue(player, WineldaDialogueFile(), npc)
        return false
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.WINELDA_276)
    }
}

class WineldaDialogueFile : DialogueBuilderFile() {
    override fun create(b: DialogueBuilder) {
        b
            .onQuestStages(Quests.TEMPLE_OF_IKOV, 5, 6, 7, 100)
            .playerl(FaceAnim.FRIENDLY, "Hi again. Could you do the honours again please?")
            .npcl(FaceAnim.FRIENDLY, "Certainly! We helps those that helps poor Winelda!")
            .endWith { _, player ->

                teleport(player, Location(2664, 9876, 0))
            }

        b
            .onPredicate { player -> getAttribute(player, GameAttributes.QUEST_IKOV_WINELDA_INTER, false) }
            .npcl("Have you got roots for poor Winelda?")
            .branch { player ->
                if (inInventory(player, Items.LIMPWURT_ROOT_225)) {
                    if (amountInInventory(player, Items.LIMPWURT_ROOT_225) >= 20) {
                        return@branch 2
                    }
                    return@branch 1
                }
                return@branch 0
            }.let { branch ->
                branch
                    .onValue(2)
                    .playerl(FaceAnim.FRIENDLY, "Yes, I've got them.")
                    .betweenStage { _, player, _, _ ->
                        removeItem(player, Item(Items.LIMPWURT_ROOT_225, 20))
                    }.iteml(Items.LIMPWURT_ROOT_225, "You give Winelda the limpwurt roots.")
                    .npcl(
                        FaceAnim.FRIENDLY,
                        "Good! Good! My potion is nearly ready! Bubble, bubble, toil and trouble!",
                    ).npcl(FaceAnim.FRIENDLY, "Now we shows them ours magic! Hold on tight!")
                    .endWith { _, player ->
                        if (getQuestStage(player, Quests.TEMPLE_OF_IKOV) == 4) {
                            setQuestStage(player, Quests.TEMPLE_OF_IKOV, 5)
                        }

                        teleport(player, Location(2664, 9876, 0))
                    }
                branch
                    .onValue(1)
                    .playerl(FaceAnim.FRIENDLY, "I've got some limpwurt roots!")
                    .npcl(FaceAnim.FRIENDLY, "We needs 20 rooteses!")
                    .end()
                branch
                    .onValue(0)
                    .playerl(FaceAnim.FRIENDLY, "How many did you need again?")
                    .npcl(FaceAnim.FRIENDLY, "We needs 20 Limpwurt roots for pot.")
                    .end()
            }

        b
            .onPredicate { _ -> true }
            .npcl(FaceAnim.FRIENDLY, "Hehe! We see you're in a pickle!")
            .npcl("Wants to be getting over the nasty lava do we?")
            .options()
            .let { optionBuilder ->
                optionBuilder
                    .option_playerl("Nah, not bothered!")
                    .npcl(FaceAnim.FRIENDLY, "Hehe! Ye'll come back! They always come back!")
                    .end()
                optionBuilder
                    .option_playerl("Yes we do!")
                    .npcl(FaceAnim.FRIENDLY, "Mocking us are we? Clever one aren't we?")
                    .npcl(
                        FaceAnim.FRIENDLY,
                        "I'm knowing some magic trickesses! I could get over easy as that!",
                    ).npcl(FaceAnim.FRIENDLY, "Don't tell them! They always come! They pester poor Winelda!")
                    .playerl(FaceAnim.FRIENDLY, "If you're such a great witch, get me over!")
                    .npcl(FaceAnim.FRIENDLY, "See! They pester Winelda!")
                    .playerl(FaceAnim.FRIENDLY, "I can do something for you!")
                    .npcl(FaceAnim.FRIENDLY, "Good! Don't pester! Help!")
                    .npcl(FaceAnim.FRIENDLY, "Get Winelda 20 limpwurt roots for my pot.")
                    .npcl(FaceAnim.FRIENDLY, "Then we shows them some magic!")
                    .endWith { _, player ->
                        setAttribute(player, GameAttributes.QUEST_IKOV_WINELDA_INTER, true)
                    }
                optionBuilder
                    .option_playerl("Yes I do!")
                    .npcl(
                        FaceAnim.FRIENDLY,
                        "I'm knowing some magic trickesses! I could get over easy as that!",
                    ).npcl(FaceAnim.FRIENDLY, "Don't tell them! They always come! They pester poor Winelda!")
                    .playerl(FaceAnim.FRIENDLY, "If you're such a great witch, get me over!")
                    .npcl(FaceAnim.FRIENDLY, "See! They pester Winelda!")
                    .playerl(FaceAnim.FRIENDLY, "I can do something for you!")
                    .npcl(FaceAnim.FRIENDLY, "Good! Don't pester! Help!")
                    .npcl(FaceAnim.FRIENDLY, "Get Winelda 20 limpwurt roots for my pot.")
                    .npcl(FaceAnim.FRIENDLY, "Then we shows them some magic!")
                    .endWith { _, player ->
                        setAttribute(player, GameAttributes.QUEST_IKOV_WINELDA_INTER, true)
                    }
            }
    }
}
