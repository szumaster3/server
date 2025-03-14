package content.region.fremennik.quest.viking.dialogue

import core.api.*
import core.api.interaction.openNpcShop
import core.api.quest.isQuestComplete
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class SigmundDialogue(
    player: Player? = null,
) : Dialogue(player) {
    private val questItems = intArrayOf(3709, 3707, 3706, 3710, 3705, 3704, 3703, 3702, 3701, 3708, 3700, 3699, 3698)

    val gender =
        if (player?.isMale == true) {
            "brother"
        } else {
            "sister"
        }
    val fName = player?.getAttribute("fremennikname", "fremmyname")

    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        if (isQuestComplete(player, Quests.THE_FREMENNIK_TRIALS)) {
            playerl(FaceAnim.HAPPY, "Hello there!")
            stage = 50
            return true
        } else if (!player.questRepository.hasStarted(Quests.THE_FREMENNIK_TRIALS)) {
            playerl(FaceAnim.HAPPY, "Hello there!")
            stage = 60
            return true
        }
        if (inInventory(player, Items.EXOTIC_FLOWER_3698, 1)) {
            playerl(FaceAnim.HAPPY, "Here's that flower you wanted.")
            stage = 30
            return true
        } else if (getAttribute(player, "sigmundreturning", false) && !anyInInventory(player, *questItems)) {
            npcl(
                FaceAnim.ASKING,
                "So... how goes it outerlander? Did you manage to obtain my flower for me yet? Or do you lack the necessary merchanting skills?",
            )
            stage = 35
            return true
        }
        if (getAttribute(player, "sigmund-started", false)) {
            playerl(FaceAnim.HAPPY, "Hello there!")
            stage = 25
            return true
        } else if (!getAttribute(player, "fremtrials:sigmund-vote", false)) {
            playerl(FaceAnim.HAPPY, "Hello there!")
            stage = 1
            return true
        } else if (getAttribute(player, "fremtrials:sigmund-vote", false)) {
            playerl(FaceAnim.HAPPY, "Hello there!")
            stage = 40
            return true
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            1 -> npcl(FaceAnim.NEUTRAL, "Hello outlander.").also { stage++ }
            2 -> playerl(FaceAnim.NEUTRAL, "Are you a member of the council?").also { stage++ }
            3 ->
                npcl(
                    FaceAnim.NEUTRAL,
                    "That I am outlander; it is a position that brings my family and I pride.",
                ).also { stage++ }

            4 ->
                playerl(
                    FaceAnim.NEUTRAL,
                    "I was wondering if I can count on your vote at the council of elders?",
                ).also { stage++ }

            5 ->
                npcl(
                    FaceAnim.THINKING,
                    "You wish to become a Fremennik? I may be persuaded to swing my vote to your favor, but you will first need to do a little task for me.",
                ).also { stage++ }

            6 -> playerl(FaceAnim.ANNOYED, "How did I know it wouldn't be that simple for your vote?").also { stage++ }
            7 ->
                npcl(
                    FaceAnim.NEUTRAL,
                    "Calm yourself outerlander. It is but a small task really... I simply require a flower.",
                ).also { stage++ }

            8 -> playerl(FaceAnim.ASKING, "A flower? What's the catch?").also { stage++ }
            9 ->
                npcl(
                    FaceAnim.NEUTRAL,
                    "The catch? Well, it is not just any flower. Someone in this town has an extremely unique flower from a far off land that they picked up on their travels.",
                ).also { stage++ }

            10 ->
                npcl(
                    FaceAnim.NEUTRAL,
                    "I would like you to demonstrate your merchanting skills to me by persuading them to part with it, and then give it to me for my vote.",
                ).also { stage++ }

            11 -> playerl(FaceAnim.THINKING, "Well... I guess that doesn't sound too hard.").also { stage++ }
            12 -> npcl(FaceAnim.HAPPY, "Excellent! You will obtain this flower for me, then?").also { stage++ }
            13 -> options("Yes", "No").also { stage++ }
            14 ->
                when (buttonId) {
                    1 ->
                        playerl(
                            FaceAnim.ASKING,
                            "Okay. I don't think this will be too difficult. Any suggestions on where to start looking for this flower?",
                        ).also {
                            setAttribute(player, "/save:sigmund-started", true)
                            setAttribute(player, "/save:sigmund-steps", 1)
                            stage++
                        }

                    2 ->
                        playerl(
                            FaceAnim.ANNOYED,
                            "You know what? No. This all sounds like a lot of hassle to me, and frankly I just can't be bothered with it right now. I'll go get someone else to vote for me.",
                        ).also { stage = 20 }
                }

            15 ->
                npcl(
                    FaceAnim.THINKING,
                    "Ah, well outerlander, if I knew where to start looking I would simply do it myself!",
                ).also { stage++ }

            16 -> playerl(FaceAnim.ANNOYED, "No help at ALL?").also { stage++ }
            17 ->
                npcl(
                    FaceAnim.NEUTRAL,
                    "We are a very insular clan, so I would not expect you to have to leave this town to find whatever you need.",
                ).also { stage = 1000 }

            20 ->
                npcl(
                    FaceAnim.NEUTRAL,
                    "As you wish outlander. If you change your mind, come and see me again; I am very interested in getting my hands on that flower",
                ).also { stage = 1000 }

            25 ->
                npcl(
                    FaceAnim.ASKING,
                    "So... how goes it outerlander? Did you manage to obtain my flower for me yet? Or do you lack the necessary merchanting skills?",
                ).also { stage++ }

            26 ->
                playerl(
                    FaceAnim.ASKING,
                    "I'm still working on it... Do you have any suggestion where to start looking for it?",
                ).also { stage++ }

            27 ->
                npcl(
                    FaceAnim.THINKING,
                    "I suggest you ask around the other Fremennik in the town. A good merchant will find exactly what their customer needs somewhere.",
                ).also { stage = 1000 }

            30 ->
                npcl(
                    FaceAnim.AMAZED,
                    "Incredible! Your merchanting skills might even match my own! I have no choice but to recommend you to the council of elders!",
                ).also {
                    removeItem(player, Items.EXOTIC_FLOWER_3698)
                    removeAttributes(player, "sigmund-steps", "sigmund-started", "sigmundreturning")
                    setAttribute(player, "/save:fremtrials:votes", getAttribute(player, "fremtrials:votes", 0) + 1)
                    setAttribute(player, "/save:fremtrials:sigmund-vote", true)
                    stage = 1000
                }

            35 ->
                playerl(
                    FaceAnim.ASKING,
                    "I'm still working on it... Do you have any suggestion where to start looking for it?",
                ).also { stage++ }

            36 ->
                npcl(
                    FaceAnim.ASKING,
                    "I suggest you ask around the other Fremennik in the town. A good merchant will find exactly what their customer needs somewhere.",
                ).also { stage++ }

            37 -> playerl(FaceAnim.ASKING, "I was making some trades, but then I lost the goods...").also { stage++ }
            38 ->
                npcl(
                    FaceAnim.THINKING,
                    "Hmmm... well try and start again at the beginning. And try to be more careful of your wares in future.",
                ).also {
                    addItemOrDrop(player, Items.PROMISSORY_NOTE_3709, 1)
                    stage = 1000
                }

            40 ->
                npcl(
                    FaceAnim.HAPPY,
                    "Hello again outerlander! I am amazed once more at your apparent skill at merchanting!",
                ).also { stage++ }

            41 -> playerl(FaceAnim.HAPPY, "So I can count on your vote at the council of elders?").also { stage++ }
            42 ->
                npcl(
                    FaceAnim.HAPPY,
                    "Absolutely, outerlander. Your merchanting skills will be a real boon to the Fremennik.",
                ).also { stage = 1000 }

            50 ->
                npcl(
                    FaceAnim.HAPPY,
                    "Greetings again $gender $fName! What can I do for you this day?",
                ).also { stage++ }

            51 -> options("Can I see your wares?", "Nothing thanks").also { stage++ }
            52 ->
                when (buttonId) {
                    1 -> playerl(FaceAnim.HAPPY, "Can I see your wares?").also { stage++ }
                    2 -> playerl(FaceAnim.HAPPY, "Nothing thanks").also { stage = 55 }
                }

            53 -> npcl(FaceAnim.HAPPY, "Certainly, $fName.").also { stage = 54 }
            54 -> {
                end()
                openNpcShop(player, NPCs.SIGMUND_THE_MERCHANT_1282)
            }

            55 ->
                npcl(
                    FaceAnim.HAPPY,
                    "Well, feel free to stop by anytime you wish $fName. You are always welcome here!",
                ).also { stage = 1000 }

            60 ->
                npcl(
                    FaceAnim.HAPPY,
                    "Hello outerlander. By the laws of our tribe, I am afraid I may not speak to you without the express permission of the chieftain.",
                ).also { stage++ }

            61 -> playerl(FaceAnim.ASKING, "Where would I find him?").also { stage++ }
            62 -> npcl(FaceAnim.HAPPY, "In the longhall, outerlander.").also { stage = 1000 }
            1000 -> end()
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.SIGMUND_THE_MERCHANT_1282)
    }
}
