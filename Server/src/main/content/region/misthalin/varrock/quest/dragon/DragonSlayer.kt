package content.region.misthalin.varrock.quest.dragon

import content.global.skill.agility.AgilityHandler
import content.region.misthalin.lumbridge.dialogue.DukeHoracioDialogue
import content.region.misthalin.varrock.quest.dragon.cutscene.DragonSlayerCutscene
import content.region.misthalin.varrock.quest.dragon.dialogue.CabinBoyDialogue
import content.region.misthalin.varrock.quest.dragon.dialogue.GuildmasterDialogue
import content.region.misthalin.varrock.quest.dragon.dialogue.OziachDialogue
import content.region.misthalin.varrock.quest.dragon.dialogue.WormbrainDialogue
import content.region.misthalin.varrock.quest.dragon.npc.*
import content.region.misthalin.varrock.quest.dragon.plugin.DragonSlayerPlugin
import content.region.misthalin.varrock.quest.dragon.plugin.MagicDoorPlugin
import core.api.*
import core.game.event.EventHook
import core.game.event.PickUpEvent
import core.game.event.SpellCastEvent
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import core.game.world.map.Location
import core.game.world.map.RegionManager.getObject
import core.plugin.ClassScanner.definePlugins
import core.plugin.Initializable
import shared.consts.*

/**
 * Represents the dragon slayer quest.
 */
@Initializable
class DragonSlayer : Quest(Quests.DRAGON_SLAYER, 18, 17, 2, Vars.VARP_QUEST_DRAGON_SLAYER_PROGRESS_176, 0, 1, 10), LoginListener {

    override fun drawJournal(player: Player, stage: Int) {
        super.drawJournal(player, stage)
        var line = 11

        if (stage == 0) {
            line(player, "I can start this quest by speaking to the !!Guildmaster?? in", line++)
            line(player, "the !!Champions' Guild??, south-west of !!Varrock??.", line++)
            line(player, "I will need to be able to defeat a !!level 83 dragon??.", line++)

            if (player.questRepository.points < 32) {
                line(player, "To enter the Champions' Guild I need !!32 Quest Points??.", line++)
            } else {
                line(player, "To enter the Champions' Guild I need 32 Quest Points.", line++, true)
            }
            line++
        }

        if (stage == 10) {
            line(player, "The Guildmaster of the Champions' Guild said I could earn", line++, true)
            line(player, "the right to wear rune armour if I went on a quest for", line++, true)
            line(player, "!!Oziach??, who makes the armour.", line++, true)
            line(player, "I should speak to !!Oziach??, who lives by the cliffs to the", line++)
            line(player, "west of !!Edgeville??.", line++)
            line++
        }

        if (stage == 15) {
            line(player, "The Guildmaster of the Champions' Guild said I could earn", line++, true)
            line(player, "the right to wear rune armour if I went on a quest for", line++, true)
            line(player, "Oziach, who makes the armour.", line++, true)
            line(player, "I spoke to Oziach in !!Edgeville??. He told me to slay the", line++, true)
            line(player, "!!dragon?? of !!Crandor island??.", line++, true)
            line(player, "I should return to the !!Champions' Guild Guildmaster?? for", line++)
            line(player, "more detailed instructions.", line++)
            line++
        }

        if (stage == 20) {
            line(player, "The Guildmaster of the Champions' Guild said I could earn", line++, true)
            line(player, "the right to wear rune armour if I went on a quest for", line++, true)
            line(player, "Oziach, who makes the armour.", line++, true)
            line(player, "I spoke to Oziach in Edgeville. He told me to slay the", line++, true)
            line(player, "dragon of Crandor island.", line++, true)
            line(player, "The Champions' Guild Guildmaster gave me more detailed", line++, true)
            line(player, "instructions.", line++, true)
            line(player, "To defeat the dragon I will need to find a !!map?? to !!Crandor??,", line++)
            line(player, "a !!ship??, a !!captain?? to take me there and some kind of", line++)
            line(player, "!!protection?? against the dragon's breath.", line++)

            // Map pieces.
            if (!player.inventory.containsItem(MAZE_PIECE) && !player.bank.containsItem(MAZE_PIECE)) {
                line(player, "One-third of the map is in !!Melzar's Maze?? near !!Rimmington??.", line++)
            } else {
                line(player, "I found the piece of the map hidden in !!Melzar's Maze??.", line++, true)
            }

            // Oracle Ice mountain.
            if (!player.inventory.containsItem(MAGIC_PIECE) && !player.bank.containsItem(MAGIC_PIECE)) {
                line(player, "One-third of the map is hidden and only the !!Oracle?? on", line++)
                line(player, "!!Ice Mountain?? will know where it is.", line++)
            } else {
                line(player, "I found the piece of the map that was hidden beneath !!Ice", line++, true)
                line(player, "Mountain??.", line++, true)
            }

            // Goblin Wormbrain.
            if (!player.inventory.containsItem(WORMBRAIN_PIECE) && !player.bank.containsItem(WORMBRAIN_PIECE)) {
                line(player, "One-third of the map was stolen by a !!goblin?? from the", line++)
                line(player, "!!Goblin Village??.", line++)
            } else {
                line(player, "I found the piece of the map that the goblin !!Wormbrain??", line++, true)
                line(player, "stole.", line++, true)
            }

            // Anti-dragon shield.
            if (!player.inventory.containsItem(SHIELD) && !player.bank.containsItem(SHIELD)) {
                line(player, "I should ask the !!Duke of Lumbridge?? for an !!anti-dragon", line++)
                line(player, "shield??.", line++)
            } else {
                line(player, "The Duke of Lumbridge gave me an !!anti-dragonbreath shield??.", line++, true)
            }

            // Buy, repair ship.
            if (!player.savedData.questData.getDragonSlayerAttribute("ship")) {
                line(player, "I should see if there is a !!ship?? for sale in !!Port Sarim??.", line++)
            } else {
                line(player, "I bought a ship in !!Port Sarim?? called the !!Lady Lumbridge??.", line++, true)
                if (!player.savedData.questData.getDragonSlayerAttribute("repaired")) {
                    line(player, "I need to repair the hole in the bottom of the ship.", line++, true)
                } else {
                    line(player, "I have repaired my ship using !!wooden planks?? and !!steel nails??.", line++, true)
                }
            }
            line++
        }

        if (stage == 30) {
            line(player, "The Guildmaster said I had to find three pieces of a map,", line++, true)
            line(player, "a ship, a captain and a shield to protect me.", line++, true)
            line(player, "I found the piece of the map hidden in Melzar's Maze.", line++, true)
            line(player, "I found the piece hidden beneath Ice Mountain.", line++, true)
            line(player, "I found the piece stolen by !!Wormbrain??.", line++, true)
            line(player, "The Duke of Lumbridge gave me an anti-dragonbreath shield.", line++, true)
            line(player, "I bought a ship in Port Sarim called the Lady Lumbridge.", line++, true)
            line(player, "I repaired the ship using wooden planks and steel nails.", line++, true)
            line(player, "!!Captain Ned?? has agreed to sail the ship to !!Crandor?? for me.", line++, true)
            line(player, "Now I should go to my ship in !!Port Sarim?? and set sail for", line++)
            line(player, "!!Crandor??!", line++)
            line++
        }

        if (stage == 40) {
            line(player, "I found all three map pieces and obtained a dragon shield.", line++, true)

            if (!player.getAttribute("demon-slayer:memorize", false)) {
                if (!inInventory(player, Items.ELVARGS_HEAD_11279)) {
                    line(player, "Now all I need to do is kill the !!dragon??!", line++)
                } else {
                    line(player, "I have slain the !!dragon??! Now I should tell !!Oziach??.", line++)
                }
            } else {
                line(player, "I found a !!secret passage?? between !!Karamja?? and !!Crandor??,", line++, true)
                line(player, "so I no longer need a seaworthy ship to get there.", line++, true)

                if (!inInventory(player, Items.ELVARGS_HEAD_11279)) {
                    line(player, "Now all I need to do is kill the !!dragon??!", line++)
                } else {
                    line(player, "I have slain the !!dragon??! Now I should tell !!Oziach??.", line++)
                }
            }
            line++
        }

        if (stage == 100) {
            line(player, "According to the !!Guildmaster of the Champions' Guild??,", line++, true)
            line(player, "I could earn the right to wear !!rune armour?? if I went on a quest", line++, true)
            line(player, "for !!Oziach??, who makes the armour.", line++, true)
            line(player, "I spoke to !!Oziach?? in !!Edgeville??. He told me to slay the", line++, true)
            line(player, "!!dragon of Crandor island??.", line++, true)
            line(player, "The Champions' Guild Guildmaster told me I had to find three pieces", line++, true)
            line(player, "of a !!map?? to !!Crandor??, a !!ship??, a !!captain?? to take me there,", line++, true)
            line(player, "and a !!shield?? to protect me from the dragon's breath.", line++, true)

            line(player, "I found the piece of the map that was hidden in !!Melzar's Maze??.", line++, true)
            line(player, "I found the piece of the map that was hidden beneath !!Ice Mountain??.", line++, true)
            line(player, "I found the piece of the map that the goblin, !!Wormbrain??, stole.", line++, true)
            line(player, "The !!Duke of Lumbridge?? gave me an !!anti-dragonbreath shield??.", line++, true)

            line(player, "I have found a secret passage leading from !!Karamja?? to !!Crandor??,", line++, true)
            line(player, "so I no longer need to worry about finding a seaworthy ship and captain.", line++, true)
            line(player, "I sailed to !!Crandor?? and killed the !!dragon??. I am not a true", line++, true)
            line(player, "champion and have proved myself worthy to wear !!rune platebody??.", line++, true)

            line(player, "<col=FF0000>QUEST COMPLETE!</col>", line++, false)
            line(player, "I gained !!2 Quest Points??, !!18,650 Strength XP??, !!18,650 Defence XP??", line++, false)
            line(player, "and the right to wear !!rune platebodies??.", line++, false)
        }
    }


    @Throws(Throwable::class)
    override fun newInstance(`object`: Any?): Quest {
        definePlugins(
            DragonSlayerPlugin(),
            MagicDoorPlugin(),
            DragonSlayerCutscene(),
            MazeDemonNPC(),
            MazeGhostNPC(),
            MazeSkeletonNPC(),
            MazeZombieNPC(),
            MelzarTheMadNPC(),
            WormbrainNPC(),
            ZombieRatNPC(),
            GuildmasterDialogue(),
            ElvargNPC(),
            WormbrainDialogue(),
            OziachDialogue(),
            DukeHoracioDialogue(),
            CabinBoyDialogue()
        )
        return this
    }

    override fun finish(player: Player) {
        super.finish(player)
        var ln = 10
        drawReward(player, "2 Quests Points", ln++)
        drawReward(player, "Ability to wear rune platebody", ln++)
        drawReward(player, "18,650 Strength XP", ln++)
        drawReward(player, "18,650 Defence XP", ln++)
        drawReward(player, "You have completed the Dragon Slayer Quest!", ln)
        sendItemZoomOnInterface(player, Components.QUEST_COMPLETE_SCROLL_277, 5, ELVARG_HEAD.id, 230)
        rewardXP(player, Skills.STRENGTH, 18650.0)
        rewardXP(player, Skills.DEFENCE, 18650.0)
        player.unhook(spellCastHook)
        player.unhook(pickedUpHook)
    }

    override fun setStage(player: Player, stage: Int) {
        super.setStage(player, stage)
        if (stage == 20) {
            player.hook(Event.SpellCast, spellCastHook)
            player.hook(Event.PickedUp, pickedUpHook)
        }
    }

    override fun login(player: Player) {
        if (getQuestStage(player, this.name) == 20) {
            player.hook(Event.SpellCast, spellCastHook)
            player.hook(Event.PickedUp, pickedUpHook)
        }
    }

    private val spellCastHook = object : EventHook<SpellCastEvent> {
        override fun process(entity: Entity, event: SpellCastEvent) {
            if (event.spellId == 19 && event.target != null && event.target.id == Items.MAP_PART_1536) {
                entity.unhook(this)
            }
        }
    }

    private val pickedUpHook = object : EventHook<PickUpEvent> {
        override fun process(entity: Entity, event: PickUpEvent) {
            if (event.itemId == Items.MAP_PART_1536) {
                entity.unhook(this)
            }
        }
    }

    companion object {
        val MAZE_KEY = Item(Items.MAZE_KEY_1542)
        val RED_KEY = Item(Items.KEY_1543)
        val ORANGE_KEY = Item(Items.KEY_1544)
        val YELLOW_KEY = Item(Items.KEY_1545)
        val BLUE_KEY = Item(Items.KEY_1546)
        val PURPLE_KEY = Item(Items.KEY_1547)
        val GREEN_KEY = Item(Items.KEY_1548)
        val MAZE_PIECE = Item(Items.MAP_PART_1535)
        val MAGIC_PIECE = Item(Items.MAP_PART_1537)
        val WORMBRAIN_PIECE = Item(Items.MAP_PART_1536)
        val SHIELD = Item(Items.ANTI_DRAGON_SHIELD_1540)
        val CRANDOR_MAP = Item(Items.CRANDOR_MAP_1538)
        val NAILS = Item(Items.STEEL_NAILS_1539, 30)
        val PLANK = Item(Items.PLANK_960)
        val HAMMER = Item(Items.HAMMER_2347)
        val ELVARG_HEAD = Item(Items.ELVARGS_HEAD_11279)

        @JvmStatic
        fun handleMagicDoor(player: Player, interaction: Boolean): Boolean {
            if (!player.savedData.questData.getDragonSlayerItem("lobster") ||
                !player.savedData.questData.getDragonSlayerItem("bowl") ||
                !player.savedData.questData.getDragonSlayerItem("silk") ||
                !player.savedData.questData.getDragonSlayerItem("wizard"))
            {
                if (interaction) {
                    sendMessage(player, "You can't see any way to open the door.")
                }
                return true
            }
            playAudio(player, Sounds.DRAGONSLAYER_MAGICDOOR_3758)
            sendMessage(player, "The door opens...")
            val `object` = getObject(Location(3050, 9839, 0))
            player.faceLocation(`object`!!.location)
            animateScenery(`object`, 6636)
            Pulser.submit(
                object : Pulse(1, player) {
                    var counter = 0

                    override fun pulse(): Boolean {
                        when (counter++) {
                            4 -> AgilityHandler.walk(
                                player, 0, player.location,
                                if (player.location.x == 3051) {
                                    Location.create(3049, 9840, 0)
                                } else {
                                    Location.create(3051, 9840, 0)
                                },
                                null, 0.0, null,
                            )

                            5 -> animateScenery(`object`, 6637)
                            6 -> {
                                animateScenery(`object`, 6635)
                                return true
                            }
                        }
                        return false
                    }
                },
            )
            return true
        }
    }
}
