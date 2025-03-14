package content.region.asgarnia.handlers.guilds.warriorsguild

import core.api.playAudio
import core.api.setAttribute
import core.api.setVarp
import core.cache.def.impl.ItemDefinition
import core.cache.def.impl.SceneryDefinition
import core.game.component.Component
import core.game.component.ComponentDefinition
import core.game.component.ComponentPlugin
import core.game.container.impl.EquipmentContainer
import core.game.dialogue.FaceAnim
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.combat.ImpactHandler.HitsplatType
import core.game.node.entity.impl.Projectile
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.node.scenery.SceneryBuilder
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import core.game.world.map.Location
import core.game.world.map.RegionManager.getObject
import core.game.world.map.zone.MapZone
import core.game.world.map.zone.ZoneBorders
import core.game.world.map.zone.ZoneBuilder
import core.game.world.update.flag.context.Animation
import core.plugin.ClassScanner.definePlugin
import core.plugin.Initializable
import core.plugin.Plugin
import core.tools.RandomFunction
import org.rs.consts.Sounds

@Initializable
class CatapultRoom :
    MapZone("wg catapult", true),
    Plugin<Any> {
    private enum class CatapultAttack(
        val graphicId: Int,
        val objectId: Int,
        val success: Animation,
        val fail: Animation,
    ) {
        SPIKY_BALL(graphicId = 679, objectId = 15617, success = Animation.create(4169), fail = Animation.create(4173)),
        FLUNG_ANVIL(graphicId = 680, objectId = 15619, success = Animation.create(4168), fail = Animation.create(4172)),
        SLASHING_BLADES(
            graphicId = 681,
            objectId = 15620,
            success = Animation.create(4170),
            fail = Animation.create(4174),
        ),
        MAGIC_MISSILE(
            graphicId = 682,
            objectId = 15618,
            success = Animation.create(4171),
            fail = Animation.create(4175),
        ),
    }

    override fun newInstance(arg: Any?): Plugin<Any> {
        ZoneBuilder.configure(this)
        definePlugin(
            object : OptionHandler() {
                override fun newInstance(arg: Any?): Plugin<Any> {
                    ItemDefinition.forId(SHIELD_ID).handlers["option:wield"] = this
                    SceneryDefinition.forId(15657).handlers["option:view"] = this
                    return this
                }

                override fun handle(
                    player: Player,
                    node: Node,
                    option: String,
                ): Boolean {
                    if (node is Item) {
                        if (player.location != TARGET) {
                            player.packetDispatch.sendMessage(
                                "You may not equip this shield outside the target area in the Warrior's Guild.",
                            )
                            return true
                        }
                        if (player.equipment[EquipmentContainer.SLOT_WEAPON] != null) {
                            player.dialogueInterpreter.sendDialogue(
                                "You will need to make sure your sword hand is free to equip this",
                                "shield.",
                            )
                            return true
                        }
                        ItemDefinition.getOptionHandlers()["wield"]!!.handle(player, node, option)
                        if (player.equipment.getNew(EquipmentContainer.SLOT_SHIELD).id == SHIELD_ID) {
                            player.interfaceManager.removeTabs(2, 3, 5, 6, 7, 11, 12)
                            player.interfaceManager.openTab(4, Component(411))
                            player.interfaceManager.setViewedTab(4)
                        }
                        return true
                    }
                    player.interfaceManager.open(Component(410))
                    return true
                }
            },
        )
        definePlugin(
            object : ComponentPlugin() {
                override fun newInstance(arg: Any?): Plugin<Any> {
                    ComponentDefinition.put(411, this)
                    return this
                }

                override fun handle(
                    player: Player,
                    component: Component,
                    opcode: Int,
                    button: Int,
                    slot: Int,
                    itemId: Int,
                ): Boolean {
                    if (button in 9..12) {
                        val attack = CatapultAttack.values()[button - 9]
                        setAttribute(player, "catapult_def", attack)
                        setVarp(player, 788, (button - 8) % 4)
                        return true
                    }
                    return false
                }
            },
        )
        return this
    }

    override fun fireEvent(
        identifier: String,
        vararg args: Any,
    ): Any? {
        return null
    }

    override fun configure() {
        super.register(ZoneBorders(2837, 3542, 2847, 3556))
        pulse.stop()
    }

    override fun enter(e: Entity): Boolean {
        if (e is Player && e.getLocation().z == 1) {
            players.add(e)
            if (!pulse.isRunning) {
                pulse.restart()
                pulse.start()
                Pulser.submit(pulse)
            }
        }
        return super.enter(e)
    }

    override fun leave(
        e: Entity,
        logout: Boolean,
    ): Boolean {
        if (e is Player) {
            players.remove(e)
            unequipShield(e)
        }
        return super.leave(e, logout)
    }

    override fun locationUpdate(
        e: Entity,
        last: Location,
    ) {
        if (e is Player && last == TARGET) {
            unequipShield(e)
        }
    }

    companion object {
        val TARGET: Location = Location.create(2842, 3545, 1)
        private const val SHIELD_ID = 8856
        private val players: MutableList<Player> = ArrayList(20)
        private var attack: CatapultAttack? = null

        private val pulse: Pulse =
            object : Pulse(10) {
                override fun pulse(): Boolean {
                    attack = RandomFunction.getRandomElement(CatapultAttack.values())
                    Pulser.submit(
                        object : Pulse(7) {
                            override fun pulse(): Boolean {
                                for (p in players) {
                                    if (p.isActive && p.location == TARGET) {
                                        if (p.equipment.getNew(EquipmentContainer.SLOT_SHIELD).id != SHIELD_ID) {
                                            p.dialogueInterpreter.sendDialogues(
                                                4287,
                                                FaceAnim.FURIOUS,
                                                "Watch out! You'll need to equip the shield as soon as",
                                                "you're on the target spot else you could get hit! Speak",
                                                "to me to get one, and make sure both your hands are",
                                                "free to equip it.",
                                            )
                                            p.walkingQueue.reset()
                                            p.walkingQueue.addPath(p.location.x + 1, p.location.y)
                                            continue
                                        }
                                        p.faceLocation(Location.create(2842, 3554, 1))
                                        if (p.getAttribute("catapult_def", CatapultAttack.MAGIC_MISSILE) != attack) {
                                            p.packetDispatch.sendMessage(
                                                "You fail defending against the " + attack!!.name + ".",
                                            )
                                            p.impactHandler.manualHit(p, 3, HitsplatType.NORMAL)
                                            p.animate(attack!!.fail)
                                        } else {
                                            p.getSkills().addExperience(Skills.DEFENCE, 10.0, true)
                                            p.packetDispatch.sendMessage(
                                                "You successfully defend against the " + attack!!.name + ".",
                                            )
                                            p.getSavedData().activityData.updateWarriorTokens(1)
                                            p.animate(attack!!.success)
                                        }
                                    }
                                }
                                return true
                            }
                        },
                    )
                    Projectile
                        .create(
                            Location.create(2842, 3554, 1),
                            Location.create(2842, 3545, 1),
                            attack!!.graphicId,
                            70,
                            32,
                            80,
                            220,
                            20,
                            11,
                        ).send()
                    val scenery = getObject(Location.create(2840, 3552, 1))
                    if (scenery != null) SceneryBuilder.replace(scenery, scenery.transform(attack!!.objectId), 4)
                    for (p in players) {
                        playAudio(p, Sounds.WARGUILD_CATAPULT_ANVIL_1911)
                    }
                    return players.isEmpty()
                }
            }

        private fun unequipShield(player: Player) {
            if (player.equipment.getNew(EquipmentContainer.SLOT_SHIELD).id == SHIELD_ID) {
                player.inventory.add(Item(SHIELD_ID), player)
                player.equipment.replace(null, EquipmentContainer.SLOT_SHIELD)
                player.interfaceManager.restoreTabs()
                player.interfaceManager.openTab(4, Component(387))
            }
        }
    }
}
