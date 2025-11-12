package content.region.misthalin.draynor.quest.anma.plugin

import content.data.skill.SkillingTool.Companion.getAxe
import content.region.misthalin.draynor.quest.anma.AnimalMagnetism
import core.api.*
import core.cache.def.impl.ItemDefinition
import core.cache.def.impl.NPCDefinition
import core.cache.def.impl.SceneryDefinition
import core.game.component.Component
import core.game.component.ComponentDefinition
import core.game.component.ComponentPlugin
import core.game.interaction.NodeUsageEvent
import core.game.interaction.OptionHandler
import core.game.interaction.UseWithHandler
import core.game.node.Node
import core.game.node.entity.impl.Animator.Priority
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.TeleportManager
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import core.game.world.map.Direction
import core.game.world.map.Location
import core.game.world.map.zone.MapZone
import core.game.world.map.zone.ZoneBorders
import core.game.world.map.zone.ZoneBuilder
import core.game.world.update.flag.context.Animation
import core.plugin.ClassScanner.definePlugin
import core.plugin.Initializable
import core.plugin.Plugin
import core.tools.RandomFunction
import shared.consts.*

/** The Animal magnetism plugin. */
@Initializable
class AnimalMagnetismPlugin : OptionHandler() {

    @Throws(Throwable::class)
    override fun newInstance(arg: Any?): Plugin<Any> {
        ItemDefinition.forId(Items.BUTTONS_688).handlers["option:polish"] = this
        ItemDefinition.forId(Items.ECTOPHIAL_4251).handlers["option:empty"] = this
        ItemDefinition.forId(Items.ECTOPHIAL_4251).handlers["option:drop"] = this
        ItemDefinition.forId(Items.ECTOPHIAL_4252).handlers["option:drop"] = this
        NPCDefinition.forId(NPCs.AVA_5198).handlers["option:trade"] = this
        SceneryDefinition.forId(Scenery.MEMORIAL_5167).handlers["option:push"] = this
        AnimalMagnetism.RESEARCH_NOTES.definition.handlers["option:translate"] = this
        ItemDefinition.forId(AnimalMagnetism.CRONE_AMULET.id).handlers["option:wear"] = this
        ItemDefinition.forId(AnimalMagnetism.CRONE_AMULET.id).handlers["option:equip"] = this
        return this
    }

    override fun handle(player: Player?, node: Node?, option: String?): Boolean {
        player ?: return false
        node ?: return false
        option ?: return false

        when (node.id) {
            Scenery.MEMORIAL_5167 -> {
                if (!hasRequirement(player, Quests.CREATURE_OF_FENKENSTRAIN)) {
                    return true
                }
                teleport(player, Location(3577, 9927), TeleportManager.TeleportType.INSTANT)
            }
            NPCs.AVA_5198,
            NPCs.AVA_5199 -> {
                if (getQuestStage(player, Quests.ANIMAL_MAGNETISM) == 0) {
                    player.dialogueInterpreter.sendDialogues(
                        node as NPC,
                        null,
                        "Hello there, I'm busy with my research. Come back in a",
                        "bit, could you?"
                    )
                    return true
                }
                openNpcShop(player, node.id)
            }
            Items.CRONE_MADE_AMULET_10500 ->
                sendMessage(player, "Perhaps you should wait a few hundred years or so?", null)
            Items.RESEARCH_NOTES_10492 -> open(player)
            Items.BUTTONS_688 -> {
                lock(player, 1)
                if (getStatLevel(player, Skills.CRAFTING) < 3) {
                    sendMessage(
                        player,
                        "You need a Crafting level of at least 3 in order to do that.",
                        null
                    )
                    return true
                }
                rewardXP(player, Skills.CRAFTING, 5.0)
                player.inventory.replace(AnimalMagnetism.POLISHED_BUTTONS, (node as Item).slot)
            }
        }
        return true
    }

    private fun clearCache(player: Player) {
        removeAttribute(player, "note-cache")
        removeAttribute(player, "note-disabled")
    }

    /**
     * Open.
     *
     * @param player the player
     */
    fun open(player: Player) {
        clearCache(player)
        openInterface(player, Components.ANMA_RGB_480)
        sendMessage(player, "You fiddle with the notes.", null)
    }

    /**
     * The hammer magnet plugin.
     */
    class HammerMagnetPlugin : UseWithHandler(Items.HAMMER_2347) {
        override fun newInstance(arg: Any?): Plugin<Any> {
            ZoneBuilder.configure(
                object : MapZone("rimmington mine", true) {
                    override fun configure() {
                        register(ZoneBorders(2970, 3230, 2984, 3249))
                    }
                }
            )
            addHandler(AnimalMagnetism.SELECTED_IRON.id, ITEM_TYPE, this)
            return this
        }

        override fun handle(event: NodeUsageEvent?): Boolean {
            event ?: return false
            val player = event.player
            animate(player, ANIMATION, false)
            lock(player, ANIMATION.definition.getDurationTicks())
            Pulser.submit(
                object : Pulse(ANIMATION.definition.getDurationTicks(), player) {
                    override fun pulse(): Boolean {
                        if (!player.zoneMonitor.isInZone("rimmington mine")) {
                            sendMessage(
                                player,
                                "You aren't in the right area for this to work.",
                                null
                            )
                        } else {
                            if (player.direction != Direction.NORTH) {
                                sendMessage(
                                    player,
                                    "You think that facing North might work better.",
                                    null
                                )
                            } else {
                                player.inventory.replace(
                                    Item(Items.BAR_MAGNET_10489),
                                    event.usedItem.slot
                                )
                                sendMessage(
                                    player,
                                    "You hammer the iron bar and create a magnet.",
                                    null
                                )
                            }
                        }
                        return true
                    }
                }
            )
            return true
        }

        companion object {
            private val ANIMATION = Animation(Animations.ANMA_MAKE_IRON_MAGNET_5365)
        }
    }

    /**
     * The undead tree plugin.
     */
    class UndeadTreePlugin :
        UseWithHandler(
            Items.MITHRIL_AXE_1355,
            Items.ADAMANT_AXE_1357,
            Items.RUNE_AXE_1359,
            Items.DRAGON_AXE_6739
        ) {
        override fun newInstance(arg: Any?): Plugin<Any> {
            definePlugin(
                object : OptionHandler() {
                    override fun newInstance(arg: Any?): Plugin<Any> {
                        NPCDefinition.forId(NPCs.UNDEAD_TREE_5208).handlers["option:chop"] = this
                        return this
                    }

                    override fun handle(player: Player?, node: Node?, option: String?): Boolean {
                        player ?: return false
                        node ?: return false
                        option ?: return false

                        val quest = player.getQuestRepository().getQuest(Quests.ANIMAL_MAGNETISM)
                        if (quest.getStage(player) <= 28) {
                            val tool = getAxe(player)
                            if (tool == null || tool.ordinal < 4) {
                                sendMessage(
                                    player,
                                    "You don't have the required axe in order to do that.",
                                    null
                                )
                                return true
                            }
                            val animation = getAnimation(tool.animation)
                            player.animate(animation, 2)
                            if (quest.getStage(player) == 28) {
                                quest.setStage(player, 29)
                            }
                            sendMessage(
                                player,
                                "The axe bounces off the undead wood." +
                                        (if (
                                            quest.getStage(player) == 28 || quest.getStage(player) == 29
                                        )
                                            " I should report this to Ava."
                                        else ""),
                                null
                            )
                            return true
                        }
                        if (freeSlots(player) < 1) {
                            sendMessage(player, "Your inventory is full right now.", null)
                            return true
                        }
                        if (
                            !inInventory(player, Items.BLESSED_AXE_10491, 1) &&
                            !inEquipment(player, Items.BLESSED_AXE_10491, 1)
                        ) {
                            sendMessage(
                                player,
                                "You don't have an axe which could possibly affect this wood.",
                                null
                            )
                            return true
                        }
                        val animation = getAnimation(Items.MITHRIL_AXE_1355)
                        lock(player, animation!!.definition.getDurationTicks())
                        if (RandomFunction.random(10) < 3) {
                            sendMessage(
                                player,
                                "You almost remove a suitable twig, but you don't quite manage it.",
                                null
                            )
                        } else {
                            addItem(player, Items.UNDEAD_TWIGS_10490, 1, Container.INVENTORY)
                            sendMessage(player, "You cut some undead twigs.", null)
                            rewardXP(player, Skills.WOODCUTTING, 5.0)
                        }
                        player.animate(animation, 2)
                        return true
                    }
                }
            )
            addHandler(NPCs.UNDEAD_TREE_5208, NPC_TYPE, this)
            return this
        }

        override fun handle(event: NodeUsageEvent?): Boolean {
            event ?: return false

            val player = event.player
            val animation = getAnimation(event.usedItem.id)
            val quest = player.getQuestRepository().getQuest(Quests.ANIMAL_MAGNETISM)
            player.animate(animation, 2)
            if (quest.getStage(player) == 28) {
                quest.setStage(player, 29)
            }
            sendMessage(
                player,
                "The axe bounces off the undead wood." +
                        (if (quest.getStage(player) == 28 || quest.getStage(player) == 29)
                            " I should report this to Ava."
                        else ""),
                null
            )
            return true
        }

        private fun getAnimation(itemId: Int): Animation? {
            for (i in IDS.indices) {
                if (IDS[i] == itemId) {
                    return Animation(Animations.ANMA_BLESSED_AXE_5366 + i, Priority.HIGH)
                }
            }
            return null
        }

        companion object {
            private val IDS =
                intArrayOf(
                    Items.MITHRIL_AXE_1355,
                    Items.ADAMANT_AXE_1357,
                    Items.RUNE_AXE_1359,
                    Items.DRAGON_AXE_6739
                )
        }
    }

    /**
     * The research note handler.
     */
    class ResearchNoteHandler : ComponentPlugin() {
        override fun newInstance(arg: Any?): Plugin<Any> {
            ComponentDefinition.forId(Components.ANMA_RGB_480).plugin = this
            return this
        }

        override fun handle(
            player: Player,
            component: Component,
            opcode: Int,
            button: Int,
            slot: Int,
            itemId: Int
        ): Boolean {
            if (player.getAttribute("note-disabled", false)) {
                return true
            }
            val data = getIndex(button)
            val toggled = data[1] as Boolean
            val configs = getConfigs(data[0] as Int)
            val quest = player.getQuestRepository().getQuest(Quests.ANIMAL_MAGNETISM)
            player.packetDispatch.sendInterfaceConfig(Components.ANMA_RGB_480, configs[0], !toggled)
            player.packetDispatch.sendInterfaceConfig(
                Components.ANMA_RGB_480,
                data[2] as Int,
                toggled
            )
            if (quest.getStage(player) == 33) {
                setNoteCache(player, data[0] as Int, !toggled)
                if (isTranslated(player)) {
                    if (player.inventory.remove(AnimalMagnetism.RESEARCH_NOTES)) {
                        player.setAttribute("note-disabled", true)
                        player.inventory.add(AnimalMagnetism.TRANSLATED_NOTES)
                        playAudio(player, Sounds.ANMA_PUZZLE_COMPLETE_3283)
                        sendMessage(player, "It suddenly all makes sense.", null)
                    }
                }
            }
            return true
        }

        private fun setNoteCache(player: Player, index: Int, toggled: Boolean) {
            val cache = getNoteCache(player)
            cache[index] = toggled
            player.setAttribute("note-cache", cache)
        }

        private fun isTranslated(player: Player): Boolean {
            val cache: Map<Int, Boolean> = getNoteCache(player)
            val correct = intArrayOf(0, 2, 3, 5, 6, 7)
            val wrong = intArrayOf(1, 4, 8)
            for (i in correct) {
                if (cache[i]!!) {
                    return false
                }
            }
            for (i in wrong) {
                if (!cache[i]!!) {
                    return false
                }
            }
            return true
        }

        private fun getNoteCache(player: Player): MutableMap<Int, Boolean> {
            var cache = player.getAttribute<MutableMap<Int, Boolean>>("note-cache", null)
            if (cache == null) {
                cache = HashMap()
                for (i in BUTTONS.indices) {
                    cache[i] = true
                }
            }
            return cache
        }

        private fun getConfigs(index: Int): IntArray {
            return intArrayOf(21 + index, 0)
        }

        private fun getIndex(buttonId: Int): Array<Any> {
            for (i in BUTTONS.indices) {
                for (k in 0 until BUTTONS[i].size - 1) {
                    if (buttonId == BUTTONS[i][k]) {
                        return arrayOf(i, k == 0, BUTTONS[i][2])
                    }
                }
            }
            return arrayOf(0, true)
        }

        companion object {
            private val BUTTONS =
                arrayOf(
                    intArrayOf(40, 39, 6),
                    intArrayOf(42, 41, 3),
                    intArrayOf(44, 43, 7),
                    intArrayOf(46, 45, 8),
                    intArrayOf(48, 47, 4),
                    intArrayOf(50, 49, 9),
                    intArrayOf(52, 51, 10),
                    intArrayOf(54, 53, 11),
                    intArrayOf(56, 55, 5)
                )
        }
    }

    /** The type Container handler. */
    class ContainerHandler
    /** Instantiates a new Container handler. */
        : UseWithHandler(Items.POLISHED_BUTTONS_10496, Items.HARD_LEATHER_1743) {
        override fun newInstance(arg: Any?): Plugin<Any> {
            addHandler(AnimalMagnetism.PATTERN.id, ITEM_TYPE, this)
            return this
        }

        override fun handle(event: NodeUsageEvent?): Boolean {
            event ?: return false

            val player = event.player
            if (!player.inventory.containsItem(AnimalMagnetism.HARD_LEATHER)) {
                sendMessage(player, "You need hard leather as well as these 2 items.", null)
                return true
            }
            if (!player.inventory.containsItem(AnimalMagnetism.POLISHED_BUTTONS)) {
                sendMessage(player, "You need polished buttons as well as these 2 items.", null)
                return true
            }
            if (
                player.inventory.remove(
                    AnimalMagnetism.HARD_LEATHER,
                    AnimalMagnetism.POLISHED_BUTTONS,
                    AnimalMagnetism.PATTERN
                )
            ) {
                playAudio(player, Sounds.ANMA_POLISH_BUTTONS_3281)
                player.inventory.add(AnimalMagnetism.CONTAINER)
            }
            return true
        }
    }

    override fun isWalk(player: Player, node: Node): Boolean {
        return node !is Item
    }

    override fun isWalk(): Boolean {
        return false
    }
}
