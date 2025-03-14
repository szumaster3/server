package content.region.asgarnia.handlers.trollheim.godwars

import core.api.registerTimer
import core.api.spawnTimer
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.game.system.timer.impl.PoisonImmunity
import org.rs.consts.NPCs

private val GODWARS_NPC =
    intArrayOf(
        NPCs.KRIL_TSUTSAROTH_6203,
        NPCs.TSTANON_KARLAK_6204,
        NPCs.ZAKLN_GRITCH_6206,
        NPCs.BALFRUG_KREEYATH_6208,
        NPCs.KREEARRA_6222,
        NPCs.WINGMAN_SKREE_6223,
        NPCs.FLOCKLEADER_GEERIN_6225,
        NPCs.FLIGHT_KILISA_6227,
        NPCs.STARLIGHT_6248,
        NPCs.SERGEANT_STRONGSTACK_6261,
        NPCs.SERGEANT_STEELWILL_6263,
        NPCs.SERGEANT_GRIMSPIKE_6265,
        NPCs.GROWLER_6250,
        NPCs.BREE_6252,
        NPCs.GENERAL_GRAARDOR_6260,
    )

class GodwarsBossBehavior : NPCBehavior(*GODWARS_NPC) {
    override fun onCreation(self: NPC) {
        registerTimer(self, spawnTimer<PoisonImmunity>())
    }
}
