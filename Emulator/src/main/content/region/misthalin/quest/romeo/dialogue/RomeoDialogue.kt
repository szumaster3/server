package content.region.misthalin.quest.romeo.dialogue

import content.region.misthalin.quest.romeo.cutscene.RomeoAndJulietCutscene
import core.api.quest.setQuestStage
import core.api.quest.updateQuestTab
import core.api.sendItemDialogue
import core.game.activity.ActivityManager
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import core.game.world.map.path.Pathfinder
import core.net.packet.PacketRepository
import core.net.packet.context.CameraContext
import core.net.packet.out.CameraViewPacket
import core.plugin.Initializable
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class RomeoDialogue(
    player: Player? = null,
) : Dialogue(player) {
    private var cutscene: RomeoAndJulietCutscene? = null
    private var phill: NPC? = null

    override fun open(vararg args: Any): Boolean {
        val quest = player.getQuestRepository().getQuest(Quests.ROMEO_JULIET)
        npc = args[0] as NPC
        if (args.size > 1) {
            cutscene = args[1] as RomeoAndJulietCutscene
            npc("This is pretty scary...")
            stage = 740
            return true
        }
        when (quest.getStage(player)) {
            0 -> {
                npc(FaceAnim.HALF_GUILTY, "Juliet. Juliet, where art thou Juliet?", "Have you seen my Juliet?")
                stage = 0
            }

            10 -> {
                npc(FaceAnim.HALF_GUILTY, "Go and speak with Juliet!")
                stage = 457
            }

            20 ->
                if (!player.inventory.contains(755, 1)) {
                    player(FaceAnim.HALF_GUILTY, "Romeo...great news...I've been in touch with Juliet!")
                    stage = 300
                } else {
                    player(
                        FaceAnim.HALF_GUILTY,
                        "Romeo...great news...I've been in touch with Juliet!",
                        "She's written a message for you...",
                    )
                    stage = 400
                }

            30 -> {
                player(FaceAnim.HALF_GUILTY, "Hey again Romeo!")
                stage = 800
            }

            40 -> {
                npc(
                    "Ooh did you manage to survive one of Lather",
                    "Fawrences sermons? I bet not, you were ages! I bet",
                    "you snoozed on the welcome mat just as soon as you",
                    "heard his voice!",
                )
                stage = 236
            }

            50 -> {
                player("Hi Romeo!")
                stage = 326
            }

            60 ->
                if (!player.inventory.contains(756, 1)) {
                    player("Hi Romeo!")
                    stage = 326
                } else {
                    interpreter.sendItemMessage(756, "Romeo spots the Cadava potion.")
                    stage = 350
                }

            70 -> {
                player(
                    "Romeo, it's all set. Juliet has drunk the potion and has",
                    "been taken down into the Crypt...now you just need to",
                    "pop along and collect her.",
                )
                stage = 71
            }

            100 -> {
                npc("I heard Juliet had died. Terrible business.")
                stage = 456
            }
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        val quest = player.getQuestRepository().getQuest(Quests.ROMEO_JULIET)
        when (stage) {
            0 -> {
                options("No sorry. I haven't seen her.", "Perhaps I could help to find her for you?")
                stage = 1
            }

            1 ->
                when (buttonId) {
                    1 -> {
                        player(FaceAnim.HALF_GUILTY, "No sorry. I haven't seen her.")
                        stage = 200
                    }

                    2 -> {
                        player(FaceAnim.HALF_GUILTY, "Perhaps I can help find her for you? What does she", "look like?")
                        stage = 10
                    }
                }

            10 -> {
                npc(FaceAnim.HALF_GUILTY, "Oh would you? That would be great! She has this sort", "of hair...")
                stage = 11
            }

            11 -> {
                player(FaceAnim.HALF_GUILTY, "Hair...check..")
                stage = 12
            }

            12 -> {
                npc(FaceAnim.HALF_GUILTY, "...and she these...great lips...")
                stage = 13
            }

            13 -> {
                player(FaceAnim.HALF_GUILTY, "Lips...right.")
                stage = 14
            }

            14 -> {
                npc(FaceAnim.HALF_GUILTY, "Oh and she has these lovley shoulders as well..")
                stage = 15
            }

            15 -> {
                player(
                    FaceAnim.HALF_GUILTY,
                    "Shoulders...right so she has hair, lips and shoulders...that",
                    "should cut it down a bit.",
                )
                stage = 16
            }

            16 -> {
                npc(
                    FaceAnim.HALF_GUILTY,
                    "Oh yes, Juliet Is very different...please tell her that she",
                    "is the love of my long and that I life to be with her?",
                )
                stage = 17
            }

            17 -> {
                player(
                    FaceAnim.HALF_GUILTY,
                    "What?",
                    "Surely you mean that 'she is the love of your life and",
                    "that you long to be with her?",
                )
                stage = 18
            }

            18 -> {
                npc(
                    FaceAnim.HALF_GUILTY,
                    "Oh yeah...what you said...tell her that, it sounds much",
                    "bettter!",
                    "Oh you're so good at this!",
                )
                stage = 19
            }

            19 -> {
                options(
                    "Yes okay, I'll let her know.",
                    "Sorry Romeo, I've got better things to do right now but",
                    "maybe later?",
                )
                stage = 20
            }

            20 ->
                when (buttonId) {
                    1 -> {
                        player(FaceAnim.HALF_GUILTY, "Yes, ok, I'll let her know.")
                        stage = 100
                    }

                    2 -> {
                        player(
                            FaceAnim.HALF_GUILTY,
                            "Sorry Romeo, I've got better things to do right now but maybe latter?",
                        )
                        stage = 50
                    }

                    3 -> end()
                }

            100 -> {
                npc(FaceAnim.HALF_GUILTY, "Oh great! And tell her that I want to kiss her a give.")
                setQuestStage(player, Quests.ROMEO_JULIET, 10)
                updateQuestTab(player)
                stage = 101
            }

            101 -> {
                player(FaceAnim.HALF_GUILTY, "You mean you want to give her a kiss!")
                stage = 102
            }

            102 -> {
                npc(FaceAnim.HALF_GUILTY, "Oh you're good...you are good!")
                stage = 103
            }

            103 -> {
                npc(
                    FaceAnim.HALF_GUILTY,
                    "I see I've picked a true professional...!",
                )
                stage = 104
            }

            104 -> {
                npc(FaceAnim.HALF_GUILTY, "Ok, thanks.")
                stage = 106
            }

            105 ->
                when (buttonId) {
                    1 -> {}
                    2 -> {}
                    3 -> {
                        npc(FaceAnim.HALF_GUILTY, "Ok, thanks.")
                        stage = 106
                    }
                }

            106 -> end()
            200 -> {
                npc(FaceAnim.HALF_GUILTY, "If you do see her please tell me!")
                stage = 201
            }

            201 -> end()
            230 -> {
                npc(FaceAnim.HALF_GUILTY, "Please, oh please! Tell me where you have seen my juliet.")
                stage = 231
            }

            231 -> {
                player(FaceAnim.HALF_GUILTY, "I found her alone on a balcony!")
                stage = 255
            }

            255 -> end()
            300 -> {
                npc(
                    FaceAnim.HALF_GUILTY,
                    "Oh great! That is great news! Well done...well done...",
                    "what a total success!",
                )
                stage = 301
            }

            301 -> {
                player(FaceAnim.HALF_GUILTY, "Yes, and she gave me a message to give you...")
                stage = 302
            }

            302 -> {
                npc(FaceAnim.HALF_GUILTY, "Ohh great! A message...wow!")
                stage = 303
            }

            303 -> {
                player(FaceAnim.HALF_GUILTY, "Yes!")
                stage = 304
            }

            304 -> {
                npc(FaceAnim.HALF_GUILTY, "A message...oh, I can't wait to read what my dear Juliet", "has to say....")
                stage = 305
            }

            305 -> {
                player(FaceAnim.HALF_GUILTY, "I know...it's exiting isn't it...?")
                stage = 306
            }

            306 -> {
                npc(FaceAnim.HALF_GUILTY, "Yes...yes...")
                stage = 307
            }

            307 -> {
                npc(FaceAnim.HALF_GUILTY, "...")
                stage = 308
            }

            308 -> {
                npc(FaceAnim.OLD_SNEAKY, "You've lost the message haven't you?")
                stage = 309
            }

            309 -> {
                player(FaceAnim.HALF_GUILTY, "Yep, haven't got a clue where it is.")
                stage = 310
            }

            310 -> end()
            400 -> {
                sendItemDialogue(player, Items.MESSAGE_755, "You hand over Juliet's message to Romeo.")
                stage = 401
            }

            401 -> {
                npc("Oh, a message! A message! I've never had a message", "before...")
                stage = 402
            }

            402 -> {
                player("Really?")
                stage = 403
            }

            403 -> {
                npc("No, no, not one!")
                stage = 404
            }

            404 -> {
                npc("Oh, well, except for the occasional court summons.")
                stage = 405
            }

            405 -> {
                npc(
                    "But they're not really 'nice' messages. Not like this one!",
                    "I'm sure that this message will be lovely.",
                )
                stage = 406
            }

            406 -> {
                player("Well are you going to open it or not?")
                stage = 407
            }

            407 -> {
                npc(
                    "Oh yes, yes, of course!",
                    "'Dearest Romeo, I am very pleased that you sent",
                    player.username + " to look for me and to tell me that you still",
                    "hold affliction...', Affliction! She thinks I'm diseased?",
                )
                stage = 408
            }

            408 -> {
                player("'Affection?'")
                stage = 409
            }

            409 -> {
                npc(
                    "Ahh yes...'still hold affection for me. I still feel great",
                    "affection for you, but unfortunately my Father opposes",
                    "our marriage.'",
                )
                stage = 410
            }

            410 -> {
                player("Oh dear...that doesn't sound too good.")
                stage = 411
            }

            411 -> {
                npc("What? '...great affection for you. Father opposes our..")
                stage = 412
            }

            412 -> {
                npc("'...marriage and will...")
                stage = 413
            }

            413 -> {
                npc("...will kill you if he sees you again!'")
                stage = 414
            }

            414 -> {
                player("I have to be honest, it's not getting any better...")
                stage = 415
            }

            415 -> {
                npc("'Our only hope is that Father Lawrence, our long time", "confidant, can help us in some way.'")
                stage = 416
            }

            416 -> {
                sendItemDialogue(player, Items.MESSAGE_755, "Romeo folds the message away.")
                stage = 417
            }

            417 -> {
                npc("Well, that's it then...we haven't got a chance.")
                stage = 418
            }

            418 -> {
                player("What about Father Lawrence?")
                stage = 419
            }

            419 -> {
                npc("...our love is over...the great romance, the life of my", "love...")
                stage = 420
            }

            420 -> {
                player("...or you could speak to Father Lawrence!")
                stage = 421
            }

            421 -> {
                npc(
                    "Oh, my aching, breaking, heart...how useless the situation",
                    "is now...we have no one to turn to...",
                )
                stage = 422
            }

            422 -> {
                quest.setStage(player, 30)
                player.getQuestRepository().syncronizeTab(player)
                player.inventory.remove(Item(755))
                player("FATHER LAWRENCE!")
                stage = 423
            }

            423 -> {
                npc("Father Lawrence?")
                stage = 424
            }

            424 -> {
                npc(
                    "Oh yes, Father Lawrence...he's our long time confidant,",
                    "he might have a solution! yes, yes, you have to go and",
                    "talk to Lather Fawrence for us and ask him if he's got",
                    "any suggestions for our predicament?",
                )
                stage = 425
            }

            425 -> {
                player("Where can I find Father Lawrence?")
                stage = 426
            }

            426 -> {
                npc("Lather Fawrence! Oh he's...")
                stage = 427
            }

            427 -> {
                npc("You know he's not my 'real' Father don't you?")
                stage = 428
            }

            428 -> {
                player("I think I suspected that he wasn't.")
                stage = 429
            }

            429 -> {
                npc(
                    "Well anyway...he tells these song, loring bermons...and",
                    "keep these here Carrockian vitizens snoring in his",
                    "church to the East North.",
                )
                stage = 430
            }

            430 -> {
                options(
                    "How are you?",
                    "Where can I find Father Lawrence?",
                    "Have you heard anything from Juliet?",
                    "Ok, thanks.",
                )
                stage = 431
            }

            431 ->
                when (buttonId) {
                    1 -> {
                        player("How are you?")
                        stage = 500
                    }

                    2 -> {
                        player("Where can I find Father Lawrence?")
                        stage = 530
                    }

                    3 -> {
                        player("Have you heard anything from Juliet?")
                        stage = 560
                    }

                    4 -> {
                        player("Ok, thanks.")
                        stage = 580
                    }
                }

            580 -> end()
            560 -> {
                npc(
                    "Sadly not my friend! And what's worse, her Father has",
                    "threatend to kill me if he sees me. I mean, that seems",
                    "a bit harsh!",
                )
                stage = 561
            }

            561 -> {
                player(
                    "Well, I shouldn't worry too much...you can always run",
                    "away if you see him...",
                )
                stage = 562
            }

            562 -> {
                npc("I just wish I could remember what he looks like! I live", "in fear of every man I see!")
                stage = 563
            }

            563 -> {
                options(
                    "How are you?",
                    "Where can I find Father Lawrence?",
                    "Have you heard anything from Juliet?",
                    "Ok, thanks.",
                )
                stage = 431
            }

            530 -> {
                npc("Lather Fawrence! Oh he's...")
                stage = 531
            }

            531 -> {
                npc("You know he's not my 'real' Father don't you?")
                stage = 532
            }

            532 -> {
                player("I think I suspected that he wasn't.")
                stage = 533
            }

            533 -> {
                npc(
                    "Well anyway...he tells these song, loring bermons...and",
                    "keep these here Carrockian vitizens snoring in his",
                    "church to the East North.",
                )
                stage = 534
            }

            534 -> {
                options(
                    "How are you?",
                    "Where can I find Father Lawrence?",
                    "Have you heard anything from Juliet?",
                    "Ok, thanks.",
                )
                stage = 431
            }

            500 -> {
                npc("Not so good my friend...I miss Judi..., Junie..., Jooopie...")
                stage = 501
            }

            501 -> {
                player("Juliet?")
                stage = 502
            }

            502 -> {
                npc("Juliet! I miss Juliet, terribly?")
                stage = 503
            }

            503 -> {
                player("Hmmm, so I see!")
                stage = 504
            }

            504 -> {
                options(
                    "How are you?",
                    "Where can I find Father Lawrence?",
                    "Have you heard anything from Juliet?",
                    "Ok, thanks.",
                )
                stage = 431
            }

            800 -> {
                options(
                    "How are you?",
                    "Where can I find Father Lawrence?",
                    "Have you heard anything from Juliet?",
                    "Ok, thanks.",
                )
                stage = 431
            }

            236 -> {
                player("Did not!")
                stage = 237
            }

            237 -> {
                npc("Oh, come on, come on, what did he say?")
                stage = 238
            }

            238 -> {
                player("He wants me to go to the Apothercary!")
                stage = 239
            }

            239 -> {
                npc("The Apothecary?")
                stage = 240
            }

            240 -> {
                npc("Oh...is he the one who mixes up all them magical potion-", "ey things?")
                stage = 241
            }

            241 -> {
                player("Yeah, I think so...but the word potion-ey doesn't exist.")
                stage = 242
            }

            242 -> {
                npc("Well, you just used it...so I guess it does exist!")
                stage = 243
            }

            243 -> {
                player("It doesn't matter...do you know where the Apothecary", "is?")
                stage = 244
            }

            244 -> {
                npc("It is Wouth Sest of here and near a sword shop.")
                stage = 245
            }

            245 -> end()
            326 -> {
                npc("Oh hello, have you seen Lather Fawrence?")
                stage = 327
            }

            327 -> {
                player(
                    "Yes, he's given me details of a potion which should help",
                    "resolve this siution. The Apothecary is helping me",
                    "prepare it.",
                )
                stage = 328
            }

            328 -> {
                npc("Oooh, it all sounds horribly complicated?")
                stage = 329
            }

            329 -> {
                npc("All I can say is I'll be glad when Juliet's finally in that", "crypt!")
                stage = 330
            }

            330 -> {
                player("Spoken like a true lover!")
                stage = 331
            }

            331 -> end()
            350 -> {
                npc("Ooohh, that's the potion is it?", "Rather you than me!")
                stage = 351
            }

            351 -> {
                player("I'm not drinking it! It's for Juliet!")
                stage = 352
            }

            352 -> end()
            71 -> {
                npc("Ah right, the potion! Great...")
                stage = 72
            }

            72 -> {
                npc("What potion would that be then?")
                stage = 73
            }

            73 -> {
                player(
                    "The Cadava potion...you know, the one which will make",
                    "her appear dead! She's in the crypt, pop along and claim",
                    "your true love.",
                )
                stage = 74
            }

            74 -> {
                npc("But I'm scared...will you come with me?")
                stage = 75
            }

            75 -> {
                player("Oh, ok...come on! I think I saw the entrance when I", "visited there last...")
                stage = 76
            }

            76 -> {
                end()
                ActivityManager.start(player, "Romeo & Juliet Cutscene", false)
                player.lock()
            }

            456 -> {
                npc("Her cousin and I are getting on well though. Thanks", "for your help.")
                stage = 457
            }

            457 -> end()
            740 -> {
                player("Oh, be quiet...")
                stage = 741
            }

            741 -> {
                player("We're here. Look, Juliet is over there!")
                stage = 742
            }

            742 -> {
                close()
                val ll = cutscene!!.base.transform(21, 36, 0)
                val x = ll.x
                val y = ll.y
                var rot: CameraContext? = null
                var pos: CameraContext? = null
                val height = 450
                val speed = 55
                val other = 1
                pos = CameraContext(player, CameraContext.CameraType.POSITION, x + 3, y + 4, height, other, speed)
                rot = CameraContext(player, CameraContext.CameraType.ROTATION, x - 1, y - 2, height, other, speed)
                PacketRepository.send(CameraViewPacket::class.java, pos)
                PacketRepository.send(CameraViewPacket::class.java, rot)
                Pulser.submit(
                    object : Pulse(5) {
                        override fun pulse(): Boolean {
                            val x = player.location.x
                            val y = player.location.y
                            var rot: CameraContext? = null
                            var pos: CameraContext? = null
                            val height = 450
                            val speed = 100
                            val other = 1
                            pos =
                                CameraContext(
                                    player,
                                    CameraContext.CameraType.POSITION,
                                    x - 5,
                                    y - 4,
                                    height,
                                    other,
                                    speed,
                                )
                            rot =
                                CameraContext(player, CameraContext.CameraType.ROTATION, x + 2, y, height, other, speed)
                            PacketRepository.send(CameraViewPacket::class.java, pos!!)
                            PacketRepository.send(CameraViewPacket::class.java, rot!!)
                            interpreter.sendDialogues(
                                player,
                                FaceAnim.HALF_GUILTY,
                                "You go over to her...and I'll go and wait over here...",
                            )
                            stage = 743
                            return true
                        }
                    },
                )
            }

            743 -> {
                npc("Ohhh, ok then...")
                stage = 744
            }

            744 -> {
                val l = cutscene!!.base.transform(18, 32, 0)
                val x = l.x
                val y = l.y
                val height = 330
                val speed = 100
                val other = 1
                val pos = CameraContext(player, CameraContext.CameraType.POSITION, x, y - 1, height, other, speed)
                val rot = CameraContext(player, CameraContext.CameraType.ROTATION, x + 20, y + 50, height, other, speed)
                PacketRepository.send(CameraViewPacket::class.java, pos)
                PacketRepository.send(CameraViewPacket::class.java, rot)
                close()
                npc.walkingQueue.reset()
                npc.walkingQueue.addPath(cutscene!!.base.transform(19, 35, 0).x, cutscene!!.base.transform(19, 35, 0).y)
                Pulser.submit(
                    object : Pulse(12) {
                        override fun pulse(): Boolean {
                            npc("Hey...Juliet...")
                            stage = 745
                            phill = NPC.create(3325, cutscene!!.base.transform(30, 37, 0))
                            phill!!.init()
                            cutscene!!.npcs.add(phill)
                            val path = Pathfinder.find(phill, cutscene!!.base.transform(20, 35, 0))
                            path.walk(phill)
                            return true
                        }
                    },
                )
            }

            745 -> {
                npc("Juliet....?")
                stage = 746
            }

            746 -> {
                npc("Oh dear...you seem to be dead.")
                stage = 747
            }

            747 -> {
                interpreter.sendDialogues(phill!!, FaceAnim.HALF_GUILTY, "Hi Romeo...I'm Phillipa!")
                stage = 748
            }

            748 -> {
                npc.face(phill)
                npc("Wow! You're a fox!")
                stage = 749
            }

            749 -> {
                interpreter.sendDialogues(
                    phill!!,
                    FaceAnim.HALF_GUILTY,
                    "It's a shame about Juliet...but perhaps we can meet up",
                    "later?",
                )
                stage = 750
            }

            750 -> {
                npc("Who's Juliet?")
                stage = 751
            }

            751 -> {
                end()
                cutscene!!.stop(true)
                quest.finish(player)
            }
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return RomeoDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.ROMEO_639)
    }
}
