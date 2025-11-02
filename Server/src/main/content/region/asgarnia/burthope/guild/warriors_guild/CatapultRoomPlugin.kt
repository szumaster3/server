package content.region.asgarnia.burthope.guild.warriors_guild

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
import shared.consts.*

/**
 * Handles the Warrior's Guild catapult room.
 */
@Initializable
class CatapultRoomPlugin : MapZone("wg catapult", true), Plugin<Any> {

    /**
     * Represents the types of catapult attacks.
     *
     * @property graphicId the graphic effect id.
     * @property objectId the object ID of the catapult projectile.
     * @property success the animation played when the attack is successfully defended.
     * @property fail the animation played when the attack is failed.
     */
    private enum class CatapultAttack(val graphicId: Int, val objectId: Int, val success: Animation, val fail: Animation) {
        SPIKY_BALL(Graphics.W_GUILD_SPIKE_BALL_679, Scenery.CATAPULT_15617, Animation.create(Animations.BEND_DOWN_ON_KNEES_HAND_IN_FRONT_OF_FACE_4169), Animation.create(Animations.FALL_BACK_KNEES_4173)),
        FLUNG_ANVIL(Graphics.W_GUILD_ANVIL_CAT_680, Scenery.CATAPULT_15619, Animation.create(Animations.MOVE_HEAD_TO_LISTEN_ON_DOOR_4168), Animation.create(Animations.FALL_BACK_BUTT_4172)),
        SLASHING_BLADES(Graphics.W_GUILD_BLADE_CAT_681, Scenery.CATAPULT_15620, Animation.create(Animations.STRETCH_KNEE_4170), Animation.create(Animations.BENDING_BOWING_IN_PAIN_4174)),
        MAGIC_MISSILE(Graphics.W_GUILD_ORANGE_BALL_682, Scenery.CATAPULT_15618, Animation.create(Animations.COVER_HEAD_HANDS_4171), Animation.create(Animations.BENDING_ON_KNEES_IN_PAIN_4175)),
    }

    override fun newInstance(arg: Any?): Plugin<Any> {
        ZoneBuilder.configure(this)
        definePlugin(
            object : OptionHandler() {
                override fun newInstance(arg: Any?): Plugin<Any> {
                    ItemDefinition.forId(SHIELD_ID).handlers["option:wield"] = this
                    SceneryDefinition.forId(Scenery.INFORMATION_SCROLL_15657).handlers["option:view"] = this
                    return this
                }

                override fun handle(player: Player, node: Node, option: String): Boolean {
                    if (node is Item) {
                        if (player.location != TARGET) {
                            player.packetDispatch.sendMessage("You may not equip this shield outside the target area in the Warrior's Guild.")
                            return true
                        }
                        if (player.equipment[EquipmentContainer.SLOT_WEAPON] != null) {
                            player.dialogueInterpreter.sendDialogue(
                                "You will need to make sure your sword hand is free to equip this",
                                "shield."
                            )
                            return true
                        }
                        ItemDefinition.getOptionHandlers()["wield"]!!.handle(player, node, option)
                        if (player.equipment.getNew(EquipmentContainer.SLOT_SHIELD).id == SHIELD_ID) {
                            player.interfaceManager.removeTabs(2, 3, 5, 6, 7, 11, 12)
                            player.interfaceManager.openTab(4, Component(Components.WARGUILD_DEFENCE_MINI_411))
                            player.interfaceManager.setViewedTab(4)
                        }
                        return true
                    }
                    player.interfaceManager.open(Component(Components.WARGUILD_DEFENCE_410))
                    return true
                }
            },
        )
        definePlugin(
            object : ComponentPlugin() {
                override fun newInstance(arg: Any?): Plugin<Any> {
                    ComponentDefinition.put(Components.WARGUILD_DEFENCE_MINI_411, this)
                    return this
                }

                override fun handle(player: Player, component: Component, opcode: Int, button: Int, slot: Int, itemId: Int): Boolean {
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

    override fun fireEvent(identifier: String, vararg args: Any): Any? = null

    /**
     * Configures the zone borders and stops the catapult pulse initially.
     */
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

    override fun leave(e: Entity, logout: Boolean): Boolean {
        if (e is Player) {
            players.remove(e)
            unequipShield(e)
        }
        return super.leave(e, logout)
    }

    override fun locationUpdate(e: Entity, last: Location) {
        if (e is Player && last == TARGET) {
            unequipShield(e)
        }
    }

    companion object {
        /**
         * The target location where players must defend against catapult attacks.
         */
        val TARGET: Location = Location.create(2842, 3545, 1)

        /**
         * Shield item id used for defense.
         */
        private const val SHIELD_ID = Items.DEFENSIVE_SHIELD_8856

        /**
         * List of active players in the catapult room.
         */
        private val players: MutableList<Player> = ArrayList(20)

        /**
         * The current catapult attack.
         */
        private var attack: CatapultAttack? = null

        /**
         * Pulse that triggers random catapult attacks.
         */
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
                                                NPCs.GAMFRED_4287,
                                                FaceAnim.CHILD_NORMAL,
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
                                            p.packetDispatch.sendMessage("You fail defending against the " + attack!!.name + ".")
                                            p.impactHandler.manualHit(p, 3, HitsplatType.NORMAL)
                                            p.animate(attack!!.fail)
                                        } else {
                                            p.getSkills().addExperience(Skills.DEFENCE, 10.0, true)
                                            p.packetDispatch.sendMessage("You successfully defend against the " + attack!!.name + ".")
                                            p.getSavedData().activityData.updateWarriorTokens(1)
                                            p.animate(attack!!.success)
                                        }
                                    }
                                }
                                return true
                            }
                        },
                    )
                    Projectile.create(
                        Location.create(2842, 3554, 1),
                        Location.create(2842, 3545, 1),
                        attack!!.graphicId,
                        70,
                        32,
                        80,
                        220,
                        20,
                        11
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
                player.interfaceManager.openTab(4, Component(Components.WORNITEMS_387))
            }
        }
    }
}
