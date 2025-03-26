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
import org.rs.consts.NPCs
import java.util.*

/**
 * The type Compost mound dialogue.
 */
@Initializable
class CompostMoundDialogue : Dialogue {
    override fun newInstance(player: Player): Dialogue {
        return CompostMoundDialogue(player)
    }

    /**
     * Instantiates a new Compost mound dialogue.
     */
    constructor()

    /**
     * Instantiates a new Compost mound dialogue.
     *
     * @param player the player
     */
    constructor(player: Player?) : super(player)

    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        options("Chat", "Withdraw", "Farming boost")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> when (buttonId) {
                1 -> when (Random().nextInt(6)) {
                    0 -> {
                        npc(
                            FaceAnim.CHILD_NORMAL,
                            "Schlorp, splort, splort, splutter shclorp?",
                            "(What we be doin' 'ere, zur?)"
                        )
                        stage = 124
                    }

                    1 -> {
                        npcl(FaceAnim.CHILD_NORMAL, "Oi've gotta braand new comboine 'aarvester!")
                        stage = 100
                    }

                    2 -> {
                        npcl(FaceAnim.CHILD_NORMAL, "What we be doin' 'ere, zur?")
                        stage = 104
                    }

                    3 -> {
                        npcl(FaceAnim.CHILD_NORMAL, "Errr...are ye gonna eat that?")
                        stage = 106
                    }

                    4 -> {
                        npcl(FaceAnim.CHILD_NORMAL, "Sigh...")
                        stage = 113
                    }

                    5 -> {
                        npcl(FaceAnim.CHILD_NORMAL, "Oi wus just a-wonderin'...")
                        stage = 117
                    }
                }

                2 -> {
                    end()
                    val forager = player.familiarManager.familiar as Forager
                    forager.openInterface()
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
                    end()
                    sendMessage(player, "Your stat cannot be boosted this way right now.")
                    return true
                }
                player.getSkills()
                    .updateLevel(Skills.FARMING, (1 + (getStatLevel(player, Skills.FARMING) * 0.02)).toInt())
                sendMessage(player, "The Compost mound has boosted your Farming stat.")
                end()
            }

            100 -> {
                playerl(FaceAnim.HALF_ASKING, "A what?")
                stage++
            }

            101 -> {
                npcl(
                    FaceAnim.CHILD_NORMAL,
                    "Well, it's a flat bit a metal wi' a 'andle that I can use ta 'aarvest all combintions o' plaants."
                )
                stage++
            }

            102 -> {
                playerl(FaceAnim.HALF_ASKING, "You mean a spade?")
                stage++
            }

            103 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Aye, 'aat'll be it.")
                stage = END_DIALOGUE
            }

            104 -> {
                playerl(FaceAnim.FRIENDLY, "Oh, I have a few things to take care of here, is all.")
                stage++
            }

            105 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Aye, right ye are, zur. Oi'll be roight there.")
                stage = END_DIALOGUE
            }

            106 -> {
                playerl(FaceAnim.HALF_ASKING, "Eat what?")
                stage++
            }

            107 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Y've got summat on yer, goin' wastin'.")
                stage++
            }

            108 -> {
                playerl(FaceAnim.DISGUSTED, "Ewwww!")
                stage++
            }

            109 -> {
                npcl(FaceAnim.CHILD_NORMAL, "So ye don' want it then?")
                stage++
            }

            110 -> {
                playerl(
                    FaceAnim.FRIENDLY,
                    "No I do not want it! Nor do I want to put my boot in your mouth for you to clean it off."
                )
                stage++
            }

            111 -> {
                npcl(FaceAnim.CHILD_NORMAL, "An' why not?")
                stage++
            }

            112 -> {
                playerl(FaceAnim.FRIENDLY, "It'll likely come out dirtier than when I put it in!")
                stage = END_DIALOGUE
            }

            113 -> {
                playerl(FaceAnim.HALF_ASKING, "What's the matter?")
                stage++
            }

            114 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Oi'm not 'appy carryin' round these young'uns where we're going.")
                stage++
            }

            115 -> {
                playerl(
                    FaceAnim.HALF_ASKING,
                    "Young'uns? Oh, the buckets of compost! Well, those wooden containers will keep them safe."
                )
                stage++
            }

            116 -> {
                npcl(FaceAnim.CHILD_NORMAL, "'Aah, that be a mighty good point, zur.")
                stage = END_DIALOGUE
            }

            117 -> {
                playerl(FaceAnim.HALF_ASKING, "Oh! What have you been eating! Your breath is making my eyes water!")
                stage++
            }

            118 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Oi! Oi'm 'urt by thaat.")
                stage++
            }

            119 -> {
                playerl(FaceAnim.SAD, "Sorry.")
                stage++
            }

            120 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Oi mean, oi even et some mints earlier.")
                stage++
            }

            121 -> {
                playerl(FaceAnim.HALF_ASKING, "You did?")
                stage++
            }

            122 -> {
                npcl(
                    FaceAnim.CHILD_NORMAL,
                    "'At's roight. Oi found some mint plaants in a big pile o' muck, and oi 'ad 'em fer me breakfast."
                )
                stage++
            }

            123 -> {
                playerl(FaceAnim.FRIENDLY, "The mystery resolves itself.")
                stage = END_DIALOGUE
            }

            124 -> {
                player("Oh, I have a few things to take care of here, is all.")
                stage++
            }

            125 -> {
                npc(
                    FaceAnim.CHILD_NORMAL,
                    "Schorp, splutter, splutter. Schlup schorp.",
                    "(Aye, right ye are, zur. Oi'll be roight there.)"
                )
                stage = END_DIALOGUE
            }
        }

        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.COMPOST_MOUND_6871, NPCs.COMPOST_MOUND_6872)
    }
}
