package content.region.karamja.quest.totem.dialogue

import content.global.travel.EssenceTeleport
import core.api.*
import core.game.dialogue.Dialogue
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.dialogue.Topic
import core.game.interaction.QueueStrength
import core.game.node.entity.impl.Projectile
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.TeleportManager
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.*

@Initializable
class CrompertyDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if(inInventory(player, Items.HAND_11763) && getVarbit(player, Vars.VARBIT_QUEST_BACK_TO_MY_ROOTS_PROGRESS_4055) >= 20) {
            playerl(FaceAnim.FRIENDLY, "You should have your parcel now, I solved the problem at the R.P.D.T... although it was rather... unpleasant.")
            stage = 39
            return true
        }
        if(getQuestStage(player, Quests.BACK_TO_MY_ROOTS) >= 1) {
            showTopics(
                Topic("Talk about Back to my Roots", 27, true),
                Topic("Talk about something else", 0, true)
            )
        } else {
            npcl(FaceAnim.HAPPY, "Hello ${player.name}, I'm Cromperty. Sedridor has told me about you. As a wizard and an inventor he has aided me in my great invention!")
            stage = 1
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> npcl(FaceAnim.HAPPY, "Hello ${player.name}, I'm Cromperty. Sedridor has told me about you. As a wizard and an inventor he has aided me in my great invention!").also { stage++ }
            1 -> options("Two jobs? that's got to be tough", "So what have you invented?", "Can you teleport me to the Rune Essence?").also { stage++ }
            2 -> when (buttonId) {
                1 -> playerl(FaceAnim.HAPPY, "Two jobs? That's got to be tough.").also { stage = 4 }
                2 -> playerl(FaceAnim.ASKING, "So, what have you invented?").also { stage = 8 }
                3 -> playerl(FaceAnim.HAPPY, "Can you teleport me to the Rune Essence?").also {
                    if (isQuestComplete(player, Quests.RUNE_MYSTERIES)) {
                        end()
                        EssenceTeleport.teleport(npc!!, player)
                    } else {
                        stage++
                    }
                }
            }
            3 -> npcl(FaceAnim.THINKING, "I have no idea what you're talking about.").also { stage = 1000 }
            4 -> npcl(FaceAnim.HAPPY, "Not when you combine them it isn't! Invent MAGIC things!").also { stage++ }
            5 -> options("So what have you invented?", "Well I shall leave you to your inventing").also { stage++ }
            6 -> when (buttonId) {
                1 -> playerl(FaceAnim.ASKING, "So, what have you invented?").also { stage = 8 }
                2 -> playerl(FaceAnim.HAPPY, "Well I shall leave you to your inventing").also { stage++ }
            }
            7 -> npcl(FaceAnim.HAPPY, "Thanks for dropping by! Stop again anytime!").also { stage = 1000 }
            8 -> npcl(FaceAnim.HAPPY, "Ah! My latest invention is my patent pending teleportation block! It emits a low level magical signal,").also { stage++ }
            9 -> npcl(FaceAnim.HAPPY, "that will allow me to locate it anywhere in the world, and teleport anything directly to it! I hope to revolutionize the entire teleportation system!").also { stage++ }
            10 -> npcl(FaceAnim.HAPPY, "Don't you think I'm great? Uh, I mean it's great?").also { stage++ }
            11 -> options("So where is the other block?", "Can I be teleported please?").also { stage++ }
            12 -> when (buttonId) {
                1 -> playerl(FaceAnim.ASKING, "So where is the other block?").also { stage = 13 }
                2 -> playerl(FaceAnim.ASKING, "Can I be teleported please?").also { stage = 19 }
            }
            13 -> npcl(FaceAnim.THINKING, "Well...Hmm. I would guess somewhere between here and the Wizards' Tower in Misthalin.").also { stage++ }
            14 -> npcl(FaceAnim.HAPPY, "All I know is that it hasn't got there yet as the wizards there would have contacted me.").also { stage++ }
            15 -> npcl(FaceAnim.THINKING, "I'm using the GPDT for delivery. They assured me it would be delivered promptly.").also { stage++ }
            16 -> playerl(FaceAnim.ASKING, "Who are the GPDT?").also { stage++ }
            17 -> npcl(FaceAnim.HAPPY, "The ${GameWorld.settings?.name} Parcel Delivery Team. They come very highly recommended.").also { stage++ }
            18 -> npcl(FaceAnim.HAPPY, "Their motto is: \"We aim to deliver your stuff at some point after you have paid us!\"").also { stage = 1000 }
            19 -> npcl(FaceAnim.HAPPY, "By all means! I'm afraid I can't give you any specifics as to where you will come out however. Presumably wherever the other block is located.").also { stage++ }
            20 -> options("Yes, that sounds good. Teleport me!", "That sounds dangerous. Leave me here.").also { stage++ }
            21 -> when (buttonId) {
                1 -> playerl(FaceAnim.HAPPY, "Yes, that sounds good. Teleport me!").also { stage = 23 }
                2 -> playerl(FaceAnim.THINKING, "That sounds dangerous. Leave me here.").also { stage++ }
            }
            22 -> npcl(FaceAnim.HAPPY, "As you wish.").also { stage = 1000 }
            23 -> npcl(FaceAnim.HAPPY, "Okey dokey! Ready?").also { stage++ }
            24 -> {
                if (isQuestInProgress(player, Quests.TRIBAL_TOTEM, 1, 49)) {
                    npcl(FaceAnim.HAPPY, "Okay, I got a signal. Get ready!").also { stage = 26 }
                } else {
                    npcl(FaceAnim.THINKING, "Hmmm... that's odd... I can't seem to get a signal...").also { stage++ }
                }
            }
            25 -> playerl(FaceAnim.SAD, "Oh well, never mind.").also { stage = 1000 }
            26 -> {
                questTeleport(player, npc)
                end()
            }
            27 -> if(getQuestStage(player, Quests.BACK_TO_MY_ROOTS) == 4) {
                if(!inInventory(player, Items.POT_LID_4440)) {
                    npc("Have you made the pot lid yet?").also { stage = 41 }
                } else {
                    npcl(FaceAnim.FRIENDLY, "Ah, brilliant, I see you have completed a fine work of art.").also { stage = 45 }
                }
            } else {
                player(FaceAnim.HALF_ASKING, "Horacio tells me that you may have something that", "could help me preserve a vine cutting?").also { stage++ }
            }
            28 -> npc(FaceAnim.FRIENDLY, "Ah yes, my very latest invention, but it's still in", "experimental stages at the moment, and quite fragile.").also { stage++ }
            29 -> player(FaceAnim.HALF_ASKING, "So what is it and can I have one?").also { stage++ }
            30 -> npc(FaceAnim.FRIENDLY, "Oh, can't possibly let you have one I'm afraid. All top", "secret and hush-hush.").also { stage++ }
            31 -> player(FaceAnim.HALF_ASKING, "Look, I need it to help Horacio... please?").also { stage++ }
            32 -> npc(FaceAnim.FRIENDLY, "Well... I guess you did test out my teleportation device", "last time, I suppose you might be of some help now.").also { stage++ }
            33 -> player(FaceAnim.HALF_ASKING, "That sounds like you want me to do something.").also { stage++ }
            34 -> npc(FaceAnim.FRIENDLY, "Indeed! You see, my latest rather clever magical", "preserving device requires one more missing part...quite", "a specialist part and it's those terribly lackadaisical", "workers at the R.P.D.T. who refuse to budge. I tell").also { stage++ }
            35 -> npc(FaceAnim.FRIENDLY, "you, they never seem to do any work... except putting", "the kettle on... and another thing...").also { stage++ }
            36 -> player(FaceAnim.HALF_ASKING, "Okay, okay, I'll go see what I can do about it.").also { stage++ }
            37 -> npc(FaceAnim.FRIENDLY, "Oh, would you? I'd be ever so grateful... and might even", "let you use my new magical preservation magic", "thingummy.").also { stage++ }
            38 -> player(FaceAnim.HALF_ASKING, "*sigh* I guess I'd better see what's happening at the", "R.P.D.T. depot then.").also {
                setQuestStage(player, Quests.BACK_TO_MY_ROOTS, 2)
                // Showing smelly table.
                setVarbit(player, Vars.VARBIT_QUEST_BACK_TO_MY_ROOTS_PROGRESS_4055, 10, true)
                stage = 1000
            }
            39 -> npc("Ah, excellent. You are a handy person to have around,", "you know.").also { stage++ }
            40 -> {
                end()
                this.npc.teleporter.send(Location.create(2683, 3326, 0), TeleportManager.TeleportType.RANDOM_EVENT_OLD)
                if(player.location == Location.create(2683, 3326, 0)) player.moveStep()
                queueScript(player, 3,QueueStrength.SOFT) {
                    npc.faceLocation(Location.create(2683, 3325, 0))
                    npc.sendChat("Now, let's see what we have here...", 1)
                    npc.animate(Animation(11141)) // TODO
                    setVarbit(player, Vars.VARBIT_QUEST_BACK_TO_MY_ROOTS_PROGRESS_4055, 25)
                    this.npc.teleporter.send(Location.create(2683, 3327, 0), TeleportManager.TeleportType.RANDOM_EVENT_OLD).also {
                        openDialogue(player, WizardCrompertyDialogue())

                    }
                }

            }

            41 -> playerl(FaceAnim.FRIENDLY, "Not yet, no.").also { stage++ }
            42 -> npcl(FaceAnim.HALF_ASKING, "Well, get cracking then!").also { stage++ }
            43 -> playerl(FaceAnim.FRIENDLY, "I thought you wanted a whole pot lid?").also { stage++ }
            44 -> npcl(FaceAnim.HALF_ASKING, "...!").also { stage = END_DIALOGUE }
            45 -> playerl(FaceAnim.FRIENDLY, "It's a pot lid.").also { stage++ }
            46 -> npcl(FaceAnim.FRIENDLY, "Yes, yes, it is, I hope it fits your pot okay - you do have a pot to go with it, right? You'll need both the pot and the lid to preserve the cutting, and make sure the seal is nice and tight. Now, to find out more about how").also { stage++ }
            47 -> npcl(FaceAnim.FRIENDLY, "to take the cuttings, Horacio tells me to tell you that... err... or is that the other way around?").also { stage++ }
            48 -> playerl(FaceAnim.FRIENDLY, "Just tell me what Horacio said.").also { stage++ }
            49 -> npcl(FaceAnim.FRIENDLY, "I think it was that you were to talk to a nice man called Garth.").also { stage++ }
            50 -> playerl(FaceAnim.FRIENDLY, "Well, that really helps... Runescape's not exactly small, you know.").also { stage++ }
            51 -> npcl(FaceAnim.FRIENDLY, "Erm. Oh, I think he said something about a jungle and that Garth was a farmer.").also { stage++ }
            52 -> playerl(FaceAnim.FRIENDLY, "Ahh, that Garth, on Karamja. Right, I'll go see him then.").also { stage++ }
            53 -> npcl(FaceAnim.FRIENDLY, "Would you like me to telep-").also { stage++ }
            54 -> playerl(FaceAnim.SCARED, "No! I'll walk, thanks!").also { stage++ }
            55 -> npcl(FaceAnim.THINKING, "You can walk on water? How impressive... I'll want to talk to you about that sometime. Perhaps I could manipulate the molecules and make them somehow get friendlier and then I could...").also {
                sendDialogueLines(player, "You think it best to depart before Wizard Cromperty thinks up", "anymore inventions.")
                setQuestStage(player, Quests.BACK_TO_MY_ROOTS, 5)
                stage = END_DIALOGUE
            }
            1000 -> end()
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = CrompertyDialogue(player)

    override fun getIds(): IntArray = intArrayOf(844)

    /**
     * Handles teleport the player to Lord Handelmort mansion
     */
    private fun questTeleport(player: Player, npc: NPC) {
        val LOCATIONS = arrayOf(Location.create(2649, 3272, 0), Location.create(2638, 3321, 0))
        npc.animate(Animation(Animations.ATTACK_437))
        npc.faceTemporary(player, 2)
        npc.graphics(Graphics(shared.consts.Graphics.CURSE_CAST_108))
        player.lock()
        playAudio(player, Sounds.CURSE_ALL_125, 0, 1)
        Projectile.create(npc, player, shared.consts.Graphics.CURSE_PROJECTILE_109).send()
        npc.sendChat("Dipsolum sententa sententi!")
        GameWorld.Pulser.submit(
            object : Pulse(1) {
                var counter = 0
                var delivered = player.questRepository.getStage(Quests.TRIBAL_TOTEM) >= 25

                override fun pulse(): Boolean {
                    when (counter++) {
                        2 -> {
                            if (delivered) {
                                setQuestStage(player, Quests.TRIBAL_TOTEM, 30)
                                player.properties.teleportLocation = LOCATIONS[1]
                            } else {
                                player.properties.teleportLocation = LOCATIONS[0]
                            }
                        }

                        3 -> {
                            player.graphics(Graphics(shared.consts.Graphics.CURSE_IMPACT_110))
                            player.unlock()
                            return true
                        }
                    }
                    return false
                }
            },
        )
    }
}

private class WizardCrompertyDialogue : DialogueFile() {
    override fun handle(componentID: Int, buttonID: Int) {
        npc = NPC(844)
        when(stage) {
            0 -> npc(FaceAnim.NEUTRAL, "Argh! My specialist equipment is broken. Those", "bumbling idiots at the R.P.D.T. have messed up a", "simple job again!").also { stage++ }
            1 -> playerl(FaceAnim.FRIENDLY, "It's a pot lid.").also { stage++ }
            2 -> npcl(FaceAnim.HALF_ASKING, "WHAT? I'll have you know this is specialist magical equipment.").also { stage++ }
            3 -> playerl(FaceAnim.FRIENDLY, "It's still a pot lid.").also { stage++ }
            4 -> npcl(FaceAnim.HALF_ASKING, "Are you implying that I'm lying, young man/woman?").also { stage++ }
            5 -> playerl(FaceAnim.FRIENDLY, "No, not at all, sir... *whispers* I'm implying you're a bit potty.").also { stage++ }
            6 -> npcl(FaceAnim.HALF_ASKING, "I heard that... I will need a new one, do you think you can make it?").also { stage++ }
            7 -> playerl(FaceAnim.FRIENDLY, "Some people make pottery to earn a living, but not me: I just run errands for potty wizards...").also { stage++ }
            8 -> npcl(FaceAnim.HALF_ASKING, "Why, you young whippersnapper! Don't you know that only foolish potters make wisecracks?").also { stage++ }
            9 -> playerl(FaceAnim.FRIENDLY, "Okay, okay, I'll make your pot lid.").also {
                setQuestStage(player!!, Quests.BACK_TO_MY_ROOTS, 4)
                setVarbit(player!!, Vars.VARBIT_QUEST_BACK_TO_MY_ROOTS_PROGRESS_4055, 30, true)
                stage = END_DIALOGUE
            }
        }
    }
}