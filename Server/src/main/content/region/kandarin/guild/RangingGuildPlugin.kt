package content.region.kandarin.guild

import content.global.skill.crafting.TanningProduct
import core.api.*
import core.cache.def.impl.SceneryDefinition
import core.game.component.Component
import core.game.container.access.InterfaceContainer.generateItems
import core.game.container.impl.EquipmentContainer
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.global.Skillcape.isMaster
import core.game.global.Skillcape.purchase
import core.game.global.action.ClimbActionHandler.climb
import core.game.global.action.DoorActionHandler
import core.game.global.action.DoorActionHandler.handleAutowalkDoor
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.InterfaceListener
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.combat.equipment.Ammunition
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.node.scenery.Scenery
import core.game.system.task.Pulse
import core.game.world.map.Location
import core.game.world.map.RegionManager
import core.game.world.map.RegionManager.getLocalNpcs
import core.game.world.map.zone.ZoneBorders
import core.game.world.update.flag.context.Animation
import core.plugin.ClassScanner
import core.plugin.Initializable
import core.plugin.Plugin
import core.tools.END_DIALOGUE
import core.tools.RandomFunction
import shared.consts.*
import kotlin.math.floor
import kotlin.math.max

class RangingGuildPlugin : InteractionListener, InterfaceListener, MapArea {

    override fun defineAreaBorders(): Array<ZoneBorders> {
        return arrayOf(ZoneBorders.forRegion(Regions.RANGING_GUILD_10549))
    }

    override fun areaEnter(entity: Entity) {
        if (entity !is Player) return

        val player = entity
        val players = RegionManager.getLocalPlayers(player)

        val inAreaPlayers = players.filter { p ->
            defineAreaBorders().any { zone -> inBorders(p, zone) }
        }

        if (inAreaPlayers.size > 1) {
            return
        }

        getLocalNpcs(Location.create(2668, 3427, 2)).forEach { n ->
            towerDirections[n.id]?.let { dir ->
                sendChat(n, "The $dir tower is occupied, get them!")
            }
        }
    }

    companion object {
        private val towerDirections = mapOf(
            NPCs.TOWER_ADVISOR_684 to "north",
            NPCs.TOWER_ADVISOR_685 to "east",
            NPCs.TOWER_ADVISOR_686 to "south",
            NPCs.TOWER_ADVISOR_687 to "west"
        )
    }

    override fun defineListeners() {

        /*
         * Handles conversation with the Ranging Guild Doorman.
         */

        on(NPCs.RANGING_GUILD_DOORMAN_679, IntType.NPC, "talk-to") { player, node ->
            sendPlayerDialogue(player, "Hello there.")
            addDialogueAction(player) { _, _ ->
                sendNPCDialogueLines(
                    player,
                    node.id,
                    FaceAnim.HALF_GUILTY,
                    false,
                    "Greetings. If you are an experienced archer, you may",
                    "want to visit the guild here...",
                )

            }
            return@on true
        }

        /*
         * Handles dialogue with a Ranging Guild Guard.
         */

        on(NPCs.GUARD_678, IntType.NPC, "talk-to") { player, node ->
            sendPlayerDialogue(player, "Hello there.")
            addDialogueAction(player) { _, _ ->
                sendNPCDialogueLines(
                    player,
                    node.id,
                    FaceAnim.HALF_GUILTY,
                    false,
                    "Greetings, traveller. Enjoy the time at the Ranging", "Guild.",
                )

            }
            return@on true
        }

        /*
         * Handles "fire-at" interaction with the archery target.
         */
        on(shared.consts.Scenery.TARGET_2513, IntType.SCENERY, "fire-at") { player, node ->
            if (player.archeryTargets <= 0) {
                sendNPCDialogueLines(
                    player,
                    NPCs.COMPETITION_JUDGE_693,
                    FaceAnim.HALF_GUILTY,
                    false,
                    "Sorry, you may only use the targets for the",
                    "competition, not for practicing.",
                )
                return@on true
            }
            if (!inInventory(player, Items.BRONZE_ARROW_882) ||
                player.equipment[EquipmentContainer.SLOT_WEAPON] == null || (
                        !player.equipment[EquipmentContainer.SLOT_WEAPON].definition.name.lowercase()
                            .contains("shortbow", true) &&
                                !player.equipment[EquipmentContainer.SLOT_WEAPON].definition.name.lowercase()
                                    .contains("longbow", true))
            ) {
                sendMessage(player, "You must have bronze arrows and a bow equipped.")
                return@on true
            }
            submitIndividualPulse(player, ArcheryCompetitionPulse(player, (node.asScenery())))
            return@on true
        }
    }

    /**
     * Represents a pulse for an archery competition event.
     *
     *  - The dark bow should fire 1 arrow in the Archery Competition, but sound of 2 arrows firing.
     */
    inner class ArcheryCompetitionPulse(
        private val player: Player,
        private val sceneryId: Scenery,
    ) : Pulse(1, player, sceneryId) {
        private fun showInterface(
            points: Int,
            arrowsLeft: Int,
            target: Int,
            msg: String,
        ) {
            openInterface(player, Components.TARGET_325)
            setVarp(player, 156, 11 - arrowsLeft)
            setVarp(player, 157, points)
            setVarp(player, 158, target)
            sendString(player, msg, Components.TARGET_325, 32)
        }

        override fun pulse(): Boolean {
            if (player.archeryTargets <= 0) {
                return true
            }

            if (delay == 1) {
                delay = player.properties.attackSpeed
            }
            if (player.equipment.remove(Item(Items.BRONZE_ARROW_882, 1))) {
                val p = Ammunition.get(Items.BRONZE_ARROW_882)?.projectile?.transform(player, sceneryId.location)
                p?.endLocation = sceneryId.location
                p?.endHeight = 25
                p?.send()
                player.animate(Animation(Animations.FIRE_BOW_426))
                player.lock(delay)

                val level = player.getSkills().getLevel(Skills.RANGE)
                val bonus = player.properties.bonuses[14]
                var prayer = 1.0
                prayer += player.prayer.getSkillBonus(Skills.RANGE)
                var cumulativeStr = floor(level * prayer)

                cumulativeStr *= 1.0
                var hit = (14 + cumulativeStr + (bonus / 8) + ((cumulativeStr * bonus) * 0.016865)).toInt() / 10 + 1
                hit += RandomFunction.randomSign(RandomFunction.random(3))

                val target = max(0.0, (13 - hit).toDouble()).toInt()
                var points = 0
                var msg = ""
                when (target) {
                    0 -> {
                        points = 100
                        msg = "Bulls-Eye!"
                    }

                    1 -> {
                        points = 50
                        msg = "Hit Yellow!"
                    }

                    2, 3, 4 -> {
                        points = 30
                        msg = "Hit Red!"
                    }

                    5, 6, 7, 8 -> {
                        points = 20
                        msg = "Hit Blue!"
                    }

                    9, 10 -> {
                        points = 10
                        msg = "Hit Black!"
                    }

                    11, 12, 13 -> {
                        points = 0
                        msg = "Missed!"
                    }
                }
                val xp = points / 2

                player.getSkills().addExperience(Skills.RANGE, xp.toDouble(), true)
                player.archeryTotal += points
                player.archeryTargets -= 1
                player.debug("Hit: $hit")
                player.debug("You have " + player.archeryTargets + " targets left.")
                player.debug("You have " + player.archeryTotal + " score.")

                showInterface(player.archeryTotal, player.archeryTargets, target, msg)
                // player.getArcheryTargets() <= 0;
                return true
            } else {
                return true
            }
        }
    }

    override fun defineInterfaceListeners() {
        onOpen(Components.RANGING_GUILD_TICKET_EXCHANGE_278) { player, _ ->
            val items = RangingGuildStock.values().map { it.item }
            player.generateItems(items, Components.RANGING_GUILD_TICKET_EXCHANGE_278, 16, listOf("Buy", "Value"), 3, 6)
            return@onOpen true
        }

        on(Components.RANGING_GUILD_TICKET_EXCHANGE_278) { player, _, opcode, _, slot, _ ->
            val exchange = RangingGuildStock.itemsMap[slot]

            if (exchange == null) {
                sendMessage(player, "Invalid exchange slot.")
                return@on true
            }

            when (opcode) {
                9 -> {
                    // Examine item.
                    sendMessage(player, itemDefinition(exchange.item.id).examine)
                }
                155 -> {
                    // Show value.
                    sendMessage(player, exchange.value)
                }
                196 -> {
                    // Exchange tickets for item.
                    val requiredTickets = exchange.tickets
                    if (freeSlots(player) < 1) {
                        sendMessage(player, "You don't have enough inventory space.")
                    } else if (!removeItem(player, Item(Items.ARCHERY_TICKET_1464, requiredTickets))) {
                        sendMessage(player, "You don't have enough Archery Tickets.")
                    } else {
                        player.inventory.add(exchange.item)
                        sendMessage(player, "You have received ${exchange.item.amount} x ${itemDefinition(exchange.item.id).name}.")
                    }
                }
            }
            return@on true
        }
    }
}

private enum class RangingGuildStock(val item: Item, val tickets: Int, val slot: Int, val value: String) {
    BARB_BOLT_TIPS(Item(Items.BARB_BOLTTIPS_47, 30), 140, 0, "The 30 Barb Boltips cost 140 Archery Tickets."),
    STUDDED_BODY(Item(Items.STUDDED_BODY_1133, 1), 150, 1, "The Studded Body costs 150 Archery Tickets."),
    RUNE_ARROW(Item(Items.RUNE_ARROW_892, 50), 2000, 2, "The 50 Rune Arrows cost 2,000 Archery Tickets."),
    COIF(Item(Items.COIF_1169, 1), 100, 3, "The Coif costs 100 Archery Tickets."),
    GREEN_D_HIDE(Item(Items.GREEN_DHIDE_BODY_1135, 1), 2400, 4, "The Dragonhide body costs 2,400 Archery Tickets."),
    ADAMANT_JAVELIN(Item(Items.ADAMANT_JAVELIN_829, 20), 2000, 5, "The 20 Adamant Javelins cost 2,000 Archery Tickets."),
    ;

    companion object {
        val itemsMap = HashMap<Int, RangingGuildStock>()

        init {
            for (exchange in values()) {
                itemsMap[exchange.slot] = exchange
            }
        }
    }
}

/**
 * Ranging guild plugin.
 */
@Initializable
private class RangingGuildOptionHandler : OptionHandler() {
    override fun newInstance(arg: Any?): Plugin<Any> {
        SceneryDefinition.forId(2514).handlers["option:open"] = this
        SceneryDefinition.forId(2511).handlers["option:climb-up"] = this
        SceneryDefinition.forId(2512).handlers["option:climb-down"] = this

        ClassScanner.definePlugins(
            TribalWeaponSalesmanDialogue(),
            BowArrowSalesmanDialogue(),
            ArmourSalesmanDialogue(),
            CompetitionJudgeDialogue(),
            LeatherWorkerDialogue(),
            TowerAdvisorDialogue(),
            TowerArcherNPC(),
        )
        return this
    }

    override fun handle(player: Player, node: Node, option: String): Boolean {
        val id = if (node is Scenery) node.getId() else 0
        when (option) {
            "open" ->
                when (id) {
                    2514 -> {
                        if (player.location.y >= 3438) {
                            if (player.getSkills().getStaticLevel(Skills.RANGE) < 40) {
                                player.dialogueInterpreter.sendDialogue("You need a Ranging level of 40 to enter here.")
                                return true
                            }
                        }
                        handleAutowalkDoor(player, (node as Scenery),
                            if (player.location.y >= 3438) {
                                Location.create(2659, 3437, 0)
                            } else {
                                Location.create(2657, 3439, 0)
                            },)
                    }
                }

            "climb-up" ->
                when (id) {
                    2511 -> {
                        setAttribute(player, "ladder", node)
                        player.interfaceManager.open(Component(Components.CWS_WARNING_23_564))
                    }
                }

            "climb-down" ->
                when (id) {
                    2512 -> climb(player, null, Location.create(2668, 3427, 0))
                }
        }
        return true
    }

    override fun getDestination(node: Node, n: Node): Location? {
        if (n is Scenery) {
            if (n.definition.hasAction("open")) {
                if (n.getId() == 2514) {
                    return if (node.location.y >= 3438) {
                        Location.create(2657, 3439, 0)
                    } else {
                        Location.create(2659, 3437, 0)
                    }
                }
                return DoorActionHandler.getDestination((node as Player), n)
            }
            if (n.getId() == 2513) return Location.create(2673, 3420, 0)
        }
        return null
    }


    /**
     * Represents the Tribal weapon salesman dialogue.
     */
    inner class TribalWeaponSalesmanDialogue(player: Player? = null) : Dialogue(player) {

        override fun open(vararg args: Any?): Boolean {
            npc = args[0] as NPC
            player("Hello there.")
            return true
        }

        override fun handle(interfaceId: Int, buttonId: Int): Boolean {
            when (stage) {
                0 -> npc("Greetings, traveller. Are you interested in any throwing", "weapons?").also { stage++ }
                1 -> options("Yes I am.", "Not really.").also { stage++ }
                2 -> when (buttonId) {
                    1 -> player("Yes I am.").also { stage++ }
                    2 -> player("Not really.").also { stage += 3 }
                }
                3 -> npc("That is a good thing.").also { stage++ }
                4 -> end().also { openNpcShop(player, npc.id) }
                5 -> npc("No bother to me.").also { stage = END_DIALOGUE }
            }
            return true
        }

        override fun getIds(): IntArray = intArrayOf(NPCs.TRIBAL_WEAPON_SALESMAN_692)
    }

    /**
     * Represents the Bow arrow salesman dialogue.
     */
    inner class BowArrowSalesmanDialogue(player: Player? = null) : Dialogue(player) {

        override fun open(vararg args: Any?): Boolean {
            npc = args[0] as NPC
            player("Hello.").also { stage = 0 }
            return true
        }

        override fun handle(interfaceId: Int, buttonId: Int): Boolean {
            when (stage) {
                0 -> npc("A fair day, traveller. Would you like to see my wares?").also { stage++ }
                1 -> options("Yes please.", "No thanks.").also { stage++ }
                2 -> when (buttonId) {
                    1 -> end().also { openNpcShop(player, npc.id) }
                    2 -> end()
                }
            }
            return true
        }

        override fun getIds(): IntArray = intArrayOf(NPCs.BOW_AND_ARROW_SALESMAN_683)
    }

    /**
     * Represents the Armour salesman dialogue.
     */
    inner class ArmourSalesmanDialogue(player: Player? = null) : Dialogue(player) {

        override fun open(vararg args: Any?): Boolean {
            npc = args[0] as NPC
            player("Good day to you.")
            return true
        }

        override fun handle(interfaceId: Int, buttonId: Int): Boolean {
            when (stage) {
                0 -> npc("And to you. Can I help you?").also { stage++ }
                1 -> if (isMaster(player, Skills.RANGE)) {
                    options("What do you do here?", "I'd like to see what you sell.", "Can I buy a Skillcape of Range?", "I've seen enough, thanks.").also { stage = 100 }
                } else {
                    options("What do you do here?", "I'd like to see what you sell.", "I've seen enough, thanks.").also { stage++ }
                }

                2 -> when (buttonId) {
                    1 -> player("What do you do here?").also { stage++ }
                    2 -> player("I'd like to see what you sell.").also { stage = 20 }
                    3 -> player("I've seen enough, thanks.").also { stage = 30 }
                }
                10 -> npc("I am a supplier of leather armours and accessories. Ask and I will tell you what I know.").also { stage++ }
                11 -> options("Tell me about your armours.", "Tell me about your accessories.", "I've seen enough, thanks.").also { stage++ }
                12 -> when (buttonId) {
                    1 -> player("Tell me about your armours.").also { stage++ }
                    2 -> player("Tell me about your accessories.").also { stage += 2 }
                    3 -> player("I've seen enough, thanks.").also { stage = 30 }
                }
                13 -> npc("I have normal, studded and hard types.").also { stage = 200 }
                14 -> npc("Ah yes we have a new range accessories in stock. Essential items for an archer like you. We have vambraces, chaps, cowls, and coifs.").also { stage = 300 }
                20 -> npc("Indeed, cast your eyes on my wares, adventurer.").also { stage++ }
                21 -> end().also { openNpcShop(player, npc.id) }
                30 -> npc("Very good, adventurer.").also { stage = END_DIALOGUE }
                100 -> when (buttonId) {
                    1 -> player("What do you do here?").also { stage = 10 }
                    2 -> player("I'd like to see what you sell.").also { stage = 20 }
                    3 -> player("Can I buy a Skillcape of Range?").also { stage++ }
                    4 -> player("I've seen enough, thanks.").also { stage = 30 }
                }
                101 -> npc("Certainly! Right when you give me 99000 coins.").also { stage++ }
                102 -> options("Okay, here you go.", "No, thanks.").also { stage++ }
                103 -> when (buttonId) {
                    1 -> player("Okay, here you go.").also { stage++ }
                    2 -> end()
                }
                104 -> if (purchase(player, Skills.RANGE)) npc("There you go! Enjoy.") else end()

                // Tell me about your armours.
                200 -> options("Tell me about normal leather.", "What's studded leather?", "What's hard leather?", "Enough about armour.").also { stage++ }
                201 -> when (buttonId) {
                    1 -> player("Tell me about normal leather.").also { stage++ }
                    2 -> player("What's studded leather?").also { stage = 203 }
                    3 -> player("What's hard leather?").also { stage = 204 }
                    4 -> player("Enough about armour.").also { stage = 30 }
                }
                202 -> npcl(FaceAnim.NEUTRAL, "Indeed, lather armour is excellent for archers. It's supple and not very heavy.").also { stage = 200 }
                203 -> npcl(FaceAnim.NEUTRAL, "Ah now that's leather covered with studs. It's more protective than ordinary leather.").also { stage = 200 }
                204 -> npcl(FaceAnim.NEUTRAL, "Hard leather is specially treated using oils and drying methods to create a hard wearing armour.").also { stage = 200 }

                // Tell me about your accessories.
                300 -> options("Tell me about vambraces.", "Tell me about chaps.", "Tell me about cowls.", "Tell me about coifs.", "Enough about armour.").also { stage++ }
                301 -> when (buttonId) {
                    1 -> player("Tell me about vambraces.").also { stage++ }
                    2 -> player("Tell me about chaps.").also { stage = 304 }
                    3 -> player("Tell me about cowls.").also { stage = 306 }
                    4 -> player("Tell me about coifs.").also { stage = 308 }
                    5 -> player("Enough about accessories.").also { stage = 30 }
                }
                302 -> npcl(FaceAnim.NEUTRAL, "Ah yes, vambraces. These useful items are for your arms.").also { stage++ }
                303 -> npcl(FaceAnim.NEUTRAL, "A protective sheath that favours the bow and arrow. An essential purchase.").also { stage = 30 }
                304 -> npcl(FaceAnim.NEUTRAL, "Chaps have two functions; firstly to protect your legs, and secondly for ease of reloading arrows.").also { stage++ }
                305 -> npcl(FaceAnim.NEUTRAL, "I can highly recommend these to you for quick archery.").also { stage = 30 }
                306 -> npcl(FaceAnim.NEUTRAL, "The cowl is a soft leather hat, ideal for protection with manoeuvrability.").also { stage++ }
                307 -> npcl(FaceAnim.NEUTRAL, "These are highly favoured with our guards.").also { stage = 30 }
                308 -> npcl(FaceAnim.NEUTRAL, "The coif is a specialized cowl, that has extra chain protection to keep your neck and shoulders safe.").also { stage++ }
                309 -> npcl(FaceAnim.NEUTRAL, "An excellent addition to our range, traveller.").also { stage = 30 }
            }
            return true
        }

        override fun getIds(): IntArray = intArrayOf(NPCs.ARMOUR_SALESMAN_682)
    }

    /**
     * Represents the Competition judge dialogue.
     */
    inner class CompetitionJudgeDialogue(player: Player? = null) : Dialogue(player) {

        override fun open(vararg args: Any?): Boolean {
            if (player.inventory.getAmount(Items.ARCHERY_TICKET_1464) >= 1000 && !hasDiaryTaskComplete(player, DiaryType.SEERS_VILLAGE, 1, 7)) {
                npc("Wow! I see that you've got yourself a whole load of ", "archery tickets. Well done!")
                finishDiaryTask(player, DiaryType.SEERS_VILLAGE, 1, 7)
                stage = -1
            } else if (player.archeryTargets > 0) {
                npc("Hello again, do you need reminding of the rules?")
                stage = 20
            } else if (player.archeryTotal == 0) {
                npc("Hello there, would you like to take part in the", "archery competition? It only costs 200 coins to", "enter.")
                stage = 0
            } else {
                val reward = player.archeryTotal / 10
                npc("Well done. Your score is: " + player.archeryTotal + ".", "For that score you will receive $reward Archery tickets.")
                player.archeryTargets = -1
                player.archeryTotal = 0
                if (!player.inventory.add(Item(Items.ARCHERY_TICKET_1464, reward))) {
                    player.bank.add(Item(Items.ARCHERY_TICKET_1464, reward))
                    sendMessage(player, "Your reward was sent to your bank.")
                }
                stage = 999
            }
            return true
        }

        override fun handle(interfaceId: Int, buttonId: Int): Boolean {
            when (stage) {
                999 -> end()
                -1 -> if (player.archeryTargets > 0) {
                    npc("Hello again, do you need reminding of the rules?")
                    stage = 20
                } else if (player.archeryTotal == 0) {
                    npc("Hello there, would you like to take part in the", "archery competition? It only costs 200 coins to", "enter.")
                    stage = 0
                } else {
                    val reward = player.archeryTotal / 10
                    npc(
                        "Well done. Your score is: " + player.archeryTotal + ".",
                        "For that score you will receive $reward Archery tickets.",
                    )
                    if (!player.inventory.add(Item(Items.ARCHERY_TICKET_1464, reward))) {
                        player.bank.add(Item(Items.ARCHERY_TICKET_1464, reward))
                        sendMessage(player, "Your reward was sent to your bank.")
                    }
                    stage = 999
                }

                0 -> {
                    options("Sure, I'll give it a go.", "What are the rules?", "No thanks.")
                    stage++
                }

                1 -> when (buttonId) {
                    1 -> {
                        player("Sure, I'll give it a go.")
                        stage = 2
                    }

                    2 -> {
                        player("What are the rules?")
                        stage = 5
                    }

                    3 -> {
                        player("No thanks.")
                        stage = 999
                    }
                }

                2 -> {
                    npc("Great! That will be 200 coins then please.")
                    stage++
                }

                3 -> if (amountInInventory(player, Items.COINS_995) < 200) {
                    player("Oops, I don't have enough coins on me...")
                    stage++
                } else {
                    end()
                    sendMessage(player, "You pay the judge and he gives you 10 bronze arrows.")
                    removeItem(player, Item(Items.COINS_995, 200))
                    addItem(player, Items.BRONZE_ARROW_882, 10)
                    player.archeryTargets = 10
                    player.archeryTotal = 0
                }

                4 -> {
                    npc("Never mind, come back when you've got enough.")
                    stage = 999
                }

                5, 22 -> {
                    npc("The rules are very simple:")
                    stage++
                }

                6, 23 -> {
                    npc(
                        "You're given 10 shots at the targets, for each hit",
                        "you will receive points. At the end you'll be",
                        "rewarded 1 ticket for every 10 points.",
                    )
                    stage++
                }

                7 -> {
                    npc(
                        "The tickets can be exchanged for goods from our stores.",
                        "Do you want to give it a go? Only 200 coins.",
                    )
                    stage++
                }

                8 -> {
                    options("Sure, I'll give it a go.", "No thanks.")
                    stage++
                }

                9 -> when (buttonId) {
                    1 -> {
                        player("Sure, I'll give it a go.")
                        stage = 2
                    }

                    3 -> {
                        player("No thanks.")
                        stage = 999
                    }
                }

                20 -> {
                    val arrows =
                        (player.inventory.getAmount(Items.BRONZE_ARROW_882) + player.equipment.getAmount(Items.BRONZE_ARROW_882))
                    if (arrows < 1) {
                        player("Well, I actually don't have any more arrows. Could I", "get some more?")
                        stage = 25
                    } else {
                        options("Yes please.", "No thanks, I've got it.", "How am I doing so far?")
                        stage++
                    }
                }

                21 -> when (buttonId) {
                    1 -> {
                        player("Yes please.")
                        stage++
                    }

                    2 -> {
                        player("No thanks, I've got it.")
                        stage = 30
                    }

                    3 -> {
                        player("How am I doing so far?")
                        stage = 40
                    }
                }

                24 -> {
                    npc("The tickets can be exchanged for goods from our stores.", "Good Luck!")
                    stage = 999
                }

                25 -> {
                    npc("Ok, but it'll cost you 100 coins.")
                    stage++
                }

                26 -> {
                    options("Sure, I'll take some.", "No thanks.")
                    stage++
                }

                27 -> when (buttonId) {
                    1 -> {
                        player("Sure, I'll take some.")
                        stage++
                    }

                    2 -> {
                        player("No thanks.")
                        stage = 999
                    }
                }

                28 -> if (player.inventory.getAmount(995) < 100) {
                    player("Oops, I don't have enough coins on me...")
                    stage++
                } else {
                    end()
                    player.packetDispatch.sendMessage("You pay the judge and he gives you 10 bronze arrows.")
                    player.inventory.remove(Item(995, 100))
                    player.inventory.add(Item(882, 10))
                }

                30 -> {
                    npc("Glad to hear it, good luck!")
                    stage = 999
                }

                40 -> {
                    val msg = if (player.archeryTotal <= 0) {
                        "You haven't started yet."
                    } else if (player.archeryTotal <= 80) {
                        "Not bad, keep going."
                    } else {
                        "You're pretty good, keep it up."
                    }
                    npc("So far your score is: " + player.archeryTotal, msg)
                    stage = 999
                }
            }
            return true
        }

        override fun getIds(): IntArray = intArrayOf(NPCs.COMPETITION_JUDGE_693)
    }

    /**
     * Represents the Leather worker dialogue.
     */
    inner class LeatherWorkerDialogue(player: Player? = null) : Dialogue(player) {

        override fun open(vararg args: Any?): Boolean {
            player("Hello.")
            return true
        }

        override fun handle(interfaceId: Int, buttonId: Int): Boolean {
            when (stage) {
                0 -> npc("Can I help you?").also { stage++ }
                1 -> options("What do you do here?", "No thanks.").also { stage++ }
                2 -> when (buttonId) {
                    1 -> player("What do you do here?").also { stage++ }
                    2 -> player("No thanks.").also { stage = 6 }
                }
                3 -> npc("Well, I can cure plain cowhides into pieces of leather", "ready for crafting.").also { stage++ }
                4 -> npc("I work with ordinary, hard or dragonhide leather and", "also snakeskin.").also { stage++ }
                5 -> end().also { TanningProduct.open(player, npc.id) }
                6 -> npc("Suit yourself.").also { stage = END_DIALOGUE }
            }
            return true
        }

        override fun getIds(): IntArray = intArrayOf(NPCs.LEATHERWORKER_680)
    }

    /**
     * Represents the Tower advisor dialogue.
     */
    inner class TowerAdvisorDialogue(player: Player? = null) : Dialogue(player) {

        override fun open(vararg args: Any?): Boolean {
            npc = args[0] as NPC
            player("Hello there, what do you do here?").also { stage++ }
            return true
        }

        override fun handle(interfaceId: Int, buttonId: Int): Boolean {
            when (stage) {
                0 -> npc("Hi. We are in charge of this practice area.").also { stage++ }
                1 -> player("This is a practice area?").also { stage++ }
                2 -> npc("Surrounding us are four towers. Each tower contains", "trained archers of a different level. You'll notice", "it's quite a distance, so you'll need a longbow.").also { stage++ }
                3 -> {
                    val rangeLevel: Int = getStatLevel(player, Skills.RANGE)
                    when {
                        // north
                        rangeLevel < 50 -> npc("As you're not very skilled, I advise you to practice", "on the north tower. That'll provide the best", "challenge for you.").also { stage = END_DIALOGUE }
                        // east
                        rangeLevel < 60 -> npc("You appear to be somewhat skilled with a bow, so I", "advise you to practice on the south tower. That'll", "provide the best challenge for you.").also { stage = END_DIALOGUE }
                        // south
                        rangeLevel < 70 -> npc("You appear to be fairly skilled with a bow, so I", "advise you to practice on the south tower. That'll", "provide the best challenge for you.").also { stage = END_DIALOGUE }
                        // west
                        else -> npc("Looks like you're very skilled, so I advise you to", "practice on the west tower. That'll provide the best", "challenge for you.").also { stage = END_DIALOGUE }
                    }
                }
            }
            return true
        }

        override fun getIds(): IntArray = intArrayOf(
            NPCs.TOWER_ADVISOR_684,
            NPCs.TOWER_ADVISOR_685,
            NPCs.TOWER_ADVISOR_686,
            NPCs.TOWER_ADVISOR_687,
        )
    }

    /**
     * Tower archer NPC.
     */
    inner class TowerArcherNPC(
        id: Int = 0,
        location: Location? = null,
    ) : AbstractNPC(id, location) {
        override fun getIds(): IntArray = intArrayOf(NPCs.TOWER_ARCHER_688, NPCs.TOWER_ARCHER_689, NPCs.TOWER_ARCHER_690, NPCs.TOWER_ARCHER_691)

        override fun construct(
            id: Int,
            location: Location,
            vararg objects: Any?,
        ): AbstractNPC = TowerArcherNPC(id, location)

        override fun finalizeDeath(killer: Entity?) {
            super.finalizeDeath(killer)
        }
    }
}