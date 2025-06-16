package content.region.misthalin.draynor.quest.swept.plugin

import content.data.GameAttributes
import core.api.*
import core.api.interaction.getNPCName
import core.api.quest.getQuestStage
import core.api.quest.setQuestStage
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.emote.Emotes
import core.game.node.scenery.Scenery
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.game.world.map.Location
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.rs.consts.*

/**
 * Represents utils for [Swept Away quest][content.region.kandarin.quest.swept.SweptAway].
 */
internal object SweptUtils {
    const val PEN_TABLE_INTERFACE = 799

    const val VARBIT_NEWT_CRATE_LABEL = 5460
    const val VARBIT_TOAD_CRATE_LABEL = 5461
    const val VARBIT_NEWT_AND_TOAD_CRATE_LABEL = 5462
    const val VARBIT_RIMMINGTON_TRAPDOOR = 5465
    const val VARBIT_LOTTIE_CHEST = 5467
    const val VARBIT_BETTY_TRAPDOOR = 5469

    const val BROOM_MODEL = 42783

    const val SWEEP_ANIMATION = 10532
    const val BROOM_ENCHANTMENT_GFX = 1865
    const val STIR_ANIMATION = 10546
    const val ENCHANT_BROOMSTICK_ANIMATION = 10535
    const val STIR_WITH_BROOMSTICK_ANIMATION = 10543
    const val FILL_GOULASH_FROM_CAULDRON_A = 10544
    const val FILL_GOULASH_FROM_CAULDRON_B = 10545
    const val TAKE_FROG_FROM_CRATE_ANIMATION = 10550
    const val TAKE_NEWT_FROM_CRATE_ANIMATION = 10551

    const val NT_CRATE = org.rs.consts.Scenery.WRONGLY_LABELLED_CRATE_NEWTS_AND_TOADS_39336
    const val N_CRATE = org.rs.consts.Scenery.WRONGLY_LABELLED_CRATE_NEWTS_39335
    const val T_CRATE = org.rs.consts.Scenery.WRONGLY_LABELLED_CRATE_TOADS_39334

    val GUS_CRATES =
        intArrayOf(
            39334,
            39335,
            39336,
        )

    val GUS_CRATES_LABELLED =
        intArrayOf(
            39337,
            39339,
            39341,
        )

    val CREATURE_PEN =
        intArrayOf(
            org.rs.consts.Scenery.SNAIL_PEN_39292,
            org.rs.consts.Scenery.SPIDER_PEN_39297,
            org.rs.consts.Scenery.BAT_PEN_39287,
            org.rs.consts.Scenery.BLACKBIRD_PEN_39281,
            org.rs.consts.Scenery.RAT_PEN_39309,
            org.rs.consts.Scenery.HOLDING_PEN_39314,
            org.rs.consts.Scenery.REPTILE_PEN_39303,
        )

    val CREATURE_PEN_ITEM =
        intArrayOf(
            Items.REPTILE_14070,
            Items.BLACKBIRD_14071,
            Items.BAT_14072,
            Items.SPIDER_14073,
            Items.SNAIL_14075,
            Items.RAT_14074,
        )

    private val CREATURES = mutableMapOf<Int, NPC>()

    private val NPC_TO_ITEM =
        mapOf(
            NPCs.BAT_8208 to Items.BAT_14072,
            NPCs.RAT_8209 to Items.RAT_14074,
            NPCs.LIZARD_8210 to Items.REPTILE_14070,
            NPCs.BLACKBIRD_8211 to Items.BLACKBIRD_14071,
            NPCs.SPIDER_8212 to Items.SPIDER_14073,
            NPCs.SNAIL_8213 to Items.SNAIL_14075,
        )

    private val PEN_LOCATIONS =
        mapOf(
            org.rs.consts.Scenery.BAT_PEN_39287 to Location(3222, 4513, 0),
            org.rs.consts.Scenery.RAT_PEN_39309 to Location(3240, 4513, 0),
            org.rs.consts.Scenery.REPTILE_PEN_39303 to Location(3240, 4504, 0),
            org.rs.consts.Scenery.BLACKBIRD_PEN_39281 to Location(3231, 4513, 0),
            org.rs.consts.Scenery.SPIDER_PEN_39297 to Location(3231, 4504, 0),
            org.rs.consts.Scenery.SNAIL_PEN_39292 to Location(3222, 4504, 0),
            org.rs.consts.Scenery.HOLDING_PEN_39314 to Location(3231, 4523, 0),
        )

    /**
     * Spawns the NPCs inside the Betty basement pens.
     */
    @JvmStatic
    fun spawnBettyBasementNPCs() {
        CREATURES[org.rs.consts.Scenery.BAT_PEN_39287] =
            NPC.create(NPCs.BAT_8208, PEN_LOCATIONS[org.rs.consts.Scenery.BAT_PEN_39287]!!)
        CREATURES[org.rs.consts.Scenery.RAT_PEN_39309] =
            NPC.create(NPCs.RAT_8209, PEN_LOCATIONS[org.rs.consts.Scenery.RAT_PEN_39309]!!)
        CREATURES[org.rs.consts.Scenery.REPTILE_PEN_39303] =
            NPC.create(NPCs.LIZARD_8210, PEN_LOCATIONS[org.rs.consts.Scenery.REPTILE_PEN_39303]!!)
        CREATURES[org.rs.consts.Scenery.BLACKBIRD_PEN_39281] =
            NPC.create(NPCs.BLACKBIRD_8211, PEN_LOCATIONS[org.rs.consts.Scenery.BLACKBIRD_PEN_39281]!!)
        CREATURES[org.rs.consts.Scenery.SPIDER_PEN_39297] =
            NPC.create(NPCs.SPIDER_8212, PEN_LOCATIONS[org.rs.consts.Scenery.SPIDER_PEN_39297]!!)
        CREATURES[org.rs.consts.Scenery.SNAIL_PEN_39292] =
            NPC.create(NPCs.SNAIL_8213, PEN_LOCATIONS[org.rs.consts.Scenery.SNAIL_PEN_39292]!!)

        CREATURES.values.forEach { it.init() }
    }

    /**
     * Resets the NPCs inside the Betty basement pens to their initial state.
     */
    @JvmStatic
    fun resetBettyBasementNPCs() {
        CREATURES.values.forEach { it.reset() }
        CREATURES.clear()
        spawnBettyBasementNPCs()
    }

    /**
     * Removes the NPCs inside the Betty basement pens.
     */
    @JvmStatic
    fun removeBettyNPCs() {
        CREATURES.values.forEach { it.reset() }
        CREATURES.clear()
    }

    /**
     * Handles the interaction when a player interacts with a pen to either add or remove creatures.
     *
     * @param player The player interacting with the pen.
     * @param targetId The ID of the pen.
     */
    @JvmStatic
    fun handlePenInteraction(
        player: Player,
        targetId: Int,
    ) {
        val currentNpc = CREATURES[targetId]

        if (currentNpc != null) {
            val npcItem = NPC_TO_ITEM[currentNpc.id]
            if (npcItem != null) {
                CREATURES.remove(targetId)
                currentNpc.clear()
                addItem(player, npcItem)

                val npcLocation = PEN_LOCATIONS[targetId]

                val message =
                    when (val npcName = getNPCName(currentNpc.id)) {
                        "Rat", "Bat" -> {
                            if (npcLocation == PEN_LOCATIONS[39414]) {
                                "You remove the $npcName from the pen. He doesn't seem very happy here."
                            } else {
                                "You remove the $npcName from the pen. He looks extremely happy here."
                            }
                        }
                        else -> {
                            if (npcLocation == PEN_LOCATIONS[39414]) {
                                "You remove the $npcName from the pen. She doesn't seem very happy here."
                            } else {
                                "You remove the $npcName from the pen. She looks extremely happy here."
                            }
                        }
                    }

                sendMessage(player, message)
            }
        } else {
            for ((npcId, itemId) in NPC_TO_ITEM) {
                if (player.inventory.contains(itemId, 1)) {
                    if (removeItem(player, itemId)) {
                        val location = PEN_LOCATIONS[targetId] ?: continue
                        val npc = NPC.create(npcId, location)
                        CREATURES[targetId] = npc
                        npc.init()

                        val message =
                            when (val npcName = getNPCName(npc.id)) {
                                "Rat", "Bat" -> {
                                    if (location == PEN_LOCATIONS[39414]) {
                                        "You put the $npcName into the pen. He doesn't seem very happy here."
                                    } else {
                                        "You put the $npcName into the pen. He looks extremely happy here."
                                    }
                                }
                                else -> {
                                    if (location == PEN_LOCATIONS[39414]) {
                                        "You put the $npcName into the pen. She doesn't seem very happy here."
                                    } else {
                                        "You put the $npcName into the pen. She looks extremely happy here."
                                    }
                                }
                            }

                        sendMessage(player, message)
                    }
                    break
                }
            }
        }
    }

    /**
     * Unlocks the Halloween emotes for the player.
     *
     * @param player The player who is unlocking the emote.
     * @param emote The emote being unlocked.
     * @param emoteName The name of the emote.
     */
    @JvmStatic
    fun unlockHalloweenEmotes(
        player: Player,
        emote: Emotes,
        emoteName: String,
    ) {
        player.emoteManager.unlock(emote)
        openInterface(player, Components.DOUBLEOBJBOX_131).also {
            sendModelOnInterface(player, Components.DOUBLEOBJBOX_131, 2, BROOM_MODEL, -1)
            sendAngleOnInterface(player, Components.DOUBLEOBJBOX_131, 2, 850, 200, 1500)
            sendString(
                player,
                "You've just learned the ${core.tools.DARK_RED}'$emoteName'</col> emote!",
                Components.DOUBLEOBJBOX_131,
                1,
            )
        }

        player.emoteManager.refresh()
    }

    /**
     * Handles the sweeping of a line of scenery.
     *
     * @param player The player performing the sweep action.
     * @param lineData A map containing the scenery data (ID and location).
     */
    @JvmStatic
    fun sweepLines(
        player: Player,
        lineData: HashMap<Int, LineScenery>,
    ) {
        lineData.values.forEach { (sceneryId, location) ->
            removeScenery(Scenery(sceneryId, location))
        }

        GlobalScope.launch {
            delay(30000)
            lineData.values.forEach { (sceneryId, location) ->
                val lineScenery = LineScenery(sceneryId, location)
                addScenery(lineScenery.sceneryId, lineScenery.location, lineScenery.rotation, lineScenery.type)
            }
        }
        sweepInteraction(player)
        checkAggieTask(player)
    }

    data class LineScenery(
        val sceneryId: Int,
        val location: Location,
        val rotation: Int = 2,
        val type: Int = 22,
    )

    @JvmStatic
    fun sweepFirstLines(player: Player) {
        val firstLineData =
            HashMap<Int, LineScenery>().apply {
                put(39363, LineScenery(39363, Location.create(3294, 4515, 0), 2))
                put(39364, LineScenery(39364, Location.create(3295, 4515, 0), 2))
                put(39365, LineScenery(39365, Location.create(3296, 4515, 0), 2))
                put(39366, LineScenery(39366, Location.create(3297, 4515, 0), 4))
            }
        sweepLines(player, firstLineData)
    }

    @JvmStatic
    fun sweepSecondLines(player: Player) {
        val secondLineData =
            HashMap<Int, LineScenery>().apply {
                put(39377, LineScenery(39377, Location.create(3297, 4514, 0), 3))
                put(39378, LineScenery(39378, Location.create(3296, 4514, 0), 1))
                put(39379, LineScenery(39379, Location.create(3296, 4513, 0), 1))
            }
        sweepLines(player, secondLineData)
    }

    @JvmStatic
    fun sweepThirdLines(player: Player) {
        val thirdLineData =
            HashMap<Int, LineScenery>().apply {
                put(39406, LineScenery(39406, Location.create(3299, 4511, 0), 3))
                put(39407, LineScenery(39407, Location.create(3298, 4511, 0), 1))
                put(39408, LineScenery(39408, Location.create(3298, 4510, 0), 1))
            }
        sweepLines(player, thirdLineData)
    }

    @JvmStatic
    fun sweepFourthLines(player: Player) {
        val fourthLineData =
            HashMap<Int, LineScenery>().apply {
                put(39413, LineScenery(39413, Location.create(3294, 4509, 0), 2))
                put(39414, LineScenery(39414, Location.create(3295, 4509, 0), 2))
                put(39415, LineScenery(39415, Location.create(3296, 4509, 0), 2))
                put(39416, LineScenery(39416, Location.create(3297, 4509, 0), 4))
            }
        sweepLines(player, fourthLineData)
    }

    /**
     * Initiates the sweeping interaction.
     *
     * @param player The player.
     */
    @JvmStatic
    fun sweepInteraction(player: Player) {
        if (getQuestStage(player, Quests.SWEPT_AWAY) < 2) {
            sendPlayerDialogue(player, "I should talk to Aggie before taking any action.")
            return
        }
        visualize(player, SWEEP_ANIMATION, Graphics.DUST_BROOM_EMOTE_1866)
        player.incrementAttribute(GameAttributes.QUEST_SWEPT_AWAY_LINE)
    }

    /**
     * Checks if the player has completed a task with Aggie.
     *
     * @param player The player.
     */
    @JvmStatic
    fun checkAggieTask(player: Player) {
        if (getAttribute(player, GameAttributes.QUEST_SWEPT_AWAY_LINE, -1) >= 3) {
            runTask(player, 2) {
                visualize(player, -1, BROOM_ENCHANTMENT_GFX)
                sendPlayerDialogue(player, "Woah, I felt that down to my toes!", FaceAnim.SCARED)
                setQuestStage(player, Quests.SWEPT_AWAY, 3)
            }
        }
    }

    /**
     * Checks if the player has completed the task with Gus.
     *
     * @param player The player.
     */
    @JvmStatic
    fun checkGusTask(player: Player) {
        if (getVarbit(player, VARBIT_NEWT_AND_TOAD_CRATE_LABEL) == 3 &&
            getVarbit(
                player,
                VARBIT_TOAD_CRATE_LABEL,
            ) == 2 &&
            getVarbit(player, VARBIT_NEWT_CRATE_LABEL) == 1
        ) {
            player.dialogueInterpreter.sendDialogue(
                NPCs.GUS_8205,
                "Hurray! I do believe that you've labelled the creates",
                "correctly! Ms Hetty will be so pleased.",
            )
            removeAttribute(player, GameAttributes.QUEST_SWEPT_AWAY_LABELS)
            setAttribute(player, GameAttributes.QUEST_SWEPT_AWAY_LABELS_COMPLETE, true)
            addDialogueAction(player) { p, _ ->
                p.dialogueInterpreter.sendDialogue(
                    NPCs.GUS_8205,
                    "Now, you need a newt, right? Feel free to take one",
                    "from the newts-only create - the other ones aren't",
                    "apprepriate for ointments and potions.",
                )
                return@addDialogueAction

            }
        } else {
            openDialogue(player, SweptAwayPlugin.GusSupportDialogue())
        }
    }

    /**
     * Handles the crate labeling process.
     *
     * @param player The player.
     * @param used The item used.
     * @param varbit The varbit associated with the crate.
     * @param value The value set for varbit.
     */
    @JvmStatic
    fun handleCrateLabelling(
        player: Player,
        used: Int,
        varbit: Int,
        value: Int,
    ) {
        if (removeItem(player, used.asItem())) {
            sendMessage(player, "You place the label on the crate.")
            setVarbit(player, varbit, value, true)
        }
    }

    /**
     * Resets Gus's task progress.
     *
     * @param player The player.
     */
    @JvmStatic
    fun resetGusTask(player: Player) {
        lock(player, 6)
        GameWorld.Pulser.submit(
            object : Pulse() {
                var counter = 0

                override fun pulse(): Boolean {
                    when (counter++) {
                        0 -> openInterface(player, Components.FADE_TO_BLACK_120)
                        3 -> {
                            setVarbit(player, VARBIT_NEWT_CRATE_LABEL, 0, true)
                            setVarbit(player, VARBIT_TOAD_CRATE_LABEL, 0, true)
                            setVarbit(player, VARBIT_NEWT_AND_TOAD_CRATE_LABEL, 0, true)

                            addItemOrDrop(player, Items.NEWT_LABEL_14065, 1)
                            addItemOrDrop(player, Items.TOAD_LABEL_14066, 1)
                            addItemOrDrop(player, Items.NEWTS_AND_TOADS_LABEL_14067, 1)
                        }

                        6 -> {
                            unlock(player)
                            openInterface(player, Components.FADE_FROM_BLACK_170)
                            return true
                        }
                    }
                    return false
                }
            },
        )
    }
}
