package content.minigame.mta.plugin

import content.global.skill.agility.AgilityHandler
import content.minigame.mta.plugin.room.TelekineticTheatrePlugin
import core.api.hasLevelStat
import core.api.openDialogue
import core.api.openInterface
import core.api.sendDialogue
import core.cache.def.impl.ItemDefinition
import core.cache.def.impl.NPCDefinition
import core.cache.def.impl.SceneryDefinition
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.GroundItem
import core.game.world.map.Direction
import core.game.world.map.Location
import core.game.world.map.zone.ZoneBuilder
import core.game.world.update.flag.context.Animation
import core.plugin.ClassScanner.definePlugins
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs.consts.Components
import org.rs.consts.NPCs
import org.rs.consts.Scenery

@Initializable
class MageTrainingArena : OptionHandler() {
    override fun newInstance(arg: Any?): Plugin<Any> {
        SceneryDefinition.forId(Scenery.DOORWAY_10721).handlers["option:enter"] = this
        NPCDefinition.forId(NPCs.REWARDS_GUARDIAN_3103).handlers["option:trade-with"] = this
        for (type in MTAType.values()) {
            if (type.mtaZone != null) {
                ZoneBuilder.configure(type.mtaZone)
            }
            SceneryDefinition.forId(type.sceneryId).handlers["option:enter"] = this
        }
        ItemDefinition.forId(TelekineticTheatrePlugin.STATUE).handlers["option:observe"] = this
        ItemDefinition.forId(TelekineticTheatrePlugin.STATUE).handlers["option:reset"] = this
        NPCDefinition.forId(NPCs.MAZE_GUARDIAN_3102).handlers["option:talk-to"] = this
        definePlugins(EnchantSpell(), TelekineticGrabSpell())
        return this
    }

    override fun handle(
        player: Player,
        node: Node,
        option: String,
    ): Boolean {
        when (node.id) {
            Scenery.DOORWAY_10721 -> {
                if (!hasLevelStat(player, Skills.MAGIC, 7)) {
                    sendDialogue(player, "You need a Magic level of at least 7 to enter the guild.")
                }
                AgilityHandler.walk(
                    player,
                    -1,
                    player.location,
                    player.location.transform(Direction.getDirection(player.location, node.location), 2),
                    Animation(1426),
                    0.0,
                    null,
                )
            }

            NPCs.REWARDS_GUARDIAN_3103 ->
                if (!player.getSavedData().activityData.isStartedMta) {
                    openDialogue(player, NPCs.REWARDS_GUARDIAN_3103, this, true, true)
                } else {
                    openInterface(player, Components.MAGICTRAINING_SHOP_197)
                }

            NPCs.MAZE_GUARDIAN_3102 -> openDialogue(player, node.id, node)
        }
        when (option) {
            "enter" -> {
                val type: MTAType = MTAType.forId(node.id)!!
                type.enter(player)
            }

            "reset", "observe" -> {
                val zone: TelekineticTheatrePlugin = TelekineticTheatrePlugin.getZone(player)
                if (option == "reset") {
                    zone.reset(player)
                } else {
                    zone.observe(player)
                }
            }
        }
        return true
    }

    override fun isWalk(
        player: Player,
        n: Node,
    ): Boolean {
        if (n !is GroundItem) {
            return true
        }
        return n.getId() != TelekineticTheatrePlugin.STATUE
    }

    override fun getDestination(
        node: Node,
        n: Node,
    ): Location? =
        if (n.id == NPCs.MAZE_GUARDIAN_3102) {
            n.location.transform(Direction.getDirection(node.location, n.location), -1)
        } else {
            null
        }

    override fun isWalk(): Boolean = false
}
