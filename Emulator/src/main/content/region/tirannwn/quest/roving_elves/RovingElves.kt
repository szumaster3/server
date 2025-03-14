package content.region.tirannwn.quest.roving_elves

import content.region.tirannwn.quest.roving_elves.dialogue.ElunedDialogue
import content.region.tirannwn.quest.roving_elves.dialogue.IslwynDialogue
import content.region.tirannwn.quest.roving_elves.handlers.MossGiantNPC
import content.region.tirannwn.quest.roving_elves.handlers.RovingElvesObstacles
import content.region.tirannwn.quest.roving_elves.handlers.RovingElvesPlugin
import core.api.inInventory
import core.api.quest.updateQuestTab
import core.api.rewardXP
import core.api.sendItemZoomOnInterface
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.entity.skill.Skills
import core.plugin.ClassScanner.definePlugin
import core.plugin.Initializable
import org.rs.consts.Components
import org.rs.consts.Items
import org.rs.consts.Quests
import org.rs.consts.Vars

@Initializable
class RovingElves : Quest(Quests.ROVING_ELVES, 105, 104, 1, Vars.VARP_QUEST_ROVING_ELVES_PROGRESS_402, 0, 1, 6) {
    override fun drawJournal(
        player: Player,
        stage: Int,
    ) {
        super.drawJournal(player, stage)
        when (stage) {
            0 ->
                line(
                    player,
                    "I can start this quest by talking to !!Islwyn?? found in !!Isafdar??.<n><n>Minimum requirements:<n>!!I must have completed the Waterfall quest??<n>!!I must be able to kill a level 84 moss giant unarmed??",
                    11,
                )
            10 ->
                line(
                    player,
                    "It appears that when I recovered !!Glarial's ashes?? from<n>her !!tomb?? near the waterfall, I disturbed her peace.<n><n>I talked to !!Islwyn??, an elf that I found in the woods.<n>When I offered to help he said I should talk with !!Eluned??,<n>she will tell me how !!elven consecration?? is done.",
                    11,
                )
            15 -> {
                if (inInventory(player, CONSECRATION_SEED, 1)) {
                    line(
                        player,
                        "<str>It appears that when I recovered Glarial's ashes from<n><str>her tomb near the waterfall, I disturbed her peace.<n><str>I talked to Islwyn, an elf that I found in the woods.<n><str>When I offered to help he said I should talk with Eluned,<n><str>she will tell me how elven consecration is done.<n><n><str>Eluned told me that I must acquire the<str> old consecration<n><str> seed <str>from the <str>guardian spirit<str> in <str>Glarial's old tomb.<n><n><str>Once I have the old seed I will need to return to Eluned, who<n><str>will re-enchant it for me.<n><n>I have the !!consecration seed??. I should return to !!Eluned??<n>and have her enchant it for me.",
                        11,
                    )
                } else {
                    line(
                        player,
                        "<str>It appears that when I recovered Glarial's ashes from<n><str>her tomb near the waterfall, I disturbed her peace.<n><str>I talked to Islwyn, an elf that I found in the woods.<n><str>When I offered to help he said I should talk with Eluned,<n><str>she will tell me how elven consecration is done.<n><n>Eluned told me that I must acquire the !!old consecration??<n><!!seed??from the !!guardian spirit?? in !!Glarial's old tomb??.<n><n>Once I have the old seed I will need to return to Eluned, who<n>will re-enchant it for me.",
                        11,
                    )
                }
                if (inInventory(player, CONSECRATION_SEED_CHARGED, 1)) {
                    line(
                        player,
                        "<str>It appears that when I recovered Glarial's ashes from<n><str>her tomb near the waterfall, I disturbed her peace.<n><str>I talked to Islwyn, an elf that I found in the woods.<n><str>When I offered to help he said I should talk with Eluned,<n><str>she will tell me how elven consecration is done.<n><n><str>Eluned told me that I must acquire the<str> old consecration<n><str> seed <str>from the <str>guardian spirit<str> in <str>Glarial's old tomb.<n><n><str>Once I have the old seed I will need to return to Eluned, who<n><str>will re-enchant it for me.<n><n>I have the !!charged consecration seed??.<n>I need to head to the treasure room under the !!waterfall?? and<n> plant the !!consecration seed?? near the !!chalice?? in order to<n> free Glarial's spirit.",
                        11,
                    )
                }
            }
            20 ->
                line(
                    player,
                    "<str>It appears that when I recovered Glarial's ashes from<n><str>her tomb near the waterfall, I disturbed her peace.<n><str>I talked to Islwyn, an elf that I found in the woods.<n><str>When I offered to help he said I should talk with Eluned,<n><str>she will tell me how elven consecration is done.<n><n><str>Eluned told me that I must acquire the<str> old consecration<n><str> seed <str>from the <str>guardian spirit<str> in <str>Glarial's old tomb.<n><n><str>Once I have the old seed I will need to return to Eluned, who<n><str>will re-enchant it for me.<n><n><str>I have the <str>charged consecration seed<str>.<n><str>I need to head to the treasure room under the <str>waterfall <str>and<n><str> plant the <str>consecration seed<str> near the <str>chalice<str> in order to<n><str> free Glarial's spirit.<n><n>I have freed !!Glarial's spirit?? by planting the consecration seed near<n>the chalice under the waterfall. The seed I planted<n> vanished. I should go speak to !!Eluned?? again.",
                    11,
                )
            100 ->
                line(
                    player,
                    "<str>It appears that when I recovered Glarial's ashes from<n><str>her tomb near the waterfall, I disturbed her peace.<n><str>I talked to Islwyn, an elf that I found in the woods.<n><str>When I offered to help he said I should talk with Eluned,<n><str>she will tell me how elven consecration is done.<n><n><str>Eluned told me that I must acquire the<str> old consecration<n><str> seed <str>from the <str>guardian spirit<str> in <str>Glarial's old tomb.<n><n><str>Once I have the old seed I will need to return to Eluned, who<n><str>will re-enchant it for me.<n><n><str>I have the <str>charged consecration seed<str>.<n><str>I need to head to the treasure room under the <str>waterfall <str>and<n><str> plant the <str>consecration seed<str> near the <str>chalice<str> in order to<n><str> free Glarial's spirit.<n><n><str>I have freed <str>Glarial's spirit <str>by planting the consecration seed near<n><str>the chalice under the waterfall. The seed I planted<n><str> vanished. I should go speak to <str>Eluned<str> again.<n><n><col=FF0000>QUEST COMPLETE!</col>",
                    11,
                )
        }
    }

    override fun start(player: Player) {
        super.start(player)
    }

    override fun finish(player: Player) {
        super.finish(player)
        var ln = 10
        drawReward(player, "1 Quest Point", ln++)
        drawReward(player, "Used elf equipment.", ln++)
        drawReward(player, "10000 Strength XP", ln++)
        drawReward(player, "You have completed Roving Elves!", ln)

        sendItemZoomOnInterface(player, Components.QUEST_COMPLETE_SCROLL_277, 5, CRYSTAL_BOW_FULL, 235)
        rewardXP(player, Skills.STRENGTH, 10000.0)
        updateQuestTab(player)
    }

    override fun newInstance(`object`: Any?): Quest {
        definePlugin(RovingElvesPlugin())
        definePlugin(RovingElvesObstacles())
        definePlugin(MossGiantNPC())
        definePlugin(ElunedDialogue())
        definePlugin(IslwynDialogue())
        return this
    }

    companion object {
        const val CRYSTAL_BOW_FULL = Items.CRYSTAL_BOW_FULL_4214
        const val CRYSTAL_SHIELD_FULL = Items.CRYSTAL_SHIELD_FULL_4225
        const val CONSECRATION_SEED = Items.CONSECRATION_SEED_4205
        const val CONSECRATION_SEED_CHARGED = Items.CONSECRATION_SEED_4206
        const val CRYSTAL_SEED = Items.CRYSTAL_SEED_4207
    }
}
