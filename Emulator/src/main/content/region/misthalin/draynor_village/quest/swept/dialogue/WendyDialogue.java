package content.region.misthalin.draynor_village.quest.swept.dialogue;

import content.region.misthalin.draynor_village.quests.swept.SweptAway;
import core.game.dialogue.Dialogue;
import core.game.dialogue.FaceAnim;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.plugin.Initializable;
import org.rs.consts.NPCs;
import org.rs.consts.Quests;

import static core.tools.DialogueHelperKt.END_DIALOGUE;

/**
 * Represents the Wendy dialogue.
 *
 * <p>Relations:
 * <ul>
 *   <li>{@link SweptAway Swept Away quest}</li>
 * </ul>
 */
@Initializable
public class WendyDialogue extends Dialogue {

    /**
     * Instantiates a new Wendy dialogue.
     */
    public WendyDialogue() {
    }

    /**
     * Instantiates a new Wendy dialogue.
     *
     * @param player the player
     */
    public WendyDialogue(Player player) {
        super(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        playerl(FaceAnim.HAPPY, "Oh, hello.");
        stage = 0;
        return true;
    }

    @Override
    public boolean handle(int interfaceId, int buttonId) {
        switch (stage) {
            case 0:
                if(!player.questRepository.isComplete(Quests.SWEPT_AWAY)) {
                    options("Who are you?", "What are you doing?", "You look a little lost.");
                } else {
                    options("Who are you?", "What are you doing?", "You look a little lost.", "Let's talk about purple cats.", "Can you remove the purple from a cat?");
                }
                stage = 1;
                break;
            case 1:
                switch (buttonId) {
                    case 1:
                        playerl(FaceAnim.HALF_ASKING,"Who are you?");
                        stage = 10;
                        break;
                    case 2:
                        playerl(FaceAnim.HALF_ASKING,"What are you doing?");
                        stage = 20;
                        break;
                    case 3:
                        playerl(FaceAnim.HALF_ASKING,"If you don't mind my saying so, you look a little lost.");
                        stage = 30;
                        break;
                    case 4:
                        playerl(FaceAnim.HALF_ASKING,"Can you make my cat purple?");
                        stage = 60;
                        break;
                    case 5:
                        playerl(FaceAnim.HALF_ASKING,"Can you remove the purple from a cat?");
                        stage = 70;
                        break;
                }
                break;
            case 10:
                npcl(null, "Me? I'm Wendy. I'm learning the ways of witchcraft from Maggie here. She's very clever, you know...and very patient.");
                stage = 0;
                break;
            case 20:
                npcl(FaceAnim.FRIENDLY,"I'm travelling with Maggie, trying to learn the ways of witchcraft.");
                stage++;
                break;
            case 21:
                player("And what are you learning at the moment?");
                stage++;
                break;
            case 22:
                npcl(FaceAnim.FRIENDLY,"I'm learning all about - um, hold on...let me just think...");
                stage++;
                break;
            case 23:
                npcl(FaceAnim.FRIENDLY,"It was something really quite fascinating, I'm sure...");
                stage++;
                break;
            case 24:
                npcl(FaceAnim.THINKING,"Uh...");
                stage++;
                break;
            case 25:
                npcl(FaceAnim.THINKING,"Hmm...");
                stage++;
                break;
            case 26:
                player("Look, don't worry about it, really.");
                stage++;
                break;
            case 27:
                npcl(FaceAnim.FRIENDLY,"Sorry, I can't quite recall at the moment, but I'm sure it will come back to me, sooner or later.");
                stage = 0;
                break;
            case 30:
                npcl(FaceAnim.FRIENDLY,"It's funny - I get that a lot! I don't think I'm lost...not at the moment, at least.");
                stage++;
                break;
            case 31:
                npcl(FaceAnim.FRIENDLY,"Keeping track of where one is isn't a problem, is it? I think it's more that I keep forgetting where I'm going.");
                stage++;
                break;
            case 32:
                options("What do you mean?", "So where are you going?");
                stage = 33;
                break;
            case 33:
                switch (buttonId) {
                    case 1:
                        playerl(FaceAnim.HALF_ASKING,"What do you mean?");
                        stage = 40;
                        break;
                    case 2:
                        playerl(FaceAnim.HALF_ASKING,"So where are you going?");
                        stage = 50;
                        break;
                }
                break;
            case 40:
                npcl(FaceAnim.FRIENDLY, "Well, it's just that there are so many interesting things to do and see, really!");
                stage++;
                break;
            case 41:
                npcl(FaceAnim.FRIENDLY, "It's all well and good to make plans for myself, but I always seem to get distracted along the way.");
                stage++;
                break;
            case 42:
                playerl(FaceAnim.HALF_ASKING,"Distracted? By what?");
                stage++;
                break;
            case 43:
                npcl(null, "Oh, well, you know. A pretty flower. A little impling flying by. A fluffy cat that looks like it needs a scratch under its chin.");
                stage++;
                break;
            case 44:
                npcl(null, "I see things like that and my best-laid plans just seem to fly right out of my head!");
                stage++;
                break;
            case 45:
                player("Hmm, I can see how they might.");
                stage = 0;
                break;
            case 50:
                npcl(null, "Well, Maggie has been teaching me the basics of witchcraft.");
                stage++;
                break;
            case 51:
                npcl(null, "I'm sure she sent me out to collect something but, come to think of it, I can't quite remember what it was.");
                stage++;
                break;
            case 52:
                playerl(null, "Well, perhaps it was something for a potion? Or a spell?");
                stage++;
                break;
            case 53:
                npcl(null, "Oo, that sounds possible! Do you remember what it might have been?");
                stage++;
                break;
            case 54:
                playerl(null, "Well, given that I wasn't there for the conversation, I have no way of knowing that, I'm afraid.");
                stage++;
                break;
            case 55:
                npcl(null, "Oh. Sad.");
                stage++;
                break;
            case 56:
                npcl(null, "Wait, what were we talking about?");
                stage = 0;
                break;
            case 60:
                npcl(null, "Of course. Just hand your cat to me so that I can take a closer look.");
                // TODO: dialogue without a cat in backpack, or as a follower.
                // npc(null, "Of course. Just bring your cat to me and let me hold her for a moment. I'll sort her out in no time.");
                // TODO: dialogue when a cat following the player.
                // npc(null, "Of course. Just pick up your cat and hand her to me so that I can take a closer look.");
                stage = END_DIALOGUE;
                break;
            case 70:
                npcl(FaceAnim.AFRAID, "I'm afraid that once the spell is cast, it's cast for good. I haven't figured out how to undo the purple yet.");
                stage++;
                break;
            case 71:
                player(FaceAnim.HALF_CRYING, "Ah, right. Hmm.");
                stage = END_DIALOGUE;
                break;
        }
        return true;
    }

    @Override
    public Dialogue newInstance(Player player) {
        return new WendyDialogue(player);
    }

    @Override
    public int[] getIds() {
        return new int[]{NPCs.WENDY_8201};
    }
}
