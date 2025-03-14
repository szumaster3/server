package content.region.desert.dialogue.alkharid

import core.api.*
import core.api.interaction.openNpcShop
import core.api.item.produceGroundItem
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.interaction.QueueStrength
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.world.map.Location
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.RandomFunction
import org.rs.consts.Items
import org.rs.consts.NPCs

@Initializable
class ShantayDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        if (args[0] != null && args[0] is NPC) {
            npc = args[0] as NPC
        }
        if (args.size == 2) {
            sendMessage(player, "Shantay saunters over to talk with you.")
            npc(
                FaceAnim.HALF_GUILTY,
                "If you want to be let out, you have to pay a fine of",
                "five gold. Do you want to pay now?",
            )
            stage = 25
            return true
        }
        npc(FaceAnim.HALF_GUILTY, "Hello effendi, I am Shantay.")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "I see you're new. Please read the billboard poster",
                    "before going into the desert. It'll give yer details on the",
                    "dangers you can face.",
                ).also {
                    stage++
                }

            1 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "There is a heartbroken mother just past the gates and",
                    "in the desert. Her name is Irena and she mourns her",
                    "lost daughter. Such a shame.",
                ).also {
                    stage++
                }

            2 ->
                options(
                    "What is this place?",
                    "Can I see what you have to sell please?",
                    "I must be going.",
                    "I want to buy a Shantay pass for 5 gold coins.",
                ).also { stage++ }

            3 ->
                when (buttonId) {
                    1 -> player(FaceAnim.HALF_GUILTY, "What is this place?").also { stage = 10 }
                    2 -> player(FaceAnim.HALF_GUILTY, "Can I see what you have to sell please?").also { stage = 20 }
                    3 -> player(FaceAnim.HALF_GUILTY, "I must be going.").also { stage++ }
                    4 ->
                        player(FaceAnim.HALF_GUILTY, "I want to buy a Shantay pass for 5 gold coins.").also {
                            stage =
                                34
                        }
                }

            4 -> npc(FaceAnim.HALF_GUILTY, "So long...").also { stage = END_DIALOGUE }
            10 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "This is the pass of Shantay. I guard this area with my",
                    "men. I am responsible for keeping this pass open and",
                    "repaired.",
                ).also {
                    stage++
                }

            11 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "My men and I prevent outlaws from getting out of the",
                    "desert. And we stop the inexeperienced from a dry death",
                    "in the sands. Which would you say you were?",
                ).also {
                    stage++
                }

            12 ->
                options(
                    "I am definitely an outlaw, prepare to die!",
                    "I am a little inexperienced.",
                    "Er, neither, I'm an adventurer.",
                ).also { stage++ }

            13 ->
                when (buttonId) {
                    1 -> player(FaceAnim.ANGRY, "I am definitely an outlaw, prepare to die.").also { stage = 22 }
                    2 -> player("I am a little inexperienced.").also { stage = 36 }
                    3 -> player("Er, neither, I'm an adventurer.").also { stage = 38 }
                }

            20 -> npc(FaceAnim.HALF_GUILTY, "Absolutely Effendi!").also { stage++ }
            21 -> end().also { openNpcShop(player, NPCs.SHANTAY_836) }
            22 -> npc(FaceAnim.LAUGH, "Ha, very funny.....").also { stage++ }
            23 -> npc(FaceAnim.NEUTRAL, "Guards arrest him!").also { stage++ }
            24 -> {
                close()
                lock(player, 10)
                sendMessage(player, "The guards arrest you and place you in the jail.")
                queueScript(player, 1, QueueStrength.SOFT) {
                    setAttribute(player, "/save:shantay-jail", true)
                    teleport(player, Location.create(3298, 3123, 0))
                    npc(
                        FaceAnim.NEUTRAL,
                        "You'll have to stay in there until you pay the fine of",
                        "five gold pieces. Do you want to pay now?",
                    )
                    stage = 25
                    return@queueScript stopExecuting(player)
                }
            }

            25 -> options("Yes, okay.", "No thanks, you're not having my money.").also { stage++ }
            26 ->
                when (buttonId) {
                    1 -> player("Yes, okay.").also { stage++ }
                    2 -> player("No thanks, you're not having my money.").also { stage = 28 }
                }

            27 -> {
                end()
                if (!removeItem(player, Item(Items.COINS_995, 5))) {
                    player("Sorry, I don't seem to have enough money.")
                } else {
                    sendMessage(player, "You hand over the five gold pieces to Shantay.")
                    sendMessage(player, "Shantay unlocks the door to the cell.")
                    npc("Great, Effendi, now please try to keep the peace.")
                    removeAttribute(player, "shantay-jail")
                }
            }

            28 ->
                npc(
                    "You have a choice. You can either pay five gold pieces",
                    "or... You can be transported to a maximum security",
                    "prision in Port Sarim",
                ).also {
                    stage++
                }

            29 -> npc("Will you pay the five gold pieces?").also { stage++ }
            30 -> options("Yes, okay.", "No, do your worst!").also { stage++ }
            31 ->
                when (buttonId) {
                    1 -> player("Yes, okay.").also { stage += 2 }
                    2 -> player("No, do your worst!").also { stage++ }
                }

            32 ->
                npc(
                    "You are to be transported to a maximum security",
                    "prision in Port Sarim. I hope you've learn an important",
                    "lesson from this.",
                ).also {
                    stage =
                        35
                }

            33 -> npc("Good, I see that you have come to your senses.").also { stage = 27 }
            34 -> {
                val random = RandomFunction.random(1, 20)
                if (freeSlots(player) == 0) {
                    npcl(FaceAnim.NEUTRAL, "Sorry friend, you'll need more inventory space to buy a pass.")
                    return true
                }
                if (!removeItem(player, Item(Items.COINS_995, 5))) {
                    npcl(
                        FaceAnim.NEUTRAL,
                        "Sorry friend, the Shantay Pass is 5 gold coins. You don't seem to have enough money!",
                    ).also { stage = END_DIALOGUE }
                } else {
                    end()
                    sendItemDialogue(player, Items.SHANTAY_PASS_1854, "You purchase a Shantay Pass.")
                    addItemOrDrop(player, Items.SHANTAY_PASS_1854)

                    if (random == 15) {
                        sendMessage(player, "Something drops out of Shantay's pocket onto the floor.")
                        sendMessage(player, "It looks like a piece of paper.")
                        produceGroundItem(player, Items.SCRUMPLED_PAPER_1847, 1, npc.location)
                    }
                }
            }

            35 -> {
                end()
                lock(player, 1)
                sendMessage(player, "You find yourself in a prison.")
                teleport(player, Location.create(3014, 3180, 0))
            }
            36 ->
                npc(
                    "Can I recommend that you purchase a full waterskin",
                    "and a knife! These items will no doubt save your life. A",
                    "waterskin will keep water from evaporating in the desert.",
                ).also {
                    stage++
                }
            37 ->
                npc(
                    "And a keen woodsman with a knife can extract juice",
                    "from a cactus. Before you go into the desert, it's",
                    "advisable to wear desert clothes. It's very hot in the",
                    "desert and you'll surely cook if you wear amour.",
                ).also {
                    stage =
                        END_DIALOGUE
                }
            38 ->
                npc(
                    "Great, I have just the thing for you the desert adventurer.",
                    "I sell desert clothes which will keep you cool in the heat",
                    "of the desert. I also sell waterskins so that you won't",
                    "die in the desert.",
                ).also {
                    stage++
                }
            39 ->
                npc(
                    "A waterskin and a knife help you survive from the juice",
                    "of a cactus. Use the chest to store your items, we'll take",
                    "them to the bank. It's hot in the desert, you'll bake in",
                    "all that armour.",
                ).also {
                    stage++
                }
            40 ->
                npc(
                    "To keep the pass open we ask for 5 gold pieces. And",
                    "we give you a Shantay Pass, just ask to see what I sell",
                    "to buy one.",
                ).also {
                    stage++
                }

            41 ->
                options(
                    "Can I see what you have to sell please?",
                    "I must be going.",
                    "Why do I have to pay to go into the desert?",
                ).also { stage++ }

            42 ->
                when (buttonId) {
                    1 -> player(FaceAnim.HALF_GUILTY, "Can I see what you have to sell please?").also { stage = 20 }
                    2 -> player(FaceAnim.HALF_GUILTY, "I must be going.").also { stage = 4 }
                    3 -> player(FaceAnim.HALF_ASKING, "Why do I have to pay to go into the desert?").also { stage++ }
                }
            43 ->
                npcl(
                    FaceAnim.NEUTRAL,
                    " Effendi, you insult me! I am not interested in making a profit from you! I merely seek to cover my expenses in keeping this pass open.",
                ).also {
                    stage++
                }
            44 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "There is repair work to carry out and also the men's wages to consider. For the paltry sum of 5 Gold pieces, I think we offer a great service.",
                ).also {
                    stage =
                        2
                }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return ShantayDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.SHANTAY_836)
    }
}
