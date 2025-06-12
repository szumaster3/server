package content.region.karamja.tzhaar_city.plugin;

import content.global.skill.slayer.SlayerManager;
import content.global.skill.slayer.Tasks;
import core.api.utils.BossKillCounter;
import core.game.activity.ActivityPlugin;
import core.game.dialogue.FaceAnim;
import core.game.event.NPCKillEvent;
import core.game.interaction.Option;
import core.game.node.Node;
import core.game.node.entity.Entity;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.diary.DiaryType;
import core.game.node.entity.skill.Skills;
import core.game.node.item.GroundItemManager;
import core.game.node.item.Item;
import core.game.node.scenery.Scenery;
import core.game.system.task.Pulse;
import core.game.world.GameWorld;
import core.game.world.map.Location;
import core.game.world.map.build.DynamicRegion;
import core.game.world.map.zone.ZoneRestriction;
import core.plugin.Initializable;
import core.tools.RandomFunction;
import org.rs.consts.Items;
import org.rs.consts.Music;
import org.rs.consts.NPCs;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static core.api.ContentAPIKt.*;

/**
 * The type Tzhaar fight caves plugin.
 */
@Initializable
public final class TzhaarFightCavePlugin extends ActivityPlugin {

    private static final int[][] WAVES = {{2734, 0}, {2736, 2}, {2739, 6}, {2741, 14}, {2743, 30}, {2745, 62}};

    private static final Location[] SPAWN_LOCATIONS = {Location.create(8, 44, 0), Location.create(38, 47, 0), Location.create(5, 9, 0), Location.create(46, 21, 0), Location.create(28, 28, 0),};

    /**
     * The Active np cs.
     */
    public List<NPC> activeNPCs = new ArrayList<>(20);

    /**
     * Instantiates a new Tzhaar fight caves plugin.
     */
    public TzhaarFightCavePlugin() {
        this(null);
    }

    /**
     * Instantiates a new Tzhaar fight caves plugin.
     *
     * @param player the player
     */
    public TzhaarFightCavePlugin(Player player) {
        super("fight caves", true, true, true, ZoneRestriction.CANNON, ZoneRestriction.RANDOM_EVENTS);
        super.player = player;
    }

    @Override
    public void register() {
        try {
            new TzhaarFightCaveNPC().newInstance(null);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean canLogout(Player p) {
        if (!p.getAttribute("fc_safe_logout", false)) {
            p.setAttribute("fc_safe_logout", true);
            p.getPacketDispatch().sendMessage("<col=4C0E00>Your logout request has been received. The minigame will be paused at the end");
            p.getPacketDispatch().sendMessage("<col=4C0E00>of this wave.");
            p.getPacketDispatch().sendMessage("<col=4C0E00>If you try to logout before that, you will have to repeat this wave.");
            return false;
        }
        return super.canLogout(p);
    }

    @Override
    public boolean start(final Player player, boolean login, java.lang.Object... args) {
        player.getAchievementDiaryManager().finishTask(player, DiaryType.KARAMJA, 0, 8);
        this.player = player;
        player.lock(3);
        int offsetX = player.getAttribute("fc_offset", 45 << 8) >> 8;
        int offsetY = player.getAttribute("fc_offset", 61) & 0xff;
        removeAttribute(player, "fc_offset");
        player.getProperties().setTeleportLocation(getBase().transform(offsetX, offsetY, 0));
        Pulse pulse;
        boolean practice = player.getAttribute("fc_practice_jad", false);
        if (!login && !practice) {
            final int wave = 0;
            setAttribute(player, "fc_wave", wave);
            player.getWalkingQueue().reset();
            pulse = new Pulse(1, player) {
                int count = 0;

                @Override
                public boolean pulse() {
                    if (count++ == 0) {
                        player.getWalkingQueue().reset();
                        player.getWalkingQueue().addPath(getBase().getX() + 44, getBase().getY() + 49, true);
                        player.getWalkingQueue().addPath(getBase().getX() + 32, getBase().getY() + 32, true);
                        player.getDialogueInterpreter().sendDialogues(NPCs.TZHAAR_MEJ_JAL_2617, FaceAnim.CHILD_GUILTY, "You're on your own now, JalYt.", "Prepare to fight for your life!");
                    } else if (count == 16) {
                        spawnWave(wave);
                        return true;
                    }
                    return false;
                }
            };
        } else {
            if (practice) {
                setAttribute(player, "fc_wave", 62);
            }
            if (player.getAttribute("fc_wave", 0) == 62) {
                player.getDialogueInterpreter().sendDialogues(NPCs.TZHAAR_MEJ_JAL_2617, FaceAnim.CHILD_GUILTY, "Look out, here comes TzTok-Jad!");
            }
            pulse = new Pulse(16, player) {
                @Override
                public boolean pulse() {
                    spawnWave(player.getAttribute("fc_wave", 0));
                    return true;
                }
            };
        }
        setAttribute(player, "fc:pulse", pulse);
        GameWorld.getPulser().submit(pulse);
        return true;
    }

    /**
     * Leave.
     *
     * @param player the player
     * @param wave   the wave
     */
    public void leave(Player player, int wave) {
        activeNPCs.clear();
        player.getProperties().setTeleportLocation(getSpawnLocation());
        player.getSkills().restore();
        boolean practice = player.getAttribute("fc_practice_jad", false);
        if (wave == 63) {
            if (!practice) {
                if (!player.getInventory().add(new Item(Items.FIRE_CAPE_6570))) {
                    GroundItemManager.create(new Item(Items.FIRE_CAPE_6570), getSpawnLocation(), player);
                }
            } else {

                int amount = 8000;
                if (!player.getInventory().add(new Item(Items.TOKKUL_6529, amount))) {
                    GroundItemManager.create(new Item(Items.TOKKUL_6529, amount), getSpawnLocation(), player);
                }
            }
            player.getPacketDispatch().sendMessage("You were victorious!");
            if (!practice) {
                BossKillCounter.addToKillCount(player, NPCs.TZTOK_JAD_2745);
                if (SlayerManager.getInstance(player).getTask() == Tasks.JAD) {
                    player.getSkills().addExperience(Skills.SLAYER, 25000);
                    SlayerManager.getInstance(player).clear();
                    player.sendMessage("You receive 25,000 slayer experience for defeating TzTok-Jad.");
                }
                playJingle(player, 76);
                player.getDialogueInterpreter().sendDialogues(NPCs.TZHAAR_MEJ_JAL_2617, FaceAnim.CHILD_GUILTY, "You even defeated TzTok-Jad, I am most impressed!", "Please accept this gift as a reward.");
                sendNews(player.getUsername() + " has been victorious in defeating TzTok-Jad for a firecape!");
            } else {
                player.getDialogueInterpreter().sendDialogues(NPCs.TZHAAR_MEJ_JAL_2617, FaceAnim.CHILD_GUILTY, "You defeated TzTok-Jad. I am most impressed!", "Here is you TokKul back.", "Maybe next time you do all training, and get real reward...");
            }
        } else if (wave <= 1) {
            player.getDialogueInterpreter().sendDialogues(NPCs.TZHAAR_MEJ_JAL_2617, FaceAnim.CHILD_GUILTY, "Well I suppose you tried... better luck next time.");
        } else {
            if (!practice) {
                player.getDialogueInterpreter().sendDialogues(NPCs.TZHAAR_MEJ_JAL_2617, FaceAnim.CHILD_GUILTY, "Well done in the cave, here, take TokKul as reward.");
            } else {
                player.getDialogueInterpreter().sendDialogues(NPCs.TZHAAR_MEJ_JAL_2617, FaceAnim.CHILD_GUILTY, "You both impatient and also failure.", "Better luck next time.");
            }
        }

        if (!practice) {
            int amount = wave << 7;
            if (amount > 0 && !player.getInventory().add(new Item(Items.TOKKUL_6529, amount))) {
                GroundItemManager.create(new Item(Items.TOKKUL_6529, amount), getSpawnLocation(), player);
            }
        }

    }

    @Override
    public boolean enter(final Entity e) {
        e.setAttribute("fight_caves", true);
        return super.enter(e);
    }

    @Override
    public boolean leave(final Entity e, boolean logout) {
        unregisterRegion(region.getId());
        if (logout) {
            Location l = player.getLocation();
            setAttribute(player, "/save:fc_offset", (l.getX() - getBase().getX()) << 8 | (l.getY() - getBase().getY()));
        } else {
            removeAttribute(player, "fc_offset");
            removeAttribute(player, "fc_wave");
            removeAttribute(player, "fc_practice_jad");
            removeAttribute(player, "fc_safe_logout");
            Pulse pulse = player.getAttribute("fc:pulse");
            if (pulse != null) {
                pulse.stop();
                removeAttribute(player, "fc:pulse");
            }
        }
        removeAttribute(player, "fight_caves");
        return super.leave(e, logout);
    }

    @Override
    public boolean death(Entity e, Entity killer) {
        if (e instanceof NPC) {
            NPC n = (NPC) e;
            if (n.getId() == NPCs.TZ_KEK_2736 || n.getId() == NPCs.TZ_KEK_2737) {
                new TzhaarFightCaveNPC(NPCs.TZ_KEK_2738, n.getLocation(), this).init();
                new TzhaarFightCaveNPC(NPCs.TZ_KEK_2738, n.getLocation().transform(1, 0, 0), this).init();
            }
            if (activeNPCs.contains(n)) {
                activeNPCs.remove(n);
                if (n.getId() == NPCs.TZTOK_JAD_2745) {
                    setAttribute(player, "/save:fc_wave", 63);
                    leave((Player) killer, 63);
                } else if (activeNPCs.isEmpty()) {
                    final int wave = player.getAttribute("fc_wave", 0) + 1;
                    setAttribute(player, "/save:fc_wave", wave);
                    if (player.getAttribute("fc_safe_logout", false)) {
                        player.getPacketDispatch().sendLogout();
                    } else {
                        if (wave == 62) {
                            player.getDialogueInterpreter().sendDialogues(NPCs.TZHAAR_MEJ_JAL_2617, FaceAnim.CHILD_GUILTY, "Look out, here comes TzTok-Jad!");
                        }
                        Pulse pulse = new Pulse(9, player) {
                            @Override
                            public boolean pulse() {
                                spawnWave(wave);
                                return true;
                            }
                        };
                        setAttribute(player, "fc:pulse", pulse);
                        GameWorld.getPulser().submit(pulse);
                    }
                }
            }
            e.clear();
        } else {
            leave((Player) e, e.getAttribute("fc_wave", 0));
        }

        if (e instanceof NPC)
            killer.dispatch(new NPCKillEvent(e.asNpc()));
        return true;
    }

    /**
     * Spawn wave.
     *
     * @param wave the wave
     */
    public void spawnWave(int wave) {
        setAttribute(player, "/save:fc_wave", wave);
        int count = wave;
        for (int i = WAVES.length - 1; i >= 0; i--) {
            if (count >= WAVES[i][1]) {
                if (count > WAVES[i][1] * 2) {
                    spawn(WAVES[i][0] + 1);
                    count -= (WAVES[i][1] + 1);
                }
                spawn(WAVES[i][0]);
                count -= WAVES[i][1] + 1;
            }
        }
        player.getPacketDispatch().sendMessage("Current wave: " + (wave + 1) + ".");
    }

    /**
     * Spawn tzhaar fight cave npc.
     *
     * @param npcId the npc id
     * @return the tzhaar fight cave npc
     */
    public TzhaarFightCaveNPC spawn(int npcId) {
        Random r = RandomFunction.RANDOM;
        Location l = SPAWN_LOCATIONS[r.nextInt(SPAWN_LOCATIONS.length)];
        TzhaarFightCaveNPC npc = new TzhaarFightCaveNPC(npcId, getBase().transform(l).transform(r.nextInt(8), r.nextInt(8), 0), this);
        npc.init();
        return npc;
    }

    @Override
    public boolean interact(Entity e, Node target, Option option) {
        if (target instanceof Scenery && target.getId() == org.rs.consts.Scenery.CAVE_ENTRANCE_9357) {
            leave((Player) e, e.getAttribute("fc_wave", 0));
            removeAttribute(player, "fc_wave");
            removeAttribute(player, "fc_offset");
            removeAttribute(player, "fc_practice_jad");
            return true;
        }
        return false;
    }

    @Override
    public ActivityPlugin newInstance(Player p) throws Throwable {
        return new TzhaarFightCavePlugin(p);
    }

    @Override
    public Location getSpawnLocation() {
        return Location.create(2438, 5169, 0);
    }

    @Override
    public void configure() {
        region = DynamicRegion.create(9551);
        setRegionBase();
        registerRegion(region.getId());
        region.setMusicId(Music.TZHAAR_473);
    }

}
