package content.global.skill.fletching

import core.api.*
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.entity.skill.SkillPulse
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.world.map.zone.ZoneBorders
import core.game.world.update.flag.context.Animation
import core.tools.RandomFunction
import core.tools.StringUtils
import shared.consts.Animations
import shared.consts.Items
import shared.consts.Quests
import shared.consts.Sounds

/**
 * Represents the fletching skill pulse.
 */
class FletchingPulse(
    player: Player,
    private val node: Item,
    private var amount: Int,
    private val fletch: Fletching.FletchingItems
) : SkillPulse<Item>(player, node) {

    companion object {
        private val bankZone = ZoneBorders(2721, 3493, 2730, 3487)
        private val ANIMATION = Animation(Animations.FLETCH_LOGS_1248)
    }

    private var finalAmount = 0

    override fun checkRequirements(): Boolean {
        if (getStatLevel(player, Skills.FLETCHING) < fletch.level) {
            sendDialogue(
                player,
                "You need a fletching skill of ${fletch.level} or above to make " +
                        (if (StringUtils.isPlusN(getItemName(fletch.id).replace("(u)", "").trim())) "an"
                        else "a") +
                        " ${getItemName(fletch.id).replace("(u)", "").trim()}"
            )
            return false
        }

        if (amount > amountInInventory(player, node!!.id)) {
            amount = amountInInventory(player, node!!.id)
        }

        if (
            fletch == Fletching.FletchingItems.OGRE_ARROW_SHAFT &&
            getQuestStage(player, Quests.BIG_CHOMPY_BIRD_HUNTING) == 0
        ) {
            sendMessage(player, "You must have started Big Chompy Bird Hunting to make those.")
            return false
        }

        if (fletch == Fletching.FletchingItems.OGRE_COMPOSITE_BOW) {
            if (getQuestStage(player, Quests.ZOGRE_FLESH_EATERS) == 0) {
                sendMessage(player, "You must have started Zogre Flesh Eaters to make those.")
                return false
            }
            if (!inInventory(player, Items.WOLF_BONES_2859, 1)) {
                sendMessage(player, "You need to have wolf bones in order to make this.")
                return false
            }
        }
        return true
    }

    override fun animate() {
        player.animate(ANIMATION)
    }

    override fun reward(): Boolean {
        if (bankZone.insideBorder(player) && fletch == Fletching.FletchingItems.MAGIC_SHORTBOW) {
            player.achievementDiaryManager.finishTask(player, DiaryType.SEERS_VILLAGE, 2, 2)
        }

        if (delay == 1) {
            setDelay(4)
            return false
        }

        if (player.inventory.remove(node)) {
            val item = Item(fletch.id, fletch.amount)

            when (fletch) {
                Fletching.FletchingItems.OGRE_ARROW_SHAFT -> {
                    finalAmount = RandomFunction.random(2, 6)
                    item.amount = finalAmount
                }
                Fletching.FletchingItems.OGRE_COMPOSITE_BOW -> {
                    if (!player.inventory.contains(Items.WOLF_BONES_2859, 1)) return false
                    else player.inventory.remove(Item(Items.WOLF_BONES_2859))
                }
                else -> {}
            }

            player.inventory.add(item)
            player.skills.addExperience(Skills.FLETCHING, fletch.experience, true)
            player.packetDispatch.sendMessage(getMessage())

            if (
                fletch.id == Fletching.FletchingItems.MAGIC_SHORTBOW.id &&
                (ZoneBorders(2721, 3489, 2724, 3493, 0).insideBorder(player) ||
                        ZoneBorders(2727, 3487, 2730, 3490, 0).insideBorder(player)) &&
                !player.achievementDiaryManager.hasCompletedTask(DiaryType.SEERS_VILLAGE, 2, 2)
            ) {
                player.setAttribute("/save:diary:seers:fletch-magic-short-bow", true)
            }
        } else {
            return true
        }

        amount--
        return amount == 0
    }

    private fun getMessage(): String =
        when (fletch) {
            Fletching.FletchingItems.ARROW_SHAFT ->
                "You carefully cut the logs into 15 arrow shafts."
            Fletching.FletchingItems.OGRE_ARROW_SHAFT ->
                "You carefully cut the logs into $finalAmount arrow shafts."
            Fletching.FletchingItems.OGRE_COMPOSITE_BOW ->
                "You carefully cut the logs into composite ogre bow."
            else -> {
                val name = getItemName(fletch.id).replace("(u)", "").trim()
                "You carefully cut the logs into ${if (StringUtils.isPlusN(name)) "an" else "a"} $name."
            }
        }
}
