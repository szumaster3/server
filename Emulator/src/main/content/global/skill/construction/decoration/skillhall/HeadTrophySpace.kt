package content.global.skill.construction.decoration.skillhall

import content.global.skill.construction.decoration.skillhall.head.*
import core.api.openDialogue
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import org.rs.consts.Scenery

class HeadTrophySpace : InteractionListener {
    override fun defineListeners() {
        on(Scenery.CRAWLING_HAND_13481, IntType.SCENERY, "talk-to") { player, node ->
            openDialogue(player, CrawlingHeadDialogue(), node)
            return@on true
        }

        on(Scenery.COCKATRICE_HEAD_13482, IntType.SCENERY, "talk-to") { player, node ->
            openDialogue(player, CockatriceHeadDialogue(), node)
            return@on true
        }

        on(Scenery.BASILISK_HEAD_13483, IntType.SCENERY, "talk-to") { player, node ->
            openDialogue(player, BasiliskHeadDialogue(), node)
            return@on true
        }

        on(Scenery.KURASK_HEAD_13484, IntType.SCENERY, "talk-to") { player, node ->
            openDialogue(player, KuraskHeadDialogue(), node)
            return@on true
        }

        on(Scenery.ABYSSAL_DEMON_HEAD_13485, IntType.SCENERY, "talk-to") { player, node ->
            openDialogue(player, AbyssalHeadDialogue(), node)
            return@on true
        }

        on(Scenery.KBD_HEADS_13486, IntType.SCENERY, "talk-to") { player, node ->
            openDialogue(player, KBDHeadDialogue(), node)
            return@on true
        }

        on(Scenery.KQ_HEAD_13487, IntType.SCENERY, "talk-to") { player, node ->
            openDialogue(player, KalphiteHeadDialogue(), node)
            return@on true
        }
    }
}
