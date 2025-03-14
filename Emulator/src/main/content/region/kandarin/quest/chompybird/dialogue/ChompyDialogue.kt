package content.region.kandarin.quest.chompybird.dialogue

import content.region.kandarin.quest.chompybird.BigChompyBirdHunting
import core.api.*
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.item.Item
import core.tools.END_DIALOGUE
import org.rs.consts.Items

class ChompyDialogue(
    val quest: Quest,
) : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        when (quest.getStage(player)) {
            0 -> handleQuestStartDialogue(player, buttonID)
            in 20 until 30 -> handleToadsiesDialogue(player, buttonID)
            in 30 until 40 -> handlePlaceToadsiesDialogue(player, buttonID)
            in 40 until 50 -> handlePlacedBaitDialogue(player, buttonID)
            in 50 until 60 -> handleRantzSucksDialogue(player, buttonID)
            in 60 until 70 -> handleWaitingForChompyDialogue(player, buttonID)
            in 70 until 80 -> handleWaitingForCookedChompy(player, buttonID)
            else -> handleImpatienceDialogue(player, buttonID)
        }
    }

    private fun handleQuestStartDialogue(
        player: Player?,
        buttonId: Int,
    ) {
        when (stage) {
            0 ->
                npcl(
                    FaceAnim.OLD_NORMAL,
                    "Hey you creature! Make some stabbers! I wanna hunt da chompy?",
                ).also { stage++ }
            1 ->
                options(
                    "What are 'stabbers'?",
                    "What's a 'chompy'?",
                    "Ok, I'll make you some 'stabbers'.",
                    "Er, make your own 'stabbers'!",
                ).also {
                    stage++
                }
            2 ->
                when (buttonId) {
                    1 -> playerl("What are 'stabbers'?").also { stage = 3 }
                    2 -> playerl("What's a 'chompy'?").also { stage = 7 }
                    3 -> playerl("Ok, I'll make you some 'stabbers'.").also { stage = 10 }
                    4 -> playerl("Er, make your own 'stabbers'!").also { stage = END_DIALOGUE }
                }
            3 ->
                npcl(
                    FaceAnim.OLD_NORMAL,
                    "For da stabbie chucker, I's wanna hunt da chompy! Creature knows what Rantz wants... ...flyin' to stabbie da chompy!",
                ).also {
                    stage++
                }
            4 ->
                npcl(
                    FaceAnim.OLD_NORMAL,
                    "The ogre shows you a huge but crude bow and then starts to nod energetically in an effort to help you understand.",
                ).also {
                    stage++
                }
            5 -> playerl("I think I understand. You want me to make some arrows for you?").also { stage++ }
            6 ->
                npcl(FaceAnim.OLD_NORMAL, "Yeah, is what Rantz sayed, make da stabbers for da stabby chucker!").also {
                    stage =
                        1
                }
            7 ->
                npcl(
                    FaceAnim.OLD_NORMAL,
                    "Da chompy is der bestest yummies for Rantz, Fycie and Bugs! We's looking for da yummies all da time. Da chompy is a big flapper, Rantz want's stabbers to sneaky, sneaky, stick da chompy.",
                ).also {
                    stage++
                }
            8 -> playerl("Ah, so 'da chompy' is some kind of bird?").also { stage++ }
            9 ->
                npcl(
                    FaceAnim.OLD_NORMAL,
                    "Yeah, is what Rantz sayed, Da chompy is da big flapper and is bestest yummies. But Rantz needs stabbers to stick da chompy... Will creatures make dem stabbers for us?",
                ).also {
                    stage =
                        1
                }
            10 ->
                npcl(
                    FaceAnim.OLD_NORMAL,
                    "Good you creature, you need sticksies from achey tree and stabbies from dog bones.",
                ).also {
                    stage =
                        END_DIALOGUE
                    ; quest.start(player)
                }
        }
    }

    private fun handleImpatienceDialogue(
        player: Player?,
        buttonId: Int,
    ) {
        val hasArrows = amountInInventory(player!!, Items.OGRE_ARROW_2866) > 0

        when (stage) {
            0 ->
                npcl(
                    FaceAnim.OLD_NORMAL,
                    "Hey you creature... Have you made me da stabbers? I wanna stick da chompy?",
                ).also {
                    stage++
                }

            1 -> {
                if (!hasArrows) {
                    playerl("Er not exactly?").also { stage++ }
                } else {
                    playerl(
                        FaceAnim.ANNOYED,
                        "Well, yes actually, as you asked so nicely. Here you go! Here's your 'stabbers'.",
                    )
                    stage = 100
                }
            }

            2 ->
                npcl(
                    FaceAnim.OLD_NORMAL,
                    "You do stabbers quick!...quick!.. Or Rantz make stabbers for Rantz and then practice for chompy sticking on creature!",
                ).also {
                    stage++
                }

            3 ->
                options(
                    "How do I make the 'stabbers' again?",
                    "Ok, I'll make the 'stabbers' for you.",
                ).also { stage++ }

            4 ->
                when (buttonId) {
                    1 -> playerl("How do I make the 'stabbers' again?").also { stage = 5 }
                    2 -> playerl("Ok, I'll make the 'stabbers' for you.").also { stage = END_DIALOGUE }
                }

            5 ->
                npcl(
                    FaceAnim.OLD_NORMAL,
                    "Grrr creature... you's no good stabber maker! You's make da stabbie bit from da dog bones, and get da sticksies from da Achey tree... simple see! Oh and da flufgies from da flappers as well!",
                ).also {
                    stage++
                }

            6 -> playerl("Oh, so I need logs from the achey tree, bones from a canine... and feather?").also { stage++ }
            7 -> npcl(FaceAnim.OLD_NORMAL, "Is just what Rantz sayed!").also { stage++ }
            8 -> npcl(FaceAnim.OLD_NORMAL, "The hulking ogre nods excitedly.").also { stage = END_DIALOGUE }

            100 -> {
                sendItemDialogue(player, Item(Items.OGRE_ARROW_2866, 20), "Rantz takes six ogre arrows off you.")
                removeItem(player, Item(Items.OGRE_ARROW_2866, 6))
                stage++
            }

            101 -> {
                npcl(
                    FaceAnim.OLD_NORMAL,
                    "Ahh, der creature has dem...goodly, goodly. Now us can stick der chompy bird...",
                )
                stage = 4
                quest.setStage(player, 20)
            }
        }
    }

    private fun handleToadsiesDialogue(
        player: Player?,
        buttonId: Int,
    ) {
        val hasToads = amountInInventory(player!!, Items.BLOATED_TOAD_2875) > 0

        when (stage) {
            0 -> npcl(FaceAnim.OLD_NORMAL, "Hey you creature you still here?").also { stage++ }
            1 ->
                npcl(
                    FaceAnim.OLD_NORMAL,
                    "Da chompy still not coming! We need da fatsy toady to get da chompy, do you got it? Do you got da fatsy toady? Then we can sneaky, sneaky stick da chompy.",
                ).also {
                    stage++
                }

            2 -> {
                if (hasToads) {
                    playerl("Yes, I have a 'fatsy toady' for you, here look!")
                    stage = 100
                } else {
                    playerl("No I haven't got the 'fatsy toady' yet!").also { stage++ }
                }
            }

            3 -> npcl(FaceAnim.OLD_NORMAL, "Dat's a pidy... but maybe Rantz can help da creature?").also { stage++ }
            4 ->
                options(
                    "How do we make the chompys come?",
                    "What are 'fatsy toadies'?",
                    "Where do we put the fatsy toadies?",
                    "What do you mean 'sneaky..sneaky.. stick da chompy?'",
                    "Ok, thanks.",
                ).also { stage++ }

            5 ->
                when (buttonId) {
                    1 -> playerl("How do we make the chompys come?").also { stage = 6 }
                    2 -> playerl("What are 'fatsy toadies'?").also { stage = 8 }
                    3 -> playerl("Where do we put the fatsy toadies?").also { stage = 9 }
                    4 -> playerl("What do you mean 'sneaky..sneaky.. stick da chompy?'").also { stage = 12 }
                    5 -> playerl("Ok, thanks.").also { stage = END_DIALOGUE }
                }
            6 ->
                npcl(
                    FaceAnim.OLD_NORMAL,
                    "Chompys love da fatsy toadies. Toadies get big on der swamp gas and der chomys are licking der lips for em as me is licking lips for da chompy. Da chompys don't like da smaller toadies from nearby swampy.",
                ).also {
                    stage++
                }
            7 ->
                npcl(
                    FaceAnim.OLD_NORMAL,
                    "Dey's fussie eaters like Rantz. Fycie an Bugs play with toadies and blower dey's all times making fatsy toadies.",
                ).also {
                    stage =
                        4
                }
            8 ->
                npcl(
                    FaceAnim.OLD_NORMAL,
                    "Fatsy toadies are da chompy burds bestest yumms. But da toadies here are too small for da chompy. You've godda make da toadies big and round!",
                ).also {
                    stage =
                        4
                }
            9 -> npcl(FaceAnim.OLD_NORMAL, "Over der!").also { stage++ }
            10 -> npcl(FaceAnim.OLD_NORMAL, "The ogre points to a small clearing to da south.").also { stage++ }
            11 ->
                npcl(FaceAnim.OLD_NORMAL, "Ok creature? You got dat? Over here by der no tree's place.").also {
                    stage =
                        4
                }
            12 ->
                npcl(
                    FaceAnim.OLD_NORMAL,
                    "Duh! You creature is a bit stoopid yes? Us needs to sneaky, sneaky.. and stick da chompy! Den we can eat da chompy!",
                ).also {
                    stage =
                        4
                }
            100 ->
                sendItemDialogue(
                    player,
                    Items.BLOATED_TOAD_2875,
                    "You show the bloated toad to Rantz. He nods with approval.",
                ).also {
                    stage++
                }
            101 ->
                npcl(
                    FaceAnim.OLD_NORMAL,
                    "Dat's a good fatsy toady, now we's need to put it for da chompy to come.",
                ).also {
                    stage++
                }
            102 -> {
                playerl("Where do I put the 'fatsy toadies'?").also { stage++ }
                quest.setStage(player, 30)
                stage = 0
            }
        }
    }

    private fun handlePlaceToadsiesDialogue(
        player: Player?,
        buttonId: Int,
    ) {
        npcl(
            FaceAnim.OLD_NORMAL,
            "Over 'dere creature, put da toadies over der! ~ The ogre points to a clearing to the south. ~",
        )
        clearHintIcon(player!!)
        registerHintIcon(player, BigChompyBirdHunting.TOAD_LOCATION, 5)
        stage = END_DIALOGUE
    }

    private fun handlePlacedBaitDialogue(
        player: Player?,
        buttonId: Int,
    ) {
        when (stage) {
            0 -> playerl("There you go, I've placed the bait.").also { stage++ }
            1 ->
                npcl(
                    FaceAnim.OLD_NORMAL,
                    "Goodz me now waits for da chompy! It shouldn't be long now. Sneaky...sneaky...",
                ).also { stage++ }

            2 -> playerl("Yes, I know...stick da chompy!").also { stage++ }
            3 ->
                npcl(
                    FaceAnim.OLD_NORMAL,
                    "Hey.. you's creature, is da fatsy toady still dere? Go get more fatsy toadies if dey all gone!",
                ).also { stage++ }

            4 ->
                playerl(
                    "What? I have to get more bait if there's none there? Does this Chompy Bird even exist I wonder?",
                ).also {
                    stage = END_DIALOGUE
                }
        }
    }

    private fun handleRantzSucksDialogue(
        player: Player?,
        buttonId: Int,
    ) {
        when (stage) {
            0 -> playerl("Hey there, you keep missing the chompy bird.").also { stage++ }
            1 ->
                npcl(
                    FaceAnim.OLD_NORMAL,
                    "I knows, I keeps missing... because your stabbers are worserer at flying than a dead dog.",
                ).also { stage++ }

            2 ->
                options(
                    "Oh, keep trying then... you might hit one through pure luck.",
                    "Come on, let me have a go...",
                ).also { stage++ }

            3 ->
                when (buttonId) {
                    1 ->
                        playerl("Oh, keep trying then... you might hit one through pure luck.").also {
                            stage = END_DIALOGUE
                        }

                    2 -> playerl("Come on, let me have a go...").also { stage = 4 }
                }

            4 -> npcl(FaceAnim.OLD_NORMAL, "No, is Rantz stabby thrower... you too weedy...").also { stage++ }
            5 ->
                options(
                    "I'm actually quite strong... please let me try.",
                    "Oh suit yourself, you'll just have to go hungry.",
                ).also { stage++ }

            6 ->
                when (buttonId) {
                    1 -> playerl("I'm actually quite strong... please let me try.").also { stage = 7 }
                    2 -> playerl("Oh suit yourself, you'll just have to go hungry.").also { stage = END_DIALOGUE }
                }

            7 ->
                npcl(
                    FaceAnim.OLD_NORMAL,
                    "Oh, ok... I lend you other stabby thrower... but creature don't better cry when it hurts itself.",
                ).also { stage++ }

            8 -> {
                sendItemDialogue(
                    player!!,
                    Items.OGRE_BOW_2883,
                    "Rantz hands over an ogre bow. It's huge! You can barely drawn back the string!",
                )
                addItemOrDrop(player, Items.OGRE_BOW_2883)
                stage = END_DIALOGUE
                quest.setStage(player, 60)
            }
        }
    }

    private fun handleWaitingForChompyDialogue(
        player: Player?,
        buttonId: Int,
    ) {
        val hasChompyBird = amountInInventory(player!!, Items.RAW_CHOMPY_2876) > 0

        assignRandomIngredients(player)
        val rantzIngredient =
            getAttribute(
                player,
                BigChompyBirdHunting.ATTR_ING_RANTZ,
                -1,
            )

        when (stage) {
            0 -> npcl(FaceAnim.OLD_NORMAL, "Hey You! Got da chompy yet?").also { stage++ }
            1 -> {
                if (hasChompyBird) {
                    playerl("Yep, here's your chompy Bird!")
                    stage = 100
                } else {
                    playerl("Not yet!").also { stage++ }
                }
            }

            2 -> npcl(FaceAnim.OLD_NORMAL, "Well hurry up and get some, we is hungry!").also { stage = END_DIALOGUE }
            100 ->
                sendItemDialogue(
                    player,
                    Items.RAW_CHOMPY_2876,
                    "You show Rantz the freshly plucked chompy carcass.",
                ).also { stage++ }

            101 ->
                npcl(
                    FaceAnim.OLD_NORMAL,
                    "Dat's a great chompy, you musta got a lucky shot wiv da stabbie chucker.",
                ).also { stage++ }

            102 ->
                npcl(
                    FaceAnim.OLD_NORMAL,
                    "Okay's now you's needs to cook da chompy! Slurp! You's can cook it's over der! ~Rantz points to a nearby spit roast.~",
                ).also { stage++ }

            103 ->
                npcl(
                    FaceAnim.OLD_NORMAL,
                    "But's we's particular about our chompy yumms. Me's wants ${getItemName(
                        rantzIngredient,
                    )} wiv mine! Fycie and Bugs want something wiv der's as well, go and ask 'em wat dey want.",
                ).also { stage++ }

            104 -> playerl("What! Now I've got the chompy bird, you expect me to cook it as well?").also { stage++ }
            105 ->
                npcl(
                    FaceAnim.OLD_NORMAL,
                    "Yep, da spit's over der! Last time Rantz did yummies, got very bad, did bad things to food.. and belly.",
                ).also { stage++ }

            106 -> {
                playerl("HUH!").also { stage = END_DIALOGUE }
                quest.setStage(player, 70)
            }
        }
    }

    private fun handleWaitingForCookedChompy(
        player: Player?,
        buttonId: Int,
    ) {
        val hasCookedChompy = amountInInventory(player!!, Items.SEASONED_CHOMPY_2882) > 0

        when (stage) {
            0 -> npcl(FaceAnim.OLD_NORMAL, "Hey You! Cook da chompy yet?").also { stage++ }
            1 -> {
                if (hasCookedChompy) {
                    playerl("Yep, here you go, here's your cooked chompy bird.")
                    stage = 100
                } else {
                    playerl("Not yet!").also { stage++ }
                }
            }

            2 -> npcl(FaceAnim.OLD_NORMAL, "Well hurry up, we is hungry!").also { stage = END_DIALOGUE }
            100 ->
                sendItemDialogue(
                    player,
                    Items.SEASONED_CHOMPY_2882,
                    "You hand over the cooked chompy bird to Rantz.",
                ).also { stage++ }

            101 ->
                npcl(
                    FaceAnim.OLD_NORMAL,
                    "Hey hey! We got da delicious chompy bird Yay! This looks really tasty as well!",
                ).also { stage++ }

            102 ->
                npcl(
                    FaceAnim.OLD_NORMAL,
                    "Tank's very much for da chompy... Fycie an Bugs like very much da chompy yumms!",
                ).also { stage++ }

            103 -> sendDialogue(player, "~The family of ogres sit down together~").also { stage++ }
            104 -> sendDialogue(player, "~and enjoy your well cooked chompy bird.~").also { stage++ }
            105 ->
                playerl("It's my pleasure!").also {
                    stage = END_DIALOGUE
                    quest.finish(player)
                }
        }
    }

    private fun assignRandomIngredients(player: Player) {
        val possibleIngredients =
            arrayListOf(
                Items.ONION_1957,
                Items.DOOGLE_LEAVES_1573,
                Items.EQUA_LEAVES_2128,
                Items.TOMATO_1982,
                Items.POTATO_1942,
                Items.CABBAGE_1965,
            )
        setAttribute(
            player,
            BigChompyBirdHunting.ATTR_ING_RANTZ,
            possibleIngredients.random().also { possibleIngredients.remove(it) },
        )
        setAttribute(
            player,
            BigChompyBirdHunting.ATTR_ING_BUGS,
            possibleIngredients.random().also { possibleIngredients.remove(it) },
        )
        setAttribute(
            player,
            BigChompyBirdHunting.ATTR_ING_FYCIE,
            possibleIngredients.random().also { possibleIngredients.remove(it) },
        )
    }
}
