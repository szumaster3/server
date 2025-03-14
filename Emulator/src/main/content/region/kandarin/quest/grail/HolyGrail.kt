package content.region.kandarin.quest.grail

import core.api.getAttribute
import core.api.rewardXP
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.world.map.Location
import core.plugin.Initializable
import org.rs.consts.Items
import org.rs.consts.Quests

@Initializable
class HolyGrail : Quest(Quests.HOLY_GRAIL, 76, 75, 1, 5, 0, 1, 10) {
    override fun newInstance(`object`: Any?): Quest {
        return this
    }

    companion object {
        val MERLIN_DOOR_ID = 24
        val MERLIN_DOOR_LOCATION_CLOSED = Location.create(2764, 3503, 1)
        val MERLIN_DOOR_LOCATION_OPEN = Location.create(2765, 3503, 1)
        val DOOR_MAGIC_WHISTLE_LOCATION = Location.create(3106, 3361, 2)

        val attribute_failed_titan = "/save:failed_to_kill_titan"

        const val VARP_INDEX = 1049
        const val VARP_HIDE_MERLIN_VALUE = 1
        const val VARP_SHOW_MERLIN_VALUE = 45
    }

    override fun drawJournal(
        player: Player,
        stage: Int,
    ) {
        super.drawJournal(player, stage)
        var ln = 11
        player ?: return
        if (stage == 0) {
            line(player, "I can start this quest by talking to !!King Arthur?? at", ln++)
            line(player, "!!Camelot Castle,?? just !!North West of Catherby??", ln++)
            line(player, "To complete this quest I must be able to !!wield Excalibur??", ln++)
            line(player, "<blue>(with !!Attack level 20)?? and defeat a !!Level 120 Black Knight??", ln++)
            line(player, "!!Titan.??", ln)
        } else if (stage == 10) {
            line(player, "!!King Arthur?? has sent me questing for the !!Holy Grail?? of", ln++, false)
            line(player, "legend. I should start my quest by speaking to !!Merlin?? in !!his??", ln++, false)
            line(player, "!!study next to the Camelot library?? for directions to it.", ln, false)
        } else if (stage == 20) {
            line(player, "I started my Quest for the Holy Grail in Camelot Castle.", ln++, true)
            line(player, "King Arthur sent me to Merlin for advice on locating it.", ln++, true)
            line(player, "!!Merlin?? suggested two things to help find the !!Grail??:", ln++, false)
            line(player, "Speak to !!Galahad?? who lives !!West?? of !!McGrubor's Wood??.", ln++, false)
            line(player, "Talk to someone on a '!!Holy Island??' he can't remember.", ln, false)
        } else if (stage == 30) {
            if (getAttribute(player, attribute_failed_titan, false)) {
                line(player, "I started my Quest for the Holy Grail in Camelot Castle.", ln++, true)
                line(player, "King Arthur sent me to Merlin for advice on locating it.", ln++, true)
                line(player, "I spoke to Galahad in his shack West of McGrubor's Wood.", ln++, true)
                line(player, "Galahad gave me a napkin from the Realm of the Fisher", ln++, true)
                line(player, "King. I used the napkin to find a holy whistle that could", ln++, true)
                line(player, "teleport me to the Realm of the Fisher King.", ln++, true)
                line(player, "I blew the Whistle at the correct location and was", ln++, true)
                line(player, "teleported to the Realm of the Fisher King. The path to the", ln++, true)
                line(player, "Fisher King's castle was blocked by a mighty warrior called", ln++, true)
                line(player, "the Black Knight Titan who seemed invincible!", ln++, true)
                line(player, "I need to find a !!weapon?? that can defeat him somehow.", ln++, false)
            } else {
                line(player, "I started my Quest for the Holy Grail in Camelot Castle.", ln++, true)
                line(player, "King Arthur sent me to Merlin for advice on locating it.", ln++, true)
                line(player, "According to a Crone on !!Entrana?? I need to go to where the", ln++, false)
                line(player, "'!!Six Heads' face?? and blow a !!magic whistle?? there.", ln++, false)
                line(player, "To get the !!Magic Whistle?? I need to carry something from", ln++, false)
                line(player, "the !!Realm of the Fisher King?? to a !!Haunted House??...", ln, false)
            }
        } else if (stage == 40) {
            line(player, "I started my Quest for the Holy Grail in Camelot Castle.", ln++, true)
            line(player, "King Arthur sent me to Merlin for advice on locating it.", ln++, true)
            line(player, "I spoke to Galahad in his shack West of McGrubor's Wood.", ln++, true)
            line(player, "Galahad gave me a napkin from the Realm of the Fisher", ln++, true)
            line(player, "King. I used the napkin to find a holy whistle that could", ln++, true)
            line(player, "teleport me to the Realm of the Fisher King.", ln++, true)
            line(player, "I blew the Whistle at the correct location and was", ln++, true)
            line(player, "teleported to the Realm of the Fisher King. The path to the", ln++, true)
            line(player, "Fisher King's castle was blocked by a mighty warrior called", ln++, true)
            line(player, "the Black Knight Titan who seemed invincible!", ln++, true)
            line(player, "I defeated the Black Knight Titan with Excalibur's", ln++, true)
            line(player, "power.Once past the Titan I entered the Grail Castle.", ln++, true)
            line(player, "The Fisher King couldn't give me the Grail, but legends say", ln++, true)
            line(player, "that the person who restores the land could claim the", ln++, true)
            line(player, "Grail.", ln++, true)
            line(player, "The !!Fisher King?? is very sick. He has asked me to find his", ln++, false)
            line(player, "son !!Sir Percival??, a !!Knight of the Round Table??.", ln++, false)

            if (player.hasItem(Item(Items.MAGIC_GOLD_FEATHER_18, 1))) {
                line(player, "!!King Arthur?? gave me a !!magic golden feather?? to help locate", ln++, false)
                line(player, "!!Sir Percival?? - I should use it to find him!", ln++, false)
            }
        } else if (stage == 50) {
            line(player, "I started my Quest for the Holy Grail in Camelot Castle.", ln++, true)
            line(player, "King Arthur sent me to Merlin for advice on locating it.", ln++, true)
            line(player, "I spoke to Galahad in his shack West of McGrubor's Wood.", ln++, true)
            line(player, "Galahad gave me a napkin from the Realm of the Fisher", ln++, true)
            line(player, "King. I used the napkin to find a holy whistle that could", ln++, true)
            line(player, "teleport me to the Realm of the Fisher King.", ln++, true)
            line(player, "I blew the Whistle at the correct location and was", ln++, true)
            line(player, "teleported to the Realm of the Fisher King. The path to the", ln++, true)
            line(player, "Fisher King's castle was blocked by a mighty warrior called", ln++, true)
            line(player, "the Black Knight Titan who seemed invincible!", ln++, true)
            line(player, "I defeated the Black Knight Titan with Excalibur's", ln++, true)
            line(player, "power.Once past the Titan I entered the Grail Castle.", ln++, true)
            line(player, "The Fisher King couldn't give me the Grail, but legends say", ln++, true)
            line(player, "that the person who restores the land could claim the", ln++, true)
            line(player, "Grail.", ln++, true)
            line(player, "I honoured the Fisher King's request to find his son, and", ln++, true)
            line(player, "used a Magic Golden Feather to track him down. When he", ln++, true)
            line(player, "heard of his father's illness he rushed back to the Grail", ln++, true)
            line(player, "Castle using a Magic Whistle that I gave him.", ln++, true)

            if (!player.hasItem(Item(Items.HOLY_GRAIL_19, 1))) {
                line(player, "I should follow him to the !!Castle?? to get the !!Holy Grail??.", ln, false)
            } else {
                line(player, "Now I have the !!Grail?? with me. I should take it to !!Arthur??.", ln, false)
            }
        } else {
            line(player, "I started my Quest for the Holy Grail in Camelot Castle.", ln++, true)
            line(player, "King Arthur sent me to Merlin for advice on locating it.", ln++, true)
            line(player, "I spoke to Galahad in his shack West of McGrubor's Wood.", ln++, true)
            line(player, "Galahad gave me a napkin from the Realm of the Fisher", ln++, true)
            line(player, "King. I used the napkin to find a holy whistle that could", ln++, true)
            line(player, "teleport me to the Realm of the Fisher King.", ln++, true)
            line(player, "I blew the Whistle at the correct location and was", ln++, true)
            line(player, "teleported to the Realm of the Fisher King. The path to the", ln++, true)
            line(player, "Fisher King's castle was blocked by a mighty warrior called", ln++, true)
            line(player, "the Black Knight Titan who seemed invincible!", ln++, true)
            line(player, "I defeated the Black Knight Titan with Excalibur's", ln++, true)
            line(player, "power.Once past the Titan I entered the Grail Castle.", ln++, true)
            line(player, "The Fisher King couldn't give me the Grail, but legends say", ln++, true)
            line(player, "that the person who restores the land could claim the", ln++, true)
            line(player, "Grail.", ln++, true)
            line(player, "I honoured the Fisher King's request to find his son, and", ln++, true)
            line(player, "used a Magic Golden Feather to track him down. When he", ln++, true)
            line(player, "heard of his father's illness he rushed back to the Grail", ln++, true)
            line(player, "Castle using a Magic Whistle that I gave him.", ln++, true)
            line(player, "I returned to the Grail Castle to find that the land had", ln++, true)
            line(player, "been renewed with Percival as the new King there. Out of", ln++, true)
            line(player, "gratitude he allowed me to take the Grail, which I took to", ln++, true)
            line(player, "King Arthur to prove my prowess as a Knight.", ln++, true)
            ln++
            line(player, "<col=FF0000>QUEST COMPLETE!", ln, false)
        }
    }

    override fun finish(player: Player) {
        super.finish(player)
        player ?: return
        var ln = 10
        player.packetDispatch.sendItemZoomOnInterface(Items.HOLY_GRAIL_19, 230, 277, 5)
        drawReward(player, "2 Quest Points", ln++)
        drawReward(player, "11,000 Prayer XP", ln++)
        drawReward(player, "15,300 Defence XP", ln)

        rewardXP(player, Skills.PRAYER, 11000.0)
        rewardXP(player, Skills.DEFENCE, 15300.0)
    }
}
