package content.global.skill.hunter.bnet

import core.api.animate
import core.api.getStatLevel
import core.api.playAudio
import core.api.quest.hasRequirement
import core.api.sendMessage
import core.game.container.impl.EquipmentContainer
import core.game.node.entity.combat.DeathTask
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.skill.SkillPulse
import core.game.node.entity.skill.Skills
import core.game.world.GameWorld
import core.game.world.update.flag.context.Animation
import core.tools.RandomFunction
import core.tools.StringUtils
import org.rs.consts.NPCs
import org.rs.consts.Quests
import org.rs.consts.Sounds
import kotlin.random.Random

class BNetPulse(
    player: Player?,
    node: NPC?,
    private val type: BNetNode,
) : SkillPulse<NPC?>(player, node) {
    private var success = false

    private var ticks = 0

    fun updateLumbridgeImplingTask(player: Player): Boolean {
        return player.zoneMonitor.isInZone("puro puro")
    }

    init {
        this.resetAnimation = false
    }

    override fun checkRequirements(): Boolean {
        if (getStatLevel(player, Skills.HUNTER) < type.level) {
            sendMessage(player, "You need a Hunter level of at least " + type.level + " in order to do that.")
            return false
        }
        if (!hasRequirement(player, Quests.ROCKING_OUT, false) &&
            (node!!.asNpc().id == NPCs.PIRATE_IMPLING_7845 || node!!.asNpc().id == NPCs.PIRATE_IMPLING_7846)
        ) {
            sendMessage(player, "You can't catch pirate implings until you have completed Rocking Out.")
            return false
        }
        if (type.hasWeapon(player)) {
            sendMessage(player, "Your hands need to be free.")
            return false
        } else if (!type.hasNet(player)) {
            sendMessage(
                player,
                "You need to be wielding a butterfly net to catch " +
                    (if (type is ImplingNode) "implings" else "butterflies") +
                    ".",
            )
            return false
        } else if (!type.hasJar(player)) {
            sendMessage(
                player,
                "You need to have a" + (if (StringUtils.isPlusN(type.jar!!.name)) "n" else "") + " " +
                    type.jar!!.name.lowercase() +
                    ".",
            )
            return false
        }
        return !node!!.isInvisible && !DeathTask.isDead(node)
    }

    override fun animate() {
        if (ticks < 1) {
            animate(player, ANIMATION)
            playAudio(player, Sounds.HUNTING_BUTTERFLYNET_2623)
        }
    }

    override fun reward(): Boolean {
        if (node!!.isInvisible || DeathTask.isDead(node)) {
            return true
        }
        if (++ticks % 2 != 0) {
            return false
        }
        if (node!!.getAttribute("dead", 0) > GameWorld.ticks) {
            sendMessage(player, "Ooops! It's gone.")
            return true
        }
        if ((isSuccessful.also { success = it })) {
            node!!.finalizeDeath(player)
            type.reward(player, node)
            node!!.setAttribute("dead", GameWorld.ticks + 10)
            if (type == BNetTypes.ECLECTIC_IMPLING.node || type == BNetTypes.ESSENCE_IMPLING.node) {
                updateLumbridgeImplingTask(player)
            }
        } else {
            node!!.moveStep()
        }
        return true
    }

    override fun message(type: Int) {
        if (type == 0) {
            node!!.setAttribute("looting", GameWorld.ticks + (ANIMATION.duration + 1))
            player.lock(ANIMATION.duration)
        }
        this.type.message(player, type, success)
    }

    private val isSuccessful: Boolean
        get() {
            var huntingLevel = player.getSkills().getLevel(Skills.HUNTER)
            val level = type.level
            if (type.hasNet(player)) {
                val net = player.equipment[EquipmentContainer.SLOT_WEAPON]
                if (net != null && net.id == 11259) {
                    huntingLevel += 5
                }
            } else {
                huntingLevel = (huntingLevel * 0.5).toInt()
            }
            val currentLevel = RandomFunction.random(huntingLevel) + 1
            val ratio = currentLevel.toDouble() / (Random.nextInt(level + 5) + 1)
            return Math.round(ratio * huntingLevel) >= level
        }

    companion object {
        private val ANIMATION = Animation(6999)
    }
}
