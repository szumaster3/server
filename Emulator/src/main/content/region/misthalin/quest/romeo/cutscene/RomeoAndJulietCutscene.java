package content.region.misthalin.quest.romeo.cutscene;

import org.rs.consts.Quests;
import core.game.activity.ActivityPlugin;
import core.game.activity.CutscenePlugin;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.world.map.Direction;
import core.game.world.map.Location;
import core.game.world.map.build.DynamicRegion;
import core.net.packet.PacketRepository;
import core.net.packet.context.CameraContext;
import core.net.packet.context.CameraContext.CameraType;
import core.net.packet.out.CameraViewPacket;
import core.plugin.Initializable;

/**
 * The type Romeo and juliet cutscene.
 */
@Initializable
public final class RomeoAndJulietCutscene extends CutscenePlugin {

    private static final Location SPAWN_LOCATION = Location.create(3211, 3424, 0);

    /**
     * Instantiates a new Romeo and juliet cutscene.
     */
    public RomeoAndJulietCutscene() {
        this(null);
    }

    /**
     * Instantiates a new Romeo and juliet cutscene.
     *
     * @param player the player
     */
    public RomeoAndJulietCutscene(final Player player) {
        super("Romeo & Juliet Cutscene");
        this.player = player;
    }

    @Override
    public boolean start(final Player player, final boolean login, Object... args) {
        final NPC romeo = NPC.create(639, base.transform(30, 37, 0));
        romeo.setDirection(Direction.EAST);
        romeo.faceLocation(getBase().transform(30, 38, 0));
        romeo.getLocks().lockMovement(10000);
        romeo.setWalks(false);
        npcs.add(NPC.create(639, base.transform(30, 37, 0)));
        for (NPC npc : npcs) {
            npc.init();
        }
        return super.start(player, login, args);
    }

    @Override
    public void open() {
        npcs.get(0).lock();
        player.getDialogueInterpreter().open(npcs.get(0).getId(), npcs.get(0), this);
        int x = player.getLocation().getX();
        int y = player.getLocation().getY();
        CameraContext rot = null;
        CameraContext pos = null;
        int height = 450;
        int speed = 100;
        int other = 1;
        pos = new CameraContext(player, CameraType.POSITION, x - 5, y - 4, height, other, speed);
        rot = new CameraContext(player, CameraType.ROTATION, x + 2, y, height, other, speed);
        PacketRepository.send(CameraViewPacket.class, pos);
        PacketRepository.send(CameraViewPacket.class, rot);
        player.lock();
        player.getLocks().lockMovement(1000000);
    }

    @Override
    public void configure() {
        region = DynamicRegion.create(9288);
        setRegionBase();
        registerRegion(region.getId());
    }

    @Override
    public void fade() {
        player.getQuestRepository().getQuest(Quests.ROMEO_JULIET).finish(player);
    }

    @Override
    public Location getStartLocation() {
        return getBase().transform(30, 38, 0);
    }

    @Override
    public ActivityPlugin newInstance(Player p) throws Throwable {
        return new RomeoAndJulietCutscene(p);
    }

    @Override
    public Location getSpawnLocation() {
        return SPAWN_LOCATION;
    }

    /**
     * Gets phillipia.
     *
     * @return the phillipia
     */
    public NPC getPhillipia() {
        return npcs.get(1);
    }

}
