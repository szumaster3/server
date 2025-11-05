package content.region.kandarin.yanille.quest.handsand

import content.data.GameAttributes
import core.api.*
import core.game.dialogue.FaceAnim
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.item.Item
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.tools.RandomFunction
import shared.consts.*

/**
 * Handles all interactions in The Hand in the Sand quest.
 */
class TheHandintheSandPlugin : InteractionListener {

    companion object {
        val BEER_IDS = intArrayOf(Items.GREENMANS_ALE_1909, Items.DRAGON_BITTER_1911)
        private val SANDY = intArrayOf(3110, 3111, NPCs.SANDY_3112, NPCs.SANDY_3113)

        val bertRotaCopy = arrayOf(
            "Sandy's Sand Corp - Brimhaven",
            "",
            "    Bert's Rota - Copy   ",
            "",
            "Week 1 - 6am-10pm - 50gps",
            "",
            "Week 2 - 6am-10pm - 50gps",
            "",
            "Week 3 - 6am-10pm - 50gps",
            "",
            "Week 4 - 6am-10pm - 50gps",
            "",
            "Week 5 - 6am-10pm - 50gps",
            "",
            "Week 6 - 6am-10pm - 50gps"
        )

        val bertRotaOriginal = arrayOf(
            "Sandy's Sand Corp - Brimhaven",
            "",
            "    Bert's Rota - Original   ",
            "",
            "Week 1 - 9am-6pm - 50gps",
            "",
            "Week 2 - 9am-6pm - 50gps",
            "",
            "Week 3 - 9am-6pm - 50gps",
            "",
            "Week 4 - 9am-6pm - 50gps",
            "",
            "Week 5 - 9am-6pm - 50gps",
            "",
            "Week 6 - 9am-10pm - 50gps"
        )
    }

    override fun defineListeners() {

        /*
         * Handles using dif beers on guard captain.
         */

        onUseWith(IntType.NPC, BEER_IDS, NPCs.GUARD_CAPTAIN_3109) { player, _, with ->
            sendNPCDialogue(player, with.id, "Yeeeuuuch! I hatesh that shtuff, jusht bring ush a beer! Mmmmm beer!", FaceAnim.OLD_DRUNK_LEFT)
            return@onUseWith true
        }

        /*
         * Handles read the Bert rota.
         */

        on(Items.BERTS_ROTA_6947, IntType.ITEM, "Read") { player, _ ->
            openInterface(player, Components.BLANK_SCROLL_222)
            sendString(player, bertRotaCopy.joinToString("<br>"), Components.BLANK_SCROLL_222, 1)
            return@on true
        }

        /*
         * Handles read the Sandy rota.
         */

        on(Items.SANDYS_ROTA_6948, IntType.ITEM, "Read") { player, _ ->
            openInterface(player, Components.BLANK_SCROLL_222)
            sendString(player, bertRotaOriginal.joinToString("<br>"), Components.BLANK_SCROLL_222, 1)
            return@on true
        }

        /*
         * Handles pickpocket the Sandy for original rot.
         */

        on(SANDY, IntType.NPC, "Pickpocket") { player, _ ->
            player.animate(Animation(Animations.HUMAN_PICKPOCKETING_881))
            if(freeSlots(player) == 0) {
                sendDialogue(player, "I'd better make room in my inventory first!")
                return@on false
            }

            val random = RandomFunction.random(1,5)
            if(random == 1) {
                sendDialogue(player, "You rummage around in Sandy's pockets.....")
                sendMessage(player, "You find a small amount of sand.")
                addItem(player, Items.SAND_6958)
            } else {
                sendDialogue(player, "You felt something but it slipped through your fingers...")
            }
            return@on true
        }

        /*
         * Handles making redberry juice.
         */

        onUseWith(IntType.ITEM, Items.REDBERRIES_1951, Items.BOTTLED_WATER_6953) { player, used, with ->
            if(removeItem(player, used.asItem()) && removeItem(player, with.asItem())) {
                sendMessage(player, "Now you just need to add the white berries to make the pink dye.")
                addItem(player, Items.REDBERRY_JUICE_6954, 1)
            }
            return@onUseWith true
        }

        /*
         * Handles making pink dye.
         */

        onUseWith(IntType.ITEM, Items.WHITE_BERRIES_239, Items.REDBERRY_JUICE_6954) { player, used, with ->
            if(removeItem(player, used.asItem()) && removeItem(player, with.asItem())) {
                addItem(player, Items.PINK_DYE_6955, 1)
            }
            return@onUseWith true
        }

        /*
         * Handles making pink lens.
         */

        onUseWith(IntType.ITEM, Items.PINK_DYE_6955, Items.LANTERN_LENS_4542) { player, used, with ->
            if(removeItem(player, used.asItem()) && removeItem(player, with.asItem())) {
                sendItemDialogue(player, Items.ROSE_TINTED_LENS_6956, "You have successfully made the rose tinted lens!")
                addItem(player, Items.ROSE_TINTED_LENS_6956, 1)
                setAttribute(player, GameAttributes.HAND_SAND_BETTY_POTION, true)
            }
            return@onUseWith true
        }

        /*
         * Handles making the truth serum
         */

        onUseWith(IntType.SCENERY, Items.ROSE_TINTED_LENS_6956, Scenery.COUNTER_10813) { player, used, _ ->
            lock(player, 3)
            queueScript(player, 1, QueueStrength.SOFT) {
                if (removeItem(player, used.asItem())) {
                    sendItemDialogue(player, Items.TRUTH_SERUM_6952, "As you focus the light on the vial and Betty pours the potion in, the lens heats up and shatters. After a few seconds Betty hands you the vial of Truth Serum.")
                    player.questRepository.setStageNonmonotonic(player.questRepository.forIndex(72), 8)
                    setVarbit(player, Vars.VARBIT_BETTY_DESK_1537, 0)
                    addItem(player, Items.TRUTH_SERUM_6952, 1)
                }
                return@queueScript stopExecuting(player)
            }
            return@onUseWith true
        }

        /*
         * Handles put the sand to truth serum.
         */

        onUseWith(IntType.ITEM, Items.PINK_DYE_6955, Items.LANTERN_LENS_4542) { player, used, with ->
            sendDialogue(player, "Perhaps you should let Betty do that, it looks tricky.")
            return@onUseWith true
        }

        /*
         * Handles put truth serum to sandy coffee.
         */

        onUseWith(IntType.SCENERY, Items.TRUTH_SERUM_6952, Scenery.SANDY_S_COFFEE_MUG_10807) { player, used, _ ->
            if(removeItem(player, used.asItem())) {
                setVarbit(player, 1535, 0, true)
                sendDialogue(player, "You pour the serum into Sandy's coffee, then a little while later watch him drink it.")
                setQuestStage(player, Quests.THE_HAND_IN_THE_SAND, 9)
            }
            return@onUseWith true
        }

        /*
         * Handles use magical orb (not activated) on sandy NPC.
         */

        onUseWith(IntType.NPC, Items.MAGICAL_ORB_6950, *SANDY) { player, _, _ ->
            sendDialogue(player,"You need to activate the magical scrying orb, obtained from the wizard in Yanille, to capture the conversation with Sandy!")
            return@onUseWith true
        }

        /*
         * Handles activating the orb.
         */

        on(Items.MAGICAL_ORB_6950, IntType.ITEM, "activate") { player, node ->
            if(getQuestStage(player, Quests.THE_HAND_IN_THE_SAND) == 9) {
                replaceSlot(player, node.index, Item(Items.MAGICAL_ORB_A_6951, 1))
                sendItemDialogue(player, Items.MAGICAL_ORB_A_6951, "You rub the magical scrying orb as the Wizard told you, it starts to glow, recording everything it sees and hears, now you can talk to Sandy in Brimhaven.")
            } else {
                sendItemDialogue(player, Items.MAGICAL_ORB_6950, "You rub the magical scrying orb but nothing interesting happens.")
            }
            return@on true
        }

    }

    override fun defineDestinationOverrides() {
        setDest(IntType.SCENERY, intArrayOf(Scenery.COUNTER_10813), "use") { player, node ->
            return@setDest Location.create(3016, 3259, 0)
        }
    }
}