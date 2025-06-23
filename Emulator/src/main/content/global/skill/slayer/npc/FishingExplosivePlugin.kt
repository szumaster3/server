package content.global.skill.slayer.npc

import core.api.*
import core.api.quest.hasRequirement
import core.cache.def.impl.SceneryDefinition
import core.game.interaction.NodeUsageEvent
import core.game.interaction.OptionHandler
import core.game.interaction.UseWithHandler
import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.combat.CombatStyle
import core.game.node.entity.combat.ImpactHandler.HitsplatType
import core.game.node.entity.impl.Projectile
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.HintIconManager
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.node.scenery.Scenery
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import core.plugin.Initializable
import core.plugin.Plugin
import core.tools.RandomFunction
import org.rs.consts.*

/**
 * Represents the fishing explosive interaction
 * used to lure [MogreNPC] out of the water.
 */
@Initializable
class FishingExplosivePlugin : OptionHandler() {

    companion object {
        private val OMINOUS_FISHING_SPOTS = intArrayOf(10087, 10088, 10089)
    }

    override fun newInstance(arg: Any?): Plugin<Any> {
        OMINOUS_FISHING_SPOTS.forEach { id ->
            SceneryDefinition.forId(id).handlers["option:lure"] = this
            SceneryDefinition.forId(id).handlers["option:bait"] = this
        }
        FishingExplosiveHandler().newInstance(arg)
        MogreNPC().newInstance(arg)
        return this
    }

    override fun handle(player: Player, node: Node, option: String): Boolean {
        sendMessage(player, "Something seems to have scared all the fishes away...")
        return true
    }

    override fun getDestination(node: Node, n: Node): Location {
        return node.location
    }

    class FishingExplosiveHandler : UseWithHandler(Items.FISHING_EXPLOSIVE_6664, Items.SUPER_FISHING_EXPLOSIVE_12633) {

        companion object {
            private val ANIMATION = Animation(Animations.THROW_385)
            private val SPLASH_GRAPHICS = Graphics(org.rs.consts.Graphics.WATER_SPLASH_68)
            private const val MOGRE_ID = NPCs.MOGRE_114
            private val MESSAGES = arrayOf("Da boom-boom kill all da fishies!", "I smack you good!", "Smash stupid human!", "Tasty human!", "Human hit me on the head!", "I get you!", "Human scare all da fishies!")
        }

        override fun newInstance(arg: Any?): Plugin<Any> {
            OMINOUS_FISHING_SPOTS.forEach { id -> addHandler(id, OBJECT_TYPE, this) }
            return this
        }

        override fun handle(event: NodeUsageEvent): Boolean {
            val player = event.player
            if (getVarbit(player, Vars.VARBIT_MINI_QUEST_MOGRE_AND_SKIPPY_1344) != 3) return false

            if (player.attributes.containsKey("hasMogre")) {
                sendDialogueLines(player, "Sinister as that fishing spot is, why would I want to", "explode it?")
                return true
            }

            if (event.usedItem.id == Items.SUPER_FISHING_EXPLOSIVE_12633 && !hasRequirement(player, Quests.KENNITHS_CONCERNS)) {
                sendMessage(player, "You must complete Kennith's Concerns to use this explosive.")
                return true
            }

            if (!player.inventory.remove(Item(event.usedItem.id, 1))) return true

            val delay = (2 + player.location.getDistance(event.usedWith.location) * 0.5).toInt()
            player.animate(ANIMATION)
            sendMessage(player, "You hurl the shuddering vial into the water...")
            sendProjectile(player, event.usedWith as Scenery)
            setAttribute(player, "hasMogre", true)

            GameWorld.Pulser.submit(object : Pulse(delay, player) {
                override fun pulse(): Boolean {
                    val location = Location.getRandomLocation(
                        event.usedWith.location, 1, true
                    )

                    val mogre = NPC.create(MOGRE_ID, location)
                    mogre.init()
                    mogre.moveStep()
                    mogre.isRespawn = false
                    mogre.attack(player)
                    mogre.setAttribute("player", player)
                    mogre.sendChat(MESSAGES[RandomFunction.random(MESSAGES.size)])
                    HintIconManager.registerHintIcon(player, mogre)
                    if (event.usedItem.id == Items.SUPER_FISHING_EXPLOSIVE_12633 && hasRequirement(player, Quests.KENNITHS_CONCERNS)) {
                        impact(mogre, 15, HitsplatType.NORMAL)
                    }

                    mogre.graphics(SPLASH_GRAPHICS)
                    sendMessage(player, "...and a Mogre appears!")

                    return true
                }
            })
            return true
        }

        private fun sendProjectile(player: Player, scenery: Scenery) {
            val p = Projectile.create(player, null, 49, 30, 20, 30, Projectile.getSpeed(player, scenery.location))
            p.endLocation = scenery.location
            p.send()
        }

        override fun getDestination(player: Player, with: Node): Location = player.location
    }

    inner class MogreNPC : AbstractNPC {

        constructor(id: Int, location: Location) : super(id, location, true)
        constructor() : super(0, null)

        override fun tick() {
            super.tick()
            val pl: Player? = getAttribute("player", null)
            if (pl == null || pl.location.getDistance(location) > 15) {
                clear()
            }
        }

        override fun clear() {
            super.clear()
            val pl: Player? = getAttribute("player", null)
            pl?.removeAttribute("hasMogre")
        }

        override fun isAttackable(entity: Entity, style: CombatStyle, message: Boolean): Boolean {
            val player = getAttribute<Player>("player", null)
            if (entity is Player) {
                if (getStatLevel(entity, Skills.SLAYER) < 32) {
                    sendMessage(entity, "Mogre is Slayer monster that requires a Slayer level of 32 to kill.")
                    return false
                }
            }
            return player == entity && super.isAttackable(entity, style, message)
        }

        override fun finalizeDeath(killer: Entity) {
            if (killer is Player) {
                finishDiaryTask(killer, DiaryType.FALADOR, 2, 7)
                clearHintIcon(killer)
            }
            super.finalizeDeath(killer)
        }

        override fun construct(id: Int, location: Location, vararg objects: Any?): AbstractNPC {
            return MogreNPC(id, location)
        }

        override fun getIds(): IntArray = intArrayOf(NPCs.MOGRE_114)
    }
}
