package core.game.system.command.sets

import content.global.plugins.item.books_and_scrolls.BookInterface
import content.global.plugins.item.books_and_scrolls.BookLine
import content.global.plugins.item.books_and_scrolls.Page
import content.global.plugins.item.books_and_scrolls.PageSet
import content.global.skill.farming.timers.Compost
import content.global.skill.farming.timers.CropGrowth
import content.minigame.trawler.plugin.FishingTrawlerContainer
import core.ServerConstants
import core.api.*
import core.cache.def.impl.NPCDefinition
import core.cache.def.impl.SceneryDefinition
import core.cache.def.impl.VarbitDefinition
import core.game.bots.AIRepository
import core.game.dialogue.InputType
import core.game.ge.GrandExchange
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.info.Rights
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.node.scenery.Scenery
import core.game.system.command.CommandMapping
import core.game.system.command.Privilege
import core.game.system.communication.CommunicationInfo
import core.game.world.map.RegionManager
import core.game.world.map.build.DynamicRegion
import core.game.world.repository.Repository
import core.plugin.Initializable
import core.tools.Log
import core.tools.StringUtils
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import shared.consts.Components
import java.awt.HeadlessException
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection

@Initializable
class MiscCommandSet : CommandSet(Privilege.ADMIN) {

    override fun defineCommands() {

        /*
         * Command for fully restoring the player's health.
         */

        define(
            name = "heal",
            privilege = Privilege.ADMIN,
            usage = "::heal",
            description = "Full restore health.",
        ) { player, _ ->
            player.fullRestore()
        }

        /*
         * Command for toggling debug mode on or off for the player.
         */
        define(
            name = "debug",
            privilege = Privilege.ADMIN,
            usage = "::debug",
            description = "Toggles debug mode.",
        ) { player, _ ->
            player.toggleDebug()
        }

        /*
         * Command for calculating your chance to hit a NPC
         *  and printing debug info.
         */

        define(
            name = "calc_accuracy",
            privilege = Privilege.ADMIN,
            usage = "::calc_accuracy <lt>NPC ID<gt>",
            description = "Calculates and prints your current chance to hit a given NPC.",
        ) { player, args ->
            val handler = player.getSwingHandler(false)
            sendMessage(player, "handler type: ${handler.type}")
            sendMessage(player, "calculateAccuracy: ${handler.calculateAccuracy(player)}")

            if (args.size > 1) {
                val npcId: Int = args[1].toInt()
                val npc = NPC(npcId)
                npc.initConfig()
                sendMessage(player, "npc: ${npc.name}. npc defence: ${npc.skills.getLevel(Skills.DEFENCE)}")
                sendMessage(player, "calculateDefence: ${handler.calculateDefence(npc, player)}")
            }
        }

        /*
         * Command for printing detailed information about the
         * player's current location, including coordinates
         * and objects.
         */

        define(
            name = "loc",
            privilege = Privilege.STANDARD,
            usage = "::loc",
            description = "Prints quite a lot of information about your current location."
        ){ player, _->
            val l = player.location
            val r = player.viewport.region
            var obj: Scenery? = null
            notify(player,"Absolute: " + l + ", regional: [" + l.localX + ", " + l.localY + "], chunk: [" + l.chunkOffsetX + ", " + l.chunkOffsetY + "], flag: [" + RegionManager.isTeleportPermitted(l) + ", " + RegionManager.getClippingFlag(l) + ", " + RegionManager.isLandscape(l) + "].")
            notify(player,"Region: [id=" + l.regionId + ", active=" + r?.isActive + ", instanced=" + (r is DynamicRegion) + "], obj=" + RegionManager.getObject(l) + ".")
            notify(player, "Jagex: ${l.z}_${l.regionId shr 8}_${l.regionId and 0xFF}_${l.localX}_${l.localY}")
            notify(player,"Object: " + RegionManager.getObject(l).also{obj = it} + ".")
            notify(player,"Object Varp: " + obj?.definition?.configFile?.varpId + " offset: " + obj?.definition?.configFile?.startBit + " size: " + (obj?.definition?.configFile?.startBit?.minus(obj?.definition?.configFile?.startBit!!)))
            log(this::class.java, Log.FINE,  "Viewport: " + l.getSceneX(player.playerFlags.lastSceneGraph) + "," + l.getSceneY(player.playerFlags.lastSceneGraph))
            val loc = "Location.create(" + l.x + ", " + l.y + ", " + l.z + ")"
            log(this::class.java, Log.FINE,  loc + "; " + player.playerFlags.lastSceneGraph + ", " + l.localX + ", " + l.localY)
            try {
                val stringSelection = StringSelection(loc)
                val clpbrd = Toolkit.getDefaultToolkit().systemClipboard
                clpbrd.setContents(stringSelection, null)
                notify(player, "Coordinates copied to clipboard.")
            } catch (e: HeadlessException) {
                reject(player, "NOTE: Paste will not be available due to remote server.")
            }
        }

        /*
         * Command for calculating and displaying
         *  the player's current maximum hit.
         */

        define(
            name = "calcmaxhit",
            privilege = Privilege.ADMIN,
            usage = "::calcmaxhit",
            description = "Calculates and shows you your current max hit.",
        ) { player, _ ->
            val swingHandler = player.getSwingHandler(false)
            val hit = swingHandler.calculateHit(player, player, 1.0)
            notify(player, "max hit (${(swingHandler as Scenery).javaClass.getName()}): $hit")
        }

        /*
         * Command for emptying the player inventory completely.
         */

        define(
            name = "empty",
            privilege = Privilege.ADMIN,
            usage = "empty",
            description = "Empties your inventory.",
        ) { player, _ ->
            player.inventory.clear()
            player.inventory.refresh()
        }

        /*
         * Command for sending a server-wide news
         * message with the provided text.
         */

        define(
            name = "news",
            privilege = Privilege.ADMIN,
            usage = "::news <lt>String<gt>",
            description = "Sends the given string as a News message.",
        ) { _, args ->
            val message = args.slice(1 until args.size).joinToString(" ")
            sendNews(message)
        }

        /*
         * Command for displaying a list of online players.
         */

        define(
            name = "players",
            privilege = Privilege.MODERATOR,
            usage = "::players",
            description = "Lists the online players.",
        ) { player, _ ->
            val rights = player.rights.ordinal
            if (player.interfaceManager.isOpened() &&
                player.interfaceManager.opened?.id != Components.QUESTJOURNAL_SCROLL_275 ||
                player.locks.isMovementLocked() ||
                player.locks.isTeleportLocked()
            ) {
                reject(player, "Please finish what you're doing first.")
            }
            openInterface(player, Components.QUESTJOURNAL_SCROLL_275)
            var i = 0
            while (i < 257) {
                sendString(player, "", 275, i)
                i++
            }
            val red = "<col=8A0808>"
            sendString(player, "<col=8A0808>" + "Players" + "</col>", 275, 2)
            var lineStart = 11
            for (p in Repository.players) {
                if (!p.isArtificial) {
                    sendString(
                        player,
                        red + "<img=" + (Rights.getChatIcon(p) - 1) + ">" + p.username +
                                if (rights >
                                    0
                                ) {
                                    " [ip=" + p.details.ipAddress + ", name=" + p.details.compName + "]"
                                } else {
                                    ""
                                },
                        275,
                        lineStart++,
                    )
                }
            }
        }

        /*
         * Command for printing debug information
         * about a specific bot.
         */

        define(
            name = "botinfo",
            privilege = Privilege.ADMIN,
            usage = "::botinfo <lt>botname<gt>",
            description = "Prints debug information about a bot",
        ) { player, args ->
            val scriptInstances = AIRepository.PulseRepository

            val botName = args.slice(1 until args.size).joinToString(" ").lowercase()
            val bot = scriptInstances[botName]
            if (bot == null) {
                reject(player, "No bot with that name found.")
                return@define
            }
            val botInfo = bot.botScript.toString()
            // Print the bot's information, max 80chars per line.
            botInfo.chunked(80).forEach { notify(player, it) }
        }

        /*
         * Command for accessing the Grand Exchange
         * book with different modes (buying, selling, search,
         * bots, botsearch).
         */

        define(
            name = "ge",
            privilege = Privilege.ADMIN,
            usage = "::ge <lt>MODE<gt> (Modes: buying, selling, search, bots, botsearch)",
            description = "Various commands for viewing GE offers.",
        ) { player, args ->
            if (args.size < 2) {
                reject(player, "Usage: ::ge mode", "Available modes: buying, selling, search, bots, botsearch")
            }

            val mode = args[1]
            when (mode) {
                "buying" -> showGeBuy(player)
                "selling" -> showGeSell(player)
                "search" -> showGeInputDialogue(player, args, ::showOffers)
                "bots" -> showGeBots(player)
                "botsearch" -> showGeInputDialogue(player, args, ::showGeBotsearch)
                else -> reject(player, "Invalid mode used. Available modes are: buying, selling, search")
            }
        }

        define(
            name = "commandbook",
            privilege = Privilege.ADMIN,
            usage = "commandbook",
            description = "Shows commands in a book (only commands)"
        ) { player, args ->

            val requestedPage = if (args.size > 1) (args[1].toIntOrNull() ?: 1) - 1 else 0
            val pages = CommandMapping.getPageIndices(player.rights.ordinal)
            val pageCount = pages.size
            val page = requestedPage.coerceIn(0, pageCount - 1)
            val startIndex = pages[page]
            val endIndex = if (page < pageCount - 1) pages[page + 1] else CommandMapping.getCommands().size

            val rawLines = ArrayList<String>()

            for (i in startIndex until endIndex) {
                val command = CommandMapping.getCommands()[i]
                if (command.privilege.ordinal > player.rights.ordinal) continue
                rawLines.add("::${command.name}")
            }

            fun splitLine(line: String, maxLen: Int = 25): List<String> {
                val result = ArrayList<String>()
                var start = 0
                while (start < line.length) {
                    val end = (start + maxLen).coerceAtMost(line.length)
                    result.add(line.substring(start, end))
                    start = end
                }
                return result
            }

            val splitLines = rawLines.flatMap { splitLine(it, 25) }

            val linesPerSide = 15
            val leftLines = ArrayList<String>()
            val rightLines = ArrayList<String>()
            val chunks = splitLines.chunked(linesPerSide * 2)

            for (chunk in chunks) {
                val leftChunk = chunk.take(linesPerSide)
                val rightChunk = if (chunk.size > linesPerSide) chunk.drop(linesPerSide) else List(linesPerSide) { "" }

                for (i in 0 until linesPerSide) {
                    leftLines.add(leftChunk.getOrElse(i) { "" })
                    rightLines.add(rightChunk.getOrElse(i) { "" })
                }
            }

            showGeBook(
                player,
                "Commands${if (pageCount > 1) " (${page + 1}/$pageCount)" else ""}",
                leftLines,
                rightLines
            )
        }

        /*
         * Command for listing all commands.
         */

        define(
            name = "commands",
            privilege = Privilege.ADMIN,
            usage = "::commands <lt>page<gt>",
            description = "Lists all the commands.",
        ) { player, args ->
            val page = if (args.size > 1) (args[1].toIntOrNull() ?: 1) - 1 else 0
            var lineid = 11
            var pages = CommandMapping.getPageIndices(player.rights.ordinal)
            var end = if (page < (pages.size - 1)) pages[page + 1] else CommandMapping.getCommands().size

            player.interfaceManager.close()

            if (page < 0) {
                reject(player, "Usage: ::commands <lt>page<gt>")
            }

            if (page > pages.size) {
                reject(player, "That page number is too high, you don't have access to that many.")
            }

            for (i in 0..310) {
                sendString(player, "", Components.QUESTJOURNAL_SCROLL_275, i)
            }

            sendString(player, "Commands" + if (pages.size > 1) { " (${page + 1}/${pages.size})" } else { "" }, Components.QUESTJOURNAL_SCROLL_275, 2,)

            for (i in pages[page] until end) {
                val command = CommandMapping.getCommands()[i]
                val rights = command.privilege.ordinal
                val icon = rights - 1

                if (rights > player.rights.ordinal) continue

                val title = buildString {
                    if (rights > 0) append("(<img=$icon>) ")
                    append("<col=ffb000>::${command.name}</col>")
                }

                sendString(player, title, Components.QUESTJOURNAL_SCROLL_275, lineid++)

                if (command.usage.isNotEmpty()) {
                    sendString(player, "<col=454545> Usage:</col> <img=3> <col=ffffff>${command.usage}</col>", Components.QUESTJOURNAL_SCROLL_275, lineid++)
                }

                if (command.description.isNotEmpty()) {
                    sendString(player, "<col=454545> Info:</col> <img=2> <col=ffffff>${command.description}</col>", Components.QUESTJOURNAL_SCROLL_275, lineid++)
                }

                sendString(player, "", Components.QUESTJOURNAL_SCROLL_275, lineid++)

                if (lineid > 306) {
                    sendString(player, "<col=ff0000>To view the next page, use ::commands ${page + 2}</col>", Components.QUESTJOURNAL_SCROLL_275, lineid)
                    break
                }
            }
            openInterface(player, Components.QUESTJOURNAL_SCROLL_275)
        }

        /*
         * Command for replying to your last received DM.
         */

        define(
            name = "reply",
            privilege = Privilege.ADMIN,
            usage = "::reply",
            description = "Opens a reply prompt to your last DM. Same as pressing tab.",
        ) { player, _ ->
            if (player.interfaceManager.isOpened()) {
                reject(player, "<col=e74c3c>Please finish what you're doing first.")
            }
            if (player.attributes.containsKey("replyTo")) {
                player.setAttribute("keepDialogueAlive", true)
                val replyTo = player.getAttribute("replyTo", "").replace("_".toRegex(), " ")
                sendInputDialogue(player, InputType.MESSAGE, StringUtils.formatDisplayName(replyTo)) { value ->
                    CommunicationInfo.sendMessage(player, replyTo.lowercase(), value as String)
                    player.removeAttribute("keepDialogueAlive")
                }
            } else {
                reject(player, "<col=3498db>You have not received any recent messages to which you can reply.")
            }
        }

        /*
         * Command for closing the currently
         * opened interface.
         */

        define(
            name = "xface",
            privilege = Privilege.ADMIN,
            usage = "::xface",
            description = "Closes the currently opened interface/dialogue.",
        ) { player, _ ->
            closeAllInterfaces(player)
        }

        /*
         * Command for setting all player skills to level 99.
         */

        define(
            name = "max",
            privilege = Privilege.ADMIN,
            usage = "::max",
            description = "Gives you all 99s."
        ) { player, _ ->
            var index = 0
            Skills.SKILL_NAME.forEach { _ ->
                player.skills.setStaticLevel(index, 99)
                player.skills.setLevel(index, 99)
                index++
            }
            player.skills.updateCombatLevel()
        }

        /*
         * Command for resetting player stats to default levels.
         */

        define(
            name = "noobme",
            privilege = Privilege.ADMIN,
            usage = "::noobme",
            description = "Sets you back to default stats.",
        ) { player, _ ->
            var index = 0
            Skills.SKILL_NAME.forEach { _ ->
                if (index == Skills.HITPOINTS) {
                    player.skills.setStaticLevel(index, 10)
                    player.skills.setLevel(index, 10)
                    index++
                } else {
                    player.skills.setStaticLevel(index, 1)
                    player.skills.setLevel(index, 1)
                    index++
                }
            }
            player.skills.updateCombatLevel()
        }

        /*
         * Command for setting a skill level
         * for yourself or another player.
         */

        define(
            name = "setlevel",
            privilege = Privilege.ADMIN,
            usage = "::setlevel <lt>SKILL NAME<gt> <lt>LEVEL<gt> <lt>PLAYER<gt>",
            description = "Sets skill level to player.",
        ) { player, args ->
            if (args.size < 3) reject(player, "Usage: ::setlevel skillname level")
            val skillname = args[1]
            val desiredLevel: Int? = args[2].toIntOrNull()
            if (desiredLevel == null) {
                reject(player, "Level must be an integer.")
            }
            if (desiredLevel!! > 99) reject(player, "Level must be 99 or lower.")
            val skill = Skills.getSkillByName(skillname)
            if (skill < 0) reject(player, "Must use a valid skill name!")
            var target = player
            if (args.size > 3) {
                val n = args.slice(3 until args.size).joinToString("_")
                val foundtarget = Repository.getPlayerByName(n)
                if (foundtarget == null) {
                    reject(player, "Invalid player \"${n}\" or player not online")
                }
                target = foundtarget!!
            }
            target.skills.setStaticLevel(skill, desiredLevel)
            target.skills.setLevel(skill, desiredLevel)
            target.skills.updateCombatLevel()
        }

        /*
         * Command for adding experience points to a skill.
         */

        define(
            name = "addxp",
            privilege = Privilege.ADMIN,
            usage = "::addxp <lt>skill name | id<gt> <lt>xp<gt>",
            description = "Add xp to skill",
        ) { player, args ->
            if (args.size != 3) reject(player, "Usage: ::addxp <lt>skill name | id<gt> <lt>xp<gt>")

            val skill = args[1].toIntOrNull() ?: Skills.getSkillByName(args[1])
            if (skill < 0 || skill >= Skills.NUM_SKILLS) reject(player, "Must use valid skill name or id.")

            val xp = args[2].toDoubleOrNull()
            if (xp == null || xp <= 0) reject(player, "Xp must be a positive number.")

            player.skills.addExperience(skill, xp!!)
        }

        /*
         * Command for completing all achievement diaries.
         */

        define(
            name = "completediaries",
            privilege = Privilege.ADMIN,
            usage = "::completediaries",
            description = "Completes all diaries."
        ) { player, _ ->
            player.achievementDiaryManager.diaries.forEach { diary ->
                diary.taskCompleted.forEachIndexed { levelIndex, tasks ->
                    tasks.indices.reversed().forEach { taskIndex ->
                        diary.finishTask(player, levelIndex, taskIndex)
                    }
                }
            }
        }

        /*
         * Command for logging the current player location.
         */

        define(name = "log", privilege = Privilege.ADMIN) { player, _ ->
            var log: ArrayList<String>? = player.getAttribute("loc-log")
            log = log ?: ArrayList<String>()
            val locString = "{${player.location.x},${player.location.y},${player.location.z},1,0}"
            log.add(locString)
            player.setAttribute("loc-log", log)
        }

        /*
         * Command for exporting logged locations to the clipboard.
         */

        define(
            name = "logdone",
            privilege = Privilege.ADMIN
        ) { player, _ ->
            val log: ArrayList<String>? = player.getAttribute("loc-log")
            log ?: return@define

            val sb = StringBuilder()
            var first = true
            for (entry in log) {
                if (!first) sb.append("-")
                sb.append(entry)
                first = false
            }

            val stringSelection = StringSelection(sb.toString())
            val clpbrd = Toolkit.getDefaultToolkit().systemClipboard
            clpbrd.setContents(stringSelection, null)

            log.clear()
            player.setAttribute("loc-log", log)
        }

        /*
         * Command for rolling Trawler
         * minigame loot and opening the reward interface.
         */

        define(
            name = "rolltrawlerloot",
            privilege = Privilege.ADMIN,
            usage = "::rolltrawlerloot <lt>ROLL COUNT<gt>",
            description = "Rolls some trawler loot and opens the reward interface."
        ) { player, args ->
            val rolls = if (args.size < 2) 100 else args[1].toIntOrNull() ?: 100
            player.setAttribute("/save:ft-rolls", rolls)
            val container = FishingTrawlerContainer(player)
            container.open()
            sendMessage(player, "Opened Fishing Trawler reward interface with $rolls rolls.")
        }

        /*
         * Command for filling the bank with one of each item.
         */

        define(
            name = "fillbank",
            privilege = Privilege.ADMIN,
            usage = "::fillbank",
            description = "Right as it says on the tin.",
        ) { player, _ ->
            for (i in 0 until ServerConstants.BANK_SIZE) {
                player.bank.add(Item(i))
            }
        }

        /*
         * Command for clearing all items from the player bank.
         */

        define(
            name = "emptybank",
            privilege = Privilege.ADMIN,
            usage = "::emptybank",
            description = "Clearing all items from the bank",
        ) { player, _ ->
            player.bank.clear()
            player.bank.update()
        }

        /*
         * Command for setting a Varp config.
         */

        define(
            name = "setconfig",
            privilege = Privilege.ADMIN,
            usage = "setconfig",
            description = "DEPRECATED: Use setvarp or setvarbit.",
        ) { player, args ->
            if (args.size < 3) {
                reject(player, "Syntax: ::setconfig configID value")
            }
            val configID = args[1].toInt()
            val configValue = args[2].toInt()
            setVarp(player, configID, configValue)
        }

        /*
         * Command for retrieving the varp
         * associated with a scenery object.
         */

        define(name = "getobjectvarp",
            privilege = Privilege.ADMIN,
            usage = "getobjectvarp",
            description = "Get varp for scenery object."
        ) { player, args ->
            if (args.size < 2) {
                reject(player, "Syntax: ::getobjectvarp objectid")
            }
            val objectID = args[1].toInt()
            notify(player, "${VarbitDefinition.forSceneryId(SceneryDefinition.forId(objectID).varbitID).varpId}")
        }

        /*
         * Command for printing information
         * about a varbit.
         */

        define(
            name = "define_varbit",
            privilege = Privilege.ADMIN,
            usage = "::define_varbit <lt>VARBIT ID<gt>",
            description = "Prints information about the given varbit.",
        ) { player, args ->
            if (args.size < 2) {
                reject(player, "Syntax: ::define_varbit varbitId")
            }
            val varbitID = args[1].toInt()
            notify(player, "$varbitID: ${VarbitDefinition.forId(varbitID)}")
        }

        /*
         * Command for setting a single varbit
         * to a value.
         */

        define(
            name = "setvarbit",
            privilege = Privilege.ADMIN,
            usage = "::setvarbit <lt>VARBIT ID<gt> <lt>VALUE<gt>",
            description = "Setting a single varbit.",
        ) { player, args ->
            if (args.size != 3) {
                reject(player, "Usage: ::setvarbit varbit value")
            }
            val index = args[1].toIntOrNull()
            val value = args[2].toIntOrNull()

            if (index == null || value == null) {
                reject(player, "Usage ::setvarbit index offset value")
            }

            setVarbit(player, index!!, value!!)
        }

        /*
         * Command for setting a range of varbits to the same value.
         */

        define(
            name = "setvarbits",
            privilege = Privilege.ADMIN,
            usage = "::setvarbits <lt>FROM VARBIT ID<gt> <lt>TO VARBIT ID<gt> <lt>VALUE<gt>",
            description = "Sets a range of varbits to the same value.",
        ) { player, args ->
            if (args.size != 4) {
                reject(player, "Usage: ::setvarbits fromvarbit tovarbit value")
            }
            val fromIndex = args[1].toIntOrNull()!!
            val toIndex = args[2].toIntOrNull()!!
            val value = args[3].toIntOrNull()!!

            for (index in fromIndex..toIndex) {
                setVarbit(player, index, value)
            }
        }

        /*
         * Command for getting the current value of a varbit.
         */

        define(
            name = "getvarbit",
            privilege = Privilege.ADMIN,
            usage = "::getvarbit <lt>VARBIT ID<gt>",
            description = "Gets the current value of a specified varbit.",
        ) { player, args ->
            if (args.size != 2) {
                reject(player, "Usage: ::getvarbit id")
            }
            val index = args[1].toIntOrNull() ?: return@define
            notify(player, "Varbit $index: Currently ${getVarbit(player, index)}")
        }

        /*
         * Command for setting the value of a varp starting at a bit offset.
         */

        define(
            name = "setvarp",
            privilege = Privilege.ADMIN,
            usage = "::setvarp <lt>VARP ID<gt> <lt>BIT OFFSET<gt> <lt>VALUE<gt>",
            description = "Sets the value starting at the bit offset of the varp.",
        ) { player, args ->
            if (args.size < 4) {
                reject(player, "Usage: ::setvarp index offset value")
            }
            val index = args[1].toIntOrNull()
            val offset = args[2].toIntOrNull()
            val value = args[3].toIntOrNull()

            if (index == null || offset == null || value == null) {
                reject(player, "Usage ::setvarp index offset value")
            }

            setVarp(player, index!!, value!! shl offset!!)
        }

        /*
         * Command for setting a varc value.
         */

        define(
            name = "setvarc",
            privilege = Privilege.ADMIN,
            usage = "::setvarc <lt>VARC ID<gt> <lt>VALUE<gt>",
            description = "Sets the value of a specific varc for the player.",
        ) { player, args ->
            if (args.size < 3) {
                reject(player, "Usage: ::setvarc index value")
            }
            val index = args[1].toShortOrNull()
            val value = args[2].toIntOrNull()

            if (index == null || value == null) {
                reject(player, "Usage ::setvarc index value")
            }

            player.packetDispatch.sendVarcUpdate(index!!, value!!)
        }

        /*
         * Command for instantly growing all planted crops by one stage.
         */

        define(
            name = "grow",
            privilege = Privilege.ADMIN,
            usage = "::grow",
            description = "Grows all planted crops by 1 stage.",
        ) { player, _ ->
            val state = getOrStartTimer<CropGrowth>(player)

            for (patch in state.getPatches()) {
                patch.nextGrowth = System.currentTimeMillis() - 1
            }

            state.run(player)
        }

        /*
         * Command for finishing any in-progress compost bins.
         */
        define(
            name = "finishbins",
            privilege = Privilege.ADMIN,
            usage = "::finishbins",
            description = "Finishes any in-progress compost bins.",
        ) { player, _ ->
            val bins = getOrStartTimer<Compost>(player).getBins()
            for (bin in bins) {
                if (!bin.isFinished && bin.isClosed) bin.finish()
            }
        }

        /*
         * Command for resetting all compost bins to their initial states.
         */

        define(
            name = "resetbins",
            privilege = Privilege.ADMIN,
            usage = "::resetbins",
            description = "Resets the player's compost bins to their initial states.",
        ) { player, _ ->
            val bins = getOrStartTimer<Compost>(player).getBins()
            for (bin in bins) bin.reset()
        }

        /*
         * Command for diseasing all crops instantly.
         */

        define(
            name = "diseasecrops",
            privilege = Privilege.ADMIN,
            usage = "::diseasecrops",
            description = "Disease all crops.",
        ) { player, _ ->
            val state = getOrStartTimer<CropGrowth>(player)
            for (patch in state.getPatches()) {
                patch.diseaseMod = -128
                patch.nextGrowth = System.currentTimeMillis() + 1
            }
            state.run(player)
        }

        /*
         * Command for adding 100 credits to the player.
         */

        define(name = "addcredits", privilege = Privilege.ADMIN) { player, _ ->
            player.details.credits += 100
        }

        /*
         * Command for finding the parent NPC of a given NPC id.
         */

        define(name = "getnpcparent") { player, args ->
            if (args.size < 2) {
                reject(player, "Usage: ::getnpcparent npcid")
            }

            val npcid = args[1].toIntOrNull() ?: reject(player, "Invalid NPC ID.")

            GlobalScope.launch {
                for (def in NPCDefinition.getDefinitions().values) {
                    def ?: continue
                    def.childNPCIds ?: continue
                    for (id in def.childNPCIds!!) {
                        if (id == npcid) {
                            notify(player, "Parent NPC: ${def.id}")
                            return@launch
                        }
                    }
                }
                notify(player, "No parent NPC found.")
            }
        }

        /*
         * Command for toggling infinite special attack for the player.
         */

        define(
            name = "infinitespecial",
            privilege = Privilege.ADMIN,
            usage = "infinitespecial true | false",
            description = "Toggling infinite special",
        ) { player, args ->
            val usageStr = "Usage: ::infinitespecial true|false"
            if (args.size < 2) {
                reject(player, usageStr)
            }
            when (args[1]) {
                "true" -> player.setAttribute("infinite-special", true)
                "false" -> player.removeAttribute("infinite-special")
                else -> reject(player, usageStr)
            }
        }

        /*
         * Command for toggling NPC aggression (aggro) on the player.
         */
        define(
            name = "aggro",
            privilege = Privilege.ADMIN,
            usage = "aggro true | false",
            description = "Toggle NPCs aggroing on you",
        ) { player, args ->
            val usageStr = "Usage: ::agro true | false"
            if (args.size < 2) {
                notify(
                    player,
                    "Allow admin aggression is currently ${player.getAttribute("/save:allow_admin_aggression", false)}",
                )
                return@define
            }
            when (args[1]) {
                "true" -> player.setAttribute("/save:allow_admin_aggression", true)
                "false" -> player.setAttribute("/save:allow_admin_aggression", false)
                else -> reject(player, usageStr)
            }
            notify(player, "Setting aggro=${args[1]}")
        }
    }

    private fun showGeBotsearch(player: Player, searchTerm: String) {
        val offerAmounts = HashMap<Int, Int>()
        val offerPrice = HashMap<Int, Int>()

        val offers =
            GrandExchange.getBotOffers().filter {
                getItemName(it.itemID).contains(searchTerm, true) ||
                    getItemName(it.itemID).equals(
                        searchTerm,
                        true,
                    )
            }

        for (offer in offers) {
            offerAmounts[offer.itemID] = offer.amount
            offerPrice[offer.itemID] = offer.offeredValue
        }

        val entries = offerAmounts.entries.sortedBy({ e -> getItemName(e.key) })
        val leftLines = ArrayList<String>(entries.size)
        val rightLines = ArrayList<String>(entries.size)
        for (entry in entries) {
            leftLines.add("${getItemName(entry.key)} (<col=357f44>x${entry.value}</col>)")
            rightLines.add("Price: <col=3a3414>${offerPrice[entry.key]}</col>gp")
        }
        showGeBook(player, "Bot Stock - \"$searchTerm\"", leftLines, rightLines)
    }

    private fun showGeBots(player: Player) {
        val offerAmounts = HashMap<Int, Int>()
        val offerPrice = HashMap<Int, Int>()

        val offers = GrandExchange.getBotOffers()

        for (offer in offers) {
            offerAmounts[offer.itemID] = offer.amount
            offerPrice[offer.itemID] = offer.offeredValue
        }

        val entries = offerAmounts.entries.sortedBy({ e -> getItemName(e.key) })
        val leftLines = ArrayList<String>(entries.size)
        val rightLines = ArrayList<String>(entries.size)
        for (entry in entries) {
            leftLines.add("${getItemName(entry.key)} (<col=357f44>x${entry.value}</col>)")
            rightLines.add("Price: <col=3a3414>${offerPrice[entry.key]}</col>gp")
        }
        showGeBook(player, "Bot Stock", leftLines, rightLines)
    }

    private fun showGeSell(player: Player) {
        val offerAmounts = HashMap<Int, Int>()
        val lowestPrice = HashMap<Int, Int>()

        val offers = GrandExchange.getValidOffers()

        for (offer in offers) {
            if (!offer.sell) continue
            var amount = offerAmounts[offer.itemID] ?: 0
            amount += offer.amountLeft

            var price = lowestPrice[offer.itemID] ?: Integer.MAX_VALUE
            if (offer.offeredValue < price) price = offer.offeredValue

            offerAmounts[offer.itemID] = amount
            lowestPrice[offer.itemID] = price
        }

        val entries = offerAmounts.entries.sortedBy({ e -> getItemName(e.key) })
        val leftLines = ArrayList<String>(entries.size)
        val rightLines = ArrayList<String>(entries.size)
        for (entry in entries) {
            leftLines.add("${getItemName(entry.key)} (<col=357f44>x${entry.value}</col>)")
            rightLines.add("Price: <col=3a3414>${lowestPrice[entry.key]}</col>gp")
        }
        showGeBook(player, "Active Sell Offers", leftLines, rightLines)
    }

    private fun showGeBuy(player: Player) {
        val offerAmounts = HashMap<Int, Int>()
        val highestPrice = HashMap<Int, Int>()

        val offers = GrandExchange.getValidOffers()

        for (offer in offers) {
            if (offer.sell) continue
            var amount = offerAmounts[offer.itemID] ?: 0
            amount += offer.amountLeft

            var price = highestPrice[offer.itemID] ?: 0
            if (offer.offeredValue > price) price = offer.offeredValue

            offerAmounts[offer.itemID] = amount
            highestPrice[offer.itemID] = price
        }

        val entries = offerAmounts.entries.sortedBy({ e -> getItemName(e.key) })
        val leftLines = ArrayList<String>(entries.size)
        val rightLines = ArrayList<String>(entries.size)
        for (entry in entries) {
            leftLines.add("${getItemName(entry.key)} (<col=357f44>x${entry.value}</col>)")
            rightLines.add("Highest: <col=3a3414>${highestPrice[entry.key]}</col>gp")
        }
        showGeBook(player, "Active Buy Offers", leftLines, rightLines)
    }

    private fun showOffers(player: Player, searchTerm: String) {
        val offers =
            GrandExchange.getValidOffers().filter {
                getItemName(it.itemID).contains(searchTerm, true) ||
                    getItemName(it.itemID).equals(
                        searchTerm,
                        true,
                    )
            }
        if (offers.isEmpty()) {
            sendMessage(player, "No results.")
            return
        }

        val buyingAmount = HashMap<Int, Int>()
        val buyingHighest = HashMap<Int, Int>()
        val sellingAmount = HashMap<Int, Int>()
        val sellingLowest = HashMap<Int, Int>()
        for (offer in offers) {
            if (offer.sell) {
                var price = sellingLowest[offer.itemID] ?: Int.MAX_VALUE
                if (offer.offeredValue < price) price = offer.offeredValue

                var amount = sellingAmount[offer.itemID] ?: 0
                amount += offer.amountLeft

                sellingAmount[offer.itemID] = amount
                sellingLowest[offer.itemID] = price
            } else {
                var price = buyingHighest[offer.itemID] ?: 0
                if (offer.offeredValue > price) price = offer.offeredValue

                var amount = buyingAmount[offer.itemID] ?: 0
                amount += offer.amountLeft

                buyingAmount[offer.itemID] = amount
                buyingHighest[offer.itemID] = price
            }
        }

        val numLines = offers.size + 1
        val leftLines = ArrayList<String>(numLines)
        val rightLines = ArrayList<String>(numLines)
        for (i in 0..numLines) {
            if (i < buyingAmount.keys.size) {
                val offer = buyingAmount.entries.elementAtOrNull(i) ?: continue
                leftLines.add("[BUYING] ${getItemName(offer.key)} (<col=357f44>x${offer.value}</col>)")
                rightLines.add("Highest: <col=3a3414>${buyingHighest[offer.key]}</col>gp")
            } else if (i == buyingAmount.keys.size) {
                leftLines.add("")
                rightLines.add("")
            } else {
                val offer = sellingAmount.entries.elementAtOrNull(i - buyingAmount.keys.size - 1) ?: continue
                leftLines.add("[SELLING] ${getItemName(offer.key)} (<col=357f44>x${offer.value}</col>)")
                rightLines.add("Lowest: <col=3a3414>${sellingLowest[offer.key]}</col>gp")
            }
        }
        showGeBook(player, "Results for \"$searchTerm\"", leftLines, rightLines)
    }

    private fun showGeInputDialogue(player: Player, args: Array<String>, op: (Player, String) -> (Unit)) {
        if (args.size > 2) {
            val target = args.copyOfRange(2, args.size).joinToString(" ").lowercase()
            op(player, target)
        } else {
            sendInputDialogue(player, InputType.STRING_LONG, "Enter search term:") { value ->
                op(player, value as String)
            }
        }
    }

    private fun showGeBook(player: Player, title: String, leftLines: ArrayList<String>, rightLines: ArrayList<String>) {
        if (leftLines.size == 0) {
            sendMessage(player, "No results.")
            return
        }
        val lineIds = BookInterface.FANCY_BOOK_26_LINE_IDS
        val contents = ArrayList<PageSet>()
        val leftChunks = leftLines.chunked(15)
        val rightChunks = rightLines.chunked(15)
        for (i in leftChunks.indices) {
            val size = leftChunks[i].size
            val leftPageLines = ArrayList<BookLine>(size)
            val rightPageLines = ArrayList<BookLine>(size)
            for (j in leftChunks[i].indices) {
                leftPageLines.add(BookLine(leftChunks[i][j], lineIds[j + 3]))
                // +3 to skip title and buttons
                rightPageLines.add(BookLine(rightChunks[i][j], lineIds[j + 3 + 15]))
                // +15 because right pages have different ids
            }
            val leftPage = Page(*leftPageLines.toTypedArray())
            val rightPage = Page(*rightPageLines.toTypedArray())
            contents.add(PageSet(leftPage, rightPage))
        }

        closeInterface(player)

        fun display(player: Player, pageNum: Int, buttonID: Int): Boolean {
            BookInterface.pageSetup(player, BookInterface.FANCY_BOOK_26, title, contents.toTypedArray())
            return true
        }
        BookInterface.openBook(player, BookInterface.FANCY_BOOK_26, ::display)
    }
}
