package content.global.skill.summoning.familiar.npc

import content.global.skill.summoning.familiar.Familiar
import content.global.skill.summoning.familiar.FamiliarSpecial
import content.global.skill.summoning.familiar.Forager
import core.api.sendMessages
import core.game.node.entity.player.Player
import core.game.node.entity.skill.SkillBonus
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.node.scenery.Scenery
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import core.game.world.map.Direction
import core.game.world.map.path.Pathfinder
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import core.tools.RandomFunction
import org.rs.consts.Items
import org.rs.consts.NPCs
import kotlin.math.floor

@Initializable
class BeaverNPC @JvmOverloads constructor(owner: Player? = null, id: Int = NPCs.BEAVER_6808) :
    Forager(owner, id, 2700, Items.BEAVER_POUCH_12021, 6, *ITEMS) {
    private var multiChop = false

    init {
        boosts.add(SkillBonus(Skills.WOODCUTTING, 2.0))
    }

    override fun construct(owner: Player, id: Int): Familiar {
        return BeaverNPC(owner, id)
    }

    override fun specialMove(special: FamiliarSpecial): Boolean {
        val `object` = special.node as Scenery
        if (!isTree(`object`.name)) {
            sendMessages(
                owner,
                "This scroll only works on naturally growing, oak, willow, arctic pine",
                "teak, mahogany, maple, yew, and magic trees."
            )
            return false
        }
        if (owner.inventory.freeSlots() == 0) {
            return false
        }
        if (`object`.location.getDistance(getLocation()) > 5) {
            sendMessages(
                owner, "The beaver is a little too far from the tree for the scroll to work - stand", "closer."
            )
            return false
        }
        val dir = Direction.getLogicalDirection(getLocation(), `object`.location)
        Pathfinder.find(getLocation(), `object`.location.transform(dir)).walk(this)
        val ticks = 2 + floor(owner.location.getDistance(`object`.location.transform(dir)) * 0.5).toInt()
        owner.lock(ticks)
        multiChop = true
        pulseManager.clear()
        Pulser.submit(object : Pulse(ticks, owner, this) {
            override fun pulse(): Boolean {
                lock(11)
                owner.lock(11)
                faceLocation(`object`.location)
                animate(Animation.create(7722))
                Pulser.submit(object : Pulse(1, owner, this@BeaverNPC) {
                    var counter: Int = 0
                    var recieved: Boolean = false

                    override fun pulse(): Boolean {
                        when (++counter) {
                            11 -> {
                                if (!recieved) {
                                    owner.inventory.add(ITEMS[RandomFunction.random(ITEMS.size)], owner)
                                }
                                multiChop = false
                                return true
                            }

                            else -> if (counter > 3) {
                                if (RandomFunction.random(12) < 4) {
                                    owner.inventory.add(ITEMS[RandomFunction.random(ITEMS.size)], owner)
                                    recieved = true
                                }
                            }
                        }
                        return false
                    }
                })
                return true
            }
        })
        return true
    }

    override fun startFollowing() {
        if (multiChop) {
            return
        }
        super.startFollowing()
    }

    private fun isTree(name: String): Boolean {
        for (s in TREE_NAMES) {
            if (s == name) {
                return true
            }
        }
        return false
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.BEAVER_6808)
    }

    companion object {
        private val ITEMS = arrayOf(
            Item(Items.LOGS_1511),
            Item(Items.ACHEY_TREE_LOGS_2862),
            Item(Items.OAK_LOGS_1521),
            Item(Items.WILLOW_LOGS_1519),
            Item(Items.TEAK_LOGS_6333),
            Item(Items.ARCTIC_PINE_LOGS_10810),
            Item(Items.MAPLE_LOGS_1517),
            Item(Items.MAHOGANY_LOGS_6332),
            Item(Items.EUCALYPTUS_LOGS_12581),
            Item(Items.PLANK_960),
            Item(Items.OAK_PLANK_8778)
        )

        private val TREE_NAMES = arrayOf(
            "Tree", "Oak", "Hollow", "Willow", "Arctic pine", "Eucalyptus", "Maple", "Yew", "Magic", "Cursed magic"
        )
    }
}
