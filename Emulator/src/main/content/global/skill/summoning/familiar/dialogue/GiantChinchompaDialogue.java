package content.global.skill.summoning.familiar.dialogue;

import core.game.dialogue.Dialogue;
import core.game.dialogue.FaceAnim;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.plugin.Initializable;
import org.rs.consts.NPCs;

import static core.tools.DialogueHelperKt.END_DIALOGUE;

/**
 * The type Giant chinchompa dialogue.
 */
@Initializable
public class GiantChinchompaDialogue extends Dialogue {

    @Override
    public Dialogue newInstance(Player player) {
        return new GiantChinchompaDialogue(player);
    }

    /**
     * Instantiates a new Giant chinchompa dialogue.
     */
    public GiantChinchompaDialogue() {}

    /**
     * Instantiates a new Giant chinchompa dialogue.
     *
     * @param player the player
     */
    public GiantChinchompaDialogue(Player player) {
        super(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        switch ((int) (Math.random() * 5)) {
            case 0:
                npcl(FaceAnim.CHILD_NORMAL, "Half a pound of tuppenny rice, half a pound of treacle...");
                stage = 0;
                break;
            case 1:
                npcl(FaceAnim.CHILD_NORMAL, "What's small, brown and blows up?");
                stage = 5;
                break;
            case 2:
                npcl(FaceAnim.CHILD_NORMAL, "I spy, with my little eye, something beginning with 'B'.");
                stage = 10;
                break;
            case 3:
                npcl(FaceAnim.CHILD_NORMAL, "I seem to have found a paper bag.");
                stage = 15;
                break;
            case 4:
                npcl(FaceAnim.CHILD_NORMAL, "Woah, woah, woah - hold up there.");
                stage = 19;
                break;
        }
        return true;
    }

    @Override
    public boolean handle(int interfaceId, int buttonId) {
        switch (stage) {
            case 0:
                playerl(FaceAnim.HALF_ASKING, "I hate it when you sing that song.");
                stage++;
                break;
            case 1:
                npcl(FaceAnim.CHILD_NORMAL, "...that's the way the money goes...");
                stage++;
                break;
            case 2:
                playerl(FaceAnim.HALF_ASKING, "Couldn't you sing 'Kumbaya' or something?");
                stage++;
                break;
            case 3:
                npcl(FaceAnim.CHILD_NORMAL, "...BANG, goes the chinchompa!");
                stage++;
                break;
            case 4:
                playerl(FaceAnim.HALF_ASKING, "Sheesh.");
                stage = END_DIALOGUE;
                break;

            case 5:
                playerl(FaceAnim.HALF_ASKING, "A brown balloon?");
                stage++;
                break;
            case 6:
                npcl(FaceAnim.CHILD_NORMAL, "A chinchompa! Pull my finger.");
                stage++;
                break;
            case 7:
                playerl(FaceAnim.HALF_ASKING, "I'm not pulling your finger.");
                stage++;
                break;
            case 8:
                npcl(FaceAnim.CHILD_NORMAL, "Nothing will happen. Truuuuust meeeeee.");
                stage++;
                break;
            case 9:
                playerl(FaceAnim.FRIENDLY, "Oh, go away.");
                stage = END_DIALOGUE;
                break;

            case 10:
                playerl(FaceAnim.HALF_ASKING, "Bomb? Bang? Boom? Blowing-up-little-chipmunk?");
                stage++;
                break;
            case 11:
                npcl(FaceAnim.CHILD_NORMAL, "No. Body odour. You should wash a bit more.");
                stage++;
                break;
            case 12:
                playerl(FaceAnim.HALF_ASKING, "Well, that was pleasant. You don't smell all that great either, you know.");
                stage++;
                break;

            case 13:
                npcl(FaceAnim.CHILD_NORMAL, "Stop talking, stop talking! Your breath stinks!");
                stage++;
                break;
            case 14:
                playerl(FaceAnim.HALF_ASKING, "We're never going to get on, are we?");
                stage = END_DIALOGUE;
                break;

            case 15:
                playerl(FaceAnim.HALF_ASKING, "Well done. Anything in it?");
                stage++;
                break;
            case 16:
                npcl(FaceAnim.CHILD_NORMAL, "Hmmm. Let me see. It seems to be full of some highly sought after, very expensive...chinchompa breath!");
                stage++;
                break;

            case 17:
                playerl(FaceAnim.FRIENDLY, "No, don't pop it!");
                stage++;
                break;
            case 18:
                playerl(FaceAnim.HALF_ASKING, "You just cannot help yourself, can you?");
                stage = END_DIALOGUE;
                break;

            case 19:
                playerl(FaceAnim.HALF_ASKING, "What is it, ratty?");
                stage++;
                break;
            case 20:
                npcl(FaceAnim.CHILD_NORMAL, "You got something in your backpack that you'd like to tell me about?");
                stage++;
                break;

            case 21:
                playerl(FaceAnim.HALF_ASKING, "I was wondering when you were going to bring up the chinchompa. I'm sure they like it in my inventory.");
                stage++;
                break;

            case 22:
                npcl(FaceAnim.CHILD_NORMAL, "Did they not teach you anything in school? Chinchompas die in hot bags. You know what happens when chinchompas die. Are you attached to your back?");
                stage++;
                break;

            case 23:
                playerl(FaceAnim.HALF_ASKING, "Medically, yes. And I kind of like it too. I get the point.");
                stage = END_DIALOGUE;
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{NPCs.GIANT_CHINCHOMPA_7353, NPCs.GIANT_CHINCHOMPA_7354};
    }
}
