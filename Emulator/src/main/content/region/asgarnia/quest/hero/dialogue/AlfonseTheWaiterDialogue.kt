package content.region.asgarnia.quest.hero.dialogue

import content.region.asgarnia.quest.hero.HeroesQuest
import core.api.interaction.openNpcShop
import core.api.openDialogue
import core.api.quest.getQuestStage
import core.api.quest.setQuestStage
import core.game.dialogue.Dialogue
import core.game.dialogue.DialogueBuilder
import core.game.dialogue.DialogueBuilderFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class AlfonseTheWaiterDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        openDialogue(player, AlfonseTheWaiterDialogueFile(), npc)

        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> options("Yes, please.", "No, thank you.", "Where do you get your Karambwan from?").also { stage++ }
            1 ->
                when (buttonId) {
                    1 -> player(FaceAnim.HALF_GUILTY, "Yes, please.").also { stage = 10 }
                    2 -> player(FaceAnim.HALF_GUILTY, "No, thank you.").also { stage = END_DIALOGUE }
                    3 -> player(FaceAnim.HALF_GUILTY, "Where do you get your Karambwan from?").also { stage = 30 }
                }

            10 -> {
                end()
                openNpcShop(player, NPCs.ALFONSE_THE_WAITER_793)
            }

            30 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "We buy directly off Lubufu, a local fisherman. He",
                    "seems to have a monopoly over Karambwan sales.",
                ).also { stage = END_DIALOGUE }
        }
        return false
    }

    override fun newInstance(player: Player?): Dialogue {
        return AlfonseTheWaiterDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.ALFONSE_THE_WAITER_793)
    }
}

class AlfonseTheWaiterDialogueFile : DialogueBuilderFile() {
    override fun create(b: DialogueBuilder) {
        b
            .onPredicate { _ -> true }
            .npc("Welcome to the Shrimp and Parrot.", "Would you like to order, sir?")
            .options()
            .let { optionBuilder ->
                optionBuilder
                    .option_playerl("Yes please.")
                    .endWith { _, player ->
                        openNpcShop(player, npc!!.id)
                    }

                optionBuilder
                    .option_playerl("No thank you.")
                    .end()

                optionBuilder
                    .optionIf("Do you sell Gherkins?") { player ->
                        return@optionIf getQuestStage(player, Quests.HEROES_QUEST) >= 2 && HeroesQuest.isPhoenix(player)
                    }.playerl("Do you sell Gherkins?")
                    .npc(
                        "Hmmmm Gherkins eh? Ask Charlie the cook, round the",
                        "back. He may have some 'gherkins' for you!",
                    ).linel("Alfonse winks at you.")
                    .endWith { _, player ->
                        if (getQuestStage(player, Quests.HEROES_QUEST) == 2) {
                            setQuestStage(player, Quests.HEROES_QUEST, 3)
                        }
                    }

                optionBuilder
                    .option("Where do you get your Karambwan from?")
                    .npc(
                        "We buy directly off Lubufu, a local fisherman. He",
                        "seems to have a monopoly over Karambwan sales.",
                    ).end()
            }
    }
}
