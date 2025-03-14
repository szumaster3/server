package content.global.skill.summoning.familiar.dialogue;

import core.game.dialogue.Dialogue;
import core.game.dialogue.FaceAnim;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.plugin.Initializable;
import org.rs.consts.Items;
import org.rs.consts.NPCs;

import static core.api.ContentAPIKt.inEquipment;
import static core.tools.DialogueHelperKt.END_DIALOGUE;

/**
 * The type Obsidian golem dialogue.
 */
@Initializable
public class ObsidianGolemDialogue extends Dialogue {

    @Override
    public Dialogue newInstance(Player player) {
        return new ObsidianGolemDialogue(player);
    }

    /**
     * Instantiates a new Obsidian golem dialogue.
     */
    public ObsidianGolemDialogue() {}

    /**
     * Instantiates a new Obsidian golem dialogue.
     *
     * @param player the player
     */
    public ObsidianGolemDialogue(Player player) {
        super(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        if (inEquipment(player, Items.FIRE_CAPE_6570, 1)) {
            npcl(FaceAnim.CHILD_NORMAL, "Truly, you are a powerful warrior, Master!");
            stage = 0;
            return true;
        }
        switch ((int) (Math.random() * 3)) {
            case 0:
                npcl(FaceAnim.CHILD_NORMAL, "How many foes have you defeated, Master?");
                stage = 5;
                break;
            case 1:
                npcl(FaceAnim.CHILD_NORMAL, "Master! We are truly a mighty duo!");
                stage = 10;
                break;
            case 2:
                npcl(FaceAnim.CHILD_NORMAL, "Do you ever doubt your programming, Master?");
                stage = 15;
                break;
        }
        return true;
    }

    @Override
    public boolean handle(int interfaceId, int buttonId) {
        switch (stage) {
            case 0:
                npcl(FaceAnim.CHILD_NORMAL, "Let us go forth and prove our strength, Master!");
                stage++;
                break;
            case 1:
                playerl(FaceAnim.HALF_ASKING, "Where would you like to prove it?");
                stage++;
                break;
            case 2:
                npcl(FaceAnim.CHILD_NORMAL,
                    "The caves of the TzHaar are filled with monsters for us to defeat, Master! TzTok-Jad shall quake in his slippers!");
                stage++;
                break;
            case 3:
                playerl(FaceAnim.HALF_ASKING, "Have you ever met TzTok-Jad?");
                stage++;
                break;
            case 4:
                npcl(FaceAnim.CHILD_NORMAL, "Alas, Master, I have not. No Master has ever taken me to see him.");
                stage = END_DIALOGUE;
                break;
            case 5:
                playerl(FaceAnim.FRIENDLY, "Quite a few, I should think.");
                stage++;
                break;
            case 6:
                npcl(FaceAnim.CHILD_NORMAL, "Was your first foe as mighty as the volcano, Master?");
                stage++;
                break;
            case 7:
                playerl(FaceAnim.FRIENDLY, "Um, not quite.");
                stage++;
                break;
            case 8:
                npcl(FaceAnim.CHILD_NORMAL, "I am sure it must have been a deadly opponent, Master!");
                stage++;
                break;
            case 9:
                player(FaceAnim.FRIENDLY, "*Cough*", "It might have been a chicken.", "*Cough*");
                stage = END_DIALOGUE;
                break;
            case 10:
                playerl(FaceAnim.HALF_ASKING, "Do you think so?");
                stage++;
                break;
            case 11:
                npcl(FaceAnim.CHILD_NORMAL, "Of course, Master! I am programmed to believe so.");
                stage++;
                break;
            case 12:
                playerl(FaceAnim.FRIENDLY, "Do you do anything you're not programmed to?");
                stage++;
                break;
            case 13:
                npcl(FaceAnim.CHILD_NORMAL, "No, Master.");
                stage++;
                break;
            case 14:
                playerl(FaceAnim.FRIENDLY, "I guess that makes things simple for you...");
                stage = END_DIALOGUE;
                break;
            case 15:
                playerl(FaceAnim.FRIENDLY, "I don't have programming. I can think about whatever I like.");
                stage++;
                break;
            case 16:
                npcl(FaceAnim.CHILD_NORMAL, "What do you think about, Master?");
                stage++;
                break;
            case 17:
                playerl(FaceAnim.FRIENDLY, "Oh, simple things: the sound of one hand clapping, where the gods come from...Simple things.");
                stage++;
                break;
            case 18:
                npcl(FaceAnim.CHILD_NORMAL, "Paradox check = positive. Error. Reboot.");
                stage = END_DIALOGUE;
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[] { NPCs.OBSIDIAN_GOLEM_7345, NPCs.OBSIDIAN_GOLEM_7346 };
    }
}
