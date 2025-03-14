package content.global.skill.summoning.familiar.dialogue.titan;

import core.game.dialogue.Dialogue;
import core.game.dialogue.FaceAnim;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.plugin.Initializable;
import org.rs.consts.NPCs;

import static core.api.ContentAPIKt.inBorders;
import static core.tools.DialogueHelperKt.END_DIALOGUE;

/**
 * The type Ice titan dialogue.
 */
@Initializable
public class IceTitanDialogue extends Dialogue {

    @Override
    public Dialogue newInstance(Player player) {
        return new IceTitanDialogue(player);
    }

    /**
     * Instantiates a new Ice titan dialogue.
     */
    public IceTitanDialogue() {}

    /**
     * Instantiates a new Ice titan dialogue.
     *
     * @param player the player
     */
    public IceTitanDialogue(Player player) {
        super(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        if (inBorders(player, 3113, 2753, 3391, 3004)) {
            npcl(FaceAnim.CHILD_NORMAL, "I'm melting!");
            stage = 0;
            return true;
        }
        switch ((int) (Math.random() * 4)) {
            case 0:
                playerl(FaceAnim.HALF_ASKING, "How are you feeling?");
                stage = 3;
                break;
            case 1:
                npcl(FaceAnim.CHILD_NORMAL, "Can we just stay away from fire for a while?");
                stage = 19;
                break;
            case 2:
                npcl(FaceAnim.CHILD_NORMAL, "I could murder an ice-cream.");
                stage = 28;
                break;
            case 3:
                npcl(FaceAnim.CHILD_NORMAL, "It's too hot here.");
                stage = 42;
                break;
        }
        return true;
    }

    @Override
    public boolean handle(int interfaceId, int buttonId) {
        switch (stage) {
            case 0:
                playerl(FaceAnim.FRIENDLY, "I have to admit, I am rather on the hot side myself.");
                stage++;
                break;
            case 1:
                npcl(FaceAnim.CHILD_NORMAL, "No, I mean I'm actually melting! My legs have gone dribbly.");
                stage++;
                break;
            case 2:
                playerl(FaceAnim.FRIENDLY, "Urk! Well, try hold it together.");
                stage = END_DIALOGUE;
                break;

            case 3:
                npcl(FaceAnim.CHILD_NORMAL, "Hot.");
                stage++;
                break;
            case 4:
                playerl(FaceAnim.HALF_ASKING, "Are you ever anything else?");
                stage++;
                break;
            case 5:
                npcl(FaceAnim.CHILD_NORMAL, "Sometimes I'm just the right temperature: absolute zero.");
                stage++;
                break;
            case 6:
                playerl(FaceAnim.HALF_ASKING, "What's that then, when it's not at home with its feet up on the couch?");
                stage++;
                break;
            case 7:
                npcl(FaceAnim.CHILD_NORMAL, "What?");
                stage++;
                break;
            case 8:
                playerl(FaceAnim.HALF_ASKING, "Absolute zero; what is it?");
                stage++;
                break;
            case 9:
                npcl(FaceAnim.CHILD_NORMAL, "Oh...it's the lowest temperature that can exist.");
                stage++;
                break;
            case 10:
                playerl(FaceAnim.HALF_ASKING, "Like the temperature of ice?");
                stage++;
                break;
            case 11:
                npcl(FaceAnim.CHILD_NORMAL, "Um, no. Rather a lot colder.");
                stage++;
                break;
            case 12:
                playerl(FaceAnim.HALF_ASKING, "Like a deepest, darkest winter day?");
                stage++;
                break;
            case 13:
                npcl(FaceAnim.CHILD_NORMAL, "Nah, that's warm by comparison.");
                stage++;
                break;
            case 14:
                playerl(FaceAnim.HALF_ASKING, "Like an Ice Barrage in your jammies?");
                stage++;
                break;
            case 15:
                npcl(FaceAnim.CHILD_NORMAL, "Even colder than that.");
                stage++;
                break;
            case 16:
                playerl(FaceAnim.FRIENDLY, "Yikes! That's rather chilly.");
                stage++;
                break;
            case 17:
                npcl(FaceAnim.CHILD_NORMAL, "Yeah. Wonderful, isn't it?");
                stage++;
                break;
            case 18:
                playerl(FaceAnim.FRIENDLY, "If you say so.");
                stage = END_DIALOGUE;
                break;

            case 19:
                playerl(FaceAnim.FRIENDLY, "I like fire, it's so pretty.");
                stage++;
                break;
            case 20:
                npcl(FaceAnim.CHILD_NORMAL, "Personally, I think it's terrifying.");
                stage++;
                break;
            case 21:
                playerl(FaceAnim.FRIENDLY, "Why?");
                stage++;
                break;
            case 22:
                npcl(FaceAnim.CHILD_NORMAL, "I'm not so keen on hot things.");
                stage++;
                break;
            case 23:
                playerl(FaceAnim.FRIENDLY, "Ah.");
                stage++;
                break;
            case 24:
                npcl(FaceAnim.CHILD_NORMAL, "Indeed.");
                stage++;
                break;
            case 25:
                playerl(FaceAnim.FRIENDLY, "I see.");
                stage++;
                break;
            case 26:
                npcl(FaceAnim.CHILD_NORMAL, "Yes. Well...");
                stage++;
                break;
            case 27:
                playerl(FaceAnim.FRIENDLY, "...let's get on with it.");
                stage = END_DIALOGUE;
                break;

            case 28:
                playerl(FaceAnim.FRIENDLY, "Is that a Slayer creature?");
                stage++;
                break;
            case 29:
                npcl(FaceAnim.CHILD_NORMAL, "Um...");
                stage++;
                break;
            case 30:
                playerl(FaceAnim.HALF_ASKING, "What does it drop?");
                stage++;
                break;
            case 31:
                npcl(FaceAnim.CHILD_NORMAL, "Erm...");
                stage++;
                break;
            case 32:
                playerl(FaceAnim.HALF_ASKING, "What level is it?");
                stage++;
                break;
            case 33:
                npcl(FaceAnim.CHILD_NORMAL, "It...");
                stage++;
                break;
            case 34:
                playerl(FaceAnim.HALF_ASKING, "Where can I find it?");
                stage++;
                break;
            case 35:
                npcl(FaceAnim.CHILD_NORMAL, "I...");
                stage++;
                break;
            case 36:
                playerl(FaceAnim.HALF_ASKING, "What equipment will I need?");
                stage++;
                break;
            case 37:
                npcl(FaceAnim.CHILD_NORMAL, "What...");
                stage++;
                break;
            case 38:
                playerl(FaceAnim.FRIENDLY, "I don't think it will be high enough level.");
                stage++;
                break;
            case 39:
                npcl(FaceAnim.CHILD_NORMAL, "Urm...");
                stage++;
                break;
            case 40:
                playerl(FaceAnim.FRIENDLY, "...");
                stage++;
                break;
            case 41:
                npcl(FaceAnim.CHILD_NORMAL, "We should get on with what we were doing.");
                stage = END_DIALOGUE;
                break;

            case 42:
                playerl(FaceAnim.FRIENDLY, "It's really not that hot. I think it's rather pleasant.");
                stage++;
                break;
            case 43:
                npcl(FaceAnim.CHILD_NORMAL, "Well, it's alright for some. Some of us don't like the heat. I burn easily - well, okay, melt.");
                stage++;
                break;
            case 44:
                playerl(FaceAnim.FRIENDLY, "Well, at least I know where to get a nice cold drink if I need one.");
                stage++;
                break;
            case 45:
                npcl(FaceAnim.CHILD_NORMAL, "What was that?");
                stage++;
                break;
            case 46:
                playerl(FaceAnim.FRIENDLY, "Nothing. Hehehehe");
                stage = END_DIALOGUE;
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[] { NPCs.ICE_TITAN_7359, NPCs.ICE_TITAN_7360 };
    }
}
