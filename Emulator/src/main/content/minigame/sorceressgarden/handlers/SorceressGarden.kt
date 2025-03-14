package content.minigame.sorceressgarden.handlers

import content.minigame.sorceressgarden.handlers.SorceressGarden.SeasonDefinitions.Companion.forGateId
import content.minigame.sorceressgarden.dialogue.SorceressApprenticeDialogue
import core.api.*
import core.api.ui.setMinimapState
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.global.action.DoorActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.NodeUsageEvent
import core.game.interaction.UseWithHandler
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.node.scenery.Scenery
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.plugin.Plugin
import core.tools.RandomFunction
import org.rs.consts.*

class SorceressGarden : InteractionListener {
    private val GATES =
        intArrayOf(
            org.rs.consts.Scenery.GATE_21709,
            org.rs.consts.Scenery.GATE_21753,
            org.rs.consts.Scenery.GATE_21731,
            org.rs.consts.Scenery.GATE_21687,
        )
    private val SQIRK_TREES =
        intArrayOf(
            org.rs.consts.Scenery.SQ_IRK_TREE_21767,
            org.rs.consts.Scenery.SQ_IRK_TREE_21768,
            org.rs.consts.Scenery.SQ_IRK_TREE_21769,
            org.rs.consts.Scenery.SQ_IRK_TREE_21766,
        )
    private val HERBS_ITEMS =
        intArrayOf(
            Items.GRIMY_GUAM_199,
            Items.GRIMY_MARRENTILL_201,
            Items.GRIMY_TARROMIN_203,
            Items.GRIMY_HARRALANDER_205,
            Items.GRIMY_RANARR_207,
            Items.GRIMY_IRIT_209,
            Items.GRIMY_AVANTOE_211,
            Items.GRIMY_KWUARM_213,
            Items.GRIMY_CADANTINE_215,
            Items.GRIMY_DWARF_WEED_217,
            Items.GRIMY_TORSTOL_219,
            Items.GRIMY_LANTADYME_2485,
            Items.GRIMY_TOADFLAX_3049,
            Items.GRIMY_SNAPDRAGON_3051,
        )
    private val HERB_DEFINITIONS = HashMap<Int, HerbDefinition>()
    private val SEASON_DEFINITIONS = HashMap<Int, SeasonDefinitions>()

    private val APPRENTICE = NPCs.APPRENTICE_5532
    private val FOUNTAIN = org.rs.consts.Scenery.FOUNTAIN_21764
    private val SHELVES = org.rs.consts.Scenery.SHELVES_21794

    init {
        for (herb in HerbDefinition.values()) {
            HERB_DEFINITIONS[herb.id] = herb
        }
        for (season in SeasonDefinitions.values()) {
            SEASON_DEFINITIONS[season.treeId] = season
        }
    }

    override fun defineListeners() {
        on(GATES, IntType.SCENERY, "open") { player, node ->
            val def = SEASON_DEFINITIONS[(node as Scenery).id]?.let { forGateId(it.gateId) }
            if (def != null) {
                if (player.getSkills().getStaticLevel(Skills.THIEVING) < def.level) {
                    sendItemDialogue(
                        player,
                        Items.HIGHWAYMAN_MASK_10692,
                        "You need Thieving level of ${def.level} to pick the lock of this gate.",
                    )
                    return@on true
                }
                DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
            }
            return@on true
        }

        on(APPRENTICE, IntType.NPC, "teleport") { player, node ->
            val npc = node as NPC
            if (player.getSavedData().globalData.hasSpokenToApprentice()) {
                SorceressApprenticeDialogue.teleport(npc, player)
            } else {
                sendNPCDialogue(player, node.id, "I can't do that now, I'm far too busy sweeping.")
            }
            return@on true
        }

        on(HERBS_ITEMS, IntType.SCENERY, "pick") { player, node ->
            val herbDef = HERB_DEFINITIONS[node.id]
            if (herbDef != null) {
                handleHerbPicking(player, node.asScenery(), herbDef)
            }
            return@on true
        }

        on(SQIRK_TREES, IntType.SCENERY, "pick-fruit") { player, node ->
            val def = SEASON_DEFINITIONS[node.id]
            if (def != null) {
                handleSqirkTreeInteraction(player, def)
            }
            return@on true
        }

        on(FOUNTAIN, IntType.SCENERY, "drink-from") { player, _ ->
            player.lock()
            GameWorld.Pulser.submit(
                object : Pulse(1, player) {
                    var counter = 0

                    override fun pulse(): Boolean {
                        when (counter++) {
                            1 -> animate(player, DRINK_ANIM)
                            // 4 -> sendGraphics(
                            //     Graphics(
                            //         111,
                            //         100,
                            //         1
                            //     ), player.location)
                            5 -> animate(player, TELE)
                            6 -> openOverlay(player, Components.FADE_TO_BLACK_115)
                            7 -> setMinimapState(player, 2)
                            9 -> teleport(player, Location(3321, 3141, 0))
                            11 -> {
                                unlock(player)
                                resetAnimator(player)
                                closeInterface(player)
                                closeOverlay(player)
                                setMinimapState(player, 0)
                                return true
                            }
                        }
                        return false
                    }
                },
            )

            return@on true
        }

        on(SHELVES, IntType.SCENERY, "search") { player, _ ->
            if (freeSlots(player) < 1) {
                sendMessage(player, "You don't have enough space in your inventory to take a beer glass.")
            } else {
                sendMessage(player, "You take an empty beer glass off the shelves.")
                addItem(player, Items.BEER_GLASS_1919, 1)
            }

            return@on true
        }
    }

    private fun handleSqirkTreeInteraction(
        player: Player,
        def: SeasonDefinitions,
    ) {
        player.lock()
        player.logoutListeners["garden"] = { it.location = def.respawn }
        animate(player, PICK_FRUIT)
        rewardXP(player, Skills.THIEVING, def.exp)
        rewardXP(player, Skills.FARMING, def.farmExp)

        GameWorld.Pulser.submit(
            object : Pulse(2, player) {
                var counter = 0

                override fun pulse(): Boolean {
                    when (counter) {
                        1 -> {
                            addItem(player, def.fruitId)
                            openOverlay(player, Components.FADE_TO_BLACK_115)
                            setMinimapState(player, 2)
                        }

                        3 -> player.properties.teleportLocation = def.respawn
                        4 -> {
                            unlock(player)
                            player.logoutListeners.remove("garden")
                            sendMessage(player, "An elemental force emanating from the garden teleports you away.")
                            setMinimapState(player, 0)
                            closeInterface(player)
                            closeOverlay(player)
                            return true
                        }
                    }
                    counter++
                    return false
                }
            },
        )
    }

    private fun handleHerbPicking(
        player: Player,
        scenery: Scenery,
        herbDef: HerbDefinition,
    ) {
        player.lock()
        player.logoutListeners["garden"] = { it.location = herbDef.respawn }
        player.animate(ANIMATION)
        player.skills.addExperience(Skills.FARMING, herbDef.exp, true)

        GameWorld.Pulser.submit(
            object : Pulse(2, player) {
                var counter = 0

                override fun pulse(): Boolean {
                    when (counter) {
                        1 -> {
                            addItem(player, HERBS_ITEMS[RandomFunction.random(0, HERBS_ITEMS.size)])
                            sendMessage(player, "You pick up a herb.")
                            openOverlay(player, Components.FADE_TO_BLACK_115)
                            setMinimapState(player, 2)
                        }

                        3 -> {
                            player.properties.teleportLocation = herbDef.respawn
                            unlock(player)
                        }

                        4 -> {
                            sendMessage(player, "An elemental force emanating from the garden teleports you away.")
                            setMinimapState(player, 0)
                            closeInterface(player)
                            closeOverlay(player)
                            player.logoutListeners.remove("garden")
                            return true
                        }
                    }
                    counter++
                    return false
                }
            },
        )
    }

    enum class HerbDefinition(
        val id: Int,
        val exp: Double,
        val respawn: Location,
    ) {
        WINTER(id = 21671, exp = 30.0, respawn = Location(2907, 5470, 0)),
        SPRING(id = 21668, exp = 40.0, respawn = Location(2916, 5473, 0)),
        AUTUMN(id = 21670, exp = 50.0, respawn = Location(2913, 5467, 0)),
        SUMMER(id = 21669, exp = 60.0, respawn = Location(2910, 5476, 0)),
        ;

        companion object {
            fun forId(id: Int): HerbDefinition? {
                for (def in values()) {
                    if (def.id == id) {
                        return def
                    }
                }
                return null
            }
        }
    }

    enum class SeasonDefinitions(
        val treeId: Int,
        val level: Int,
        val farmExp: Double,
        val exp: Double,
        val fruitId: Int,
        val juiceId: Int,
        val fruitAmt: Int,
        val boost: Int,
        val energy: Int,
        val osmanExp: Double,
        val gateId: Int,
        val respawn: Location,
    ) {
        WINTER(21769, 1, 30.0, 70.0, 10847, 10851, 5, 0, 10, 350.0, 21709, Location(2907, 5470, 0)),
        SPRING(21767, 25, 40.0, 337.5, 10844, 10848, 4, 1, 20, 1350.0, 21753, Location(2916, 5473, 0)),
        AUTUMN(21768, 45, 50.0, 783.3, 10846, 10850, 3, 2, 30, 2350.0, 21731, Location(2913, 5467, 0)),
        SUMMER(21766, 65, 60.0, 1500.0, 10845, 10849, 2, 3, 40, 3000.0, 21687, Location(2910, 5476, 0)),
        ;

        companion object {
            @JvmStatic
            fun forFruitId(fruitId: Int): SeasonDefinitions? {
                for (def in values()) {
                    if (fruitId == def.fruitId) return def
                }
                return null
            }

            @JvmStatic
            fun forGateId(gateId: Int): SeasonDefinitions? {
                for (def in values()) {
                    if (gateId == def.gateId) return def
                }
                return null
            }

            @JvmStatic
            fun forJuiceId(juiceId: Int): SeasonDefinitions? {
                for (def in values()) {
                    if (juiceId == def.juiceId) return def
                }
                return null
            }

            @JvmStatic
            fun forTreeId(treeId: Int): SeasonDefinitions? {
                for (def in values()) {
                    if (treeId == def.treeId) return def
                }
                return null
            }
        }
    }

    class SqirkJuicePlugin :
        UseWithHandler(
            Items.SPRING_SQIRK_10844,
            Items.SUMMER_SQIRK_10845,
            Items.AUTUMN_SQIRK_10846,
            Items.WINTER_SQIRK_10847,
        ) {
        override fun handle(event: NodeUsageEvent): Boolean {
            val item: Item = event.usedItem
            val with: Item = event.baseItem
            val player: Player = event.player
            val def = SeasonDefinitions.forFruitId(item.id)
            if (with == null || player == null || def == null) return true
            val amt = player.inventory.getAmount(item)
            if (!player.inventory.containItems(Items.BEER_GLASS_1919)) {
                player.dialogueInterpreter.open(43382, 0)
                return true
            }
            if (amt < def.fruitAmt) {
                player.dialogueInterpreter.open(43382, 1, item.id)
                return true
            }
            player.animate(CRUSH_ITEM)
            player.skills.addExperience(Skills.COOKING, 5.0, true)
            player.inventory.remove(Item(item.id, def.fruitAmt))
            player.inventory.remove(Item(1919))
            player.inventory.add(Item(def.juiceId))
            player.dialogueInterpreter.sendDialogue("You squeeze " + def.fruitAmt + " sq'irks into an empty glass.")
            return true
        }

        @Throws(Throwable::class)
        override fun newInstance(arg: Any?): Plugin<Any?> {
            addHandler(233, ITEM_TYPE, this)
            return this
        }

        companion object {
            private val CRUSH_ITEM = Animation(Animations.PESTLE_MORTAR_364)
        }
    }

    companion object {
        private val ANIMATION = Animation(Animations.MULTI_BEND_OVER_827)
        private val DRINK_ANIM = Animation(Animations.SORCERESS_GARDEN_DRINK_WATER_5796)
        private val TELE = Animation(714)
        private val PICK_FRUIT = Animation(Animations.PICK_FROM_FRUIT_TREE_2280)
    }
}

class SqirkMakingDialogue(
    player: Player? = null,
) : Dialogue(player) {
    private var dialogueId = 0
    private var definition: SorceressGarden.SeasonDefinitions? = null

    override fun getIds(): IntArray {
        return intArrayOf(43382)
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (dialogueId) {
            0 -> end()
            1 ->
                when (stage) {
                    0 -> {
                        interpreter.sendDialogue(
                            "You need " + definition!!.fruitAmt + " sq'irks of this kind to fill a glass of juice.",
                        )
                        stage = 1
                    }

                    1 -> end()
                }
        }
        return true
    }

    override fun open(vararg args: Any): Boolean {
        dialogueId = args[0] as Int
        when (dialogueId) {
            0 ->
                player(
                    FaceAnim.THINKING,
                    "I should get an empty beer glass to",
                    "hold the juice before I squeeze the fruit.",
                )
            1 -> {
                definition = SorceressGarden.SeasonDefinitions.forFruitId(args[1] as Int)
                if (definition == null) end()
                player(FaceAnim.THINKING, "I think I should wait till I have", "enough fruits to make a full glass.")
            }
        }
        stage = 0
        return true
    }
}
