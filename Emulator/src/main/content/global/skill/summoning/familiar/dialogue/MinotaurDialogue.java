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
 * The type Minotaur dialogue.
 */
@Initializable
public class MinotaurDialogue extends Dialogue {

    @Override
    public Dialogue newInstance(Player player) {
        return new MinotaurDialogue(player);
    }

    /**
     * Instantiates a new Minotaur dialogue.
     */
    public MinotaurDialogue() {}

    /**
     * Instantiates a new Minotaur dialogue.
     *
     * @param player the player
     */
    public MinotaurDialogue(Player player) {
        super(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        if (inEquipment(player, Items.GUTHANS_HELM_4724, 1)) {
            npcl(FaceAnim.CHILD_NORMAL, "...");
            stage = 0;
            return true;
        }
        switch ((int) (Math.random() * 4)) {
            case 0:
                npcl(FaceAnim.CHILD_NORMAL, "All this walking about is making me angry.");
                stage = 6;
                break;
            case 1:
                npcl(FaceAnim.CHILD_NORMAL, "Can you tell me why we're not fighting yet?");
                stage = 10;
                break;
            case 2:
                npcl(FaceAnim.CHILD_NORMAL, "Hey, no-horns?");
                stage = 12;
                break;
            case 3:
                npcl(FaceAnim.CHILD_NORMAL, "Hey no-horns!");
                stage = 18;
                break;
        }
        return true;
    }

    @Override
    public boolean handle(int interfaceId, int buttonId) {
        switch (stage) {
            case 0:
                playerl(FaceAnim.HALF_ASKING, "What?");
                stage++;
                break;
            case 1:
                npcl(FaceAnim.CHILD_NORMAL, "Are you having a laugh?");
                stage++;
                break;
            case 2:
                playerl(FaceAnim.FRIENDLY, "I'm not sure I know what you-");
                stage++;
                break;
            case 3:
                npcl(FaceAnim.CHILD_NORMAL,
                    "Listen, no-horns, you have two choices: take off the horns yourself or I'll headbutt you until they fall off.");
                stage++;
                break;
            case 4:
                playerl(FaceAnim.FRIENDLY, "Yessir.");
                stage++;
                break;
            case 5:
                npcl(FaceAnim.CHILD_NORMAL, "Good, no-horns. Let's not have this conversation again.");
                stage = END_DIALOGUE;
                break;
            case 6:
                playerl(FaceAnim.FRIENDLY, "You seem to be quite happy about that.");
                stage++;
                break;
            case 7:
                npcl(FaceAnim.CHILD_NORMAL,
                    "Yeah! There's nothing like getting a good rage on and then working it out on some no-horns.");
                stage++;
                break;
            case 8:
                playerl(FaceAnim.FRIENDLY, "I can't say I know what you mean.");
                stage++;
                break;
            case 9:
                npcl(FaceAnim.CHILD_NORMAL, "Well I didn't think a no-horns like you would get it!");
                stage = END_DIALOGUE;
                break;
            case 10:
                playerl(FaceAnim.FRIENDLY, "Buck up; I'll find you something to hit soon.");
                stage++;
                break;
            case 11:
                npcl(FaceAnim.CHILD_NORMAL,
                    "You'd better, no-horns, because that round head of yours is looking mighty axeable.");
                stage = END_DIALOGUE;
                break;
            case 12:
                playerl(FaceAnim.HALF_ASKING, "Why do you keep calling me no-horns?");
                stage++;
                break;
            case 13:
                npcl(FaceAnim.CHILD_NORMAL, "Do I really have to explain that?");
                stage++;
                break;
            case 14:
                playerl(FaceAnim.FRIENDLY, "No, thinking about it, it's pretty self-evident.");
                stage++;
                break;
            case 15:
                npcl(FaceAnim.CHILD_NORMAL, "Glad we're on the same page, no-horns.");
                stage++;
                break;
            case 16:
                playerl(FaceAnim.FRIENDLY, "So, what did you want?");
                stage++;
                break;
            case 17:
                npcl(FaceAnim.CHILD_NORMAL, "I've forgotten, now. I'm sure it'll come to me later.");
                stage = END_DIALOGUE;
                break;
            case 18:
                playerl(FaceAnim.HALF_ASKING, "Yes?");
                stage++;
                break;
            case 19:
                npcl(FaceAnim.CHILD_NORMAL, "Oh, I don't have anything to say, I was just yelling at you.");
                stage++;
                break;
            case 20:
                playerl(FaceAnim.HALF_ASKING, "Why?");
                stage++;
                break;
            case 21:
                npcl(FaceAnim.CHILD_NORMAL, "No reason. I do like to mess with the no-horns, though.");
                stage = END_DIALOGUE;
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{
            NPCs.BRONZE_MINOTAUR_6853, NPCs.BRONZE_MINOTAUR_6854,
            NPCs.IRON_MINOTAUR_6855, NPCs.IRON_MINOTAUR_6856,
            NPCs.STEEL_MINOTAUR_6857, NPCs.STEEL_MINOTAUR_6858,
            NPCs.MITHRIL_MINOTAUR_6859, NPCs.MITHRIL_MINOTAUR_6860,
            NPCs.ADAMANT_MINOTAUR_6861, NPCs.ADAMANT_MINOTAUR_6862,
            NPCs.RUNE_MINOTAUR_6863, NPCs.RUNE_MINOTAUR_6864
        };
    }
}
