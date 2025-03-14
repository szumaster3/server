package content.region.kandarin.quest.tree

import core.api.*
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.entity.skill.Skills
import core.game.world.map.Location
import core.plugin.Initializable
import org.rs.consts.Items
import org.rs.consts.Quests
import org.rs.consts.Vars

@Initializable
class TreeGnomeVillage :
    Quest(Quests.TREE_GNOME_VILLAGE, 125, 124, 2, Vars.VARP_QUEST_TREE_GNOME_VILLAGE_PROGRESS_111, 0, 1, 9) {
    companion object {
        val mazeVillage = Location(2515, 3159, 0)
        val mazeEntrance = Location(2504, 3192, 0)
    }

    override fun drawJournal(
        player: Player,
        stage: Int,
    ) {
        super.drawJournal(player, stage)
        var line = 12
        player ?: return
        if (stage == 0) {
            line(player, "You can start this quest by speaking to !!King Bolren?? in the", line++, stage > 0)
            line(player, "!!Tree Gnome Village??.", line++, stage > 0)
            line++
        }
        if (stage >= 10) {
            line(player, "King Bolren said that one of their orbs of", line++, stage > 10)
            line(player, "protection has been stolen by Khazard troops.", line++, stage > 10)
            line(player, "Commander Montai, North !!of the maze, can help you get?? it", line++, stage > 10)
            line++
        }
        if (stage >= 20) {
            line(player, "Commander Montai has asked you to gather !!6 normal logs??", line++, stage > 20)
            line(player, "to make more battlements.", line++, stage > 20)
            line++
        }
        if (stage == 25) {
            line(player, "You should ask Commander Montai for further instruction.", line++)
            line++
        }
        if (stage >= 30) {
            line(player, "Commander Montai needs help locating his !!tracker gnomes?? to", line++, stage > 30)
            line(player, "gather the coordinates to strike the Khazard stronghold", line++, stage > 30)
            line(player, "You must speak with:", line++, stage > 30)
            line(player, "Tracker Gnome 1", line++, getAttribute(player, "treegnome:tracker1", false) || stage >= 100)
            line(player, "Tracker Gnome 2", line++, getAttribute(player, "treegnome:tracker2", false) || stage >= 100)
            line(player, "Tracker Gnome 3", line++, getAttribute(player, "treegnome:tracker3", false) || stage >= 100)
            line++
        }
        if (stage >= 31) {
            line(player, "The ballista opened a hole in the !!Khazard stronghold??.", line++, stage > 31)
            line(player, "Commander Montai says the !!Orb of Protection?? should be in there", line++, stage > 31)
            line++
        }
        if (inInventory(player, Items.ORB_OF_PROTECTION_587)) {
            line(player, "You've retrieved the orb! You should return it to !!King Bolren??", line++)
        }
        if (stage >= 40) {
            line(player, "Khazard troops took the other !!Orbs of Protection?? ", line++, stage > 40)
            line(player, "They were headed north of the stronghold.", line++, stage > 40)
            line(player, "A warlord carries the orbs.", line++, stage > 40)
            line++
        }
        if (inInventory(player, Items.ORBS_OF_PROTECTION_588)) {
            line(player, "You've retrieved the orbs!", line++)
            line(player, "You should return them it to !!King Bolren??", line++)
        }
        if (stage >= 99) {
            line(player, "You have returned the orbs!", line++, stage > 99)
            line(player, "Speak with King Bolren for a reward!", line++, stage > 99)
            line++
        }
        if (stage == 100) {
            line(player, "<col=FF0000>QUEST COMPLETE!</col>", line)
        }
    }

    override fun finish(player: Player) {
        super.finish(player)
        player ?: return
        var ln = 10
        player.packetDispatch.sendItemZoomOnInterface(Items.GNOME_AMULET_589, 230, 277, 5)
        drawReward(player, "2 Quest Points", ln++)
        drawReward(player, "11,450 Attack XP", ln++)
        drawReward(player, "Gnome Amulet of Protection", ln)
        rewardXP(player, Skills.ATTACK, 11450.0)
        addItemOrDrop(player, Items.GNOME_AMULET_589)
        removeAttributes(player, "treegnome:tracker1", "treegnome:tracker2", "treegnome:tracker3", "treegnome:xcoord")
    }

    override fun newInstance(`object`: Any?): Quest {
        return this
    }
}
