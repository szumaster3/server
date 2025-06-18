package content.region.karamja.quest.mm.plugin

import content.region.karamja.quest.mm.dialogue.CrateMMDialogue
import core.api.animate
import core.api.openDialogue
import core.api.sendMessage
import core.api.submitIndividualPulse
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.system.task.Pulse
import org.rs.consts.Animations
import org.rs.consts.Scenery

class CratePlugin : InteractionListener {
    private val monkeyAmuletMouldCrate = Scenery.CRATE_4724
    private val threadCrate = Scenery.CRATE_4718
    private val monkeyTalkingDentures = Scenery.CRATE_4715
    private val bananaCrate = Scenery.CRATE_4723
    private val monkeyMadnessEntranceDown = Scenery.CRATE_4714
    private val tinderboxCrate = Scenery.CRATE_4719
    private val slimyGnomeEyesCrate = Scenery.CRATE_4716
    private val hammersCrate = Scenery.CRATE_4726

    override fun defineListeners() {
        on(monkeyAmuletMouldCrate, IntType.SCENERY, "search") { player, _ ->
            animate(player, Animations.HUMAN_OPEN_CHEST_536)
            sendMessage(player, "You search the crate.")
            openDialogue(player, CrateMMDialogue(0))
            return@on true
        }

        on(threadCrate, IntType.SCENERY, "search") { player, _ ->
            sendMessage(player, "You search the crate.")
            submitIndividualPulse(
                player,
                object : Pulse() {
                    var counter = 0

                    override fun pulse(): Boolean {
                        when (counter++) {
                            3 -> openDialogue(player, CrateMMDialogue(1))
                        }
                        return false
                    }
                },
            )
            return@on true
        }

        on(monkeyTalkingDentures, IntType.SCENERY, "search") { player, _ ->
            sendMessage(player, "You search the crate.")
            submitIndividualPulse(
                player,
                object : Pulse() {
                    var counter = 0

                    override fun pulse(): Boolean {
                        when (counter++) {
                            3 -> openDialogue(player, CrateMMDialogue(2))
                        }
                        return false
                    }
                },
            )
            return@on true
        }

        on(bananaCrate, IntType.SCENERY, "search") { player, _ ->
            sendMessage(player, "You search the crate.")
            submitIndividualPulse(
                player,
                object : Pulse() {
                    var counter = 0

                    override fun pulse(): Boolean {
                        when (counter++) {
                            3 -> openDialogue(player, CrateMMDialogue(3))
                        }
                        return false
                    }
                },
            )
            return@on true
        }

        on(monkeyMadnessEntranceDown, IntType.SCENERY, "search") { player, _ ->
            sendMessage(player, "You search the crate.")
            openDialogue(player, CrateMMDialogue(4))
            return@on true
        }

        on(tinderboxCrate, IntType.SCENERY, "search") { player, _ ->
            sendMessage(player, "You search the crate.")
            submitIndividualPulse(
                player,
                object : Pulse() {
                    var counter = 0

                    override fun pulse(): Boolean {
                        when (counter++) {
                            3 -> openDialogue(player, CrateMMDialogue(5))
                        }
                        return false
                    }
                },
            )
            return@on true
        }

        on(slimyGnomeEyesCrate, IntType.SCENERY, "search") { player, _ ->
            sendMessage(player, "You search the crate.")
            submitIndividualPulse(
                player,
                object : Pulse() {
                    var counter = 0

                    override fun pulse(): Boolean {
                        when (counter++) {
                            3 -> openDialogue(player, CrateMMDialogue(6))
                        }
                        return false
                    }
                },
            )
            return@on true
        }

        on(hammersCrate, IntType.SCENERY, "search") { player, _ ->
            sendMessage(player, "You search the crate.")
            submitIndividualPulse(
                player,
                object : Pulse() {
                    var counter = 0

                    override fun pulse(): Boolean {
                        when (counter++) {
                            3 -> openDialogue(player, CrateMMDialogue(7))
                        }
                        return false
                    }
                },
            )
            return@on true
        }
    }
}
