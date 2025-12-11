package content.region.misthalin.lumbridge.dialogue

import content.data.skill.RepairableSkillingTool
import content.data.skill.RepairableSkillingTool.Companion.forId
import content.global.plugins.item.equipment.BarrowsEquipmentRegister.Companion.TICKS
import core.api.openNpcShop
import core.api.sendNPCDialogue
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.dialogue.Topic
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.Diary
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.item.Item
import core.game.world.GameWorld.settings
import core.tools.END_DIALOGUE
import shared.consts.Items
import shared.consts.NPCs
import java.util.*

/**
 * Represents the Bob dialogue.
 */
class BobDialogue(player: Player? = null) : Dialogue(player) {
    private var itemId = 0
    private var item: Item? = null
    private val level = 1

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> npc(FaceAnim.FURIOUS, "Get yer own!").also { stage = END_DIALOGUE }
            1 -> npc(FaceAnim.HAPPY, "Yes! I buy and sell axes! Take your pick (or axe)!").also { stage++ }
            2 -> end().also { openNpcShop(player, npc.id) }
            3 -> sendNPCDialogue(player, npc.id, "Of course I'll repair it, though the materials may cost you. Just hand me the item and I'll have a look.", FaceAnim.FRIENDLY).also { stage = END_DIALOGUE }
            4 -> when {
                Diary.canClaimLevelRewards(player, DiaryType.LUMBRIDGE, level) -> player("I've done all the medium tasks in my Lumbridge", "Achievement Diary.").also { stage = 13 }
                Diary.canReplaceReward(player, DiaryType.LUMBRIDGE, level) -> player("I've seemed to have lost my explorer's ring...").also { stage = 17 }
                else -> showTopics(
                    Topic("What is the Achievement Diary?",5),
                    Topic("What are the rewards?",9),
                    Topic("How do I claim the rewards?",11),
                    Topic("See you later.", END_DIALOGUE)
                )
            }
            6 ->  npcl(FaceAnim.FRIENDLY, "Ah, well it's a diary that helps you keep track of particular achievements you've made in the world of " + settings!!.name + ". In Lumbridge and Draynor i can help you discover some very useful things indeed.").also { stage++ }
            7 ->  npc(FaceAnim.FRIENDLY, "Eventually with enough exploration you will be", "rewarded for your explorative efforts.").also { stage++ }
            8 ->  npc(FaceAnim.FRIENDLY, "You can access your Achievement Diary by going to", "the Quest Journal. When you've opened the Quest", "Journal click on the green star icon on the top right", "hand corner. This will open the diary.").also { stage = 4 }
            9 ->  npc(FaceAnim.FRIENDLY, "Ah, well there are different rewards for each", "Achievement Diary. For completing the Lumbridge and", "Draynor diary you are presented with an explorer's", "ring.").also { stage++ }
            10 -> npc(FaceAnim.FRIENDLY, "This ring will become increasingly useful with each", "section of the diary that you complete.").also { stage = 4 }
            11 -> npc(FaceAnim.FRIENDLY, "You need to complete the tasks so that they're all ticked", "off, then you can claim your reward. Most of them are", "straightforward although you might find some required", "quests to be started, if not finished.").also { stage++ }
            12 -> npc(FaceAnim.FRIENDLY, "To claim the explorer's ring speak to Explorer Jack", " in Lumbridge, Ned in Draynor Village or myself.").also { stage = 4 }
            13 -> npc(FaceAnim.FRIENDLY, "Yes I see that, you'll be wanting your", "reward then I assume?").also { stage++ }
            14 -> player(FaceAnim.HALF_GUILTY, "Yes please.").also { stage++ }
            15 -> {
                Diary.flagRewarded(player, DiaryType.LUMBRIDGE, level)
                npc(FaceAnim.FRIENDLY, "This ring is a representation of the adventures you", "went on to complete your tasks.").also { stage++ }
            }
            16 -> player(FaceAnim.HAPPY, "Wow, thanks!").also { stage = END_DIALOGUE }
            17 -> {
                Diary.grantReplacement(player, DiaryType.LUMBRIDGE, level)
                npc(FaceAnim.FRIENDLY, "You better be more careful this time.")
                stage = END_DIALOGUE
            }
            18 -> showTopics(
                Topic("Yes, please.",20),
                Topic("No, thank you.",19),
            )
            19 -> player(FaceAnim.NEUTRAL, "On second thoughts, no thanks.").also { stage = END_DIALOGUE }
            20 -> {
                end()
                if (BobDialogue.repairItem != null) {
                    if (!player.inventory.contains(Items.COINS_995, BobDialogue.repairItem!!.cost)) {
                        end()
                        player.packetDispatch.sendMessage("You don't have enough to pay him.")
                        return true
                    }
                    if (!player.inventory.contains(itemId, 1)) {
                        end()
                        return true
                    }
                    player.inventory.remove(Item(Items.COINS_995, BobDialogue.repairItem!!.cost))
                    if (player.inventory.remove(Item(itemId, 1))) {
                        player.inventory.add(BobDialogue.repairItem!!.product)
                    }
                    var cost = "free"
                    if (BobDialogue.repairItem!!.cost != 0) {
                        cost = BobDialogue.repairItem!!.cost.toString() + " gold coins"
                    }
                }
                if (BobDialogue.repairItem == null) {
                    var cost: String? = "free"
                    val type = BarrowsEquipment.formattedName(itemId)
                    val single = BarrowsEquipment.getSingleName(type!!)
                    val equipment = BarrowsEquipment.getEquipmentType(type)
                    val newString = type.lowercase(Locale.getDefault()).replace(single!!, "").trim { it <= ' ' }
                        .replace("'s", "")
                    val fullSet: BarrowsEquipment.BarrowsFullEquipment? =
                        BarrowsEquipment.BarrowsFullEquipment.forName("$newString $equipment")
                    if (BarrowsEquipment.getFormattedCost(equipment!!, item!!) !== 0) {
                        cost = "${BarrowsEquipment.getFormattedCost(equipment!!, item!!)} gold coins"
                    }
                    if (!player.inventory.contains(Items.COINS_995, BarrowsEquipment.getFormattedCost(equipment!!, item!!))) {
                        end()
                        player.packetDispatch.sendMessage("You don't have enough to pay him.")
                        return true
                    }
                    if (fullSet == null || fullSet.full == null) {
                        player.packetDispatch.sendMessage("Nothing interesting happens.")
                        return true
                    }
                    if (!player.inventory.contains(itemId, 1)) {
                        end()
                        return true
                    }
                    player.inventory.remove(
                        Item(
                            Items.COINS_995, BarrowsEquipment.getFormattedCost(
                                equipment, item!!
                            )
                        )
                    )
                    if (player.inventory.remove(Item(itemId, 1))) {
                        player.inventory.add(fullSet.full)
                    }
                }
            }
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return BobDialogue(player)
    }

    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        var repair = false
        var wrong = false
        if (npc.id == 3797 && args.size == 1) {
            player("Can you repair my items for me?")
            stage = 3
            return true
        }
        if (args.size == 1) {
            showTopics(
                Topic("Give me a quest!", 0),
                Topic("Have you anything to sell?", 1),
                Topic("Can you repair my items for me?", 3),
                Topic("Talk about Achievement Diaries", 4),
            )
            stage = 0
            return true
        }
        if (args[1] != null) {
            repair = args[1] as Boolean
        }
        if (args[2] != null) {
            wrong = args[2] as Boolean
        }
        if (args[3] != null) {
            BobDialogue.repairItem = forId(args[3] as Int)
            itemId = args[3] as Int
        }
        if (args[4] != null) {
            item = args[4] as Item
        }
        if (repair && !wrong) {
            var cost = "free"
            if (BobDialogue.repairItem != null) {
                if (BobDialogue.repairItem!!.cost != 0) {
                    cost = BobDialogue.repairItem!!.cost.toString() + " gold coins"
                }
            } else {
                val type = BarrowsEquipment.formattedName(itemId)
                val single = BarrowsEquipment.getSingleName(
                    type!!
                )
                val equipment = BarrowsEquipment.getEquipmentType(
                    type
                )
                val newString = type.lowercase(Locale.getDefault()).replace(single!!, "").trim { it <= ' ' }
                    .replace("'s", "")
                val newewString = StringBuilder()
                newewString.append(newString).append(" $equipment")
                if (BarrowsEquipment.getFormattedCost(
                        equipment!!,
                        item!!
                    ) != 0
                ) {
                    cost = BarrowsEquipment.getFormattedCost(
                        equipment,
                        item!!
                    ).toString() + " gold coins"
                }
            }
            npc("That'll cost you $cost to fix, are you sure?")
            stage = 18
            return true
        }
        if (repair && wrong) {
            npc("Sorry friend, but I can't do anything with that.")
            stage = END_DIALOGUE
            return true
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.BOB_519, NPCs.SQUIRE_3797, NPCs.TINDEL_MARCHANT_1799)
    }

    companion object {
        private var repairItem: RepairableSkillingTool? = null
    }

    /**
     * The Barrows equipment.
     */
    class BarrowsEquipment {
        /**
         * Get base string.
         *
         * @return the string.
         */
        /**
         * Array containing the base barrows brothers names.
         */
        val base: Array<String> = arrayOf("dharok", "verac", "ahrim", "torag", "guthan", "karil")

        /**
         * The enum Barrows.
         */
        enum class BarrowsFullEquipment(
            /**
             * Gets the full item.
             *
             * @return the full item
             */
            val full: Item
        ) {
            /**
             * Verac legs barrows.
             */
            VERAC_LEGS(Item(Items.VERACS_PLATESKIRT_4759, 1)),

            /**
             * Verac top barrows.
             */
            VERAC_TOP(Item(Items.VERACS_BRASSARD_4757, 1)),

            /**
             * Verac weapon barrows.
             */
            VERAC_WEAPON(Item(Items.VERACS_FLAIL_4755, 1)),

            /**
             * Verac helm barrows.
             */
            VERAC_HELM(Item(Items.VERACS_HELM_4753, 1)),

            /**
             * Torag legs barrows.
             */
            TORAG_LEGS(Item(Items.TORAGS_PLATELEGS_4751, 1)),

            /**
             * Torag body barrows.
             */
            TORAG_BODY(Item(Items.TORAGS_PLATEBODY_4749, 1)),

            /**
             * Torag helm barrows.
             */
            TORAG_HELM(Item(Items.TORAGS_HELM_4745, 1)),

            /**
             * Torag weapon barrows.
             */
            TORAG_WEAPON(Item(Items.TORAGS_HAMMERS_4747, 1)),

            /**
             * Karil helm barrows.
             */
            KARIL_HELM(Item(Items.KARILS_COIF_4732, 1)),

            /**
             * Karil weapon barrows.
             */
            KARIL_WEAPON(Item(Items.KARILS_CROSSBOW_4734, 1)),

            /**
             * Karil body barrows.
             */
            KARIL_BODY(Item(Items.KARILS_LEATHERTOP_4736, 1)),

            /**
             * Karil legs barrows.
             */
            KARIL_LEGS(Item(Items.KARILS_LEATHERSKIRT_4738, 1)),

            /**
             * Guthan helm barrows.
             */
            GUTHAN_HELM(Item(Items.GUTHANS_HELM_4724, 1)),

            /**
             * Guthan body barrows.
             */
            GUTHAN_BODY(Item(Items.GUTHANS_PLATEBODY_4728, 1)),

            /**
             * Guthan legs barrows.
             */
            GUTHAN_LEGS(Item(Items.GUTHANS_CHAINSKIRT_4730, 1)),

            /**
             * Guthan weapon barrows.
             */
            GUTHAN_WEAPON(Item(Items.GUTHANS_WARSPEAR_4726, 1)),

            /**
             * Dharok helm barrows.
             */
            DHAROK_HELM(Item(Items.DHAROKS_HELM_4716, 1)),

            /**
             * Dharok body barrows.
             */
            DHAROK_BODY(Item(Items.DHAROKS_PLATEBODY_4720, 1)),

            /**
             * Dharok legs barrows.
             */
            DHAROK_LEGS(Item(Items.DHAROKS_PLATELEGS_4722, 1)),

            /**
             * Dharok weapon barrows.
             */
            DHAROK_WEAPON(Item(Items.DHAROKS_GREATAXE_4718, 1)),

            /**
             * Ahrim helm barrows.
             */
            AHRIM_HELM(Item(Items.AHRIMS_HOOD_4708, 1)),

            /**
             * Ahrim body barrows.
             */
            AHRIM_BODY(Item(Items.AHRIMS_ROBETOP_4712, 1)),

            /**
             * Ahrim legs barrows.
             */
            AHRIM_LEGS(Item(Items.AHRIMS_ROBESKIRT_4714, 1)),

            /**
             * Ahrim weapon barrows.
             */
            AHRIM_WEAPON(Item(Items.AHRIMS_STAFF_4710, 1));

            companion object {
                /**
                 * For name barrows.
                 *
                 * @param name the name
                 * @return the barrows
                 */
                fun forName(name: String): BarrowsFullEquipment? {
                    var name = name
                    if (name == "guthan body body") {
                        name = "guthan body"
                    } else if (name == "torag body body") {
                        name = "torag body"
                    } else if (name == "verac body") {
                        name = "verac top"
                    }
                    for (barrow in values()) {
                        if (barrow.name.lowercase(Locale.getDefault()).replace("_", " ").trim { it <= ' ' }
                                .equals(name, ignoreCase = true)) {
                            return barrow
                        }
                    }
                    return null
                }
            }
        }

        companion object {
            /**
             * Array containing weapon names.
             */
            private val weapon_names = arrayOf("flail", "greataxe", "spear", "x-bow", "hammer", "hammers", "staff")

            /**
             * Array containing body part names.
             */
            private val body_names = arrayOf("top", "platebody", "body")

            /**
             * Array containing helm part names.
             */
            private val helm_names = arrayOf("hood", "helm", "coif")

            /**
             * Array containing leg part names.
             */
            private val leg_names = arrayOf("skirt", "legs", "plateskirt", "platelegs")

            /**
             * Array containing the prices associated with each equipment part (weapon, body, legs, helm).
             */
            private val prices = arrayOf(
                arrayOf<Any>("weapon", 100),
                arrayOf<Any>("body", 90),
                arrayOf<Any>("legs", 80),
                arrayOf<Any>("helm", 60)
            )

            /**
             * Array containing the item IDs and names for all Barrows items.
             */
            private val ITEMS = arrayOf(
                arrayOf<Any>(4856, "Ahrim's hood"),
                arrayOf<Any>(4857, "Ahrim's hood"),
                arrayOf<Any>(4858, "Ahrim's hood"),
                arrayOf<Any>(4859, "Ahrim's hood"),
                arrayOf<Any>(4860, "Ahrim's hood"),
                arrayOf<Any>(4862, "Ahrim's staff"),
                arrayOf<Any>(4863, "Ahrim's staff"),
                arrayOf<Any>(4864, "Ahrim's staff"),
                arrayOf<Any>(4865, "Ahrim's staff"),
                arrayOf<Any>(4866, "Ahrim's staff"),
                arrayOf<Any>(4868, "Ahrim's top"),
                arrayOf<Any>(4869, "Ahrim's top"),
                arrayOf<Any>(4870, "Ahrim's top"),
                arrayOf<Any>(4871, "Ahrim's top"),
                arrayOf<Any>(4872, "Ahrim's top"),
                arrayOf<Any>(4874, "Ahrim's skirt"),
                arrayOf<Any>(4875, "Ahrim's skirt"),
                arrayOf<Any>(4876, "Ahrim's skirt"),
                arrayOf<Any>(4877, "Ahrim's skirt"),
                arrayOf<Any>(4878, "Ahrim's skirt"),
                arrayOf<Any>(4880, "Dharok's helm"),
                arrayOf<Any>(4881, "Dharok's helm"),
                arrayOf<Any>(4882, "Dharok's helm"),
                arrayOf<Any>(4883, "Dharok's helm"),
                arrayOf<Any>(4884, "Dharok's helm"),
                arrayOf<Any>(4886, "Dharok's greataxe"),
                arrayOf<Any>(4887, "Dharok's greataxe"),
                arrayOf<Any>(4888, "Dharok's greataxe"),
                arrayOf<Any>(4889, "Dharok's greataxe"),
                arrayOf<Any>(4890, "Dharok's greataxe"),
                arrayOf<Any>(4892, "Dharok's platebody"),
                arrayOf<Any>(4893, "Dharok's platebody"),
                arrayOf<Any>(4894, "Dharok's platebody"),
                arrayOf<Any>(4895, "Dharok's platebody"),
                arrayOf<Any>(4896, "Dharok's platebody"),
                arrayOf<Any>(4898, "Dharok's platelegs"),
                arrayOf<Any>(4899, "Dharok's platelegs"),
                arrayOf<Any>(4900, "Dharok's platelegs"),
                arrayOf<Any>(4901, "Dharok's platelegs"),
                arrayOf<Any>(4902, "Dharok's platelegs"),
                arrayOf<Any>(4904, "Guthan's helm"),
                arrayOf<Any>(4905, "Guthan's helm"),
                arrayOf<Any>(4906, "Guthan's helm"),
                arrayOf<Any>(4907, "Guthan's helm"),
                arrayOf<Any>(4908, "Guthan's helm"),
                arrayOf<Any>(4910, "Guthan's spear"),
                arrayOf<Any>(4911, "Guthan's spear"),
                arrayOf<Any>(4912, "Guthan's spear"),
                arrayOf<Any>(4913, "Guthan's spear"),
                arrayOf<Any>(4914, "Guthan's spear"),
                arrayOf<Any>(4916, "Guthan's body"),
                arrayOf<Any>(4917, "Guthan's body"),
                arrayOf<Any>(4918, "Guthan's body"),
                arrayOf<Any>(4919, "Guthan's body"),
                arrayOf<Any>(4920, "Guthan's body"),
                arrayOf<Any>(4922, "Guthan's skirt"),
                arrayOf<Any>(4923, "Guthan's skirt"),
                arrayOf<Any>(4924, "Guthan's skirt"),
                arrayOf<Any>(4925, "Guthan's skirt"),
                arrayOf<Any>(4926, "Guthan's skirt"),
                arrayOf<Any>(4928, "Karil's coif"),
                arrayOf<Any>(4929, "Karil's coif"),
                arrayOf<Any>(4930, "Karil's coif"),
                arrayOf<Any>(4931, "Karil's coif"),
                arrayOf<Any>(4932, "Karil's coif"),
                arrayOf<Any>(4934, "Karil's x-bow"),
                arrayOf<Any>(4935, "Karil's x-bow"),
                arrayOf<Any>(4936, "Karil's x-bow"),
                arrayOf<Any>(4937, "Karil's x-bow"),
                arrayOf<Any>(4938, "Karil's x-bow"),
                arrayOf<Any>(4940, "Karil's top"),
                arrayOf<Any>(4941, "Karil's top"),
                arrayOf<Any>(4942, "Karil's top"),
                arrayOf<Any>(4943, "Karil's top"),
                arrayOf<Any>(4944, "Karil's top"),
                arrayOf<Any>(4946, "Karil's skirt"),
                arrayOf<Any>(4947, "Karil's skirt"),
                arrayOf<Any>(4948, "Karil's skirt"),
                arrayOf<Any>(4949, "Karil's skirt"),
                arrayOf<Any>(4950, "Karil's skirt"),
                arrayOf<Any>(4952, "Torag's helm"),
                arrayOf<Any>(4953, "Torag's helm"),
                arrayOf<Any>(4954, "Torag's helm"),
                arrayOf<Any>(4955, "Torag's helm"),
                arrayOf<Any>(4956, "Torag's helm"),
                arrayOf<Any>(4958, "Torag's hammers"),
                arrayOf<Any>(4959, "Torag's hammers"),
                arrayOf<Any>(4960, "Torag's hammers"),
                arrayOf<Any>(4961, "Torag's hammers"),
                arrayOf<Any>(4962, "Torag's hammers"),
                arrayOf<Any>(4964, "Torag's body"),
                arrayOf<Any>(4965, "Torag's body"),
                arrayOf<Any>(4966, "Torag's body"),
                arrayOf<Any>(4967, "Torag's body"),
                arrayOf<Any>(4968, "Torag's body"),
                arrayOf<Any>(4970, "Torag's legs"),
                arrayOf<Any>(4971, "Torag's legs"),
                arrayOf<Any>(4972, "Torag's legs"),
                arrayOf<Any>(4973, "Torag's legs"),
                arrayOf<Any>(4974, "Torag's legs"),
                arrayOf<Any>(4976, "Verac's helm"),
                arrayOf<Any>(4977, "Verac's helm"),
                arrayOf<Any>(4978, "Verac's helm"),
                arrayOf<Any>(4979, "Verac's helm"),
                arrayOf<Any>(4980, "Verac's helm"),
                arrayOf<Any>(4982, "Verac's flail"),
                arrayOf<Any>(4983, "Verac's flail"),
                arrayOf<Any>(4984, "Verac's flail"),
                arrayOf<Any>(4985, "Verac's flail"),
                arrayOf<Any>(4986, "Verac's flail"),
                arrayOf<Any>(4988, "Verac's top"),
                arrayOf<Any>(4989, "Verac's top"),
                arrayOf<Any>(4990, "Verac's top"),
                arrayOf<Any>(4991, "Verac's top"),
                arrayOf<Any>(4992, "Verac's top"),
                arrayOf<Any>(4994, "Verac's skirt"),
                arrayOf<Any>(4995, "Verac's skirt"),
                arrayOf<Any>(4996, "Verac's skirt"),
                arrayOf<Any>(4997, "Verac's skirt"),
                arrayOf<Any>(4998, "Verac's skirt")
            )

            /**
             * Gets the formatted cost of a Barrows item based on its name and charge level.
             *
             * @param name the name of the item
             * @param item the item object
             * @return the formatted cost as an integer
             */
            fun getFormattedCost(name: String, item: Item): Int {
                val ticks = TICKS
                val degrades = intArrayOf(100, 75, 50, 25, 0)
                for (i in prices.indices) {
                    val check = prices[i][0] as String
                    if (check == name) {
                        var degrade = 0
                        for (d in degrades) {
                            if (item.name.contains(d.toString())) {
                                degrade = d
                                break
                            }
                        }
                        degrade -= (25 - (25 * (item.charge.toDouble() / ticks.toDouble()))).toInt()
                        val max = prices[i][1] as Int * 1000
                        return (max - (max * (degrade * 0.01))).toInt()
                    }
                }
                return 0
            }

            /**
             * Gets the base cost for a Barrows item based on its name.
             *
             * @param name the name of the item
             * @return the cost as an integer
             */
            fun getCost(name: String): Int {
                for (i in prices.indices) {
                    val check = prices[i][0] as String
                    if (check == name) {
                        return prices[i][1] as Int
                    }
                }
                return 0
            }

            /**
             * Checks if the item with the specified ID is part of the Barrows set.
             *
             * @param id the item ID
             * @return true if the item is part of Barrows, false otherwise
             */
            fun isBarrowsItem(id: Int): Boolean {
                for (i in ITEMS.indices) {
                    if (ITEMS[i][0] as Int == id) {
                        return true
                    }
                }
                return false
            }

            /**
             * Retrieves the formatted name for a Barrows item based on its ID.
             *
             * @param id the item ID
             * @return the formatted name of the item
             */
            fun formattedName(id: Int): String? {
                for (i in ITEMS.indices) {
                    if (ITEMS[i][0] as Int == id) {
                        return ITEMS[i][1] as String
                    }
                }
                return null
            }

            /**
             * Gets the equipment type (e.g., weapon, body, helm, legs) for a Barrows item based on its name.
             *
             * @param name the name of the item
             * @return the type of the equipment as a string
             */
            fun getEquipmentType(name: String): String? {
                var name = name
                name = name.lowercase(Locale.getDefault()).replace("verac's", "").replace("karil's", "")
                    .replace("dharok's", "").replace("torag's", "").replace("guthan's", "").replace("ahrim's", "")
                    .trim { it <= ' ' }
                for (i in weapon_names.indices) {
                    if (weapon_names[i].contains(name)) {
                        return "weapon"
                    }
                }
                for (k in body_names.indices) {
                    if (body_names[k].contains(name)) {
                        return "body"
                    }
                }
                for (z in leg_names.indices) {
                    if (leg_names[z].contains(name)) {
                        return "legs"
                    }
                }
                for (q in helm_names.indices) {
                    if (helm_names[q].contains(name)) {
                        return "helm"
                    }
                }
                return null
            }

            /**
             * Gets single name.
             *
             * @param name the name.
             * @return the single name.
             */
            fun getSingleName(name: String): String? {
                var name = name
                name = name.lowercase(Locale.getDefault()).replace("verac's", "").replace("karil's", "")
                    .replace("dharok's", "").replace("torag's", "").replace("guthan's", "").replace("ahrim's", "")
                    .trim { it <= ' ' }
                for (i in weapon_names.indices) {
                    if (weapon_names[i].contains(name)) {
                        return weapon_names[i]
                    }
                }
                for (k in body_names.indices) {
                    if (body_names[k].contains(name)) {
                        return body_names[k]
                    }
                }
                for (z in leg_names.indices) {
                    if (leg_names[z].contains(name)) {
                        return leg_names[z]
                    }
                }
                for (q in helm_names.indices) {
                    if (helm_names[q].contains(name)) {
                        return helm_names[q]
                    }
                }
                return null
            }
        }
    }
}
