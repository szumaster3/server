package content.region.kandarin.yanille.quest.handsand

import content.data.GameAttributes
import core.api.*
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.dialogue.Topic
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.RandomFunction
import shared.consts.Items
import shared.consts.NPCs
import shared.consts.Quests
import shared.consts.Vars

/**
 * Handles the Sandy dialogue in The Hand in the Sand quest.
 */
@Initializable
class SandyDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        val handProgress = getVarbit(player, Vars.VARBIT_QUEST_THE_HAND_IN_THE_SAND_PROGRESS_1527)
        val questStage = getQuestStage(player, Quests.THE_HAND_IN_THE_SAND)

        when {
            questStage == 4 -> {
                player(FaceAnim.HALF_ASKING, "Hello Sir, do you run the Sand Corp?")
            }
            inInventory(player, Items.SANDYS_ROTA_6948) -> {
                sendDialogue(player, "You already have the rota, perhaps you should take both rotas back to Bert in Yanille.")
                stage = END_DIALOGUE
            }
            handProgress == 5 -> {
                npcl(FaceAnim.NEUTRAL, "I don't have time to talk to you. Go away!")
                stage = 7
            }
            inInventory(player, Items.MAGICAL_ORB_6950) && questStage == 9 -> {
                sendDialogue(player, "You need to activate the magical scrying orb, obtained from the wizard in Yanille, to capture the conversation with Sandy!")
            }
            inInventory(player, Items.MAGICAL_ORB_A_6951) -> {
                player("Now, I'm going to ask you some questions and I want", "you to answer me truthfully...")
                stage = 9
            }
            questStage == 10 -> {
                npcl(FaceAnim.NEUTRAL, "I don't have time to talk to you. Go away!")
                stage = END_DIALOGUE
            }
            else -> {
                npcl(FaceAnim.NEUTRAL, "Sand is yellow,")
                stage = 4
            }
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        val handProgress = getVarbit(player, Vars.VARBIT_QUEST_THE_HAND_IN_THE_SAND_PROGRESS_1527)
        when (stage) {
            0 -> npcl(FaceAnim.HALF_ASKING, "Who wants to know?").also { stage++ }
            1 -> player(FaceAnim.HALF_ASKING, "I'm [player name]. I'm here investigating the possible murder of a wizard.").also { stage++ }
            2 -> npcl(FaceAnim.HALF_ASKING, "I don't care about that, I have far too much work to do. Let the authorities take care of things like murder and stop snooping around my office!").also { stage++ }
            3 -> sendDialogue(player, "Sand seems very keen to get you out of the office, perhaps you should take a look around.").also {
                setQuestStage(player, Quests.THE_HAND_IN_THE_SAND, 5)
                setVarbit(player, Vars.VARBIT_QUEST_THE_HAND_IN_THE_SAND_PROGRESS_1527, 2, true)
                stage = END_DIALOGUE
            }

            4 -> npcl(FaceAnim.NEUTRAL, "Sand is grand,").also { stage++ }
            5 -> npcl(FaceAnim.NEUTRAL, "Sand puts money,").also { stage++ }
            6 -> npcl(FaceAnim.NEUTRAL, "In my hand!").also { stage = END_DIALOGUE }
            7 -> showTopics(
                Topic("There's a herd of huge mutant herring about to drop from the sky!",8),
                Topic("But the pygmy shrews have eaten all the sand!", 8),
                Topic("A small parrot with a pink banana is sitting outside your window!", 8)
            )
            8 -> {
                end()
                val distract = RandomFunction.random(0,3)
                if(distract == 1) {
                    npc("Wow! I must see this!").also { stage = END_DIALOGUE }
                    setVarbit(player, 1535, 1, true)
                    sendMessage(player, "Sandy turns to look out of the window, now is your chance!")
                } else {
                    npc("I'm not falling for that one!").also { stage = END_DIALOGUE }
                }
            }
            9 -> {
                val topics = mutableListOf<Topic<Int>>()
                if (!hasAsked(player, 0)) topics.add(Topic("Why is Bert's rota different from the original?", 10))
                if (!hasAsked(player, 1)) topics.add(Topic("Why doesn't Bert remember the change in his hours?", 11))
                if (!hasAsked(player, 2)) topics.add(Topic("What happened to the wizard?", 14))
                topics.add(Topic("Ok, I'm done with you.", 16))
                showTopics(*topics.toTypedArray())
            }
            10 -> npcl(FaceAnim.NEUTRAL, "Because... I changed it.").also { setAsked(player, 0); stage = 9 }
            11 -> npcl(FaceAnim.NEUTRAL, "Because.... because.... I bribed a wizard to put a spell on him so he would believe everything I say!!").also { setAsked(player, 1); stage++ }
            12 -> player(FaceAnim.HALF_ASKING, "Why?").also { stage++ }
            13 -> npcl(FaceAnim.NEUTRAL, "So that I could make him work longer without paying him more!").also { stage = 9 }
            14 -> npcl(FaceAnim.HALF_GUILTY, "I...I... I KILLED HIM!").also { setAsked(player, 2); stage++ }
            15 -> npcl(FaceAnim.HALF_GUILTY, "So I wouldn't have to pay him and no one would know. I put his body in the next load of sand.").also { stage = 9 }
            16 -> {
                val mask = player.getAttribute<Int>(GameAttributes.HAND_SAND_SANDY_ANSWERS) ?: 0
                if (mask != 0b111) {
                    sendDialogue(player, "You need to ask Sandy all the questions.").also { stage = 9 }
                } else {
                    playerl(FaceAnim.HALF_THINKING, "I think I have enough evidence now, you can go for now, but I think you're up to your neck in it!").also { stage++ }
                }
            }
            17 -> {
                end()
                setQuestStage(player, Quests.THE_HAND_IN_THE_SAND, 10)
                removeAttribute(player, GameAttributes.HAND_SAND_SANDY_ANSWERS)
                sendItemDialogue(player, Items.MAGICAL_ORB_A_6951, "Sandy has told you all he knows. The magical orb is full and needs to be returned to the Wizard in Yanille.")
            }

        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = SandyDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.SANDY_3112)

    private fun hasAsked(player: Player, index: Int): Boolean {
        val mask = player.getAttribute<Int>(GameAttributes.HAND_SAND_SANDY_ANSWERS) ?: 0
        return (mask and (1 shl index)) != 0
    }

    private fun setAsked(player: Player, index: Int) {
        val mask = player.getAttribute<Int>(GameAttributes.HAND_SAND_SANDY_ANSWERS) ?: 0
        player.setAttribute(GameAttributes.HAND_SAND_SANDY_ANSWERS, mask or (1 shl index))
    }
}
