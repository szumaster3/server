package content.global.skill.hunter

import core.api.lock
import core.api.playAudio
import core.game.node.entity.player.Player
import core.game.node.entity.skill.SkillPulse
import core.game.node.entity.skill.Skills
import core.game.node.scenery.Scenery
import org.rs.consts.Sounds

class TrapDismantlePulse(
    player: Player?,
    node: Scenery?,
    wrapper: TrapWrapper,
) : SkillPulse<Scenery?>(player, node) {
    val wrapper: TrapWrapper = wrapper
    val trap: Traps = wrapper.type

    private var ticks = 0
    private val instance: HunterManager = HunterManager.getInstance(player)

    init {
        if (checkRequirements()) {
            when (trap) {
                Traps.BIRD_SNARE -> {
                    lock(player!!, 5)
                    playAudio(player, Sounds.HUNTING_DISMANTLE_2632, 50)
                }

                Traps.BOX_TRAP -> {
                    lock(player!!, 4)
                    playAudio(player, Sounds.HUNTING_DISMANTLE_2632, 50)
                }

                Traps.NET_TRAP -> {
                    lock(player!!, 5)
                    playAudio(player, Sounds.HUNTING_DISMANTLE_2632, 20)
                }

                Traps.RABBIT_SNARE, Traps.DEAD_FALL -> {
                    lock(player!!, 4)
                    playAudio(player, Sounds.HUNTING_DISMANTLE_2632, 80)
                }

                Traps.IMP_BOX -> {
                }
            }
        }
    }

    override fun checkRequirements(): Boolean {
        if (!instance.isOwner(node)) {
            player.sendMessage("This isn't your trap!")
            return false
        }
        val itemCount = wrapper.items.size + (if (wrapper.type.settings.isObjectTrap) 0 else 1)
        val difference = itemCount - player.inventory.freeSlots()
        if (player.inventory.freeSlots() < itemCount) {
            player.packetDispatch.sendMessage(
                "You don't have enough inventory space. You need " + difference + " more free slot" +
                    (if (difference > 1) "s" else "") +
                    ".",
            )
            return false
        }
        return true
    }

    override fun animate() {
        if (ticks < 1) {
            player.animator.forceAnimation(trap.settings.dismantleAnimation)
        }
    }

    override fun reward(): Boolean {
        if (++ticks % (trap.settings.dismantleAnimation.definition.durationTicks + 1) != 0) {
            return false
        }
        if (wrapper.type.settings.clear(wrapper, 1)) {
            instance.deregister(wrapper)
            if (wrapper.isCaught) {
                player.getSkills().addExperience(Skills.HUNTER, wrapper.reward.experience, true)
            }
            player.packetDispatch.sendMessage("You dismantle the trap.")
        }
        return true
    }

    override fun message(type: Int) {
        when (type) {
            0 -> {
                val ticks = wrapper.ticks + (wrapper.type.settings.dismantleAnimation.definition.durationTicks) + 1
                wrapper.ticks = ticks
                wrapper.busyTicks = ticks
            }
        }
    }
}
