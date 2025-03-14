package content.global.skill.construction.servants

import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.world.GameWorld
import core.tools.DARK_BLUE
import org.rs.consts.NPCs

class ServantRickDialogue : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.RICK_4235)
        when (stage) {
            0 -> player("What can you do?").also { stage++ }
            1 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "I'm a great cook, me! I used to work with a rat-catcher, I used to cook for him.",
                ).also { stage++ }

            2 -> npcl(FaceAnim.HALF_GUILTY, "There's a dozen different ways you can cook a rat!").also { stage++ }
            3 -> end()
        }
    }
}

class ServantRickDialogueExtension : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.RICK_4235)
        when (stage) {
            0 -> player("Tell me about your previous jobs.").also { stage++ }
            1 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Well, city warder Bravek once threw a chair at me and yelled at me to get him a hangover cure. So I made it and I think it worked, 'cause then he threw another chair at me and that one hit!",
                ).also { stage++ }

            2 -> playerl(FaceAnim.HALF_GUILTY, "So you've been in West Ardougne?").also { stage++ }
            3 -> npcl(FaceAnim.HALF_GUILTY, "Yeah but I ain't got the plague or nuthin'! Honest, guv!").also { stage++ }
            4 -> end()
        }
    }
}

class ServantMaidDialogue : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.MAID_4237)
        when (stage) {
            0 -> player("What can you do?").also { stage++ }
            1 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Well, I can, um, I can cook meals and make tea and everything, and I can even take things to and from the bank for you.",
                ).also { stage++ }

            2 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "I won't make any mistakes this time and everything will be fine!",
                ).also { stage++ }

            3 -> end()
        }
    }
}

class ServantMaidDialogueExtension : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.MAID_4237)
        when (stage) {
            0 -> player("Tell me about your previous jobs.").also { stage++ }
            1 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Oh! Oh! I, well, I, er. It wasn't really my fault, I mean, it was, but not really. I mean, how was I to know that that plate was so valuable?",
                ).also { stage++ }

            2 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "It was just lying around and I don't know art, it just looked like a pretty pattern and I just had to use it because there weren't enough plates.",
                ).also { stage++ }

            3 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "And no one had told me that Dennis was off sick and hadn't fed the dogs so I wasn't expecting them to jump up at me when I was carrying the food and it, it went all over the place, gravy and potatoes and bits of fine porcelain and I'm so sorry.",
                ).also { stage++ }

            4 -> npcl(FaceAnim.HALF_GUILTY, "It won't happen again, I promise.").also { stage++ }
            5 -> end()
        }
    }
}

class ServantCookDialogue : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.COOK_4239)
        when (stage) {
            0 -> player("What can you do?").also { stage++ }
            1 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "I, sir, am the finest cook in all ${GameWorld.settings!!.name}!",
                ).also { stage++ }

            2 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "I can also make good time going to the bank or the sawmill.",
                ).also { stage++ }

            3 -> end()
        }
    }
}

class ServantCookDialogueExtension : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.COOK_4239)
        when (stage) {
            0 -> player("Tell me about your previous jobs.").also { stage++ }
            1 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "I used to be the cook for the old Duke of Lumbridge. Visiting dignitaries praised me for my fine banquets!",
                ).also { stage++ }

            2 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "But then someone found a rule that said that only one family could hold that post.",
                ).also { stage++ }

            3 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Overnight I was fired and replaced by some fool who can't even bake a cake without help!",
                ).also { stage++ }

            4 -> end()
        }
    }
}

class ServantButlerDialogue : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.BUTLER_4241)
        when (stage) {
            0 -> player("What can you do?").also { stage++ }
            1 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    DARK_BLUE + "I can fulfill all sir's domestic service needs with efficiency and impeccable manners.",
                ).also { stage++ }

            2 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    DARK_BLUE +
                        "I hate to boast, but I can say with confidence that no mortal can make trips to the bank or sawmill faster than I!",
                ).also { stage++ }

            3 -> end()
        }
    }
}

class ServantButlerDialogueExtension : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.BUTLER_4241)
        when (stage) {
            0 -> player("Tell me about your previous jobs.").also { stage++ }
            1 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    DARK_BLUE +
                        "From a humble beginning as a dish-washer I have worked my way up through the ranks of domestic service in the households of nobles from Varrock and Ardougne.",
                ).also { stage++ }

            2 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    DARK_BLUE +
                        "As a life-long servant I have naturally suppressed any personality of my own and trained myself never to use the second person when talking to a superior.",
                ).also { stage++ }

            3 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    DARK_BLUE +
                        "I have usually worked in the large households of the aristocracy, but now that such a large number of private persons are building their own houses I decided to offer them my services.",
                ).also { stage++ }

            4 -> end()
        }
    }
}

class ServantDemonButlerDialogue : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.DEMON_BUTLER_4243)
        when (stage) {
            0 -> player("What can you do?").also { stage++ }
            1 ->
                npcl(
                    FaceAnim.OLD_NORMAL,
                    DARK_BLUE +
                        "I have learned my trade under the leash of some of the harshest masters of the Demon Dimensions.",
                ).also { stage++ }

            2 ->
                npcl(
                    FaceAnim.OLD_NORMAL,
                    DARK_BLUE +
                        "I can cook to satisfy the most infernal stomachs, and fly on wings of flame to deposit thine items in the bank or bring planks from the sawmill in seconds.",
                ).also { stage++ }

            3 -> end()
        }
    }
}

class ServantDemonButlerDialogueExtension : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.DEMON_BUTLER_4243)
        when (stage) {
            0 -> player("Tell me about your previous jobs.").also { stage++ }
            1 ->
                npcl(
                    FaceAnim.OLD_NORMAL,
                    DARK_BLUE +
                        "For millennia I have served and waited on the mighty Demon Lords of the Infernal Dimensions.",
                ).also { stage++ }

            2 ->
                npcl(
                    FaceAnim.OLD_NORMAL,
                    DARK_BLUE +
                        "I began as a humble footman in the household of Lord Thammaron, and for several centuries I was the private valet to Delrith.",
                ).also { stage++ }

            3 ->
                npcl(
                    FaceAnim.OLD_NORMAL,
                    DARK_BLUE +
                        "I have also worked in the Grim Underworld, escorting the souls of the dead to their final abodes. But the incessant shadows and hellfire wary me, so I have come to serve mortal masters in the realms of light.",
                ).also { stage++ }

            4 -> end()
        }
    }
}
