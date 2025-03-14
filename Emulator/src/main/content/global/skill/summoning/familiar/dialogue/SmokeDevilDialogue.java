package content.global.skill.summoning.familiar.dialogue;

import core.game.dialogue.Dialogue;
import core.game.dialogue.FaceAnim;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.plugin.Initializable;
import org.rs.consts.NPCs;

import static core.tools.DialogueHelperKt.END_DIALOGUE;

/**
 * The type Smoke devil dialogue.
 */
@Initializable
public class SmokeDevilDialogue extends Dialogue {

    @Override
    public Dialogue newInstance(Player player) {
        return new SmokeDevilDialogue(player);
    }

    /**
     * Instantiates a new Smoke devil dialogue.
     */
    public SmokeDevilDialogue() {}

    /**
     * Instantiates a new Smoke devil dialogue.
     *
     * @param player the player
     */
    public SmokeDevilDialogue(Player player) {
        super(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        int randomChoice = (int) (Math.random() * 4);
        switch (randomChoice) {
            case 0:
                npcl(FaceAnim.CHILD_NORMAL, "When are you going to be done with that?");
                stage = 0;
                break;
            case 1:
                npcl(FaceAnim.CHILD_NORMAL, "Hey!");
                stage = 6;
                break;
            case 2:
                npcl(FaceAnim.CHILD_NORMAL, "Ah, this is the life!");
                stage = 16;
                break;
            case 3:
                npcl(FaceAnim.CHILD_NORMAL, "Why is it always so cold here?");
                stage = 22;
                break;
        }
        return true;
    }

    @Override
    public boolean handle(int interfaceId, int buttonId) {
        switch (stage) {
            case 0:
                playerl(FaceAnim.FRIENDLY, "Soon, I hope.");
                stage++;
                break;
            case 1:
                npcl(FaceAnim.CHILD_NORMAL, "Good, because this place is too breezy.");
                stage++;
                break;
            case 2:
                playerl(FaceAnim.HALF_ASKING, "What do you mean?");
                stage++;
                break;
            case 3:
                npcl(FaceAnim.CHILD_NORMAL, "I mean, it's tricky to keep hovering in this draft.");
                stage++;
                break;
            case 4:
                playerl(FaceAnim.FRIENDLY, "Ok, we'll move around a little if you like.");
                stage++;
                break;
            case 5:
                npcl(FaceAnim.CHILD_NORMAL, "Yes please!");
                stage = END_DIALOGUE;
                break;
            case 6:
                playerl(FaceAnim.HALF_ASKING, "Yes?");
                stage++;
                break;
            case 7:
                npcl(FaceAnim.CHILD_NORMAL, "Where are we going again?");
                stage++;
                break;
            case 8:
                playerl(FaceAnim.FRIENDLY, "Well, I have a lot of things to do today, so we might go a lot of places.");
                stage++;
                break;
            case 9:
                npcl(FaceAnim.CHILD_NORMAL, "Are we there yet?");
                stage++;
                break;
            case 10:
                playerl(FaceAnim.FRIENDLY, "No, not yet.");
                stage++;
                break;
            case 11:
                npcl(FaceAnim.CHILD_NORMAL, "How about now?");
                stage++;
                break;
            case 12:
                playerl(FaceAnim.FRIENDLY, "No.");
                stage++;
                break;
            case 13:
                npcl(FaceAnim.CHILD_NORMAL, "Are we still not there?");
                stage++;
                break;
            case 14:
                playerl(FaceAnim.ANNOYED, "NO!");
                stage++;
                break;
            case 15:
                npcl(FaceAnim.CHILD_NORMAL, "Okay, just checking.");
                stage = END_DIALOGUE;
                break;
            case 16:
                playerl(FaceAnim.HALF_ASKING, "Having a good time up there?");
                stage++;
                break;
            case 17:
                npcl(FaceAnim.CHILD_NORMAL, "Yeah! It's great to feel the wind in your tentacles.");
                stage++;
                break;
            case 18:
                playerl(FaceAnim.FRIENDLY, "Sadly, I don't know what that feels like.");
                stage++;
                break;
            case 19:
                npcl(FaceAnim.CHILD_NORMAL, "Why not?");
                stage++;
                break;
            case 20:
                playerl(FaceAnim.FRIENDLY, "No tentacles for a start.");
                stage++;
                break;
            case 21:
                npcl(FaceAnim.CHILD_NORMAL, "Well, nobody's perfect.");
                stage = END_DIALOGUE;
                break;
            case 22:
                playerl(FaceAnim.FRIENDLY, "I don't think it's that cold.");
                stage++;
                break;
            case 23:
                npcl(FaceAnim.CHILD_NORMAL, "It is compared to back home.");
                stage++;
                break;
            case 24:
                playerl(FaceAnim.FRIENDLY, "How hot is it where you are from?");
                stage++;
                break;
            case 25:
                npcl(FaceAnim.CHILD_NORMAL, "I can never remember. What is the vaporisation point of steel again?");
                stage++;
                break;
            case 26:
                playerl(FaceAnim.FRIENDLY, "Pretty high.");
                stage++;
                break;
            case 27:
                playerl(FaceAnim.FRIENDLY, "No wonder you feel cold here...");
                stage = END_DIALOGUE;
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[] { NPCs.SMOKE_DEVIL_6865, NPCs.SMOKE_DEVIL_6866 };
    }
}
