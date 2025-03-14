package content.global.skill.cooking.handlers

import core.api.forceWalk
import core.api.getStatLevel
import core.api.inInventory
import core.api.sendMessage
import core.game.node.entity.player.Player
import core.game.node.entity.skill.SkillPulse
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.world.map.Location
import org.rs.consts.Animations
import org.rs.consts.Items

class AppleMushPulse(
    player: Player?,
    item: Item?,
) : SkillPulse<Item?>(player, null) {
    private var tick = 0

    override fun checkRequirements(): Boolean {
        if (getStatLevel(player, Skills.COOKING) < 14) {
            sendMessage(player, "You need a cooking level of 14 to do this.")
            return false
        }
        if (!inInventory(player, Items.BUCKET_1925)) {
            sendMessage(player, "You need a bucket to do that.")
            return false
        }
        if (!inInventory(player, Items.COOKING_APPLE_1955, 4)) {
            sendMessage(player, "You need at least 4 cooking apples.")
            return false
        }

        return true
    }

    override fun start() {
        super.start()
        if (player.location != START_LOCATION) {
            return forceWalk(player, START_LOCATION, "smart")
        }
    }

    override fun animate() {
    }

    override fun reward(): Boolean {
        return false
    }

    companion object {
        val START_LOCATION: Location = Location.create(2914, 10193, 1)
        val BARREL_LOCATION: Location = Location(2914, 10192, 1)
        val JUMP_ON_BARREL: Int = Animations.JUMPING_ON_APPLE_MUSH_2306
        val APPLE_MUSH: Item = Item(Items.APPLE_MUSH_5992, 1)
    }
}
