package content.global.skill.runecrafting

import content.global.skill.runecrafting.items.Talisman
import content.global.skill.runecrafting.pouch.RunePouchPlugin
import content.global.skill.runecrafting.runes.CombinationHandler
import content.global.skill.runecrafting.scenery.Altar
import content.global.travel.EssenceTeleport.home
import content.global.travel.EssenceTeleport.teleport
import core.api.*
import core.api.quest.hasRequirement
import core.api.quest.isQuestComplete
import core.api.ui.setMinimapState
import core.cache.def.impl.ItemDefinition
import core.cache.def.impl.NPCDefinition
import core.cache.def.impl.SceneryDefinition
import core.game.global.action.ClimbActionHandler
import core.game.interaction.OptionHandler
import core.game.interaction.QueueStrength
import core.game.node.Node
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.info.Rights
import core.game.node.entity.player.link.TeleportManager
import core.game.node.item.Item
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.plugin.ClassScanner.definePlugin
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs.consts.Components
import org.rs.consts.Quests

@Initializable
class RunecraftingPlugin : OptionHandler() {
    override fun newInstance(arg: Any?): Plugin<Any?> {
        addNodes()
        definePlugin(RunePouchPlugin())
        definePlugin(CombinationHandler())
        SceneryDefinition.forId(2492).handlers["option:use"] = this
        NPCDefinition.forId(553).handlers["option:teleport"] = this
        NPCDefinition.forId(2328).handlers["option:teleport"] = this
        SceneryDefinition.forId(26849).handlers["option:climb"] = this
        SceneryDefinition.forId(26850).handlers["option:climb"] = this
        SceneryDefinition.forId(268).handlers["option:climb"] = this
        SceneryDefinition.forId(26844).handlers["option:squeeze-through"] = this
        SceneryDefinition.forId(26845).handlers["option:squeeze-through"] = this
        return this
    }

    override fun handle(
        player: Player,
        node: Node,
        option: String,
    ): Boolean {
        if (!isQuestComplete(player, Quests.RUNE_MYSTERIES) && player.details.rights !== Rights.ADMINISTRATOR) {
            sendMessage(player, "You need to finish the Rune Mysteries Quest in order to do this.")
            return true
        }
        when (node.id) {
            2492 -> {
                home(player, node)
                sendMessage(player, "You step through the portal...")
                return true
            }

            26844, 26845 -> {
                val location = if (node.id == 26845) Location(3309, 4820, 0) else Location(3311, 4817, 0)
                lock(player, 4)
                openOverlay(player, Components.FADE_TO_BLACK_115)
                setMinimapState(player, 2)
                queueScript(player, 4, QueueStrength.SOFT) {
                    setMinimapState(player, 0)
                    teleport(player, location)
                    closeInterface(player)
                    closeOverlay(player)
                    return@queueScript stopExecuting(player)
                }
                return true
            }

            553, 2328, 844 -> {
                teleport((node as NPC), player)
                return true
            }
        }
        when (option) {
            "use" -> {
                val altar = Altar.forScenery(node.asScenery())
                teleport(player, altar!!.ruin!!.base, TeleportManager.TeleportType.INSTANT)
            }

            "craft-rune" -> {
                if (node.location == Location(3151, 3484)) {
                    sendMessage(player, "You can only craft Astral runes on Lunar Isle.")
                    return true
                }
                val a = Altar.forScenery((node.asScenery()))
                if (a == Altar.ASTRAL) {
                    if (!hasRequirement(player, Quests.LUNAR_DIPLOMACY)) return true
                }
                player.pulseManager.run(RunecraftPulse(player, null, a!!, false, null))
            }

            "locate" -> {
                val talisman = Talisman.forItem((node.asItem()))
                talisman!!.locate(player)
            }

            "climb" -> {
                when (node.id) {
                    26849 -> ClimbActionHandler.climb(player, Animation(827), Location(3271, 4861, 0))
                    26850 -> ClimbActionHandler.climb(player, Animation(828), Location(2452, 3230, 0))
                }
            }
        }
        return true
    }

    private fun addNodes() {
        for (altar in Altar.values()) {
            SceneryDefinition.forId(altar.objs).handlers["option:craft-rune"] = this
            SceneryDefinition.forId(altar.exit).handlers["option:use"] = this
        }
        for (talisman in Talisman.values()) {
            ItemDefinition.forId(talisman.item.id).handlers["option:locate"] = this
        }
    }

    override fun isWalk(): Boolean {
        return false
    }

    override fun isWalk(
        player: Player,
        node: Node,
    ): Boolean {
        return node !is Item
    }
}
