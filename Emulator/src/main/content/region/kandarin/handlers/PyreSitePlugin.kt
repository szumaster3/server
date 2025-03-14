package content.region.kandarin.handlers

import content.data.items.SkillingTool
import content.global.skill.firemaking.Log
import core.cache.def.impl.SceneryDefinition
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.combat.CombatStyle
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.drop.DropFrequency
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.ChanceItem
import core.game.node.item.Item
import core.game.node.scenery.Scenery
import core.game.node.scenery.SceneryBuilder
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.plugin.ClassScanner
import core.plugin.Initializable
import core.plugin.Plugin
import core.tools.RandomFunction
import org.rs.consts.Items
import org.rs.consts.NPCs

@Initializable
class PyreSitePlugin : OptionHandler() {
    @Throws(Throwable::class)
    override fun newInstance(arg: Any?): Plugin<Any> {
        SceneryDefinition.forId(25286).handlers["option:construct"] = this
        ClassScanner.definePlugin(FerociousBarbarianNPC())
        return this
    }

    override fun handle(
        player: Player,
        node: Node,
        option: String,
    ): Boolean {
        for (location in USED_LOCATIONS) {
            if (location.withinDistance(node.location, 3)) {
                player.sendMessage("This pyre site is in use currently.")
                return true
            }
        }

        if (!player.inventory.containsItem(CHEWED_BONES) && !player.inventory.containsItem(MANGLED_BONES)) {
            player.sendMessage("You need chewed bones or mangled bones in order to do this.")
            return true
        }

        if (!player.inventory.contains(590, 1)) {
            player.sendMessage("You need a tinderbox in order to do this.")
            return true
        }

        val tool = SkillingTool.getHatchet(player)
        if (tool == null) {
            player.sendMessage("You need an axe in order to do this.")
            return true
        }

        val type =
            LogType.getType(player) ?: run {
                player.sendMessage("You don't have any logs.")
                return true
            }

        if (player.getAttribute("barb", null) != null && (player.getAttribute("barb") as NPC).isActive) {
            player.sendMessage("You must defeat the barbarian spirit first.")
            return true
        }

        player.setAttribute("logType", type)
        ritual(player, node as Scenery)
        return true
    }

    private fun ritual(
        player: Player,
        scenery: Scenery,
    ) {
        player.lock()
        USED_LOCATIONS.add(scenery.location)
        player.faceLocation(scenery.location)
        GameWorld.Pulser.submit(getPulse(player, scenery))
    }

    private fun getPulse(
        player: Player,
        scenery: Scenery,
    ): Pulse {
        val logType = player.getAttribute("logType", LogType.NORMAL)
        val tool = SkillingTool.getHatchet(player)
        val bones =
            if (player.inventory.containsItem(CHEWED_BONES)) {
                player.inventory.getItem(CHEWED_BONES)
            } else {
                player.inventory.getItem(
                    MANGLED_BONES,
                )
            }

        return object : Pulse(1, player) {
            var count = 0
            var objectId = 25288

            override fun pulse(): Boolean {
                if ((count % 6 == 0 || count == 0) && count <= 10 && objectId <= 25291) {
                    player.animate(getAnimation(tool))
                    player.faceLocation(scenery.location)
                }
                if (count % 4 == 0) {
                    if (objectId == 25291) {
                        if (player.inventory.remove(Item(logType.log.logId), bones)) {
                            player.animator.reset()
                            player.skills.addExperience(Skills.CRAFTING, logType.experiences[0])
                            player.skills.addExperience(Skills.FIREMAKING, logType.experiences[1])

                            if (bones.id == CHEWED_BONES.id) {
                                player.inventory.add(getRandomItem(player), player)
                                player.sendMessages(
                                    "The ancient barbarian is laid to rest. Your future prayer training is blessed,",
                                    "as his spirit ascends to a glorious afterlife. Spirits drop an object into your",
                                    "pack.",
                                )
                            } else {
                                val barb = NPC.create(752, scenery.location.transform(scenery.direction, 1))
                                (barb as FerociousBarbarianNPC).target = player
                                barb.init()
                                barb.moveStep()
                                player.setAttribute("barb", barb)
                                player.unlock()
                            }
                        }
                    }

                    if (objectId == 25295) {
                        return true
                    }

                    replace(objectId++, scenery, player)
                }
                count++
                return false
            }

            override fun stop() {
                super.stop()
                player.unlock()
                replace(25286, scenery, player)
                USED_LOCATIONS.remove(scenery.location)
            }
        }
    }

    private fun replace(
        newId: Int,
        ship: Scenery,
        player: Player,
    ) {
        val newShip =
            Scenery(
                newId,
                getLocation(newId, ship),
                10,
                if (ship.location.x == 2503) 4 else 1,
            )
        SceneryBuilder.add(newShip)
    }

    private fun getLocation(
        newId: Int,
        ship: Scenery,
    ): Location {
        var location = ship.location.transform(ship.direction, -2)
        when {
            ship.location.x == 2507 || ship.location.x == 2519 -> location = location.transform(-1, 0, 0)
            ship.location.x == 2503 -> location = location.transform(-2, -1, 0)
        }
        return location
    }

    private fun getRandomItem(player: Player): Item {
        if (RandomFunction.random(250) == 10) {
            return DFH
        }
        return RandomFunction.getChanceItem(REWARDS)
    }

    override fun getDestination(
        node: Node,
        n: Node,
    ): Location? {
        return if (n is Scenery) {
            n.location.transform(n.direction, 1)
        } else {
            null
        }
    }

    fun getAnimation(tool: SkillingTool?): Animation? {
        return when (tool) {
            SkillingTool.BRONZE_AXE -> Animation.create(3291)
            SkillingTool.IRON_AXE -> Animation.create(3290)
            SkillingTool.STEEL_AXE -> Animation.create(3289)
            SkillingTool.BLACK_AXE -> Animation.create(3288)
            SkillingTool.MITHRIL_AXE -> Animation.create(3287)
            SkillingTool.ADAMANT_AXE -> Animation.create(3286)
            SkillingTool.RUNE_AXE -> Animation.create(3285)
            SkillingTool.DRAGON_AXE -> Animation.create(3292)
            else -> null
        }
    }

    enum class LogType(
        val log: Log,
        val level: Int,
        val experiences: DoubleArray,
        val enhancedExp: Int,
    ) {
        NORMAL(Log.NORMAL, 11, doubleArrayOf(10.0, 40.0), 1),
        ACHEY(Log.ACHEY, 11, doubleArrayOf(10.0, 40.0), 1),
        OAK(Log.OAK, 25, doubleArrayOf(15.0, 60.0), 2),
        WILLOW(Log.WILLOW, 40, doubleArrayOf(22.5, 90.0), 2),
        TEAK(Log.TEAK, 45, doubleArrayOf(26.2, 105.0), 3),
        ARCTIC_PINE(Log.ARCTIC_PINE, 52, doubleArrayOf(31.2, 125.0), 3),
        MAPLE(Log.MAPLE, 55, doubleArrayOf(35.0, 140.0), 3),
        MAHOGANY(Log.MAHOGANY, 60, doubleArrayOf(39.5, 158.0), 4),
        EUCALYPTUS(Log.EUCALYPTUS, 65, doubleArrayOf(43.7, 175.0), 4),
        YEW(Log.YEW, 70, doubleArrayOf(48.7, 195.0), 5),
        MAGIC(Log.MAGIC, 85, doubleArrayOf(60.0, 250.0), 5),
        ;

        companion object {
            fun getType(player: Player): LogType? {
                for (type in values()) {
                    if (player.inventory.containsItem(Item(type.log.logId))) {
                        return type
                    }
                }
                return null
            }
        }
    }

    inner class FerociousBarbarianNPC
        @JvmOverloads
        constructor(
            id: Int = -1,
            location: Location? = null,
        ) : AbstractNPC(id, location) {
            var target: Player? = null

            init {
                this.isRespawn = false
                this.isAggressive = true
            }

            override fun handleTickActions() {
                if (target == null) {
                    return
                }
                if (!target!!.isActive || !target!!.location.withinDistance(getLocation())) {
                    clear()
                }
                if (!properties.combatPulse.isAttacking) {
                    properties.combatPulse.attack(target)
                }
            }

            override fun isAttackable(
                entity: Entity,
                style: CombatStyle,
                message: Boolean,
            ): Boolean {
                if (entity is Player && entity !== target) {
                    if (message) {
                        entity.packetDispatch.sendMessage("It's not after you.")
                    }
                    return false
                }
                return super.isAttackable(entity, style, message)
            }

            override fun finalizeDeath(killer: Entity) {
                super.finalizeDeath(killer)
                if (killer is Player && killer == target) {
                    target!!.removeAttribute("barb")
                }
            }

            override fun construct(
                id: Int,
                location: Location,
                vararg objects: Any,
            ): AbstractNPC {
                return FerociousBarbarianNPC(id, location)
            }

            override fun getIds(): IntArray {
                return intArrayOf(NPCs.FEROCIOUS_BARBARIAN_SPIRIT_752)
            }
        }

    companion object {
        private val REWARDS =
            arrayOf(
                ChanceItem(560, 8, 15, DropFrequency.COMMON),
                ChanceItem(565, 4, 7, DropFrequency.COMMON),
                ChanceItem(1601, 2, 2, DropFrequency.UNCOMMON),
                ChanceItem(532, 10, 10, DropFrequency.RARE),
                ChanceItem(100, 2, 2, DropFrequency.COMMON),
                ChanceItem(9145, 5, 5, DropFrequency.COMMON),
                ChanceItem(9144, 10, 10, DropFrequency.UNCOMMON),
                ChanceItem(892, 10, 10, DropFrequency.COMMON),
                ChanceItem(867, 20, 20, DropFrequency.UNCOMMON),
                ChanceItem(816, 20, 20, DropFrequency.COMMON),
                ChanceItem(9419, 1, 1, DropFrequency.COMMON),
            )
        private val CHEWED_BONES = Item(Items.CHEWED_BONES_11338)
        private val MANGLED_BONES = Item(Items.MANGLED_BONES_11337)
        private val DFH = Item(Items.DRAGON_FULL_HELM_11335)
        private val USED_LOCATIONS = ArrayList<Location>(20)
    }
}
