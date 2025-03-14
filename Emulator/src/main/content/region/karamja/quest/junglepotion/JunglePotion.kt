package content.region.karamja.quest.junglepotion

import content.global.skill.herblore.herbs.Herbs
import content.region.karamja.quest.junglepotion.dialogue.JogreCavernDialogue
import content.region.karamja.quest.junglepotion.dialogue.TrufitusDialogue
import content.region.karamja.quest.junglepotion.handlers.JungleObjective
import content.region.karamja.quest.junglepotion.handlers.JunglePotionPlugin
import core.api.sendString
import core.api.setVarbit
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.entity.skill.Skills
import core.plugin.ClassScanner.definePlugin
import core.plugin.Initializable
import org.rs.consts.Quests
import org.rs.consts.Vars

@Initializable
class JunglePotion : Quest(Quests.JUNGLE_POTION, 81, 80, 1, Vars.VARP_QUEST_JUNGLE_POTION_PROGRESS_175, 0, 1, 12) {
    override fun drawJournal(
        player: Player,
        stage: Int,
    ) {
        super.drawJournal(player, stage)
        when (stage) {
            0 ->
                line(
                    player,
                    "<blue>I can start this quest by speaking to <red>Trufitus Shakaya<n><blue>who lives in the main hut in <red>Tai Bwo Wannai<n><blue>village on the island of <red>Karamja.",
                    11,
                )

            10, 20, 30, 40, 50 -> {
                val objective = JungleObjective.forStage(stage)
                if (player.inventory.containsItem(objective.herb.product)) {
                    line(
                        player,
                        "<str>I spoke to Trufitus, he needs to commune with the<n><str>gods, he's asked me to help him by collecting herbs.<n><n><str>I picked some fresh " +
                            objective.getName() +
                            " for Trufitus.<n><n><blue>I need to give the <red>" +
                            objective.getName() +
                            " <blue> to <red>Trufitus.",
                        11,
                    )
                    return
                }
                line(
                    player,
                    "<str>I spoke to Trufitus, he needs to commune with the<n><str>gods, he's asked me to help him by collecting herbs.<n><n><blue>I need to pick some fresh <red>" +
                        objective.getName() +
                        " <blue>for <red>Trufitus.",
                    11,
                )
            }

            60 ->
                line(
                    player,
                    "<str>I spoke to Trufitus, he needs to commune with the<n><str>gods, he's asked me to help him by collecting herbs.<n><n><str>I have given Trufitus Snakeweed, Ardrigal,<n><str>Sito Foil, Volencia moss and Rogues purse.<n><n><str>Trufitus needs to commune with the gods.<n><blue>I should speak to <red>Trufitus.",
                    11,
                )

            100 ->
                line(
                    player,
                    "<str>Trufitus Shakaya of the Tai Bwo Wannai village needed<n><str>some jungle herbs in order to make a potion which would<n><str>help him commune with the gods. I collected five lots<n><str>of jungle herbs for him and he was able to<n><str>commune with the gods.<n><n><str>As a reward he showed me some herblore techniques.<n><n><col=FF0000>QUEST COMPLETE!</col>",
                    11,
                )
        }
    }

    override fun finish(player: Player) {
        super.finish(player)
        sendString(player, "1 Quest Point", 277, 8 + 2)
        sendString(player, "775 Herblore XP", 277, 9 + 2)
        player.packetDispatch.sendItemZoomOnInterface(Herbs.VOLENCIA_MOSS.product.id, 235, 277, 3 + 2)
        player.getSkills().addExperience(Skills.HERBLORE, 775.0)
        player.getQuestRepository().syncronizeTab(player)
        setVarbit(player, 897, 2)
        setVarbit(player, 898, 2)
        setVarbit(player, 899, 2)
        setVarbit(player, 900, 2)
        setVarbit(player, 896, 2, true)
    }

    override fun newInstance(`object`: Any?): Quest {
        definePlugin(TrufitusDialogue())
        definePlugin(JogreCavernDialogue())
        definePlugin(JunglePotionPlugin())
        return this
    }

    override fun getConfig(
        player: Player,
        stage: Int,
    ): IntArray {
        if (stage == 0) return intArrayOf(175, 0)
        if (stage == 100) return intArrayOf(175, 15)
        if (stage > 0) return intArrayOf(175, 1)
        return intArrayOf(175, 15)
    }
}
