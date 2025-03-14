package content.global.activity.shootingstar

import content.data.items.SkillingTool
import core.api.*
import core.game.node.entity.player.Player
import core.game.node.entity.skill.SkillPulse
import core.game.node.entity.skill.Skills
import core.game.node.scenery.Scenery
import core.game.world.GameWorld

class ShootingStarMiningPulse(
    player: Player?,
    node: Scenery?,
    val star: ShootingStar,
) : SkillPulse<Scenery?>(player, node) {
    private var ticks = 0

    override fun start() {
        if (!star.isSpawned) return

        if (!player.isArtificial) {
            star.notifyNewPlayer(player)
        }
        super.start()
    }

    override fun stop() {
        super.stop()

        if (!player.isArtificial) {
            star.notifyPlayerLeave(player)
        }
    }

    override fun checkRequirements(): Boolean {
        tool = SkillingTool.getPickaxe(player)
        if (!star.starScenery.isActive || !star.isSpawned) {
            return false
        }

        if (!star.isDiscovered && !player.isArtificial) {
            val bonusXp = 75 * getStatLevel(player, Skills.MINING)
            player.incrementAttribute("/save:shooting-star:bonus-xp", bonusXp)
            sendNews(player.username + " is the discoverer of the crashed star near " + star.location + "!")
            sendMessage(
                player,
                "You have ${
                    player.skills.experienceMultiplier *
                        getAttribute(
                            player,
                            "shooting-star:bonus-xp",
                            0,
                        ).toDouble()
                } bonus xp towards mining stardust.",
            )
            ShootingStarPlugin.submitScoreBoard(player)
            star.isDiscovered = true
            return getStatLevel(player, Skills.MINING) >= star.miningLevel
        }

        if (getStatLevel(player, Skills.MINING) < star.miningLevel) {
            sendDialogue(
                player,
                "You need a Mining level of at least " + star.miningLevel + " in order to mine this layer.",
            )
            return false
        }
        if (tool == null) {
            sendMessage(player, "You do not have a pickaxe to use.")
            return false
        }
        if (freeSlots(player) < 1 &&
            !inInventory(
                player,
                ShootingStarPlugin.STAR_DUST,
                1,
            )
        ) {
            sendDialogue(player, "Your inventory is too full to hold any more stardust.")
            return false
        }
        return true
    }

    override fun animate() {
        animate(player, tool.animation)
    }

    override fun reward(): Boolean {
        if (++ticks % 4 != 0) {
            return false
        }
        if (!checkReward()) {
            return false
        }
        if (GameWorld.settings?.isDevMode == true) {
            star.dustLeft = 1
        }

        val bonusXp = player.getAttribute("shooting-star:bonus-xp", 0).toDouble()
        var xp = star.level.exp.toDouble()
        if (bonusXp > 0) {
            val delta = Math.min(bonusXp, xp)
            player.incrementAttribute("/save:shooting-star:bonus-xp", (-delta).toInt())
            xp += delta
            if (getAttribute(player, "shooting-star:bonus-xp", 0) <= 0) {
                sendMessage(player, "You have obtained all of your bonus xp from the star.")
            }
        }

        rewardXP(player, Skills.MINING, xp)
        if (ShootingStarPlugin.getStarDust(player) < 200) {
            addItem(player, ShootingStarPlugin.STAR_DUST, 1)
        }

        star.decDust()
        return false
    }

    fun rollBlueprint(player: Player) {
        val chance =
            when (star.level) {
                ShootingStarType.LEVEL_9 -> 250
                ShootingStarType.LEVEL_8 -> 500
                ShootingStarType.LEVEL_7 -> 750
                ShootingStarType.LEVEL_6 -> 1000
                ShootingStarType.LEVEL_5 -> 2000
                ShootingStarType.LEVEL_4 -> 3000
                ShootingStarType.LEVEL_3 -> 4000
                ShootingStarType.LEVEL_2 -> 5000
                ShootingStarType.LEVEL_1 -> 10000
            }
    }

    override fun message(type: Int) {
        when (type) {
            0 -> sendMessage(player, "You swing your pickaxe at the rock.")
        }
    }

    private fun checkReward(): Boolean {
        val skill = Skills.MINING
        val level = 1 + player.skills.getLevel(skill) + player.familiarManager.getBoost(skill)
        val hostRatio: Double = Math.random() * (100.0 * star.level.rate)
        val clientRatio: Double = Math.random() * ((level - star.miningLevel) * (1.0 + tool.ratio))
        return hostRatio < clientRatio
    }
}
