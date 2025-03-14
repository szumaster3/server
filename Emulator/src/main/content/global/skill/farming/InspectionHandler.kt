package content.global.skill.farming

import core.api.sendMessage
import core.cache.def.impl.SceneryDefinition
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.scenery.Scenery
import core.plugin.Initializable
import core.plugin.Plugin
import core.tools.prependArticle

@Initializable
class InspectionHandler : OptionHandler() {
    override fun newInstance(arg: Any?): Plugin<Any> {
        SceneryDefinition.setOptionHandler("inspect", this)
        return this
    }

    override fun handle(
        player: Player?,
        node: Node?,
        option: String?,
    ): Boolean {
        if (player == null || node == null) return false

        val patch =
            FarmingPatch.forObject(node.asScenery()) ?: run {
                sendMessage(player, "This is an improperly handled inspect option. Please report this.")
                return true
            }

        when (patch) {
            FarmingPatch.ENRICHED_SEED -> handleEnrichedSeedInspection(player, node)
            else -> handleGeneralInspection(player, patch)
        }

        return true
    }

    private fun handleEnrichedSeedInspection(
        player: Player,
        node: Node,
    ) {
        val scenery = node.asScenery()
        when (scenery) {
            Scenery(41016, node.location) ->
                sendMessage(
                    player,
                    "This is a specially prepared herb patch. You think it's probably best not to use it in case you get into trouble.",
                )

            Scenery(41017, node.location) -> sendMessage(player, "The seed has only just been planted.")
            Scenery(41018, node.location) -> sendMessage(player, "The herb is fully grown and ready to harvest.")
        }
    }

    private fun handleGeneralInspection(
        player: Player,
        patch: FarmingPatch,
    ) {
        val p = patch.getPatchFor(player)
        val patchName = p.patch.type.displayName()

        val statusPatchType =
            if (patch == FarmingPatch.TROLL_STRONGHOLD_HERB) {
                "This is a very special herb patch."
            } else {
                "This is ${prependArticle(patchName)}."
            }

        val statusCompost =
            if (p.compost == CompostType.NONE) {
                "The soil has not been treated."
            } else {
                "The soil has been treated with ${p.compost.name.lowercase()}."
            }

        val statusStage =
            when {
                p.plantable == Plantable.SCARECROW -> ""
                p.isWeedy() -> "The patch needs weeding."
                p.isEmptyAndWeeded() -> "The patch is empty and weeded."
                p.isDiseased && !p.isDead -> "The patch is diseased and needs attending to before it dies."
                p.isDead -> "The patch has become infected by disease and has died."
                p.isGrown() -> "The patch is fully grown."
                else -> "The patch has something growing in it."
            }

        val statusGardener =
            when {
                patch == FarmingPatch.TROLL_STRONGHOLD_HERB -> "My Arm will look after this patch for you."
                p.protectionPaid -> "A nearby gardener is looking after this patch for you."
                else -> ""
            }

        sendMessage(player, "$statusPatchType $statusCompost $statusStage".trim())
        if (statusGardener.isNotEmpty()) sendMessage(player, statusGardener)
    }
}
