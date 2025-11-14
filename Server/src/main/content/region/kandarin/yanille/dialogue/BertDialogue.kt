package content.region.kandarin.yanille.dialogue

import content.data.GameAttributes
import content.region.kandarin.yanille.quest.handsand.TheHandintheSand
import core.api.*
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.dialogue.Topic
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.Items
import shared.consts.NPCs
import shared.consts.Quests
import shared.consts.Vars

/**
 * Handles the Bert dialogue in The Hand in the Sand quest.
 */
@Initializable
class BertDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        val store = TheHandintheSand.getStoreFile()
        val username = player?.username?.lowercase() ?: ""
        val alreadyClaimed = store[username]?.asBoolean ?: false
        val hasBankSpace = hasSpaceFor(player, Item(Items.BUCKET_OF_SAND_1783, 1)) ?: false
        val questStage = getQuestStage(player, Quests.THE_HAND_IN_THE_SAND)

        if (isQuestComplete(player, Quests.THE_HAND_IN_THE_SAND)) {
            when {
                alreadyClaimed -> {
                    npc("'Ello there, ${player?.username}! Hope yer be havin' fun. I be a wee bi' busy to 'elp wit' yon sand. Come back tomorrow.")
                    stage = END_DIALOGUE
                }

                !hasBankSpace -> {
                    npc("Eeee, ye've go' no free space in yon bank, ${player?.username}. Make some room 'tween the fishes 'n boots fore ye come back ta me.")
                    stage = END_DIALOGUE
                }

                else -> {
                    val randomDialogue = (1..4).random()
                    when (randomDialogue) {
                        1 -> {
                            npcl(FaceAnim.HAPPY, "Mornin' ${player.username}!")
                            stage = 100
                        }
                        2 -> {
                            npcl(FaceAnim.HAPPY, "'Ello there ${player.username}!")
                            stage = 110
                        }
                        3 -> {
                            npcl(FaceAnim.HAPPY, "I thought i' were you comin' in ${player.username}. I sposin' you be wantin' yer sand today?")
                            stage = 120
                        }
                        4 -> {
                            npcl(FaceAnim.HAPPY, "'Ello again ${player.username}!")
                            stage = 130
                        }
                    }
                }
            }
            return true
        }

        when (questStage) {
            1 -> {
                npcl(FaceAnim.HALF_ASKING, "Did ye see yon Guard Capt'n 'bout hand?")
                stage = 11
            }
            3 -> {
                npcl(FaceAnim.HALF_ASKING, "Wha' info ye find 'bout hand, ${player.username}?")
                stage = 15
            }
            4 -> {
                npcl(FaceAnim.HALF_ASKING, "Ey'up ${player.username}. Did yer see Sandy in Brimhaven 'bout me rota?")
                stage = 21
            }
            6 -> {
                npcl(FaceAnim.HALF_ASKING, "I be hopin' tha search is goin' well... are tha wizard's owning up ta anythin' yet?")
                stage = 29
            }
            else -> {
                if (inInventory(player, Items.SANDYS_ROTA_6948)) {
                    playerl(FaceAnim.FRIENDLY, "I managed to get a copy of the original rota. Your hours changed a week ago!")
                    stage = 25
                } else {
                    npcl(FaceAnim.SAD, "Eeee, wha' shall I do! I'll mos' certainly lose tha job...")
                }
            }
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
            5 -> playerl(FaceAnim.HALF_ASKING, "Oh? The Guard Captain is drunk in the pub you say? That's not good, what will you do?").also { stage++ }
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
                setVarbit(player, Vars.VARBIT_QUEST_THE_HAND_IN_THE_SAND_PROGRESS_1527, 1, true)
                setVarbit(player, 1535, 0, true)
                stage = END_DIALOGUE
            }

            11 -> playerl(FaceAnim.HALF_GUILTY, "Not at the moment, but I will be seeing him soon.").also { stage++ }
            12 -> if(!inInventory(player, Items.SANDY_HAND_6945) || !inBank(player, Items.SANDY_HAND_6945)) {
                playerl(FaceAnim.SAD, "Err, I kind of... lost my grip on it.").also { stage++ }
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

            15 -> playerl(FaceAnim.HALF_ASKING, "I dug up quite a lot about the hand. Can you tell me about your job?").also { stage++ }
            16 -> npcl(FaceAnim.HALF_ASKING, "Sand! Lots o' sand! Me boss be Sandy o' Sandy's Sand Corp based in Brimhaven on tha isle of Karamja an' I hauls sand fr' there to yon sand pit.").also { stage++ }
            17 -> npcl(FaceAnim.HALF_ASKING, "I's looong harrrrd hours, bu' keeps me busy, y'know what tha say! 'Idle hands'r Zamorak's tools.'").also { stage++ }
            18 -> playerl(FaceAnim.HALF_ASKING, "So you're employed by Sandy's Sand Corp in Brimhaven. Have you changed your hours recently?").also { stage++ }
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
                playerl(FaceAnim.HALF_GUILTY, "Err, no, I kind of... lost it...").also { stage++ }
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
            23 -> playerl(FaceAnim.ASKING, "What did you say?").also { stage++ }
            24 -> npcl(FaceAnim.HALF_THINKING, "Is you stupid? I said I'd give ye another but yer bags be full!").also { stage = END_DIALOGUE }


            25 -> npcl(FaceAnim.FRIENDLY, "Nae! Nae! I din't remember tha', bu'... hmmm, aye... tha' migh' be it...").also { stage++ }
            26 -> player(FaceAnim.HALF_ASKING, "What? Give me a hand here, I'm having a hard time understanding how you don't remember changing hours!").also { stage++ }
            27 -> npcl(FaceAnim.FRIENDLY, "I's all be tha wizard's fault! Tha magic leaks fr'm yon magic guild I tells yer! Tha's why this weirrrrd scroll appeareded a week ag").also { stage++ }
            28 -> player(FaceAnim.HALF_ASKING, "A scroll appeared? Can I take a look at it while you look at the rotas?").also {
                if(removeItem(player, Items.SANDYS_ROTA_6948)) addItem(player, Items.A_MAGIC_SCROLL_6949 , 1)
                setQuestStage(player, Quests.THE_HAND_IN_THE_SAND, 6)
                stage++
            }
            29 -> npcl(FaceAnim.FRIENDLY, "O'course ${player.username}, le's be 'avin'yon rota and 'ere be tha scroll, yer be takin' it back ta those inferrrnal wizards quick sharp!").also { stage = END_DIALOGUE }
            30 -> playerl(FaceAnim.NEUTRAL, "I've found out a lot and will let you know when it's all over.").also { stage = END_DIALOGUE }

            100 -> {
                player("Hello there Bert!")
                stage = 101
            }
            101 -> {
                npcl(FaceAnim.HAPPY, "I be goin' ou' on a limb and guessin' ye wan's yer sand today!")
                stage = 102
            }
            102 -> {
                player("Yes, I'd like the sand please Bert.")
                stage = 103
            }
            103 -> {
                npcl(FaceAnim.HAPPY, "I'll ge' righ' on it!")
                stage = 140
            }

            110 -> {
                player("Hope you're having a good day!")
                stage = 111
            }
            111 -> {
                npcl(FaceAnim.NEUTRAL, "Oh arr, no' too bad, though sometimes I could do wi' a second pair o' hands! I'll be gettin' on movin' them there buckets o' sand to yer bank!")
                stage = 112
            }
            112 -> {
                player("Thanks Bert, see you tomorrow!")
                stage = 140
            }

            120 -> {
                player("Aye...I mean, yes please Bert!")
                stage = 121
            }
            121 -> {
                npcl(FaceAnim.HAPPY, "I'll ge' on wit' movin' it. Thankee fer makin' sure Sandy go' it in t'neck fer 'is double dealin's!")
                stage = 122
            }
            122 -> {
                player("Excellent! While you move the sand to my bank, I'll do something else, see you later!")
                stage = 140
            }

            130 -> {
                player("Hello Bert, could I have my sand today please?")
                stage = 131
            }
            131 -> {
                npcl(FaceAnim.HAPPY, "Aye, give me a minute cause I be rushed off me feet!")
                stage = 132
            }
            132 -> {
                player("Thanks for the sand Bert!")
                stage = 140
            }

            140 -> {
                end()
                rewardSand(player,84)
            }

        }
        /*
        when (stage) {
            0 -> playerl(FaceAnim.FRIENDLY, "Bert! Good news!").also { stage++ }
            1 -> npcl(FaceAnim.FRIENDLY, "Arrr...Good news be always handy.").also { stage++ }
            2 -> playerl(FaceAnim.FRIENDLY, "They arrested Sandy for the murder of a wizard and the sand pit now refills itself!").also { stage++ }
            3 -> npcl(FaceAnim.FRIENDLY, "ME JOB! I'VE LOSTED ME JOB! 'ow c'n yer say tha' be good news?? Me wife'll tear me limb fr'm limb!").also { stage++ }
            4 -> playerl(FaceAnim.FRIENDLY, "Don't worry, the Wizards are going to pay you a large pension so that you can retire...").also { stage++ }
            5 -> npcl(FaceAnim.FRIENDLY, "Bu' wha'll I be doin' wit' me day now! I be lovin' tha sand.").also { stage++ }
            6 -> playerl(FaceAnim.FRIENDLY, "What will you do with your day? Well....You could build sand castles with your own two hands!").also { stage++ }
            7 -> npcl(FaceAnim.FRIENDLY, "I din't think so... bu' iffen yer ever need someone ta haul buckets o'sand 'round, ye be lettin' me know ${player!!.username}, I's can help yer!").also { stage++ }
            8 -> playerl(FaceAnim.FRIENDLY, "Wow! That would be great, buckets of sand direct to bank everday you say? That's great!").also { stage++ }
            9 -> end()
        }
        */
        return true
    }

    override fun newInstance(player: Player?): Dialogue = BertDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.BERT_3108)


    companion object {
        fun rewardSand(player:Player, amount: Int) {
            player?.bank?.add(Item(Items.BUCKET_OF_SAND_1783, amount))
            val store = TheHandintheSand.getStoreFile()
            val username = player?.username?.lowercase() ?: return
            store.addProperty(username, true)
            player.setAttribute(GameAttributes.HAND_SAND_LAST_SAND_CLAIM, System.currentTimeMillis())
            player?.sendMessages("Thanks for the sand Bert!")
            closeDialogue(player)
        }
    }
}
