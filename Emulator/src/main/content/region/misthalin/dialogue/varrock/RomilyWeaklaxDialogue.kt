package content.region.misthalin.dialogue.varrock

import core.api.removeAttribute
import core.api.setAttribute
import core.game.dialogue.Dialogue
import core.game.interaction.NodeUsageEvent
import core.game.interaction.UseWithHandler
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.item.Item
import core.plugin.ClassScanner.definePlugin
import core.plugin.Initializable
import core.plugin.Plugin
import core.tools.RandomFunction
import org.rs.consts.Items
import org.rs.consts.NPCs

@Initializable
class RomilyWeaklaxDialogue(
    player: Player? = null,
) : Dialogue(player) {
    private var pieId = 0
    private var pieAmt = 0
    private var pieReward: PieReward? = null

    override fun init() {
        super.init()
        definePlugin(RomilyWildPieHandler())
    }

    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        pieId = player.getAttribute(keyId, 0)
        pieAmt = player.getAttribute(keyAmt, 0)
        pieReward = PieReward.forId(pieId)
        if (args.size > 1) {
            val usedWith = args[1] as Item
            if (usedWith.id == Items.WILD_PIE_7208) {
                npc("Is that a wild pie for me?")
                stage = 100
                return true
            }
        }
        npc("Hello and welcome to my pie shop, how can I help you?")
        stage =
            if (pieId != 0 && pieAmt != 0) {
                2
            } else {
                0
            }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            999 -> end()
            0 -> {
                options("I'd like to buy some pies.", "Do you need any help?", "I'm good thanks.")
                stage = 1
            }

            1 ->
                when (buttonId) {
                    1 -> {
                        player("I'd like to buy some pies.")
                        stage = 10
                    }

                    2 -> {
                        player("Do you need any help?")
                        stage = 20
                    }

                    3 -> {
                        player("I'm good thanks.")
                        stage = 999
                    }
                }

            2 -> {
                options("I'd like to buy some pies.", "I've got those pies you wanted.", "I'm good thanks.")
                stage = 1
            }

            3 ->
                when (buttonId) {
                    1 -> {
                        player("I'd like to buy some pies.")
                        stage = 10
                    }

                    2 -> {
                        player("I've got those pies you wanted.")
                        stage = 50
                    }

                    3 -> {
                        player("I'm good thanks.")
                        stage = 999
                    }
                }

            10 -> {
                npc("Take a look.")
                stage++
            }

            11 -> {
                end()
                npc.openShop(player)
            }

            20 -> {
                npc(
                    "Actually I could, you see I'm running out of stock and I",
                    "don't have tme to bake any more pies. would you be",
                    "willing to bake me some pies? I'll pay you well for them.",
                )
                stage = 21
            }

            21 -> {
                options("Sure, what do you need?", "Sorry, I can't help you.")
                stage = 22
            }

            22 ->
                when (buttonId) {
                    1 -> {
                        player("Sure, what do you need?")
                        stage = 60
                    }

                    2 -> {
                        player("Sorry, I can't help you.")
                        stage++
                    }
                }

            23 -> {
                npc("Come back if you ever want to bake pies.")
                stage = 999
            }

            50 -> {
                val piesInInventory = player.inventory.getAmount(pieId)
                val deficit = pieAmt - piesInInventory
                if (piesInInventory == 0) {
                    npc(
                        "Doesn't look like you have any of the",
                        pieAmt.toString() + " " + Item(pieId).name + "s I requested.",
                    )
                    stage = 999
                } else if (deficit == 0) {
                    npc("Thank you very much!")
                    removeAttribute(player, keyAmt)
                    removeAttribute(player, keyId)
                } else {
                    npc("Thank you, if you could bring me the other $deficit that'd", "be great!")
                    setAttribute(player, "/save:" + keyAmt, deficit)
                }
                player.inventory.remove(Item(pieId, piesInInventory))
                player.inventory.add(Item(995, pieReward!!.reward * piesInInventory))
                stage = 999
            }

            60 -> {
                pieAmt = RandomFunction.random(1, 28)
                pieId = PieReward.values()[RandomFunction.nextInt(PieReward.values().size)].id
                setAttribute(player, "/save:" + keyAmt, pieAmt)
                setAttribute(player, "/save:" + keyId, pieId)
                npc("Great, can you bake me " + pieAmt + " " + Item(pieId).name + "s please.")
                stage = 999
            }

            100 -> {
                player("Yes, it is.")
                stage++
            }

            101 -> {
                npc("Oh, how splendid! Let me take that from you then.")
                player.inventory.remove(Item(Items.WILD_PIE_7208))
                player.achievementDiaryManager.finishTask(player, DiaryType.VARROCK, 2, 5)
                stage++
            }

            102 -> {
                npc("Now, was there anything else you needed?")
                stage =
                    if (pieId != 0 && pieAmt != 0) {
                        2
                    } else {
                        0
                    }
            }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.ROMILY_WEAKLAX_3205)
    }

    private enum class PieReward(
        var id: Int,
        var reward: Int,
    ) {
        APPLE(Items.APPLE_PIE_2323, 84),

        REDBERRY(Items.REDBERRY_PIE_2325, 90),

        MEAT(Items.MEAT_PIE_2327, 96),

        GARDEN(Items.GARDEN_PIE_7178, 112),

        FISH(Items.FISH_PIE_7188, 125),

        ADMIRAL(Items.ADMIRAL_PIE_7198, 387),
        ;

        companion object {
            fun forId(id: Int): PieReward? {
                for (pie in values()) {
                    if (pie.id == id) {
                        return pie
                    }
                }
                return null
            }
        }
    }

    class RomilyWildPieHandler : UseWithHandler(Items.WILD_PIE_7208) {
        override fun newInstance(arg: Any?): Plugin<Any> {
            addHandler(3205, NPC_TYPE, this)
            return this
        }

        override fun handle(event: NodeUsageEvent): Boolean {
            if (!event.player.achievementDiaryManager
                    .getDiary(DiaryType.VARROCK)!!
                    .isComplete(2, 5)
            ) {
                event.player.dialogueInterpreter.open(3205, event.usedItem)
            }
            return true
        }
    }

    companion object {
        private const val keyAmt = "romily-weaklax:pie-amt"
        private const val keyId = "romily-weaklax:pie-assigned"
    }
}
