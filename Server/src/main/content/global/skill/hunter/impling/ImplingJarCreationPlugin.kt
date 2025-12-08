package content.global.skill.hunter.impling

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.game.node.item.Item
import shared.consts.Items
import shared.consts.Scenery

class ImplingJarCreationPlugin : InteractionListener {

    companion object {
        private val FLOWERS = Items.FLOWERS_2460..Items.FLOWERS_2477
        private val LANTERNS = intArrayOf(Items.OIL_LAMP_4525, Items.OIL_LANTERN_4535, Items.BULLSEYE_LANTERN_4546, Items.SAPPHIRE_LANTERN_4700)
    }

    override fun defineListeners() {

        /*
         * Handles use tar, imp repellent, jar, or lanterns on the oil still.
         */

        onUseWith(
            IntType.SCENERY,
            LANTERNS + Items.SWAMP_TAR_1939 + Items.IMP_REPELLENT_11262 + Items.BUTTERFLY_JAR_10012,
            Scenery.LAMP_OIL_STILL_5909
        ) { player, used, _ ->
            fillOilStill(player, used.asItem())
            return@onUseWith true
        }

        /*
         * Handles create anchovy oil (Sieve + anchovy paste)
         */

        onUseWith(IntType.ITEM, Items.ANCHOVY_PASTE_11266, Items.SIEVE_6097) { player, _, _ ->
            makeOil(player)
            return@onUseWith true
        }

        /*
         * Handles create imp repellent (Anchovy oil + flower)
         */

        onUseWith(IntType.ITEM, Items.ANCHOVY_OIL_11264, *FLOWERS.toIntArray()) { player, flower, oil ->
            makeRepellent(player, oil.asItem(), flower.asItem())
            return@onUseWith true
        }
    }

    private fun fillOilStill(player: Player, used: Item) {
        val state = getVarp(player, 425)

        when (used.id) {
            Items.IMP_REPELLENT_11262, Items.BUTTERFLY_JAR_10012 -> {
                when (state) {
                    0 -> {
                        if (used.id == Items.IMP_REPELLENT_11262) {
                            player.inventory.replace(Item(Items.VIAL_229), used.slot)
                            setVarp(player, 425, 64, true)
                            sendMessage(player, "You refine some imp repellent.")
                        } else {
                            sendMessage(player, "There is no refined imp repellent in the still.")
                        }
                    }

                    32 -> {
                        sendMessage(player, "There is already lamp oil in the still.")
                    }

                    64 -> {
                        if (used.id == Items.IMP_REPELLENT_11262) {
                            sendMessage(player, "There is already imp repellent in the still.")
                        } else {
                            player.inventory.replace(Item(Items.IMPLING_JAR_11260), used.slot)
                            setVarp(player, 425, 0, true)
                            sendMessage(player, "You turn the butterfly jar into an impling jar.")
                        }
                    }

                    else -> {
                        sendMessage(player, "There is no refined imp repellent in the still.")
                    }
                }

                return
            }

            Items.SWAMP_TAR_1939 -> {
                if (state == 32) {
                    sendMessage(player, "There is already lamp oil in the still.")
                    return
                }
                if (removeItem(player, Items.SWAMP_TAR_1939)) {
                    setVarp(player, 425, 32, true)
                    sendMessage(player, "You refine some swamp tar into lamp oil.")
                }

                return
            }
        }

        if (state == 0) {
            sendMessage(player, "There is no oil in the still.")
            return
        }

        val filled = when (used.id) {
            Items.OIL_LAMP_4525         -> Items.OIL_LAMP_4522
            Items.OIL_LANTERN_4535      -> Items.OIL_LANTERN_4537
            Items.BULLSEYE_LANTERN_4546 -> Items.BULLSEYE_LANTERN_4548
            Items.SAPPHIRE_LANTERN_4700 -> Items.SAPPHIRE_LANTERN_4701
            else -> null
        }

        if (filled != null) {
            player.inventory.replace(Item(filled), used.slot)
            setVarp(player, 425, 0, true)
            sendMessage(player, "You fill the item with oil.")
        }
    }

    private fun makeRepellent(player: Player, oil: Item, flower: Item) {
        if (removeItem(player, flower)) {
            player.inventory.replace(Item(Items.IMP_REPELLENT_11262), oil.slot)
            sendMessage(player, "You mix the flower petals with the anchovy oil to make a strange-smelling concoction.")
        }
    }

    private fun makeOil(player: Player) {
        if (!inInventory(player, Items.VIAL_229)) {
            sendMessage(player, "You need an empty vial to put your anchovy oil into.")
            return
        }
        if (!inInventory(player, Items.ANCHOVY_PASTE_11266, 8)) {
            sendMessage(player, "You need 8 anchovy pastes to make anchovy oil.")
            return
        }

        if (removeItem(player, Item(Items.ANCHOVY_PASTE_11266, 8)) && removeItem(player, Items.VIAL_229)) {
            addItem(player, Items.ANCHOVY_OIL_11264)
        }
    }
}
