package content.region.kandarin.yanille.dialogue

import core.api.*
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.dialogue.Topic
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.Items
import shared.consts.NPCs
import shared.consts.Quests

/**
 * Handles the Bert dialogue in The Hand in the Sand quest.
 */
@Initializable
class BertDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        if(getQuestStage(player, Quests.THE_HAND_IN_THE_SAND) >= 1) {
            npcl(FaceAnim.HALF_ASKING, "Did ye see yon Guard Capt'n 'bout hand?").also { stage = 11}
        } else if(getQuestStage(player, Quests.THE_HAND_IN_THE_SAND) == 3) {
            npcl(FaceAnim.HALF_ASKING, "Wha' info ye find 'bout hand, ${player.username}?").also { stage = 15 }
        } else if(getQuestStage(player, Quests.THE_HAND_IN_THE_SAND) == 4) {
            npcl(FaceAnim.HALF_ASKING, "Ey'up ${player.username}. Did yer see Sandy in Brimhaven 'bout me rota?").also { stage = 21 }
        } else {
            npcl(FaceAnim.SAD, "Eeee, wha' shall I do! I'll mos' certainly lose tha job...")
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        val hasBertRota = inInventory(player, Items.BERTS_ROTA_6947) || inBank(player, Items.BERTS_ROTA_6947)

        when(stage) {
            0 -> player(FaceAnim.HALF_ASKING, "Lose your job? What's wrong, why?").also { stage++ }
            1 -> npcl(FaceAnim.SAD, "I w-w-work...over yon sand pit...and weeell...I found...this...hand! T'were buried in't Sand!").also { stage++ }
            2 -> showTopics(
                Topic("Oh, you found a hand in the sand - that's nice for you.", END_DIALOGUE, false),
                Topic("Eww a hand, in the sand! Why haven't you told the authorities?", 3, true),
            )
            3 -> playerl(FaceAnim.FRIENDLY, "Eww a hand, in the sand! Why haven't you told the authorities?").also { stage++ }
            4 -> npcl(FaceAnim.SAD, "They's no' wha' they once was. Tha cap'ain o'the Guard spends near all o'the time drunk in yon pub.").also { stage++ }
            5 -> player(FaceAnim.HALF_ASKING, "Oh? The Guard Captain is drunk in the pub you say? That's not good, what will you do?").also { stage++ }
            6 -> npcl(FaceAnim.HAPPY, "Weeellll... do yer think yer could 'elp me?").also { stage++ }
            7 -> showTopics(
                Topic("I want no part in this!", END_DIALOGUE, true),
                Topic("Sure, I'll give you a hand.", 8, false)
            )
            8 -> npcl(FaceAnim.FRIENDLY, "....Nae, ye can 'have the 'and as h'evidence.").also { stage++ }
            9 -> sendItemDialogue(player, Items.SANDY_HAND_6945, "Bert gives you a rather smelly, sand covered hand.").also {
                addItemOrDrop(player, Items.SANDY_HAND_6945, 1)
                stage++
            }
            10 -> npcl(FaceAnim.FRIENDLY, "P'raps tha smell will get t'Guard Cap'ain's nose out o'his beer fer 2 seconds!").also {
                setQuestStage(player, Quests.THE_HAND_IN_THE_SAND, 1)
                stage = END_DIALOGUE
            }

            11 -> player(FaceAnim.HALF_GUILTY, "Not at the moment, but I will be seeing him soon.").also { stage++ }
            12 -> if(!inInventory(player, Items.SANDY_HAND_6945) || !inBank(player, Items.SANDY_HAND_6945)) {
                player(FaceAnim.SAD, "Err, I kind of... lost my grip on it.").also { stage++ }
            } else {
                end()
                stage = END_DIALOGUE
            }
            13 -> npcl(FaceAnim.HAPPY, "Thank t'gods! I tho' I searched up another when I been pickin' this'un up outside... Take tha' blasted thing to yon Guard Captain quick sharp.").also { stage++ }
            14 -> {
                end()
                addItemOrDrop(player, Items.SANDY_HAND_6945, 1)
                playerl(FaceAnim.FRIENDLY, "Thanks Bert, I'll go see the Guard Captain right now.")
                stage = END_DIALOGUE
            }

            15 -> player(FaceAnim.HALF_ASKING, "I dug up quite a lot about the hand. Can you tell me about your job?").also { stage++ }
            16 -> npcl(FaceAnim.HALF_ASKING, "Sand! Lots o' sand! Me boss be Sandy o' Sandy's Sand Corp based in Brimhaven on tha isle of Karamja an' I hauls sand fr' there to yon sand pit.").also { stage++ }
            17 -> npcl(FaceAnim.HALF_ASKING, "I's looong harrrrd hours, bu' keeps me busy, y'know what tha say! 'Idle hands'r Zamorak's tools.'").also { stage++ }
            18 -> player(FaceAnim.HALF_ASKING, "So you're employed by Sandy's Sand Corp in Brimhaven. Have you changed your hours recently?").also { stage++ }
            19 -> if(freeSlots(player) == 0) {
                npcl(FaceAnim.FRIENDLY, "Yer coul' see fer yerself iffen yer had space in yorn invent'ry, come back when yer do.").also { stage = END_DIALOGUE }
            } else {
                npcl(FaceAnim.FRIENDLY, "Nae! See fer yersel', here's a copy o' me rota tha' be held a' head office - yer can looksee iffin ye talk t' Sandy, me boss.").also {
                    addItem(player, Items.BERTS_ROTA_6947, 1)
                    stage++
                }
            }
            20 -> playerl(FaceAnim.HAPPY, "Thanks for the Rota Bert. I'll go check for the original with Sandy in Brimhaven.").also {
                setQuestStage(player, Quests.THE_HAND_IN_THE_SAND, 4)
                stage = END_DIALOGUE
            }

            21 -> if(!hasBertRota) {
                player(FaceAnim.HALF_GUILTY, "Err, no, I kind of... lost it...").also { stage++ }
            } else {
                player("No, I'll fit it in my schedule somewhere soon.").also { stage = END_DIALOGUE }
            }

            22 -> if(freeSlots(player) == 0) {
                npcl(FaceAnim.FRIENDLY, "Lucky fer yorn tha' I's made a copy then ain't it, I's 'ave been given it to yer iffen you 'ad some space in yer invent'ry.").also { stage++ }
            } else {
                npcl(FaceAnim.FRIENDLY, "Lucky fer yorn tha' I's made a copy then ain't it, 'ere 'ave another.")
                addItem(player, Items.BERTS_ROTA_6947, 1)
                stage = END_DIALOGUE
            }
            23 -> player(FaceAnim.ASKING, "What did you say?").also { stage++ }
            24 -> npcl(FaceAnim.HALF_THINKING, "Is you stupid? I said I'd give ye another but yer bags be full!").also { stage = END_DIALOGUE }

        }

        // when (stage) {
        //     0 -> playerl(FaceAnim.FRIENDLY, "Bert! Good news!").also { stage++ }
        //     1 -> npcl(FaceAnim.FRIENDLY, "Arrr...Good news be always handy.").also { stage++ }
        //     2 -> playerl(FaceAnim.FRIENDLY, "They arrested Sandy for the murder of a wizard and the sand pit now refills itself!").also { stage++ }
        //     3 -> npcl(FaceAnim.FRIENDLY, "ME JOB! I'VE LOSTED ME JOB! 'ow c'n yer say tha' be good news?? Me wife'll tear me limb fr'm limb!").also { stage++ }
        //     4 -> playerl(FaceAnim.FRIENDLY, "Don't worry, the Wizards are going to pay you a large pension so that you can retire...").also { stage++ }
        //     5 -> npcl(FaceAnim.FRIENDLY, "Bu' wha'll I be doin' wit' me day now! I be lovin' tha sand.").also { stage++ }
        //     6 -> playerl(FaceAnim.FRIENDLY, "What will you do with your day? Well....You could build sand castles with your own two hands!").also { stage++ }
        //     7 -> npcl(FaceAnim.FRIENDLY, "I din't think so... bu' iffen yer ever need someone ta haul buckets o'sand 'round, ye be lettin' me know ${player!!.username}, I's can help yer!").also { stage++ }
        //     8 -> playerl(FaceAnim.FRIENDLY, "Wow! That would be great, buckets of sand direct to bank everday you say? That's great!").also { stage++ }
        //     9 -> end()
        // }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = BertDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.BERT_3108)
}
