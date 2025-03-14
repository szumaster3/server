package content.global.skill.fletching

import core.api.*
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.entity.skill.SkillPulse
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.world.map.zone.ZoneBorders
import core.tools.StringUtils
import org.rs.consts.Animations
import org.rs.consts.Items
import org.rs.consts.Quests

class FletchingPulse(
    player: Player?,
    node: Item?,
    amount: Int,
    fletch: Fletching.FletchingItems,
) : SkillPulse<Item?>(player, node) {
    private val fletch: Fletching.FletchingItems
    private var amount = 0
    private val trim = getItemName(fletch.id).replace("(u)", "").trim()
    private val bankZone = ZoneBorders(2721, 3493, 2730, 3487)

    init {
        this.amount = amount
        this.fletch = fletch
    }

    override fun checkRequirements(): Boolean {
        if (getStatLevel(player, Skills.FLETCHING) < fletch.level) {
            sendDialogue(
                player,
                "You need a fletching skill of " + fletch.level + " or above to make " +
                    (if (StringUtils.isPlusN(trim)) "an" else "a") +
                    " " +
                    trim,
            )
            return false
        }
        if (amount > amountInInventory(player, node!!.id)) {
            amount = amountInInventory(player, node!!.id)
        }
        if (fletch == Fletching.FletchingItems.OGRE_ARROW_SHAFT) {
            if (player.getQuestRepository().getQuest(Quests.BIG_CHOMPY_BIRD_HUNTING).getStage(player) == 0) {
                sendMessage(player, "You must have started Big Chompy Bird Hunting to make those.")
                return false
            }
        }
        if (fletch == Fletching.FletchingItems.OGRE_COMPOSITE_BOW) {
            if (player.getQuestRepository().getQuest(Quests.ZOGRE_FLESH_EATERS).getStage(player) == 0) {
                sendMessage(player, "You must have started Zogre Flesh Eaters to make those.")
                return false
            }
        }
        return true
    }

    override fun animate() {
        animate(player, Animations.FLETCH_LOGS_1248)
    }

    override fun reward(): Boolean {
        if (bankZone.insideBorder(player) && fletch == Fletching.FletchingItems.MAGIC_SHORTBOW) {
            finishDiaryTask(player, DiaryType.SEERS_VILLAGE, 2, 2)
        }
        if (delay == 1) {
            super.setDelay(3)
            return false
        }
        if (removeItem(player, node)) {
            val item = Item(fletch.id, fletch.amount)
            if (fletch == Fletching.FletchingItems.OGRE_ARROW_SHAFT) {
                item.amount = 4
            }
            if (fletch == Fletching.FletchingItems.OGRE_COMPOSITE_BOW) {
                item.id = Items.UNSTRUNG_COMP_BOW_4825
            }
            addItem(player, item.id, item.amount)
            rewardXP(player, Skills.FLETCHING, fletch.experience)
            val message = message
            sendMessage(player, message)

            if (fletch.id == Fletching.FletchingItems.MAGIC_SHORTBOW.id &&
                (
                    ZoneBorders(
                        2721,
                        3489,
                        2724,
                        3493,
                        0,
                    ).insideBorder(player) ||
                        ZoneBorders(
                            2727,
                            3487,
                            2730,
                            3490,
                            0,
                        ).insideBorder(player)
                ) &&
                !hasDiaryTaskComplete(player, DiaryType.SEERS_VILLAGE, 2, 2)
            ) {
                setAttribute(player, "/save:diary:seers:fletch-magic-short-bow", true)
            }
        } else {
            return true
        }
        amount--
        return amount == 0
    }

    val message: String
        get() {
            val itemName = getItemName(fletch.id).lowercase().replace("(u)", "").trim()
            val an = if (StringUtils.isPlusN(itemName)) "an" else "a"

            return when (fletch) {
                Fletching.FletchingItems.ARROW_SHAFT -> "You carefully cut the wood into 15 arrow shafts."
                Fletching.FletchingItems.OGRE_COMPOSITE_BOW -> "You carefully cut the wood into composite ogre bow."
                else -> "You carefully cut the wood into $an $itemName."
            }
        }
}
