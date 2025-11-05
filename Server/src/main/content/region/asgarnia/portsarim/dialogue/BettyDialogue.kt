package content.region.asgarnia.portsarim.dialogue

import content.data.GameAttributes
import content.region.misthalin.draynor.quest.swept.plugin.SweptUtils
import core.api.*
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.dialogue.IfTopic
import core.game.dialogue.Topic
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.*

/**
 * Represents the Betty dialogue.
 */
@Initializable
class BettyDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if(getQuestStage(player, Quests.THE_HAND_IN_THE_SAND) >= 7 && getVarbit(player, Vars.VARBIT_QUEST_THE_HAND_IN_THE_SAND_PROGRESS_1527) == 4 && !inInventory(player, Items.ROSE_TINTED_LENS_6956)) {
            npcl(FaceAnim.HALF_ASKING, "Hello deary! Have you managed to make that lens yet?").also { stage = 38 }
        } else {
            npc("Welcome to the magic emporium.")
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        val hasBottle = inInventory(player, Items.BOTTLED_WATER_6953) && inBank(player, Items.BOTTLED_WATER_6953)

        when (stage) {
            0 -> if (getQuestStage(player, Quests.SWEPT_AWAY) >= 1) {
                options("Talk to Betty about Swept Away.", "Talk to Betty about her shop.", "Talk to Betty about pink dye.").also { stage = 10 }
            } else if (isQuestComplete(player, Quests.THE_HAND_IN_THE_SAND)) {
                options("Can I see your wares?", "Sorry, I'm not into magic.", "Talk to Betty about pink dye.").also { stage++ }
            } else if(isQuestInProgress(player, Quests.THE_HAND_IN_THE_SAND, 1, 99)) {
                showTopics(
                    IfTopic("Talk to Betty about the Hand in the Sand.", 34, getQuestStage(player, Quests.THE_HAND_IN_THE_SAND) >= 7, true),
                    Topic("Talk to Betty about her shop.", 33, true)
                )
            } else {
                options("Can I see your wares?", "Sorry, I'm not into magic.").also { stage++ }
            }
            1 -> when (buttonId) {
                1 -> {
                    end()
                    openNpcShop(player, NPCs.BETTY_583)
                }
                2 -> player("Sorry, I'm not into magic.").also { stage++ }
                3 -> npc("Zavistic told me what a good job you did. If you want", "some more pink dye, I have made up a batch and you", "can have some for 20 gold.").also { stage = 3 }
            }

            2 -> npc(FaceAnim.HAPPY, "Well, if you see anyone who is into Magic, please send", "them my way.").also {
                stage = END_DIALOGUE
            }

            3 -> options("No thanks, Betty.", "Yes, please!").also { stage++ }
            4 -> when (buttonId) {
                1 -> player("No thanks, Betty.").also { stage = END_DIALOGUE }
                2 -> player("Yes, please!").also { stage++ }
            }

            5 -> {
                end()
                if (freeSlots(player) == 0) {
                    sendMessage(player, "You don't have enough inventory space.")
                } else if (!removeItem(player, Item(Items.COINS_995, 20))) {
                    sendDialogue(player, "You don't have enough coins for that.")
                } else {
                    sendDialogue("You hand over 20 gold pieces in return for the dye.")
                    addItem(player, Items.PINK_DYE_6955)
                    setAttribute(player, "diary:falador:pink-dye-from-betty", true)
                }
            }
            10 -> when (buttonId) {
                1 -> if (!inInventory(player, Items.BETTYS_WAND_14068)) {
                    player("I was wondering if you could help me out with an", "enchantment.").also { stage++ }
                } else {
                    player("I've got your wand for you.").also { stage = 30 }
                }
                2 -> player("Talk to Betty about her shop.").also { stage++ }
                3 -> npc("Zavistic told me what a good job you did. If you want", "some more pink dye, I have made up a batch and you", "can have some for 20 gold.").also { stage = 3 }
            }
            11 -> npc("I suppose that depends on what sort of enchantment", "you're looking for.").also { stage++ }
            12 -> npc("Well, Maggie needs her broom enchanted so that she", "can finish the stuff she's brewing in her cauldron.").also { stage++ }
            13 -> npc("Ah, I see! She's brewing again, is she? Well, I'd be", "happy to help - Maggie always comes up with the most", "amazing brews.").also { stage++ }
            14 -> player("Wonderful! Thank you so much.").also { stage++ }
            15 -> npc("Now, Maggie always likes things on the spicy side; so,", "what I really need for this sort of enchantment is my", "wand. I keep it down in the cellar in a locked chest.").also { stage++ }
            16 -> npc("If you could retrieve the wand and bring it to me, I'd", "be happy to enchant Maggie's broom for you.").also { stage++ }
            17 -> npc("There's just one little problem.").also { stage++ }
            18 -> player("What's that?").also { stage++ }
            19 -> npc("You see, my chest is locked by highly magical means.", "I'd explain it to you in detail, but I haven't the time.").also { stage++ }
            20 -> npc("You'll find my apprentice, Lottie, downstairs - just pop", "down the trapdoor over there and tell her I sent you.").also { stage++ }
            21 -> npc("She'll be happy to explain everything.").also { stage++ }
            22 -> player("Okay, thanks.").also { stage = END_DIALOGUE }
            30 -> npc("Excellent! Let me just enchant that broom for you,", "then.").also {
                visualize(player, -1, SweptUtils.BROOM_ENCHANTMENT_GFX)
                setAttribute(player, GameAttributes.QUEST_SWEPT_AWAY_BETTY_ENCH, true)
                stage++
            }
            31 -> npc("There you go! I'm sure t hat's just the spice that", "Maggie's looking for.").also { stage++ }
            32 -> player("Many thanks.").also { stage = END_DIALOGUE }
            33 -> options("Can I see your wares?", "Sorry, I'm not into magic.").also { stage = 1 }
            34 -> if(getAttribute(player, GameAttributes.HAND_SAND_BETTY_POTION, false)) {
                npcl(FaceAnim.FRIENDLY, "Wonderful deary. When you're ready, just stand in the open doorway and focus the light on the empty vial on my desk and I'll pour the serum into it.").also { stage = 40 }
            } else {
                playerl(FaceAnim.HALF_ASKING, "I've come from Yanille, the wizard says you can make Truth Serum?").also { stage++ }
            }
            35 -> npcl(FaceAnim.FRIENDLY, "This is true deary, I'll need an empty vial.").also { stage++ }
            36 -> if(!removeItem(player, Items.VIAL_229)) {
                playerl(FaceAnim.HAPPY, "I'll have to go find one then, I'll be back!").also { stage = END_DIALOGUE }
            } else {
                player("I have one here!").also { stage++ }
            }
            37 -> {
                npcl(FaceAnim.FRIENDLY, "That's good, now you'll need to make a rose tinted lens. Pink dye can be made from red berries in this bottle to make redberry juice, then add white berries. Just use that on a bullseye lens.")
                setVarbit(player, Vars.VARBIT_QUEST_THE_HAND_IN_THE_SAND_PROGRESS_1527, 4, true)
                addItem(player, Items.BOTTLED_WATER_6953, 1)
                stage = END_DIALOGUE
            }
            38 -> showTopics(
                Topic("I'm still working on it.", END_DIALOGUE, false),
                Topic("I'm afraid I've forgotten how!", 39, false),
                // Inauthentic.
                IfTopic("Ask about bottled water.", 42, !hasBottle && freeSlots(player) >= 1, true)
            )
            39 -> npcl(FaceAnim.FRIENDLY, "Pink dye can be made from red berries in the bottle I gave you. Add white berries to make the pink dye and then you just need to use that on a bullseye lens. Good luck!").also { stage = END_DIALOGUE }
            40 -> player("Ok, what does that do?").also { stage++ }
            41 -> npcl(FaceAnim.FRIENDLY, "Why it makes the person who drinks it unable to hide in the shadow of lies. The light of truth will shine!").also {
                end()
                player.lock(3)
                npc.walkingQueue.reset(false)
                runTask(player, 1) {
                    forceWalk(npc.asNpc(), Location(3012, 3258, 0), "")
                    npc.faceLocation(Location(3014, 3258, 0))
                    npc.animate(Animation(Animations.HUMAN_MULTI_USE_832))
                    runTask(player, 2) {
                        setVarbit(player, Vars.VARBIT_BETTY_DESK_1537, 1)
                        sendItemDialogue(player, Items.VIAL_229, "Betty places a vial on her counter.")
                    }
                }
            }
            42 -> {
                end()
                addItem(player, Items.BOTTLED_WATER_6953, 1)
            }


        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.BETTY_583)
}
