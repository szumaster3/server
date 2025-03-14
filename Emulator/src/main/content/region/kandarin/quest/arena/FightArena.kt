package content.region.kandarin.quest.arena

import core.api.addItemOrDrop
import core.api.rewardXP
import core.api.sendItemZoomOnInterface
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.entity.skill.Skills
import core.plugin.Initializable
import org.rs.consts.Components
import org.rs.consts.Items
import org.rs.consts.Quests
import org.rs.consts.Vars

@Initializable
class FightArena : Quest(Quests.FIGHT_ARENA, 61, 60, 2, Vars.VARP_QUEST_FIGHT_ARENA_PROGRESS_17, 0, 1, 14) {
    override fun drawJournal(
        player: Player,
        stage: Int,
    ) {
        super.drawJournal(player, stage)
        var line = 11
        if (stage == 0) {
            line(player, "I can start this quest by speaking to !!Lady Servil?? just", line++)
            line(player, "!!North-West?? of the !!Khazard Port??.", line++)
            line += 1
            line(player, "I must be able to defeat a !!level 137?? enemy.", line++)
            limitScrolling(player, line, true)
        }
        if (stage > 1) {
            line += 1
            line(player, "I encountered a distraught Lady Servil, who said that her son and", line++, stage >= 10)
            line(player, "husband had been kidnapped by the !!evil General Khazard??,", line++, stage >= 10)
            line(player, "and were being forced to fight in his !!fight arena??.", line++, stage >= 10)
            line += 1
            line(
                player,
                "I headed to the arena to try and find !!Lady Servil's son?? and !!husband??.",
                line++,
                stage >= 10,
            )
            line++
        }
        if (stage >= 10) {
            line(player, "I found some !!Khazard armour?? in the armoury, on the north-east", line++, stage >= 10)
            line(player, "edge of town.", line++, stage >= 10)
            line++
            line(player, "I used it to disguise myself as a guard so I could look around.", line++, stage >= 20)
            line++
        }
        if (stage >= 35) {
            line(
                player,
                "I found Lady Servil's son, !!Jeremy Servil??, in one of the !!prison cells??.",
                line++,
                stage > 40,
            )
            line(player, "He told me that a bald, fat, !!lazy guard??", line++, stage > 40)
            line(player, "with a goatee was in charge of the keys.", line++, stage > 40)
            line++
        }
        if (stage >= 50) {
            line(player, "I found the guard Jeremy mentioned.", line++, stage > 55)
            line(player, "He said that he'd like a drink, but too much !!Khali brew??", line++, stage > 55)
            line(player, "would make him fall asleep.", line++, stage > 55)
            line++
        }
        if (stage >= 60) {
            line(player, "I plied the !!lazy guard?? with some !!Khali brew?? and he passed out.", line++, stage > 67)
            line(player, "I was able to get !!his keys??.", line++, stage > 67)
            line++
        }
        if (stage >= 68) {
            line(player, "I found and !!freed Jeremy??, who told me that !!his father??", line++, stage > 69)
            line(player, "had been taken to fight in the !!fight arena??.", line++, stage > 69)
            line(player, "We went there to save him.", line++, stage > 69)
            line++
        }
        if (stage >= 71) {
            line(player, "I had to fight a !!large ogre?? to stop it killing !!Sir Servil??.", line++, stage > 72)
            line(player, "When I'd defeated it, !!General Khazard?? had locked me up.", line++, stage > 72)
            line++
        }
        if (stage >= 88) {
            line(player, "I was led to the !!fight arena?? and forced to", line++, stage > 88)
            line(player, "fight a !!Colossal Scorpion??, followed by", line++, stage > 90)
            line(player, "!!Khazard's monstrous?? pet dog called !!Bouncer??.", line++, stage > 90)
            line++
        }
        if (stage >= 91) {
            line(player, "!!General Khazard?? released the !!Servils??,", line++, stage >= 97)
            line(player, "but he was so angry that I'd killed", line++, stage >= 97)
            line(player, "!!Bouncer?? that he came to fight me himself.", line++, stage >= 97)
            line++
        }
        if (stage == 100) {
            line(player, "I escaped from the arena and returned to Lady Servil", line++, true)
            line(player, "She thanked me profusely and rewarded me for my help.", line++, true)
            line++
            line(player, "<col=FF0000>QUEST COMPLETE!", line, false)
            limitScrolling(player, line, false)
        }
    }

    override fun finish(player: Player) {
        super.finish(player)
        var ln = 10
        sendItemZoomOnInterface(player, Components.QUEST_COMPLETE_SCROLL_277, 5, Items.COINS_6964, 230)
        drawReward(player, "2 Quest Points", ln++)
        drawReward(player, "1000 gold coins", ln++)
        drawReward(player, "12,175 Attack XP", ln++)
        drawReward(player, "2,175 Thieving XP", ln)

        rewardXP(player, Skills.ATTACK, 12175.0)
        rewardXP(player, Skills.THIEVING, 2175.0)
        addItemOrDrop(player, Items.COINS_995, 1000)
    }

    override fun newInstance(`object`: Any?): Quest {
        return this
    }
}
