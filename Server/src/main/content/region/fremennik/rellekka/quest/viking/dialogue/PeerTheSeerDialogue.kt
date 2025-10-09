package content.region.fremennik.rellekka.quest.viking.dialogue

import content.data.GameAttributes
import content.region.fremennik.rellekka.quest.viking.FremennikTrials
import core.api.*
import core.api.getQuestStage
import core.api.isQuestComplete
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import core.tools.RandomFunction
import shared.consts.Items
import shared.consts.NPCs
import shared.consts.Quests
import kotlin.random.Random

/**
 * Represents the Peer The Seer dialogue.
 *
 * # Relations
 * - [FremennikTrials]
 */
@Initializable
class PeerTheSeerDialogue(player: Player? = null) : Dialogue(player) {

    val predictionOne = arrayOf("one", "two", "three", "four", "five", "six", "seven", "eight", "ten")
    val predictionTwo = arrayOf("black", "blue", "brown", "cyan", "green", "pink", "purple", "red", "yellow")
    val predictionThree = arrayOf("fire giant", "ghosts", "giant", "goblin", "green dragon", "hobgoblin", "lesser demon", "moss giant", "ogre", "zombie")
    val predictionFour = arrayOf("Al Kharid", "Ardougne", "Burthorpe", "Canifis", "Catherby", "Falador", "Karamja", "Varrock", "The Wilderness", "Yanille")
    val predictionFive = arrayOf("battleaxe", "crossbow", "dagger", "javelin", "long sword", "mace", "scimitar", "spear", "warhammer")
    val predictionSix = arrayOf("Agility", "Cooking", "Crafting", "Fishing", "Fletching", "Herblore", "Mining", "Runecrafting", "Thieving")
    val PREDICTIONS = arrayOf(
        "You will find luck today with the number ${predictionOne[RandomFunction.getRandom(8)]}.",
        "The colour ${predictionTwo[RandomFunction.getRandom(8)]} will bring you luck this day.",
        "The enemy called ${predictionThree[RandomFunction.getRandom(9)]} is your lucky totem this day.",
        "The place called ${predictionFour[RandomFunction.getRandom(9)]} will be worth your time to visit.",
        "The stars tell me that you should use a ${predictionFive[RandomFunction.getRandom(8)]} in combat today.",
        "You would be wise to train the skill ${predictionSix[RandomFunction.getRandom(8)]}"
    )
    var prediction = RandomFunction.getRandom(5)
    private var selectedArmour: BoneArmour? = null

    override fun open(vararg args: Any?): Boolean {
        val hasSkeletalPiece = hasAnItem(player, Items.SKULL_PIECE_6163, Items.RIBCAGE_PIECE_6165, Items.FIBULA_PIECE_6167).container == player.inventory

        if (inInventory(player, Items.WARRIORS_CONTRACT_3710, 1)) {
            playerl(FaceAnim.HAPPY, "Can I have a weather forecast now please?")
            stage = 15
            return true
        } else if (inInventory(player, Items.WEATHER_FORECAST_3705, 1)) {
            playerl(FaceAnim.ASKING, "So, about this forecast...")
            stage = 20
            return true
        } else if (getAttribute(player, GameAttributes.QUEST_VIKING_SIGMUND_RETURN, false)) {
            playerl(FaceAnim.ASKING, "I've got an item to trade but I don't know if it's for you.")
            stage = 26
            return true
        } else if (getAttribute(player, GameAttributes.QUEST_VIKING_SIGMUND_PROGRESS, 0) == 10) {
            playerl(FaceAnim.ASKING, "I don't suppose you have any idea where I could find a brave and powerful warrior to act as a bodyguard?")
            stage = 10
            return true
        } else if (getAttribute(player, GameAttributes.QUEST_VIKING_SIGMUND_PROGRESS, 0) == 9) {
            playerl(FaceAnim.ASKING, "I don't suppose you have any idea where I could find a weather forecast from the Fremennik Seer do you?")
            return true
        } else if (getAttribute(player, GameAttributes.QUEST_VIKING_PEER_START, false) && !player.inventory.isEmpty || !player.equipment.isEmpty) {
            playerl(FaceAnim.ASKING, "So I can bring nothing with me when I enter your house?")
            stage = 100
            return true
        } else if (getAttribute(player, GameAttributes.QUEST_VIKING_PEER_START, false) && player.inventory.isEmpty && player.equipment.isEmpty) {
            playerl(FaceAnim.ASKING, "So I just have to enter by one door of your house, and leave by the other?")
            stage = 110
            return true
        } else if (getAttribute(player, GameAttributes.QUEST_VIKING_PEER_VOTE, false)) {
            playerl(FaceAnim.HALF_ASKING, "So you will vote for me at the council?")
            stage = 120
            return true
        } else if (isQuestComplete(player, Quests.THE_FREMENNIK_TRIALS)) {
            if(!hasSkeletalPiece) {
                player(FaceAnim.HAPPY, "Hello Peer.")
                stage = 150
            } else {
                sendOptions(player!!, title = "What would you like to ask?", "Wallasalki Bone Armour", "The Fremennik Trials")
                stage = 159
            }
            return true
        } else if (player.questRepository.hasStarted(Quests.THE_FREMENNIK_TRIALS)) {
            npcl(FaceAnim.SAD, "Hello outerlander. What do you want?")
            stage = 50
            return true
        }
        if (getQuestStage(player, Quests.THE_FREMENNIK_TRIALS) == 0) {
            if (!hasSkeletalPiece) {
                npc(FaceAnim.SAD, "I have no interest in talking to you just now outerlander.")
                stage = 1000
            } else {
                sendOptions(player!!, title = "What would you like to ask?", "Wallasalki Bone Armour", "The Fremennik Trials")
                stage = 167
            }
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> npcl(FaceAnim.ANNOYED, "Er.... Yes, because I AM the Fremennik Seer.").also { stage++ }
            1 -> playerl(FaceAnim.ASKING, "Can I have a weather forecast then please?").also { stage++ }
            2 -> npcl(FaceAnim.THINKING, "You require a divination of the weather? This is a simple matter for me, but I will require something in return from you for this small service.").also { stage++ }
            3 -> playerl(FaceAnim.ASKING, "I knew you were going to say that...").also { stage++ }
            4 -> npcl(FaceAnim.HAPPY, "Do not fret, outerlander; it is a fairly simple matter. I require a bodyguard for protection. Find someone willing to offer me this service.").also { stage++ }
            5 -> playerl(FaceAnim.ASKING, "That's all?").also { stage++ }
            6 -> npcl(FaceAnim.HAPPY, "That is all.").also {
                player.incrementAttribute(GameAttributes.QUEST_VIKING_SIGMUND_PROGRESS, 1)
                stage = 1000
            }
            10 -> npcl(FaceAnim.ANNOYED, "If I did, then I would simply have asked them myself now, wouldn't I, outerlander?").also { stage = 1000 }
            15 -> npcl(FaceAnim.ANNOYED, "I have already told you outerlander; You may have a reading from me when I have a signed contract from a warrior guaranteeing my protection.").also { stage++ }

            16 -> playerl(FaceAnim.HAPPY, "Yeah, I know; I have one right here from Thorvald.").also {
                removeItem(player, Items.WARRIORS_CONTRACT_3710)
                addItemOrDrop(player, Items.WEATHER_FORECAST_3705, 1)
                stage++
            }
            17 -> npcl(FaceAnim.AMAZED, "You have not only persuaded one of the Fremennik to act as a servant to me, but you have enlisted the aid of mighty Thorvald himself???").also { stage++ }
            18 -> npcl(FaceAnim.HAPPY, "You may take this forecast with my blessing outerlander. You have offered me the greatest security I can imagine.").also { stage = 1000 }
            20 -> npcl(FaceAnim.THINKING, "Yes, outerlander?").also { stage++ }
            21 -> playerl(FaceAnim.ASKING, "I still don't know why you didn't just let me have one anyway in the first place. Surely it means nothing to you?").also { stage++ }
            22 -> npcl(FaceAnim.THINKING, "That is not true, outerlander. Although I see glimpses of the future all of the time, using my powers brings the attention of the gods to me.").also { stage++ }
            23 -> npcl(FaceAnim.THINKING, "Some of the gods are spiteful and cruel, and I fear if I use my powers too much then I will meet with unpredictable accidents.").also { stage++ }
            24 -> npcl(FaceAnim.HAPPY, "This is why I needed protection.").also { stage++ }
            25 -> playerl(FaceAnim.THINKING, "Okay... I... think I understand...").also { stage = 1000 }
            26 -> npcl(FaceAnim.ANNOYED, "Not me, I'm afraid.").also { stage = 1000 }
            50 -> playerl(FaceAnim.ASKING, "Hello. I'm looking for members of the council of elders to vote for me to become a Fremennik.").also { stage++ }
            51 -> npcl(FaceAnim.THINKING, "Are you now? Well that is interesting. Usually outlanders do not concern themselves with our ways like that.").also { stage++ }
            52 -> npcl(FaceAnim.HAPPY, "I am one of the members of the council of elders, and should you be able to prove to me that you have something to offer my clan I will vote in your favour at the next meeting.").also { stage++ }
            53 -> playerl(FaceAnim.ASKING, "How can I prove that to you?").also { stage++ }
            54 -> npcl(FaceAnim.HAPPY, "Well, I have but a simple test. This building behind me is my house. Inside I have constructed a puzzle.").also { stage++ }
            55 -> npcl(FaceAnim.HAPPY, "As a Seer to the clan, I value intelligence very highly, so you may think of it as an intelligence test of sorts.").also { stage++ }
            56 -> playerl(FaceAnim.THINKING, "An intelligence test? I thought barbarians were stupid!").also { stage++ }
            57 -> npcl(FaceAnim.ANNOYED, "That is the opinion that outlanders usually hold of my people, it is true. But that is because people often confuse knowledge with wisdom.").also { stage++ }
            58 -> npcl(FaceAnim.ANNOYED, "My puzzle tests not what you know, but what you can work out. All members of our clan have been tested when they took their trials.").also { stage++ }
            59 -> playerl(FaceAnim.ASKING, "So what exactly does this puzzle consist of, then?").also { stage++ }
            60 -> npcl(FaceAnim.HAPPY, "Well, firstly you must enter my house with no items, weapons or armour. Then it is a simple matter of entering through one door and leaving by the other.").also { stage++ }
            61 -> playerl(FaceAnim.ASKING, "I can't take anything in there with me?").also { stage++ }
            62 -> npcl(FaceAnim.HAPPY, "That is correct outerlander. Everything you need to complete the puzzle you will find inside the building. Nothing more.").also { stage++ }
            63 -> npcl(FaceAnim.HAPPY, "So what say you outerlander? You think you have the wit to earn yourself my vote?").also { stage++ }
            64 -> options("Yes", "No").also { stage++ }
            65 -> when (buttonId) {
                1 -> {
                    playerl(FaceAnim.HAPPY, "Yes, I accept your challenge, I have one small question, however...")
                    setAttribute(player, GameAttributes.QUEST_VIKING_PEER_START, true)
                    setAttribute(player, GameAttributes.QUEST_VIKING_PEER_RIDDLE, Random.nextInt(0, 3))
                    stage = 70
                }
                2 -> {
                    playerl(FaceAnim.HAPPY, "No, thinking about stuff isn't really my 'thing'. I'd rather go kill something. I'll find someone else to vote for me")
                    stage++
                }
            }
            66 -> npcl(FaceAnim.HAPPY, "As you wish, outerlander.").also { stage = 1000 }
            70 -> npcl(FaceAnim.ASKING, "Yes outerlander?").also { stage++ }
            71 -> playerl(FaceAnim.THINKING, "Well... you say I can bring nothing with me when I enter your house...").also { stage++ }
            72 -> npcl(FaceAnim.ANNOYED, "Yes outerlander??").also { stage++ }
            73 -> playerl(FaceAnim.THINKING, "Well...").also { stage++ }
            74 -> npcl(FaceAnim.ANGRY, "Yes, outerlander???").also { stage++ }
            75 -> playerl(FaceAnim.ASKING, "Where is the nearest bank?").also { stage++ }
            76 -> npcl(FaceAnim.HAPPY, "Ah, I see your problem outerlander. The nearest bank to here is the place known to outlanders as the Seers Village.").also { stage++ }
            77 -> npcl(FaceAnim.HAPPY, "It is some way South. I do however have an alternative, should you wish to take it.").also { stage++ }
            78 -> playerl(FaceAnim.ASKING, "And what is that?").also { stage++ }
            79 -> npcl(FaceAnim.HAPPY, "I can store all the weapons, armour and items that you have upon you directly into your bank account.").also { stage++ }
            80 -> npcl(FaceAnim.HAPPY, "This will tax what little magic I possess however, so you will have to travel to the bank to withdraw them again.").also { stage++ }
            81 -> npcl(FaceAnim.HAPPY, "What say you outerlander? Do you wish me to do this for you?").also { stage++ }
            82 -> options("Yes", "No").also { stage++ }
            83 -> when (buttonId) {
                1 -> {
                    val slotAmount = player.inventory.itemCount() + player.equipment.itemCount()
                    if (slotAmount < player.bank.freeSlots()) {
                        dumpContainer(player, player.inventory)
                        dumpContainer(player, player.equipment)
                        stage = 1000
                    } else {
                        npcl(FaceAnim.SAD, "I am sorry outerlander, the spell is not working.")
                        stage = 1000
                    }
                }
                2 -> playerl(FaceAnim.HAPPY, "No thanks. Nobody touches my stuff but me!").also { stage++ }
            }
            84 -> npcl(FaceAnim.HAPPY, "As you wish, outerlander.").also { stage++ }
            85 -> npcl(FaceAnim.NEUTRAL, "You may attempt my little task when you have deposited your equipment in the bank.").also { stage = 1000 }
            100 -> npcl(FaceAnim.HAPPY, "That is correct outerlander, but as I say, I can use my small skill in magic to send your items directly into your bank account from here.").also { stage++ }
            101 -> npcl(FaceAnim.HAPPY, "You will need to manually go to the bank to withdraw them again however.").also { stage++ }
            102 -> npcl(FaceAnim.HAPPY, "Would you like me to perform this small spell upon you, outerlander?").also { stage = 82 }
            110 -> npcl(FaceAnim.HAPPY, "That is correct outerlander. Be warned it is not as easy as it may at first sound...").also { stage = 1000 }
            120 -> npcl(FaceAnim.HAPPY, "Absolutely, outerlander. Your wisdom in passing my test marks you as worthy in my eyes.").also { stage = 1000 }

            /*
             * After quest dialogues.
             */

            150 -> npcl(FaceAnim.HAPPY, "Greetings to you, brother ${FremennikTrials.getFremennikName(player)}! What brings you to see me again?",).also { stage++ }
            154 -> options("Can you tell my future?", "Can you bank my stuff?", "Nothing really.").also { stage++ }
            155 -> when (buttonId) {
                1 -> playerl(FaceAnim.ASKING, "I was wondering if you could give me a reading on my future...?").also { stage = 157 }
                2 -> {
                    lock(player, 1)
                    player.bank.openDepositBox()
                    stage = 1000
                }
                3 -> playerl(FaceAnim.HAPPY, "Nothing really, I just stopped by to say hello.").also { stage++ }
            }
            156 -> npcl(FaceAnim.HAPPY, "Well, hello to you too!").also { stage = 1000 }
            157 -> npcl(FaceAnim.HAPPY, "Ah, you would like a prediction? I do not see that that would be so difficult... Wait a moment...").also { stage++ }
            158 -> npcl(FaceAnim.HAPPY, "Here is your prediction: ${PREDICTIONS[prediction]}").also { stage = 1000 }
            159 -> when(buttonId) {
                1 -> playerl(FaceAnim.HALF_ASKING, "Hello there. I have some wallasalki bone fragments, and I understand you may be able to use them to make some armour for me...?").also { stage = 161 }
                2 -> options("Can you tell my future?", "Can you bank my stuff?", "Nothing really.").also { stage = 155 }
            }
            160 -> npcl(FaceAnim.FRIENDLY, "As you wish.").also { stage = 1000 }
            161 -> npcl(FaceAnim.FRIENDLY, "As you wish, ${FremennikTrials.getFremennikName(player)}. What kind of armour were you thinking of exactly?").also { stage++ }
            162 -> options("A fine helm", "Sturdy bodyarmour", "Powerful leg armour", "Nothing").also { stage++ }
            163 -> {
                selectedArmour = when (buttonId) {
                    1 -> BoneArmour.HELM
                    2 -> BoneArmour.TOP
                    3 -> BoneArmour.LEGS
                    4 -> null
                    else -> null
                }

                stage = if (selectedArmour != null) {
                    164
                } else {
                    160
                }
            }
            164 -> npc(FaceAnim.HALF_ASKING, "I am afraid it will cost you ${selectedArmour!!.coinAmount} coins to make that for you... Is that okay?").also { stage++ }
            165 -> options("YES", "NO") .also { stage++ }
            166 -> when (buttonId) {
                1 -> {
                    val armour = selectedArmour!!
                    val hasMaterials = player!!.inventory.containsItems(
                        Item(Items.COINS_995, armour.coinAmount),
                        Item(Items.DAGANNOTH_HIDE_6155, armour.hideAmount),
                        Item(armour.boneItem, 1)
                    )
                    if (!hasMaterials) {
                        npc(FaceAnim.HALF_GUILTY, "My apologies ${FremennikTrials.getFremennikName(player!!)}. You need ${armour.hideAmount} daggermouth hides, 1 ${getItemName(armour.boneItem).lowercase()}, and ${armour.coinAmount} coins.")
                        stage = 1000
                    } else {
                        removeItem(player!!, Item(Items.COINS_995, armour.coinAmount))
                        removeItem(player!!, Item(Items.DAGANNOTH_HIDE_6155, armour.hideAmount))
                        removeItem(player!!, Item(armour.boneItem, 1))
                        addItem(player!!, armour.product, 1)
                        npc(FaceAnim.FRIENDLY, "Here is your ${armour.name.lowercase().replace('_',' ')}! Want me to make another?")
                        stage = 162
                    }
                }
                2 -> npc(FaceAnim.FRIENDLY, "As you wish, ${FremennikTrials.getFremennikName(player!!)}. What kind of armour were you thinking of exactly?").also { stage = 162 }
            }
            167 -> when(buttonId) {
                1 -> playerl(FaceAnim.HALF_ASKING, "Hello there. I have some wallasalki bone fragments, and I understand you may be able to use them to make some armour for me...?").also { stage++ }
                2 -> npcl(FaceAnim.NEUTRAL, "I have no interest in talking to you just now outerlander.").also { stage = 1000 }
            }
            168 -> npcl(FaceAnim.NEUTRAL, "I see great things in your future outerlander, but I will not go against the wishes of the tribe, and will not make armour for an outerlander.").also { stage++ }
            169 -> npcl(FaceAnim.SAD, "You may try and gain a dispensation from Chieftain Brundt, but I suspect you will be unsuccessful...").also { stage = 1000 }
            1000 -> end()
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.PEER_THE_SEER_1288)
}

private enum class BoneArmour(val product: Int, val hideAmount: Int, val boneItem: Int, val coinAmount: Int) {
    HELM(Items.SKELETAL_HELM_6137, 1, Items.SKULL_PIECE_6163, 5000),
    TOP(Items.SKELETAL_TOP_6139, 3, Items.RIBCAGE_PIECE_6165, 10000),
    LEGS(Items.SKELETAL_BOTTOMS_6141, 2, Items.FIBULA_PIECE_6167, 7500)
}
