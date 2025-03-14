package content.global.skill.summoning.familiar.dialogue.titan;

import core.game.dialogue.Dialogue;
import core.game.dialogue.FaceAnim;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.plugin.Initializable;
import org.rs.consts.Items;
import org.rs.consts.NPCs;

import static core.api.ContentAPIKt.inInventory;
import static core.tools.DialogueHelperKt.END_DIALOGUE;

/**
 * The type Iron titan dialogue.
 */
@Initializable
public class IronTitanDialogue extends Dialogue {

    @Override
    public Dialogue newInstance(Player player) {
        return new IronTitanDialogue(player);
    }

    /**
     * Instantiates a new Iron titan dialogue.
     */
    public IronTitanDialogue() {}

    /**
     * Instantiates a new Iron titan dialogue.
     *
     * @param player the player
     */
    public IronTitanDialogue(Player player) {
        super(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        if (inInventory(player, Items.IRON_BAR_2351, 1)) {
            npcl(FaceAnim.CHILD_NORMAL, "Are you using that iron bar, boss?");
            stage++;
            return true;
        }
        switch ((int) (Math.random() * 4)) {
            case 0:
                playerl(FaceAnim.HALF_ASKING, "Titan?");
                stage = 7;
                break;
            case 1:
                npcl(FaceAnim.CHILD_NORMAL, "Boss!");
                stage = 14;
                break;
            case 2:
                npcl(FaceAnim.CHILD_NORMAL, "Boss?");
                stage = 21;
                break;
            case 3:
                playerl(FaceAnim.HALF_ASKING, "How are you today, titan?");
                stage = 27;
                break;
        }
        return true;
    }

    @Override
    public boolean handle(int interfaceId, int buttonId) {
        switch (stage) {
            case 0:
                playerl(FaceAnim.HALF_ASKING, "Well, not right now, why?");
                stage++;
                break;
            case 1:
                npcl(FaceAnim.CHILD_NORMAL, "Can I have it, then?");
                stage++;
                break;
            case 2:
                playerl(FaceAnim.HALF_ASKING, "What for?");
                stage++;
                break;
            case 3:
                npcl(FaceAnim.CHILD_NORMAL, "I've got a cunning plan.");
                stage++;
                break;
            case 4:
                playerl(FaceAnim.HALF_ASKING, "Involving my iron bar?");
                stage++;
                break;
            case 5:
                npcl(FaceAnim.CHILD_NORMAL, "No, but if I sell your iron bar I'll have enough money to buy a new hat.");
                stage++;
                break;
            case 6:
                npcl(FaceAnim.CHILD_NORMAL, "You can't go through with a cunning plan without the right headgear, boss!");
                stage = END_DIALOGUE;
                break;

            case 7:
                npcl(FaceAnim.CHILD_NORMAL, "Yes, boss?");
                stage++;
                break;
            case 8:
                playerl(FaceAnim.HALF_ASKING, "What's that in your hand?");
                stage++;
                break;
            case 9:
                npcl(FaceAnim.CHILD_NORMAL, "I'm glad you asked that, boss.");
                stage++;
                break;
            case 10:
                npcl(FaceAnim.CHILD_NORMAL, "This is the prototype for the Iron Titan (tm) action figure. You just pull this string here and he fights crime with real action sounds.");
                stage++;
                break;
            case 11:
                playerl(FaceAnim.ASKING, "Titan?");
                stage++;
                break;
            case 12:
                npcl(FaceAnim.CHILD_NORMAL, "Yes, boss?");
                stage++;
                break;
            case 13:
                playerl(FaceAnim.STRUGGLE, "Never mind.");
                stage = END_DIALOGUE;
                break;

            case 14:
                playerl(FaceAnim.HALF_ASKING, "What?");
                stage++;
                break;
            case 15:
                npcl(FaceAnim.CHILD_NORMAL, "I've just had a vision of the future.");
                stage++;
                break;
            case 16:
                playerl(FaceAnim.FRIENDLY, "I didn't know you were a fortune teller. Let's hear it then.");
                stage++;
                break;
            case 17:
                npcl(FaceAnim.CHILD_NORMAL, "Just imagine, boss, an Iron Titan (tm) on every desk.");
                stage++;
                break;
            case 18:
                playerl(FaceAnim.FRIENDLY, "That doesn't even make sense.");
                stage++;
                break;
            case 19:
                npcl(FaceAnim.CHILD_NORMAL, "Hmm. It was a bit blurry, perhaps the future is having technical issues at the moment.");
                stage++;
                break;
            case 20:
                playerl(FaceAnim.FRIENDLY, "Riiight.");
                stage = END_DIALOGUE;
                break;

            case 21:
                playerl(FaceAnim.HALF_ASKING, "Yes, titan?");
                stage++;
                break;
            case 22:
                npcl(FaceAnim.CHILD_NORMAL, "You know how you're the boss and I'm the titan?");
                stage++;
                break;
            case 23:
                playerl(FaceAnim.HALF_ASKING, "Yes?");
                stage++;
                break;
            case 24:
                npcl(FaceAnim.CHILD_NORMAL, "Do you think we could swap for a bit?");
                stage++;
                break;
            case 25:
                playerl(FaceAnim.FRIENDLY, "No, titan!");
                stage++;
                break;
            case 26:
                npcl(FaceAnim.CHILD_NORMAL, "Aww...");
                stage = END_DIALOGUE;
                break;

            case 27:
                npcl(FaceAnim.CHILD_NORMAL, "I'm very happy.");
                stage++;
                break;
            case 28:
                playerl(FaceAnim.HALF_ASKING, "That's marvellous, why are you so happy?");
                stage++;
                break;
            case 29:
                npcl(FaceAnim.CHILD_NORMAL, "Because I love the great taste of Iron Titan (tm) cereal!");
                stage++;
                break;
            case 30:
                playerl(FaceAnim.ASKING, "?");
                stage++;
                break;
            case 31:
                playerl(FaceAnim.ASKING, "You're supposed to be working for me, not promoting yourself.");
                stage++;
                break;
            case 32:
                npcl(FaceAnim.CHILD_NORMAL, "Sorry, boss.");
                stage = END_DIALOGUE;
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[] { NPCs.IRON_TITAN_7375, NPCs.IRON_TITAN_7376 };
    }
}
