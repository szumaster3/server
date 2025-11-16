package content.global.skill.summoning.familiar.dialogue

import content.global.skill.summoning.familiar.Forager
import core.api.getDynLevel
import core.api.getStatLevel
import core.api.sendMessage
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.NPCs
import java.util.*

/**
 * Represents the Compost Mound familiar dialogue.
 */
@Initializable
class CompostMoundDialogue : Dialogue {
    override fun newInstance(player: Player?): Dialogue = CompostMoundDialogue(player)

    constructor()
    constructor(player: Player?) : super(player)

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        options("Chat", "Withdraw", "Farming boost")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> when (buttonId) {
                1 -> {
                    when (Random().nextInt(6)) {
                        0 -> {
                            npc(
                                FaceAnim.CHILD_NORMAL,
                                "Schlorp, splort, splort, splutter shclorp?",
                                "(What we be doin' 'ere, zur?)"
                            )
                            stage = 124
                        }

                        1 -> {
                            npcl(FaceAnim.CHILD_NORMAL, "Oi've gotta braand new comboine 'aarvester!"); stage = 100
                        }

                        2 -> {
                            npcl(FaceAnim.CHILD_NORMAL, "What we be doin' 'ere, zur?"); stage = 104
                        }

                        3 -> {
                            npcl(FaceAnim.CHILD_NORMAL, "Errr...are ye gonna eat that?"); stage = 106
                        }

                        4 -> {
                            npcl(FaceAnim.CHILD_NORMAL, "Sigh..."); stage = 113
                        }

                        5 -> {
                            npcl(FaceAnim.CHILD_NORMAL, "Oi wus just a-wonderin'..."); stage = 117
                        }
                    }
                }

                2 -> {
                    end()
                    (player.familiarManager.familiar as? Forager)?.openInterface()
                }

                3 -> {
                    player("Can you boost my Farming stat, please?")
                    stage = 30
                }
            }

            30 -> {
                npc("Schlup glorp sputter!", "(Oi do believe oi can!)")
                stage++
            }

            31 -> {
                if (getDynLevel(player, Skills.FARMING) > getStatLevel(player, Skills.FARMING)) {
                    sendMessage(player, "Your stat cannot be boosted this way right now.")
                    end()
                    return true
                } else {
                    val newLevel = (1 + (getStatLevel(player, Skills.FARMING) * 0.02)).toInt()
                    player.getSkills().updateLevel(Skills.FARMING, newLevel)
                    sendMessage(player, "The Compost mound has boosted your Farming stat.")
                    end()
                    return true
                }
            }

            100 -> playerl(FaceAnim.HALF_ASKING, "A what?").also { stage++ }
            101 -> npcl(FaceAnim.CHILD_NORMAL, "Well, it's a flat bit a metal wi' a 'andle that I can use ta 'aarvest all combintions o' plaants.").also { stage++ }
            102 -> playerl(FaceAnim.HALF_ASKING, "You mean a spade?").also { stage++ }
            103 -> npcl(FaceAnim.CHILD_NORMAL, "Aye, 'aat'll be it.").also { stage = END_DIALOGUE }
            104 -> playerl(FaceAnim.FRIENDLY, "Oh, I have a few things to take care of here, is all.").also { stage++ }
            105 -> npcl(FaceAnim.CHILD_NORMAL, "Aye, right ye are, zur. Oi'll be roight there.").also { stage = END_DIALOGUE }
            106 -> playerl(FaceAnim.HALF_ASKING, "Eat what?").also { stage++ }
            107 -> npcl(FaceAnim.CHILD_NORMAL, "Y've got summat on yer, goin' wastin'.").also { stage++ }
            108 -> playerl(FaceAnim.DISGUSTED, "Ewwww!").also { stage++ }
            109 -> npcl(FaceAnim.CHILD_NORMAL, "So ye don' want it then?").also { stage++ }
            110 -> playerl(FaceAnim.FRIENDLY, "No I do not want it! Nor do I want to put my boot in your mouth for you to clean it off.").also { stage++ }
            111 -> npcl(FaceAnim.CHILD_NORMAL, "An' why not?").also { stage++ }
            112 -> playerl(FaceAnim.FRIENDLY, "It'll likely come out dirtier than when I put it in!").also { stage = END_DIALOGUE }
            113 -> playerl(FaceAnim.HALF_ASKING, "What's the matter?").also { stage++ }
            114 -> npcl(FaceAnim.CHILD_NORMAL, "Oi'm not 'appy carryin' round these young'uns where we're going.").also { stage++ }
            115 -> playerl(FaceAnim.HALF_ASKING, "Young'uns? Oh, the buckets of compost! Well, those wooden containers will keep them safe.").also { stage++ }
            116 -> npcl(FaceAnim.CHILD_NORMAL, "'Aah, that be a mighty good point, zur.").also { stage = END_DIALOGUE }
            117 -> playerl(FaceAnim.HALF_ASKING, "Oh! What have you been eating! Your breath is making my eyes water!").also { stage++ }
            118 -> npcl(FaceAnim.CHILD_NORMAL, "Oi! Oi'm 'urt by thaat.").also { stage++ }
            119 -> playerl(FaceAnim.SAD, "Sorry.").also { stage++ }
            120 -> npcl(FaceAnim.CHILD_NORMAL, "Oi mean, oi even et some mints earlier.").also { stage++ }
            121 -> playerl(FaceAnim.HALF_ASKING, "You did?").also { stage++ }
            122 -> npcl(FaceAnim.CHILD_NORMAL, "'At's roight. Oi found some mint plaants in a big pile o' muck, and oi 'ad 'em fer me breakfast.").also { stage++ }
            123 -> playerl(FaceAnim.FRIENDLY, "The mystery resolves itself.").also { stage = END_DIALOGUE }
            124 -> player("Oh, I have a few things to take care of here, is all.").also { stage++ }
            125 -> npc(FaceAnim.CHILD_NORMAL, "Schorp, splutter, splutter. Schlup schorp.", "(Aye, right ye are, zur. Oi'll be roight there.)").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.COMPOST_MOUND_6871, NPCs.COMPOST_MOUND_6872)
}