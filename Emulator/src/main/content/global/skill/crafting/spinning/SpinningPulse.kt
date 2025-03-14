package content.global.skill.crafting.spinning

import core.api.*
import core.game.container.impl.EquipmentContainer
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.entity.skill.SkillPulse
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import org.rs.consts.Items
import org.rs.consts.Sounds

class SpinningPulse(
    player: Player?,
    node: Item?,
    var amount: Int,
    val type: Spinning,
) : SkillPulse<Item?>(player, node) {
    var ticks = 0

    override fun checkRequirements(): Boolean {
        closeInterface(player)
        if (getStatLevel(player, Skills.CRAFTING) < type.level) {
            sendMessage(player, "You need a crafting level of " + type.level + " to make this.")
            return false
        }
        if (!inInventory(player, type.need, 1)) {
            sendMessage(player, "You need " + getItemName(type.need) + " to do this.")
            return false
        }
        return true
    }

    override fun animate() {
        if (ticks % 5 == 0) {
            animate(player, ANIMATION)
            playAudio(player, Sounds.SPINNING_2590)
        }
    }

    override fun reward(): Boolean {
        var tickThreshold = 4
        if ((
                player.achievementDiaryManager.getDiary(DiaryType.SEERS_VILLAGE)!!.isComplete(2) &&
                    withinDistance(
                        player,
                        Location(2711, 3471, 1),
                    ) &&
                    player.equipment[EquipmentContainer.SLOT_HAT] != null
            ) &&
            player.equipment[EquipmentContainer.SLOT_HAT].id == Items.SEERS_HEADBAND_1_14631
        ) {
            tickThreshold = 2
        }
        if (++ticks % tickThreshold != 0) {
            return false
        }
        if (removeItem(player, Item(type.need, 1))) {
            val item = Item(type.product, 1)
            player.inventory.add(item)
            rewardXP(player, Skills.CRAFTING, type.exp)

            if (player.viewport.region.id == 10806 && !hasDiaryTaskComplete(player, DiaryType.SEERS_VILLAGE, 0, 4)) {
                if (player.getAttribute("diary:seers:bowstrings-spun", 0) >= 4) {
                    setAttribute(player, "/save:diary:seers:bowstrings-spun", 5)
                    finishDiaryTask(player, DiaryType.SEERS_VILLAGE, 0, 4)
                } else {
                    setAttribute(
                        player,
                        "/save:diary:seers:bowstrings-spun",
                        getAttribute(player, "diary:seers:bowstrings-spun", 0) + 1,
                    )
                }
            }
        }
        amount--
        return amount < 1
    }

    override fun message(type: Int) {}

    companion object {
        private val ANIMATION = Animation(894)
    }
}
