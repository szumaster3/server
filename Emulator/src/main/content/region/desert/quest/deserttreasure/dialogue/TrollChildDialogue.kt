package content.region.desert.quest.deserttreasure.dialogue

import content.region.desert.quest.deserttreasure.DesertTreasure
import content.region.desert.quest.deserttreasure.handlers.DTUtils
import core.api.inInventory
import core.api.openDialogue
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
class TrollChildDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        openDialogue(player!!, TrollChildDialogueFile(), npc)
        return false
    }

    override fun newInstance(player: Player?): Dialogue = TrollChildDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.BANDIT_1932)
}

class TrollChildDialogueFile : DialogueBuilderFile() {
    companion object {
        fun dialogueBeforeQuestCrying(builder: DialogueBuilder): DialogueBuilder =
            builder
                .playerl("Hello there.")
                .line("The troll child is crying to itself.", "It is ignoring you completely.")

        fun dialogueStillCrying(builder: DialogueBuilder): DialogueBuilder =
            builder
                .playerl("Hello there.")
                .npcl(FaceAnim.OLD_NEARLY_CRYING, "Waaaaaaa!")
                .line(
                    "This troll seems very upset about something.",
                    "Maybe some sweet food would take his mind off things?",
                )

        fun dialogueStoppedCrying(builder: DialogueBuilder): DialogueBuilder =
            builder
                .playerl("Hello there.")
                .npc(FaceAnim.OLD_SAD, "-sniff-", "H-hello there.")
                .playerl("Why so sad, little troll?")
                .npc(
                    FaceAnim.OLD_NEARLY_CRYING,
                    "It was the bad man!",
                    "He hurt my mommy and daddy!",
                    "He made them all freezey!",
                ).playerl("Bad man...?")
                .npcl(
                    FaceAnim.OLD_NEARLY_CRYING,
                    "He said it was because they stole his diamond! But they never did! They found it, and didn't know who it belonged to!",
                ).npcl(
                    FaceAnim.OLD_NEARLY_CRYING,
                    "My mommy always told me stealing is wrong, they would never steal from someone!",
                ).npcl(
                    FaceAnim.OLD_NEARLY_CRYING,
                    "Then he did some wavey hand thing and my mommy and daddy got frozified!",
                ).playerl(
                    "A diamond you say? Listen, I think I might be able to help your parents, but I need that Diamond in return.",
                ).npcl(
                    FaceAnim.OLD_NEARLY_CRYING,
                    "-sniff- I don't think they really wanted it anyway, they would have given it back to the bad man if he'd asked before freezifying them...",
                ).npcl(
                    FaceAnim.OLD_NEARLY_CRYING,
                    "I give you my promise mister that if you unfreeze my mommy and daddy, you can have the stupid diamond.",
                ).npcl(FaceAnim.OLD_NEARLY_CRYING, "Do we have a deal?")

        fun dialogueYesToHelp(builder: DialogueBuilder): DialogueBuilder =
            builder
                .playerl("Absolutely. Don't worry kid, I'll get your parents back to you safe and sound.")

        fun dialogueNoToHelp(builder: DialogueBuilder): DialogueBuilder =
            builder
                .playerl(
                    "Sorry, I can't make any promises about that, and I don't think I have the time to waste trying to defrost some stupid ice trolls.",
                ).npcl(FaceAnim.OLD_NEARLY_CRYING, "Waaaaaaa!")

        fun dialogueHaveYouFreedThem(builder: DialogueBuilder): DialogueBuilder =
            builder
                .npcl(FaceAnim.OLD_SAD, "You didn't free my mommy and daddy yet?")
                .player("Not yet...")
                .npc(FaceAnim.OLD_SAD, "Please try harder!", "I love my mommy and daddy!")

        fun dialogueThankYou(builder: DialogueBuilder): DialogueBuilder =
            builder
                .npc(
                    FaceAnim.OLD_CALM_TALK1,
                    "Thanks for all of your help!",
                    "I'm surprised you managed to survive the blizzard,",
                    "being a thin skinned fleshy and all!",
                ).player("What can I say?", "I'm a lot tougher than I look.")

        fun dialogueLostDiamond(builder: DialogueBuilder): DialogueBuilder =
            builder
                .playerl("I lost that diamond of Ice you gave me...")
                .npcl(
                    FaceAnim.OLD_CALM_TALK1,
                    "That's okay, it blew back on an icy wind... It's almost like it wants to stay here! Here, take it back, you've earned it.",
                )
    }

    override fun create(b: DialogueBuilder) {
        b
            .onQuestStages(Quests.DESERT_TREASURE, 0, 1, 2, 3, 4, 5, 6, 7, 8)
            .let { dialogueBeforeQuestCrying(it) }
            .end()

        b
            .onQuestStages(Quests.DESERT_TREASURE, 9)
            .branch { player ->
                return@branch DTUtils.getSubStage(player, DesertTreasure.iceStage)
            }.let { branch ->

                branch
                    .onValue(0)
                    .let { dialogueStillCrying(it) }
                    .end()

                branch
                    .onValue(1)
                    .let { dialogueStoppedCrying(it) }
                    .options()
                    .let { optionBuilder ->
                        optionBuilder
                            .option("Yes")
                            .let { dialogueYesToHelp(it) }
                            .endWith { _, player ->
                                if (DTUtils.getSubStage(player, DesertTreasure.iceStage) == 1) {
                                    DTUtils.setSubStage(player, DesertTreasure.iceStage, 2)
                                }
                            }
                        optionBuilder
                            .option("No")
                            .let { dialogueNoToHelp(it) }
                            .end()
                    }

                branch
                    .onValue(2)
                    .let { dialogueHaveYouFreedThem(it) }
                    .end()

                branch
                    .onValue(3)
                    .let { dialogueHaveYouFreedThem(it) }
                    .end()

                branch
                    .onValue(4)
                    .let { dialogueHaveYouFreedThem(it) }
                    .end()

                branch
                    .onValue(100)
                    .branch { player ->
                        return@branch if (!inInventory(player, Items.ICE_DIAMOND_4671)) {
                            1
                        } else {
                            0
                        }
                    }.let { branch ->
                        branch
                            .onValue(1)
                            .let { dialogueLostDiamond(it) }
                            .end()
                        branch
                            .onValue(0)
                            .let { dialogueThankYou(it) }
                            .end()
                    }
            }
    }
}
