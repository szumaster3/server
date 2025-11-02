package content.region.misthalin.varrock.quest.dslay.cutscene;

import core.game.activity.ActivityPlugin;
import core.game.activity.CutscenePlugin;
import core.game.node.entity.player.Player;
import core.game.world.map.Location;
import core.game.world.map.build.DynamicRegion;
import core.game.world.repository.Repository;
import core.net.packet.PacketRepository;
import core.net.packet.context.CameraContext;
import core.net.packet.out.CameraViewPacket;
import shared.consts.Music;
import shared.consts.NPCs;
import shared.consts.Quests;

import static core.api.ContentAPIKt.playJingle;

/**
 * The type Wally cutscene plugin.
 */
public class WallyCutscenePlugin extends CutscenePlugin {

    /**
     * Instantiates a new Wally cutscene plugin.
     */
    public WallyCutscenePlugin() {
        this(null);
    }

    /**
     * Instantiates a new Wally cutscene plugin.
     *
     * @param player the player
     */
    public WallyCutscenePlugin(final Player player) {
        super("Wally cutscene");
        this.player = player;
    }

    @Override
    public ActivityPlugin newInstance(Player p) throws Throwable {
        return new WallyCutscenePlugin(p);
    }

    @Override
    public void open() {
        player.lock();
        PacketRepository.send(CameraViewPacket.class, new CameraContext(player, CameraContext.CameraType.POSITION, player.getLocation().getX() + 2, player.getLocation().getY() + 3, 305, 1, 35));
        PacketRepository.send(CameraViewPacket.class, new CameraContext(player, CameraContext.CameraType.ROTATION, player.getLocation().getX() + 10, player.getLocation().getY() + 12, 305, 1, 35));
        player.getDialogueInterpreter().open(NPCs.GYPSY_ARIS_882, Repository.findNPC(NPCs.GYPSY_ARIS_882), this);
    }

    @Override
    public void end() {
        player.unlock();
    }

    @Override
    public void fade() {
        playJingle(player, Music.WALLY_CUTSCENE_196);
        player.getQuestRepository().getQuest(Quests.DEMON_SLAYER).start(player);
        player.getDialogueInterpreter().open(NPCs.GYPSY_ARIS_882, Repository.findNPC(NPCs.GYPSY_ARIS_882), this);
    }

    @Override
    public Location getSpawnLocation() {
        return Location.create(3203, 3423, 0);
    }

    @Override
    public Location getStartLocation() {
        return base.transform(26, 38, 0);
    }

    @Override
    public void configure() {
        region = DynamicRegion.create(12852);
        setRegionBase();
        registerRegion(region.getId());
    }

}
