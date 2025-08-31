package content.region.kandarin.khazard.quest.arena.dialogue

import core.api.allInEquipment
import core.api.isQuestComplete
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.Items
import shared.consts.NPCs
import shared.consts.Quests

@Initializable
class KhazardGuardDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC

        val hasArmour = allInEquipment(player, Items.KHAZARD_HELMET_74, Items.KHAZARD_ARMOUR_75)
        val hasCompleteFightArena = isQuestComplete(player, Quests.FIGHT_ARENA)

        when{
            hasCompleteFightArena -> npcl(FaceAnim.FRIENDLY, "It's you! I don't believe it. You beat the General! You are a traitor to the uniform!").also { stage = END_DIALOGUE }
            !hasArmour -> playerl(FaceAnim.FRIENDLY, "Hi.").also { stage = 7 }
            else -> playerl(FaceAnim.FRIENDLY, "Hello.")
        }

        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> when(npc.id){
                NPCs.KHAZARD_GUARD_253 -> npcl(FaceAnim.ASKING, "Can I help you stranger?").also { stage = 14 }
                NPCs.KHAZARD_GUARD_256 -> npcl(FaceAnim.ASKING, "Despicable thieving scum, that was good armour. Did you see anyone around here soldier?").also { stage = 15 }
                else -> npcl(FaceAnim.FRIENDLY, "I've never seen you around here before!").also { stage++ }
            }
            1 -> playerl(FaceAnim.FRIENDLY, "Long live General Khazard!").also { stage++ }
            2 -> npcl(FaceAnim.FRIENDLY, "Erm.. yes.. soldier, I take it you're new around here?").also { stage++ }
            3 -> playerl(FaceAnim.FRIENDLY, "You could say that.").also { stage++ }
            4 -> npcl(FaceAnim.FRIENDLY, "Khazard died two hundred years ago. However his dark spirit remains in the form of the undead maniac General Khazard.").also { stage++ }
            5 -> npcl(FaceAnim.FRIENDLY, "Remember he is your master, always watching. Got that newbie?").also { stage++ }
            6 -> playerl(FaceAnim.FRIENDLY, "Undead, maniac, master. Got it, loud and clear.").also { stage = END_DIALOGUE }

            7 -> when(npc.id){
                NPCs.KHAZARD_GUARD_253 -> npcl(FaceAnim.ANNOYED, "I don't know you stranger. Get off our land.").also { stage = END_DIALOGUE }
                NPCs.KHAZARD_GUARD_255 -> npcl(FaceAnim.ANNOYED, "This area is restricted, leave now!").also { stage = 13 }
                NPCs.KHAZARD_GUARD_256 -> npcl(FaceAnim.ANNOYED, "I don't know who you are. Get out of my house stranger.").also { stage = END_DIALOGUE }
                else -> npcl(FaceAnim.ANNOYED, "This area is restricted. Leave now! OUT! And don't come back.").also { stage++ }
            }
            8 -> options("I apologise, I stumbled in here by mistake.", "You have no right to tell me where I can and cannot go.").also { stage++ }
            9 -> when (buttonId) {
                1 -> playerl(FaceAnim.HALF_WORRIED, "I apologise, I stumbled in here by mistake.").also { stage++ }
                2 -> playerl(FaceAnim.HALF_WORRIED, "You have no right to tell me where I can and cannot go.").also { stage = 11 }
            }
            10 -> npcl(FaceAnim.FRIENDLY, "Well, don't just stand there - get out of here!").also { stage = END_DIALOGUE }
            11 -> npcl(FaceAnim.FRIENDLY, "Fair enough. Let's do this the hard way.").also { stage++ }
            12 -> {
                end()
                npc.attack(player)
            }
            13 -> npcl(FaceAnim.ANGRY, "OUT! And don't come back!").also { stage = END_DIALOGUE }
            14 -> npcl(FaceAnim.FRIENDLY, "Oh, you're a guard as well. That's ok then. We don't like strangers around here.").also { stage = END_DIALOGUE }
            15 -> playerl(FaceAnim.SILENT, "Me? No, no one!").also { stage++ }
            16 -> npcl(FaceAnim.SUSPICIOUS, "Hmmmm").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(
        NPCs.KHAZARD_GUARD_253,
        NPCs.KHAZARD_GUARD_254,
        NPCs.KHAZARD_GUARD_255,
        NPCs.KHAZARD_GUARD_256
    )
}
