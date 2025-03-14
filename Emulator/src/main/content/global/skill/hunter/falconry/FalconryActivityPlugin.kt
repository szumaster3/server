package content.global.skill.hunter.falconry

import core.api.*
import core.cache.def.impl.ItemDefinition
import core.cache.def.impl.NPCDefinition
import core.game.activity.ActivityPlugin
import core.game.interaction.Option
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.HintIconManager
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.world.map.Location
import core.game.world.map.zone.ZoneBorders
import core.plugin.ClassScanner.definePlugin
import core.plugin.Plugin
import org.rs.consts.Items
import org.rs.consts.NPCs

class FalconryActivityPlugin : ActivityPlugin(FALCONRY_ZONE, false, false, false) {
    override fun newInstance(p: Player): ActivityPlugin {
        return FalconryActivityPlugin()
    }

    override fun start(
        player: Player,
        login: Boolean,
        vararg args: Any,
    ): Boolean {
        setAttribute(player, "/save:$FALCONRY_ZONE", true)
        return super.start(player, login, *args)
    }

    override fun leave(
        entity: Entity,
        logout: Boolean,
    ): Boolean {
        super.leave(entity, logout)
        return true
    }

    override fun interact(
        e: Entity,
        target: Node,
        option: Option,
    ): Boolean {
        return false
    }

    override fun getSpawnLocation(): Location? {
        return null
    }

    override fun configure() {
        register(ZoneBorders(2363, 3574, 2395, 3620))
    }

    override fun register() {
        definePlugin(FalconryPlugin())
    }

    class FalconryPlugin : OptionHandler() {
        override fun newInstance(arg: Any?): Plugin<Any> {
            NPCDefinition.forId(5093).handlers["option:quick-falconry"] = this
            NPCDefinition.forId(5094).handlers["option:retrieve"] = this
            for (falconCatch in FalconCatch.values()) {
                NPCDefinition.forId(falconCatch.npc).handlers["option:catch"] = this
            }
            ItemDefinition.forId(10023).handlers["equipment"] = this
            ItemDefinition.forId(10024).handlers["equipment"] = this
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

        override fun isWalk(): Boolean {
            return false
        }

        override fun isWalk(
            player: Player,
            node: Node,
        ): Boolean {
            if (node.asNpc() is NPC) return node.id == NPCs.MATTHIAS_5093 || node.id == NPCs.GYR_FALCON_5094
            return false
        }

        companion object {
            private val BONES = Item(Items.BONES_526)
            private val FALCON = Item(Items.FALCONERS_GLOVE_10024)
            private val GLOVE = Item(Items.FALCONERS_GLOVE_10023)
        }
    }

    companion object {
        val FALCONRY_ZONE = "falconry"

        fun removeItems(player: Player?) {
            removeItem(player!!, Items.FALCONERS_GLOVE_10023, Container.INVENTORY)
            removeItem(player, Items.FALCONERS_GLOVE_10023, Container.EQUIPMENT)
            removeItem(player, Items.FALCONERS_GLOVE_10024, Container.INVENTORY)
            removeItem(player, Items.FALCONERS_GLOVE_10024, Container.EQUIPMENT)
        }
    }
}
