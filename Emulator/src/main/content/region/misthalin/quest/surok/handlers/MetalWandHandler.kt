package content.region.misthalin.quest.surok.handlers

import content.global.skill.runecrafting.scenery.Altar
import core.api.*
import core.game.interaction.NodeUsageEvent
import core.game.interaction.UseWithHandler
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs.consts.Sounds

@Initializable
class MetalWandHandler : UseWithHandler(WhatLiesBelowListener.WAND) {
    @Throws(Throwable::class)
    override fun newInstance(arg: Any?): Plugin<Any> {
        addHandler(Altar.CHAOS.objs, OBJECT_TYPE, this)
        return this
    }

    override fun handle(event: NodeUsageEvent): Boolean {
        val player = event.player
        if (!inInventory(player, WhatLiesBelowListener.CHAOS_RUNES, 15)) {
            player.sendMessage("You need 15 chaos runes.")
            return true
        }

        val chaosItem =
            if (inInventory(player, WhatLiesBelowListener.CHAOS_TALISMAN)
            ) {
                WhatLiesBelowListener.CHAOS_TALISMAN
            } else {
                WhatLiesBelowListener.CHAOS_TIARA
            }
        if (getStatLevel(player, Skills.RUNECRAFTING) >= 35) {
            if (chaosItem != null && removeItem(player, Item(WhatLiesBelowListener.CHAOS_RUNES, 15))) {
                lock(player, 5)
                playAudio(player, Sounds.SUROK_BINDWAND_3524)
                removeItem(player, WhatLiesBelowListener.WAND)
                addItem(player, WhatLiesBelowListener.INFUSED_WAND)
                animate(player, 6104)
                sendDialogueLines(
                    player,
                    "The metal wand bursts into life and crackles with arcane",
                    "power. This is a powerful instrument indeed!",
                )
            }
            return true
        } else {
            sendDialogue(player, "You need a Runecrafting level of 35 to make the infused wand.")
            return false
        }
    }
}
