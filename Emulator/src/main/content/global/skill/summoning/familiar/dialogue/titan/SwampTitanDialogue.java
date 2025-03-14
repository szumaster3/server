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
 * The type Swamp titan dialogue.
 */
@Initializable
public class SwampTitanDialogue extends Dialogue {

    @Override
    public Dialogue newInstance(Player player) {
        return new SwampTitanDialogue(player);
    }

    /**
     * Instantiates a new Swamp titan dialogue.
     */
    public SwampTitanDialogue() {}

    /**
     * Instantiates a new Swamp titan dialogue.
     *
     * @param player the player
     */
    public SwampTitanDialogue(Player player) {
        super(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        if (inInventory(player, Items.SWAMP_TAR_1939, 1)) {
            npcl(FaceAnim.CHILD_NORMAL, "Do you smell that? Swamp tar, master. I LOVE the smell of swamp tar in the morning. Smells like...victorin.");
            stage = 0;
            return true;
        }
        switch ((int) (Math.random() * 4)) {
            case 0:
                npcl(FaceAnim.CHILD_NORMAL, "I'm alone, all alone I say.");
                stage = 4;
                break;
            case 1:
                npcl(FaceAnim.CHILD_NORMAL, "Oh, Guthix! I'm so alone, I have no fr");
                stage = 11;
                break;
            case 2:
                npcl(FaceAnim.CHILD_NORMAL, "Are you my friend, master?");
                stage = 23;
                break;
            case 3:
                playerl(FaceAnim.FRIENDLY, "Cheer up, it might never happen!");
                stage = 27;
                break;
        }
        return true;
    }

    @Override
    public boolean handle(int interfaceId, int buttonId) {
        switch (stage) {
            case 0:
                playerl(FaceAnim.FRIENDLY, "You actually LIKE the smell of this stuff? It's gross.");
                stage++;
                break;
            case 1:
                npcl(FaceAnim.CHILD_NORMAL, "Of course! I am made of swamp, after all.");
                stage++;
                break;
            case 2:
                playerl(FaceAnim.FRIENDLY, "Oh, I'm sorry. I didn't mean...I meant the swamp tar itself smells gross, not you. You smell like lavender. Yes, lavender.");
                stage++;
                break;
            case 3:
                npcl(FaceAnim.CHILD_NORMAL, "*sob* Lavender? Lavender! Why would you be so mean? I'm supposed to smell bad.");
                stage = END_DIALOGUE;
                break;

            case 4:
                playerl(FaceAnim.FRIENDLY, "Oh, stop being so melodramatic.");
                stage++;
                break;
            case 5:
                npcl(FaceAnim.CHILD_NORMAL, "It's not easy being greenery...well, decomposing greenery.");
                stage++;
                break;
            case 6:
                playerl(FaceAnim.HALF_ASKING, "Surely, you're not the only swamp...thing in the world? What about the other swamp titans?");
                stage++;
                break;
            case 7:
                npcl(FaceAnim.CHILD_NORMAL, "They're not my friends...they pick on me...they're so mean...");
                stage++;
                break;
            case 8:
                playerl(FaceAnim.ASKING, "Why would they do that?");
                stage++;
                break;
            case 9:
                npcl(FaceAnim.CHILD_NORMAL, "They think I DON'T smell.");
                stage++;
                break;
            case 10:
                playerl(FaceAnim.FRIENDLY, "Oh, yes. That is, er, mean...");
                stage = END_DIALOGUE;
                break;

            case 11:
                playerl(FaceAnim.FRIENDLY, "Oh, not again. Look, I'll be your friend.");
                stage++;
                break;
            case 12:
                npcl(FaceAnim.CHILD_NORMAL, "You'll be my friend, master?");
                stage++;
                break;
            case 13:
                playerl(FaceAnim.FRIENDLY, "Yeah, sure, why not.");
                stage++;
                break;
            case 14:
                npcl(FaceAnim.CHILD_NORMAL, "Really?");
                stage++;
                break;
            case 15:
                playerl(FaceAnim.FRIENDLY, "Really really...");
                stage++;
                break;
            case 16:
                npcl(FaceAnim.CHILD_NORMAL, "Oh, I'm so happy!");
                stage++;
                break;
            case 17:
                playerl(FaceAnim.FRIENDLY, "...even if you do smell like a bog of eternal stench.");
                stage++;
                break;
            case 18:
                npcl(FaceAnim.CHILD_NORMAL, "Wait...you think I smell bad?");
                stage++;
                break;
            case 19:
                playerl(FaceAnim.FRIENDLY, "Erm, yes, but I didn't me");
                stage++;
                break;
            case 20:
                npcl(FaceAnim.CHILD_NORMAL, "Oh, that's the nicest thing anyone's ever said to me! Thank you, master, thank you so much.");
                stage++;
                break;

            case 21:
                npcl(FaceAnim.CHILD_NORMAL, "You're my friend AND you think I smell. I'm so very happy!");
                stage++;
                break;
            case 22:
                playerl(FaceAnim.FRIENDLY, "I guess I did mean it like that.");
                stage = END_DIALOGUE;
                break;

            case 23:
                playerl(FaceAnim.ASKING, "Of course I am. I summoned you, didn't I?");
                stage++;
                break;
            case 24:
                npcl(FaceAnim.CHILD_NORMAL, "Yes, but that was just to do some fighting. When you're done with me you'll send me back.");
                stage++;
                break;

            case 25:
                playerl(FaceAnim.FRIENDLY, "I'm sure I'll need you again later.");
                stage++;
                break;
            case 26:
                npcl(FaceAnim.CHILD_NORMAL, "Please don't send me back.");
                stage = END_DIALOGUE;
                break;

            case 27:
                npcl(FaceAnim.CHILD_NORMAL, "Oh, why did you have to go and say something like that?");
                stage++;
                break;
            case 28:
                playerl(FaceAnim.FRIENDLY, "Like what? I'm trying to cheer you up.");
                stage++;
                break;
            case 29:
                npcl(FaceAnim.CHILD_NORMAL, "There's no hope for me, oh woe, oh woe.");
                stage++;
                break;
            case 30:
                playerl(FaceAnim.FRIENDLY, "I'll leave you alone, then.");
                stage++;
                break;
            case 31:
                npcl(FaceAnim.CHILD_NORMAL, "NO! Don't leave me, master!");
                stage = END_DIALOGUE;
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[] { NPCs.SWAMP_TITAN_7329, NPCs.SWAMP_TITAN_7330 };
    }
}
