package content.region.desert.quest.deserttreasure.dialogue

import content.region.desert.quest.deserttreasure.DesertTreasure
import content.region.desert.quest.deserttreasure.plugin.DTUtils
import core.api.*
import core.api.quest.getQuestStage
import core.game.dialogue.Dialogue
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.interaction.QueueStrength
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Components
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class FatherTrollDialogue(player: Player? = null) : Dialogue(player) {

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        if (DTUtils.getSubStage(player, DesertTreasure.iceStage) == 3 &&
            getVarbit(player, DesertTreasure.varbitFrozenFather) == 1 &&
            getVarbit(player, DesertTreasure.varbitFrozenMother) == 1
        ) {
            openDialogue(player!!, ChatFatherAndMotherTrollDialogueFile(), npc)
        } else if (DTUtils.getSubStage(player, DesertTreasure.iceStage) == 3) {
            openDialogue(
                player,
                object : DialogueFile() {
                    override fun handle(
                        componentID: Int,
                        buttonID: Int,
                    ) {
                        when (stage) {
                            0 ->
                                npcl(
                                    FaceAnim.OLD_CALM_TALK2,
                                    "Oh thank you! It was really cold in there! But please, you must free my wife as well! Our son is depending on us!",
                                ).also { stage = END_DIALOGUE }
                        }
                    }
                },
                npc,
            )
        } else if (DTUtils.getSubStage(player, DesertTreasure.iceStage) == 4) {
            openDialogue(player!!, ChatFatherAndMotherTrollAfterDialogueFile(), npc)
        } else if ((
                getQuestStage(player, Quests.DESERT_TREASURE) == 9 &&
                    DTUtils.getSubStage(player, DesertTreasure.iceStage) >= 5
            ) ||
            getQuestStage(player, Quests.DESERT_TREASURE) >= 10
        ) {
            openDialogue(
                player,
                object : DialogueFile() {
                    override fun handle(
                        componentID: Int,
                        buttonID: Int,
                    ) {
                        when (stage) {
                            0 ->
                                npcl(
                                    FaceAnim.OLD_CALM_TALK2,
                                    "Thanks again for freeing me from that ice block! I might be a troll, but it was real uncomfortable in there!",
                                ).also { stage = END_DIALOGUE }
                        }
                    }
                },
                npc,
            )
        }
        return false
    }

    override fun newInstance(player: Player?): Dialogue = FatherTrollDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.ICE_TROLL_1943)
}

@Initializable
class MotherTrollDialogue(player: Player? = null) : Dialogue(player) {

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        println(
            getQuestStage(player, Quests.DESERT_TREASURE) == 9 &&
                DTUtils.getSubStage(player, DesertTreasure.iceStage) >= 5,
        )
        if (DTUtils.getSubStage(player, DesertTreasure.iceStage) == 3 &&
            getVarbit(player, DesertTreasure.varbitFrozenFather) == 1 &&
            getVarbit(player, DesertTreasure.varbitFrozenMother) == 1
        ) {
            openDialogue(player!!, ChatFatherAndMotherTrollDialogueFile(), npc)
        } else if (DTUtils.getSubStage(player, DesertTreasure.iceStage) == 3) {
            openDialogue(
                player,
                object : DialogueFile() {
                    override fun handle(
                        componentID: Int,
                        buttonID: Int,
                    ) {
                        when (stage) {
                            0 ->
                                npcl(
                                    FaceAnim.OLD_CALM_TALK2,
                                    "Wow, thanks for breaking me out of that ice! But please, my husband is still trapped in there!",
                                ).also { stage = END_DIALOGUE }
                        }
                    }
                },
                npc,
            )
        } else if (DTUtils.getSubStage(player, DesertTreasure.iceStage) == 4) {
            openDialogue(player!!, ChatFatherAndMotherTrollAfterDialogueFile(), npc)
        } else if ((
                getQuestStage(player, Quests.DESERT_TREASURE) == 9 &&
                    DTUtils.getSubStage(player, DesertTreasure.iceStage) >= 5
            ) ||
            getQuestStage(player, Quests.DESERT_TREASURE) >= 10
        ) {
            openDialogue(
                player,
                object : DialogueFile() {
                    override fun handle(
                        componentID: Int,
                        buttonID: Int,
                    ) {
                        when (stage) {
                            0 ->
                                npcl(
                                    FaceAnim.OLD_CALM_TALK2,
                                    "Thanks again for freeing me from that ice block! I don't know what my little snookums would have done without us!",
                                ).also { stage = END_DIALOGUE }
                        }
                    }
                },
                npc,
            )
        }
        return false
    }

    override fun newInstance(player: Player?): Dialogue = MotherTrollDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.TROLL_MOTHER_1950)
}

class ChatFatherAndMotherTrollDialogueFile : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        when (stage) {
            0 ->
                npcl(
                    NPCs.TROLL_FATHER_1948,
                    FaceAnim.OLD_CALM_TALK2,
                    "Phew! Am I glad to be out of that big ice cube! Are you okay too darling?",
                ).also { stage++ }

            1 ->
                npcl(
                    NPCs.TROLL_MOTHER_1950,
                    FaceAnim.OLD_CALM_TALK2,
                    "Yes, I thought we were done for! Why ever did that nasty Kamil freeze us up there anyway?",
                ).also { stage++ }

            2 -> playerl("He must have been trying to protect his Diamond...").also { stage++ }
            3 ->
                npcl(
                    NPCs.TROLL_FATHER_1948,
                    FaceAnim.OLD_CALM_TALK2,
                    "You mean that diamond I found the other day belonged to him? But why didn't he just ask for it back? It's not like I really want it or anything!",
                ).also { stage++ }

            4 ->
                npcl(
                    NPCs.TROLL_FATHER_1948,
                    FaceAnim.OLD_CALM_TALK2,
                    "And how did you know we had that diamond anyway, fleshy?",
                ).also { stage++ }

            5 ->
                playerl(
                    "Your son told me. That's why I rescued you, it is very important that I have that diamond...",
                ).also {
                    stage++
                }

            6 ->
                npcl(
                    NPCs.TROLL_MOTHER_1950,
                    FaceAnim.OLD_CALM_TALK2,
                    "Ooohhhhh, my poor baby! He must have been so worried about us...",
                ).also { stage++ }

            7 ->
                npcl(
                    NPCs.TROLL_FATHER_1948,
                    FaceAnim.OLD_CALM_TALK2,
                    "Yes, but he certainly inherited his Dad's smarts!",
                ).also { stage++ }

            8 ->
                npcl(
                    NPCs.TROLL_FATHER_1948,
                    FaceAnim.OLD_CALM_TALK2,
                    "If he'd told this fleshy that he had the Diamond and not us, we might never have been rescued!",
                ).also { stage++ }

            9 -> playerl("Wait... what? That stupid little troll kid had the diamond all along?").also { stage++ }
            10 ->
                npcl(
                    NPCs.TROLL_MOTHER_1950,
                    FaceAnim.OLD_CALM_TALK2,
                    "Don't you talk about my baby like that!",
                ).also { stage++ }

            11 ->
                npcl(
                    NPCs.TROLL_FATHER_1948,
                    FaceAnim.OLD_CALM_TALK2,
                    "Now, now dear, all's well that ends well. We've been freed and this fleshy has certainly earned himself that diamond.",
                ).also { stage++ }

            12 ->
                npcl(
                    NPCs.TROLL_FATHER_1948,
                    FaceAnim.OLD_CALM_TALK2,
                    "Let's get out of this terrible place and see our son!",
                ).also {
                    stage++
                    if (DTUtils.getSubStage(player!!, DesertTreasure.iceStage) == 3) {
                        DTUtils.setSubStage(player!!, DesertTreasure.iceStage, 4)
                    }
                }

            13 -> {
                queueScript(player!!, 0, QueueStrength.SOFT) { stage: Int ->
                    when (stage) {
                        0 -> {
                            closeOverlay(player!!)
                            openOverlay(player!!, Components.FADE_TO_BLACK_120)
                            return@queueScript delayScript(player!!, 6)
                        }

                        1 -> {
                            teleport(player!!, Location(2836, 3739, 0))
                            return@queueScript delayScript(player!!, 1)
                        }

                        2 -> {
                            openOverlay(player!!, Components.FADE_FROM_BLACK_170)
                            return@queueScript delayScript(player!!, 6)
                        }

                        3 -> {
                            closeOverlay(player!!)
                            openDialogue(player!!, ChatFatherAndMotherTrollAfterDialogueFile())
                            return@queueScript stopExecuting(player!!)
                        }

                        else -> return@queueScript stopExecuting(player!!)
                    }
                }
                end()
            }
        }
    }
}

class ChatFatherAndMotherTrollAfterDialogueFile : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        when (stage) {
            0 ->
                npcl(
                    NPCs.TROLL_CHILD_1933,
                    FaceAnim.OLD_CALM_TALK2,
                    "Mommy! Daddy! You're free!",
                ).also { stage++ }

            1 ->
                npc(
                    NPCs.TROLL_FATHER_1948,
                    FaceAnim.OLD_CALM_TALK1,
                    "That's right son, and it's all thanks to this brave",
                    "adventurer here.",
                    "Now, make sure you hand over that diamond he was",
                    "looking for.",
                ).also { stage++ }

            2 ->
                npcl(
                    NPCs.TROLL_FATHER_1948,
                    FaceAnim.OLD_CALM_TALK1,
                    "It has been nothing but trouble for us, let's just get back to our cave and have dinner.",
                ).also { stage++ }

            3 ->
                npcl(
                    NPCs.TROLL_MOTHER_1950,
                    FaceAnim.OLD_CALM_TALK2,
                    "That's right son, it's your favorite tonight too! A big plate of raw mackerel!",
                ).also { stage++ }

            4 -> npcl(NPCs.TROLL_CHILD_1933, FaceAnim.OLD_CALM_TALK2, "RAW MACKEREL! YUMMY!").also { stage++ }
            5 ->
                npcl(
                    NPCs.TROLL_CHILD_1933,
                    FaceAnim.OLD_CALM_TALK1,
                    "Here ya go mister! Thanks for getting my mom and dad away from the bad man!",
                ).also {
                    stage++
                    if (DTUtils.getSubStage(player!!, DesertTreasure.iceStage) in 3..4) {
                        addItemOrDrop(player!!, Items.ICE_DIAMOND_4671)
                        DTUtils.setSubStage(player!!, DesertTreasure.iceStage, 100)
                    }
                }

            6 ->
                playerl("Don't worry about it, just as long as I don't have to go back into that blizzard.").also {
                    stage = END_DIALOGUE
                }
        }
    }
}
