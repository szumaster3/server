package core.game.node.entity.combat.equipment

import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.world.update.flag.context.Graphics
import core.tools.RandomFunction
import org.rs.consts.Items

enum class ArmourSet(
    private val endGraphics: Graphics?,
    set: Array<IntArray>,
) {
    AHRIM(
        endGraphics = Graphics(401, 96),
        set =
            arrayOf(
                intArrayOf(
                    Items.AHRIMS_HOOD_4708,
                    Items.AHRIMS_HOOD_100_4856,
                    Items.AHRIMS_HOOD_75_4857,
                    Items.AHRIMS_HOOD_50_4858,
                    Items.AHRIMS_HOOD_25_4859,
                ),
                intArrayOf(
                    Items.AHRIMS_STAFF_4710,
                    Items.AHRIMS_STAFF_100_4862,
                    Items.AHRIMS_STAFF_75_4863,
                    Items.AHRIMS_STAFF_50_4864,
                    Items.AHRIMS_STAFF_25_4865,
                ),
                intArrayOf(
                    Items.AHRIMS_ROBETOP_4712,
                    Items.AHRIMS_TOP_100_4868,
                    Items.AHRIMS_TOP_75_4869,
                    Items.AHRIMS_TOP_50_4870,
                    Items.AHRIMS_TOP_25_4871,
                ),
                intArrayOf(
                    Items.AHRIMS_ROBESKIRT_4714,
                    Items.AHRIMS_SKIRT_100_4874,
                    Items.AHRIMS_SKIRT_75_4875,
                    Items.AHRIMS_SKIRT_50_4876,
                    Items.AHRIMS_SKIRT_25_4877,
                ),
            ),
    ) {
        override fun effect(
            e: Entity,
            victim: Entity,
            state: BattleState,
        ): Boolean {
            if (RandomFunction.random(100) < 20) {
                victim.getSkills().updateLevel(Skills.STRENGTH, -5, 0)
                return true
            }
            return false
        }

        override fun isUsing(e: Entity?): Boolean {
            if (e is NPC && e.id == 2025) {
                return true
            }
            return super.isUsing(e)
        }
    },

    DHAROK(
        endGraphics = null,
        set =
            arrayOf(
                intArrayOf(
                    Items.DHAROKS_HELM_4716,
                    Items.DHAROKS_HELM_100_4880,
                    Items.DHAROKS_HELM_75_4881,
                    Items.DHAROKS_HELM_50_4882,
                    Items.DHAROKS_HELM_25_4883,
                ),
                intArrayOf(
                    Items.DHAROKS_GREATAXE_4718,
                    Items.DHAROKS_AXE_100_4886,
                    Items.DHAROKS_AXE_75_4887,
                    Items.DHAROKS_AXE_50_4888,
                    Items.DHAROKS_AXE_25_4889,
                ),
                intArrayOf(
                    Items.DHAROKS_PLATEBODY_4720,
                    Items.DHAROKS_BODY_100_4892,
                    Items.DHAROKS_BODY_75_4893,
                    Items.DHAROKS_BODY_50_4894,
                    Items.DHAROKS_BODY_25_4895,
                ),
                intArrayOf(
                    Items.DHAROKS_PLATELEGS_4722,
                    Items.DHAROKS_LEGS_100_4898,
                    Items.DHAROKS_LEGS_75_4899,
                    Items.DHAROKS_LEGS_50_4900,
                    Items.DHAROKS_LEGS_25_4901,
                ),
            ),
    ) {
        override fun isUsing(e: Entity?): Boolean {
            if (e is NPC && e.id == 2026) {
                return true
            }
            return super.isUsing(e)
        }
    },

    GUTHAN(
        endGraphics = Graphics(398, 96),
        set =
            arrayOf(
                intArrayOf(
                    Items.GUTHANS_HELM_4724,
                    Items.GUTHANS_HELM_100_4904,
                    Items.GUTHANS_HELM_75_4905,
                    Items.GUTHANS_HELM_50_4906,
                    Items.GUTHANS_HELM_25_4907,
                ),
                intArrayOf(
                    Items.GUTHANS_WARSPEAR_4726,
                    Items.GUTHANS_SPEAR_100_4910,
                    Items.GUTHANS_SPEAR_75_4911,
                    Items.GUTHANS_SPEAR_50_4912,
                    Items.GUTHANS_SPEAR_25_4913,
                ),
                intArrayOf(
                    Items.GUTHANS_PLATEBODY_4728,
                    Items.GUTHANS_BODY_100_4916,
                    Items.GUTHANS_BODY_75_4917,
                    Items.GUTHANS_BODY_50_4918,
                    Items.GUTHANS_BODY_25_4919,
                ),
                intArrayOf(
                    Items.GUTHANS_CHAINSKIRT_4730,
                    Items.GUTHANS_SKIRT_100_4922,
                    Items.GUTHANS_SKIRT_75_4923,
                    Items.GUTHANS_SKIRT_50_4924,
                    Items.GUTHANS_SKIRT_25_4925,
                ),
            ),
    ) {
        override fun effect(
            e: Entity,
            victim: Entity,
            state: BattleState,
        ): Boolean {
            if (RandomFunction.random(100) < 25) {
                e.getSkills().heal(state.estimatedHit)
                return true
            }
            return false
        }

        override fun isUsing(e: Entity?): Boolean {
            if (e is NPC && e.id == 2027) {
                return true
            }
            return super.isUsing(e)
        }
    },

    KARIL(
        endGraphics = Graphics(400, 96),
        set =
            arrayOf(
                intArrayOf(
                    Items.KARILS_COIF_4732,
                    Items.KARILS_COIF_100_4928,
                    Items.KARILS_COIF_75_4929,
                    Items.KARILS_COIF_50_4930,
                    Items.KARILS_COIF_25_4931,
                ),
                intArrayOf(
                    Items.KARILS_CROSSBOW_4734,
                    Items.KARILS_X_BOW_100_4934,
                    Items.KARILS_X_BOW_75_4935,
                    Items.KARILS_X_BOW_50_4936,
                    Items.KARILS_X_BOW_25_4937,
                ),
                intArrayOf(
                    Items.KARILS_LEATHERTOP_4736,
                    Items.KARILS_TOP_100_4940,
                    Items.KARILS_TOP_75_4941,
                    Items.KARILS_TOP_50_4942,
                    Items.KARILS_TOP_25_4943,
                ),
                intArrayOf(
                    Items.KARILS_LEATHERSKIRT_4738,
                    Items.KARILS_SKIRT_100_4946,
                    Items.KARILS_SKIRT_75_4947,
                    Items.KARILS_SKIRT_50_4948,
                    Items.KARILS_SKIRT_25_4949,
                ),
            ),
    ) {
        override fun effect(
            e: Entity,
            victim: Entity,
            state: BattleState,
        ): Boolean {
            if (state.estimatedHit > 9 && RandomFunction.random(100) < 20) {
                victim.getSkills().updateLevel(Skills.AGILITY, -(state.estimatedHit / 10), 0)
                return true
            }
            return false
        }

        override fun isUsing(e: Entity?): Boolean {
            if (e is NPC && e.id == 2028) {
                return true
            }
            return super.isUsing(e)
        }
    },

    TORAG(
        endGraphics = Graphics(399, 96),
        set =
            arrayOf(
                intArrayOf(
                    Items.TORAGS_HELM_4745,
                    Items.TORAGS_HELM_100_4952,
                    Items.TORAGS_HELM_75_4953,
                    Items.TORAGS_HELM_50_4954,
                    Items.TORAGS_HELM_25_4955,
                ),
                intArrayOf(
                    Items.TORAGS_HAMMERS_4747,
                    Items.TORAGS_HAMMER_100_4958,
                    Items.TORAGS_HAMMER_75_4959,
                    Items.TORAGS_HAMMER_50_4960,
                    Items.TORAGS_HAMMER_25_4961,
                ),
                intArrayOf(
                    Items.TORAGS_PLATEBODY_4749,
                    Items.TORAGS_BODY_100_4964,
                    Items.TORAGS_BODY_75_4965,
                    Items.TORAGS_BODY_50_4966,
                    Items.TORAGS_BODY_25_4967,
                ),
                intArrayOf(
                    Items.TORAGS_PLATELEGS_4751,
                    Items.TORAGS_LEGS_100_4970,
                    Items.TORAGS_LEGS_75_4971,
                    Items.TORAGS_LEGS_50_4972,
                    Items.TORAGS_LEGS_25_4973,
                ),
            ),
    ) {
        override fun effect(
            e: Entity,
            victim: Entity,
            state: BattleState,
        ): Boolean {
            if (state.estimatedHit > 0 && RandomFunction.random(100) < 20) {
                if (victim is Player) {
                    victim.settings.updateRunEnergy(20.0)
                }
                return true
            }
            return false
        }

        override fun isUsing(e: Entity?): Boolean {
            if (e is NPC && e.id == 2029) {
                return true
            }
            return super.isUsing(e)
        }
    },

    VERAC(
        endGraphics = null,
        set =
            arrayOf(
                intArrayOf(
                    Items.VERACS_HELM_4753,
                    Items.VERACS_HELM_100_4976,
                    Items.VERACS_HELM_75_4977,
                    Items.VERACS_HELM_50_4978,
                    Items.VERACS_HELM_25_4979,
                ),
                intArrayOf(
                    Items.VERACS_FLAIL_4755,
                    Items.VERACS_FLAIL_100_4982,
                    Items.VERACS_FLAIL_75_4983,
                    Items.VERACS_FLAIL_50_4984,
                    Items.VERACS_FLAIL_25_4985,
                ),
                intArrayOf(
                    Items.VERACS_BRASSARD_4757,
                    Items.VERACS_TOP_100_4988,
                    Items.VERACS_TOP_75_4989,
                    Items.VERACS_TOP_50_4990,
                    Items.VERACS_TOP_25_4991,
                ),
                intArrayOf(
                    Items.VERACS_PLATESKIRT_4759,
                    Items.VERACS_SKIRT_100_4994,
                    Items.VERACS_SKIRT_75_4995,
                    Items.VERACS_SKIRT_50_4996,
                    Items.VERACS_SKIRT_25_4997,
                ),
            ),
    ) {
        override fun isUsing(e: Entity?): Boolean {
            if (e is NPC && e.id == 2030) {
                return true
            }
            return super.isUsing(e)
        }
    }, ;

    private val sets = Array(4) { arrayOfNulls<Item>(5) }

    init {
        for (i in set.indices) {
            for (k in sets[i].indices) {
                sets[i][k] = Item(set[i][k])
            }
        }
    }

    fun visualize(
        e: Entity?,
        victim: Entity,
    ) {
        if (endGraphics != null) {
            victim.graphics(endGraphics)
        }
    }

    open fun effect(
        e: Entity,
        victim: Entity,
        state: BattleState,
    ): Boolean {
        return false
    }

    open fun isUsing(e: Entity?): Boolean {
        if (e !is Player) {
            return false
        }
        var hits = 0
        for (i in sets.indices) {
            for (item in sets[i]) {
                if (e.equipment.containsAtLeastOneItem(item!!.id)) {
                    hits++
                    break
                }
            }
        }
        return hits == 4
    }
}
