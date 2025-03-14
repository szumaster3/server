package content.region.karamja.quest.mm.dialogue

import content.region.karamja.quest.mm.cutscene.DungeonPlanWithAwowogeiCutscene
import core.api.addItem
import core.api.anyInEquipment
import core.api.quest.getQuestStage
import core.api.quest.setQuestStage
import core.api.sendDialogue
import core.api.sendItemDialogue
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class GarkorDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        if (anyInEquipment(player, *itemIds)) {
            npcl(
                FaceAnim.AMAZED,
                "My my, Zooknock has outdone himself this time. You do look very much like a monkey you know.",
            )
            return true
        }
        when (getQuestStage(player, Quests.MONKEY_MADNESS)) {
            FIRST_TALK -> playerl(FaceAnim.NEUTRAL, "Hello?").also { stage = 6 }
            AFTER_CHALLENGE ->
                npcl(
                    FaceAnim.OLD_DEFAULT,
                    "Well done on winning Awowogei trust. I overheard everything from here.",
                ).also { stage = 21 }

            FINAL_BATTLE -> playerl(FaceAnim.WORRIED, "What shall we do?").also { stage = 28 }
            AFTER_BATTLE ->
                npcl(
                    FaceAnim.OLD_HAPPY,
                    "Well done, human! That was a most impressive display of skill.",
                ).also { stage = 47 }

            else -> sendDialogue(player, "Caranock seems too busy to talk.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                npcl(
                    FaceAnim.OLD_DEFAULT,
                    "My my, Zooknock has outdone himself this time. You do look very much like a monkey you know.",
                ).also { stage++ }

            1 -> playerl(FaceAnim.NEUTRAL, "I know.").also { stage++ }
            2 ->
                npcl(
                    FaceAnim.OLD_DEFAULT,
                    "And by happy coincidence you appear to be just the right sort of monkey.",
                ).also { stage++ }

            3 ->
                npcl(
                    FaceAnim.OLD_DEFAULT,
                    "I need you now to seek audience with Awowogei. Claim you are an envoy from the monkeys of Karamja and are seeking an alliance.",
                ).also { stage++ }

            4 -> npcl(FaceAnim.OLD_DEFAULT, "You must win his trust if we are to succeed.").also { stage++ }
            5 -> {
                end()
                setQuestStage(player!!, Quests.MONKEY_MADNESS, 35)
            }

            6 ->
                npcl(
                    FaceAnim.OLD_DEFAULT,
                    "A fine day you have chosen to visit this hellish island, human.",
                ).also { stage++ }

            7 ->
                playerl(
                    FaceAnim.NEUTRAL,
                    "Good day to you too Sergeant. I've been sent by your King Narnode to -",
                ).also { stage++ }

            8 ->
                npcl(
                    FaceAnim.OLD_DEFAULT,
                    "Investigate the circumstances surrounding the mysterious disappearance of my squad. Yes, I know...",
                ).also { stage++ }

            9 -> playerl(FaceAnim.NEUTRAL, "How did you know that?").also { stage++ }
            10 ->
                npcl(
                    FaceAnim.OLD_DEFAULT,
                    "The King and I are still in communication, albeit sporadic. I decided I need a human of your calibre to assist me. It is pleasing to see you are still alive.",
                ).also { stage++ }

            11 -> playerl(FaceAnim.NEUTRAL, "Why do you need a human?").also { stage++ }
            12 ->
                npcl(
                    FaceAnim.OLD_DEFAULT,
                    "There is more going on than meets your eye, human. Did you not find it strange that an entire squad be sent to decommission a shipyard?",
                ).also { stage++ }

            13 -> playerl(FaceAnim.NEUTRAL, "Well -").also { stage++ }
            14 ->
                npcl(
                    FaceAnim.OLD_DEFAULT,
                    "Indeed. But there are more pressing matters at hand. Three of my squad have been captured and placed in the jail. They are watched over by somewhat overpowering guards.",
                ).also { stage++ }

            15 ->
                npcl(
                    FaceAnim.OLD_DEFAULT,
                    "Before we can resume our original mission we must rescue them.",
                ).also { stage++ }

            16 ->
                playerl(
                    FaceAnim.NEUTRAL,
                    "I know about the guards - I had to sneak out between the change of shifts.",
                ).also { stage++ }

            17 ->
                npcl(
                    FaceAnim.OLD_DEFAULT,
                    "Trust me; we too have considered this, but whilst it is possible for one, it is near impossible for three.",
                ).also { stage++ }

            18 ->
                npcl(
                    FaceAnim.OLD_DEFAULT,
                    "We have considered many things. I have my squad mage and sappers working below us right now. My assassin Karam, is operating in the village itself.",
                ).also { stage++ }

            19 -> npcl(FaceAnim.OLD_DEFAULT, "I remain here so that I may overhear Awowogei's plans.").also { stage++ }
            20 -> {
                end()
                setQuestStage(player!!, Quests.MONKEY_MADNESS, 30)
            }

            21 -> npcl(FaceAnim.OLD_DEFAULT, "However, you efforts may be in vain...").also { stage++ }
            22 -> playerl(FaceAnim.NEUTRAL, "What do you mean?").also { stage++ }
            23 ->
                npcl(
                    FaceAnim.OLD_DEFAULT,
                    "Progress in the caves has been slow. Whilst you were in Ardougne, Bunkwicket and Waymottin overheard a slightly disturbing conversation.",
                ).also { stage++ }

            24 -> playerl(FaceAnim.NEUTRAL, "Who was speaking? What was said?").also { stage++ }
            25 ->
                npcl(FaceAnim.OLD_DEFAULT, "Listen closely whilst I narrate the details...").also {
                    setQuestStage(player!!, Quests.MONKEY_MADNESS, 50)
                    DungeonPlanWithAwowogeiCutscene(player!!).start()
                    stage++
                }

            26 -> {
                end()

                stage = 27
            }

            27 -> end()
            28 -> npcl(FaceAnim.OLD_DEFAULT, "Zooknock and I have come up with a plan.").also { stage++ }
            29 -> playerl(FaceAnim.NEUTRAL, "What kind of plan?").also { stage++ }
            30 ->
                npcl(
                    FaceAnim.OLD_DEFAULT,
                    "I hope you were listening closely. The teleportation spell that was provided will teleport ALL of the 10th squad, no matter where we may be.",
                ).also { stage++ }

            31 ->
                npcl(
                    FaceAnim.OLD_DEFAULT,
                    "In effect, the spell will break Lumo, Bunkdo and Carado out of the jail for us.",
                ).also { stage++ }

            32 ->
                playerl(
                    FaceAnim.NEUTRAL,
                    "But you will be teleported straight into whatever trap they have prepared!",
                ).also { stage++ }

            33 ->
                npcl(
                    FaceAnim.OLD_DEFAULT,
                    "Indeed. This is where you come in. Do not forget that we are the 10th squad of the Royal Guard and that we are more than capable of holding our own.",
                ).also { stage++ }

            34 ->
                npcl(
                    FaceAnim.OLD_DEFAULT,
                    "With your assistance, we should be able to defeat whatever is thrown at us.",
                ).also { stage++ }

            35 -> playerl(FaceAnim.NEUTRAL, "But how will i join you?").also { stage++ }
            36 ->
                npcl(
                    FaceAnim.OLD_DEFAULT,
                    "Simple. We fool the teleportation spell that you are in fact a member of our squad.",
                ).also { stage++ }

            37 -> playerl(FaceAnim.NEUTRAL, "What?").also { stage++ }
            38 ->
                npcl(
                    FaceAnim.OLD_DEFAULT,
                    "Zooknock knows Glough's grasp of magic well. He believes that spell is linked to the sigils that all of us our squad carry",
                ).also { stage++ }

            39 ->
                npcl(
                    FaceAnim.OLD_DEFAULT,
                    "It is these sigils that identify us as a member of the squad.",
                ).also { stage++ }

            40 ->
                sendItemDialogue(
                    player!!,
                    Items.TENTH_SQUAD_SIGIL_4035,
                    "Garkor hands you some kind of medallion.",
                ).also {
                    addItem(player!!, Items.TENTH_SQUAD_SIGIL_4035, 1)
                    stage++
                }

            41 -> npcl(FaceAnim.OLD_DEFAULT, "Welcome to the 10th squad, ${player!!.name}.").also { stage++ }
            42 -> playerl(FaceAnim.NEUTRAL, "What is it?").also { stage++ }
            43 ->
                npcl(
                    FaceAnim.OLD_DEFAULT,
                    "It is a replica Waymottin has made of our squad sigils. If you wear that when the spell is cast, you will be summoned along with the rest of us.",
                ).also { stage++ }

            44 ->
                npcl(
                    FaceAnim.OLD_DEFAULT,
                    "You should prepare. Collect your thoughts and belongs and then wear the sigil. Hurry, human, we do not with to enter this fight without you.",
                ).also { stage++ }

            45 -> playerl(FaceAnim.NEUTRAL, "All i have to do is to wear the sigil?").also { stage++ }
            46 ->
                npcl(FaceAnim.OLD_DEFAULT, "Yes - but do not do so until you are ready.").also {
                    stage = END_DIALOGUE
                }

            47 -> playerl(FaceAnim.GUILTY, "Thank you").also { stage++ }
            48 ->
                npcl(
                    FaceAnim.OLD_DEFAULT,
                    "You should report to king Narnode immediately. Tell him that the 10th squad still survives and has suffered no casualries.",
                ).also { stage++ }

            49 -> playerl(FaceAnim.NEUTRAL, "Rest assured, I will do so.").also { stage++ }
            50 -> playerl(FaceAnim.NEUTRAL, "How do I leave this place?").also { stage++ }
            51 -> npcl(FaceAnim.OLD_DEFAULT, "Speak to Zooknock. He will arrange for you to leave.").also { stage++ }
            52 -> {
                end()
                setQuestStage(player!!, Quests.MONKEY_MADNESS, 96)
            }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.GARKOR_1411)
    }

    companion object {
        val FIRST_TALK = 25
        val AFTER_BATTLE = 99
        val FINAL_BATTLE = 50
        val AFTER_CHALLENGE = 46

        val itemIds = IntArray(4031 - 4024 + 1) { it + 4024 }
    }
}
