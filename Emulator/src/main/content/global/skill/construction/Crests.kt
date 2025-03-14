package content.global.skill.construction

import core.api.anyInInventory
import core.api.hasLevelStat
import core.api.quest.isQuestComplete
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import org.rs.consts.Items
import org.rs.consts.Quests

private val CREST_SYMBOLS =
    mapOf(
        "ARRAV" to "the Shield of Arrav symbol",
        "ASGARNIA" to "the symbol of Asgarnia",
        "DORGESHUUN" to "the Dorgeshuun brooch",
        "DRAGON" to "a dragon",
        "FAIRY" to "a fairy",
        "GUTHIX" to "the symbol of Guthix",
        "HAM" to "the symbol of the HAM cult.",
        "HORSE" to "a horse",
        "JOGRE" to "Jiggig",
        "KANDARIN" to "the symbol of Kandarin",
        "MISTHALIN" to "the symbol of Misthalin",
        "MONEY" to "a bag of money",
        "SARADOMIN" to "the symbol of Saradomin",
        "SKULL" to "a skull",
        "VARROCK" to "the symbol of Varrock",
        "ZAMORAK" to "the symbol of Zamorak",
    )

enum class Crests(
    val cost: Int = 5000,
) : CrestRequirement {
    ARRAV {
        override fun eligible(player: Player): Boolean = isQuestComplete(player, Quests.SHIELD_OF_ARRAV)
    },
    ASGARNIA,
    DORGESHUUN {
        override fun eligible(player: Player): Boolean = isQuestComplete(player, Quests.THE_LOST_TRIBE)
    },
    DRAGON {
        override fun eligible(player: Player): Boolean = isQuestComplete(player, Quests.DRAGON_SLAYER)
    },
    FAIRY {
        override fun eligible(player: Player): Boolean = isQuestComplete(player, Quests.LOST_CITY)
    },
    GUTHIX {
        override fun eligible(player: Player): Boolean = hasLevelStat(player, Skills.PRAYER, 70)
    },
    HAM,
    HORSE {
        override fun eligible(player: Player): Boolean =
            anyInInventory(
                player,
                Items.TOY_HORSEY_2524,
                Items.TOY_HORSEY_2520,
                Items.TOY_HORSEY_2526,
                Items.TOY_HORSEY_2522,
            )
    },
    JOGRE,
    KANDARIN,
    MISTHALIN,
    MONEY(500000),
    SARADOMIN {
        override fun eligible(player: Player): Boolean = hasLevelStat(player, Skills.PRAYER, 70)
    },
    SKULL {
        override fun eligible(player: Player): Boolean = player.skullManager.isSkulled
    },
    VARROCK,
    ZAMORAK {
        override fun eligible(player: Player): Boolean = hasLevelStat(player, Skills.PRAYER, 70)
    }, ;

    fun getSymbol(): String = CREST_SYMBOLS[this.name] ?: "unknown crest"
}
