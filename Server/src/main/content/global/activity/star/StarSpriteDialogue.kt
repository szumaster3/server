package content.global.activity.star

import com.google.gson.JsonObject
import core.ServerStore
import core.ServerStore.Companion.getBoolean
import core.api.*
import core.game.dialogue.Dialogue
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.Items
import shared.consts.NPCs
import kotlin.math.roundToInt

/**
 * Represents the Star Sprite dialogue.
 */
@Initializable
class StarSpriteDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if (getStoreFile().getBoolean(player.username.lowercase()) || !inInventory(player, ShootingStarPlugin.STAR_DUST, 1)) {
            npc("Hello, strange creature.")
        } else {
            npc("Thank you for helping me out of here.").also { stage = 50 }
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> npc("I'm a star sprite! I was in my star in the sky, when it", "lost control and crashed into the ground. With half my", "star sticking into the ground, I became stuck.", "Fortunately, I was mined out by the kind creatures of").also { stage++ }
            1 -> npc("your race.").also { stage++ }
            2 -> options("What's a star sprite?", "What are you going to do without your star?", "I thought stars were huge balls of burning gas.", "Well, I'm glad you're okay.").also { stage++ }
            3 -> when (buttonId) {
                1 -> player("What's a star sprite?").also { stage = 10 }
                2 -> player("What are you going to do without your star?").also { stage = 20 }
                3 -> player("I thought stars were huge balls of burning gas.").also { stage = 30 }
                4 -> player("Well, I'm glad you're okay.").also { stage = 40 }
            }

            10 -> npc("We're what makes the stars in the sky shine. I made", "this star shine when it was in the sky.").also { stage++ }
            11 -> options("What are you going to do without your star?", "I thought stars were huge balls of burning gas.", "Well, I'm glad you're okay.").also { stage++ }
            12 -> when (buttonId) {
                1 -> player("What are you going to do without your star?").also { stage = 20 }
                2 -> player("I thought stars were huge balls of burning gas.").also { stage = 30 }
                3 -> player("Well, I'm glad you're okay.").also { stage = 40 }
            }
            20 -> npc("Don't worry about me. I'm sure I'll find some good", "rocks around here and get back up into the sky in no", "time.").also { stage++ }
            21 -> options("What's a star sprite?", "I thought stars were huge balls of burning gas.", "Well, I'm glad you're okay.").also { stage++ }
            22 -> when (buttonId) {
                1 -> player("What's a star sprite?").also { stage = 10 }
                2 -> player("I thought stars were huge balls of burning gas.").also { stage = 30 }
                3 -> player("Well, I'm glad you're okay.").also { stage = 40 }
            }

            30 -> npc("Most of them are, but a lot of shooting stars on this", "plane of the multiverse are rocks with star sprites in", "them.").also { stage++ }
            31 -> options("What's a star sprite?", "What are you going to do without your star?", "Well, I'm glad you're okay.").also { stage++ }
            32 -> when (buttonId) {
                1 -> player("What's a star sprite?").also { stage = 10 }
                2 -> player("What are you going to do without your star?").also { stage = 20 }
                3 -> player("Well, I'm glad you're okay.").also { stage = 40 }
            }

            40 -> npc("Thank you.").also { stage = END_DIALOGUE }
            50 -> {
                val dust = if (amountInInventory(player, ShootingStarPlugin.STAR_DUST) > 200) {
                    200
                } else {
                    amountInInventory(
                        player,
                        ShootingStarPlugin.STAR_DUST,
                    )
                }
                if (removeItem(player, Item(ShootingStarPlugin.STAR_DUST, dust))) {
                    val cosmicRunes = (Math.ceil(0.76 * dust) * AMPLIFIER).toInt()
                    val astralRunes = (Math.ceil(0.26 * dust) * AMPLIFIER).toInt()
                    val goldOre = (Math.ceil(0.1 * dust) * AMPLIFIER).toInt()
                    val coins = (Math.ceil(250.01 * dust) * AMPLIFIER).roundToInt()
                    player.inventory.add(Item(COSMIC_RUNE, cosmicRunes), player)
                    player.inventory.add(Item(ASTRAL_RUNE, astralRunes), player)
                    player.inventory.add(Item(NOTED_GOLD_ORE, goldOre), player)
                    player.inventory.add(Item(COINS, coins), player)
                    npc(
                        "I have rewarded you by making it so you can mine",
                        "extra ore for the next 15 minutes. Also, have $cosmicRunes",
                        "cosmic runes, $astralRunes astral runes, $goldOre gold ore and $coins",
                        "coins.",
                    )
                    getStoreFile().addProperty(player.username.lowercase(), true)
                    val timer = getOrStartTimer<ShootingStarBonus>(player)
                    timer.ticksLeft = 1500
                }
                stage = 52
            }

            52 -> end()
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = StarSpriteDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.STAR_SPRITE_8091)

    fun getStoreFile(): JsonObject = ServerStore.getArchive("daily-shooting-star")

    companion object {
        private const val COSMIC_RUNE = Items.COSMIC_RUNE_564
        private const val ASTRAL_RUNE = Items.ASTRAL_RUNE_9075
        private const val NOTED_GOLD_ORE = Items.GOLD_ORE_445
        private const val COINS = Items.COINS_995
        private const val AMPLIFIER = 1.0
    }
}
