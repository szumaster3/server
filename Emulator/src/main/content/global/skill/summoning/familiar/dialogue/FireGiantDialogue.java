package content.global.skill.summoning.familiar.dialogue;

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
 * The type Fire giant dialogue.
 */
@Initializable
public class FireGiantDialogue extends Dialogue {

    @Override
    public Dialogue newInstance(Player player) {
        return new FireGiantDialogue(player);
    }

    /**
     * Instantiates a new Fire giant dialogue.
     */
    public FireGiantDialogue() {}

    /**
     * Instantiates a new Fire giant dialogue.
     *
     * @param player the player
     */
    public FireGiantDialogue(Player player) {
        super(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        if (inInventory(player, Items.TINDERBOX_590, 1)) {
            npcl(FaceAnim.CHILD_NORMAL, "Relight my fire.");
            stage = 0;
            return true;
        }
        switch ((int) (Math.random() * 5)) {
            case 0:
                npcl(FaceAnim.CHILD_NORMAL, "Pick flax.");
                stage = 8;
                break;
            case 1:
                npcl(FaceAnim.CHILD_NORMAL, "You're fanning my flame with your wind spells.");
                stage = 12;
                break;
            case 2:
                npcl(FaceAnim.CHILD_NORMAL, "I'm burning up.");
                stage = 14;
                break;
            case 3:
                npcl(FaceAnim.CHILD_NORMAL, "It's raining flame!");
                stage = 17;
                break;
            case 4:
                npcl(FaceAnim.CHILD_NORMAL, "Let's go fireside.");
                stage = 20;
                break;
        }
        return true;
    }

    @Override
    public boolean handle(int interfaceId, int buttonId) {
        switch (stage) {
            case 0:
                npcl(FaceAnim.CHILD_NORMAL, "A tinderbox is my only desire.");
                stage++;
                break;
            case 1:
                playerl(FaceAnim.HALF_ASKING, "What are you singing?");
                stage++;
                break;
            case 2:
                npcl(FaceAnim.CHILD_NORMAL, "Just a song I heard a while ago.");
                stage++;
                break;
            case 3:
                playerl(FaceAnim.HALF_ASKING, "It's not very good.");
                stage++;
                break;
            case 4:
                npcl(FaceAnim.CHILD_NORMAL, "You're just jealous of my singing voice.");
                stage++;
                break;
            case 5:
                playerl(FaceAnim.HALF_ASKING, "Where did you hear this again?");
                stage++;
                break;
            case 6:
                npcl(FaceAnim.CHILD_NORMAL, "Oh, you know, just with some other fire titans. Out for a night on the pyres.");
                stage++;
                break;
            case 7:
                playerl(FaceAnim.FRIENDLY, "Hmm. Come on then. We have stuff to do.");
                stage = END_DIALOGUE;
                break;
            case 8:
                npcl(FaceAnim.CHILD_NORMAL, "Jump to it.");
                stage++;
                break;
            case 9:
                npcl(FaceAnim.CHILD_NORMAL, "If you want to get to fletching level 99.");
                stage++;
                break;
            case 10:
                playerl(FaceAnim.FRIENDLY, "That song...is terrible.");
                stage++;
                break;
            case 11:
                npcl(FaceAnim.CHILD_NORMAL, "Sorry.");
                stage = END_DIALOGUE;
                break;
            case 12:
                npcl(FaceAnim.CHILD_NORMAL, "I'm singeing the curtains with my heat.");
                stage++;
                break;
            case 13:
                playerl(FaceAnim.FRIENDLY, "Oooh, very mellow.");
                stage = END_DIALOGUE;
                break;
            case 14:
                npcl(FaceAnim.CHILD_NORMAL, "I want the world to know.");
                stage++;
                break;
            case 15:
                npcl(FaceAnim.CHILD_NORMAL, "I got to let it show.");
                stage++;
                break;
            case 16:
                playerl(FaceAnim.FRIENDLY, "Catchy.");
                stage = END_DIALOGUE;
                break;
            case 17:
                npcl(FaceAnim.CHILD_NORMAL, "Huzzah!");
                stage++;
                break;
            case 18:
                playerl(FaceAnim.FRIENDLY, "You have a...powerful voice.");
                stage++;
                break;
            case 19:
                npcl(FaceAnim.CHILD_NORMAL, "Thanks.");
                stage = END_DIALOGUE;
                break;
            case 20:
                npcl(FaceAnim.CHILD_NORMAL, "I think I've roasted the sofa.");
                stage++;
                break;
            case 21:
                npcl(FaceAnim.CHILD_NORMAL, "I think I've burnt down the hall.");
                stage++;
                break;
            case 22:
                playerl(FaceAnim.HALF_ASKING, "Can't you sing quietly?");
                stage++;
                break;
            case 23:
                npcl(FaceAnim.CHILD_NORMAL, "Sorry.");
                stage = END_DIALOGUE;
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{NPCs.FIRE_GIANT_7003, NPCs.FIRE_GIANT_7004};
    }
}

