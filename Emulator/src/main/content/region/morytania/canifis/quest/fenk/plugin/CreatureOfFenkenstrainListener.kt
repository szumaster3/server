package content.region.morytania.canifis.quest.fenk.plugin

import content.region.morytania.canifis.quest.fenk.CreatureOfFenkenstrain
import content.region.morytania.canifis.quest.fenk.dialogue.BookcaseEastDialogueFile
import content.region.morytania.canifis.quest.fenk.dialogue.BookcaseWest
import content.region.morytania.canifis.quest.fenk.dialogue.RoavarDialogueFile
import core.api.*
import core.api.quest.finishQuest
import core.api.quest.getQuest
import core.api.quest.getQuestStage
import core.api.quest.setQuestStage
import core.game.dialogue.FaceAnim
import core.game.global.action.DoorActionHandler
import core.game.global.action.PickupHandler
import core.game.interaction.InteractionListener
import core.game.node.item.GroundItem
import core.game.system.task.Pulse
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests
import org.rs.consts.Scenery

/*
    TODO: Check, remove content from later revisions, until implementation of Garden of Tranquillity normal ring also work.

    Transportation

    The ring of charos (a) has the following uses for transportation:

     - [ ] Free boat trips between the Karamja, Ardougne, and Port Sarim ports.
     - [ ] Free trips to the Piscatoris Fishing Colony (unnecessary after completion of Swan Song as travel becomes permanently free).
     - [1/2] Reduced cost for Magic carpets to 100 coins, or 75 coins once a Rogue Trader completion.
     - [x] Reduced Charter Ship costs (the prices are a half of the original price, or a quarter if Cabin Fever is completed).
     - [ ] Free boat rides from Mort'ton to the middle of Mort Myre.

     Quests

     - [ ] Used in The Great Brain Robbery (the inactivated version also works).
     - [ ] Get the recipe for Asgoldian ale for free during Recipe for Disaster: Freeing the Mountain Dwarf and the ability to do the quest again.
     - [ ] Can be used instead of a fish to bribe one of the penguins during Cold War.
     - [ ] Cheaper snake charming kit in Pollnivneach for The Feud.
     - [ ] Charm farmers to get various unique seeds for Garden of Tranquillity.
     - [ ] Get the dirty laundry during While Guthix Sleeps.
     - [ ] Used on Ali Morrisane to obtain the original name of the Pharaoh Queen in Missing My Mummy.
     - [ ] Used to convince Ali Morrisane to tell you the location of some lost goods during Do No Evil.
     - [ ] An additional chat option to charm Milton the Miller during The Chosen Commander.
     - [ ] Can be used in Heroes' Quest instead of 1,000 coins when getting Master thief's armband solo as a Phoenix Gang member.

     Other

    - [x] The residents of Canifis will think you are a werewolf and will be nice to you (the inactivated version will grant this as well).
    - [ ] Dwarven Ferryman's fee for taking gold to bank near the Dondakan's mine reduced from 20% of gold ores to 10%.
    - [ ] Reduced Blast Furnace cost (for those with less than 60 Smithing).
    - [ ] Ability to convince Brundt the Chieftain to change your Fremennik name.
    - [x] Ability to convince the Fossegrimen south-west of Rellekka into accepting bass in place of shark to charge your enchanted lyre with 20 teleports.
    - [x] Used to access the Werewolf Agility Course (the inactivated version will grant this as well).
    - [ ] Sell grey wolf fur for 150 coins instead of 120 coins to Baraek in Varrock fur stall.
    - [ ] Sell molten glass for 25 coins instead of 20 coins to Fritz the Glassblower on Entrana.
    - [ ] 50% Luke will tell the story of how he lost half of his body.
    - [ ] Get more information out of Movario and Darve (also requires a Pendant of Lucien and has not yet started While Guthix Sleeps).
    - [ ] Choose the colour of your kitten from Gertrude.
    - [ ] Wizard Elriss will tell more about her research in the Runecrafting Guild (any colour of the full runecrafter robes must also be worn).
    - [ ] Beefy Bill will bank your beef, cowhide, and flour for free.
       1. Transfer flour, cowhides, and raw beef to the bank, charging a 10% commission [?] (rounded up),
         or for free if charmed with an activated ring of charos.
       2. If players use coins on him, he will say "Thanks!" and they will lose 1 coin.
       3. If players try to bank cooked beef, he will say "It's unhygienic to mix raw and cooked meats." and he will not bank it.
       4. If players try to bank raw undead beef, he will say, "Woah! Whatever that thing is, I'm not transporting it."
    - [ ] Can be used for prosecution in Court Cases for those with lower than 65 Attack. Also gives insight into jury members' backgrounds and motives on the jury selection screen.
    - [ ] Though he's not weak minded, the Wise Old Man will tell you more about his partyhat.
    - [ ] The sellers of Capes of Accomplishment will ask for 92,000 coins instead of the usual 99,000 when buying a Cape of Accomplishment.
    - [ ] The only exception to this is the Quest Cape, as the Wise Old Man is not weak-minded. Asking to buy the Quest Cape with the ring equipped will result in some humorous dialogue instead.
    - [ ] During the 2006 Christmas event when speaking to Shanty Claws, he will say something like: "Oh, I didn't think I'd see another werewolf here."

 */
class CreatureOfFenkenstrainListener : InteractionListener {
    override fun defineListeners() {
        addClimbDest(Location.create(3504, 9970, 0), Location.create(3504, 3571, 0))

        on(Items.NULL_5164, SCENERY, "read") { player, _ ->
            if (getQuestStage(player, Quests.CREATURE_OF_FENKENSTRAIN) < 7) {
                sendDialogueLines(
                    player,
                    "The signpost has a note pinned onto it. The note says:",
                    "'---- Braindead Butler Wanted ----",
                    "Gravedigging skills essential - Hunchback advantageous",
                    "See Dr Fenkenstrain at the castle NE of Canifis'",
                )
            } else {
                sendDialogueLines(
                    player,
                    "The signpost has a note pinned onto it. The note says:",
                    "'AAARRGGGHHHHH!!!!!'",
                )
            }
            if (getQuest(player, Quests.CREATURE_OF_FENKENSTRAIN).hasRequirements(player) &&
                getQuestStage(player, Quests.CREATURE_OF_FENKENSTRAIN) == 0
            ) {
                setQuestStage(player, Quests.CREATURE_OF_FENKENSTRAIN, 1)
            }
            return@on true
        }

        on(intArrayOf(Scenery.GRAVE_5168, Scenery.GRAVE_5169), SCENERY, "read") { player, node ->
            val grave = Graves.locationMap[node.location]
            sendMessage(player, "The grave says:")
            sendMessage(player, "  'Here lies ${grave?.graveName ?: "Unknown"} - REST IN PEACE'")
            return@on true
        }

        on(intArrayOf(Scenery.GRAVE_5168, Scenery.GRAVE_5169), SCENERY, "dig") { player, node ->
            if (!inInventory(player, Items.SPADE_952)) {
                sendMessage(player, "You need a spade to do that.")
                return@on true
            }
            val grave = Graves.locationMap[node.location]
            sendMessage(player, "You start digging...")
            animate(player, Animation(831))
            player.pulseManager.run(
                object : Pulse(5) {
                    override fun pulse(): Boolean {
                        player.animate(Animation(-1))
                        if (grave?.unearthItem != null &&
                            !hasAnItem(player, grave.unearthItem).exists()

                        ) {
                            sendItemDialogue(player, grave.unearthItem, grave.unearthText)
                            addItemOrDrop(player, grave.unearthItem)
                        } else {
                            sendMessage(player, "...but the grave is empty.")
                        }
                        return true
                    }
                },
            )
            return@on true
        }

        on(Scenery.BOOKCASE_5166, SCENERY, "search") { player, node ->
            if (node.location.equals(Location(3555, 3558, 1))) {
                openDialogue(player, BookcaseEastDialogueFile())
                return@on true
            } else if (node.location.equals(Location(3542, 3558, 1))) {
                openDialogue(player, BookcaseWest())
                return@on true
            } else {
                sendMessage(player, "It is a bookcase full of books")
                return@on true
            }
        }

        onUseWith(ITEM, Items.MARBLE_AMULET_4187, Items.OBSIDIAN_AMULET_4188) { player, _, _ ->
            if (removeItem(player, Items.MARBLE_AMULET_4187) && removeItem(player, Items.OBSIDIAN_AMULET_4188)) {
                sendItemDialogue(
                    player,
                    Items.STAR_AMULET_4183,
                    "The marble and obsidian amulets snap together tightly to form a six-pointed amulet.",
                )
                addItemOrDrop(player, Items.STAR_AMULET_4183)
            }
            return@onUseWith true
        }

        onUseWith(SCENERY, Items.STAR_AMULET_4183, Items.NULL_5167) { player, _, _ ->
            if (removeItem(player, Items.STAR_AMULET_4183)) {
                sendItemDialogue(
                    player,
                    Items.STAR_AMULET_4183,
                    "The star amulet fits exactly into the depression on the coffin lid.",
                )
                setAttribute(player, CreatureOfFenkenstrain.attributeUnlockedMemorial, true)
            }
            return@onUseWith true
        }

        on(Scenery.ENTRANCE_5170, SCENERY, "open") { player, node ->
            if (inInventory(player, Items.CAVERN_KEY_4184) && removeItem(player, Items.CAVERN_KEY_4184)) {
                DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
            } else {
                sendMessage(player, "The door is locked.")
            }
            return@on true
        }

        onUseWith(SCENERY, Items.CAVERN_KEY_4184, Scenery.ENTRANCE_5170) { player, used, with ->
            if (removeItem(player, used)) {
                DoorActionHandler.handleAutowalkDoor(player, with.asScenery())
            }
            return@onUseWith true
        }

        on(Scenery.CHEST_5163, SCENERY, "search") { player, _ ->
            sendItemDialogue(player, Items.CAVERN_KEY_4184, "You take a key out of the chest.")
            addItemOrDrop(player, Items.CAVERN_KEY_4184)
            return@on true
        }

        on(Items.PICKLED_BRAIN_4199, GROUND_ITEM, "take") { player, node ->
            if (node.location.equals(3492, 3474, 0)) {
                openDialogue(player, RoavarDialogueFile(2), findLocalNPC(player, NPCs.ROAVAR_1042)!!)
            } else {
                PickupHandler.take(player, node as GroundItem)
            }
            return@on true
        }

        onUseWith(ITEM, Items.PICKLED_BRAIN_4199, Items.DECAPITATED_HEAD_4197) { player, used, with ->
            if (removeItem(player, used) && removeItem(player, with)) {
                sendItemDialogue(
                    player,
                    Items.DECAPITATED_HEAD_4198,
                    "You squeeze the pickled brain into the decapitated head.",
                )
                addItemOrDrop(player, Items.DECAPITATED_HEAD_4198)
            }
            return@onUseWith true
        }

        on(Items.NULL_5167, SCENERY, "search") { player, node ->
            val scenery = node.asScenery()
            if (getAttribute(player, CreatureOfFenkenstrain.attributeUnlockedMemorial, false) ||
                getQuestStage(
                    player,
                    Quests.CREATURE_OF_FENKENSTRAIN,
                ) > 2
            ) {
                animateScenery(player, scenery, 1620)
                var dest: Location? = null
                if (scenery.location.equals(Location(3505, 3571))) {
                    dest = Location(3504, 9969)
                } else if (scenery.location.equals(Location(3578, 3527))) {
                    dest = Location(3577, 9927)
                }
                if (dest != null) {
                    player.pulseManager.run(
                        object : Pulse(3) {
                            override fun pulse(): Boolean {
                                resetAnimator(player)
                                teleport(player, dest!!)
                                return true
                            }
                        },
                    )
                }
            } else {
                if (scenery.location.equals(Location(3505, 3571))) {
                    sendMessage(
                        player,
                        "You find a depression in the memorial stone in the shape of a six-pointed star.",
                    )
                } else {
                    sendMessage(player, "You find nothing remarkable about the memorial stone.")
                }
            }
            return@on true
        }

        on(Items.NULL_5167, SCENERY, "push") { player, node ->
            val scenery = node.asScenery()
            if (getAttribute(player, CreatureOfFenkenstrain.attributeUnlockedMemorial, false) ||
                getQuestStage(player, Quests.CREATURE_OF_FENKENSTRAIN) > 2
            ) {
                animateScenery(player, scenery, 1620)
                var dest: Location? = null
                if (scenery.location.equals(Location(3505, 3571))) {
                    dest = Location(3504, 9969)
                } else if (scenery.location.equals(Location(3578, 3527))) {
                    dest = Location(3577, 9927)
                }
                if (dest != null) {
                    player.pulseManager.run(
                        object : Pulse(3) {
                            override fun pulse(): Boolean {
                                resetAnimator(player)
                                teleport(player, dest!!)
                                return true
                            }
                        },
                    )
                }
            } else {
                sendMessage(player, "The coffin is incredibly heavy, and does not budge.")
            }
            return@on true
        }

        on(Scenery.DOOR_5174, SCENERY, "open") { player, node ->
            if (getAttribute(player, CreatureOfFenkenstrain.attributeUnlockedShed, false) ||
                getQuestStage(
                    player,
                    Quests.CREATURE_OF_FENKENSTRAIN,
                ) >= 5
            ) {
                DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
            } else if (inInventory(player, Items.SHED_KEY_4186)) {
                if (removeItem(player, Items.SHED_KEY_4186)) {
                    DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
                    setAttribute(player, CreatureOfFenkenstrain.attributeUnlockedShed, true)
                }
            } else {
                sendMessage(player, "The door is locked.")
            }
            return@on true
        }

        onUseWith(SCENERY, Items.SHED_KEY_4186, Scenery.DOOR_5174) { player, used, with ->
            if (getAttribute(player, CreatureOfFenkenstrain.attributeUnlockedShed, false) ||
                getQuestStage(
                    player,
                    Quests.CREATURE_OF_FENKENSTRAIN,
                ) >= 5
            ) {
                DoorActionHandler.handleAutowalkDoor(player, with.asScenery())
            } else if (removeItem(player, used)) {
                DoorActionHandler.handleAutowalkDoor(player, with.asScenery())
                setAttribute(player, CreatureOfFenkenstrain.attributeUnlockedShed, true)
            }
            return@onUseWith true
        }

        on(Items.NULL_5158, SCENERY, "take-from") { player, _ ->
            sendMessage(player, "You take a garden cane from the pile.")
            addItemOrDrop(player, Items.GARDEN_CANE_4189)
            return@on true
        }

        on(Items.NULL_5157, SCENERY, "search") { player, _ ->
            sendItemDialogue(player, Items.GARDEN_BRUSH_4190, "You find a garden brush in the cupboard.")

            addItemOrDrop(player, Items.GARDEN_BRUSH_4190)
            return@on true
        }

        onUseWith(
            ITEM,
            intArrayOf(Items.GARDEN_BRUSH_4190, Items.EXTENDED_BRUSH_4191, Items.EXTENDED_BRUSH_4192),
            Items.GARDEN_CANE_4189,
        ) { player, used, with ->
            if (!inInventory(player, Items.BRONZE_WIRE_1794)) {
                sendMessage(player, "You need some bronze wire to tie them together.")
                return@onUseWith true
            }
            if (removeItem(player, Items.BRONZE_WIRE_1794) && removeItem(player, used) && removeItem(player, with)) {
                sendMessage(player, "You attach the cane to the brush.")
                when (used.id) {
                    Items.GARDEN_BRUSH_4190 -> {
                        addItemOrDrop(player, Items.EXTENDED_BRUSH_4191)
                    }

                    Items.EXTENDED_BRUSH_4191 -> {
                        addItemOrDrop(player, Items.EXTENDED_BRUSH_4192)
                    }

                    Items.EXTENDED_BRUSH_4192 -> {
                        addItemOrDrop(player, Items.EXTENDED_BRUSH_4193)
                    }
                }
            }
            return@onUseWith true
        }

        on(Scenery.FIREPLACE_5165, SCENERY, "examine") { player, _ ->
            if (!inInventory(player, Items.CONDUCTOR_4201)) {
                sendMessage(player, "You give the chimney a jolly good clean out.")
                sendItemDialogue(
                    player,
                    Items.CONDUCTOR_MOULD_4200,
                    "A lightning conductor mould falls down out of the chimney.",
                )
                addItemOrDrop(player, Items.CONDUCTOR_MOULD_4200)
                return@on true
            }
            sendMessage(player, "It looks like it needs a good sweep out.")
            return@on true
        }

        onUseWith(SCENERY, Items.EXTENDED_BRUSH_4193, Scenery.FIREPLACE_5165) { player, _, _ ->
            sendMessage(player, "You give the chimney a jolly good clean out.")
            sendItemDialogue(
                player,
                Items.CONDUCTOR_MOULD_4200,
                "A lightning conductor mould falls down out of the chimney.",
            )
            addItemOrDrop(player, Items.CONDUCTOR_MOULD_4200)
            return@onUseWith true
        }

        on(Scenery.LIGHTNING_CONDUCTOR_5176, SCENERY, "repair") { player, node ->
            if (!inInventory(player, Items.CONDUCTOR_4201)) {
                sendMessage(player, "You need to repair it with a conductor.")
                return@on true
            }
            if (removeItem(player, Items.CONDUCTOR_4201)) {
                if (getQuestStage(player, Quests.CREATURE_OF_FENKENSTRAIN) == 4) {
                    setQuestStage(player, Quests.CREATURE_OF_FENKENSTRAIN, 5)
                }
                sendDialogue(
                    player,
                    "You repair the lightning conductor not one moment too soon - a tremendous bold of lightning melts the new lightning conductor, and power blazes throughout the castle, if only briefly.",
                )
                val scenery = node.asScenery()
                replaceScenery(scenery, Items.NULL_5177, 3, node.location)
                animateScenery(player, scenery, 1632)
            }
            return@on true
        }

        on(Scenery.DOOR_5172, SCENERY, "open") { player, node ->
            if (inInventory(player, Items.TOWER_KEY_4185) || getQuestStage(player, Quests.CREATURE_OF_FENKENSTRAIN) > 7
            ) {
                DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
            } else {
                sendMessage(player, "The door is locked.")
            }
            return@on true
        }

        onUseWith(SCENERY, Items.TOWER_KEY_4185, Scenery.DOOR_5172) { player, _, with ->
            if (inInventory(player, Items.TOWER_KEY_4185)) {
                DoorActionHandler.handleAutowalkDoor(player, with.asScenery())
            }
            return@onUseWith true
        }

        on(NPCs.DR_FENKENSTRAIN_1670, NPC, "pickpocket") { player, _ ->
            if (getQuestStage(player, Quests.CREATURE_OF_FENKENSTRAIN) == 7) {
                sendMessage(player, "You steal the Ring of Charos from Fenkenstrain.")
                finishQuest(player, Quests.CREATURE_OF_FENKENSTRAIN)
            } else if (getQuestStage(player, Quests.CREATURE_OF_FENKENSTRAIN) > 7 &&
                hasAnItem(player, Items.RING_OF_CHAROS_4202).container == null
            ) {
                addItemOrDrop(player, Items.RING_OF_CHAROS_4202, 1)
                sendMessage(player, "You steal the Ring of Charos from Fenkenstrain.")
            } else {
                sendNPCDialogue(
                    player,
                    NPCs.DR_FENKENSTRAIN_1670,
                    "What do you think you're doing???",
                    FaceAnim.NEUTRAL,
                )
            }
            return@on true
        }
    }

    companion object {
        private val itemToAttribute =
            hashMapOf(
                Items.ARMS_4195 to CreatureOfFenkenstrain.attributeArms,
                Items.LEGS_4196 to CreatureOfFenkenstrain.attributeLegs,
                Items.TORSO_4194 to CreatureOfFenkenstrain.attributeTorso,
                Items.DECAPITATED_HEAD_4197 to CreatureOfFenkenstrain.attributeHead,
            )

        enum class Graves(
            val location: Location,
            val graveName: String,
            val unearthText: String,
            val unearthItem: Int?,
        ) {
            GRAVE1(Location(3541, 3541), "Anton Hayes", "...but the grave is empty.", null),
            GRAVE2(Location(3542, 3486), "Callum Elding", "...but the grave is empty.", null),
            GRAVE3(Location(3585, 3497), "Domin O'Raleigh", "...but the grave is empty.", null),
            GRAVE4(
                Location(3608, 3491),
                "Ed Lestwit",
                "...and you unearth a decapitated head.",
                Items.DECAPITATED_HEAD_4197,
            ),
            GRAVE5(Location(3588, 3472), "Elena Frey", "...but the grave is empty.", null),
            GRAVE6(Location(3593, 3509), "Eryn Treforest", "...but the grave is empty.", null),
            GRAVE7(Location(3594, 3491), "Isla Skye", "...but the grave is empty.", null),
            GRAVE8(Location(3596, 3479), "Jayna Harrow", "...but the grave is empty.", null),
            GRAVE9(Location(3604, 3466), "Jayne Corbo", "...but the grave is empty.", null),
            GRAVE10(Location(3608, 3466), "Kandik Kludge", "...but the grave is empty.", null),
            GRAVE11(Location(3616, 3478), "Korvic Frey", "...but the grave is empty.", null),
            GRAVE12(Location(3619, 3469), "Marabella Kludge", "...but the grave is empty.", null),
            GRAVE13(Location(3626, 3495), "Marcus Harrow", "...but the grave is empty.", null),
            GRAVE14(Location(3629, 3483), "Petrik Corbo", "...but the grave is empty.", null),
            GRAVE15(Location(3631, 3476), "Serra Alcanthric", "...but the grave is empty.", null),
            GRAVE16(Location(3631, 3500), "Toran Alcanthric", "...but the grave is empty.", null),
            GRAVE17(Location(3572, 3527), "Unknown", "...but the grave is empty.", null),
            GRAVE18(Location(3576, 3526), "Unknown", "...but the grave is empty.", null),
            GRAVE19(Location(3639, 3470), "Lord Rologray", "...but the grave is empty.", null),
            GRAVE20(Location(3634, 3503), "Lord Rologarth", "...but the grave is empty.", null),
            GRAVE21(Location(3502, 3576), "Lady Rolobrae", "...and you unearth a torso.", Items.TORSO_4194),
            GRAVE22(Location(3504, 3577), "Lord Rolomere", "...and you unearth a pair of arms.", Items.ARMS_4195),
            GRAVE23(Location(3506, 3576), "Lord Rolovanne", "...and you unearth a pair of legs.", Items.LEGS_4196),
            ;

            companion object {
                @JvmField
                val locationMap = Graves.values().associateBy { it.location }
            }
        }
    }
}
