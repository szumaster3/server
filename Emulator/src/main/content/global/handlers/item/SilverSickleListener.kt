package content.global.handlers.item

import content.region.morytania.quest.druidspirit.NSUtils.castBloom
import core.api.*
import core.api.quest.getQuestStage
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import org.rs.consts.Animations
import org.rs.consts.Items
import org.rs.consts.Quests

class SilverSickleListener : InteractionListener {
    private val sickleIDs =
        intArrayOf(Items.SILVER_SICKLEB_2963, Items.ENCHANTED_SICKLE_EMERALDB_13156, Items.SILVER_SICKLE_EMERALDB_13155)

    override fun defineListeners() {
        on(sickleIDs, IntType.ITEM, "operate", "cast bloom") { player, node ->
            if (getQuestStage(player, Quests.NATURE_SPIRIT) >= 75) {
                if (!inZone(player, "Mort Myre Swamp")) {
                    sendMessage(player, "You can cast the spell in the swamp.")
                    return@on true
                }

                if (inEquipment(player, Items.ENCHANTED_SICKLE_EMERALDB_13156)) {
                    return@on true
                }
                if (node.name.contains("emerald", true)) {
                    animate(player, Animations.LEGACY_OF_SEERGAZE_EMERALD_SICKLE_BLOOM_9021)
                } else {
                    animate(player, Animations.SILVER_SICKLE_1100)
                }
                castBloom(player)
            } else {
                sendDialogue(player, "You need to start the Nature Spirit to use this.")
            }
            return@on true
        }

        onEquip(Items.ENCHANTED_SICKLE_EMERALDB_13156) { player, node ->
            return@onEquip false
        }

        onUseWith(IntType.ITEM, Items.EMERALD_1605, Items.SILVER_SICKLEB_2963) { player, used, with ->
            val itemSlot = with.asItem().slot
            if (!inInventory(player, Items.CHISEL_1755)) {
                sendMessage(player, "You need a chisel to do that.")
                return@onUseWith false
            }

            if (removeItem(player, used.asItem())) {
                replaceSlot(player, itemSlot, Item(Items.SILVER_SICKLE_EMERALDB_13155, 1))
                player.dialogueInterpreter.sendItemMessage(
                    Items.SILVER_SICKLE_EMERALDB_13155,
                    "You carefully and skilfully construct an emerald-",
                    "adorned blessed silver sickle.",
                )
                rewardXP(player, Skills.CRAFTING, 20.0)
            }
            return@onUseWith true
        }
    }
}
