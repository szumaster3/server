package content.global.handlers.npc

import core.game.node.entity.npc.AbstractNPC
import core.game.world.map.Location
import core.plugin.Initializable
import org.rs.consts.NPCs

@Initializable
class GuardNPC(
    id: Int = 0,
    location: Location? = null,
) : AbstractNPC(id, location) {
    override fun construct(
        id: Int,
        location: Location,
        vararg objects: Any,
    ): AbstractNPC {
        return GuardNPC(id, location)
    }

    override fun getIds(): IntArray {
        return ID
    }

    companion object {
        private val ID =
            intArrayOf(
                NPCs.BARBARIAN_GUARD_197,
                NPCs.BLACK_GUARD_BERSERKER_2134,
                NPCs.BLACK_GUARD_BERSERKER_2135,
                NPCs.BLACK_GUARD_BERSERKER_2136,
                NPCs.CAVE_GOBLIN_GUARD_2073,
                NPCs.CAVE_GOBLIN_GUARD_2074,
                NPCs.CITY_GUARD_862,
                NPCs.ENCLAVE_GUARD_870,
                NPCs.FORTRESS_GUARD_4603,
                NPCs.FORTRESS_GUARD_4604,
                NPCs.FORTRESS_GUARD_4605,
                NPCs.FORTRESS_GUARD_4606,
                NPCs.FORTRESS_GUARD_609,
                NPCs.GNOME_GUARD_163,
                NPCs.GNOME_GUARD_164,
                NPCs.GOBLIN_GUARD_489,
                NPCs.GUARDIAN_OF_ARMADYL_274,
                NPCs.GUARDIAN_OF_ARMADYL_275,
                NPCs.GUARD_206,
                NPCs.GUARD_2699,
                NPCs.GUARD_2700,
                NPCs.GUARD_2701,
                NPCs.GUARD_2702,
                NPCs.GUARD_2703,
                NPCs.GUARD_296,
                NPCs.GUARD_298,
                NPCs.GUARD_299,
                NPCs.GUARD_32,
                NPCs.GUARD_3228,
                NPCs.GUARD_3229,
                NPCs.GUARD_3230,
                NPCs.GUARD_3231,
                NPCs.GUARD_3232,
                NPCs.GUARD_3233,
                NPCs.GUARD_3241,
                NPCs.GUARD_3407,
                NPCs.GUARD_3408,
                NPCs.GUARD_3715,
                NPCs.GUARD_4257,
                NPCs.GUARD_4258,
                NPCs.GUARD_4259,
                NPCs.GUARD_4260,
                NPCs.GUARD_4307,
                NPCs.GUARD_4308,
                NPCs.GUARD_4309,
                NPCs.GUARD_4310,
                NPCs.GUARD_4311,
                NPCs.GUARD_4336,
                NPCs.GUARD_5800,
                NPCs.GUARD_5801,
                NPCs.GUARD_5919,
                NPCs.GUARD_5920,
                NPCs.GUARD_678,
                NPCs.GUARD_9,
                NPCs.GUARD_BANDIT_196,
                NPCs.HAM_GUARD_1710,
                NPCs.HAM_GUARD_1711,
                NPCs.HAM_GUARD_1712,
                NPCs.JAIL_GUARD_447,
                NPCs.JAIL_GUARD_448,
                NPCs.JAIL_GUARD_449,
                NPCs.JAIL_GUARD_917,
                NPCs.KHAZARD_GUARD_253,
                NPCs.KHAZARD_GUARD_254,
                NPCs.KHAZARD_GUARD_255,
                NPCs.KHAZARD_GUARD_256,
                NPCs.MARKET_GUARD_1317,
                NPCs.MARKET_GUARD_2236,
                NPCs.MARKET_GUARD_2571,
                NPCs.PIRATE_GUARD_799,
                NPCs.ROWDY_GUARD_842,
                NPCs.SECURITY_GUARD_4375,
                NPCs.SHANTAY_GUARD_837,
                NPCs.TOWER_GUARD_877,
                NPCs.TYRAS_GUARD_1200,
                NPCs.TYRAS_GUARD_1203,
                NPCs.TYRAS_GUARD_1204,
            )
    }
}
