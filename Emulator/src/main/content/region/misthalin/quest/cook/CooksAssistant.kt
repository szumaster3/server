package content.region.misthalin.quest.cook

import core.api.*
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.entity.skill.Skills
import core.plugin.Initializable
import org.rs.consts.Components
import org.rs.consts.Quests
import org.rs.consts.Vars

@Initializable
class CooksAssistant : Quest(Quests.COOKS_ASSISTANT, 15, 14, 1, Vars.VARP_QUEST_COOKS_ASSISTANT_PROGRESS_29, 0, 1, 2) {
    val MILK = 1927
    val FLOUR = 1933
    val EGG = 1944

    override fun drawJournal(
        player: Player,
        stage: Int,
    ) {
        super.drawJournal(player, stage)
        var line = 12
        var stage = player?.questRepository?.getStage(Quests.COOKS_ASSISTANT)!!
        if (stage < 10) {
            line(player, "I can start this quest by speaking to the !!Cook?? in the", line++)
            line(player, "!!Kitchen?? on the ground floor of !!Lumbridge Castle.??", line)
        } else if (stage in 10..99) {
            line(player, "It's the !!Duke of Lumbridge's?? birthday and I have to help", line++)
            line(player, "his !!Cook?? make him a !!birthday cake.?? To do this I need to", line++)
            line(player, "bring him the following ingredients:", line++)
            if (player.getAttribute(
                    "cooks_assistant:milk_submitted",
                    false,
                ) ||
                player.getAttribute("cooks_assistant:all_submitted", false)
            ) {
                line(player, "---I have given the cook a bucket of milk./--", line++)
            } else if (inInventory(player, MILK, 1)) {
                line(player, "I have found a !!bucket of milk?? to give to the cook.", line++)
            } else {
                line(player, "I need to find a !!bucket of milk.?? There's a cattle field east", line++)
                line(player, "of Lumbridge, I should make sure I take an empty bucket", line++)
                line(player, "with me.", line++)
            }
            if (player.getAttribute(
                    "cooks_assistant:flour_submitted",
                    false,
                ) ||
                player.getAttribute("cooks_assistant:all_submitted", false)
            ) {
                line(player, "---I have given the cook a pot of flour./--", line++)
            } else if (inInventory(player, FLOUR, 1)) {
                line(player, "I have found a !!pot of flour?? to give to the cook.", line++)
            } else {
                line(player, "I need to find a !!pot of flour.?? There's a mill found north-", line++)
                line(player, "west of Lumbridge, I should take an empty pot with me.", line++)
            }
            if (player.getAttribute(
                    "cooks_assistant:egg_submitted",
                    false,
                ) ||
                player.getAttribute("cooks_assistant:all_submitted", false)
            ) {
                line(player, "---I have given the cook an egg./--", line++)
            } else if (inInventory(player, EGG, 1)) {
                line(player, "I have found an !!egg?? to give to the cook.", line++)
            } else {
                line(player, "I need to find an !!egg.?? The cook normally gets his eggs from", line++)
                line(player, "the Groats' farm, found just to the west of the cattle", line++)
                line(player, "field.", line)
            }
            if (player.getAttribute(
                    "cooks_assistant:all_submitted",
                    false,
                ) ||
                (
                    player.getAttribute(
                        "cooks_assistant:milk_submitted",
                        false,
                    ) &&
                        player.getAttribute(
                            "cooks_assistant:flour_submitted",
                            false,
                        ) &&
                        player.getAttribute("cooks_assistant:egg_submitted", false)
                )
            ) {
                line(player, "I should return to the !!Cook.??", line)
            }
        } else if (stage >= 100) {
            line(player, "---It was the Duke of Lumbridge's birthday,  but his cook had/--", line++)
            line(player, "---forgotten to buy the ingredients he needed to make him a/--", line++)
            line(player, "---cake. I brought the cook an egg, some flour and some milk/--", line++)
            line(player, "---and then cook made a delicious looking cake with them./--", line++)
            line += 1
            line(player, "---As a reward he now lets me use his high quality range/--", line++)
            line(player, "---which lets me burn things less whenever I wish to cook/--", line++)
            line(player, "---there./--", line++)
            line += 1
            line(player, "<col=FF0000>QUEST COMPLETE!</col>", line)
        }
    }

    override fun finish(player: Player) {
        var ln = 10
        super.finish(player)
        sendString(
            player,
            "You have completed the ${Quests.COOKS_ASSISTANT} Quest!",
            Components.QUEST_COMPLETE_SCROLL_277,
            4,
        )
        sendItemZoomOnInterface(player, Components.QUEST_COMPLETE_SCROLL_277, 5, 1891, 240)
        drawReward(player, "1 Quest Point", ln++)
        drawReward(player, "300 Cooking XP", ln)
        rewardXP(player, Skills.COOKING, 300.0)
        removeAttributes(
            player,
            "cooks_assistant:milk_submitted",
            "cooks_assistant:flour_submitted",
            "cooks_assistant:egg_submitted",
            "cooks_assistant:all_submitted",
            "cooks_assistant:submitted_some_items",
        )
    }

    override fun newInstance(`object`: Any?): Quest {
        return this
    }
}
