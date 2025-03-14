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
 * The type Albino rat dialogue.
 */
@Initializable
public class AlbinoRatDialogue extends Dialogue {

    @Override
    public Dialogue newInstance(Player player) {
        return new AlbinoRatDialogue(player);
    }

    /**
     * Instantiates a new Albino rat dialogue.
     */
    public AlbinoRatDialogue() {}

    /**
     * Instantiates a new Albino rat dialogue.
     *
     * @param player the player
     */
    public AlbinoRatDialogue(Player player) {
        super(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        Random random = new Random();
        int randomIndex = random.nextInt(4);

        switch (randomIndex) {
            case 0:
                npcl(FaceAnim.CHILD_NORMAL, "Hey boss, we going to do anything wicked today?");
                stage = 0;
                break;
            case 1:
                npcl(FaceAnim.CHILD_NORMAL, "Hey boss, can we go and loot something now?");
                stage = 4;
                break;
            case 2:
                npcl(FaceAnim.CHILD_NORMAL, "So what we up to today, boss?");
                stage = 9;
                break;
            case 3:
                npcl(FaceAnim.CHILD_NORMAL, "You know, boss, I don't think you're totally into this whole 'evil' thing.");
                stage = 13;
                break;
        }

        return true;
    }

    @Override
    public boolean handle(int interfaceId, int buttonId) {
        switch (stage) {
            case 0:
                playerl(FaceAnim.FRIENDLY, "Well, I don't know why we would: I tend not to go around being wicked.");
                stage++;
                break;

            case 1:
                npcl(FaceAnim.CHILD_NORMAL, "Not even a little?");
                stage++;
                break;
            case 2:
                playerl(FaceAnim.FRIENDLY, "Well there was that one time... I'm sorry, no wickedness today.");
                stage++;
                break;

            case 3:
                npcl(FaceAnim.CHILD_NORMAL, "Awwwwww...");
                stage = END_DIALOGUE;
                break;

            case 4:
                playerl(FaceAnim.HALF_ASKING, "Well, what did you have in mind?");
                stage++;
                break;
            case 5:
                npcl(FaceAnim.CHILD_NORMAL, "I dunno - where are we headed?");
                stage++;
                break;
            case 6:
                playerl(FaceAnim.FRIENDLY, "I hadn't decided yet.");
                stage++;
                break;
            case 7:
                npcl(FaceAnim.CHILD_NORMAL, "When we get there, let's loot something nearby!");
                stage++;
                break;
            case 8:
                playerl(FaceAnim.FRIENDLY, "Sounds like a plan, certainly.");
                stage = END_DIALOGUE;
                break;

            case 9:
                playerl(FaceAnim.FRIENDLY, "Oh I'm sure we'll find something to occupy our time.");
                stage++;
                break;
            case 10:
                npcl(FaceAnim.CHILD_NORMAL, "Let's go robbin' graves again!");
                stage++;
                break;
            case 11:
                playerl(FaceAnim.ASKING, "What do you mean 'again'?");
                stage++;
                break;
            case 12:
                npcl(FaceAnim.CHILD_NORMAL, "Nuffin'...");
                stage = END_DIALOGUE;
                break;

            case 13:
                playerl(FaceAnim.HALF_ASKING, "I wonder what gave you that impression?");
                stage++;
                break;
            case 14:
                npcl(FaceAnim.CHILD_NORMAL, "Well, I worked with a lot of evil people; some of the best.");
                stage++;
                break;

            case 15:
                playerl(FaceAnim.HALF_ASKING, "Such as?");
                stage++;
                break;
            case 16:
                npcl(FaceAnim.CHILD_NORMAL, "I'm not telling! I've got my principles to uphold.");
                stage++;
                break;
            case 17:
                playerl(FaceAnim.FRIENDLY, "There is honour amongst thieves, it would seem.");
                stage = END_DIALOGUE;
                break;
        }

        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{NPCs.ALBINO_RAT_6847, NPCs.ALBINO_RAT_6848};
    }
}
