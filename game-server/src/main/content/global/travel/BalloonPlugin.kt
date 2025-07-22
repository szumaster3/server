package content.global.travel

import content.data.GameAttributes
import core.api.*
import core.cache.def.impl.ItemDefinition
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.InterfaceListener
import core.game.interaction.QueueStrength
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.info.Rights
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.tools.END_DIALOGUE
import org.rs.consts.*

/**
 * Represents balloon travel data.
 */
enum class Balloon(
    val areaName: String,
    val npc: Int,
    val destination: Location,
    val logId: Int,
    val requiredLevel: Int,
    val varbitId: Int,
    val componentId: Int,
    val button: Int,
    val wrapperId: Int
) {
    ENTRANA("Entrana", NPCs.AUGUSTE_5049, Location(2809, 3356), Items.LOGS_1511, 20, Vars.VARBIT_QUEST_ENLIGHTENED_JOURNEY_ENTRANA_BALLOON_2867, 25, 17, 19133),
    TAVERLEY("Taverley", NPCs.ASSISTANT_STAN_5057, Location(2940, 3420), Items.LOGS_1511, 20, Vars.VARBIT_QUEST_ENLIGHTENED_JOURNEY_TAVERLEY_BALLOON_2868, 22, 18, 19135),
    CRAFT_GUILD("Crafting Guild", NPCs.ASSISTANT_BROCK_5054, Location(2924, 3303), Items.OAK_LOGS_1521, 30, Vars.VARBIT_QUEST_ENLIGHTENED_JOURNEY_CRAFTING_GUILD_BALLOON_2871, 20, 16, 19141),
    VARROCK("Varrock", NPCs.ASSISTANT_SERF_5053, Location(3298, 3481), Items.WILLOW_LOGS_1519, 40, Vars.VARBIT_QUEST_ENLIGHTENED_JOURNEY_VARROCK_BALLOON_2872, 21, 19, 19143),
    CASTLE_WARS("Castle Wars", NPCs.ASSISTANT_MARROW_5055, Location(2462, 3108), Items.YEW_LOGS_1515, 50, Vars.VARBIT_QUEST_ENLIGHTENED_JOURNEY_CASTLE_WARS_BALLOON_2869, 24, 14, 19137),
    GRAND_TREE("Grand Tree", NPCs.ASSISTANT_LE_SMITH_5056, Location(2480, 3458), Items.MAGIC_LOGS_1513, 60, Vars.VARBIT_QUEST_ENLIGHTENED_JOURNEY_GRAND_TREE_BALLOON_2870, 23, 15, 19139);

    companion object {
        /**
         * A map for quick lookup of [Balloon] enum instances by their NPC id.
         */
        private val npcMap = values().associateBy { it.npc }

        /**
         * A button map id for balloon locations.
         */
        private val buttonToBalloon: Map<Int, Balloon> by lazy {
            values().associateBy { it.button }
        }

        /**
         * Returns the balloon location to the given interface button id.
         *
         * @param buttonId the ID of the interface button
         * @return the matching [Balloon] instance or null if no match is found
         */
        fun fromButtonId(buttonId: Int): Balloon? = buttonToBalloon[buttonId]

        /**
         * A map that associates scenery object id with their balloon locations.
         */
        private val sceneryToBalloon: Map<Int, Balloon> by lazy {
            values().associateBy { it.wrapperId }
        }

        /**
         * Returns the balloon location to the given scenery id.
         *
         * @param id the ID of the scenery object
         * @return the matching [Balloon] instance or null if no match is found
         */
        fun fromSceneryId(id: Int): Balloon? = sceneryToBalloon[id]

        /**
         * Returns the balloon location to the given npc id.
         *
         * @param npcId the ID of the npc
         * @return the matching [Balloon] instance or null if no match is found
         */
        fun fromNpcId(npcId: Int): Balloon? = npcMap[npcId]

        /**
         * Animation map.
         */
        private val size = values().size

        /**
         * Represents animation ids for balloon travel routes.
         */
        private val animations: Array<IntArray> = Array(size) { IntArray(size) { 0 } }.apply {
            this[ENTRANA.ordinal][TAVERLEY.ordinal] = 5110
            this[TAVERLEY.ordinal][ENTRANA.ordinal] = 5111
            this[ENTRANA.ordinal][CRAFT_GUILD.ordinal] = 5112
            this[CRAFT_GUILD.ordinal][ENTRANA.ordinal] = 5113
            this[ENTRANA.ordinal][VARROCK.ordinal] = 5114
            this[VARROCK.ordinal][ENTRANA.ordinal] = 5115
            this[ENTRANA.ordinal][GRAND_TREE.ordinal] = 5116
            this[GRAND_TREE.ordinal][ENTRANA.ordinal] = 5117
            this[ENTRANA.ordinal][CASTLE_WARS.ordinal] = 5118
            this[CASTLE_WARS.ordinal][ENTRANA.ordinal] = 5119
            this[VARROCK.ordinal][CRAFT_GUILD.ordinal] = 5120
            this[CRAFT_GUILD.ordinal][VARROCK.ordinal] = 5121
            this[VARROCK.ordinal][TAVERLEY.ordinal] = 5122
            this[TAVERLEY.ordinal][VARROCK.ordinal] = 5123
            this[TAVERLEY.ordinal][CRAFT_GUILD.ordinal] = 5124
            this[CRAFT_GUILD.ordinal][TAVERLEY.ordinal] = 5125
            this[TAVERLEY.ordinal][CASTLE_WARS.ordinal] = 5126
            this[CASTLE_WARS.ordinal][TAVERLEY.ordinal] = 5127
            this[CRAFT_GUILD.ordinal][CASTLE_WARS.ordinal] = 5128
            this[CASTLE_WARS.ordinal][CRAFT_GUILD.ordinal] = 5129
            this[VARROCK.ordinal][CASTLE_WARS.ordinal] = 5130
            this[CASTLE_WARS.ordinal][VARROCK.ordinal] = 5131
            this[GRAND_TREE.ordinal][CASTLE_WARS.ordinal] = 5132
            this[CASTLE_WARS.ordinal][GRAND_TREE.ordinal] = 5133
            this[GRAND_TREE.ordinal][CRAFT_GUILD.ordinal] = 5134
            this[CRAFT_GUILD.ordinal][GRAND_TREE.ordinal] = 5135
            this[TAVERLEY.ordinal][GRAND_TREE.ordinal] = 5136
            this[GRAND_TREE.ordinal][TAVERLEY.ordinal] = 5137
            this[VARROCK.ordinal][GRAND_TREE.ordinal] = 5138
            this[GRAND_TREE.ordinal][VARROCK.ordinal] = 5139
        }

        /**
         * Gets the animation id for traveling from one balloon location to another.
         *
         * @param from The origin balloon location.
         * @param to The destination balloon location.
         * @return The animation id.
         * @throws IllegalStateException if no animation exists for the given route.
         */
        fun getAnimationId(from: Balloon, to: Balloon): Int {
            return animations[from.ordinal][to.ordinal].takeIf { it != 0 }
                ?: error("No animation for route [$from] -> [$to]")
        }

        /**
         * Unlocks a new destination after a successful navigation from Entrana.
         */
        fun unlockDestination(player: Player, destination: Balloon) {
            if (getVarbit(player, destination.varbitId) != 1) {
                setVarbit(player, destination.varbitId, 1, true)
                val xp = 2000
                if (destination != ENTRANA) {
                    rewardXP(player, Skills.FIREMAKING, xp.toDouble())
                }
                sendMessage(player, "You have unlocked the balloon route to ${destination.areaName}!")
            }
        }
    }
}

/**
 * Handles the balloon travel system.
 */
class BalloonTravel : InterfaceListener, InteractionListener {

    init {
        assistants.forEach { (npcId, location) ->
            NPC(npcId, location).apply {
                init()
                isWalks = true
            }
        }
    }

    companion object {
        /**
         * Represents the assistant npc.
         */
        private val assistantIds = intArrayOf(5050, 5053, 5054, 5055, 5056, 5057, 5063, 5065)

        /**
         * Represents the basket ids.
         */
        private val basketIds = intArrayOf(Scenery.BASKET_19128, Scenery.BASKET_19129)

        /**
         * Represents assistant npcs and their spawn locations.
         */
        private val assistants = mapOf(
            NPCs.ASSISTANT_SERF_5053 to Location.create(3298, 3484, 0),
            NPCs.ASSISTANT_LE_SMITH_5056 to Location.create(2480, 3458, 0),
            NPCs.ASSISTANT_STAN_5057 to Location.create(2938, 3424, 0)
        )

        /**
         * Opens the interface.
         */
        private fun openBalloonInterface(player: Player, location: Balloon) {
            player.setAttribute(GameAttributes.BALLOON_ORIGIN, location)
            openInterface(player, Components.ZEP_BALLOON_MAP_469)
            setComponentVisibility(player, Components.ZEP_BALLOON_MAP_469, location.componentId, false)
        }

        /**
         * Executes the flight.
         */
        fun flightPulse(player: Player, destination: Balloon) {
            val origin = player.getAttribute<Balloon>(GameAttributes.BALLOON_ORIGIN)
            if (origin == null) {
                player.debug("null location.")
                return
            }

            lock(player, 7)
            lockInteractions(player, 7)

            val animationId = Balloon.getAnimationId(origin, destination)
            val animationTime = Animation(animationId).duration

            playJingle(player, 118)

            closeInterface(player)
            setMinimapState(player, 2)
            openInterface(player, Components.FADE_TO_BLACK_120)
            sendMessage(player, "You board the balloon and fly to ${destination.areaName}.")
            queueScript(player, 4, QueueStrength.SOFT) { ticks: Int ->
                when (ticks) {
                    0 -> {
                        openInterface(player, Components.ZEP_BALLOON_MAP_469)
                        setComponentVisibility(player, Components.ZEP_BALLOON_MAP_469, 12, false)
                        animateInterface(player, Components.ZEP_BALLOON_MAP_469, 12, animationId)
                        player.teleport(destination.destination)
                        return@queueScript delayScript(player, animationTime)
                    }

                    1 -> {
                        unlock(player)
                        closeInterface(player)
                        setMinimapState(player, 0)
                        openOverlay(player, Components.FADE_FROM_BLACK_170)
                        removeAttribute(player, GameAttributes.BALLOON_ORIGIN)
                        sendDialogue(player, "You arrive safely in ${destination.areaName}.")
                        return@queueScript stopExecuting(player)
                    }

                    else -> return@queueScript stopExecuting(player)
                }
            }
        }
    }

    override fun defineInterfaceListeners() {
        on(Components.ZEP_BALLOON_MAP_469) { player, _, _, buttonID, _, _ ->
            val destination = Balloon.fromButtonId(buttonID) ?: return@on true
            val isAdmin = player.rights == Rights.ADMINISTRATOR

            //TODO: You can open new locations from Entrana.

            if (!hasLevelStat(player, Skills.FIREMAKING, destination.requiredLevel)) {
                sendDialogue(
                    player,
                    "You require a Firemaking level of ${destination.requiredLevel} to travel to ${destination.areaName}."
                )
                return@on true
            }

            val origin = player.getAttribute<Balloon>(GameAttributes.BALLOON_ORIGIN)
            if (origin == destination) {
                sendDialogue(player, "You can't fly to the same location.")
                return@on true
            }

            if (player.familiarManager.hasFamiliar()) {
                sendMessage(player, "You can't take a follower on a ride.")
                return@on true
            }

            if (player.settings.weight > 40.0) {
                sendDialogue(player, "You're carrying too much weight to fly. Try reducing your weight below 40 kg.")
                return@on true
            }

            if (destination == Balloon.ENTRANA) {
                if (!isAdmin && !ItemDefinition.canEnterEntrana(player)) {
                    sendDialogue(player, "You can't take flight with weapons and armour to Entrana.")
                    return@on true
                } else {
                    sendMessage(player, "You are quickly searched.")
                }
            }

            if (isAdmin) {
                flightPulse(player, destination)
                return@on true
            }

            if (removeItem(player, Item(destination.logId, 1))) {
                flightPulse(player, destination)
                if (destination == Balloon.VARROCK) {
                    finishDiaryTask(player, DiaryType.VARROCK, 2, 17)
                }
            } else {
                val requiredItem = getItemName(destination.logId).lowercase().removeSuffix("s").trim()
                sendDialogue(player, "You need at least one $requiredItem.")
            }
            return@on true
        }
    }

    override fun defineListeners() {

        /*
         * Handles interaction with basket scenery.
         */

        on(basketIds, IntType.SCENERY, "use") { player, node ->
            val sceneryId = node.asScenery().wrapper.id
            val location = Balloon.fromSceneryId(sceneryId)
            if (location != null) {
                openBalloonInterface(player, location)
            }
            return@on true
        }

        /*
         * Handles interaction with npc.
         */

        on(assistantIds, IntType.NPC, "talk-to") { player, node ->
            openDialogue(player, AssistantDialogue(), node)
            return@on true
        }

        on(assistantIds, IntType.NPC, "Fly") { player, node ->
            if (!isQuestComplete(player, Quests.ENLIGHTENED_JOURNEY)) {
                sendMessage(player, "You must complete ${Quests.ENLIGHTENED_JOURNEY} before you can use it.")
                return@on true
            }

            val location = Balloon.fromNpcId(node.id)
            if (location != null) {
                openBalloonInterface(player, location)
            }
            return@on true
        }
    }
}

private class AssistantDialogue : DialogueFile() {

    override fun handle(componentID: Int, buttonID: Int) {
        val faceExpression = if (npc!!.id != NPCs.ASSISTANT_LE_SMITH_5056) FaceAnim.HALF_GUILTY else FaceAnim.OLD_NORMAL
        when (stage) {
            0 -> npcl(faceExpression, "Do you want to use the balloon? Just so you know, some locations require special logs and high Firemaking skills.").also { stage++ }
            1 -> when (npc!!.id) {
                NPCs.AUGUSTE_5050 -> options("Yes.", "No.").also { stage++ }
                else -> options("Yes.", "No.", "Who are you?").also { stage++ }
            }
            2 -> when (buttonID) {
                1 -> if (!isQuestComplete(player!!, Quests.ENLIGHTENED_JOURNEY)) {
                    npcl(faceExpression, "Oh, Sorry...You must complete Enlightened Journey before you can use it.").also { stage = END_DIALOGUE }
                } else {
                    player("Yes.").also { stage = 7 }
                }

                2 -> player("No.").also { stage = END_DIALOGUE }
                3 -> player("Who are you?").also { stage++ }
            }

            3 -> when (npc!!.id) {
                5053 -> npcl(faceExpression, "I am a Serf. Assistant Serf to you! Auguste freed me and gave me this job.").also { stage++ }
                5055 -> npcl(faceExpression, "I am Assistant Marrow. I'm working here part time while I study to be a doctor.").also { stage++ }
                5056 -> npcl(faceExpression, "I am Assistant Le Smith. I used to work as a glider pilot, but they kicked me off.").also { stage = 7 }
                5057 -> npcl(faceExpression, "I am Stan. Auguste hired me to look after this balloon. I make sure people are prepared to fly.").also { stage++ }
                5065 -> npcl(faceExpression, "I am Assistant Brock. I serve under Auguste as his number two assistant.").also { stage++ }
            }

            4 -> npcl(faceExpression, "Do you want to use the balloon?").also { stage++ }
            5 -> options("Yes.", "No.").also { stage++ }
            6 -> when (buttonID) {
                1 -> {
                    if (!isQuestComplete(player!!, Quests.ENLIGHTENED_JOURNEY)) {
                        npcl(faceExpression, "Oh, Sorry...You must complete ${Quests.ENLIGHTENED_JOURNEY} before you can use it.").also { stage = END_DIALOGUE }
                    } else {
                        player("Yes.").also { stage = 7 }
                    }
                }

                2 -> player("No.").also { stage = END_DIALOGUE }
            }

            7 -> {
                end()
                openInterface(player!!, Components.ZEP_BALLOON_MAP_469)
            }

            8 -> playerl(FaceAnim.FRIENDLY, "Why?").also { stage++ }
            9 -> npcl(faceExpression, "They said I was too full of hot air.").also { stage = 4 }
        }
    }
}