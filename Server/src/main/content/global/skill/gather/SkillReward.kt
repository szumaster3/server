package content.global.skill.gather

import content.data.skill.SkillingTool
import content.global.skill.gather.mining.MiningNode
import content.global.skill.gather.woodcutting.WoodcuttingNode
import core.api.getDynLevel
import core.api.getFamiliarBoost
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.tools.RandomFunction
import kotlin.math.ceil
import kotlin.random.Random

/**
 * Utility object for calculating skill-based success chances.
 */
object SkillReward {
    /**
     * Checks if a woodcutting action succeeds based on woodcutting level,
     * the resource node, and the tool used.
     *
     * @param player        the player.
     * @param resource      the woodcutting node.
     * @param tool          the used tool.
     * @return true if the mining attempt succeeds, false otherwise
     */
    @JvmStatic
    fun checkWoodcuttingReward(player: Player, resource: WoodcuttingNode, tool: SkillingTool): Boolean {
        val skill = Skills.WOODCUTTING
        val level: Int = player.getSkills().getLevel(skill) + player.familiarManager.getBoost(skill)
        val hostRatio = RandomFunction.randomDouble(100.0)
        val lowMod: Double = if (tool == SkillingTool.BLACK_AXE) resource.tierModLow / 2 else resource.tierModLow
        val low: Double = resource.baseLow + tool.ordinal * lowMod
        val highMod: Double = if (tool == SkillingTool.BLACK_AXE) resource.tierModHigh / 2 else resource.tierModHigh
        val high: Double = resource.baseHigh + tool.ordinal * highMod
        val clientRatio = RandomFunction.getSkillSuccessChance(low, high, level)
        return hostRatio < clientRatio
    }

    /**
     * Checks if a mining action succeeds based on mining level, object, and the tool used.
     *
     * @param player        the player.
     * @param resource      the mining node.
     * @param tool          the used tool.
     * @return true if the mining attempt succeeds, false otherwise
     */
    @JvmStatic
    fun checkMiningReward(player: Player, resource: MiningNode?, tool: SkillingTool, ): Boolean {
        val level = 1 + getDynLevel(player, Skills.MINING) + getFamiliarBoost(player, Skills.MINING)
        val hostRatio = Math.random() * (100.0 * resource!!.rate)
        var toolRatio = tool.ratio
        val clientRatio = Math.random() * ((level - resource.level) * (1.0 + toolRatio))

        return hostRatio < clientRatio
    }

    /**
     * Checks if an agility action succeeds.
     *
     * @param player        the player performing the action
     * @param level         the required agility level
     * @param failChance    the base fail chance of the obstacle
     * @return true if the agility action succeeds, false otherwise
     */
    @JvmStatic
    fun checkAgilitySuccess(player: Player, level: Int, failChance: Double): Boolean {
        val levelDiff = player.skills.getLevel(Skills.AGILITY) - level
        if (levelDiff > 69) return false
        val chance = (1 + levelDiff) * 0.01 * Random.nextDouble()
        return chance <= (Random.nextDouble() * failChance)
    }


    /**
     * Gets the success chance for a skill based interaction.
     */
    fun getSuccessChance(player: Player, low: Double, high: Double, skill: Int): Double {
        val level = player.skills.getLevel(skill)
        return RandomFunction.getSkillSuccessChance(low, high, level)
    }

    /**
     * Rolls for success and returns true if action succeeds.
     */
    fun rollSuccess(player: Player, low: Double, high: Double, skill: Int): Boolean {
        val chance = getSuccessChance(player, low, high, skill)
        return RandomFunction.randomDouble(100.0) < chance
    }

    /**
     * Calculates whether an action succeeds based on the player skill level.
     * @param player The player whose skill level is used for the calculation.
     * @param skill The id of the skill to check (e.g., Mining, Thieving, etc.).
     * @return `true` if the action succeeds, `false` otherwise.
     */
    fun success(player: Player, skill: Int): Boolean {
        val level = player.getSkills().getLevel(skill).toDouble()
        val req = 30.0
        val successChance = ceil((level * 50 - req) / req / 3 * 4)
        val roll = RandomFunction.random(99)
        return successChance >= roll
    }
}