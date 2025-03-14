package content.region.karamja.dialogue.brimhaven

import content.region.asgarnia.quest.hero.HeroesQuest
import core.api.getAttribute
import core.api.openDialogue
import core.api.quest.getQuestStage
import core.api.setAttribute
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
class GruborDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        openDialogue(player, GruborDialogueFile(), npc)
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                options(
                    "Would you like your hedges trimming?",
                    "I want to come in.",
                    "Do you want to trade?",
                ).also { stage++ }

            1 ->
                when (buttonId) {
                    1 -> player(FaceAnim.HALF_GUILTY, "Would you like your hedges trimming?").also { stage = 10 }
                    2 -> player(FaceAnim.HALF_GUILTY, "I want to come in.").also { stage = 20 }
                    3 -> player(FaceAnim.HALF_GUILTY, "Do you want to trade?").also { stage = 30 }
                }

            10 ->
                npc(FaceAnim.HALF_GUILTY, "Eh? Don't be daft! We don't even HAVE any hehdges!").also {
                    stage = END_DIALOGUE
                }

            20 -> npc(FaceAnim.HALF_GUILTY, "No, go away.").also { stage = END_DIALOGUE }
            30 -> npc(FaceAnim.HALF_GUILTY, "No, I'm busy.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return GruborDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.GRUBOR_789)
    }
}

class GruborDialogueFile : DialogueBuilderFile() {
    override fun create(b: DialogueBuilder) {
        b
            .onPredicate { player ->
                getQuestStage(player, Quests.HEROES_QUEST) >= 2 &&
                    getAttribute(player, HeroesQuest.attributeGruborLetsYouIn, false) &&
                    HeroesQuest.isBlackArm(player)
            }.playerl("Hi.")
            .npcl("Hi, I'm a little busy right now.")
            .end()

        b
            .onPredicate { player ->
                getQuestStage(player, Quests.HEROES_QUEST) >= 2 &&
                    !getAttribute(player, HeroesQuest.attributeGruborLetsYouIn, false) &&
                    HeroesQuest.isBlackArm(player)
            }.npcl(FaceAnim.THINKING, "Yes? What do you want?")
            .options()
            .let { optionBuilder ->

                optionBuilder
                    .option_playerl("Rabbit's foot.")
                    .npcl("Eh? What are you on about? Go away!")
                    .end()

                optionBuilder
                    .option_playerl("Four leaved clover.")
                    .npcl("Oh you're one of the gang are you? Ok, hold up a second, I'll just let you in through here.")
                    .linel("You hear the door being unbarred from inside.")
                    .endWith { _, player ->
                        setAttribute(player, HeroesQuest.attributeGruborLetsYouIn, true)
                    }

                optionBuilder
                    .option_playerl("Lucky horseshoe.")
                    .npcl("Eh? What are you on about? Go away!")
                    .end()

                optionBuilder
                    .option_playerl("Black cat.")
                    .npcl("Eh? What are you on about? Go away!")
                    .end()
            }

        b
            .onPredicate { _ -> true }
            .npcl(FaceAnim.THINKING, "Yes? What do you want?")
            .options()
            .let { optionBuilder ->

                optionBuilder
                    .option_playerl("Would you like your hedges trimming?")
                    .npcl("Eh? Don't be daft! We don't even HAVE any hedges!")
                    .end()

                optionBuilder
                    .option_playerl("I want to come in.")
                    .npcl("No, go away.")
                    .end()

                optionBuilder
                    .option_playerl("Do you want to trade?")
                    .npcl("No, I'm busy.")
                    .end()
            }
    }
}
