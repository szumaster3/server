package content.global.skill.construction.servants

import content.global.skill.construction.BuildHotspot
import content.global.skill.construction.Room
import content.global.skill.construction.items.PlankType
import core.api.*
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.interaction.MovementPulse
import core.game.node.entity.impl.PulseType
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.game.world.GameWorld.Pulser
import core.game.world.map.Location
import core.game.world.map.path.Pathfinder
import core.tools.BLACK
import core.tools.DARK_BLUE
import shared.consts.Items
import shared.consts.NPCs
import shared.consts.Vars

/**
 * Represents the house servants dialogue.
 */
class HouseServantDialogue(player: Player? = null) : Dialogue(player) {

    private var sawmill = false
    private var logs: Item? = null
    private val lastFetch = "con:lastfetch"
    private val lastFetchType = "con:lastfetchtype"

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        val manager = player.houseManager
        val type = ServantType.forId(npc.id)
        val expression = if (type?.ordinal == 5) FaceAnim.OLD_DEFAULT else FaceAnim.HALF_GUILTY
        val fontColor = if (type?.ordinal == 5) DARK_BLUE else BLACK
        val gender = if (player!!.isMale) "sir" else "madam"
        val inHouse = manager.isInHouse(player)

        if (args.size > 1) {
            sawmill = args[1] as Boolean
            logs = args[2] as Item
        }

        // Player has no house.
        if (!manager.hasHouse() && inHouse) {
            npc(expression, fontColor + "You don't have a house that I can work in.", "I'll be waiting here if you decide to buy a house.").also { stage = 100 }
            return true
        }

        // Player has no servant yet.
        if (!manager.hasServant()) {
            val requirements = getStatLevel(player, Skills.CONSTRUCTION) >= type!!.level
            if (requirements) {
                // Offer to hire a new servant.
                when(type?.ordinal) {
                    1 -> npcl(FaceAnim.SAD, "'Allo mate! Got a job going? Only ${type.cost} coins!").also { stage = 0 }
                    2 -> npcl(FaceAnim.FRIENDLY, "Oh! Please hire me, $gender! I'm very good, well, I'm not bad, and my fee's only ${type.cost} coins.").also { stage = 0 }
                    3 -> npcl(FaceAnim.HALF_ASKING, "You're not aristocracy but I suppose you'd do. Do you want a good cook for ${type.cost} coins?").also { stage = 0 }
                    4 -> npcl(FaceAnim.NEUTRAL, "Good day, $gender. Would $gender care to hire a good butler for 5000 coins?").also { stage = 0 }
                    5 -> npcl(FaceAnim.OLD_DEFAULT, fontColor + "Greetings! I am Alathazdrar, butler to the Demon Lords, and I offer thee my services for a mere ${type.cost} coins!").also { stage = 0 }
                }
            } else {
                // Player does not meet level requirements.
                when(type?.ordinal) {
                    1 -> npcl(FaceAnim.SAD, "Sorry mate, but I'm not allowed to work for anyone without level 20 Construction. It's a safety 'azard!").also { stage = 100 }
                    2 -> npcl(FaceAnim.SAD, "Oh! Oh dear! I'm terribly sorry, $gender, but I'm not allowed to work for anyone unless they have level 25 Construction. If a house is poorly made I might damage it by mistake!").also { stage = 100 }
                    3 -> npcl(FaceAnim.HALF_GUILTY, "Hmph! I don't work for just anyone, you know! I refuse to work for someone who's below level 30 Construction!").also { stage = 100 }
                    4 -> npcl(FaceAnim.NEUTRAL, "I must respectfully decline, $gender. I offer a very exclusive service and will only work for people with over level 40 Construction.").also { stage = 100 }
                    5 -> npcl(FaceAnim.OLD_DEFAULT, fontColor + "You, a mere mortal with level ${player.skills.getLevel(Skills.CONSTRUCTION)} Construction, wish to employ ME, butler to Great Ones of the Outer Darkness? Ha ha ha! I refuse to work for anyone below level 50 Construction.").also { stage = 100 }
                }
            }
            return true
        }

        // Player already has a servant.
        val servant = manager.servant
        if (servant.item == null) servant.item = Item(0, 0)

        if (!inHouse) {
            // Player is not in house but tries to hire a different servant.
            if (npc.id != servant.id) {
                npc(expression, fontColor + "You already have someone working for you.", "Fire them first before hiring me.").also { stage = 100 }
            }
            return true
        }

        // Handle sawmill task.
        if (sawmill) {
            npc(expression, fontColor + "Very well, I will take these logs to the mill and", fontColor + "have them converted into planks.").also { stage = 110 }
            return true
        }

        // Servant returning items.
        if (servant.item.amount > 0) {
            if (freeSlots(player) < 1) {
                // Inventory full, servant waits with items.
                // Butler -> "Your goods, sir."
                npc(expression, fontColor + "I have returned with what you asked me to", fontColor + "retrieve. As I see your inventory is full, I shall wait", fontColor + "with these " + fontColor + servant.item.amount + fontColor + " items until you are ready.").also { stage = 100 }
            } else {
                // Inventory has space, deliver items.
                // Demon butler -> "I shall fly on wings of unholy flame to bring you the", "items you desire from where they are stored safely."
                npc(expression, fontColor + "I have returned with what you asked me to", fontColor + "retrieve.").also { stage = 150 }
            }
            return true
        }

        // Greeting and service usage info.
        when(type?.ordinal) {
            1 -> npcl(FaceAnim.THINKING, "Yes, $gender?").also { stage = 50 } // TODO
            2 -> npcl(FaceAnim.HALF_THINKING, "Yes? I mean, yes, $gender?").also { stage = 50 }
            3 -> npcl(FaceAnim.HALF_THINKING, "Yes, $gender?").also { stage = 50 } // TODO
            4 -> npcl(FaceAnim.THINKING, "Yes, $gender?").also { stage = 50 }
            5 -> npcl(FaceAnim.OLD_DEFAULT, "$fontColor I am at thy command, my master.").also { stage = 50 }
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        val manager = player.houseManager
        val servant = manager.servant
        var type = ServantType.forId(npc.id)
        val expression = if (type?.ordinal == 5) FaceAnim.OLD_DEFAULT else FaceAnim.HALF_GUILTY
        val fontColor = if (type?.ordinal == 5) DARK_BLUE else BLACK
        val gender = if (player!!.isMale) "sir" else "madam"
        when (stage) {
            0 -> options("What can you do?", "Tell me about your previous jobs.", "You're hired!").also { stage++ }
            1 -> when (buttonId) {
                1 -> player("What can you do?").also { stage = 200 }
                2 -> player("Tell me about your previous jobs.").also { stage = 206 }
                3 -> {
                    if (!manager.hasHouse()) {
                        npc(expression, fontColor + "You don't have a house that I can work in.", "I'll be waiting here if you decide to buy a house.")
                        stage = 100
                        return true
                    }
                    player("You're hired!")
                    stage = 2
                }
            }
            2 -> {
                when(type?.ordinal) {
                   1 -> npcl(FaceAnim.CHEER_TALK, "Cheers, mate! Look forward to working with you!")
                   2 -> npcl(FaceAnim.HAPPY, "Oh! Oh, thank you $gender, thank you!")
                   3 -> npcl(FaceAnim.FRIENDLY, "Alright, $gender. I can start work immediately.")
                   4 -> npcl(FaceAnim.HAPPY, "Thank you, $gender. I can start work immediately.")
                   5 -> npcl(FaceAnim.OLD_DEFAULT, fontColor + "I shall devote my every art to thy service, my Master.")
                }
                stage++
            }
            3 -> {
                if (type != null && player.inventory.getAmount(Items.COINS_995) >= type.cost
                    && player.inventory.remove(Item(Items.COINS_995, type.cost))) {

                    val servant = Servant(type)
                    manager.servant = servant

                    val hideMap = mapOf(
                        ServantType.RICK to 1,
                        ServantType.MAID to 3,
                        ServantType.COOK to 5,
                        ServantType.BUTLER to 6,
                        ServantType.DEMON_BUTLER to 8
                    )

                    val hide = hideMap[type] ?: 0
                    setVarbit(player, Vars.VARBIT_POH_SERVANT_HIRED_2190, hide, true)
                    sendDialogue(player, "The servant heads to your house.")
                    stage = 100
                } else {
                    playerl(FaceAnim.HALF_GUILTY, "You're hired... well, I don't have the money on me, but can I owe you? I'm good for it, honest.")
                    stage = 4
                }
            }
            4 -> {
                when(type?.ordinal) {
                    1 -> npcl(FaceAnim.THINKING, "Nice try, mate, but I don't start work without cash up front.")
                    2 -> npcl(FaceAnim.SAD, "Oh... I'm terribly sorry, $gender, but I'd really prefer to be paid before I start work.")
                    3 -> npcl(FaceAnim.HALF_THINKING, "Are you having a laugh? No cash up front, no cook.")
                    4 -> npcl(FaceAnim.NEUTRAL, "I regret that I cannot agree to begin my duties until I have been paid.")
                    5 -> npcl(FaceAnim.OLD_DEFAULT, fontColor + "I regret, Master, that I must insist on payment up front.")
                }
                stage = 100
            }

            // In house options.
            50 -> {
                val followOp = if (servant.pulseManager.isMovingPulse) "Stop following me." else "Follow me."
                options("Go to the bank/sawmill...", "Misc...", followOp, "You're fired!")
                stage++
            }
            51 -> {
                type = servant.type
                when (buttonId) {
                    1 -> {
                        val lastFetch = if (!servant.attributes.containsKey(lastFetch)) {
                            "Repeat last fetch task"
                        } else {
                            "Fetch another " + (type.capacity.toString() + " x " + (servant.getAttribute<Any>(lastFetch) as Item).name.lowercase()) + " (" + (servant.getAttribute(lastFetchType)) + ")"
                        }
                        options(lastFetch, "Go to the bank", "Go to the sawmill", "Pay wages (${servant.uses}/8 uses)")
                        stage++
                    }
                    2 -> options("Greet guests", "Cook me something").also { stage = 56 }
                    3 -> {
                        if (servant.pulseManager.isMovingPulse) servant.pulseManager.clear(PulseType.STANDARD) else follow(player, npc)
                        stage = 100
                    }
                    4 -> player("You're fired!").also { stage = 75 }
                }
            }
            // Fetch / Bank / Sawmill logic.
            52 -> when (buttonId) {
                1 -> {
                    if (!servant.attributes.containsKey(lastFetch)) {
                        npc(expression, fontColor + "I haven't recently fetched anything from the bank or", fontColor + "sawmill for you.")
                        stage = 50
                        return true
                    }
                    when (servant.getAttribute<String>(lastFetchType)) {
                        "bank" -> bankFetch(player, servant.getAttribute(lastFetch))
                        else -> {
                            end()
                            sawmillRun(player, servant.getAttribute(lastFetch))
                        }
                    }
                }
                2 -> options("Planks", "Oak planks", "Teak planks", "Mahogany planks", "More...").also { stage = 60 }
                3 -> {
                    if (type == ServantType.MAID || type == ServantType.RICK) {
                        npc(expression, fontColor + "I am unable to travel to the sawmill for you.")
                        stage = 100
                        return true
                    }
                    npc(expression, fontColor + "Hand the logs to me and I will take them to the", fontColor + "sawmill for you.")
                    stage = 100
                }
                4 -> {
                    stage = 100
                    if (servant.uses < 1) {
                        npc(expression, fontColor + "You have no need to pay me yet, I haven't performed", fontColor + "any of my services for you.")
                        return true
                    }
                    if (!player.inventory.containsItem(Item(Items.COINS_995, type!!.cost))) {
                        npc(expression, fontColor + "Thanks for the kind gesture, but you don't have enough", fontColor + "money to pay me. I require " + fontColor + type.cost + fontColor + " coins every eight uses", fontColor + "of my services.")
                        return true
                    }
                    if (player.inventory.remove(Item(Items.COINS_995, type.cost))) {
                        npc(expression, fontColor + "Thank you very much.")
                        servant.uses = 0
                        return true
                    }
                }
            }
            // Guest greeting / cooking options.
            56 -> when (buttonId) {
                1 -> {
                    servant.isGreet = !servant.isGreet
                    player("Please ${if (servant.isGreet) "greet" else "do not greet"} all new guests upon arrival.")
                    stage++
                }
                2 -> {
                    if (type!!.food.isEmpty()) {
                        npc(expression, fontColor + "I don't know any recipes.")
                        stage = 100
                        return true
                    }

                    if (type.food.size > 1) {
                        options(
                            getItemName(type.food[0]!!),
                            getItemName(type.food[1]!!),
                            "Actually I don't want any"
                        ).also { stage = 58 }
                    } else {
                        options(
                            getItemName(type.food[0]!!),
                            "Actually I don't want any"
                        ).also { stage = 59 }
                    }
                }
            }
            57 -> {
                when(type?.ordinal) {
                    5 -> npc(expression, fontColor + "Yes, Master. I shall spread the aura of thy hospitality to all visitors!")
                    else -> npc(expression, "Very good, $gender.")
                }
                stage = 50
            }
            // Cooking multiple options.
            58 -> {
                if (freeSlots(player) < 1) {
                    npc(expression, fontColor + "I would love to share my fine cooking with you, but your hands are currently full.")
                    stage = 100
                    return true
                }
                if (!requirements(player, null, false)) { end(); return true }

                val room = player.houseManager.getRoom(player.location)
                val shelfLevel = getShelfLevel(room)

                when (buttonId) {
                    1 -> { // food[0]
                        if (type == ServantType.BUTLER && room?.isBuilt(BuildHotspot.DINING_TABLE) != true) {
                            npc(expression, fontColor + "I regret that I cannot make a meal unless ${if (player.isMale) "sir" else "madam"} has a dining table for me to serve it on.")
                            stage = 100
                        } else {
                            addItemOrDrop(player, type!!.food[0]!!, 1)
                            servant.uses += 1
                            npc(expression, fontColor + "Luckily for you, I already have some made. Here you go.")
                            stage = 50
                        }
                    }
                    2 -> { // food[1]
                        if ((type == ServantType.COOK || type == ServantType.BUTLER) && shelfLevel > 0) {
                            options("Yes please", "No thank you", "Actually I don't want any").also { stage = 300 }
                        } else if (type == ServantType.COOK || type == ServantType.BUTLER) {
                            npc(expression, fontColor + "You don't have a kitchen with the proper furniture for me to work in.")
                            stage = 100
                        } else {
                            addItemOrDrop(player, type!!.food[1]!!, 1)
                            servant.uses += 1
                            npc(expression, fontColor + "Luckily for you, I already have some made. Here you go.")
                            stage = 50
                        }
                    }
                    3 -> {
                        player("Actually, I don't want any.").also { stage = 100 }
                        return true
                    }
                }
            }
            //food[0]
            59 -> {
                if (freeSlots(player) < 1) {
                    npc(expression, fontColor + "I would love to share my fine cooking with you, but your hands are currently full.")
                    stage = 100
                    return true
                }
                if (!requirements(player, null, false)) { end(); return true }

                val room = player.houseManager.getRoom(player.location)

                when (buttonId) {
                    1 -> {
                        if (type == ServantType.BUTLER && room?.isBuilt(BuildHotspot.DINING_TABLE) != true) {
                            npc(expression, fontColor + "I regret that I cannot make a meal unless ${if (player.isMale) "sir" else "madam"} has a dining table for me to serve it on.")
                            stage = 100
                        } else {
                            addItemOrDrop(player, type!!.food[0]!!, 1)
                            servant.uses += 1
                            npc(expression, fontColor + "Luckily for you, I already have some made. Here you go.")
                            stage = 100
                        }
                    }
                    2 -> {
                        player("Actually, I don't want any.").also { stage = 100 }
                        return true
                    }
                }
            }
            300 -> options("Yes please","No thank you","Actually I don't want any").also { stage = 301 }
            301 -> when(buttonId){
                1,2 -> {
                    type!!.food.getOrNull(1)?.let { addItemOrDrop(player, it, 1) }
                    servant.uses += 1
                    stage = 100
                }
                3 -> { player("Actually, I don't want any."); stage = 100 }
            }
            // Plank / Material fetch stages.
            60 -> when (buttonId) {
                1 -> bankFetch(player, Item(Items.PLANK_960))
                2 -> bankFetch(player, Item(Items.OAK_PLANK_8778))
                3 -> bankFetch(player, Item(Items.TEAK_PLANK_8780))
                4 -> bankFetch(player, Item(Items.MAHOGANY_PLANK_8782))
                5 -> options("Soft clay", "Limestone bricks", "Steel bars", "Cloth", "More...").also { stage++ }
            }
            61 -> when (buttonId) {
                1 -> bankFetch(player, Item(Items.SOFT_CLAY_1761))
                2 -> bankFetch(player, Item(Items.LIMESTONE_BRICK_3420))
                3 -> bankFetch(player, Item(Items.STEEL_BAR_2353))
                4 -> bankFetch(player, Item(Items.BOLT_OF_CLOTH_8790))
                5 -> options("Gold leaves", "Marble blocks", "Magic stones").also { stage++ }
            }
            62 -> when (buttonId) {
                1 -> bankFetch(player, Item(Items.GOLD_LEAF_4692))
                2 -> bankFetch(player, Item(Items.MARBLE_BLOCK_8786))
                3 -> bankFetch(player, Item(Items.MAGIC_STONE_8788))
            }
            // Firing servant.
            75 -> npc(expression, fontColor + "Very well. I will return to the Guild of the Servants", fontColor + "in Ardougne if you wish to re-hire me.").also { stage++ }
            76 -> {
                end()
                servant.item.amount = 0
                servant.uses = 0
                servant.clear()
                servant.location = Location(0, 0)
                setVarbit(player, Vars.VARBIT_POH_SERVANT_HIRED_2190, 0, true)
                manager.servant = null
            }
            100 -> {
                end()
            }
            // Sawmill run.
            110 -> {
                end()
                sawmillRun(player, logs)
            }
            // Deliver items to player.
            150 -> {
                if (servant.item == null) { end(); return true }
                var amtLeft = servant.item.amount
                var flag = false

                if (amtLeft < 1 || servant.item == null) {
                    npc(expression, fontColor + "I don't have any items left.").also { stage = 100 }
                    return true
                }

                if (amtLeft > freeSlots(player)) {
                    amtLeft = freeSlots(player)
                    flag = true
                }
                servant.item.amount -= amtLeft
                player.inventory.add(Item(servant.item.id, amtLeft))
                if (flag) {
                    npc(expression, fontColor + "I still have " + fontColor + servant.item.amount + fontColor + " left for you to take from me.").also { stage = 100 }
                } else {
                    end()
                }
            }
            // "What can you do?"
            200 -> when (type?.ordinal) {
                1 -> npcl(FaceAnim.HALF_GUILTY, "I'm a great cook, me! I used to work with a rat-catcher, I used to cook for him.").also { stage++ }
                2 -> npcl(FaceAnim.HALF_GUILTY, "Well, I can, um, I can cook meals and make tea and everything, and I can even take things to and from the bank for you.").also { stage += 2 }
                3 -> npcl(FaceAnim.HALF_GUILTY, "I, sir, am the finest cook in all ${GameWorld.settings!!.name}!").also { stage += 3 }
                4 -> npcl(FaceAnim.HALF_GUILTY, "I can fulfill all sir's domestic service needs with efficiency and impeccable manners.").also { stage += 4 }
                5 -> npcl(FaceAnim.OLD_NORMAL, DARK_BLUE + "I have learned my trade under the leash of some of the harshest masters of the Demon Dimensions.").also { stage += 5 }
            }
            201 -> npcl(FaceAnim.HALF_GUILTY, "There's a dozen different ways you can cook a rat!").also { stage = 0 }
            202 -> npcl(FaceAnim.HALF_GUILTY, "I won't make any mistakes this time and everything will be fine!").also { stage = 0 }
            203 -> npcl(FaceAnim.HALF_GUILTY, "I can also make good time going to the bank or the sawmill.").also { stage = 0 }
            204 -> npcl(FaceAnim.HALF_GUILTY, "I hate to boast, but I can say with confidence that no mortal can make trips to the bank or sawmill faster than I!").also { stage = 0 }
            205 -> npcl(FaceAnim.OLD_NORMAL, DARK_BLUE + "I can cook to satisfy the most infernal stomachs, and fly on wings of flame to deposit thine items in the bank or bring planks from the sawmill in seconds.").also { stage = 0 }

            // "Tell me about your previous jobs."
            206 -> when (type?.ordinal) {
                1 -> npcl(FaceAnim.HALF_GUILTY, "Well, city warder Bravek once threw a chair at me and yelled at me to get him a hangover cure. So I made it and I think it worked, 'cause then he threw another chair at me and that one hit!").also { stage++ }
                2 -> npcl(FaceAnim.HALF_GUILTY, "Oh! Oh! I, well, I, er. It wasn't really my fault, I mean, it was, but not really. I mean, how was I to know that that plate was so valuable?").also { stage += 3 }
                3 -> npcl(FaceAnim.HALF_GUILTY, "I used to be the cook for the old Duke of Lumbridge. Visiting dignitaries praised me for my fine banquets!").also { stage += 6 }
                4 -> npcl(FaceAnim.HALF_GUILTY, "From a humble beginning as a dish-washer I have worked my way up through the ranks of domestic service in the households of nobles from Varrock and Ardougne.").also { stage += 8 }
                5 -> npcl(FaceAnim.OLD_NORMAL, DARK_BLUE + "For millennia I have served and waited on the mighty Demon Lords of the Infernal Dimensions.").also { stage += 10 }
            }

            207 -> playerl(FaceAnim.HALF_GUILTY, "So you've been in West Ardougne?").also { stage++ }
            208 -> npcl(FaceAnim.HALF_GUILTY, "Yeah but I ain't got the plague or nuthin'! Honest, guv!").also { stage = 0 }

            209 -> npcl(FaceAnim.HALF_GUILTY, "It was just lying around and I don't know art, it just looked like a pretty pattern and I just had to use it because there weren't enough plates.").also { stage++ }
            210 -> npcl(FaceAnim.HALF_GUILTY, "I just had to use it because there weren't enough plates. And no one had told me that Dennis was off sick and hadn't fed the dogs so I wasn't expecting them to jump up at me when I was carrying the food and it, it").also { stage++ }
            211 -> npcl(FaceAnim.HALF_GUILTY, "went all over the place, gravy and potatoes and bits of fine porcelain and I'm so sorry. It won't happen again, I promise.").also { stage = 0 }

            212 -> npcl(FaceAnim.HALF_GUILTY, "But then someone found a rule that said that only one family could hold that post.").also { stage++ }
            213 -> npcl(FaceAnim.HALF_GUILTY, "Overnight I was fired and replaced by some fool who can't even bake a cake without help!").also { stage = 0 }

            214 -> npcl(FaceAnim.HALF_GUILTY, "As a life-long servant I have naturally suppressed any personality of my own and trained myself never to use the second person when talking to a superior.").also { stage++ }
            215 -> npcl(FaceAnim.HALF_GUILTY, "I have usually worked in the large households of the aristocracy, but now that such a large number of private persons are building their own houses I decided to offer them my services.").also { stage = 0 }

            216 -> npcl(FaceAnim.OLD_NORMAL, DARK_BLUE + "I began as a humble footman in the household of Lord Thammaron, and for several centuries I was the private valet to Delrith.").also { stage++ }
            217 -> npcl(FaceAnim.OLD_NORMAL, DARK_BLUE + "I have also worked in the Grim Underworld, escorting the souls of the dead to their final abodes. But the incessant shadows and hellfire wary me, so I have come to serve mortal masters in the realms of light.").also { stage = 0 }
        }

        return true
    }

    /**
     * Checks if the player meets the requirements for a servant interaction.
     *
     * @param player The player attempting the action.
     * @param item Optional item involved in the action.
     * @param sawmill True if the check is for sawmill tasks; false otherwise.
     * @return True if requirements are met, false otherwise.
     */
    private fun requirements(player: Player?, item: Item?, sawmill: Boolean): Boolean {
        val manager = player!!.houseManager
        val servant = manager.servant
        val butler = servant.type.ordinal == 5
        val expression = if (butler) FaceAnim.OLD_DEFAULT else FaceAnim.HALF_GUILTY
        val fontColor = if (butler) DARK_BLUE else BLACK
        if (!sawmill && freeSlots(player) < 1) {
            npc(expression, fontColor + "You don't have any space in your inventory.").also { stage = 100 }
            return false
        }
        if (servant.uses >= 8) {
            player.sendMessage("<col=CC0000>The servant has left your service due to a lack of payment.</col>")
            servant.item.amount = 0
            servant.uses = 0
            servant.clear()
            servant.location = Location(0, 0)
            manager.servant = null
            end()
            return false
        }
        if (servant.item.amount > 0) {
            npc(expression, fontColor + "You can't send me off again, I'm still holding some of", fontColor + "your previous items.")
            return false
        }
        return true
    }

    /**
     * Handles sending logs to the sawmill via the servant.
     *
     * @param player The player requesting the sawmill service.
     * @param item The item (log) to be sent.
     */
    private fun sawmillRun(player: Player, item: Item?) {
        val manager = player.houseManager
        val servant = manager.servant
        val type = manager.servant.type
        val expression = if (type == ServantType.DEMON_BUTLER) FaceAnim.OLD_DEFAULT else FaceAnim.HALF_GUILTY
        val fontColor = if (type == ServantType.DEMON_BUTLER) DARK_BLUE else BLACK
        if (item == null || !requirements(player, item, true)) {
            return
        }
        if (type == ServantType.MAID || type == ServantType.RICK) {
            npc(expression,fontColor + "I am unable to take planks to the sawmill.")
            return
        }
        var amt = player.inventory.getAmount(item)
        if (amt < 1) {
            npc(expression,fontColor + "You don't have any more of that type of log.")
            return
        }
        for (plank in PlankType.values()) {
            if (plank.log == item.id) {
                if (amt > type.capacity) {
                    amt = type.capacity
                }
                if (!player.inventory.contains(Items.COINS_995, plank.price * amt)) {
                    npc(expression,
                        fontColor + "You don't have enough coins for me to do that.",
                        fontColor + "I can hold " + fontColor + type.capacity + fontColor + " logs and each of this type of log",
                        fontColor + "costs " + fontColor + plank.price + fontColor + " coins each to convert into plank form.",
                    )
                    return
                }
                end()
                if (player.inventory.remove(Item(item.id, amt)) && player.inventory.remove(Item(Items.COINS_995, amt * plank.price),)) {
                    manager.servant.item = Item(plank.plank, amt)
                    servant.isInvisible = true
                    servant.locks.lockMovement(100)
                    Pulser.submit(
                        object : Pulse((type.timer / 0.6).toInt()) {
                            override fun pulse(): Boolean {
                                servant.isInvisible = false
                                servant.locks.unlockMovement()
                                servant.setAttribute(lastFetch, Item(item.id, 1))
                                servant.setAttribute(lastFetchType, "sawmill")
                                interpreter.open(servant.id, servant)
                                return true
                            }
                        },
                    )
                }
                break
            }
        }
    }

    /**
     * Handles fetching items from the bank via the servant.
     *
     * @param player The player requesting the fetch.
     * @param item The item to fetch from the bank.
     */
    private fun bankFetch(player: Player?, item: Item?) {
        val manager = player!!.houseManager
        val servant = manager.servant
        val type = manager.servant.type
        val expression = if (type == ServantType.DEMON_BUTLER) FaceAnim.OLD_DEFAULT else FaceAnim.HALF_GUILTY
        val fontColor = if (type == ServantType.DEMON_BUTLER) DARK_BLUE else BLACK
        if (item == null || !requirements(player, item, false)) {
            return
        }
        if (!inBank(player, item.id)) {
            npc(expression,fontColor + "You don't seem to have any of those in the bank.",).also { stage = 100 }
            return
        }
        end()
        servant.isInvisible = true
        servant.locks.lockMovement(100)
        Pulser.submit(
            object : Pulse((type.timer / 0.6).toInt()) {
                override fun pulse(): Boolean {
                    if (player.houseManager.houseRegion != player.viewport.region) {
                        return true
                    }
                    var amt = player.bank.getAmount(item.id)
                    if (amt < 1) {
                        return true
                    }
                    if (amt > type.capacity) {
                        amt = type.capacity
                    }
                    servant.isInvisible = false
                    servant.locks.unlockMovement()
                    val fetch = Item(item.id, amt)
                    if (player.bank.remove(fetch)) {
                        manager.servant.item = fetch
                        interpreter.open(servant.id, servant)
                    }
                    servant.setAttribute(lastFetch, Item(fetch.id, 1))
                    servant.setAttribute(lastFetchType, "bank")
                    manager.servant.uses += 1
                    follow(player, servant)
                    return true
                }
            },
        )
    }

    /**
     * Makes a servant follow a player.
     *
     * @param player The player to follow.
     * @param npc The NPC that will follow the player.
     */
    private fun follow(player: Player, npc: NPC) {
        npc.pulseManager.run(
            object : MovementPulse(npc, player, Pathfinder.SMART) {
                override fun pulse(): Boolean = false
            },
            PulseType.STANDARD,
        )
    }

    private fun isFollowing(npc: NPC): Boolean {
        return npc.pulseManager.hasPulseRunning()
    }

    fun getShelfLevel(room: Room?): Int {
        return when {
            room?.isBuilt(BuildHotspot.SHELVES_2) == true -> 2
            room?.isBuilt(BuildHotspot.SHELVES) == true -> 1
            else -> 0
        }
    }

    override fun newInstance(player: Player?): Dialogue = HouseServantDialogue(player)

    override fun getIds(): IntArray =
        intArrayOf(
            NPCs.RICK_4235,
            NPCs.RICK_4236,
            NPCs.MAID_4237,
            NPCs.MAID_4238,
            NPCs.COOK_4239,
            NPCs.COOK_4240,
            NPCs.BUTLER_4241,
            NPCs.BUTLER_4242,
            NPCs.DEMON_BUTLER_4243,
            NPCs.DEMON_BUTLER_4244
        )

}
