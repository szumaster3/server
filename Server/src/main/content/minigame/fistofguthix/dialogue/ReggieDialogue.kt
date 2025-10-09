package content.minigame.fistofguthix.dialogue

import core.api.openInterface
import core.api.sendOptions
import core.api.setTitle
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.dialogue.IfTopic
import core.game.dialogue.Topic
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.world.GameWorld
import core.plugin.Initializable
import shared.consts.Components
import shared.consts.Items
import shared.consts.NPCs

/**
 * Represents the Reggie dialogue.
 */
@Initializable
class ReggieDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npcl(FaceAnim.HAPPY, "Welcome, adventurer. Have you come to have a look at my fine wares?")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        var buyAmount = 0
        when (stage) {
            0 -> showTopics(
                Topic("Yes, I want to see what you're selling.", 1, true),
                Topic("No, I have some questions.", 3,false),
                IfTopic("I want to buy tokens.",10,GameWorld.settings?.allow_token_purchase == true)
            )
            1 -> npcl(FaceAnim.HAPPY, "Certainly!").also { stage++ }
            2 -> end().also { openInterface(player, Components.FOG_REWARDS_732) }
            3 -> showTopics(
                Topic("What currency are you trading in?", 4, true),
                Topic("Can you recharge my items?", 20,false),
                Topic("Can you uncharge my items?", 30,false),
                Topic("Why do all of your wares degrade?", 40,true),
                Topic("No, I'm just passing by.", 100,false),
            )
            4 -> playerl(FaceAnim.HALF_ASKING, "What currency are you trading in? It doesn't seem to be regular coins.").also { stage++ }
            5 -> npcl(FaceAnim.HAPPY,"You are absolutely right, my friend. I exchange my goods for so called Fist of Guthix tokens.").also { stage++ }
            6 -> showTopics(
                Topic("How can I get hold of these tokens?", 7, false),
                Topic("What are Fist of Guthix tokens?", 9,false),
                Topic("I have to go now.", 99,false)
            )
            7 -> npcl(FaceAnim.FRIENDLY, "If you win a game in the Fist of Guthix arena, you will be awarded with tokens, based on how well you perform.").also { stage++ }
            8 -> showTopics(
                Topic("What are you selling?", 2, true),
                Topic("I have more questions that I want answered.", 3,true),
                Topic("Thanks for the help.", 100,false)
            )
            9 -> npcl(FaceAnim.FRIENDLY, "Oh, they're nothing special in themselves. They are just tokens we use as currency.").also { stage = 8 }
            10 -> {
                setTitle(player, 4)
                sendOptions(player, title = "How many tokens do you want to buy?", "50", "100", "250", "500").also { stage++ }
            }
            11 -> {
                buyAmount = when (buttonId) {
                    1 -> 50
                    2 -> 100
                    3 -> 250
                    4 -> 500
                    else -> 0
                }
                if (buyAmount > 0) {
                    if (player?.inventory?.containsItem(Item(Items.COINS_995, 1000 * buyAmount)) == true) {
                        player.inventory?.add(Item(Items.FIST_OF_GUTHIX_TOKEN_12852, buyAmount))
                        player.inventory?.remove(Item(Items.COINS_995, 1000 * buyAmount))
                    } else {
                        player?.sendMessage("You don't have enough coins for that.")
                    }
                }
                end()
            }
            20 -> npcl(FaceAnim.FRIENDLY, "By items, I assume you mean items originally acquired from me. If so, the answer is yes, my friend. I gladly spread the power of Guthix, if I am able to.").also { stage++ }
            21 -> playerl(FaceAnim.HAPPY, "That's great.").also { stage++ }
            22 -> npcl(FaceAnim.FRIENDLY, "It certainly is. The power of Guthix does not come cheaply, though. So, I'm afraid I will have to charge you some tokens for it. And I can only recharge items that have been completely depleted of their magical energy.").also { stage++ }
            23 -> playerl(FaceAnim.HALF_ASKING, "Sounds fair. So, how do I go about doing this?").also { stage++ }
            24 -> npcl(FaceAnim.FRIENDLY, "Just hand me the item you want recharged and I'll see what I can do.").also { stage++ }
            25 -> showTopics(
                Topic("I want to see what you're selling first.", 1, true),
                Topic( "How can I get hold of these tokens?", 7,false),
                Topic("I still have more questions.", 3,true),
                Topic( "Okay, I'll do that.", 100,false)
            )
            30 -> npcl(FaceAnim.FRIENDLY, "You don't want to keep the power of Guthix? That's strange. But yes, of course I can do this for you. Please be aware that you won't get anything in return, except the degraded item, and it will cost you tokens to have it charged again.").also { stage++ }
            31 -> playerl(FaceAnim.FRIENDLY, "Yes, I will keep that in mind.").also { stage++ }
            32 -> npcl(FaceAnim.FRIENDLY, "Good. In that case, just hand me the item from which you want the charge removed.").also { stage = 25 }
            40 -> playerl(FaceAnim.HALF_ASKING, "Why do all of your wares degrade? I have seen plenty of similar items in my days, but they all seem to last a lot longer than yours.").also { stage++ }
            41 -> npcl(FaceAnim.FRIENDLY, "A most accurate observation, my friend. But the other items you've come across in your travels most certainly have not been directly infused with the power emanating from the Fist of Guthix.").also { stage++ }
            42 -> playerl(FaceAnim.THINKING, "Umm...I suppose not. But wouldn't the power of Guthix have made the items stronger?").also { stage++ }
            43 -> npcl(FaceAnim.FRIENDLY, "Why, yes, they have. Keeping a shop so close to the source of this power has resulted in the items being infused with energy.").also { stage++ }
            44 -> npcl(FaceAnim.FRIENDLY, "This makes them stronger than they normally would have been, but, unfortunately, they are hardly suitable vessels for such divine power.").also { stage++ }
            45 -> npcl(FaceAnim.FRIENDLY, "As they are being used, the power will be spent, and all that will remain is an empty, weakened shell.").also { stage++ }
            46 -> npcl(FaceAnim.FRIENDLY, "When this happens, just come back to me and I will gladly recharge them for you.").also { stage++ }
            47 -> playerl(FaceAnim.HALF_WORRIED, "...For a small fee.").also { stage++ }
            48 -> npcl(FaceAnim.LAUGH, "Haha. Yes, my friend, for a small fee. The power of a god does not come cheaply, not even in small doses. If we were to give it out for free, we would run out of resources in an instant. Balance must be maintained.").also { stage++ }
            49 -> playerl(FaceAnim.FRIENDLY, "Okay, I will keep that in mind. Thank you.").also { stage++ }
            50 -> npcl(FaceAnim.HALF_ASKING, "Now, is there anything else I can help you with?").also { stage = 3 }
            99 -> playerl(FaceAnim.HALF_GUILTY, "Sorry, but I have to leave now. Goodbye.").also { stage = 100 }
            100 -> end()
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.REGGIE_7601)
}
