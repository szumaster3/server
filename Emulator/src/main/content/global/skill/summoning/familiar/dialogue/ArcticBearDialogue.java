package content.global.skill.summoning.familiar.dialogue;

import core.game.dialogue.Dialogue;
import core.game.dialogue.FaceAnim;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.plugin.Initializable;
import org.rs.consts.NPCs;

import java.util.Random;

import static core.tools.DialogueHelperKt.END_DIALOGUE;

/**
 * The type Arctic bear dialogue.
 */
@Initializable
public class ArcticBearDialogue extends Dialogue {

    @Override
    public Dialogue newInstance(Player player) {
        return new ArcticBearDialogue(player);
    }

    /**
     * Instantiates a new Arctic bear dialogue.
     */
    public ArcticBearDialogue() {}

    /**
     * Instantiates a new Arctic bear dialogue.
     *
     * @param player the player
     */
    public ArcticBearDialogue(Player player) {
        super(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        Random rand = new Random();
        switch (rand.nextInt(5)) {
            case 0:
                npcl(FaceAnim.CHILD_NORMAL, "Crikey! We're tracking ourselves a real live one here. I call 'em 'Brighteyes'.");
                stage = 0;
                break;
            case 1:
                npcl(FaceAnim.CHILD_NORMAL, "Crikey! Something seems to have startled Brighteyes, here.");
                stage = 5;
                break;
            case 2:
                npcl(FaceAnim.CHILD_NORMAL, "We're tracking Brighteyes here as goes about " +
                        (player.isMale() ? "his" : "her") + " daily routine.");
                stage = 8;
                break;
            case 3:
                npcl(FaceAnim.CHILD_NORMAL, "These little guys get riled up real easy.");
                stage = 11;
                break;
            case 4:
                npcl(FaceAnim.CHILD_NORMAL, "I'm going to use this snow to blend in and get closer to this little feller.");
                stage = 12;
                break;
        }
        return true;
    }

    @Override
    public boolean handle(int interfaceId, int buttonId) {
        switch (stage) {
            case 0:
                playerl(FaceAnim.HALF_ASKING, "Will you stop stalking me like that?");
                stage++;
                break;
            case 1:
                npcl(FaceAnim.CHILD_NORMAL, "Lookit that! Something's riled this one up good and proper.");
                stage++;
                break;
            case 2:
                playerl(FaceAnim.HALF_ASKING, "Who are you talking to anyway?");
                stage++;
                break;
            case 3:
                npcl(FaceAnim.CHILD_NORMAL, "Looks like I've been spotted.");
                stage++;
                break;
            case 4:
                playerl(FaceAnim.HALF_ASKING, "Did you think you didn't stand out here or something?");
                stage = END_DIALOGUE;
                break;
            case 5:
                playerl(FaceAnim.HALF_ASKING, "What? What's happening?");
                stage++;
                break;
            case 6:
                npcl(FaceAnim.CHILD_NORMAL, "Maybe " + (player.isMale() ? "he" : "she") + " scented a rival.");
                stage++;
                break;
            case 7:
                playerl(FaceAnim.FRIENDLY, "I smell something, but it's not a rival.");
                stage = END_DIALOGUE;
                break;
            case 8:
                playerl(FaceAnim.FRIENDLY, "My name is Player, not Brighteyes!");
                stage++;
                break;
            case 9:
                npcl(FaceAnim.CHILD_NORMAL, "Looks like the little critter's upset about something.");
                stage++;
                break;
            case 10:
                playerl(FaceAnim.FRIENDLY, "I wonder if he'd be quiet if I just did really boring stuff.");
                stage = END_DIALOGUE;
                break;
            case 11:
                playerl(FaceAnim.HALF_ASKING, "Who wouldn't be upset with a huge bear tracking along behind them, commenting on everything they do?");
                stage = END_DIALOGUE;
                break;
            case 12:
                playerl(FaceAnim.FRIENDLY, "I'm looking right at you. I can still see you, you know.");
                stage++;
                break;
            case 13:
                npcl(FaceAnim.CHILD_NORMAL, "I don't think they can see me...");
                stage++;
                break;
            case 14:
                playerl(FaceAnim.FRIENDLY, "*Siiiigh*");
                stage++;
                break;
            case 15:
                npcl(FaceAnim.CHILD_NORMAL, "So, I'm gonna get a little closer and see if I can rile 'em up.");
                stage++;
                break;
            case 16:
                sendDialogue("The bear nudges you in the stomach.");
                stage++;
                break;
            case 17:
                playerl(FaceAnim.FRIENDLY, "Hey!");
                stage++;
                break;
            case 18:
                npcl(FaceAnim.CHILD_NORMAL, "Willya lookit that! Lookit them teeth; I'd be a goner if it got hold of me!");
                stage++;
                break;
            case 19:
                playerl(FaceAnim.FRIENDLY, "You have no idea how true that is.");
                stage = END_DIALOGUE;
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{NPCs.ARCTIC_BEAR_6839, NPCs.ARCTIC_BEAR_6840};
    }
}
