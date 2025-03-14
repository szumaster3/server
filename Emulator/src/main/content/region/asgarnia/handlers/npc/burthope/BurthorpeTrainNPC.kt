package content.region.asgarnia.handlers.npc.burthope

import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.npc.NPC
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import core.game.world.map.Direction
import core.game.world.map.Location
import core.game.world.map.RegionManager.getLocalNpcs
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import core.tools.RandomFunction

@Initializable
class BurthorpeTrainNPC : AbstractNPC {
    private var delay: Long = 0

    constructor() : super(0, null)

    private constructor(id: Int, location: Location) : super(id, location, id == 1061) {
        super.setDirection(Direction.EAST)
    }

    override fun init() {
        super.init()
        if (id == 1063) {
            faceLocation(getLocation().transform(2, 0, 0))
        }
        if (id == 1066 || id == 1067 || id == 1068) {
            faceLocation(Location.create(2893, 3532, 0))
        }
        if (id == 1064) {
            faceLocation(getLocation().transform(0, 1, 0))
        }
        if (id == 1062) {
            faceLocation(Location.create(2893, 3539, 0))
        }
    }

    override fun construct(
        id: Int,
        location: Location,
        vararg objects: Any,
    ): AbstractNPC {
        return BurthorpeTrainNPC(id, location)
    }

    override fun tick() {
        if (delay < System.currentTimeMillis()) {
            action()
        }
        super.tick()
    }

    override fun getWalkRadius(): Int {
        return if (id == 1061) 6 else 12
    }

    override fun getIds(): IntArray {
        return IDS
    }

    private fun action() {
        when (id) {
            1061 -> {
                sendChat(MESSAGES[RandomFunction.random(MESSAGES.size)])
                delay = System.currentTimeMillis() + 9000 + RandomFunction.random(1000, 5000)
                val soldiers = getLocalNpcs(this)
                var soldier: NPC? = null
                while (soldier == null) {
                    soldier = soldiers[RandomFunction.random(soldiers.size)]
                    if (soldier.id != 1063) {
                        soldier = null
                    }
                }
                val sol: NPC = soldier
                Pulser.submit(
                    object : Pulse(1) {
                        val sold: NPC = sol

                        override fun pulse(): Boolean {
                            sold.sendChat("Yes, sir!")
                            return true
                        }
                    },
                )
            }

            1063 -> {
                faceLocation(getLocation().transform(2, 0, 0))
                animate(if (RandomFunction.random(2) == 1) PUNCH else KICK)
                delay = System.currentTimeMillis() + 3000 + RandomFunction.random(1000, 3000)
            }

            1066, 1067, 1068 -> {
                animate(EAT)
                delay = System.currentTimeMillis() + 3000 + RandomFunction.random(1000, 2000)
            }

            1062 -> {
                val rand = RandomFunction.random(5)
                val animation =
                    if (rand == 1) {
                        PUNCH
                    } else if (rand == 2) {
                        KICK
                    } else {
                        DEFEND
                    }
                animate(animation)
                delay = System.currentTimeMillis() + 3500
                faceLocation(getLocation().transform(0, -1, 0))
                Pulser.submit(
                    object : Pulse(2) {
                        override fun pulse(): Boolean {
                            val soldiers = getLocalNpcs(this@BurthorpeTrainNPC, 12)
                            for (n in soldiers) {
                                if (n.id != 1064) {
                                    continue
                                }
                                n.animate(animation)
                            }
                            return true
                        }
                    },
                )
            }
        }
    }

    companion object {
        private val IDS = intArrayOf(1063, 1061, 1066, 1067, 1068, 1062, 1064, 1073, 1074, 1076, 1077)
        private val PUNCH = Animation(422)
        private val KICK = Animation(423)
        private val DEFEND = Animation(424)
        private val EAT = Animation(1145)
        private val MESSAGES =
            arrayOf(
                "Good work soldier!",
                "Push it!",
                "Work it!",
                "The dummy is the enemy. Kill it!",
                "Put your back into it soldier!",
                "You're not out for a sunday stroll soldier!",
                "My daughter can hit harder than that!",
                "I want to see you sweat!",
                "Keep it up soldier!",
            )
    }
}
