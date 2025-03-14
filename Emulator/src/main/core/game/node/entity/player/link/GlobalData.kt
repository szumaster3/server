package core.game.node.entity.player.link

import content.data.GodBook
import org.json.simple.JSONArray
import org.json.simple.JSONObject

class GlobalData {
    private var runReplenishCharges: Int = 0
    private var lowAlchemyCharges: Int = 0
    private var forgingUses: Int = 0
    private var ectoCharges: Int = 0
    private var playerTestStage: Int = 0
    private var barrowsLoots: Int = 0
    private var lootSharePoints: Int = 0
    private var tutorialStage: Int = 0

    @JvmField
    var karamjaBananas: Int = 0
    private var savedX: Int = 0
    private var savedY: Int = 0
    private var savedH: Int = 0
    private var taskAmount: Int = 0
    private var taskPoints: Int = 0
    private var godBook: Int = -1
    private var zaffAmount: Int = 8
    private var essenceTeleporter: Int = 0
    private var recoilDamage: Int = 40
    private var hunterCapeCharges: Int = 0
    private var bossCounters: IntArray = IntArray(25)
    private val rcDecays: IntArray = IntArray(3)
    private var assistExperience: DoubleArray = DoubleArray(9)
    private var homeTeleportDelay: Long = 0
    private var assistTime: Long = 0
    private var chatPing: Long = 0
    private var tutorClaim: Long = 0
    private var magicSkillCapeDelay: Long = 0
    private var hunterCapeDelay: Long = 0
    private var charmingDelay: Long = 0
    private var overChargeDelay: Long = 0
    private var lootShareDelay: Long = 0
    private var doubleExp: Long = 0
    private var globalTeleporterDelay: Long = 0
    var starSpriteDelay: Long = 0
    private var runReplenishDelay: Long = 0
    private var lowAlchemyDelay: Long = 0
    private var minigameTeleportDelay: Long = 0
    private var silkSteal: Long = 0
    private var teaSteal: Long = 0
    private var zaffTime: Long = 0
    private var doubleExpDelay: Long = 0
    private var dropDelay: Long = 0
    private var luthasTask: Boolean = false
    private var lumbridgeRope: Boolean = false
    private var apprentice: Boolean = false
    private var fritzGlass: Boolean = false
    private var wydinEmployee: Boolean = false
    private var draynorRecording: Boolean = false
    private var geTutorial: Boolean = false
    private var joinedMonastery: Boolean = false
    private var disableDeathScreen: Boolean = false
    private var disableNews: Boolean = false
    private var enableCoinMachine: Boolean = false
    private var enableCharmCollector: Boolean = false
    private var macroDisabled: Boolean = false
    private var strongholdRewards: BooleanArray = BooleanArray(4)
    var readPlaques: BooleanArray = BooleanArray(7)
    private var abyssData: BooleanArray = BooleanArray(4)
    private var travelLogs: BooleanArray = BooleanArray(45)
    private var godBooks: BooleanArray = BooleanArray(3)
    private var godPages: BooleanArray = BooleanArray(4)

    fun parse(data: JSONObject) {
        tutorialStage = data["tutorialStage"].toString().toInt()
        homeTeleportDelay = data["homeTeleportDelay"].toString().toLong()
        lumbridgeRope = data["lumbridgeRope"] as Boolean
        apprentice = data["apprentice"] as Boolean
        assistTime = data["assistTime"].toString().toLong()
        val ae = data["assistExperience"] as JSONArray
        for (i in 0 until ae.size) {
            assistExperience[i] = ae[i].toString().toDouble()
        }
        val sr = data["strongHoldRewards"] as JSONArray
        for (i in 0 until sr.size) {
            strongholdRewards[i] = sr[i] as Boolean
        }
        chatPing = data["chatPing"].toString().toLong()
        tutorClaim = data["tutorClaim"].toString().toLong()
        luthasTask = data["luthasTask"] as Boolean
        karamjaBananas = data["karamjaBananas"].toString().toInt()
        silkSteal = data["silkSteal"].toString().toLong()
        teaSteal = data["teaSteal"].toString().toLong()
        zaffAmount = data["zafAmount"].toString().toInt()
        zaffTime = data["zafTime"].toString().toLong()
        fritzGlass = data["fritzGlass"] as Boolean
        wydinEmployee = data["wydinEmployee"] as Boolean
        draynorRecording = data["draynorRecording"] as Boolean
        geTutorial = data["geTutorial"] as Boolean
        essenceTeleporter = data["essenceTeleporter"].toString().toInt()
        recoilDamage = data["recoilDamage"].toString().toInt()
        doubleExpDelay = data["doubleExpDelay"].toString().toLong()
        joinedMonastery = data["joinedMonastery"] as Boolean
        val rp = data["readPlaques"] as JSONArray
        for (i in 0 until rp.size) {
            readPlaques[i] = rp[i] as Boolean
        }
        forgingUses = data["forgingUses"].toString().toInt()
        ectoCharges = data["ectoCharges"].toString().toInt()
        dropDelay = data["dropDelay"].toString().toLong()
        val ad = data["abyssData"] as JSONArray
        for (i in 0 until ad.size) {
            abyssData[i] = ad[i] as Boolean
        }
        val rd = data["rcDecays"] as JSONArray
        for (i in 0 until rd.size) {
            rcDecays[i] = rd[i].toString().toInt()
        }
        disableDeathScreen = data["disableDeathScreen"] as Boolean
        playerTestStage = data["playerTestStage"].toString().toInt()
        charmingDelay = data["charmingDelay"].toString().toLong()
        val tl = data["travelLogs"] as JSONArray
        for (i in 0 until tl.size) {
            travelLogs[i] = tl[i] as Boolean
        }
        val gb = data["godBooks"] as JSONArray
        for (i in 0 until gb.size) {
            godBooks[i] = gb[i] as Boolean
        }
        disableNews = data["disableNews"] as Boolean
        val gp = data["godPages"] as JSONArray
        for (i in 0 until gp.size) {
            godPages[i] = gp[i] as Boolean
        }
        overChargeDelay = data["overChargeDelay"].toString().toLong()
        val bc = data["bossCounters"] as JSONArray
        for (i in 0 until bc.size) {
            bossCounters[i] = bc[i].toString().toInt()
        }
        barrowsLoots = data["barrowsLoots"].toString().toInt()
        lootShareDelay = data["lootShareDelay"].toString().toLong()
        lootSharePoints = data["lootSharePoints"].toString().toInt()
        doubleExp = data["doubleExp"].toString().toLong()
        globalTeleporterDelay = data["globalTeleporterDelay"].toString().toLong()
        starSpriteDelay = data["starSpriteDelay"].toString().toLong()
        runReplenishDelay = data["runReplenishDelay"].toString().toLong()
        runReplenishCharges = data["runReplenishCharges"].toString().toInt()
        lowAlchemyCharges = data["lowAlchemyCharges"].toString().toInt()
        lowAlchemyDelay = data["lowAlchemyDelay"].toString().toLong()
        magicSkillCapeDelay = data["magicSkillCapeDelay"].toString().toLong()
        hunterCapeDelay = data["hunterCapeDelay"].toString().toLong()
        hunterCapeCharges = data["hunterCapeCharges"].toString().toInt()
        taskAmount = data["taskAmount"].toString().toInt()
        taskPoints = data["taskPoints"].toString().toInt()
        macroDisabled = data["macroDisabled"] as Boolean
    }

    fun setSavedLocation(
        x: Int,
        y: Int,
        z: Int,
    ) {
        savedX = x
        savedY = y
        savedH = z
    }

    fun getTravelLogs(): BooleanArray {
        return travelLogs
    }

    fun removeTravelLog(index: Int) {
        travelLogs[index] = false
    }

    fun hasTravelLog(index: Int): Boolean {
        return travelLogs[index]
    }

    fun setTravelLog(index: Int) {
        travelLogs[index] = true
    }

    fun setCharmingDelay(delay: Long) {
        charmingDelay = delay
    }

    fun getCharmingDelay(): Long {
        return charmingDelay
    }

    fun getTestStage(): Int {
        return playerTestStage
    }

    fun setTestStage(stage: Int) {
        playerTestStage = stage
    }

    fun getTutorialStage(): Int {
        return tutorialStage
    }

    fun setTutorialStage(tutorialStage: Int) {
        this.tutorialStage = tutorialStage
    }

    fun getHomeTeleportDelay(): Long {
        return homeTeleportDelay
    }

    fun setHomeTeleportDelay(homeTeleportDelay: Long) {
        this.homeTeleportDelay = homeTeleportDelay
    }

    fun hasTiedLumbridgeRope(): Boolean {
        return lumbridgeRope
    }

    fun setLumbridgeRope(lumbridgeRope: Boolean) {
        this.lumbridgeRope = lumbridgeRope
    }

    fun hasSpokenToApprentice(): Boolean {
        return apprentice
    }

    fun setApprentice(apprentice: Boolean) {
        this.apprentice = apprentice
    }

    fun getAssistTime(): Long {
        return assistTime
    }

    fun setAssistTime(assistTime: Long) {
        this.assistTime = assistTime
    }

    fun getAssistExperience(): DoubleArray {
        return assistExperience
    }

    fun setAssistExperience(assistExperience: DoubleArray) {
        this.assistExperience = assistExperience
    }

    fun getStrongHoldRewards(): BooleanArray {
        return strongholdRewards
    }

    fun hasStrongholdReward(reward: Int): Boolean {
        return strongholdRewards[reward - 1]
    }

    fun getChatPing(): Long {
        return chatPing
    }

    fun setChatPing(chatPing: Long) {
        this.chatPing = chatPing
    }

    fun getTutorClaim(): Long {
        return tutorClaim
    }

    fun setTutorClaim(tutorClaim: Long) {
        this.tutorClaim = tutorClaim
    }

    fun isLuthasTask(): Boolean {
        return luthasTask
    }

    fun setLuthasTask(luthasTask: Boolean) {
        this.luthasTask = luthasTask
    }

    fun getKaramjaBananas(): Int {
        return karamjaBananas
    }

    fun setKaramjaBannanas(karamjaBannanas: Int) {
        this.karamjaBananas = karamjaBannanas
    }

    fun getTeaSteal(): Long {
        return teaSteal
    }

    fun setTeaSteal(teaSteal: Long) {
        this.teaSteal = teaSteal
    }

    fun getSilkSteal(): Long {
        return silkSteal
    }

    fun setSilkSteal(silkSteal: Long) {
        this.silkSteal = silkSteal
    }

    fun getZaffAmount(): Int {
        return zaffAmount
    }

    fun setZaffAmount(zaffAmount: Int) {
        this.zaffAmount = zaffAmount
    }

    fun getZaffTime(): Long {
        return zaffTime
    }

    fun isDraynorRecording(): Boolean {
        return draynorRecording
    }

    fun setDraynorRecording(draynorRecording: Boolean) {
        this.draynorRecording = draynorRecording
    }

    fun isWydinEmployee(): Boolean {
        return wydinEmployee
    }

    fun setWydinEmployee(wydinEmployee: Boolean) {
        this.wydinEmployee = wydinEmployee
    }

    fun setZaffTime(zaffTime: Long) {
        this.zaffTime = zaffTime
    }

    fun isFritzGlass(): Boolean {
        return fritzGlass
    }

    fun setFritzGlass(frizGlass: Boolean) {
        this.fritzGlass = frizGlass
    }

    fun isGeTutorial(): Boolean {
        return geTutorial
    }

    fun setGeTutorial(geTutorial: Boolean) {
        this.geTutorial = geTutorial
    }

    fun getEssenceTeleporter(): Int {
        return essenceTeleporter
    }

    fun setEssenceTeleporter(essenceTeleporter: Int) {
        this.essenceTeleporter = essenceTeleporter
    }

    fun getRecoilDamage(): Int {
        return recoilDamage
    }

    fun setRecoilDamage(recoilDamage: Int) {
        this.recoilDamage = recoilDamage
    }

    fun getDoubleExpDelay(): Long {
        return doubleExpDelay
    }

    fun setDoubleExpDelay(doubleExpDelay: Long) {
        this.doubleExpDelay = doubleExpDelay
    }

    fun isJoinedMonastery(): Boolean {
        return joinedMonastery
    }

    fun setJoinedMonastery(joinedMonastery: Boolean) {
        this.joinedMonastery = joinedMonastery
    }

    fun hasReadPlaques(): Boolean {
        for (i in readPlaques.indices) {
            if (!readPlaques[i]) {
                return false
            }
        }
        return true
    }

    fun setGodBook(book: GodBook) {
        godBooks[book.ordinal] = true
    }

    fun hasCompletedGodBook(book: GodBook): Boolean {
        return godBooks[book.ordinal]
    }

    fun getForgingUses(): Int {
        return forgingUses
    }

    fun setForgingUses(forgingUses: Int) {
        this.forgingUses = forgingUses
    }

    fun getEctoCharges(): Int {
        return ectoCharges
    }

    fun setEctoCharges(ectoCharges: Int) {
        this.ectoCharges = ectoCharges
    }

    fun resetAbyss() {
        for (i in abyssData.indices) {
            abyssData[i] = false
        }
    }

    fun setAbyssCharge(ordinal: Int) {
        abyssData[ordinal] = true
    }

    fun hasAbyssCharge(ordinal: Int): Boolean {
        return abyssData[ordinal]
    }

    fun getDropDelay(): Long {
        return dropDelay
    }

    fun setDropDelay(dropDelay: Long) {
        this.dropDelay = dropDelay
    }

    fun getRcDecay(ordinal: Int): Int {
        if (ordinal < 0) {
            return 0
        }
        return rcDecays[ordinal]
    }

    fun getRcDecays(): IntArray {
        return rcDecays
    }

    fun isDeathScreenDisabled(): Boolean {
        return disableDeathScreen
    }

    fun setDisableDeathScreen(b: Boolean) {
        this.disableDeathScreen = b
    }

    fun getGodBooks(): BooleanArray {
        return godBooks
    }

    fun setGodBooks(godBooks: BooleanArray) {
        this.godBooks = godBooks
    }

    fun getGodBook(): Int {
        return godBook
    }

    fun setGodBook(godBook: Int) {
        this.godBook = godBook
    }

    fun isDisableNews(): Boolean {
        return disableNews
    }

    fun setDisableNews(disableNews: Boolean) {
        this.disableNews = disableNews
    }

    fun getGodPages(): BooleanArray {
        return godPages
    }

    fun setGodPages(godPages: BooleanArray) {
        this.godPages = godPages
    }

    fun getOverChargeDelay(): Long {
        return overChargeDelay
    }

    fun setOverChargeDelay(overChargeDelay: Long) {
        this.overChargeDelay = overChargeDelay
    }

    fun getBossCounters(): IntArray {
        return bossCounters
    }

    fun setBossCounters(bossCounters: IntArray) {
        this.bossCounters = bossCounters
    }

    fun getBarrowsLoots(): Int {
        return barrowsLoots
    }

    fun setBarrowsLoots(barrowsLoots: Int) {
        this.barrowsLoots = barrowsLoots
    }

    fun getLootSharePoints(): Int {
        return lootSharePoints
    }

    fun setLootSharePoints(lootSharePoints: Int) {
        this.lootSharePoints = lootSharePoints
    }

    fun getLootShareDelay(): Long {
        return lootShareDelay
    }

    fun setLootShareDelay(lootShareDelay: Long) {
        this.lootShareDelay = lootShareDelay
    }

    fun getDoubleExp(): Long {
        return doubleExp
    }

    fun setDoubleExp(doubleExp: Long) {
        this.doubleExp = doubleExp
    }

    fun hasDoubleExp(): Boolean {
        return doubleExp > System.currentTimeMillis()
    }

    fun getGlobalTeleporterDelay(): Long {
        return globalTeleporterDelay
    }

    fun setGlobalTeleporterDelay(globalTeleporterDelay: Long) {
        this.globalTeleporterDelay = globalTeleporterDelay
    }

    fun getRunReplenishDelay(): Long {
        return runReplenishDelay
    }

    fun setRunReplenishDelay(runReplenishDelay: Long) {
        this.runReplenishDelay = runReplenishDelay
    }

    fun getRunReplenishCharges(): Int {
        return runReplenishCharges
    }

    fun setRunReplenishCharges(runReplenishCharges: Int) {
        this.runReplenishCharges = runReplenishCharges
    }

    fun getLowAlchemyCharges(): Int {
        return lowAlchemyCharges
    }

    fun setLowAlchemyCharges(lowAlchemyCharges: Int) {
        this.lowAlchemyCharges = lowAlchemyCharges
    }

    fun getLowAlchemyDelay(): Long {
        return lowAlchemyDelay
    }

    fun setLowAlchemyDelay(lowAlchemyDelay: Long) {
        this.lowAlchemyDelay = lowAlchemyDelay
    }

    fun isEnableCoinMachine(): Boolean {
        return enableCoinMachine
    }

    fun setEnableCoinMachine(enableCoinMachine: Boolean) {
        this.enableCoinMachine = enableCoinMachine
    }

    fun getMagicSkillCapeDelay(): Long {
        return magicSkillCapeDelay
    }

    fun setMagicSkillCapeDelay(magicSkillCapeDelay: Long) {
        this.magicSkillCapeDelay = magicSkillCapeDelay
    }

    fun getHunterCapeDelay(): Long {
        return hunterCapeDelay
    }

    fun setHunterCapeDelay(hunterCapeDelay: Long) {
        this.hunterCapeDelay = hunterCapeDelay
    }

    fun getHunterCapeCharges(): Int {
        return hunterCapeCharges
    }

    fun setHunterCapeCharges(hunterCapeCharges: Int) {
        this.hunterCapeCharges = hunterCapeCharges
    }

    fun isEnableCharmCollector(): Boolean {
        return enableCharmCollector
    }

    fun setEnableCharmCollector(enableCharmCollector: Boolean) {
        this.enableCharmCollector = enableCharmCollector
    }

    fun getMinigameTeleportDelay(): Long {
        return minigameTeleportDelay
    }

    fun setMinigameTeleportDelay(delay: Long) {
        this.minigameTeleportDelay = delay
    }

    fun getSavedH(): Int {
        return savedH
    }

    fun setSavedH(savedH: Int) {
        this.savedH = savedH
    }

    fun getSavedY(): Int {
        return savedY
    }

    fun setSavedY(savedY: Int) {
        this.savedY = savedY
    }

    fun getSavedX(): Int {
        return savedX
    }

    fun setSavedX(savedX: Int) {
        this.savedX = savedX
    }

    fun getTaskAmount(): Int {
        return taskAmount
    }

    fun setTaskAmount(taskAmount: Int) {
        this.taskAmount = taskAmount
    }

    fun getTaskPoints(): Int {
        return taskPoints
    }

    fun setTaskPoints(taskPoints: Int) {
        this.taskPoints = taskPoints
    }

    fun setMacroDisabled(disabled: Boolean) {
        this.macroDisabled = disabled
    }

    fun getMacroDisabled(): Boolean {
        return this.macroDisabled
    }

    fun getAbyssData(): BooleanArray {
        return abyssData
    }

    fun getPlayerTestStage(): Int {
        return playerTestStage
    }
}
