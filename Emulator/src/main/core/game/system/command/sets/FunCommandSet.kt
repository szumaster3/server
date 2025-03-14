package core.game.system.command.sets

import content.global.handlers.item.SpadeDigUtils
import content.region.misc.handlers.tutorial.CharacterDesign
import core.api.*
import core.game.dialogue.DialogueFile
import core.game.node.entity.combat.ImpactHandler
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.info.login.PlayerSaver
import core.game.system.command.Privilege
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.game.world.map.Location
import core.game.world.map.RegionManager
import core.game.world.repository.Repository.getPlayerByName
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.RandomFunction
import org.json.simple.JSONObject
import org.rs.consts.Sounds
import java.awt.HeadlessException
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection
import kotlin.streams.toList

@Initializable
class FunCommandSet : CommandSet(Privilege.ADMIN) {
    var npcs: List<NPC> = ArrayList()

    override fun defineCommands() {
        define(
            name = "npcanim",
            privilege = Privilege.ADMIN,
            usage = "::npcanim <lt>Animation ID<gt>",
        ) { player, args ->
            if (args.size < 2) {
                reject(player, "Syntax error: ::npcanim <Animation ID>")
            }
            npcs = RegionManager.getLocalNpcs(player.location, 10)
            for (n in npcs) {
                n.sendChat(args.slice(1 until args.size).joinToString(" "))
                n.lock(6)
                n.faceTemporary(player, 6)
                n.animator.animate(Animation(args[1].toInt()))
                n.animate(Animation.create(-1), 6)
            }
        }

        define(
            name = "pnpc",
            privilege = Privilege.MODERATOR,
            usage = "::pnpc <lt>NPC ID<gt>",
            description = "Transforms the player into the given NPC.",
        ) { player, args ->
            if (args.size < 2) {
                reject(player, "Usage: ::pnpc <npcid>")
                return@define
            }

            val pnpc_id = args[1].toIntOrNull()
            if (pnpc_id == null) {
                reject(player, "<npcid> must be a valid integer.")
            }

            player.appearance.transformNPC(pnpc_id!!)
            notify(player, "Transformed into NPC $pnpc_id")
        }

        define(name = "bank", privilege = Privilege.ADMIN, usage = "", description = "Opens your bank.") { player, _ ->
            player.bank.open()
        }

        define(
            name = "invis",
            privilege = Privilege.ADMIN,
            usage = "",
            description = "Makes you invisible to others.",
        ) { player, _ ->
            player.isInvisible = !player.isInvisible
            notify(player, "You are now ${if (player.isInvisible) "invisible" else "visible"} to others.")
        }

        define(
            name = "1hit",
            privilege = Privilege.ADMIN,
            usage = "",
            description = "Makes you kill things in 1 hit.",
        ) { player, _ ->
            player.setAttribute("1hko", !player.getAttribute("1hko", false))
            notify(player, "1-hit KO mode " + if (player.getAttribute("1hko", false)) "on." else "off.")
        }

        define(
            name = "god",
            privilege = Privilege.ADMIN,
            usage = "",
            description = "Makes you invulnerable to damage.",
        ) { player, _ ->
            player.setAttribute("godMode", !player.getAttribute("godMode", false))
            notify(player, "God mode ${if (player.getAttribute("godMode", false)) "enabled." else "disabled."}")
        }

        define(name = "mrboneswildride") { player, args ->
            val p: Player =
                if (args.size > 2) {
                    reject(player, "Usage: ::mrboneswildride <username>")
                    return@define
                } else if (args.size == 1) {
                    player
                } else if (getPlayerByName(args[1]) == null) {
                    reject(player, "ERROR: Username not found. Usage: ::mrboneswildride <username>")
                    return@define
                } else {
                    getPlayerByName(args[1]) ?: return@define
                }
            val boneMode = !p.getAttribute("boneMode", false)
            p.setAttribute("boneMode", boneMode)
            notify(p, "Bone Mode ${if (boneMode) "<col=00ff00>ENGAGED</col>." else "<col=ff0000>POWERING DOWN</col>."}")
            p.appearance.rideCart(boneMode)
            if (p.appearance.isRidingMinecart) {
                var i = 0
                GameWorld.Pulser.submit(
                    object : Pulse(1, player) {
                        override fun pulse(): Boolean {
                            if (i++ % 12 == 0) p.sendChat("I want to get off Mr. Bones Wild Ride.")
                            p.moveStep()
                            return !p.appearance.isRidingMinecart
                        }
                    },
                )
            }
        }

        define(name = "makeover", privilege = Privilege.MODERATOR) { player, _ ->
            CharacterDesign.open(player)
        }

        define(name = "dumpappearance", privilege = Privilege.MODERATOR) { player, _ ->
            val json = JSONObject()
            PlayerSaver(player).saveAppearance(json)
            val equipJson = PlayerSaver(player).saveContainer(player.equipment)
            json["equipment"] = equipJson
            val jsonString = json.toJSONString()
            try {
                val clpbrd = Toolkit.getDefaultToolkit().systemClipboard
                clpbrd.setContents(StringSelection(jsonString), null)
                notify(player, "Appearance and equipment copied to clipboard.")
            } catch (e: HeadlessException) {
                reject(player, "NOTE: Paste will not be available due to remote server.")
            }
        }

        define("bury") { player, _ ->
            if (player.inventory.isEmpty) {
                reject(player, "You have no items to bury.")
            }

            player.dialogueInterpreter.open(
                object : DialogueFile() {
                    override fun handle(
                        componentID: Int,
                        buttonID: Int,
                    ) {
                        when (stage) {
                            0 ->
                                dialogue(
                                    "This will bury your whole inventory in this spot.",
                                    "Are you sure?",
                                ).also { stage++ }

                            1 -> options("Yes", "No").also { stage++ }
                            2 ->
                                when (buttonID) {
                                    1 -> bury(player).also { end() }
                                    2 -> stage = END_DIALOGUE
                                }
                        }
                    }
                },
            )
        }

        define(
            name = "appearance",
            privilege = Privilege.ADMIN,
            usage = "",
            description = "Allows you to change your appearance.",
        ) { player, _ ->
            CharacterDesign.reopen(player)
        }

        define(
            name = "barrage",
            privilege = Privilege.ADMIN,
            usage = "::barrage radius ",
            description = "Cast a weak barrage on all nearby players. Will never kill players",
        ) { player, args ->
            if (args.size != 2) reject(player, "Usage: ::barrage radius[max = 50]")
            val radius = if (args[1].toInt() > 50) 50 else args[1].toInt()
            val nearbyPlayers =
                RegionManager
                    .getLocalPlayers(player, radius)
                    .stream()
                    .filter { p: Player -> p.username != player.username }
                    .toList()
            animate(player, 1978)
            playGlobalAudio(player.location, Sounds.ICE_CAST_171)
            for (p in nearbyPlayers) {
                playGlobalAudio(p.location, Sounds.ICE_BARRAGE_IMPACT_168, 20)
                val impactAmount = if (p.skills.lifepoints < 10) 0 else RandomFunction.getRandom(3)
                impact(p, impactAmount, ImpactHandler.HitsplatType.NORMAL)
                p.graphics(Graphics(369, 0))
            }
        }
    }

    fun bury(player: Player) {
        val loc = Location.create(player.location)
        val inv = player.inventory.toArray().filterNotNull()
        SpadeDigUtils.registerListener(player.location) { p ->
            for (item in inv) {
                addItemOrDrop(p, item.id, item.amount)
                sendMessage(p, "You dig and find ${if (item.amount > 1) "some" else "a"} ${item.name}")
            }
            sendNews("${player.username} has found the hidden treasure! Congratulations!!!")
            SpadeDigUtils.listeners.remove(loc)
        }
        player.inventory.clear()
        notify(player, "You have buried your loot at $loc")
    }
}
