package content.region.misthalin.varrock.quest.dslay.dialogue

import content.region.misthalin.varrock.quest.dslay.plugin.DemonSlayerUtils
import core.api.*
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.dialogue.IfTopic
import core.game.dialogue.Topic
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.node.scenery.Scenery
import core.game.node.scenery.SceneryBuilder
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.tools.END_DIALOGUE
import shared.consts.Animations
import shared.consts.Items
import shared.consts.NPCs
import shared.consts.Quests

class TraibornDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npc(FaceAnim.HALF_GUILTY, "Ello young thingummywut.")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        val questStage = getQuestStage(player, Quests.DEMON_SLAYER)
        val talkAgain = player.getAttribute("demon-slayer:traiborn", false)
        val hasBones = player.inventory.containsItem(BONES[0]) || player.inventory.containsItem(BONES[1])
        val hasKey = hasAnItem(player, DemonSlayerUtils.THIRD_KEY.id).container != null
        val receivedKey = player.getAttribute("demon-slayer:third-key", false)

        when (stage) {
            0 -> if (hasBones || talkAgain) {
                npc("How are you doing finding bones?").also { stage = 12 }
            } else if (receivedKey && hasKey && questStage < 100) {
                npcl(FaceAnim.THINKING, "Don't you have somewhere to be, young thingummywut? You still have that key you asked me for.").also { stage = 57 }
            } else if (!hasKey && receivedKey) {
                player("I've lost the key you gave to me.").also { stage = 21 }
            } else {
                showTopics(
                    Topic("What's a thingummywut?", 23),
                    Topic("Teach me to be a mighty powerful wizard.", 33),
                    IfTopic("I need to get a key given to you by Sir Prysin.", 1, questStage == 20 && !hasKey)
                )
            }
            1 -> npc("Sir Prysin? Who's that? What would I want his key", "for?").also { stage++ }
            2 -> showTopics(
                Topic("He told me you were looking after it for him.", 37),
                Topic("He's one of the King's knights.", 41),
                Topic("Well, have you got any keys knocking around?", 3),
                Topic("Talk about something else.", 0),
            )
            3 -> npc("Now you come to mention it, yes I do have a key. It's", "in my special closet of valuable stuff. Now how do I get", "into that?").also { stage++ }
            4 -> npc("I sealed it using one of my magic rituals. So it would", "makes sense that another ritual would open it again.").also { stage++ }
            5 -> player("So do you know what ritual to use?").also { stage++ }
            6 -> npc("Let me think a second.").also { stage++ }
            7 -> npc("Yes a simple drazier style ritual should suffice. Hmm,", "main problem with that is I'll need 25 sets of bones.", "Now where am I going to get hold of something like", "that?").also { stage++ }
            8 -> showTopics(
                Topic("Hmm, that's too bad. I really need that key.", 9),
                Topic("I'll get the bones for you.", 10),
            )
            9  -> npc("Ah well, sorry I couldn't be any more help.").also { stage = END_DIALOGUE }
            10 -> npc("Ooh that would be very good of you.").also { stage++ }
            11 -> player("Ok I'll speak to you when I've got some bones.").also {
                end()
                setAttribute(player, "/save:demon-slayer:traiborn", true)
            }
            12 -> {
                val bonesInInventory = BONES.sumOf { amountInInventory(player, it.id) }
                if (bonesInInventory == 0) {
                    player("I don't have any bones yet.")
                    stage = 18
                } else {
                    val submitted = player.getAttribute(TOTAL_BONES, 0) as Int
                    val stillNeeded = TOTAL_BONES_NEEDED - submitted

                    var given = 0
                    BONES.forEach { bone ->
                        val available = amountInInventory(player, bone.id)
                        val toRemove = minOf(available, stillNeeded - given)
                        if (toRemove > 0) {
                            player.inventory.remove(Item(bone.id, toRemove))
                            given += toRemove
                        }
                    }

                    player.setAttribute("/save:$TOTAL_BONES", submitted + given)
                    player("I have some bones.")
                    stage = 13
                }
            }

            13 -> npc("Give 'em here then.").also { stage++ }

            14 -> {
                val given = player.getAttribute(TOTAL_BONES, 0)
                val sets = if (given == 1) "set" else "sets"
                sendItemDialogue(player, BONES[0], "You give Traiborn $given $sets of bones.")
                stage++
            }

            15 -> {
                val given = player.getAttribute(TOTAL_BONES, 0)
                val remaining = TOTAL_BONES_NEEDED - given

                if (remaining > 0) {
                    npc("I still need $remaining more.")
                    stage = 56
                } else {
                    removeAttribute(player, TOTAL_BONES)
                    npc("Hurrah! That's all $TOTAL_BONES_NEEDED sets of bones.")
                    stage = 16
                }
            }
            16 -> {
                npc.animate(Animation(Animations.TRAIBORN_SUMMON_CABINET_4602))
                npc("Wings of dark and colour too,", "Spreading in the morning dew;", "Locked away I have a key;", "Return it now, please, unto me.")
                stage++
            }
            17 -> {
                val scenery =
                    Scenery(shared.consts.Scenery.TRAIBORN_WARDROBE_17434, Location.create(3113, 3161, 1), 11, 1)
                SceneryBuilder.add(scenery)
                npc.faceLocation(scenery.location)
                npc.animate(ANIMATION)
                if (player.inventory.remove(BONES[0]) || player.inventory.remove(BONES[1])) {
                    removeAttribute(player, "demon-slayer:traiborn")
                    player.inventory.add(DemonSlayerUtils.THIRD_KEY)
                    interpreter.sendItemMessage(DemonSlayerUtils.THIRD_KEY.id, "Traiborn hands you a key.")
                    setAttribute(player, "/save:demon-slayer:third-key", true)
                    stage = 19
                } else {
                    npcl(FaceAnim.NEUTRAL, "I still need 25 sets of bones.")
                    stage = END_DIALOGUE
                    return true
                }
                Pulser.submit(object : Pulse(1) {
                    var counter = 0
                    override fun pulse(): Boolean {
                        when (counter++) {
                            5 -> npc.face(player)
                            7 -> {
                                SceneryBuilder.remove(scenery)
                                return true
                            }
                        }
                        return false
                    }
                })
            }
            18 -> npcl(FaceAnim.NEUTRAL, "Nevermind, keep working on it.").also { stage = END_DIALOGUE }
            19 -> player("Thank you very much.").also { stage++ }
            20 -> npcl(FaceAnim.NEUTRAL, "Not a problem for a friend of Sir What's-his-face.").also { stage = END_DIALOGUE }
            21 -> npcl(FaceAnim.NEUTRAL, "Yes I know, it was returned to me.").also { stage++ }
            22 -> npcl(FaceAnim.HALF_GUILTY, "If you want it back you're going to have to collect another 25 sets of bones.").also {
                setAttribute(player, "/save:demon-slayer:traiborn", true)
                removeAttribute(player, "demon-slayer:third-key")
                stage = END_DIALOGUE
            }
            23 -> npcl(FaceAnim.HALF_GUILTY, "A thingummywut? Where? Where?").also { stage++ }
            24 -> npc(FaceAnim.ANNOYED, "Those pesky thingummywuts. They get everywhere.", "They leave a terrible mess too.").also { stage++ }
            25 -> showTopics(
                Topic("Err you just called me a thingummywut.", 28),
                Topic("Tell me what they look like and I'll mash 'em.", 26)
            )
            26 -> npcl(FaceAnim.LAUGH, "Don't be ridiculous. No-one has ever seen one.").also { stage++ }
            27 -> npc(FaceAnim.NEUTRAL, "They're invisible, or a myth, or a figment of my", "imagination. Can't remember which right now.").also { stage = END_DIALOGUE }
            28 -> npc(FaceAnim.ASKING, "You're a thingummywut? I've never seen one up close", "before. They said I was mad!").also { stage++ }
            29 -> npc(FaceAnim.HAPPY, "Now you are my proof! There ARE thingummywuts in", "this tower. Now where can I find a cage big enough to", "keep you?").also { stage++ }
            30 -> showTopics(
                Topic("Err I'd better be off really.", 31),
                Topic("They're right, you are mad.", 32)
            )
            31 -> npc(FaceAnim.HALF_GUILTY, "Oh ok, have a good time, and watch out for sheep!", "They're more cunning than they look.").also { stage = END_DIALOGUE }
            32 -> npc(FaceAnim.HALF_GUILTY, "That's a pity. I thought maybe they were winding me", "up.").also { stage = END_DIALOGUE }
            33 -> npc(FaceAnim.HALF_THINKING, "Wizard eh? You don't want any truck with that sort.", "They're not to be trusted. That's what I've heard", "anyways.").also { stage++ }
            34 -> showTopics(
                Topic("So aren't you a wizard?", 35),
                Topic("Oh I'd better stop talking to you then.", 36)
            )
            35 -> npc(FaceAnim.ANNOYED, "How dare you? Of course I'm a wizard. Now don't be", "so cheeky or I'll turn you into a frog.").also { stage = END_DIALOGUE }
            36 -> npcl(FaceAnim.FRIENDLY, "Cheerio then. It was nice chatting to you.").also { stage = END_DIALOGUE }
            37 -> npcl(FaceAnim.HALF_THINKING, "That wasn't very clever of him. I'd lose my head if it wasn't screwed on.").also { stage++ }
            38 -> npcl(FaceAnim.NEUTRAL, "Go and tell him to find someone else to look after his valuables in future.").also { stage++ }
            39 -> showTopics(
                Topic("Okay, I'll go and tell him that.", 31),
                Topic("Err I'd better be off really.", 40),
                Topic("Well, have you got any keys knocking around?", 3)
            )
            40 -> npcl(FaceAnim.NEUTRAL, "Oh ok, have a good time, and watch out for sheep! They're more cunning than they look.").also { stage = END_DIALOGUE }
            41 -> npc(FaceAnim.NEUTRAL, "Say, I remember one of the King's knights. He had nice shoes...").also { stage++ }
            42 -> npcl(FaceAnim.ASKING, "...and didn't like my homemade spinach rolls.").also { stage++ }
            43 -> npcl(FaceAnim.HALF_ASKING, "Would you like a spinach roll?").also { stage++ }
            44 -> showTopics(
                Topic("Yes, please.", 31),
                Topic("Err I'd better be off really.", 40),
                Topic("Just tell me if you have the key.", 47)
            )
            45 -> sendItemDialogue(player, Items.SPINACH_ROLL_1969, "Traiborn digs around in the pockets of his robes. After a few moments he triumphantly presents you with a spinach roll.").also { stage++ }
            46 -> playerl(FaceAnim.HAPPY, "Thank you very much.").also { stage = END_DIALOGUE }
            47 -> npcl(FaceAnim.THINKING, "The key? The key to what?").also { stage++ }
            48 -> npcl(FaceAnim.HALF_THINKING, "There's more than one key in the world don't you know? Would be a bit odd if there was only the one.").also { stage++ }
            49 -> showTopics(
                Topic("It's the key to get a sword called Silverlight.", 50),
                Topic("You've lost it, haven't you?", 54),
            )
            50 -> npcl(FaceAnim.HALF_ASKING, "Silverlight? Never heard of that. Sounds a good name for a ship. Are you sure it's not the name of a ship rather than a sword?").also { stage++ }
            51 -> showTopics(
                Topic("Yeah, pretty sure.", 52),
                Topic("Err I'd better be off really.", 40),
                Topic("Just tell me if you have the key.", 47)
            )
            52 -> npc("That's a pity, waste of a name.").also { stage++ }
            53 -> showTopics(
                Topic("Err I'd better be off really.", 40),
                Topic("Well, have you got any keys knocking around?", 3)
            )
            54 -> npcl(FaceAnim.HALF_THINKING, "Me? Lose things? That's a nasty accusation.").also { stage++ }
            55 -> playerl(FaceAnim.HALF_ASKING, "Well, have you got any keys knocking around?").also { stage = 4 }
            56 -> playerl(FaceAnim.FRIENDLY, "Ok, I'll keep looking.").also { stage = END_DIALOGUE }
            57 -> playerl(FaceAnim.FRIENDLY, "You're right. I've got a demon to slay.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.TRAIBORN_881)

    companion object {
        private val BONES = arrayOf(Item(Items.BONES_526, 25), Item(Items.BONES_2530, 25))
        private val ANIMATION = Animation(Animations.OPEN_CHEST_536)
        private const val TOTAL_BONES = "demon-slayer:traiborn:bones"
        private const val TOTAL_BONES_NEEDED = 25
    }

}
