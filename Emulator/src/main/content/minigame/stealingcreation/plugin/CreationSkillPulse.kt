package content.minigame.stealingcreation.plugin

import core.api.*
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.update.flag.context.Animation
import core.tools.RandomFunction
import kotlin.math.ceil

class CreationSkillPulse(
    val player: Player,
    val node: Node,
    val animation: Animation,
    var itemUsed: Item,
    val baseId: Int,
    val sceneryIndex: Int,
) : Pulse(1, player, node) {
    private lateinit var definitions: CreationScenery
    private val skillId = Skills.HUNTER

    enum class CreationScenery(
        val baseTime: Int,
        val randomLife: Int,
        val randomTime: Int,
        val level: Int,
    ) {
        CLASS_1(10, -1, 1, 1),
        CLASS_2(20, 200, 6, 20),
        CLASS_3(25, 300, 12, 40),
        CLASS_4(30, 400, 16, 60),
        CLASS_5(35, 500, 20, 80),
    }

    fun animate() {
        animate(player, animation)
    }

    override fun setDelay(delay: Int) {
        if (freeSlots(player) == 0) {
            sendMessage(player, "Not enough space in your inventory.")
            return
        } else if (RandomFunction.getRandom(definitions.randomLife) == 0) {
        }
        addItem(player, StealingCreation.sacredClayItem[sceneryIndex], 1)
        return
    }

    fun checkReward(
        player: Player,
        skillId: Int,
    ): Int {
        val playerLevel = player.getSkills().getLevel(Skills.FISHING)
        val fishLevel = definitions.level
        val modifier: Int = skillId
        val randomAmt: Int = RandomFunction.random(4)
        var cycleCount = 1.0
        val otherBonus = 0.0
        cycleCount = ceil(((fishLevel + otherBonus) * 50 - playerLevel * 10) / modifier * 0.25 - randomAmt * 4)
        if (cycleCount < 1) cycleCount = 1.0
        var delay = cycleCount.toInt() + 1
        delay /= delay
        return delay
    }

    override fun pulse(): Boolean {
        definitions = CreationScenery.CLASS_5
        return !(getStatLevel(player, StealingCreation.getScenery()) < definitions.level || itemUsed == null)
    }

    override fun stop() {
        super.stop()
        resetAnimator(player)
    }
}
