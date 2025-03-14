package content.minigame.puropuro.dialogue

import core.api.*
import core.game.dialogue.Dialogue
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Components
import org.rs.consts.Items
import org.rs.consts.NPCs

@Initializable
class ElnockInquisitorDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npc("Ah, good day, it's you again. What can I do for you?")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                if (!inInventory(player, Items.IMPLING_SCROLL_11273) && !inBank(player, Items.IMPLING_SCROLL_11273)) {
                    npc("Ah, I notice you don't own an impling collector's scroll.").also { stage = 34 }
                } else {
                    options(
                        "Can you remind me how to catch implings again?",
                        "Can I trade some jarred implings please?",
                        "Do you have some spare equipment I can use?",
                    ).also { stage++ }
                }

            1 ->
                when (buttonId) {
                    1 -> player("Can you remind me how to catch implings again?").also { stage = 10 }
                    2 -> player("Can I trade some jarred implings please?").also { stage = 20 }
                    3 -> player("Do you have some spare equipment I can use?").also { stage = 30 }
                }

            10 -> npc("Certainly.").also { stage++ }
            11 ->
                npc(
                    "Firstly you will need a butterfly net in which to catch",
                    "them and at least one special impling jar to store an",
                    "impling.",
                ).also {
                    stage++
                }
            12 ->
                npc(
                    "You will also require some experience as a Hunter",
                    "since these creatures are elusive. The more immature",
                    "implings require less experience, but some of the rarer",
                    "implings are extraordinarily hard to find and catch.",
                ).also {
                    stage++
                }
            13 ->
                npc(
                    "Once you have caught one, you may break the jar",
                    "open and obtain the object the impling is carrying.",
                    "Alternatively, you may exchange certain combinations of",
                    "jars with me. I will return the jars to my clients. In",
                ).also {
                    stage++
                }
            14 ->
                npc(
                    "exchange I will be able to provide you with some",
                    "equipment that may help you hunt butterflies more",
                    "effectively.",
                ).also {
                    stage++
                }
            15 ->
                npc(
                    "also beware. Those imps walking around the maze do",
                    "not like the fact that their kindred spirits are being",
                    "captured and will attempt to steal any full jars you have",
                    "on you, setting the implings free.",
                ).also {
                    stage =
                        END_DIALOGUE
                }
            20 -> {
                end()
                openInterface(player, Components.ELNOCK_EXCHANGE_540)
            }

            30 -> {
                if (!player.getSavedData().activityData.isElnockSupplies) {
                    player.getSavedData().activityData.isElnockSupplies = true
                    player.inventory.add(Item(10010), player)
                    player.inventory.add(Item(11262, 1), player)
                    player.inventory.add(Item(11260, 6), player)
                    npc("Here you go!")
                    stage++
                } else {
                    npc(
                        "Since I have already given you some equipment for free,",
                        "I'll be willing to sell you some now.",
                    ).also {
                        stage =
                            36
                    }
                }
            }
            31 ->
                npc(
                    "If you are ready to start hunting implings, then enter",
                    "the main part of the maze.",
                ).also { stage++ }
            32 ->
                npc("Just push through the wheat that surrounds the centre", "of the maze and get catching!").also {
                    stage =
                        END_DIALOGUE
                }
            34 -> options("Yes, please.", "No, thanks.").also { stage++ }
            35 ->
                when (buttonId) {
                    1 ->
                        sendItemDialogue(
                            player,
                            Items.IMPLING_SCROLL_11273,
                            "Elnock gives you a scroll. If you check it whilst in the maze, you will see how many of each impling you have captured.",
                        ).also {
                            addItem(player, Items.IMPLING_SCROLL_11273)
                            end()
                        }

                    2 -> end()
                }

            36 -> player("*sigh*", "Alright, sell me some Impling jars.").also { stage++ }
            37 -> npc("I'll sell you five jars for 25,000gp, what do you say?").also { stage++ }
            38 ->
                if (inInventory(player, Items.COINS_995, 25000)) {
                    player("Wow, that's expensive!", "Alright... here's some gold for a few jars.").also { stage++ }
                } else {
                    player("Actually, I don't have that many coins at the moment.").also { stage = END_DIALOGUE }
                }

            39 ->
                if (!removeItem(player, Item(Items.COINS_995, 25000))) {
                    player("Actually, I don't have that many coins at the moment.").also { stage = END_DIALOGUE }
                } else {
                    npc("Young one, that is the price you pay for", "forgetting to save some impling jars!").also {
                        addItemOrDrop(player, Items.IMPLING_JAR_11260, 5)
                        stage = END_DIALOGUE
                    }
                }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.ELNOCK_INQUISITOR_6070)
    }
}
