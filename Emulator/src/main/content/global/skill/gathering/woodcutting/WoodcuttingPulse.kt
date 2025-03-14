package content.global.skill.gathering.woodcutting

import content.data.GameAttributes
import content.data.items.SkillingTool
import content.data.items.SkillingTool.Companion.getHatchet
import content.data.tables.BirdNestDropTable.Companion.drop
import content.data.tables.BirdNestDropTable.Companion.getRandomNest
import content.global.skill.farming.FarmingPatch.Companion.forObject
import core.api.*
import core.game.container.impl.EquipmentContainer
import core.game.dialogue.FaceAnim
import core.game.event.ResourceProducedEvent
import core.game.node.entity.impl.Projectile
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.entity.skill.Skills
import core.game.node.scenery.Scenery
import core.game.system.command.sets.STATS_BASE
import core.game.system.command.sets.STATS_LOGS
import core.game.system.task.Pulse
import core.game.world.map.Location
import core.game.world.map.RegionManager.getLocalPlayers
import core.tools.RandomFunction
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Sounds
import java.util.stream.Collectors

class WoodcuttingPulse(
    private val player: Player,
    private val node: Scenery,
) : Pulse(1, player, node) {
    private val woodcuttingSounds =
        intArrayOf(
            Sounds.WOODCUTTING_HIT_3038,
            Sounds.WOODCUTTING_HIT_3039,
            Sounds.WOODCUTTING_HIT_3040,
            Sounds.WOODCUTTING_HIT_3041,
            Sounds.WOODCUTTING_HIT_3042,
        )

    private var resource: WoodcuttingNode? = null
    private var ticks = 0

    var resetAnimation: Boolean = true

    init {
        super.stop()
    }

    fun message(type: Int) {
        if (type == 0) {
            sendMessage(player, "You swing your axe at the tree.")
        }
    }

    override fun pulse(): Boolean {
        if (!checkRequirements()) {
            return true
        }
        animate()
        return reward()
    }

    override fun stop() {
        if (resetAnimation) {
            resetAnimator(player)
        }
        super.stop()
        message(1)
    }

    override fun start() {
        resource = WoodcuttingNode.forId(node.id)
        if (resource == null) {
            return
        }
        if (checkRequirements()) {
            super.start()
            message(0)
        }
    }

    fun checkRequirements(): Boolean {
        if (getStatLevel(player, Skills.WOODCUTTING) < resource!!.level) {
            sendMessage(player, "You need a woodcutting level of " + resource!!.level + " to chop this tree.")
            return false
        }
        if (getHatchet(player) == null) {
            sendMessage(player, "You do not have an axe to use.")
            return false
        }
        if (freeSlots(player) < 1) {
            sendDialogue(
                player,
                "Your inventory is too full to hold any more " + getItemName(resource!!.reward).lowercase() + ".",
            )
            return false
        }
        return true
    }

    fun animate() {
        if (!player.animator.isAnimating) {
            animate(player, getHatchet(player)!!.animation)
            val playersAroundMe =
                getLocalPlayers(player, 2)
                    .stream()
                    .filter { p: Player ->
                        p.username !=
                            player.username
                    }.collect(Collectors.toList())
            val soundIndex = RandomFunction.random(0, woodcuttingSounds.size)
            for (p in playersAroundMe) playAudio(p!!, woodcuttingSounds[soundIndex])
        }
    }

    fun reward(): Boolean {
        if (++ticks % 4 != 0) {
            return false
        }
        if (node.id == 10041) {
            sendNPCDialogue(
                player,
                NPCs.BANK_GUARD_2574,
                if (RandomFunction.random(2) ==
                    1
                ) {
                    "You'll blow my cover! I'm meant to be hidden!"
                } else {
                    "Will you stop that?"
                },
                FaceAnim.FURIOUS,
            )
            return true
        }
        if (!checkReward(getHatchet(player))) {
            return false
        }

        if (getHatchet(player)!!.id == Items.INFERNO_ADZE_13661 && RandomFunction.random(100) < 25) {
            sendMessage(player, "You chop some logs. The heat of the inferno adze incinerates them.")
            Projectile
                .create(
                    player,
                    null,
                    1776,
                    35,
                    30,
                    20,
                    25,
                ).transform(player, Location(player.location.x + 2, player.location.y), true, 25, 25)
                .send()
            rewardXP(player, Skills.WOODCUTTING, resource!!.experience)
            rewardXP(player, Skills.FIREMAKING, resource!!.experience)
            return rollDepletion()
        }

        var reward = resource!!.reward
        var rewardAmount = 0
        if (reward > 0) {
            reward = calculateReward(reward)
            rewardAmount = calculateRewardAmount(reward)

            val experience = calculateExperience(resource!!.reward, rewardAmount)
            rewardXP(player, Skills.WOODCUTTING, experience)

            if (resource == WoodcuttingNode.STANDARD_TREE_10) {
                if (!getAttribute(player, GameAttributes.TUTORIAL_COMPLETE, false)) {
                    sendItemDialogue(player, Items.LOGS_1511, "You get some logs.")
                }

                if (getAttribute(player, GameAttributes.TUTORIAL_STAGE, -1) < 4) {
                    sendUnclosableDialogue(
                        player,
                        "You cannot cut down this tree yet. You must progress further in the tutorial.",
                    )
                }
            }

            if (resource == WoodcuttingNode.DRAMEN_TREE) {
                sendMessage(player, "You cut a branch from the Dramen tree.")
                stop()
            } else {
                sendMessage(player, "You get some " + getItemName(reward).lowercase() + ".")
            }

            addItem(player, reward, rewardAmount)
            player.dispatch(ResourceProducedEvent(reward, rewardAmount, node, -1))
            var cutLogs = getAttribute(player, "$STATS_BASE:$STATS_LOGS", 0)
            setAttribute(player, "/save:$STATS_BASE:$STATS_LOGS", ++cutLogs)
            val chance = 282
            if (RandomFunction.random(chance) == chance / 2) {
                if (!player.inventory.add(getRandomNest(false)!!.nest)) {
                    drop(player)
                }
            }
        }

        return rollDepletion()
    }

    private fun rollDepletion(): Boolean {
        if (resource!!.respawnRate > 0) {
            if (RandomFunction.roll(8) ||
                resource!!.identifier.toInt() == 1 ||
                resource!!.identifier.toInt() == 2 ||
                resource!!.identifier.toInt() == 3 ||
                resource!!.identifier.toInt() == 6 ||
                resource!!.identifier.toInt() == 19
            ) {
                if (resource!!.isFarming) {
                    val fPatch = forObject(node.asScenery())
                    if (fPatch != null) {
                        val patch = fPatch.getPatchFor(player, true)
                        patch.setCurrentState(patch.getCurrentState() + 1)
                    }
                    return true
                }
                if (resource!!.emptyId > -1) {
                    replaceScenery(node, node.transform(resource!!.emptyId).id, resource!!.respawnDuration)
                } else {
                    replaceScenery(node, node.transform(0).id, resource!!.respawnDuration)
                }
                node.isActive = false

                playAudio(player, Sounds.TREE_FALLING_2734)
                return true
            }
        }
        return false
    }

    private fun calculateRewardAmount(reward: Int): Int {
        var amount = 1

        if (reward == 3239 && RandomFunction.random(100) >= 10) {
            amount = 0
        }

        if (reward == 1511 &&
            isDiaryComplete(player, DiaryType.SEERS_VILLAGE, 1) &&
            player.viewport.region.id == 10806
        ) {
            amount = 2
        }

        return amount
    }

    private fun calculateExperience(
        reward: Int,
        amount: Int,
    ): Double {
        var amount = amount
        var experience = resource!!.experience
        if (player.location.regionId == 10300) {
            return 1.0
        }
        if (reward == 3239) {
            if (amount >= 1) {
                experience = 275.2
            } else {
                amount = 1
            }
        }

        if (reward == 1517 &&
            player.achievementDiaryManager.getDiary(DiaryType.SEERS_VILLAGE)!!.isComplete(1) &&
            player.equipment[EquipmentContainer.SLOT_HAT] != null &&
            player.equipment[EquipmentContainer.SLOT_HAT].id == Items.SEERS_HEADBAND_1_14631
        ) {
            experience *= 1.10
        }
        return experience * amount
    }

    private fun calculateReward(reward: Int): Int {
        return reward
    }

    private fun checkReward(tool: SkillingTool?): Boolean {
        val skill = Skills.WOODCUTTING
        val level = player.getSkills().getLevel(skill) + player.familiarManager.getBoost(skill)
        val hostRatio = RandomFunction.randomDouble(100.0)
        val lowMod = if (tool == SkillingTool.BLACK_AXE) resource!!.tierModLow / 2 else resource!!.tierModLow
        val low = resource!!.baseLow + (tool!!.ordinal * lowMod)
        val highMod = if (tool == SkillingTool.BLACK_AXE) resource!!.tierModHigh / 2 else resource!!.tierModHigh
        val high = resource!!.baseHigh + (tool.ordinal * highMod)
        val clientRatio = RandomFunction.getSkillSuccessChance(low, high, level)
        return hostRatio < clientRatio
    }
}
