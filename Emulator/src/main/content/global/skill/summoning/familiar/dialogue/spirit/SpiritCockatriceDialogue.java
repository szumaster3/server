package content.global.skill.summoning.familiar.dialogue.spirit;

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
 * The type Spirit cockatrice dialogue.
 */
@Initializable
public class SpiritCockatriceDialogue extends Dialogue {

    @Override
    public Dialogue newInstance(Player player) {
        return new SpiritCockatriceDialogue(player);
    }

    /**
     * Instantiates a new Spirit cockatrice dialogue.
     */
    public SpiritCockatriceDialogue() {}

    /**
     * Instantiates a new Spirit cockatrice dialogue.
     *
     * @param player the player
     */
    public SpiritCockatriceDialogue(Player player) {
        super(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        if (inEquipment(player, Items.MIRROR_SHIELD_4156, 1)) {
            npcl(FaceAnim.OLD_NORMAL, "You know, I'm sensing some trust issues here.");
            stage = 0;
            return true;
        }

        int randomIndex = (int) (Math.random() * 4);
        switch (randomIndex) {
            case 0:
                npcl(FaceAnim.OLD_NORMAL, "Is this what you do for fun?");
                stage = 11;
                break;
            case 1:
                playerl(FaceAnim.FRIENDLY, "You know, I think I might train as a hypnotist.");
                stage = 14;
                break;
            case 2:
                npcl(FaceAnim.OLD_NORMAL, "Come on, let's have a staring contest!");
                stage = 21;
                break;
            case 3:
                npcl(FaceAnim.OLD_NORMAL, "You know, sometimes I don't think we're good friends.");
                stage = 24;
                break;
        }
        return true;
    }

    @Override
    public boolean handle(int interfaceId, int buttonId) {
        switch (stage) {
            case 0:
                playerl(FaceAnim.FRIENDLY, "I'm not sure I know what you are talking about.");
                stage++;
                break;
            case 1:
                npcl(FaceAnim.OLD_NORMAL, "What are you holding?");
                stage++;
                break;
            case 2:
                playerl(FaceAnim.FRIENDLY, "A mirror shield.");
                stage++;
                break;
            case 3:
                npcl(FaceAnim.OLD_NORMAL, "And what do those do?");
                stage++;
                break;
            case 4:
                playerl(FaceAnim.FRIENDLY, "Mumblemumble...");
                stage++;
                break;
            case 5:
                npcl(FaceAnim.OLD_NORMAL, "What was that?");
                stage++;
                break;
            case 6:
                playerl(FaceAnim.FRIENDLY, "It protects me from your gaze attack.");
                stage++;
                break;
            case 7:
                npcl(FaceAnim.OLD_NORMAL, "See! Why would you need one unless you didn't trust me?");
                stage++;
                break;
            case 8:
                playerl(FaceAnim.FRIENDLY, "Who keeps demanding that we stop and have staring contests?");
                stage++;
                break;
            case 9:
                npcl(FaceAnim.OLD_NORMAL, "How about we drop this and call it even?");
                stage++;
                break;
            case 10:
                playerl(FaceAnim.FRIENDLY, "Fine by me.");
                stage = END_DIALOGUE;
                break;
            case 11:
                playerl(FaceAnim.FRIENDLY, "Sometimes. Why, what do you do for fun?");
                stage++;
                break;
            case 12:
                npcl(FaceAnim.OLD_NORMAL, "I find things and glare at them until they die!");
                stage++;
                break;
            case 13:
                playerl(FaceAnim.FRIENDLY, "Well...everyone needs a hobby, I suppose.");
                stage = END_DIALOGUE;
                break;
            case 14:
                playerl(FaceAnim.HALF_ASKING, "Isn't that an odd profession for a cockatrice?");
                stage++;
                break;
            case 15:
                npcl(FaceAnim.OLD_NORMAL, "Not at all! I've already been practicing!");
                stage++;
                break;
            case 16:
                playerl(FaceAnim.FRIENDLY, "Oh, really? How is that going?");
                stage++;
                break;
            case 17:
                npcl(FaceAnim.OLD_NORMAL, "Not good. I tell them to look in my eyes and that they are feeling sleepy.");
                stage++;
                break;
            case 18:
                playerl(FaceAnim.FRIENDLY, "I think I can see where this is headed.");
                stage++;
                break;
            case 19:
                npcl(FaceAnim.OLD_NORMAL, "And then they just lie there and stop moving.");
                stage++;
                break;
            case 20:
                playerl(FaceAnim.FRIENDLY, "I hate being right sometimes.");
                stage = END_DIALOGUE;
                break;
            case 21:
                playerl(FaceAnim.FRIENDLY, "You win!");
                stage++;
                break;
            case 22:
                npcl(FaceAnim.OLD_NORMAL, "Yay! I win again!");
                stage++;
                break;
            case 23:
                playerl(FaceAnim.FRIENDLY, "Oh, it's no contest alright.");
                stage = END_DIALOGUE;
                break;
            case 24:
                playerl(FaceAnim.HALF_ASKING, "What do you mean?");
                stage++;
                break;
            case 25:
                npcl(FaceAnim.OLD_NORMAL, "Well, you never make eye contact with me for a start.");
                stage++;
                break;
            case 26:
                playerl(FaceAnim.HALF_ASKING, "What happened the last time someone made eye contact with you?");
                stage++;
                break;
            case 27:
                npcl(FaceAnim.CHILD_NORMAL, "Oh, I petrified them really good! Ooooh...okay, point taken.");
                stage = END_DIALOGUE;
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{NPCs.SPIRIT_COCKATRICE_6875, NPCs.SPIRIT_COCKATRICE_6876};
    }
}
