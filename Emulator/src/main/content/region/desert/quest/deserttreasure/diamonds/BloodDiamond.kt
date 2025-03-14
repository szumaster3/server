package content.region.desert.quest.deserttreasure.diamonds

import content.region.desert.quest.deserttreasure.handlers.DTUtils
import content.region.desert.quest.deserttreasure.DesertTreasure
import core.api.*
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.entity.npc.NPC
import core.game.world.map.Direction
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Scenery

class BloodDiamond : InteractionListener {
    override fun defineListeners() {
        onUseWith(ITEM, Items.SILVER_POT_4660, Items.SPICE_2007) { player, used, with ->
            if (removeItem(player, used) && removeItem(player, with)) {
                sendMessage(player, "You add some spices to the pot.")
                addItemOrDrop(player, Items.SILVER_POT_4664)
            }
            return@onUseWith true
        }
        onUseWith(ITEM, Items.SILVER_POT_4660, Items.GARLIC_POWDER_4668) { player, used, with ->
            if (removeItem(player, used) && removeItem(player, with)) {
                sendMessage(player, "You add some crushed garlic to the pot.")
                addItemOrDrop(player, Items.SILVER_POT_4662)
            }
            return@onUseWith true
        }
        onUseWith(ITEM, Items.SILVER_POT_4662, Items.SPICE_2007) { player, used, with ->
            if (removeItem(player, used) && removeItem(player, with)) {
                sendMessage(player, "You add some spices to the pot.")
                addItemOrDrop(player, Items.SILVER_POT_4666)
            }
            return@onUseWith true
        }
        onUseWith(ITEM, Items.SILVER_POT_4664, Items.GARLIC_POWDER_4668) { player, used, with ->
            if (removeItem(player, used) && removeItem(player, with)) {
                sendMessage(player, "You add some crushed garlic to the pot.")
                addItemOrDrop(player, Items.SILVER_POT_4666)
            }
            return@onUseWith true
        }

        onUseWith(ITEM, Items.BLESSED_POT_4661, Items.SPICE_2007) { player, used, with ->
            if (removeItem(player, used) && removeItem(player, with)) {
                sendMessage(player, "You add some spices to the pot.")
                addItemOrDrop(player, Items.BLESSED_POT_4665)
            }
            return@onUseWith true
        }
        onUseWith(ITEM, Items.BLESSED_POT_4661, Items.GARLIC_POWDER_4668) { player, used, with ->
            if (removeItem(player, used) && removeItem(player, with)) {
                sendMessage(player, "You add some crushed garlic to the pot.")
                addItemOrDrop(player, Items.BLESSED_POT_4663)
            }
            return@onUseWith true
        }
        onUseWith(ITEM, Items.BLESSED_POT_4663, Items.SPICE_2007) { player, used, with ->
            if (removeItem(player, used) && removeItem(player, with)) {
                sendMessage(player, "You add some spices to the pot.")
                addItemOrDrop(player, Items.BLESSED_POT_4667)
            }
            return@onUseWith true
        }
        onUseWith(ITEM, Items.BLESSED_POT_4665, Items.GARLIC_POWDER_4668) { player, used, with ->
            if (removeItem(player, used) && removeItem(player, with)) {
                sendMessage(player, "You add some crushed garlic to the pot.")
                addItemOrDrop(player, Items.BLESSED_POT_4667)
            }
            return@onUseWith true
        }

        onUseWith(
            ITEM,
            intArrayOf(
                Items.SILVER_POT_4660,
                Items.BLESSED_POT_4661,
                Items.SILVER_POT_4662,
                Items.BLESSED_POT_4663,
                Items.SILVER_POT_4664,
                Items.BLESSED_POT_4665,
                Items.SILVER_POT_4666,
                Items.BLESSED_POT_4667,
            ),
            Items.GARLIC_1550,
        ) { player, _, _ ->
            sendMessage(player, "You need to crush the garlic before adding it to the pot.")
            return@onUseWith true
        }

        onUseWith(SCENERY, Items.BLESSED_POT_4667, Scenery.VAMPIRE_TOMB_6437) { player, used, with ->
            val prevNpc = getAttribute<NPC?>(player, DesertTreasure.attributeDessousInstance, null)
            prevNpc?.clear()

            sendMessage(player, "You pour the blood from the pot onto the tomb.")
            removeItem(player, used)

            val scenery = with.asScenery()
            replaceScenery(scenery, Scenery.VAMPIRE_TOMB_6438, Animation(1915).duration)
            animateScenery(player, scenery, 1915)

            DTUtils.spawnProjectiles()

            val npc = NPC(NPCs.DESSOUS_1914)
            queueScript(player, 1, QueueStrength.SOFT) { stage: Int ->
                when (stage) {
                    0 -> {
                        spawnProjectile(Location(3570, 3402), Location(3570, 3405), 351, 0, 0, 0, 40, 0)
                        delayScript(player, 1)
                    }

                    1 -> {
                        npc.apply {
                            isRespawn = false
                            isWalks = false
                            location = Location(3570, 3405, 0)
                            direction = Direction.NORTH
                        }
                        setAttribute(player, DesertTreasure.attributeDessousInstance, npc)
                        setAttribute(npc, "target", player)

                        npc.init()
                        npc.attack(player)
                        stopExecuting(player)
                    }

                    else -> stopExecuting(player)
                }
            }

            return@onUseWith true
        }
    }
}
