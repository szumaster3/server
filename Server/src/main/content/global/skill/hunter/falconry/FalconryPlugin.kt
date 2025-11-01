package content.global.skill.hunter.falconry

import core.api.*
import core.cache.def.impl.ItemDefinition
import core.cache.def.impl.NPCDefinition
import core.game.activity.ActivityPlugin
import core.game.container.impl.EquipmentContainer
import core.game.interaction.Option
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.impl.Projectile
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.HintIconManager
import core.game.node.entity.skill.SkillPulse
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import core.game.world.map.Location
import core.game.world.map.zone.ZoneBorders
import core.game.world.map.zone.ZoneRestriction
import core.game.world.repository.Repository
import core.plugin.ClassScanner
import core.plugin.Plugin
import core.tools.RandomFunction
import shared.consts.Graphics
import shared.consts.Items
import shared.consts.NPCs
import shared.consts.Sounds

class FalconryPlugin : ActivityPlugin(FALCONRY_ZONE, false, false, false), MapArea {

    companion object {
        val FALCONRY_ZONE = "falconry"

        fun removeItems(player: Player) {
            removeItem(player, Items.FALCONERS_GLOVE_10023, Container.INVENTORY)
            removeItem(player, Items.FALCONERS_GLOVE_10023, Container.EQUIPMENT)
            removeItem(player, Items.FALCONERS_GLOVE_10024, Container.INVENTORY)
            removeItem(player, Items.FALCONERS_GLOVE_10024, Container.EQUIPMENT)
        }
    }

    override fun newInstance(p: Player): ActivityPlugin = content.global.skill.hunter.falconry.FalconryPlugin()

    override fun start(player: Player, login: Boolean, vararg args: Any): Boolean {
        setAttribute(player, "/save:$FALCONRY_ZONE", true)
        return super.start(player, login, *args)
    }

    override fun leave(entity: Entity, logout: Boolean): Boolean {
        super.leave(entity, logout)
        return true
    }

    override fun interact(e: Entity, target: Node, option: Option): Boolean = false

    override fun getSpawnLocation(): Location? = null

    override fun configure() {
        register(ZoneBorders(2363, 3574, 2395, 3620))
    }

    override fun register() {
        ClassScanner.definePlugin(FalconryPlugin())
    }

    class FalconryPlugin : OptionHandler() {
        override fun newInstance(arg: Any?): Plugin<Any> {
            NPCDefinition.forId(NPCs.MATTHIAS_5093).handlers["option:quick-falconry"] = this
            NPCDefinition.forId(NPCs.GYR_FALCON_5094).handlers["option:retrieve"] = this
            for (falconCatch in FalconCatch.values()) {
                NPCDefinition.forId(falconCatch.npc).handlers["option:catch"] = this
            }
            ItemDefinition.forId(Items.FALCONERS_GLOVE_10023).handlers["equipment"] = this
            ItemDefinition.forId(Items.FALCONERS_GLOVE_10024).handlers["equipment"] = this
            return this
        }

        override fun fireEvent(
            identifier: String,
            vararg args: Any,
        ): Any {
            val player = args[0] as Player
            if (identifier == "unequip") {
                if (player.zoneMonitor.isInZone(FALCONRY_ZONE)) {
                    sendDialogue(player, "Leave the area in order to remove your falcon.")
                    return false
                } else {
                    removeItems(player)
                }
            }
            return true
        }

        override fun handle(
            player: Player,
            node: Node,
            option: String,
        ): Boolean {
            val npc = node as NPC
            when (option) {
                "quick-falconry" -> {
                    player.dialogueInterpreter.open(NPCs.MATTHIAS_5093, null, true)
                }

                "catch" -> {
                    player.face(node.asNpc())
                    player.pulseManager.run(FalconryCatchPulse(player, node, FalconCatch.forNPC(node)!!))
                }

                "retrieve" -> {
                    if (getAttribute(npc, "falcon:owner", "") != player.username) {
                        sendMessage(player, "This isn't your falcon.")
                    } else if (freeSlots(player) < 1) {
                        sendMessage(player, "You don't have enough inventory space.")
                    }
                    npc.clear()
                    HintIconManager.removeHintIcon(player, 1)
                    val falconCatch = npc.getAttribute<FalconCatch>("falcon:catch")
                    player.getSkills().addExperience(Skills.HUNTER, falconCatch.experience, true)
                    sendMessage(player, "You retrieve the falcon as well as the fur of the dead kebbit.")
                    player.inventory.add(falconCatch.item)
                    player.inventory.add(BONES)
                    if (removeItem(player, Items.FALCONERS_GLOVE_10023, Container.EQUIPMENT)) {
                        player.equipment.add(FALCON, true, false)
                    } else {
                        removeItem(player, GLOVE, Container.INVENTORY)
                        player.inventory.add(FALCON)
                    }
                }
            }
            return true
        }

        override fun isWalk(): Boolean = false

        override fun isWalk(player: Player, node: Node): Boolean {
            if (node.asNpc() is NPC) return node.id == NPCs.MATTHIAS_5093 || node.id == NPCs.GYR_FALCON_5094
            return false
        }

        companion object {
            private val BONES = Item(Items.BONES_526)
            private val FALCON = Item(Items.FALCONERS_GLOVE_10024)
            private val GLOVE = Item(Items.FALCONERS_GLOVE_10023)
        }
    }

    override fun defineAreaBorders(): Array<ZoneBorders> = arrayOf(ZoneBorders(2363, 3574, 2395, 3620))

    override fun areaLeave(
        entity: Entity,
        logout: Boolean,
    ) {
        super.areaLeave(entity, logout)
        if (entity is Player && !entity.isArtificial) {
            val player = entity.asPlayer()
            removeAttribute(player, "falconry")
            if (anyInEquipment(player, Items.FALCONERS_GLOVE_10023, Items.FALCONERS_GLOVE_10024) ||
                anyInInventory(player, Items.FALCONERS_GLOVE_10023, Items.FALCONERS_GLOVE_10024)
            ) {
                removeItems(player)
            }
        }
    }

    override fun getRestrictions(): Array<ZoneRestriction> =
        arrayOf(ZoneRestriction.CANNON, ZoneRestriction.FIRES, ZoneRestriction.RANDOM_EVENTS)
}

private enum class FalconCatch(val npc: Int, val level: Int, val experience: Double, val item: Item) {
    SPOTTED_KEBBIT(NPCs.SPOTTED_KEBBIT_5098, 43, 104.0, Item(Items.SPOTTED_KEBBIT_FUR_10125)),
    DARK_KEBBIT(NPCs.DARK_KEBBIT_5099, 57, 132.0, Item(Items.DARK_KEBBIT_FUR_10115)),
    DASHING_KEBBIT(NPCs.DASHING_KEBBIT_5100, 69, 156.0, Item(Items.DASHING_KEBBIT_FUR_10127)),
    ;

    companion object {
        @JvmStatic
        fun forItem(item: Item): FalconCatch? {
            for (falconCatch in values()) {
                if (item.id == falconCatch.item.id) {
                    return falconCatch
                }
            }
            return null
        }

        @JvmStatic
        fun forNPC(npc: NPC): FalconCatch? {
            for (falconCatch in values()) {
                if (npc.id == falconCatch.npc) {
                    return falconCatch
                }
            }
            return null
        }
    }
}

private class FalconryCatchPulse(player: Player?, node: NPC, private val falconCatch: FalconCatch) :
    SkillPulse<NPC?>(player, node) {
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
                        val projectile = Projectile.create(node!!.asNpc(), Repository.findNPC(5093), 918)
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

    fun success(): Boolean =
        if (originalLocation !== node!!.asNpc().location) {
            RandomFunction.random(1, 3) == 2
        } else {
            RandomFunction.getRandom(3) * player.getSkills().getLevel(Skills.HUNTER) / 3 > falconCatch.level / 2
        }

    companion object {
        private val FALCON = Item(Items.FALCONERS_GLOVE_10024)
        private val GLOVE = Item(Items.FALCONERS_GLOVE_10023)
    }
}