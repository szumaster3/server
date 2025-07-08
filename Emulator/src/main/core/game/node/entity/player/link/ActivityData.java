package core.game.node.entity.player.link;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * The type Activity data.
 */
public final class ActivityData {
    private int pestPoints;
    private int warriorGuildTokens;
    private int bountyHunterRate;
    private int bountyRogueRate;
    private boolean[] barrowBrothers = new boolean[6];
    private int barrowKills;
    private int barrowTunnelIndex;
    private int kolodionStage;
    private int[] godCasts = new int[3];
    private int kolodionBoss;
    private boolean elnockSupplies;
    private long lastBorkBattle;
    private byte borkKills;
    private boolean lostCannon;
    private boolean startedMta;
    private boolean bonesToPeaches;
    private int solvedMazes;
    private int fogRating;
    private boolean hardcoreDeath;
    boolean topGrabbed;

    /**
     * Instantiates a new Activity data.
     */
    public ActivityData() {

    }

    /**
     * Parse.
     *
     * @param data the data
     */
    public void parse(JSONObject data) {
        pestPoints = Integer.parseInt(data.get("pestPoints").toString());
        warriorGuildTokens = Integer.parseInt(data.get("warriorGuildTokens").toString());
        bountyHunterRate = Integer.parseInt(data.get("bountyHunterRate").toString());
        bountyRogueRate = Integer.parseInt(data.get("bountyRogueRate").toString());
        barrowKills = Integer.parseInt(data.get("barrowKills").toString());
        JSONArray bb = (JSONArray) data.get("barrowBrothers");
        for (int i = 0; i < bb.size(); i++) {
            barrowBrothers[i] = (boolean) bb.get(i);
        }
        barrowTunnelIndex = Integer.parseInt(data.get("barrowTunnelIndex").toString());
        kolodionStage = Integer.parseInt(data.get("kolodionStage").toString());
        JSONArray gc = (JSONArray) data.get("godCasts");
        for (int i = 0; i < gc.size(); i++) {
            godCasts[i] = Integer.parseInt(gc.get(i).toString());
        }
        kolodionBoss = Integer.parseInt(data.get("kolodionBoss").toString());
        elnockSupplies = (boolean) data.get("elnockSupplies");
        lastBorkBattle = Long.parseLong(data.get("lastBorkBattle").toString());
        startedMta = (boolean) data.get("startedMta");
        lostCannon = (boolean) data.get("lostCannon");
        bonesToPeaches = (boolean) data.get("bonesToPeaches");
        solvedMazes = Integer.parseInt(data.get("solvedMazes").toString());
        fogRating = Integer.parseInt(data.get("fogRating").toString());
        borkKills = Byte.parseByte(data.get("borkKills").toString());
        hardcoreDeath = (boolean) data.get("hardcoreDeath");
        topGrabbed = (boolean) data.get("topGrabbed");
    }

    /**
     * Is elnock supplies boolean.
     *
     * @return the boolean
     */
    public boolean isElnockSupplies() {
        return elnockSupplies;
    }

    /**
     * Sets elnock supplies.
     *
     * @param elnockSupplies the elnock supplies
     */
    public void setElnockSupplies(boolean elnockSupplies) {
        this.elnockSupplies = elnockSupplies;
    }

    /**
     * Increase pest points.
     *
     * @param pestPoints the pest points
     */
    public void increasePestPoints(int pestPoints) {
        if (pestPoints + this.pestPoints > 500) {
            this.pestPoints = 500;
        } else {
            this.pestPoints += pestPoints;
        }
    }

    /**
     * Decrease pest points.
     *
     * @param pestPoints the pest points
     */
    public void decreasePestPoints(int pestPoints) {
        this.pestPoints -= pestPoints;
    }

    /**
     * Gets pest points.
     *
     * @return the pest points
     */
    public int getPestPoints() {
        return pestPoints;
    }

    /**
     * Sets pest points.
     *
     * @param pestPoints the pest points
     */
    public void setPestPoints(int pestPoints) {
        this.pestPoints = pestPoints;
    }

    /**
     * Gets warrior guild tokens.
     *
     * @return the warrior guild tokens
     */
    public int getWarriorGuildTokens() {
        return warriorGuildTokens;
    }

    /**
     * Sets warrior guild tokens.
     *
     * @param warriorGuildTokens the warrior guild tokens
     */
    public void setWarriorGuildTokens(int warriorGuildTokens) {
        this.warriorGuildTokens = warriorGuildTokens;
    }

    /**
     * Update warrior tokens.
     *
     * @param amount the amount
     */
    public void updateWarriorTokens(int amount) {
        this.warriorGuildTokens += amount;
    }

    /**
     * Gets bounty hunter rate.
     *
     * @return the bounty hunter rate
     */
    public int getBountyHunterRate() {
        return bountyHunterRate;
    }

    /**
     * Update bounty hunter rate.
     *
     * @param rate the rate
     */
    public void updateBountyHunterRate(int rate) {
        this.bountyHunterRate += rate;
    }

    /**
     * Gets bounty rogue rate.
     *
     * @return the bounty rogue rate
     */
    public int getBountyRogueRate() {
        return bountyRogueRate;
    }

    /**
     * Update bounty rogue rate.
     *
     * @param rate the rate
     */
    public void updateBountyRogueRate(int rate) {
        this.bountyRogueRate += rate;
    }

    /**
     * Get barrow brothers boolean [ ].
     *
     * @return the boolean [ ]
     */
    public boolean[] getBarrowBrothers() {
        return barrowBrothers;
    }

    /**
     * Sets barrow brothers.
     *
     * @param barrowBrothers the barrow brothers
     */
    public void setBarrowBrothers(boolean[] barrowBrothers) {
        this.barrowBrothers = barrowBrothers;
    }

    /**
     * Gets barrow kills.
     *
     * @return the barrow kills
     */
    public int getBarrowKills() {
        return barrowKills;
    }

    /**
     * Sets barrow kills.
     *
     * @param barrowKills the barrow kills
     */
    public void setBarrowKills(int barrowKills) {
        if (barrowKills > 10000) {
            barrowKills = 10000;
        }
        this.barrowKills = barrowKills;
    }

    /**
     * Gets barrow tunnel index.
     *
     * @return the barrow tunnel index
     */
    public int getBarrowTunnelIndex() {
        return barrowTunnelIndex;
    }

    /**
     * Sets barrow tunnel index.
     *
     * @param barrowTunnelIndex the barrow tunnel index
     */
    public void setBarrowTunnelIndex(int barrowTunnelIndex) {
        this.barrowTunnelIndex = barrowTunnelIndex;
    }

    /**
     * Sets kolodion stage.
     *
     * @param stage the stage
     */
    public void setKolodionStage(int stage) {
        this.kolodionStage = stage;
    }

    /**
     * Has started kolodion boolean.
     *
     * @return the boolean
     */
    public boolean hasStartedKolodion() {
        return kolodionStage == 1;
    }

    /**
     * Has killed kolodion boolean.
     *
     * @return the boolean
     */
    public boolean hasKilledKolodion() {
        return kolodionStage >= 2;
    }

    /**
     * Has received kolodion reward boolean.
     *
     * @return the boolean
     */
    public boolean hasReceivedKolodionReward() {
        return kolodionStage == 3;
    }

    /**
     * Get god casts int [ ].
     *
     * @return the int [ ]
     */
    public int[] getGodCasts() {
        return godCasts;
    }

    /**
     * Gets kolodion boss.
     *
     * @return the kolodion boss
     */
    public int getKolodionBoss() {
        return kolodionBoss;
    }

    /**
     * Sets kolodion boss.
     *
     * @param kolodionBoss the kolodion boss
     */
    public void setKolodionBoss(int kolodionBoss) {
        this.kolodionBoss = kolodionBoss;
    }

    /**
     * Gets last bork battle.
     *
     * @return the last bork battle
     */
    public long getLastBorkBattle() {
        return lastBorkBattle;
    }

    /**
     * Sets last bork battle.
     *
     * @param lastBorkBattle the last bork battle
     */
    public void setLastBorkBattle(long lastBorkBattle) {
        this.lastBorkBattle = lastBorkBattle;
    }

    /**
     * Has killed bork boolean.
     *
     * @return the boolean
     */
    public boolean hasKilledBork() {
        return lastBorkBattle > 0;
    }

    /**
     * Is lost cannon boolean.
     *
     * @return the boolean
     */
    public boolean isLostCannon() {
        return lostCannon;
    }

    /**
     * Sets lost cannon.
     *
     * @param lostCannon the lost cannon
     */
    public void setLostCannon(boolean lostCannon) {
        this.lostCannon = lostCannon;
    }

    /**
     * Is started mta boolean.
     *
     * @return the boolean
     */
    public boolean isStartedMta() {
        return startedMta;
    }

    /**
     * Sets started mta.
     *
     * @param startedMta the started mta
     */
    public void setStartedMta(boolean startedMta) {
        this.startedMta = startedMta;
    }

    /**
     * Is bones to peaches boolean.
     *
     * @return the boolean
     */
    public boolean isBonesToPeaches() {
        return bonesToPeaches;
    }

    /**
     * Sets bones to peaches.
     *
     * @param bonesToPeaches the bones to peaches
     */
    public void setBonesToPeaches(boolean bonesToPeaches) {
        this.bonesToPeaches = bonesToPeaches;
    }

    /**
     * Gets solved mazes.
     *
     * @return the solved mazes
     */
    public int getSolvedMazes() {
        return solvedMazes;
    }

    /**
     * Sets solved mazes.
     *
     * @param solvedMazes the solved mazes
     */
    public void setSolvedMazes(int solvedMazes) {
        this.solvedMazes = solvedMazes;
    }

    /**
     * Gets fog rating.
     *
     * @return the fog rating
     */
    public int getFogRating() {
        return fogRating;
    }

    /**
     * Sets fog rating.
     *
     * @param fogRating the fog rating
     */
    public void setFogRating(int fogRating) {
        this.fogRating = fogRating;
    }

    /**
     * Gets bork kills.
     *
     * @return the bork kills
     */
    public byte getBorkKills() {
        return borkKills;
    }

    /**
     * Sets bork kills.
     *
     * @param borkKills the bork kills
     */
    public void setBorkKills(byte borkKills) {
        this.borkKills = borkKills;
    }

    /**
     * Gets hardcore death.
     *
     * @return the hardcore death
     */
    public boolean getHardcoreDeath() {
        return hardcoreDeath;
    }

    /**
     * Sets hardcore death.
     *
     * @param hardcoreDeath the hardcore death
     */
    public void setHardcoreDeath(boolean hardcoreDeath) {
        this.hardcoreDeath = hardcoreDeath;
    }

    /**
     * Sets top grabbed.
     *
     * @param topGrabbed the top grabbed
     */
    public void setTopGrabbed(boolean topGrabbed) {
        this.topGrabbed = topGrabbed;
    }

    /**
     * Is top grabbed boolean.
     *
     * @return the boolean
     */
    public boolean isTopGrabbed() {
        return topGrabbed;
    }

    /**
     * Gets kolodion stage.
     *
     * @return the kolodion stage
     */
    public int getKolodionStage() {
        return kolodionStage;
    }
}