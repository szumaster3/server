package content.region.kandarin.quest.seaslug.dialogue

import content.region.kandarin.quest.seaslug.SeaSlug
import core.api.*
import core.api.quest.getQuestStage
import core.api.quest.isQuestComplete
import core.api.quest.setQuestStage
import core.game.dialogue.Dialogue
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.system.task.Pulse
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs
import org.rs.consts.Quests
import org.rs.consts.Sounds

@Initializable
class KentDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        when {
            isQuestComplete(player, Quests.SEA_SLUG) -> npc("Hello there, ${player.username}!").also { stage = 200 }
            getQuestStage(player, Quests.SEA_SLUG) < 15 ->
                npc("Oh thank Saradomin! I thought I'd be left out here", "forever.").also {
                    stage =
                        0
                }
            else -> player("Hello.").also { stage = 100 }
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                player(
                    FaceAnim.HALF_GUILTY,
                    "Your wife sent me out to find you and your boy.",
                    "Kennith's fine by the way, he's on the platform.",
                ).also {
                    stage++
                }
            1 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "I knew the row boat wasn't sea worthy. I couldn't risk",
                    "bringing him along but you must get him off that",
                    "platform.",
                ).also {
                    stage++
                }
            2 -> player(FaceAnim.HALF_ASKING, "What's going on here?").also { stage++ }
            3 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "Five days ago we pulled in a huge catch. As well as fish",
                    "we caught small slug like creatures, hundreds of them.",
                ).also {
                    stage++
                }
            4 -> npc(FaceAnim.HALF_GUILTY, "That's when the fishermen began to act strange.").also { stage++ }
            5 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "It was the sea slugs, they attack themselves to your",
                    "body and somehow take over the mind of the carrier.",
                ).also {
                    stage++
                }
            6 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "I told Kennith to hide until I returned but I was",
                    "washed up here.",
                ).also { stage++ }
            7 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "Please go back and get my boy, you can send help for",
                    "me later.",
                ).also { stage++ }
            8 ->
                npc(FaceAnim.SCARED, "${player.username} wait!").also {
                    setQuestStage(player!!, Quests.SEA_SLUG, 15)
                    setAttribute(player, "/save:${SeaSlug.ATTRIBUTE_TALK_WITH_KENT}", true)
                    stage++
                }
            9 -> {
                end()
                submitWorldPulse(
                    object : Pulse() {
                        var ticks = 0

                        override fun pulse(): Boolean {
                            when (ticks++) {
                                2 -> {
                                    sendMessage(player, "*slooop*")
                                    playAudio(player, Sounds.SLUG_SCOOP_SLUG_3025)
                                    visualize(findLocalNPC(player, NPCs.KENT_701)!!, 4807, 790)
                                    sendMessage(player, "He pulls a sea slug from under your top.")
                                }

                                4 -> {
                                    openDialogue(player, KentDialogueFile())
                                    return true
                                }
                            }
                            return false
                        }
                    },
                )
            }
            100 -> npc(FaceAnim.HALF_GUILTY, "Oh my, I must get back to shore.").also { stage = END_DIALOGUE }
            200 -> player("Hello again Kent.").also { stage++ }
            201 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "I never did get the chance to thank you properly for saving Kennith and myself.",
                ).also {
                    stage++
                }
            202 -> playerl(FaceAnim.FRIENDLY, "Oh, don't be silly, it was nothing really.").also { stage++ }
            203 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Play it down if you will. It was a truly brave thing you did.",
                ).also { stage++ }
            204 ->
                player(
                    FaceAnim.FRIENDLY,
                    "I only did what anyone would have done in my position. I have to go now, take care.",
                ).also {
                    stage++
                }
            205 -> npc(FaceAnim.FRIENDLY, "You too, ${player.username}. Goodbye.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.KENT_701)
    }

    internal class KentDialogueFile : DialogueFile() {
        override fun handle(
            componentID: Int,
            buttonID: Int,
        ) {
            npc = NPC(NPCs.KENT_701)
            when (stage) {
                0 -> npc("A few more minutes and that thing would have full", "control of your body.").also { stage++ }
                1 -> player("Yuck! Thanks Kent.").also { stage = END_DIALOGUE }
            }
        }
    }
}
