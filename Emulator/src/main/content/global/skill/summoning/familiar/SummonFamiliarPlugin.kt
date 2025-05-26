package content.global.skill.summoning.familiar

import content.global.skill.summoning.familiar.npc.IbisNPC
import core.api.quest.isQuestComplete
import core.api.sendMessage
import core.cache.def.impl.ItemDefinition
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.item.Item
import core.game.world.map.zone.ZoneBorders
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs.consts.Quests

@Initializable
class SummonFamiliarPlugin : OptionHandler() {

    @Throws(Throwable::class)
    override fun newInstance(arg: Any?): Plugin<Any>? {
        ItemDefinition.setOptionHandler("summon", this)
        return null
    }

    override fun handle(player: Player, node: Node, option: String): Boolean {
        val item = node as Item

        if (!isQuestComplete(player, Quests.WOLF_WHISTLE) && player.getAttribute<Any?>("in-cutscene", null) == null) {
            sendMessage(player, "You have to complete Wolf Whistle before you can summon a familiar.")
            return true
        }

        player.familiarManager.summon(item, false)

        val familiar = player.familiarManager.familiar
        if (player.familiarManager.hasFamiliar()
            && familiar is IbisNPC
            && (
                ZoneBorders(3011, 3222, 3017, 3229, 0).insideBorder(player) ||
                ZoneBorders(3011, 3220, 3015, 3221, 0).insideBorder(player)
            )
        ) {
            player.achievementDiaryManager.finishTask(player, DiaryType.FALADOR, 2, 9)
        }

        return true
    }
}
