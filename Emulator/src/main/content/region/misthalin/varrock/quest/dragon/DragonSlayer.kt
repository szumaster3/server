package content.region.misthalin.varrock.quest.dragon

import content.global.skill.agility.AgilityHandler
import content.region.misthalin.lumbridge.dialogue.DukeHoracioDialogue
import content.region.misthalin.varrock.quest.dragon.cutscene.DragonSlayerCutscene
import content.region.misthalin.varrock.quest.dragon.dialogue.ChestDialogue
import content.region.misthalin.varrock.quest.dragon.dialogue.GuildmasterDialogue
import content.region.misthalin.varrock.quest.dragon.dialogue.OziachDialogue
import content.region.misthalin.varrock.quest.dragon.dialogue.WormbrainDialogue
import content.region.misthalin.varrock.quest.dragon.handlers.DragonSlayerPlugin
import content.region.misthalin.varrock.quest.dragon.handlers.MagicDoorPlugin
import content.region.misthalin.varrock.quest.dragon.handlers.npc.*
import core.api.*
import core.api.quest.getQuestStage
import core.game.component.Component
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
import org.rs.consts.*

@Initializable
class DragonSlayer :
    Quest(Quests.DRAGON_SLAYER, 18, 17, 2, Vars.VARP_QUEST_DRAGON_SLAYER_PROGRESS_176, 0, 1, 10),
    LoginListener {
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
            ChestDialogue(),
            GuildmasterDialogue(),
            ElvargNPC(),
            WormbrainDialogue(),
            OziachDialogue(),
            DukeHoracioDialogue(),
        )
        return this
    }

    override fun drawJournal(
        player: Player,
        stage: Int,
    ) {
        super.drawJournal(player, stage)
        when (getStage(player)) {
            0 -> {
                player.packetDispatch.sendString(
                    BLUE + "I can start this quest by speaking to the " + RED + "Guildmaster " + BLUE + "in",
                    275,
                    4 + 7,
                )
                player.packetDispatch.sendString(
                    BLUE + "the " + RED + "Champions' Guild" + BLUE + " , south-west of Varrock.",
                    275,
                    5 + 7,
                )
                player.packetDispatch.sendString(
                    BLUE + "I will need to be able to defeat a " + RED + "level 83 dragon.",
                    275,
                    6 + 7,
                )
                if (player.questRepository.points < 32) {
                    player.packetDispatch.sendString(
                        BLUE + "To enter the Champions' Guild I need" + RED + " 32 Quest Points.",
                        275,
                        7 + 7,
                    )
                } else {
                    player.packetDispatch.sendString(
                        "<str>To enter the Champions' Guild I need 32 Quest Points.",
                        275,
                        7 + 7,
                    )
                }
            }

            10 -> {
                line(player, "<str>The Guildmaster of the Champions' Guild said I could earn", 4 + 7)
                line(player, "<str>the right to wear rune armour if I went on a quest for", 5 + 7)
                line(player, "<str>Oziach, who makes the armour.", 6 + 7)
                line(
                    player,
                    BLUE + "I should speak to " + RED + "Oziach" + BLUE + ", who lives by the cliffs to the",
                    7 + 7,
                )
                line(player, BLUE + "west of " + RED + "Edgeville.", 8 + 7)
            }

            15 -> {
                line(player, "<str>The Guildmaster of the Champions' Guild said I could earn", 4 + 7)
                line(player, "<str>the right to wear rune armour if I went on a quest for", 5 + 7)
                line(player, "<str>Oziach, who makes the armour.", 6 + 7)
                line(player, "<str>I spoke to Oziach in Edgeville. He told me to slay the", 7 + 7)
                line(player, "<str>dragon of Crandor island.", 8 + 7)
                line(
                    player,
                    BLUE + "I should return to the " + RED + "Champions' Guild Guildmaster " + BLUE + "for",
                    9 + 7,
                )
                line(player, BLUE + "more detailed instructions.", 10 + 7)
            }

            20 -> {
                line(player, "<str>The Guildmaster of the Champions' Guild said I could earn", 4 + 7)
                line(player, "<str>the right to wear rune armour if I went on a quest for", 5 + 7)
                line(player, "<str>Oziach, who makes the armour.", 6 + 7)
                line(player, "<str>I spoke to Oziach in Edgeville. He told me to slay the", 7 + 7)
                line(player, "<str>dragon of Crandor island.", 8 + 7)
                line(player, "<str>The Champions' Guild Guildmaster gave me more detailed", 9 + 7)
                line(player, "<str>instructions.", 10 + 7)
                line(
                    player,
                    BLUE + "To defeat the dragon I will need to find a " + RED + "map " + BLUE + "to Crandor, a",
                    11 + 7,
                )
                line(
                    player,
                    RED + "ship" + BLUE + ", a " + RED + "captain " + BLUE + "to take me there and some kind of",
                    12 + 7,
                )
                line(player, RED + "protection " + BLUE + "against the dragon's breath.", 13 + 7)
                if (!player.inventory.containsItem(MAZE_PIECE) && !player.bank.containsItem(MAZE_PIECE)) {
                    line(player, BLUE + "One-third of the map is in " + RED + "Melzar's Maze" + BLUE + ", near", 14 + 7)
                    line(player, RED + "Rimmington" + ".", 15 + 7)
                } else {
                    line(player, "<str>I found the piece of the map that was hidden in Melzar's", 14 + 7)
                    line(player, "<str>Maze.", 15 + 7)
                }
                if (!player.inventory.containsItem(MAGIC_PIECE) && !player.bank.containsItem(MAGIC_PIECE)) {
                    line(
                        player,
                        BLUE + "One-third of the map is hidden and only the " + RED + "Oracle " + BLUE + "on " + RED +
                            "Ice",
                        16 + 7,
                    )
                    line(player, RED + "Mountain" + BLUE + " will know where it is.", 17 + 7)
                } else {
                    line(player, "<str>I found the piece of the map that was hidden beneath Ice", 16 + 7)
                    line(player, "<str>Mountain.", 18 + 7)
                }
                if (!player.inventory.containsItem(WORMBRAIN_PIECE) && !player.bank.containsItem(WORMBRAIN_PIECE)) {
                    line(
                        player,
                        BLUE + "One-third of the map was stolen by a " + RED + "goblin " + BLUE + "from the",
                        18 + 7,
                    )
                    line(player, RED + "Goblin Village.", 19 + 7)
                } else {
                    line(player, "<str>I found the piece of the map that the goblin, Wormbrain,", 18 + 7)
                    line(player, "<str>stole.", 19 + 7)
                }
                if (!player.inventory.containsItem(SHIELD) && !player.bank.containsItem(SHIELD)) {
                    line(
                        player,
                        BLUE + "I should ask the " + RED + "Duke of Lumbridge " + BLUE + "for an " + RED + "anti-",
                        20 + 7,
                    )
                    line(player, RED + "dragonbreath shield.", 21 + 7)
                } else {
                    line(player, "<str>The Duke of Lumbridge gave me an anti-dragonbreath", 20 + 7)
                    line(player, "<str>shield.", 21 + 7)
                }
                if (!player.savedData.questData.getDragonSlayerAttribute("ship")) {
                    line(
                        player,
                        BLUE + "I should see if there is a " + RED + "ship " + BLUE + "for sale in " + RED +
                            "Port Sarim",
                        22 + 7,
                    )
                } else {
                    line(player, "<str>I bought a ship in Port Sarim called the Lady Lumbridge.", 22 + 7)
                    if (!player.savedData.questData.getDragonSlayerAttribute("repaired")) {
                        line(player, "<str>I need to repair the hole in bottom of the ship.", 23 + 7)
                    } else {
                        line(player, "<str>I have repaired my ship using wooden planks and steel", 23 + 7)
                        line(player, "<str>nails.", 24 + 7)
                    }
                }
            }

            30 -> {
                line(player, "<str>The Guildmaster of the Champions' Guild said I could earn", 4 + 7)
                line(player, "<str>the right to wear rune armour if I went on a quest for", 5 + 7)
                line(player, "<str>Oziach, who makes the armour.", 6 + 7)
                line(player, "<str>I spoke to Oziach in Edgeville. He told me to slay the", 7 + 7)
                line(player, "<str>dragon of Crandor island.", 8 + 7)
                line(player, "<str>The Champions' Guild Guildmaster told me I had to find", 9 + 7)
                line(player, "<str>three pieces of a map to Crandor, a ship, a captain to take", 10 + 7)
                line(player, "<str>me there and a shield to protect me from the dragon's", 11 + 7)
                line(player, "<str>breath.", 12 + 7)
                line(player, "<str>I found the piece of the map that was hidden in Melzar's", 13 + 7)
                line(player, "<str>Maze.", 14 + 7)
                line(player, "<str>I found the piece of the map that was hidden beneath Ice", 15 + 7)
                line(player, "<str>Mountain.", 16 + 7)
                line(player, "<str>I found the piece of the map that the goblin, Wormbrain,", 17 + 7)
                line(player, "<str>stole.", 18 + 7)
                line(player, "<str>The Duke of Lumbridge gave me an anti-dragonbreath", 19 + 7)
                line(player, "<str>shield.", 20 + 7)
                line(player, "<str>I bought a ship in Port Sarim called the Lady Lumbridge", 21 + 7)
                line(player, "<str>I have repaired my ship using wooden planks and steel", 22 + 7)
                line(player, "<str>nails.", 23 + 7)
                line(player, "<str>Captain Ned from Draynor Village has agreed to sail the", 24 + 7)
                line(player, "<str>ship to Crandor for me.", 25 + 7)
                line(
                    player,
                    BLUE + "Now I should go to my ship in " + RED + "Port Sarim " + BLUE + "and set sail for",
                    26 + 7,
                )
                line(player, RED + "Crandor" + BLUE + "!", 27 + 7)
            }

            40 -> {
                line(player, "<str>The Guildmaster of the Champions' Guild said I could earn", 4 + 7)
                line(player, "<str>the right to wear rune armour if I went on a quest for", 5 + 7)
                line(player, "<str>Oziach, who makes the armour.", 6 + 7)
                line(player, "<str>I spoke to Oziach in Edgeville. He told me to slay the", 7 + 7)
                line(player, "<str>dragon of Crandor island.", 8 + 7)
                line(player, "<str>The Champions' Guild Guildmaster told me I had to find", 9 + 7)
                line(player, "<str>three pieces of a map to Crandor, a ship, a captain to take", 10 + 7)
                line(player, "<str>me there and a shield to protect me from the dragon's", 11 + 7)
                line(player, "<str>breath.", 12 + 7)
                line(player, "<str>I found the piece of the map that was hidden in Melzar's", 13 + 7)
                line(player, "<str>Maze.", 14 + 7)
                line(player, "<str>I found the piece of the map that was hidden beneath Ice", 15 + 7)
                line(player, "<str>Mountain.", 16 + 7)
                line(player, "<str>I found the piece of the map that the goblin, Wormbrain,", 17 + 7)
                line(player, "<str>stole.", 18 + 7)
                line(player, "<str>The Duke of Lumbridge gave me an anti-dragonbreath", 19 + 7)
                line(player, "<str>shield.", 20 + 7)
                if (!player.getAttribute("demon-slayer:memorize", false)) {
                    if (!inInventory(player, Items.ELVARGS_HEAD_11279)) {
                        line(player, BLUE + "Now all I need to do is kill the " + RED + "dragon" + BLUE + "!", 21 + 7)
                    } else {
                        line(
                            player,
                            BLUE + "I have slain the dragon! Now I just need to tell " + RED + "Oziach.",
                            21 + 7,
                        )
                    }
                } else {
                    line(player, "<str>I have found a secret passage leading from Karamja to", 21 + 7)
                    line(player, "<str>Crandor, so I no longer need to worry about finding a", 22 + 7)
                    line(player, "<str>seaworthy ship and captain to take me there.", 23 + 7)
                    if (!inInventory(player, Items.ELVARGS_HEAD_11279)) {
                        line(player, BLUE + "Now all I need to do is kill the " + RED + "dragon" + BLUE + "!", 24 + 7)
                    } else {
                        line(
                            player,
                            BLUE + "I have slain the dragon! Now I just need to tell " + RED + "Oziach.",
                            24 + 7,
                        )
                    }
                }
            }

            100 -> {
                line(player, "<str>The Guildmaster of the Champions' Guild said I could earn", 4 + 7)
                line(player, "<str>the right to wear rune armour if I went on a quest for", 5 + 7)
                line(player, "<str>Oziach, who makes the armour.", 6 + 7)
                line(player, "<str>I spoke to Oziach in Edgeville. He told me to slay the", 7 + 7)
                line(player, "<str>dragon of Crandor island.", 8 + 7)
                line(player, "<str>The Champions' Guild Guildmaster told me I had to find", 9 + 7)
                line(player, "<str>three pieces of a map to Crandor, a ship, a captain to take", 10 + 7)
                line(player, "<str>me there and a shield to protect me from the dragon's", 11 + 7)
                line(player, "<str>breath.", 12 + 7)
                line(player, "<str>I found the piece of the map that was hidden in Melzar's", 13 + 7)
                line(player, "<str>Maze.", 14 + 7)
                line(player, "<str>I found the piece of the map that was hidden beneath Ice", 15 + 7)
                line(player, "<str>Mountain.", 16 + 7)
                line(player, "<str>I found the piece of the map that the goblin, Wormbrain,", 17 + 7)
                line(player, "<str>stole.", 18 + 7)
                line(player, "<str>The Duke of Lumbridge gave me an anti-dragonbreath", 19 + 7)
                line(player, "<str>shield.", 20 + 7)
                line(player, "<str>I have found a secret passage leading from Karamja to", 21 + 7)
                line(player, "<str>Crandor, so I no longer need to worry about finding a", 22 + 7)
                line(player, "<str>seaworthy ship and captain to take me there.", 23 + 7)
                line(player, "<str>I sailed to Crandor and killed the dragon. I am not a true", 24 + 7)
                line(player, "<str>champion and have proved myself worthy to wear rune", 25 + 7)
                line(player, "<str>platemail!", 26 + 7)
                line(player, "<col=FF0000>QUEST COMPLETE!</col>", 28 + 7)
                line(
                    player,
                    BLUE + "I gained " + RED + "2 Quest Points" + BLUE + ", " + RED + "18,650 Strength XP" + BLUE + ", " +
                        RED +
                        "18,650",
                    29 + 7,
                )
                line(player, RED + "Defence XP " + BLUE + "and the right to wear " + RED + "rune platebodies.", 30 + 7)
            }
        }
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
        player.unhook(SpellCastHook)
        player.unhook(PickedUpHook)
    }

    override fun setStage(
        player: Player,
        stage: Int,
    ) {
        super.setStage(player, stage)
        if (stage == 20) {
            player.hook(Event.SpellCast, SpellCastHook)
            player.hook(Event.PickedUp, PickedUpHook)
        }
    }

    override fun login(player: Player) {
        if (getQuestStage(player, this.name) == 20) {
            player.hook(Event.SpellCast, SpellCastHook)
            player.hook(Event.PickedUp, PickedUpHook)
        }
    }

    private val SpellCastHook =
        object : EventHook<SpellCastEvent> {
            override fun process(
                entity: Entity,
                event: SpellCastEvent,
            ) {
                if (event.spellId == 19 && event.target != null && event.target.id == Items.MAP_PART_1536) {
                    entity.unhook(this)
                }
            }
        }
    private val PickedUpHook =
        object : EventHook<PickUpEvent> {
            override fun process(
                entity: Entity,
                event: PickUpEvent,
            ) {
                if (event.itemId == Items.MAP_PART_1536) {
                    entity.unhook(this)
                }
            }
        }

    companion object {
        @JvmField
        val MAZE_KEY = Item(Items.MAZE_KEY_1542)

        @JvmField
        val RED_KEY = Item(Items.KEY_1543)

        @JvmField
        val ORANGE_KEY = Item(Items.KEY_1544)

        @JvmField
        val YELLOW_KEY = Item(Items.KEY_1545)

        @JvmField
        val BLUE_KEY = Item(Items.KEY_1546)

        @JvmField
        val PURPLE_KEY = Item(Items.KEY_1547)

        @JvmField
        val GREEN_KEY = Item(Items.KEY_1548)

        @JvmField
        val MAZE_PIECE = Item(Items.MAP_PART_1535)

        @JvmField
        val MAGIC_PIECE = Item(Items.MAP_PART_1537)

        @JvmField
        val WORMBRAIN_PIECE = Item(Items.MAP_PART_1536)

        val SHIELD = Item(Items.ANTI_DRAGON_SHIELD_1540)

        @JvmField
        val CRANDOR_MAP = Item(Items.CRANDOR_MAP_1538)

        @JvmField
        val MAP_COMPONENT = Component(Components.DRAGON_SLAYER_QIP_MAP_547)

        @JvmField
        val NAILS = Item(Items.STEEL_NAILS_1539, 30)

        @JvmField
        val PLANK = Item(Items.PLANK_960)

        @JvmField
        val HAMMER = Item(Items.HAMMER_2347)

        @JvmField
        val ELVARG_HEAD = Item(Items.ELVARGS_HEAD_11279)

        @JvmStatic
        fun handleMagicDoor(
            player: Player,
            interaction: Boolean,
        ): Boolean {
            if (!player.savedData.questData.getDragonSlayerItem("lobster") ||
                !player.savedData.questData.getDragonSlayerItem(
                    "bowl",
                ) ||
                !player.savedData.questData.getDragonSlayerItem("silk") ||
                !player.savedData.questData.getDragonSlayerItem(
                    "wizard",
                )
            ) {
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
                            4 ->
                                AgilityHandler.walk(
                                    player,
                                    0,
                                    player.location,
                                    if (player.location.x == 3051) {
                                        Location.create(3049, 9840, 0)
                                    } else {
                                        Location.create(
                                            3051,
                                            9840,
                                            0,
                                        )
                                    },
                                    null,
                                    0.0,
                                    null,
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
