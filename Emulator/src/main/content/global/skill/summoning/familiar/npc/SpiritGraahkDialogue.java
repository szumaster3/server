package content.global.skill.summoning.familiar.npc;

import content.global.skill.summoning.familiar.Familiar;
import core.game.dialogue.Dialogue;
import core.game.dialogue.FaceAnim;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.TeleportManager.TeleportType;
import core.game.world.map.Location;
import core.game.world.map.zone.impl.WildernessZone;
import core.plugin.Initializable;
import org.rs.consts.NPCs;

import static core.tools.DialogueHelperKt.END_DIALOGUE;

/**
 * The type Spirit graahk dialogue.
 */
@Initializable
public final class SpiritGraahkDialogue extends Dialogue {

    /**
     * Instantiates a new Spirit graahk dialogue.
     */
    public SpiritGraahkDialogue() {
    }

    /**
     * Instantiates a new Spirit graahk dialogue.
     *
     * @param player the player
     */
    public SpiritGraahkDialogue(Player player) {
        super(player);
    }

    @Override
    public Dialogue newInstance(Player player) {
        return new SpiritGraahkDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        if (!(npc instanceof Familiar)) {
            return false;
        }
        final Familiar fam = (Familiar) npc;
        if (fam.getOwner() != player) {
            player.getPacketDispatch().sendMessage("This is not your familiar.");
            return true;
        } else {
            interpreter.sendOptions("Select an Option", "Chat", "Teleport");
        }
        return true;
    }

    @Override
    public boolean handle(int interfaceId, int buttonId) {
        switch (buttonId) {
            case 1:
                int randomIndex = (int) (Math.random() * 4);
                switch (randomIndex) {
                    case 0:
                        playerl(FaceAnim.FRIENDLY, "Your spikes are looking particularly spiky today.");
                        stage = 10;
                        break;
                    case 1:
                        npcl(FaceAnim.OLD_DEFAULT, "My spikes hurt, could you pet them for me?");
                        stage = 16;
                        break;
                    case 2:
                        npcl(FaceAnim.OLD_DEFAULT, "Hi!");
                        stage = 17;
                        break;
                    case 3:
                        playerl(FaceAnim.FRIENDLY, "How's your day going?");
                        stage = 24;
                        break;
                }
                break;
            case 2:
                if (!WildernessZone.checkTeleport(player, 20)) {
                    player.sendMessage("You cannot teleport with the Graahk above level 20 wilderness.");
                    end();
                } else {
                    player.getTeleporter().send(new Location(2786, 3002), TeleportType.NORMAL);
                    end();
                }
                break;
            case 10:
                npcl(FaceAnim.OLD_DEFAULT, "Really, you think so?");
                stage++;
                break;
            case 11:
                playerl(FaceAnim.FRIENDLY, "Yes. Most pointy, indeed.");
                stage++;
                break;
            case 12:
                npcl(FaceAnim.OLD_DEFAULT, "That's really kind of you to say. I was going to spike you but I won't now...");
                stage++;
                break;
            case 13:
                playerl(FaceAnim.FRIENDLY, "Thanks?");
                stage++;
                break;
            case 14:
                npcl(FaceAnim.OLD_DEFAULT, "...I'll do it later instead.");
                stage++;
                break;
            case 15:
                playerl(FaceAnim.FRIENDLY, "*sigh!*");
                stage = END_DIALOGUE;
                break;
            case 16:
                playerl(FaceAnim.FRIENDLY, "Aww, of course I can I'll just... Oww! I think you drew blood that time.");
                stage = END_DIALOGUE;
                break;
            case 17:
                playerl(FaceAnim.FRIENDLY, "Hello. Are you going to spike me again?");
                stage++;
                break;
            case 18:
                npcl(FaceAnim.OLD_DEFAULT, "No, I got a present to apologise for last time.");
                stage++;
                break;
            case 19:
                playerl(FaceAnim.FRIENDLY, "That's really sweet, thank you.");
                stage++;
                break;
            case 20:
                npcl(FaceAnim.OLD_DEFAULT, "Here you go, it's a special cushion to make you comfortable.");
                stage++;
                break;
            case 21:
                playerl(FaceAnim.FRIENDLY, "It's made of spikes!");
                stage++;
                break;
            case 22:
                npcl(FaceAnim.OLD_DEFAULT, "Yes, but they're therapeutic spikes.");
                stage++;
                break;
            case 23:
                playerl(FaceAnim.FRIENDLY, "...");
                stage = END_DIALOGUE;
                break;
            case 24:
                npcl(FaceAnim.OLD_DEFAULT, "It's great! Actually I've got something to show you!");
                stage++;
                break;
            case 25:
                playerl(FaceAnim.FRIENDLY, "Oh? What's that?");
                stage++;
                break;
            case 26:
                npcl(FaceAnim.OLD_DEFAULT, "You'll need to get closer!");
                stage++;
                break;
            case 27:
                playerl(FaceAnim.FRIENDLY, "I can't see anything...");
                stage++;
                break;
            case 28:
                npcl(FaceAnim.OLD_DEFAULT, "It's really small - even closer.");
                stage++;
                break;
            case 29:
                playerl(FaceAnim.FRIENDLY, "Oww! I'm going to have your spikes trimmed!");
                stage = END_DIALOGUE;
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{NPCs.SPIRIT_GRAAHK_7363, NPCs.SPIRIT_GRAAHK_7364};
    }
}