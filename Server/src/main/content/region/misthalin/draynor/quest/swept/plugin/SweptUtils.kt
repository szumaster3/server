package content.region.misthalin.draynor.quest.swept.plugin

import content.data.GameAttributes
import core.api.*
import core.api.getNPCName
import core.api.getQuestStage
import core.api.setQuestStage
import core.api.unlockEmote
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
import shared.consts.*
import shared.consts.Scenery as Objects

internal object SweptUtils {

    const val PEN_TABLE_INTERFACE = Components.MAGICAL_SLOPE_799
    const val BROOM_MODEL = 42783
    const val SWEEP_ANIMATION = Animations.SWEEP_BROOM_10532
    const val BROOM_ENCHANTMENT_GFX = 1865
    const val VARBIT_NEWT_CRATE_LABEL = 5460
    const val VARBIT_TOAD_CRATE_LABEL = 5461
    const val VARBIT_NEWT_AND_TOAD_CRATE_LABEL = 5462
    const val VARBIT_RIMMINGTON_TRAPDOOR = 5465
    const val VARBIT_LOTTIE_CHEST = 5467
    const val VARBIT_BETTY_TRAPDOOR = 5469

    const val STIR_ANIMATION = 10546
    const val ENCHANT_BROOMSTICK_ANIMATION = 10535
    const val STIR_WITH_BROOMSTICK_ANIMATION = 10543
    const val FILL_GOULASH_FROM_CAULDRON_A = 10544
    const val FILL_GOULASH_FROM_CAULDRON_B = 10545
    const val TAKE_FROG_FROM_CRATE_ANIMATION = 10550
    const val TAKE_NEWT_FROM_CRATE_ANIMATION = 10551

    const val NT_CRATE = shared.consts.Scenery.WRONGLY_LABELLED_CRATE_NEWTS_AND_TOADS_39336
    const val N_CRATE = shared.consts.Scenery.WRONGLY_LABELLED_CRATE_NEWTS_39335
    const val T_CRATE = shared.consts.Scenery.WRONGLY_LABELLED_CRATE_TOADS_39334

    val GUS_CRATES = intArrayOf(39334, 39335, 39336)
    val GUS_CRATES_LABELLED = intArrayOf(39337, 39339, 39341)

    val CREATURES = mutableMapOf<Pen, Pair<NPC, Int>>()

    enum class Pen(val penId: Int, val npcId: Int, val itemId: Int, val location: Location, val modelId: Int) {
        BAT(Objects.BAT_PEN_39287, NPCs.BAT_8208, Items.BAT_14072, Location(3222, 4513, 0), 42750),
        RAT(Objects.RAT_PEN_39309, NPCs.RAT_8209, Items.RAT_14074, Location(3240, 4513, 0), 42747),
        REPTILE(Objects.REPTILE_PEN_39303, NPCs.LIZARD_8210, Items.REPTILE_14070, Location(3240, 4504, 0), 42748),
        BLACKBIRD(Objects.BLACKBIRD_PEN_39281, NPCs.BLACKBIRD_8211, Items.BLACKBIRD_14071, Location(3231, 4513, 0), 42746),
        SPIDER(Objects.SPIDER_PEN_39297, NPCs.SPIDER_8212, Items.SPIDER_14073, Location(3231, 4504, 0), 42751),
        SNAIL(Objects.SNAIL_PEN_39292, NPCs.SNAIL_8213, Items.SNAIL_14075, Location(3222, 4504, 0), 42749),
        HOLDING(Objects.HOLDING_PEN_39314, -1, -1, Location(3231, 4523, 0), -1);

        companion object {
            private val map = values().associateBy { it.penId }
            fun fromId(id: Int) = map[id]
        }
    }

    private val COMPONENT_TO_PEN = mapOf(
        10 to Objects.HOLDING_PEN_39314,
        11 to Objects.BAT_PEN_39287,
        12 to Objects.BLACKBIRD_PEN_39281,
        13 to Objects.RAT_PEN_39309,
        14 to Objects.SNAIL_PEN_39292,
        15 to Objects.SPIDER_PEN_39297,
        16 to Objects.REPTILE_PEN_39303
    )

    @JvmStatic
    fun spawnBettyBasementNPCs() {
        Pen.values()
            .filter { it.npcId != -1 }
            .forEach { pen ->
                val npc = NPC.create(pen.npcId, pen.location)
                CREATURES[pen] = Pair(npc, pen.itemId)
                npc.init()
            }
    }

    @JvmStatic
    fun resetBettyBasementNPCs() {
        CREATURES.values.forEach { it.first.reset() }
        CREATURES.clear()
        spawnBettyBasementNPCs()
    }

    @JvmStatic
    fun removeBettyNPCs() {
        CREATURES.values.forEach { it.first.reset() }
        CREATURES.clear()
    }

    @JvmStatic
    fun handlePenInteraction(player: Player, pen: Pen) {
        val existing = CREATURES[pen]

        if (existing != null) {
            val (npc, storedItem) = existing
            CREATURES.remove(pen)
            npc.clear()
            if (storedItem != -1) addItem(player, storedItem)

            val npcName = getNPCName(npc.id)
            val message = if (pen == Pen.HOLDING) {
                "You remove the $npcName from the pen."
            } else {
                "You remove the $npcName from the pen."
            }

            sendMessage(player, message)
            player.incrementAttribute(GameAttributes.QUEST_SWEPT_AWAY_CREATURE_INTER)
            return
        }

        if (CREATURES.values.any { it.first.id == pen.npcId && pen.npcId != -1 }) {
            sendMessage(player, "You can't put a creature into a pen that already has one inside.")
            return
        }

        val entry: Pen = if (pen == Pen.HOLDING) {
            Pen.values().firstOrNull { it.itemId != -1 && player.inventory.contains(it.itemId, 1) }
                ?: run {
                    sendMessage(player, "You have no creatures to put into the pen.")
                    return
                }
        } else pen

        if (entry.itemId != -1) removeItem(player, entry.itemId)

        val npc = NPC.create(entry.npcId, pen.location)
        val itemToStore = if (entry.itemId != -1) entry.itemId else -1
        CREATURES[pen] = Pair(npc, itemToStore)
        npc.init()

        val npcName = getNPCName(npc.id)
        val pronoun = if (npcName in listOf("Rat", "Bat")) "He" else "She"
        val correctPen = Pen.values().firstOrNull { it.npcId == npc.id }
        val message = if (pen != Pen.HOLDING && pen == correctPen) {
            "You put the $npcName into the pen. $pronoun looks extremely happy here."
        } else {
            "You put the $npcName into the pen. $pronoun doesn't seem very happy here."
        }
        sendMessage(player, message)
    }

    @JvmStatic
    fun checkMagicSlate(player: Player) {
        openInterface(player, PEN_TABLE_INTERFACE)
        COMPONENT_TO_PEN.forEach { (component, penId) ->
            val pen = Pen.fromId(penId) ?: return@forEach
            val npcPair = CREATURES[pen]
            val modelId = npcPair?.first?.let { pen.modelId } ?: -1
            sendModelOnInterface(player, PEN_TABLE_INTERFACE, component, modelId, -1)
        }
    }

    data class LineScenery(
        val sceneryId: Int,
        val location: Location,
        val rotation: Int = 2,
        val type: Int = 22
    )

    @JvmStatic
    fun sweepLines(player: Player, lineData: Map<Int, LineScenery>) {
        lineData.values.forEach { removeScenery(Scenery(it.sceneryId, it.location)) }

        GlobalScope.launch {
            delay(30_000)
            lineData.values.forEach { addScenery(it.sceneryId, it.location, it.rotation, it.type) }
        }

        sweepInteraction(player)
        checkAggieTask(player)
    }

    @JvmStatic
    fun sweepLineSet(player: Player, lines: List<LineScenery>) {
        sweepLines(player, lines.associateBy { it.sceneryId })
    }

    @JvmStatic
    fun sweepFirstLines(player: Player) {
        sweepLineSet(
            player,
            listOf(
                LineScenery(39363, Location.create(3294, 4515, 0), 2),
                LineScenery(39364, Location.create(3295, 4515, 0), 2),
                LineScenery(39365, Location.create(3296, 4515, 0), 2),
                LineScenery(39366, Location.create(3297, 4515, 0), 4),
            )
        )
    }

    @JvmStatic
    fun sweepSecondLines(player: Player) {
        sweepLineSet(
            player,
            listOf(
                LineScenery(39377, Location.create(3297, 4514, 0), 3),
                LineScenery(39378, Location.create(3296, 4514, 0), 1),
                LineScenery(39379, Location.create(3296, 4513, 0), 1),
            )
        )
    }

    @JvmStatic
    fun sweepThirdLines(player: Player) {
        sweepLineSet(
            player,
            listOf(
                LineScenery(39406, Location.create(3299, 4511, 0), 3),
                LineScenery(39407, Location.create(3298, 4511, 0), 1),
                LineScenery(39408, Location.create(3298, 4510, 0), 1),
            )
        )
    }

    @JvmStatic
    fun sweepFourthLines(player: Player) {
        sweepLineSet(
            player,
            listOf(
                LineScenery(39413, Location.create(3294, 4509, 0), 2),
                LineScenery(39414, Location.create(3295, 4509, 0), 2),
                LineScenery(39415, Location.create(3296, 4509, 0), 2),
                LineScenery(39416, Location.create(3297, 4509, 0), 4),
            )
        )
    }


    @JvmStatic
    fun sweepInteraction(player: Player) {
        if (getQuestStage(player, Quests.SWEPT_AWAY) < 2) {
            sendPlayerDialogue(player, "I should talk to Aggie before taking any action.")
            return
        }
        visualize(player, SWEEP_ANIMATION, Graphics.DUST_BROOM_EMOTE_1866)
        player.incrementAttribute(GameAttributes.QUEST_SWEPT_AWAY_LINE)
    }

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

    @JvmStatic
    fun checkGusTask(player: Player) {
        if (
            getVarbit(player, VARBIT_NEWT_AND_TOAD_CRATE_LABEL) == 3 &&
            getVarbit(player, VARBIT_TOAD_CRATE_LABEL) == 2 &&
            getVarbit(player, VARBIT_NEWT_CRATE_LABEL) == 1
        ) {
            player.dialogueInterpreter.sendDialogue(
                NPCs.GUS_8205,
                "Hurray! I do believe that you've labelled the crates correctly! Ms Hetty will be so pleased."
            )
            removeAttribute(player, GameAttributes.QUEST_SWEPT_AWAY_LABELS)
            setAttribute(player, GameAttributes.QUEST_SWEPT_AWAY_LABELS_COMPLETE, true)
        } else {
            openDialogue(player, SweptAwayPlugin.GusSupportDialogue())
        }
    }

    @JvmStatic
    fun handleCrateLabelling(player: Player, used: Int, varbit: Int, value: Int) {
        if (removeItem(player, used.asItem())) {
            sendMessage(player, "You place the label on the crate.")
            setVarbit(player, varbit, value, true)
        }
    }

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
            }
        )
    }

    @JvmStatic
    fun unlockHalloweenEmotes(player: Player, emote: Emotes, emoteName: String) {
        unlockEmote(player, emote)
        openInterface(player, Components.DOUBLEOBJBOX_131).also {
            sendModelOnInterface(player, Components.DOUBLEOBJBOX_131, 2, BROOM_MODEL, -1)
            sendAngleOnInterface(player, Components.DOUBLEOBJBOX_131, 2, 850, 200, 1500)
            sendString(player, "You've just learned the ${core.tools.DARK_RED}'$emoteName'</col> emote!", Components.DOUBLEOBJBOX_131, 1)
        }
    }
}
