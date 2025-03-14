package content.global.ame.evilchicken

import content.global.ame.RandomEventNPC
import core.api.getWorldTicks
import core.api.utils.WeightBasedTable
import core.game.node.entity.Entity
import core.game.node.entity.npc.NPC
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import core.tools.RandomFunction
import org.rs.consts.Items
import org.rs.consts.NPCs
import java.lang.Integer.max

val ids = 2463..2468

class EvilChickenNPC(
    override var loot: WeightBasedTable? = null,
) : RandomEventNPC(NPCs.EVIL_CHICKEN_2463) {
    val phrases =
        arrayOf(
            "Bwuk",
            "Bwuk bwuk bwuk",
            "Flee from me, @name!",
            "Begone, @name!",
            "Bwaaaauuuk bwuk bwuk",
            "MUAHAHAHAHAAA!",
        )

    override fun talkTo(npc: NPC) {}

    override fun init() {
        super.init()
        val index = max(0, (player.properties.combatLevel / 20) - 1)
        val id = ids.toList()[index]
        this.transform(id)
        this.attack(player)
        sendChat(
            phrases.random().replace(
                "@name",
                player.name.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() },
            ),
        )
        this.isRespawn = false
    }

    override fun finalizeDeath(killer: Entity?) {
        super.finalizeDeath(killer)
        GroundItemManager.create(Item(Items.FEATHER_314, RandomFunction.random(50, 300)), this.dropLocation, player)
    }

    override fun tick() {
        if (!player.location.withinDistance(this.location, 8)) {
            this.terminate()
        }
        if (getWorldTicks() % 10 == 0) {
            sendChat(
                phrases.random().replace(
                    "@name",
                    player.name.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() },
                ),
            )
        }
        super.tick()
        if (!player.viewport.currentPlane.npcs
                .contains(this)
        ) {
            this.clear()
        }
    }
}
