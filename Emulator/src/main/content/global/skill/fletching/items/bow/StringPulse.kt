package content.global.skill.fletching.items.bow

import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.entity.skill.SkillPulse
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.world.map.zone.ZoneBorders
import core.game.world.update.flag.context.Animation

/**
 * The type String pulse.
 */
class StringPulse
/**
 * Instantiates a new String pulse.
 *
 * @param player the player
 * @param node   the node
 * @param bow    the bow
 * @param amount the amount
 */(player: Player?, node: Item?, private val bow: Strings, private var amount: Int) : SkillPulse<Item?>(player, node) {
    override fun checkRequirements(): Boolean {
        if (delay == 1) {
            delay = 2
        }
        if (player.getSkills().getLevel(Skills.FLETCHING) < bow.level) {
            player.dialogueInterpreter.sendDialogue("You need a fletching level of " + bow.level + " to string this bow.")
            return false
        }
        if (!player.inventory.containsItem(Item(bow.unfinished))) {
            return false
        }
        if (!player.inventory.containsItem(Item(bow.string))) {
            player.dialogueInterpreter.sendDialogue("You seem to have run out of bow strings.")
            return false
        }
        animate()
        return true
    }

    override fun animate() {
        player.animate(Animation.create(bow.animation))
    }

    override fun reward(): Boolean {
        if (player.inventory.remove(Item(bow.unfinished), Item(bow.string))) {
            player.inventory.add(Item(bow.product))
            player.getSkills().addExperience(Skills.FLETCHING, bow.experience, true)
            player.packetDispatch.sendMessage("You add a string to the bow.")

            if (bow == Strings.MAGIC_SHORTBOW && (ZoneBorders(2721, 3489, 2724, 3493, 0).insideBorder(player)
                        || ZoneBorders(2727, 3487, 2730, 3490, 0).insideBorder(player))
                && player.getAttribute("diary:seers:fletch-magic-short-bow", false)
            ) {
                player.achievementDiaryManager.finishTask(player, DiaryType.SEERS_VILLAGE, 2, 2)
            }
        }
        if (!player.inventory.containsItem(Item(bow.string)) || !player.inventory.containsItem(
                Item(
                    bow.unfinished
                )
            )
        ) {
            return true
        }
        amount--
        return amount == 0
    }

    override fun message(type: Int) {
    }
}