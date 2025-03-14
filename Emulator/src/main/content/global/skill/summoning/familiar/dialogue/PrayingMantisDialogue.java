package content.global.skill.summoning.familiar.dialogue;

import core.game.dialogue.Dialogue;
import core.game.dialogue.FaceAnim;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.plugin.Initializable;
import org.rs.consts.Items;
import org.rs.consts.NPCs;

import static core.api.ContentAPIKt.inEquipmentOrInventory;
import static core.tools.DialogueHelperKt.END_DIALOGUE;

/**
 * The type Praying mantis dialogue.
 */
@Initializable
public class PrayingMantisDialogue extends Dialogue {

    @Override
    public Dialogue newInstance(Player player) {
        return new PrayingMantisDialogue(player);
    }

    /**
     * Instantiates a new Praying mantis dialogue.
     */
    public PrayingMantisDialogue() {}

    /**
     * Instantiates a new Praying mantis dialogue.
     *
     * @param player the player
     */
    public PrayingMantisDialogue(Player player) {
        super(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        if (inEquipmentOrInventory(player, Items.BUTTERFLY_NET_10010, 1) || inEquipmentOrInventory(player, Items.MAGIC_BUTTERFLY_NET_11259, 1)) {
            npc(FaceAnim.CHILD_NORMAL, "Clatter click chitter click?", "(Wouldn't you learn focus better if you used chopsticks?)");
            stage = 0;
            return true;
        }
        switch ((int) (Math.random() * 4)) {
            case 0:
                npc(FaceAnim.CHILD_NORMAL, "Chitter chirrup chirrup?", "(Have you been following your training, grasshopper?)");
                stage = 4;
                break;
            case 1:
                npc(FaceAnim.CHILD_NORMAL, "Chitterchitter chirrup clatter.", "(Today, grasshopper, I will teach you to walk on rice paper.)");
                stage = 9;
                break;
            case 2:
                npc(FaceAnim.CHILD_NORMAL, "Clatter chirrup chirp chirrup clatter clatter.", "(A wise man once said; 'Feed your mantis and it will be happy'.)");
                stage = 13;
                break;
            case 3:
                npc(FaceAnim.CHILD_NORMAL, "Clatter chirrupchirp-", "(Today, grasshopper, we will-)");
                stage = 15;
                break;
        }
        return true;
    }

    @Override
    public boolean handle(int interfaceId, int buttonId) {
        switch (stage) {
            case 0:
                playerl(FaceAnim.FRIENDLY, "Huh?");
                stage++;
                break;
            case 1:
                npc(FaceAnim.CHILD_NORMAL, "Clicker chirrpchirrup.", "(For catching the butterflies, grasshopper.)");
                stage++;
                break;
            case 2:
                playerl(FaceAnim.FRIENDLY, "Oh, right! Well, if I use anything but the net I squash them.");
                stage++;
                break;
            case 3:
                npc(FaceAnim.CHILD_NORMAL, "Chirrupchirrup click!", "(Then, I could have them!)");
                stage = END_DIALOGUE;
                break;
            case 4:
                playerl(FaceAnim.FRIENDLY, "Yes, almost every day.");
                stage++;
                break;
            case 5:
                npc(FaceAnim.CHILD_NORMAL, "Chirrupchirrup chirrup.", "('Almost' is not good enough.)");
                stage++;
                break;
            case 6:
                playerl(FaceAnim.FRIENDLY, "Well, I'm trying as hard as I can.");
                stage++;
                break;
            case 7:
                npc(FaceAnim.CHILD_NORMAL, "Chirrup chitter chitter chirrup?", "(How do you expect to achieve enlightenment at this rate, grasshopper?)");
                stage++;
                break;
            case 8:
                playerl(FaceAnim.FRIENDLY, "Spontaneously.");
                stage = END_DIALOGUE;
                break;
            case 9:
                playerl(FaceAnim.HALF_ASKING, "What if I can't find any?");
                stage++;
                break;
            case 10:
                npc(FaceAnim.CHILD_NORMAL, "Clatter chitter click chitter...", "(Then we will wander about and punch monsters in the head...)");
                stage++;
                break;
            case 11:
                playerl(FaceAnim.HALF_ASKING, "I could do in an enlightened way if you want?");
                stage++;
                break;
            case 12:
                npc(FaceAnim.CHILD_NORMAL, "Chirrupchitter!", "(That will do!)");
                stage = END_DIALOGUE;
                break;
            case 13:
                playerl(FaceAnim.HALF_ASKING, "Is there any point to that saying?");
                stage++;
                break;
            case 14:
                npc(FaceAnim.CHILD_NORMAL, "Clatter chirrupchirrup chirp.", "(I find that a happy mantis is its own point.)");
                stage = END_DIALOGUE;
                break;
            case 15:
                playerl(FaceAnim.FRIENDLY, "You know, I'd rather you call me something other than grasshopper.");
                stage++;
                break;
            case 16:
                npc(FaceAnim.CHILD_NORMAL, "Clitterchirp?", "(Is there a reason for this?)");
                stage++;
                break;
            case 17:
                playerl(FaceAnim.FRIENDLY, "You drool when you say it.");
                stage++;
                break;
            case 18:
                npc(FaceAnim.CHILD_NORMAL, "Clickclatter! Chirrup chirpchirp click chitter...", "(I do not! Why would I drool when I call you a juicy...)");
                stage++;
                break;
            case 19:
                npc(FaceAnim.CHILD_NORMAL, "...clickclick chitter clickchitter click...", "(...succulent, nourishing, crunchy...)");
                stage++;
                break;
            case 20:
                npc(FaceAnim.CHILD_NORMAL, "*Drooool*");
                stage++;
                break;
            case 21:
                playerl(FaceAnim.FRIENDLY, "You're doing it again!");
                stage = END_DIALOGUE;
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[] { NPCs.PRAYING_MANTIS_6798, NPCs.PRAYING_MANTIS_6799 };
    }
}
