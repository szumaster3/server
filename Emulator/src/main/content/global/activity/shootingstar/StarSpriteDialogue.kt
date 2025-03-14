package content.global.activity.shootingstar

import core.ServerStore
import core.ServerStore.Companion.getBoolean
import core.api.*
import core.game.dialogue.Dialogue
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.RandomFunction
import core.tools.colorize
import org.json.simple.JSONObject
import org.rs.consts.NPCs

@Initializable
class StarSpriteDialogue(
    player: Player? = null,
) : Dialogue(player) {
    val COSMIC_RUNE = 564
    val ASTRAL_RUNE = 9075
    val GOLD_ORE = 445
    val COINS = 995
    val AMPLIFIER = 1.0

    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        if (getStoreFile().getBoolean(player.username.lowercase()) ||
            !inInventory(
                player,
                ShootingStarPlugin.STAR_DUST,
                1,
            )
        ) {
            npc("Hello, strange creature.").also { stage = 0 }
        } else {
            npc("Thank you for helping me out of here.").also { stage = 50 }
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                npc(
                    "I'm a star sprite! I was in my star in the sky, when it",
                    "lost control and crashed into the ground. With half my",
                    "star sticking into the ground, I became stuck.",
                    "Fortunately, I was mined out by the kind creatures of",
                ).also { stage++ }

            1 -> npc("your race.").also { stage++ }
            2 ->
                options(
                    "What's a star sprite?",
                    "What are you going to do without your star?",
                    "I thought stars were huge balls of burning gas.",
                    "Well, I'm glad you're okay.",
                ).also { stage++ }

            3 ->
                when (buttonId) {
                    1 -> player("What's a star sprite?").also { stage = 10 }
                    2 -> player("What are you going to do without your star?").also { stage = 20 }
                    3 -> player("I thought stars were huge balls of burning gas.").also { stage = 30 }
                    4 -> player("Well, I'm glad you're okay.").also { stage = 40 }
                }

            10 ->
                npc(
                    "We're what makes the stars in the sky shine. I made",
                    "this star shine when it was in the sky.",
                ).also { stage++ }

            11 ->
                options(
                    "What are you going to do without your star?",
                    "I thought stars were huge balls of burning gas.",
                    "Well, I'm glad you're okay.",
                ).also { stage++ }

            12 ->
                when (buttonId) {
                    1 -> player("What are you going to do without your star?").also { stage = 20 }
                    2 -> player("I thought stars were huge balls of burning gas.").also { stage = 30 }
                    3 -> player("Well, I'm glad you're okay.").also { stage = 40 }
                }

            20 ->
                npc(
                    "Don't worry about me. I'm sure I'll find some good",
                    "rocks around here and get back up into the sky in no",
                    "time.",
                ).also { stage++ }

            21 ->
                options(
                    "What's a star sprite?",
                    "I thought stars were huge balls of burning gas.",
                    "Well, I'm glad you're okay.",
                ).also { stage++ }

            22 ->
                when (buttonId) {
                    1 -> player("What's a star sprite?").also { stage = 10 }
                    2 -> player("I thought stars were huge balls of burning gas.").also { stage = 30 }
                    3 -> player("Well, I'm glad you're okay.").also { stage = 40 }
                }

            30 ->
                npc(
                    "Most of them are, but a lot of shooting stars on this",
                    "plane of the multiverse are rocks with star sprites in",
                    "them.",
                ).also { stage++ }

            31 ->
                options(
                    "What's a star sprite?",
                    "What are you going to do without your star?",
                    "Well, I'm glad you're okay.",
                ).also { stage++ }

            32 ->
                when (buttonId) {
                    1 -> player("What's a star sprite?").also { stage = 10 }
                    2 -> player("What are you going to do without your star?").also { stage = 20 }
                    3 -> player("Well, I'm glad you're okay.").also { stage = 40 }
                }

            40 -> npc("Thank you.").also { stage = END_DIALOGUE }
            50 -> {
                val dust =
                    if (amountInInventory(player, ShootingStarPlugin.STAR_DUST) > 200) {
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
                    val coins =
                        (Math.ceil(250.0 * dust) * AMPLIFIER).toInt()
                    player.inventory.add(Item(COSMIC_RUNE, cosmicRunes), player)
                    player.inventory.add(Item(ASTRAL_RUNE, astralRunes), player)
                    player.inventory.add(Item(GOLD_ORE, goldOre), player)
                    player.inventory.add(Item(COINS, coins), player)
                    npc(
                        "I have rewarded you by making it so you can mine",
                        "extra ore for the next 15 minutes. Also, have $cosmicRunes",
                        "cosmic runes, $astralRunes astral runes, $goldOre gold ore and $coins",
                        "coins.",
                    )
                    getStoreFile()[player.username.lowercase()] = true
                    val timer = getOrStartTimer<StarBonus>(player)
                    timer.ticksLeft = 1500
                }
                stage = 52
            }

            52 -> end()
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return StarSpriteDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.STAR_SPRITE_8091)
    }

    fun rollForRingBonus(
        player: Player,
        bonusId: Int,
        bonusBaseAmt: Int,
    ) {
        if (RandomFunction.roll(3)) {
            var bonus = getOrStartTimer<StarBonus>(player)
            bonus.ticksLeft += 500
            sendMessage(player, colorize("%RYour ring shines dimly as if imbued with energy."))
        } else if (RandomFunction.roll(5)) {
            addItem(player, bonusId, bonusBaseAmt)
            sendMessage(player, colorize("%RYour ring shines brightly as if surging with energy and then fades out."))
        } else if (RandomFunction.roll(25)) {
            getStoreFile()[player.username.lowercase()] = false
            sendMessage(player, colorize("%RYour ring vibrates briefly as if surging with power, and then stops."))
        }
    }

    fun getStoreFile(): JSONObject {
        return ServerStore.getArchive("daily-shooting-star")
    }
}
