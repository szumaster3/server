package content.global.plugin.item

import core.Util
import core.api.*
import core.cache.def.impl.ItemDefinition
import core.cache.def.impl.SceneryDefinition
import core.game.dialogue.Dialogue
import core.game.dialogue.DialogueInterpreter
import core.game.interaction.NodeUsageEvent
import core.game.interaction.OptionHandler
import core.game.interaction.UseWithHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.node.item.WeightedChanceItem
import core.game.world.update.flag.context.Animation
import core.plugin.ClassScanner
import core.plugin.Initializable
import core.plugin.Plugin
import core.tools.RandomFunction
import org.rs.consts.Animations
import org.rs.consts.Items
import org.rs.consts.Scenery
import org.rs.consts.Sounds

/**
 * Handles interactions with fishbowl.
 */
@Initializable
class FishbowlOptionHandler : OptionHandler() {
    /**
     * Registers handlers for fishbowl related item options and initializes supporting plugins.
     *
     * @param arg Optional initialization argument.
     * @return This plugin instance.
     */
    override fun newInstance(arg: Any?): Plugin<Any> {
        ItemDefinition.forId(FISHBOWL_WATER).handlers["option:empty"] = this
        ItemDefinition.forId(FISHBOWL_SEAWEED).handlers["option:empty"] = this
        for (id in intArrayOf(FISHBOWL_BLUE, FISHBOWL_GREEN, FISHBOWL_SPINE)) {
            val def = ItemDefinition.forId(id)
            def.handlers["option:talk-at"] = this
            def.handlers["option:play-with"] = this
            def.handlers["option:feed"] = this
            def.handlers["option:drop"] = this
        }
        ClassScanner.definePlugin(FishbowlDialogue())
        ClassScanner.definePlugin(FeedPetFishHandler())
        AquariumPlugin().newInstance(arg)
        return this
    }

    /**
     * Handles fishbowl-related option interactions performed by the player.
     */
    override fun handle(
        player: Player,
        node: Node,
        option: String,
    ): Boolean {
        if (node is Item) {
            val item = node.asItem()
            when (item.id) {
                FISHBOWL_WATER, FISHBOWL_SEAWEED ->
                    if (removeItem(player, item)) {
                        lock(player, 2)
                        addItem(player, FISHBOWL_EMPTY)
                        playAudio(player, Sounds.LIQUID_2401, 0, 1)
                        sendMessage(player, "You empty the contents of the fishbowl onto the ground.")
                    }

                FISHBOWL_BLUE, FISHBOWL_GREEN, FISHBOWL_SPINE ->
                    when (option) {
                        "talk-at" -> {
                            lock(player, ANIM_TALK.duration)
                            animate(player, ANIM_TALK)
                            return player.dialogueInterpreter.open("fishbowl-options", option)
                        }

                        "play-with" -> {
                            lock(player, ANIM_PLAY.duration)
                            animate(player, ANIM_PLAY)
                            return player.dialogueInterpreter.open("fishbowl-options", option)
                        }

                        "feed" -> return player.dialogueInterpreter.open("fishbowl-options", option)
                        "drop" -> return player.dialogueInterpreter.open("fishbowl-options", option, item)
                    }
            }
        }

        return true
    }

    /**
     * Handles feeding pet fish using fish food on fishbowl items.
     */
    private inner class FeedPetFishHandler : UseWithHandler(Items.FISH_FOOD_272) {

        /**
         * Registers this handler for feeding fish on different fishbowl items.
         *
         * @param arg Optional argument.
         * @return This handler instance.
         */
        override fun newInstance(arg: Any?): Plugin<Any?> {
            addHandler(FISHBOWL_BLUE, ITEM_TYPE, this)
            addHandler(FISHBOWL_GREEN, ITEM_TYPE, this)
            addHandler(FISHBOWL_SPINE, ITEM_TYPE, this)
            return this
        }

        /**
         * Opens the feeding dialogue when fish food is used on a fishbowl.
         *
         * @param event The node usage event.
         * @return True to indicate the action was handled.
         */
        override fun handle(event: NodeUsageEvent): Boolean = event.player.dialogueInterpreter.open("fishbowl-options", "feed")
    }

    /**
     * Dialogue for fishbowl.
     *
     * @param player The player.
     */
    inner class FishbowlDialogue(
        player: Player? = null,
    ) : Dialogue(player) {
        private var fishbowl: Item? = null
        private var option: String? = null

        override fun newInstance(player: Player?): Dialogue = FishbowlDialogue(player)

        override fun open(vararg args: Any?): Boolean {
            for (arg in args) {
                if (arg is Item) {
                    this.fishbowl = arg
                } else if (arg is String) {
                    this.option = arg
                }
            }
            when (option) {
                "talk-at" -> {
                    player("Good fish. Just keep swimming... swimming... swimming...")
                    stage = 1
                }

                "play-with" -> {
                    player("Jump! 'Cmon " + (if (player.isMale) "girl" else "boy") + ", Jump!")
                    stage = 2
                }

                "feed" -> {
                    if (inInventory(player, Items.FISH_FOOD_272) && removeItem(player, Item(Items.FISH_FOOD_272))) {
                        addItem(player, Items.AN_EMPTY_BOX_6675, 1)
                        lock(player, ANIM_FEED.duration)
                        animate(player, ANIM_FEED)
                        sendMessage(player, "You feed your fish.")
                    } else if (anyInInventory(player, Items.POISONED_FISH_FOOD_274)) {
                        sendMessage(player,"You can't poison your own pet!")
                    } else {
                        sendMessage(player,"You don't have any fish food.")
                    }
                }

                "drop" -> {
                    sendDialogue("If you drop your fishbowl it will break!")
                    stage = 4
                }
            }
            return true
        }

        override fun handle(
            interfaceId: Int,
            buttonId: Int,
        ): Boolean {
            when (stage) {
                999 -> end()
                1 -> {
                    sendDialogue("The fish swims. It is clearly an obedient fish.")
                    stage = 999
                }

                2 -> {
                    sendDialogue("The fish bumps into the side of the fishbowl. Then it swims some", "more.")
                    stage++
                }

                3 -> {
                    player("Good fish...")
                    stage = 999
                }

                4 -> {
                    options("Drop it regardless", "Keep hold")
                    stage++
                }

                5 ->
                    when (buttonId) {
                        1 -> {
                            sendDialogue("The fishbowl shatters on the ground.")
                            player.inventory.remove(fishbowl)
                            stage = 999
                        }

                        2 -> {
                            sendDialogue("You keep a hold of it for now.")
                            stage = 999
                        }
                    }
            }
            return true
        }

        override fun getIds(): IntArray = intArrayOf(DialogueInterpreter.getDialogueKey("fishbowl-options"))
    }

    /**
     * Handles the fish-catching aquarium scenery option and fishing logic.
     */
    class AquariumPlugin : OptionHandler() {
        /**
         * Registers the aquarium "fish-in" option handler and the tiny net use handler.
         *
         * @param arg Optional argument.
         * @return This plugin instance.
         */
        override fun newInstance(arg: Any?): Plugin<Any?> {
            SceneryDefinition.forId(Scenery.AQUARIUM_10091).handlers["option:fish-in"] = this
            ClassScanner.definePlugin(TinyNetHandler())
            return this
        }

        /**
         * Handles the "fish-in" option on the aquarium scenery by attempting to catch fish.
         */
        override fun handle(
            player: Player,
            node: Node,
            option: String,
        ): Boolean = getFish(player)

        /**
         * Attempts to catch fish using a tiny net and fishbowl seaweed.
         *
         * @param player The player catching fish.
         * @return True after processing the catch attempt.
         */
        fun getFish(player: Player): Boolean {
            if (!anyInInventory(player, TINY_NET)) {
                sendMessage(player, "You see some tiny fish swimming around... but how to catch them?")
                return true
            } else if (removeItem(player, Item(FISHBOWL_SEAWEED))) {
                sendMessage(player, "You wave the net around...")
                rewardXP(player, Skills.FISHING, 1.0)
                val level = player.getSkills().getLevel(Skills.FISHING)
                val blueChance = Math.round(Util.clamp(-0.6667 * level.toDouble() + 106.0, 40.0, 60.0)).toInt()
                val greenChance = Math.round(Util.clamp(0.2941 * level.toDouble() + 19.7059, 20.0, 40.0)).toInt()
                val spineChance = Math.round(Util.clamp(0.6667 * level.toDouble() - 46.0, 0.0, 20.0)).toInt()

                val fishChance =
                    arrayOf(
                        WeightedChanceItem(FISHBOWL_BLUE, 1, blueChance),
                        WeightedChanceItem(FISHBOWL_GREEN, 1, greenChance),
                        WeightedChanceItem(FISHBOWL_SPINE, 1, spineChance),
                    )

                val fish = RandomFunction.rollWeightedChanceTable(*fishChance)
                player.inventory.add(fish)
                var msg = "[ REPORT BUG ]"
                when (fish.id) {
                    FISHBOWL_BLUE -> msg = "Bluefish"
                    FISHBOWL_GREEN -> msg = "Greenfish"
                    FISHBOWL_SPINE -> msg = "Spinefish"
                }
                sendMessage(player, "...and you catch a Tiny $msg!")
                finishDiaryTask(player, DiaryType.SEERS_VILLAGE, 1, 10)
                return true
            } else {
                sendMessage(player, "You need something to put your catch in!")
                return true
            }
        }

        /**
         * Handles using the tiny net on aquarium scenery to catch fish.
         */
        private inner class TinyNetHandler : UseWithHandler(TINY_NET) {
            /**
             * Registers this handler for using the tiny net on the aquarium scenery object.
             *
             * @param arg Optional argument.
             * @return This handler instance.
             */
            override fun newInstance(arg: Any?): Plugin<Any?> {
                addHandler(Scenery.AQUARIUM_10091, OBJECT_TYPE, this)
                return this
            }
            /**
             * Processes the use of the tiny net on the aquarium scenery by attempting to catch fish.
             *
             * @param event The node usage event.
             * @return True to indicate the event was handled.
             */
            override fun handle(event: NodeUsageEvent): Boolean = getFish(event.player)
        }
    }

    companion object {
        private const val FISHBOWL_EMPTY = Items.FISHBOWL_6667
        private const val FISHBOWL_WATER = Items.FISHBOWL_6668
        private const val FISHBOWL_SEAWEED = Items.FISHBOWL_6669
        private const val FISHBOWL_BLUE = Items.FISHBOWL_6670
        private const val FISHBOWL_GREEN = Items.FISHBOWL_6671
        private const val FISHBOWL_SPINE = Items.FISHBOWL_6672
        private const val TINY_NET = Items.TINY_NET_6674
        private val ANIM_TALK = Animation(Animations.NODDING_AT_FISHBOWL_2782)
        private val ANIM_PLAY = Animation(Animations.PLAY_WITH_FISHBOWL_2780)
        private val ANIM_FEED = Animation(Animations.FEED_BOWL_2781)
    }
}
