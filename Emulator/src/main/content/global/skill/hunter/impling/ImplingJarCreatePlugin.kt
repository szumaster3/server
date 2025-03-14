package content.global.skill.hunter.impling

import core.api.*
import core.game.interaction.NodeUsageEvent
import core.game.interaction.UseWithHandler
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.node.scenery.Scenery
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs.consts.Items

@Initializable
class ImplingJarCreatePlugin :
    UseWithHandler(
        Items.SIEVE_6097,
        Items.ANCHOVY_OIL_11264,
        Items.SWAMP_TAR_1939,
        Items.OIL_LAMP_4525,
        Items.OIL_LANTERN_4535,
        Items.BULLSEYE_LANTERN_4546,
        Items.SAPPHIRE_LANTERN_4700,
        Items.IMP_REPELLENT_11262,
        Items.BUTTERFLY_JAR_10012,
    ) {
    private val FLOWERS = (Items.FLOWERS_2460..Items.FLOWERS_2477).toIntArray()

    override fun newInstance(arg: Any?): Plugin<Any> {
        addHandler(Items.ANCHOVY_PASTE_11266, ITEM_TYPE, this)
        for (i in FLOWERS) {
            addHandler(i, ITEM_TYPE, this)
        }
        for (i in getChildren(5908)) {
            addHandler(i, OBJECT_TYPE, this)
        }
        addHandler(5909, OBJECT_TYPE, this)
        return this
    }

    override fun handle(event: NodeUsageEvent): Boolean {
        val player = event.player
        if (event.usedWith is Scenery) {
            if ((event.usedItem.id in 4525..4700) ||
                event.usedItem.id == 1939 ||
                event.usedItem.id == 11262 ||
                event.usedItem.id == 10012
            ) {
                fillOilStill(player, event.usedItem)
            }
            return true
        }
        if ((event.usedWith as Item).id == 6097) {
            makeOil(player)
        } else if (event.usedItem.id == 11264) {
            makeRepellent(player, event.usedItem, event.usedWith as Item)
        }
        return true
    }

    private fun fillOilStill(
        player: Player,
        used: Item,
    ) {
        val configValue = getVarp(player, 425)
        if (used.id == 11262 || used.id == 10012) {
            if (configValue == 0 && used.id == 11262) {
                player.inventory.replace(Item(229), used.slot)
                sendMessage(player, "You refine some imp repellent.")
                setVarp(player, 425, 64, true)
                return
            } else if (configValue == 32) {
                sendMessage(player, "There is already lamp oil in the still.")
                return
            } else if (configValue == 64) {
                if (used.id == 11262) {
                    sendMessage(player, "There is already imp repellent in the still.")
                } else {
                    player.inventory.replace(Item(11260), used.slot)
                    setVarp(player, 425, 0, true)
                    sendMessage(player, "You turn the butterfly jar into an impling jar.")
                }
                return
            }
            sendMessage(player, "There is no refined imp repellent in the still.")
            return
        }
        if (configValue == 64) {
            sendMessage(player, "There is already imp repellent in the still.")
            return
        }
        if (configValue == 32 && used.id == 1939) {
            sendMessage(player, "There is already lamp oil in the still.")
            return
        }
        if (used.id == 1939) {
            if (player.inventory.remove(Item(1939))) {
                setVarp(player, 425, 32, true)
                sendMessage(player, "You refine some swamp tar into lamp oil.")
            }
        } else {
            if (configValue == 0) {
                sendMessage(player, "There is no oil in the still.")
            } else {
                if (inInventory(player, 4525, 1) ||
                    inInventory(player, 4535, 1) ||
                    inInventory(
                        player,
                        4546,
                        1,
                    ) ||
                    inInventory(player, 4700, 1)
                ) {
                    val replace =
                        Item(
                            if (used.id ==
                                4525
                            ) {
                                4522
                            } else if (used.id == 4535) {
                                4537
                            } else if (used.id == 4546) {
                                4548
                            } else {
                                4701
                            },
                        )
                    player.inventory.replace(replace, used.slot)
                    setVarp(player, 425, 0, true)
                    sendMessage(player, "You fill the item with oil.")
                }
            }
        }
    }

    private fun makeRepellent(
        player: Player,
        oil: Item,
        usedWith: Item,
    ) {
        if (isFlower(usedWith.id)) {
            if (removeItem(player, usedWith)) {
                player.inventory.replace(Item(11262), oil.slot)
                sendMessage(
                    player,
                    "You mix the flower petals with the anchovy oil to make a very strange-smelling concoction.",
                )
            }
        }
    }

    private fun makeOil(player: Player) {
        if (!inInventory(player, 229, 1)) {
            sendMessage(player, "You need an empty vial to put your anchovy oil into.")
            return
        }
        if (!inInventory(player, 11266, 8)) {
            sendMessage(player, "You need 8 anchovy paste's in order to make anchovy oil.")
            return
        }
        if (player.inventory.remove(Item(11266, 8), Item(229))) {
            player.inventory.add(Item(11264))
        }
    }

    private fun isFlower(id: Int): Boolean {
        for (i in FLOWERS) {
            if (i == id) {
                return true
            }
        }
        return false
    }
}
