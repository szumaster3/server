package content.region.karamja.quest.totem.dialogue

import core.api.*
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class KangaiMauDialogue(player: Player? = null, ) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        when {
            isQuestComplete(player, Quests.TRIBAL_TOTEM) -> {
                npcl(FaceAnim.HAPPY, "Many greetings esteemed thief.")
                stage = 28
            }
            player.questRepository.hasStarted(Quests.TRIBAL_TOTEM) -> {
                npcl(FaceAnim.ASKING, "Have you got our totem back?")
                stage = 24
            }
            else -> npcl(FaceAnim.HAPPY, "Hello. I'm Kangai Mau of the Rantuki Tribe.")
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> options("And what are you doing here in Brimhaven?", "I'm in search of adventure!", "Who are the Rantuki tribe?").also { stage++ }
            1 -> when (buttonId) {
                1 -> playerl(FaceAnim.ASKING, "And what are you doing here in Brimhaven?").also { stage = 5 }
                2 -> playerl(FaceAnim.HAPPY, "I'm in search of adventure!").also { stage = 15 }
                3 -> playerl(FaceAnim.ASKING, "Who are the Rantuki Tribe?").also { stage = 10 }
            }
            5 -> npcl(FaceAnim.HAPPY, "I'm looking for someone brave to go on important mission. Someone skilled in thievery and sneaking about.").also { stage++ }
            6 -> npcl(FaceAnim.HAPPY, "I am told I can find such people in Brimhaven.").also { stage++ }
            7 -> playerl(FaceAnim.HAPPY, "Yep. I have heard there are many of that type here.").also { stage++ }
            8 -> npcl(FaceAnim.THINKING, "Let's hope I find them.").also { stage = END_DIALOGUE }
            10 -> npcl(FaceAnim.HAPPY, "A proud and noble tribe of Karamja.").also { stage++ }
            11 -> npcl(FaceAnim.ANGRY, "But now we are few, as men come from across, steal our land, and settle on our hunting grounds").also { stage = END_DIALOGUE }
            15 -> npcl(FaceAnim.HAPPY, "Adventure is something I may be able to give. I need someone to go on a mission to the city of Ardougne.").also { stage++ }
            16 -> npcl(FaceAnim.HAPPY, "There you will find the house of Lord Handlemort. In his house he has our tribal totem. We need it back.").also { stage++ }
            17 -> playerl(FaceAnim.ASKING, "Why does he have it?").also { stage++ }
            18 -> npcl(FaceAnim.ANGRY, "Lord Handlemort is an Ardougnese explorer which means he think he have the right to come to my tribal home,").also { stage++ }
            19 -> npcl(FaceAnim.ANGRY, "steal our stuff and put in his private museum.").also { stage++ }
            20 -> playerl(FaceAnim.THINKING, "How can I find Handlemoret's house? Ardougne IS a big place...").also { stage++ }
            21 -> npcl(FaceAnim.ANNOYED, "I don't know Ardougne. You tell me.").also { stage++ }
            22 -> playerl(FaceAnim.HAPPY, "Ok, I will get it back.").also {
                player.questRepository.getQuest(Quests.TRIBAL_TOTEM).start(player)
                player.questRepository.getQuest(Quests.TRIBAL_TOTEM).setStage(player, 10)
                stage++
            }
            23 -> npcl(FaceAnim.HAPPY, "Best of luck with that adventurer").also { stage = END_DIALOGUE }
            24 -> if(inInventory(player, Items.TOTEM_1857, 1)) {
                playerl(FaceAnim.HAPPY, "Yes I have.").also { stage = 26 }
            } else {
                playerl(FaceAnim.SAD, "No, it's not that easy.").also { stage++ }
            }
            25 -> npc("Bah, you no good.").also { stage = END_DIALOGUE }
            26 -> npcl(FaceAnim.HAPPY, "You have??? Many thanks brave adventurer! Here, have some freshly cooked Karamjan fish, caught specially by my tribe.").also { stage++ }
            27 -> {
                end()
                removeItem(player, Items.TOTEM_1857)
                sendItemDialogue(player, Items.TOTEM_1857, "You hand over the Tribal Totem.")
                finishQuest(player, Quests.TRIBAL_TOTEM)
            }
            28 -> player(FaceAnim.NEUTRAL, "Hey.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.KANGAI_MAU_846)
}
