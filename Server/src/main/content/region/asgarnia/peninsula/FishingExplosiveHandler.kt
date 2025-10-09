package content.region.asgarnia.peninsula

import content.global.skill.slayer.npc.MogreNPC
import core.game.interaction.UseWithHandler
import core.game.world.update.flag.context.Animation
import shared.consts.Items
import core.api.*
import core.game.interaction.NodeUsageEvent
import core.game.node.Node
import core.game.node.entity.combat.ImpactHandler
import core.game.node.entity.impl.Projectile
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.HintIconManager
import core.game.node.item.Item
import core.game.node.scenery.Scenery
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.game.world.map.Location
import core.game.world.update.flag.context.Graphics
import core.plugin.Initializable
import core.plugin.Plugin
import core.tools.RandomFunction
import shared.consts.*

@Initializable
class FishingExplosiveHandler : UseWithHandler(Items.FISHING_EXPLOSIVE_6664, Items.SUPER_FISHING_EXPLOSIVE_12633) {

    override fun newInstance(arg: Any?): Plugin<Any> {
        OMINOUS_FISHING_SPOTS.forEach { addHandler(it, OBJECT_TYPE, this) }
        return this
    }

    override fun handle(event: NodeUsageEvent): Boolean {
        val player = event.player

        if (!GameWorld.settings!!.isDevMode &&
            getVarbit(player, Vars.VARBIT_MINI_QUEST_MOGRE_AND_SKIPPY_1344) != 3) {
            return false
        }

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
                val location = Location.getRandomLocation(event.usedWith.location, 2, true)
                val mogre = MogreNPC(MOGRE_ID, location)
                mogre.init()
                mogre.isRespawn = false
                mogre.setAttribute("player", player)
                mogre.sendChat(MESSAGES[RandomFunction.random(MESSAGES.size)])
                HintIconManager.registerHintIcon(player, mogre)

                if (event.usedItem.id == Items.SUPER_FISHING_EXPLOSIVE_12633 && hasRequirement(player, Quests.KENNITHS_CONCERNS)) {
                    impact(mogre, 15, ImpactHandler.HitsplatType.NORMAL)
                }

                mogre.graphics(SPLASH_GRAPHICS)
                sendMessage(player, "...and a Mogre appears!")
                mogre.attack(player)

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

    companion object {
        private val ANIMATION = Animation(Animations.THROW_385)
        private val SPLASH_GRAPHICS = Graphics(shared.consts.Graphics.WATER_SPLASH_68)
        private const val MOGRE_ID = NPCs.MOGRE_114
        private val MESSAGES = arrayOf("Da boom-boom kill all da fishies!", "I smack you good!", "Smash stupid human!", "Tasty human!", "Human hit me on the head!", "I get you!", "Human scare all da fishies!")
    }

}