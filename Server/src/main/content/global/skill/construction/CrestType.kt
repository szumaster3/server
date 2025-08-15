package content.global.skill.construction

import core.api.anyInInventory
import core.api.hasLevelStat
import core.api.isQuestComplete
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import shared.consts.Items
import shared.consts.Quests

enum class CrestType(
    val symbol: String,
    val cost: Int = 5000,
    val requirement: (Player) -> Boolean = { true }
) {
    ARRAV("the Shield of Arrav symbol", requirement = { isQuestComplete(it, Quests.SHIELD_OF_ARRAV) }),
    ASGARNIA("the symbol of Asgarnia"),
    DORGESHUUN("the Dorgeshuun brooch", requirement = { isQuestComplete(it, Quests.THE_LOST_TRIBE) }),
    DRAGON("a dragon", requirement = { isQuestComplete(it, Quests.DRAGON_SLAYER) }),
    FAIRY("a fairy", requirement = { isQuestComplete(it, Quests.LOST_CITY) }),
    GUTHIX("the symbol of Guthix", requirement = { hasLevelStat(it, Skills.PRAYER, 70) }),
    HAM("the symbol of the HAM cult."),
    HORSE("a horse", requirement = { anyInInventory(it, Items.TOY_HORSEY_2524, Items.TOY_HORSEY_2520, Items.TOY_HORSEY_2526, Items.TOY_HORSEY_2522) }),
    JOGRE("Jiggig"),
    KANDARIN("the symbol of Kandarin"),
    MISTHALIN("the symbol of Misthalin"),
    MONEY("a bag of money", cost = 500000),
    SARADOMIN("the symbol of Saradomin", requirement = { hasLevelStat(it, Skills.PRAYER, 70) }),
    SKULL("a skull", requirement = { it.skullManager.isSkulled }),
    VARROCK("the symbol of Varrock"),
    ZAMORAK("the symbol of Zamorak", requirement = { hasLevelStat(it, Skills.PRAYER, 70) });

    fun eligible(player: Player): Boolean = requirement(player)
}