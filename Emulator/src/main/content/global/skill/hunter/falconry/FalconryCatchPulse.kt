package content.global.skill.hunter.falconry

import core.api.*
import core.game.container.impl.EquipmentContainer
import core.game.node.entity.impl.Projectile
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.skill.SkillPulse
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import core.game.world.map.Location
import core.game.world.repository.Repository.findNPC
import core.tools.RandomFunction
import org.rs.consts.Graphics
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Sounds

class FalconryCatchPulse(
    player: Player?,
    node: NPC,
    private val falconCatch: FalconCatch,
) : SkillPulse<NPC?>(player, node) {
    private val originalLocation: Location = node.location
    private var checked = false
    private var ticks = 0

    override fun start() {
        player.faceTemporary(node!!.asNpc(), 1)
        node!!.asNpc().walkingQueue.reset()
        player.walkingQueue.reset()
        super.start()
    }

    override fun checkRequirements(): Boolean {
        if (!checked) {
            checked = true
            if (node!!.asNpc().location.getDistance(player.location) > 15) {
                sendMessage(player, "You can't catch a kebbit that far away.")
                return false
            }
            if (getStatLevel(player, Skills.HUNTER) < falconCatch.level) {
                sendMessage(
                    player,
                    "You need a Hunter level of at least " + falconCatch.level + " to catch this kebbit.",
                )
                return false
            }
            if (player.equipment[EquipmentContainer.SLOT_HANDS] != null ||
                player.equipment[EquipmentContainer.SLOT_SHIELD] != null
            ) {
                sendDialogue(player, "Sorry, free your hands, weapon, and shield slot first.")
                return false
            }
            if (player.equipment[EquipmentContainer.SLOT_WEAPON] == null || !player.equipment.containsItem(FALCON)) {
                sendMessage(player, "You need a falcon to catch a kebbit.")
                return false
            }
            if (player.equipment.remove(FALCON)) {
                player.equipment.add(GLOVE, true, false)
                sendProjectile()
            }
            lock(node!!.asNpc(), distance + 1)
            lock(player, distance + 1)
        }
        return true
    }

    override fun stop() {
        super.stop()
        unlock(player)
    }

    override fun animate() {}

    override fun reward(): Boolean {
        if (++ticks % distance != 0) {
            return false
        }
        val success = success()
        sendMessage(
            player,
            if (success) "The falcon successfully swoops down and captures the kebbit." else "The falcon swoops down on the kebbit, but just misses catching it.",
        )

        if (success) {
            node!!.asNpc().finalizeDeath(player)
            val falcon = NPC.create(NPCs.GYR_FALCON_5094, node!!.asNpc().location)
            setAttribute(falcon, "falcon:owner", player.username)
            setAttribute(falcon, "falcon:catch", this.falconCatch)
            falcon.init()
            registerHintIcon(player, falcon)
            playAudio(player, Sounds.HUNTING_FALCON_SWOOP_2634, 10, 1, node!!.asNpc().location, 12)
            Pulser.submit(
                object : Pulse(100, falcon) {
                    override fun pulse(): Boolean {
                        if (!falcon.isActive) {
                            return true
                        }
                        val projectile = Projectile.create(node!!.asNpc(), findNPC(5093), 918)
                        projectile.speed = 80
                        projectile.send()
                        sendMessage(
                            player,
                            "Your falcon has left its prey. You see it heading back toward the falconer.",
                        )
                        falcon.clear()
                        return true
                    }
                },
            )
        } else {
            if (removeItem(player, GLOVE, Container.EQUIPMENT)) {
                player.equipment.add(FALCON, true, false)
            }
        }
        player.face(null)
        return true
    }

    private fun sendProjectile() {
        val projectile = Projectile.create(player, node!!.asNpc(), Graphics.FALCONRY_BIRD_FLOAT_918)
        projectile.speed = 80
        projectile.startHeight = 26
        projectile.endHeight = 1
        projectile.send()
        playAudio(player, Sounds.HUNTING_FALCON_FLY_2633)
    }

    val distance: Int
        get() = (2 + player.location.getDistance(node!!.asNpc().location) * 0.5).toInt()

    fun success(): Boolean {
        return if (originalLocation !== node!!.asNpc().location) {
            RandomFunction.random(1, 3) == 2
        } else {
            RandomFunction.getRandom(3) * player.getSkills().getLevel(Skills.HUNTER) / 3 > falconCatch.level / 2
        }
    }

    companion object {
        private val FALCON = Item(Items.FALCONERS_GLOVE_10024)
        private val GLOVE = Item(Items.FALCONERS_GLOVE_10023)
    }
}
