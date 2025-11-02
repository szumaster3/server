package content.global.skill.gathering.woodcutting

import content.data.items.SkillingTool
import content.data.items.SkillingTool.Companion.getAxe
import content.global.skill.farming.FarmingPatch.Companion.forObject
import core.api.*
import core.cache.def.impl.ItemDefinition.forId
import core.game.container.impl.EquipmentContainer
import core.game.dialogue.FaceAnim
import core.game.event.ResourceProducedEvent
import core.game.node.entity.impl.Animator
import core.game.node.entity.impl.Projectile
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.entity.skill.Skills
import core.game.node.scenery.Scenery
import core.game.node.scenery.SceneryBuilder
import core.game.system.command.sets.STATS_BASE
import core.game.system.command.sets.STATS_LOGS
import core.game.system.task.Pulse
import core.game.world.map.Location
import core.game.world.map.RegionManager.getLocalPlayers
import core.game.world.update.flag.context.Animation
import core.tools.RandomFunction
import shared.consts.Items
import shared.consts.NPCs
import shared.consts.Sounds
import java.util.*
import java.util.stream.Collectors

/**
 * Woodcutting skill pulse
 *
 * @author ceik
 */
class WoodcuttingPulse(private val player: Player, private val node: Scenery) : Pulse(1, player, node) {

    private val woodcuttingSounds = intArrayOf(
        Sounds.WOODCUTTING_HIT_3038,
        Sounds.WOODCUTTING_HIT_3039,
        Sounds.WOODCUTTING_HIT_3040,
        Sounds.WOODCUTTING_HIT_3041,
        Sounds.WOODCUTTING_HIT_3042
    )

    private var resource: WoodcuttingNode? = null
    private var ticks = 0
    private var resetAnimation: Boolean = true

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
            player.animate(Animation(-1, Animator.Priority.HIGH))
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
        if (getAxe(player) == null) {
            sendMessage(player, "You do not have an axe to use.")
            return false
        }
        if (freeSlots(player) < 1) {
            sendDialogue(
                player,
                "Your inventory is too full to hold any more " + forId(resource!!.reward).name.lowercase(Locale.getDefault()) + "."
            )
            return false
        }
        return true
    }

    fun animate() {
        if (!player.animator.isAnimating) {
            player.animate(SkillingTool.getAxe(player)?.let { Animation(it.animation) })

            val playersAroundMe = getLocalPlayers(player, 2)
                .stream()
                .filter { p: Player -> p.username != player.username }
                .collect(Collectors.toList())

            val soundIndex = RandomFunction.random(0, woodcuttingSounds.size)

            for (p in playersAroundMe) {
                playAudio(p!!, woodcuttingSounds[soundIndex])
            }
        }
    }


    fun reward(): Boolean {
        if (++ticks % 4 != 0) {
            return false
        }
        if (node.id == shared.consts.Scenery.TREE_10041) {
            sendNPCDialogue(
                player,
                NPCs.BANK_GUARD_2574,
                if (RandomFunction.random(2) == 1) "You'll blow my cover! I'm meant to be hidden!" else "Will you stop that?",
                FaceAnim.FURIOUS
            )
            return true
        }
        if (!checkReward(getAxe(player)!!)) {
            return false
        }

        // 20% chance to auto burn logs when using "inferno adze" item
        if (getAxe(player)!!.id == Items.INFERNO_ADZE_13661 && RandomFunction.random(100) < 25) {
            sendMessage(player, "You chop some logs. The heat of the inferno adze incinerates them.")
            Projectile.create(player, null, 1776, 35, 30, 20, 25)
                .transform(player, Location(player.location.x + 2, player.location.y), true, 25, 25).send()
            rewardXP(player, Skills.WOODCUTTING, resource!!.experience)
            rewardXP(player, Skills.FIREMAKING, resource!!.experience)
            return rollDepletion()
        }

        // Actual reward calculations
        var reward = resource!!.reward
        var rewardAmount = 0
        if (reward > 0) {
            reward = calculateReward(reward) // Calculate rewards
            rewardAmount = calculateRewardAmount(reward) // Calculate amount

            // SkillingPets.checkPetDrop(player, SkillingPets.BEAVER); // roll for pet

            // Add experience
            val experience = calculateExperience(resource!!.reward, rewardAmount)

            rewardXP(player, Skills.WOODCUTTING, experience)

            // Send the message for the resource reward, and in the case of the dramen tree, authentically abort the chopping action
            if (resource == WoodcuttingNode.DRAMEN_TREE) {
                sendMessage(player, "You cut a branch from the Dramen tree.")
                stop()
            } else {
                sendMessage(player, "You get some " + forId(reward).name.lowercase(Locale.getDefault()) + ".")
            }
            // Reward
            addItem(player, reward, rewardAmount)
            player.dispatch(ResourceProducedEvent(reward, rewardAmount, node, -1))
            var cutLogs = player.getAttribute("$STATS_BASE:$STATS_LOGS", 0)
            player.setAttribute("/save:$STATS_BASE:$STATS_LOGS", ++cutLogs)

            // Calculate bonus bird nest for mining.
            // int chance = 282;
            // if (RandomFunction.random(chance) == chance / 2) {
            //     if(SkillcapePerks.isActive(SkillcapePerks.NEST_HUNTER,player)){
            //         if(!player.getInventory().add(BirdNest.getRandomNest(false).getNest())){
            //             BirdNest.drop(player);
            //         }
            //     } else {
            //         BirdNest.drop(player);
            //     }
            // }
        }

        return rollDepletion()
    }

    private fun rollDepletion(): Boolean {
        // transform to depleted version
        // OSRS and RS3 Wikis both agree: All trees present in 2009 are a 1/8 fell chance, aside from normal trees/dead trees which are 100%
        // OSRS: https://oldschool.runescape.wiki/w/Woodcutting scroll down to the mechanics section
        // RS3 : https://runescape.wiki/w/Woodcutting scroll down to the mechanics section, and expand the tree felling chances table
        if (resource!!.respawnRate > 0) {
            if (RandomFunction.roll(8) || resource!!.identifier.toInt() == 1 || resource!!.identifier.toInt() == 2 || resource!!.identifier.toInt() == 3 || resource!!.identifier.toInt() == 6) {
                if (resource!!.isFarming) {
                    val fPatch = forObject(node.asScenery())
                    if (fPatch != null) {
                        val patch = fPatch.getPatchFor(player, true)
                        patch.setCurrentState(patch.getCurrentState() + 1)
                    }
                    return true
                }
                if (resource!!.emptyId > -1) {
                    SceneryBuilder.replace(node, node.transform(resource!!.emptyId), resource!!.respawnDuration)
                } else {
                    SceneryBuilder.replace(node, node.transform(0), resource!!.respawnDuration)
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

        // 3239: Hollow tree (bark) 10% chance of obtaining
        if (reward == Items.BARK_3239 && RandomFunction.random(100) >= 10) {
            amount = 0
        }

        // Seers village medium reward - extra normal log while in seer's village
        if (reward == Items.LOGS_1511 && player.achievementDiaryManager.getDiary(DiaryType.SEERS_VILLAGE)
                .isComplete(1) && player.viewport.region!!.id == 10806
        ) {
            amount = 2
        }

        return amount
    }

    private fun calculateExperience(reward: Int, amount: Int): Double {
        var amount = amount
        var experience = resource!!.experience

        if (player.location.regionId == 10300) {
            return 1.0
        }

        // Bark
        if (reward == Items.BARK_3239) {
            // If we receive the item, give the full experience points otherwise give the base amount
            if (amount >= 1) {
                experience = 275.2
            } else {
                amount = 1
            }
        }

        // Seers village medium reward - extra 10% xp from maples while wearing headband
        if (reward == Items.MAPLE_LOGS_1517 && player.achievementDiaryManager.getDiary(DiaryType.SEERS_VILLAGE)
                .isComplete(1)
            && player.equipment[EquipmentContainer.SLOT_HAT] != null && player.equipment[EquipmentContainer.SLOT_HAT].id == 14631
        ) {
            experience *= 1.10
        }

        return experience * amount
    }

    private fun calculateReward(reward: Int): Int {
        return reward
    }

    /**
     * Checks if the player gets rewarded.
     *
     * @return `True` if so.
     */
    private fun checkReward(tool: SkillingTool): Boolean {
        val skill = Skills.WOODCUTTING
        val level = player.getSkills().getLevel(skill) + player.familiarManager.getBoost(skill)
        val hostRatio = RandomFunction.randomDouble(100.0)
        val lowMod = if (tool == SkillingTool.BLACK_AXE) resource!!.tierModLow / 2 else resource!!.tierModLow
        val low = resource!!.baseLow + (tool.ordinal * lowMod)
        val highMod = if (tool == SkillingTool.BLACK_AXE) resource!!.tierModHigh / 2 else resource!!.tierModHigh
        val high = resource!!.baseHigh + (tool.ordinal * highMod)
        val clientRatio = RandomFunction.getSkillSuccessChance(low, high, level)
        return hostRatio < clientRatio
    }
}
