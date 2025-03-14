package content.region.kandarin.quest.merlin

import content.region.kandarin.quest.merlin.handlers.MerlinUtils
import core.api.*
import core.api.quest.updateQuestTab
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.plugin.Initializable
import org.rs.consts.Components
import org.rs.consts.Items
import org.rs.consts.Quests
import org.rs.consts.Vars

@Initializable
class MerlinCrystal : Quest(Quests.MERLINS_CRYSTAL, 87, 86, 6, Vars.VARP_QUEST_MERLIN_CRYSTAL_PROGRESS_14, 0, 1, 7) {
    override fun drawJournal(
        player: Player,
        stage: Int,
    ) {
        super.drawJournal(player, stage)
        var line = 12

        when (stage) {
            0 -> {
                line(player, "I can start this quest by speaking to !!King Arthur?? at", line++)
                line(player, "!!Camelot Castle??, just !!North West of Catherby??", line++)
                line(player, "I must be able to defeat a !!level 37 enemy??.", line)
            }

            10 -> {
                line(player, "I spoke to King Arthur and he said I would be worthy of", line++, true)
                line(player, "becoming a Knight of the Round Table if I could free Merlin", line++, true)
                line(player, "from a giant crystal that he has been trapped in.", line++, true)
                line(player, "I should ask the !!other Knights?? if they have any !!advice?? for", line++, false)
                line(player, "me on how I should go about doing this.", line++, false)
            }

            20 -> {
                line(player, "I spoke to King Arthur and he said I would be worthy of", line++, true)
                line(player, "becoming a Knight of the Round Table if I could free Merlin", line++, true)
                line(player, "from a giant crystal that he has been trapped in.", line++, true)
                line(player, "!!Gawain?? thinks this was the work of !!Morgan Le Faye?? who", line++, false)
                line(player, "lives in an !!impenetrable fortress?? south of here full of", line++, false)
                line(player, "!!renegade knights?? led by the evil !!Sir Mordred??", line++, false)
            }

            30 -> {
                line(player, "I spoke to King Arthur and he said I would be worthy of", line++, true)
                line(player, "becoming a Knight of the Round Table if I could free Merlin", line++, true)
                line(player, "from a giant crystal that he has been trapped in.", line++, true)
                line(player, "Gawain told me it was the work of Morgan Le Faye.", line++, true)
                line(player, "I told Lancelot of Gawain's suspicions, and he told me that", line++, true)
                line(player, "Mordred's Fortress is not completely impenetratable.", line++, true)
                line(player, "There might be a way to enter with a !!delivery by sea??...", line++, false)
            }

            40 -> {
                line(player, "I spoke to King Arthur and he said I would be worthy of", line++, true)
                line(player, "becoming a Knight of the Round Table if I could free Merlin", line++, true)
                line(player, "from a giant crystal that he has been trapped in.", line++, true)
                line(player, "Gawain told me it was the work of Morgan Le Faye.", line++, true)
                line(player, "I told Lancelot of Gawain's suspicions, and he told me that", line++, true)
                line(player, "Mordred's Fortress is not completely impenetratable.", line++, true)
                line(player, "I stowed away in a shipment of candles and reached the", line++, true)
                line(player, "Stronghold of Sir Mordred, and bested him in combat.", line++, true)
                line(player, "In return for sparing her son, Morgan Le Faye told me how I", line++, true)
                line(player, "could break the spell that had imprisoned Merlin.", line++, true)
                line(player, "I need to summon the spirit !!Thrantax?? at a !!magic symbol?? as", line++, false)
                line(player, "close to the Crystal as I can possibly find.", line++, false)
                line(player, "To bind !!Thrantax?? to my will I will need the following:", line++, false)

                if (player.getAttribute(MerlinUtils.ATTR_STATE_ALTAR_FINISH, false) == true) {
                    line(player, "I have the magic words from the Chaos Altar memorised", line++, true)
                } else {
                    line(player, "Some !!magic words?? from a !!Chaos Altar??", line++, false)
                }

                if (inInventory(player, Items.LIT_BLACK_CANDLE_32, 1)) {
                    line(player, "I have a lit black candle with me", line++, true)
                } else {
                    line(player, "A !!lit Black Candle??", line++, false)
                }

                if (inInventory(player, Items.EXCALIBUR_35, 1)) {
                    line(player, "I have the Holy Sword Excalibur with me", line++, true)
                } else {
                    line(player, "The !!Holy Sword Excalibur?? from the !!Lady of the Lake??", line++, false)
                }

                if (inInventory(player, Items.BAT_BONES_530, 1)) {
                    line(player, "I have some bat bones with me", line++, true)
                } else {
                    line(player, "Some !!bat bones??", line++, false)
                }
            }

            50 -> {
                line(player, "I spoke to King Arthur and he said I would be worthy of", line++, true)
                line(player, "becoming a Knight of the Round Table if I could free Merlin", line++, true)
                line(player, "from a giant crystal that he has been trapped in.", line++, true)
                line(player, "Gawain told me it was the work of Morgan Le Faye.", line++, true)
                line(player, "I told Lancelot of Gawain's suspicions, and he told me that", line++, true)
                line(player, "Mordred's Fortress is not completely impenetratable.", line++, true)
                line(player, "I stowed away in a shipment of candles and reached the", line++, true)
                line(player, "Stronghold of Sir Mordred, and bested him in combat.", line++, true)
                line(player, "In return for sparing her son, Morgan Le Faye told me how I", line++, true)
                line(player, "could break the spell that had imprisoned Merlin.", line++, true)
                line(player, "I need to summon the spirit Thrantax at a magic symbol as", line++, true)
                line(player, "close to the Crystal as I can possibly find.", line++, true)
                line(player, "I summoned the Spirit Thrantax and forced him to aid me in", line++, true)
                line(player, "breaking Morgan Le Fayes curse upon Merlin. He used his", line++, true)
                line(player, "magic to weaken the crystal so that a blow from Excalibur", line++, true)
                line(player, "would be able to shatter it for good.", line++, true)
                line(player, "I should free !!Merlin?? by using !!Excalibur?? on the !!crystal??", line++, false)
            }

            60 -> {
                line(player, "I spoke to King Arthur and he said I would be worthy of", line++, true)
                line(player, "becoming a Knight of the Round Table if I could free Merlin", line++, true)
                line(player, "from a giant crystal that he has been trapped in.", line++, true)
                line(player, "Gawain told me it was the work of Morgan Le Faye.", line++, true)
                line(player, "I told Lancelot of Gawain's suspicions, and he told me that", line++, true)
                line(player, "Mordred's Fortress is not completely impenetratable.", line++, true)
                line(player, "I stowed away in a shipment of candles and reached the", line++, true)
                line(player, "Stronghold of Sir Mordred, and bested him in combat.", line++, true)
                line(player, "In return for sparing her son, Morgan Le Faye told me how I", line++, true)
                line(player, "could break the spell that had imprisoned Merlin.", line++, true)
                line(player, "I need to summon the spirit Thrantax<blue> at a magic symbol as", line++, true)
                line(player, "<blue>close to the Crystal as I can possibly find.", line++, true)
                line(player, "I summoned the Spirit Thrantax and forced him to aid me in", line++, true)
                line(player, "breaking Morgan Le Fayes curse upon Merlin. He used his", line++, true)
                line(player, "magic to weaken the crystal so that a blow from Excalibur", line++, true)
                line(player, "would be able to shatter it for good.", line++, true)
                line(player, "I freed Merlin by shattering the Crystal that trapped him", line++, true)
                line(player, "Now that !!Merlin?? is freed I should speak to !!King Arthur?? to", line++, false)
                line(player, "claim my !!reward?? and become a !!Knight of the Round Table??", line++, false)
            }

            100 -> {
                line(player, "I spoke to King Arthur and he said I would be worthy of", line++, true)
                line(player, "becoming a Knight of the Round Table if I could free Merlin", line++, true)
                line(player, "from a giant crystal that he has been trapped in.", line++, true)
                line(player, "Gawain told me it was the work of Morgan Le Faye.", line++, true)
                line(player, "I told Lancelot of Gawain's suspicions, and he told me that", line++, true)
                line(player, "Mordred's Fortress is not completely impenetratable.", line++, true)
                line(player, "I stowed away in a shipment of candles and reached the", line++, true)
                line(player, "Stronghold of Sir Mordred, and bested him in combat.", line++, true)
                line(player, "In return for sparing her son, Morgan Le Faye told me how I", line++, true)
                line(player, "could break the spell that had imprisoned Merlin.", line++, true)
                line(player, "I need to summon the spirit Thrantax at a magic symbol as", line++, true)
                line(player, "close to the Crystal as I can possibly find.", line++, true)
                line(player, "I summoned the Spirit Thrantax and forced him to aid me in", line++, true)
                line(player, "breaking Morgan Le Fayes curse upon Merlin. He used his", line++, true)
                line(player, "magic to weaken the crystal so that a blow from Excalibur", line++, true)
                line(player, "would be able to shatter it for good.", line++, true)
                line(player, "I freed Merlin by shattering the Crystal that trapped him", line++, true)
                line(player, "and when I told King Arthur how I had singlehandedly freed", line++, true)
                line(player, "Merlin from his prison when his other Knights had failed he", line++, true)
                line(player, "immediately made me a Knight of the Round Table", line++, true)
                line++
                line(player, "<col=FF0000>QUEST COMPLETE!", line, false)
            }
        }
    }

    override fun newInstance(`object`: Any?): Quest {
        return this
    }

    override fun finish(player: Player) {
        super.finish(player)
        var line = 10
        sendString(player, "6 Quest Points", Components.QUEST_COMPLETE_SCROLL_277, line++)
        sendString(player, "Excalibur", Components.QUEST_COMPLETE_SCROLL_277, line)
        sendItemZoomOnInterface(player, Components.QUEST_COMPLETE_SCROLL_277, 5, Items.EXCALIBUR_35, 235)
        removeAttributes(
            player,
            MerlinUtils.ATTR_STATE_ALTAR_FINISH,
            MerlinUtils.ATTR_STATE_CLAIM_EXCALIBUR,
            MerlinUtils.ATTR_STATE_TALK_LADY,
            MerlinUtils.ATTR_STATE_TALK_BEGGAR,
            MerlinUtils.ATTR_STATE_TALK_CANDLE,
        )
        setVarbit(player, Vars.VARBIT_SCENERY_MUSEUM_DISPLAY_9_3655, 1, true)
        updateQuestTab(player)
    }
}
