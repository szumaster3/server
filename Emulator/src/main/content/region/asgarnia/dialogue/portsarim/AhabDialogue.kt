package content.region.asgarnia.dialogue.portsarim

import core.api.quest.isQuestComplete
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class AhabDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        if (args.size > 1) {
            val isUsedTelegrabSpell = args[1] as Boolean
            if (!isUsedTelegrabSpell) {
                npc(FaceAnim.FURIOUS, "Oi! Get yer hands off me beer!")
            } else {
                npc(FaceAnim.FURIOUS, "Don't ye go castin' spells on me beer!")
            }
            stage = END_DIALOGUE
        } else {
            npc("Arrr, matey!")
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                options(
                    "Arrr!",
                    "Are you going to sit there all day?",
                    "Do you have anything for trade?",
                ).also { stage++ }
            1 ->
                when (buttonId) {
                    1 -> player("Arrr!").also { stage = 10 }
                    2 -> player("Are you going to sit there all day?").also { stage = 20 }
                    3 -> player("Do you have anything for trade?").also { stage = 38 }
                }
            10 -> npc("Arrr, matey!").also { stage = 0 }
            20 -> npc("Aye, I am. I canna walk, ye see.").also { stage++ }
            21 -> player("What's stopping you from walking?").also { stage++ }
            22 -> npc("Arrr, I'ave only the one leg! I lost its twin when my last", "ship went down.").also { stage++ }
            23 -> player("But I can see both your legs!").also { stage++ }
            24 ->
                npc(
                    "Nay, young laddie, this be a false leg. For years I had me a",
                    "sturdy wooden peg-leg, but now I wear this dainty little",
                    "feller.",
                ).also {
                    stage++
                }
            25 -> npc("Yon peg-leg kept getting stuck in the floorboards.").also { stage++ }
            26 -> player("Right...").also { stage++ }
            27 ->
                npc(
                    "Perhaps a bright young laddie like yerself would like to",
                    "help me? I be needing another ship to go a-hunting my",
                    "enemy.",
                ).also {
                    stage++
                }
            28 ->
                if (!isQuestComplete(player, Quests.DRAGON_SLAYER)) {
                    player("No, I don't have a ship.").also { stage = 39 }
                } else {
                    player("Well, I do have a ship that I'm not using.", "It's the Lady Lumbridge.").also { stage++ }
                }
            29 -> npc("Arrr! That ship be known to me, and a fine lass she is.").also { stage++ }
            30 -> player("I suppose she might be...").also { stage++ }
            31 -> npc("So would ye be kind enough to let", "me take her out to sea?").also { stage++ }
            32 -> player("I had to pay 2000gp for that ship! ", "Have you got that much?").also { stage++ }
            33 ->
                npc(
                    "Nay, I have nary a penny to my name. All my wordly goods",
                    "went down with me old ship.",
                ).also { stage++ }
            34 -> player("So you're actually asking me to give you a free ship.").also { stage++ }
            35 -> npc("Arrr! Would ye be so kind?").also { stage++ }
            36 -> player("No I jolly well wouldn't!").also { stage++ }
            37 -> npc("Arrr.").also { stage = END_DIALOGUE }
            38 ->
                npc("Nothin' at the moment, but then again the Customs", "Agents are on the warpath right now.").also {
                    stage =
                        END_DIALOGUE
                }
            39 -> npc("Arr matey... You make me sad.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.AHAB_2692)
    }
}
