package content.region.misthalin.varrock.guild.cooks_guild.dialogue

import content.data.GameAttributes
import core.api.*
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.interaction.NodeUsageEvent
import core.game.interaction.UseWithHandler
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.item.Item
import core.plugin.ClassScanner
import core.plugin.Initializable
import core.plugin.Plugin
import core.tools.END_DIALOGUE
import core.tools.RandomFunction
import shared.consts.Items
import shared.consts.NPCs

/**
 * Represents the Romily Weaklax dialogue.
 *
 * # Relations
 * [Varrock Achievement Diary][content.region.misthalin.handlers.varrock.VarrockAchievementDiary]
 */
@Initializable
class RomilyWeaklaxDialogue(player: Player? = null) : Dialogue(player) {
    private var pieId = 0
    private var pieAmt = 0
    private var pieReward: PieReward? = null

    override fun init() {
        super.init()
        ClassScanner.definePlugin(RomilyWildPieHandler())
    }

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        pieId = getAttribute(player, GameAttributes.DIARY_VARROCK_ROMILY_WEAKLAX_PIE_ASSIGN, 0)
        pieAmt = getAttribute(player, GameAttributes.DIARY_VARROCK_ROMILY_WEAKLAX_PIE_AMOUNT, 0)
        pieReward = PieReward.forId(pieId)

        if (args.size > 1 && (args[1] as Item).id == Items.WILD_PIE_7208) {
            npc(FaceAnim.HALF_ASKING, "Is that a wild pie for me?")
            stage = 100
        } else {
            npc(FaceAnim.HAPPY, "Hello and welcome to my pie shop, how can I help you?")
            stage = if (pieId != 0 && pieAmt != 0) 2 else 0
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> options("I'd like to buy some pies.", "Do you need any help?", "I'm good thanks.").also { stage++ }

            1 -> when (buttonId) {
                1 -> player("I'd like to buy some pies.").also { stage = 10 }
                2 -> player("Do you need any help?").also { stage = 20 }
                3 -> player("I'm good thanks.").also { stage = END_DIALOGUE }
            }

            2 -> options(
                "I'd like to buy some pies.", "I've got those pies you wanted.", "I'm good thanks."
            ).also { stage++ }

            3 -> when (buttonId) {
                1 -> player("I'd like to buy some pies.").also { stage = 10 }
                2 -> player("I've got those pies you wanted.").also { stage = 50 }
                3 -> player("I'm good thanks.").also { stage = END_DIALOGUE }
            }

            10 -> npc("Take a look.").also { stage++ }
            11 -> end().also { openNpcShop(player, NPCs.ROMILY_WEAKLAX_3205) }

            20 -> npc("Actually, I could use help baking pies. Would you help me?").also { stage++ }
            21 -> options("Sure, what do you need?", "Sorry, I can't help you.").also { stage++ }
            22 -> when (buttonId) {
                1 -> player("Sure, what do you need?").also { stage = 60 }
                2 -> player("Sorry, I can't help you.").also { stage++ }
            }

            23 -> npc("Come back if you ever want to bake pies.").also { stage = END_DIALOGUE }

            50 -> {
                val piesInInventory = amountInInventory(player, pieId)
                val deficit = pieAmt - piesInInventory
                when {
                    piesInInventory == 0 -> {
                        npc("Doesn't look like you have the $pieAmt ${Item(pieId).name}s I requested.")
                        stage = END_DIALOGUE
                    }

                    deficit == 0 -> {
                        npc("Thank you very much!")
                        removeAttribute(player, GameAttributes.DIARY_VARROCK_ROMILY_WEAKLAX_PIE_AMOUNT)
                        removeAttribute(player, GameAttributes.DIARY_VARROCK_ROMILY_WEAKLAX_PIE_AMOUNT)
                    }

                    else -> {
                        npc("Thank you! If you could bring me the other $deficit pies, that'd be great!")
                        setAttribute(player, GameAttributes.DIARY_VARROCK_ROMILY_WEAKLAX_PIE_AMOUNT, deficit)
                    }
                }
                player.inventory.remove(Item(pieId, piesInInventory))
                player.inventory.add(Item(Items.COINS_995, pieReward!!.reward * piesInInventory))
                stage = END_DIALOGUE
            }

            60 -> {
                pieAmt = RandomFunction.random(1, 28)
                pieId = PieReward.values()[RandomFunction.nextInt(PieReward.values().size)].id
                setAttribute(player, GameAttributes.DIARY_VARROCK_ROMILY_WEAKLAX_PIE_AMOUNT, pieAmt)
                setAttribute(player, GameAttributes.DIARY_VARROCK_ROMILY_WEAKLAX_PIE_AMOUNT, pieId)
                npc("Great, can you bake me $pieAmt ${Item(pieId).name}s please?")
                stage = END_DIALOGUE
            }

            100 -> player("Yes, it is.").also { stage++ }
            101 -> {
                npc("Oh, how splendid! Let me take that from you.")
                player.inventory.remove(Item(Items.WILD_PIE_7208))
                player.achievementDiaryManager.finishTask(player, DiaryType.VARROCK, 2, 5)
                stage++
            }

            102 -> {
                npc("Now, was there anything else you needed?")
                stage = if (pieId != 0 && pieAmt != 0) 2 else 0
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.ROMILY_WEAKLAX_3205)

    private enum class PieReward(val id: Int, val reward: Int) {
        APPLE(Items.APPLE_PIE_2323, 84), REDBERRY(Items.REDBERRY_PIE_2325, 90), MEAT(Items.MEAT_PIE_2327, 96), GARDEN(
            Items.GARDEN_PIE_7178,
            112
        ),
        FISH(Items.FISH_PIE_7188, 125), ADMIRAL(Items.ADMIRAL_PIE_7198, 387);

        companion object {
            fun forId(id: Int): PieReward? = values().find { it.id == id }
        }
    }

    class RomilyWildPieHandler : UseWithHandler(Items.WILD_PIE_7208) {
        override fun newInstance(arg: Any?): Plugin<Any> {
            addHandler(NPCs.ROMILY_WEAKLAX_3205, NPC_TYPE, this)
            return this
        }

        override fun handle(event: NodeUsageEvent): Boolean {
            if (!hasDiaryTaskComplete(event.player, DiaryType.VARROCK, 2, 5)) {
                openDialogue(event.player, NPCs.ROMILY_WEAKLAX_3205, event.usedItem)
            }
            return true
        }
    }

}