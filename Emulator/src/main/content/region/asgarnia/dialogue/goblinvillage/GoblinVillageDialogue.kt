package content.region.asgarnia.dialogue.goblinvillage

import core.api.quest.isQuestComplete
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.RandomFunction
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class GoblinVillageDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        val rand = RandomFunction.random(4)
        when (rand) {
            0 -> {
                end()
                npc(FaceAnim.OLD_DEFAULT, "I kill you human!")
                npc.attack(player)
            }

            1 -> npc(FaceAnim.OLD_DEFAULT, "Go away smelly human!").also { stage = 17 }
            2 -> npc(FaceAnim.OLD_DEFAULT, "Happy goblin new century!").also { stage = 0 }
            3 -> npc(FaceAnim.OLD_DEFAULT, "What you doing here?").also { stage = 2 }
            4 ->
                if (isQuestComplete(player, Quests.GOBLIN_DIPLOMACY)) {
                    npc(FaceAnim.OLD_DEFAULT, "Brown armour best!").also { stage = 7 }
                } else {
                    npc(FaceAnim.OLD_DEFAULT, "Go away smelly human!").also { stage = 17 }
                }
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> options("Happy new century!", "What is the goblin new century?").also { stage++ }
            1 ->
                when (buttonId) {
                    1 -> player(FaceAnim.HALF_GUILTY, "Happy new century!").also { stage = END_DIALOGUE }
                    2 -> player(FaceAnim.HALF_GUILTY, "What is the goblin new century?").also { stage = 10 }
                }

            2 -> options("I'm here to kill all you goblins!", "I'm just looking around.").also { stage++ }
            3 ->
                when (buttonId) {
                    1 -> player(FaceAnim.HALF_GUILTY, "I'm here to kill all you goblins!").also { stage++ }
                    2 -> player(FaceAnim.HALF_GUILTY, "I'm just looking around.").also { stage = 5 }
                }

            4 ->
                npc(FaceAnim.OLD_DEFAULT, "I kill you!").also {
                    end()
                    npc.attack(player)
                }
            5 ->
                npc(FaceAnim.OLD_DEFAULT, "Me not sure that allowed. You have to check with", "generals.").also {
                    stage =
                        END_DIALOGUE
                }
            6 -> npc(FaceAnim.OLD_DEFAULT, "I kill you human!").also { stage = 17 }
            7 -> options("Err, okay.", "Why is brown best?").also { stage++ }
            8 ->
                when (buttonId) {
                    1 -> player(FaceAnim.HALF_GUILTY, "Err, okay.").also { stage = END_DIALOGUE }
                    2 -> player(FaceAnim.THINKING, "Why is brown best?").also { stage++ }
                }
            9 ->
                npc(
                    FaceAnim.OLD_DEFAULT,
                    "General Wartface and General Bentnoze both say it is.",
                    "And normally they never agree!",
                ).also {
                    stage =
                        21
                }
            10 ->
                npcl(
                    FaceAnim.OLD_DEFAULT,
                    "Goblin century mark year of big battle on Plain of Mud. That when Big High War God give us commandments.",
                ).also {
                    stage++
                }
            11 ->
                options(
                    "Who is the Big High War God?",
                    "What are the goblin commandments?",
                    "Where is the Plain of Mud?",
                    "I need to go.",
                ).also {
                    stage++
                }
            12 ->
                when (buttonId) {
                    1 -> playerl(FaceAnim.HALF_ASKING, "Who is the Big High War God?").also { stage++ }
                    2 -> playerl(FaceAnim.HALF_ASKING, "What are the goblin commandments?").also { stage = 18 }
                    3 -> playerl(FaceAnim.HALF_ASKING, "Where is the Plain of Mud?").also { stage = 20 }
                    4 -> end()
                }
            13 ->
                npcl(
                    FaceAnim.OLD_DEFAULT,
                    "Big High War God take goblins and make them strong! Without him, we small and weak and stupid.",
                ).also {
                    stage++
                }
            14 ->
                npcl(
                    FaceAnim.OLD_DEFAULT,
                    "But thanks to Big High War God, we most powerful race in Gielinor!",
                ).also { stage++ }
            15 ->
                playerl(
                    FaceAnim.THINKING,
                    "Umm... you're clearly not the most powerful race in Gielinor.",
                ).also { stage++ }
            16 -> npcl(FaceAnim.OLD_DEFAULT, "Not to doubt word of Big High War God! I kill you!").also { stage++ }
            17 -> {
                end()
                npc.attack(player)
            }
            18 ->
                npcl(
                    FaceAnim.OLD_DEFAULT,
                    "Slay enemies of Big High War God! Not show mercy! Not run from battle! Not doubt word of Big High War God!",
                ).also {
                    stage++
                }
            19 ->
                npc(
                    FaceAnim.OLD_DEFAULT,
                    "Without commandments we not know right from wrong. We be like other races who not know war is good!",
                ).also {
                    stage =
                        17
                }
            20 -> npcl(FaceAnim.OLD_DEFAULT, "That goblin secret! No human ever find Plain of Mud!").also { stage = 17 }
            21 ->
                if (npc.id == NPCs.GOBLIN_4488 ||
                    npc.id == NPCs.GOBLIN_4489 ||
                    npc.id == NPCs.GOBLIN_4491 ||
                    npc.id == NPCs.GOBLIN_4492
                ) {
                    player(FaceAnim.THINKING, "But you're still wearing green armour!").also { stage++ }
                } else {
                    npcl(FaceAnim.OLD_DEFAULT, "But you're still wearing red armour!").also { stage += 2 }
                }
            22 -> npcl(FaceAnim.OLD_DEFAULT, "Um... my brown armour getting wash.").also { stage = END_DIALOGUE }
            23 -> npcl(FaceAnim.OLD_DEFAULT, "It not red, it just red-ish brown!").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(
            NPCs.GOBLIN_4483,
            NPCs.GOBLIN_4488,
            NPCs.GOBLIN_4489,
            NPCs.GOBLIN_4484,
            NPCs.GOBLIN_4491,
            NPCs.GOBLIN_4485,
            NPCs.GOBLIN_4486,
            NPCs.GOBLIN_4492,
            NPCs.GOBLIN_4487,
            NPCs.GOBLIN_4481,
            NPCs.GOBLIN_4479,
            NPCs.GOBLIN_4482,
            NPCs.GOBLIN_4480,
        )
    }
}
