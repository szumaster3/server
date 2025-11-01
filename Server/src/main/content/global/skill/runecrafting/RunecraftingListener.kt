package content.global.skill.runecrafting

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import shared.consts.Items
import shared.consts.Vars

class RunecraftingListener : InteractionListener {

    private val pouchIDs = (Items.SMALL_POUCH_5509..Items.GIANT_POUCH_5515).toIntArray()
    private val tiara = IntArray(RunecraftingStaff.values().size) { RunecraftingStaff.values()[it].tiaraId }
    private val staves = IntArray(RunecraftingStaff.values().size) { RunecraftingStaff.values()[it].staffId }
    private val stavesMap = HashMap<Int, Int>()
    private val tiaraMap = HashMap<Int, Int>()

    init {
        for ((index, staffId) in staves.withIndex()) {
            stavesMap[staffId] = 1 shl index
        }
        for ((index, tiaraId) in tiara.withIndex()) {
            tiaraMap[tiaraId] = 1 shl index
        }
    }

    override fun defineListeners() {

        /*
         * Handles use of rc pouches.
         */

        on(pouchIDs, IntType.ITEM, "fill", "empty", "check", "drop") { player, node ->
            val option = getUsedOption(player)
            val runeEssenceAmount = amountInInventory(player, Items.RUNE_ESSENCE_1436)
            val pureEssenceAmount = amountInInventory(player, Items.PURE_ESSENCE_7936)

            if (runeEssenceAmount == pureEssenceAmount && option == "fill") return@on true

            val essence = checkAmount(runeEssenceAmount, pureEssenceAmount)

            when (option) {
                "fill" -> player.pouchManager.addToPouch(node.id, essence.amount, essence.id)
                "empty" -> player.pouchManager.withdrawFromPouch(node.id)
                "check" -> player.pouchManager.checkAmount(node.id)
                "drop" -> openDialogue(player, 9878, Item(node.id))
            }

            return@on true
        }

        /*
         * Handles use rc staff on altar object.
         */

        RunecraftingStaff.values().forEach { staff ->
            val altar = map(staff)
            altar?.let {
                onUseWith(IntType.SCENERY, staff.talisman, it.scenery) { player, used, _ ->
                    setTitle(player, 2)
                    sendOptions(player, "Do you want to enchant a tiara or staff?", "Tiara.", "Staff.")
                    addDialogueAction(player) { p, button ->
                        if (button == 2 && !inInventory(p, Items.TIARA_5525, 1)) {
                            return@addDialogueAction sendMessage(p, "You need a tiara.")
                        }
                        if (button == 3 && !inInventory(p, Items.RUNECRAFTING_STAFF_13629, 1)) {
                            return@addDialogueAction sendMessage(p, "You need a runecrafting staff.")
                        }
                        enchant(p, used.asItem(), button, staff)
                    }
                    return@onUseWith true
                }
            }
        }

        /*
         * Handles equip/unequip stuff.
         */

        onEquip(tiara + staves) { player, n ->
            val num = tiaraMap[n.id] ?: stavesMap[n.id] ?: 0
            setVarp(player, Vars.VARP_ABYSS_SCENERY_491, num)
            return@onEquip true
        }

        onUnequip(tiara + staves) { player, _ ->
            setVarp(player, Vars.VARP_ABYSS_SCENERY_491, 0)
            return@onUnequip true
        }
    }

    /**
     * Maps a [RunecraftingStaff] to [Altar].
     *
     * @param staff the rc staff.
     * @return the [Altar].
     */
    fun map(staff: RunecraftingStaff): Altar? {
        return try {
            Altar.valueOf(staff.name)
        } catch (e: IllegalArgumentException) {
            null
        }
    }

    private fun checkAmount(
        runeEssenceAmount: Int,
        pureEssenceAmount: Int,
    ): Item {
        val isRunePreferred = runeEssenceAmount >= pureEssenceAmount
        val id = if (isRunePreferred) Items.RUNE_ESSENCE_1436 else Items.PURE_ESSENCE_7936
        val amount = if (isRunePreferred) runeEssenceAmount else pureEssenceAmount

        return Item(id, amount)
    }

    private fun enchant(
        player: Player,
        itemId: Item,
        buttonId: Int,
        product: RunecraftingStaff,
    ) {
        closeDialogue(player)
        removeItem(player, if (buttonId == 3) Items.RUNECRAFTING_STAFF_13629 else Items.TIARA_5525)
        replaceSlot(player, itemId.index, if (buttonId == 3) Item(product.staffId, 1) else Item(product.tiaraId, 1))
        rewardXP(player, Skills.RUNECRAFTING, product.experience)
        sendMessage(player, "You bind the power of the talisman into your ${if (buttonId == 3) "staff" else "tiara"}.")
    }
}
