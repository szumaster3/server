package content.region.misthalin.varrock.quest.phoenixgang

import content.region.misthalin.dialogue.KingRoaldDialogue
import content.region.misthalin.varrock.quest.phoenixgang.plugin.JohnnyBeardNPC
import content.region.misthalin.varrock.quest.phoenixgang.plugin.ShieldOfArravPlugin
import content.region.misthalin.varrock.quest.phoenixgang.dialogue.*
import core.api.removeAttribute
import core.api.setAttribute
import core.api.setVarbit
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.item.Item
import core.plugin.ClassScanner
import core.plugin.Initializable
import org.rs.consts.Items
import org.rs.consts.Quests
import org.rs.consts.Vars

@Initializable
class ShieldofArrav :
    Quest(
        Quests.SHIELD_OF_ARRAV,
        29,
        28,
        1,
        Vars.VARP_QUEST_SHIELD_OF_ARRAV_PROGRESS_145,
        0,
        1,
        7,
    ) {
    companion object {
        /**
         * Represents the shield of arrav book item.
         */
        val BOOK = Item(Items.BOOK_757)

        /**
         * Represents the intel report item.
         */
        val INTEL_REPORT = Item(Items.INTEL_REPORT_761)

        /**
         * Represents the weapon store item key.
         */
        val KEY = Item(Items.WEAPON_STORE_KEY_759)

        /**
         * Represents the phoenix shield item.
         */
        val PHOENIX_SHIELD = Item(Items.BROKEN_SHIELD_763)

        /**
         * Represents the black arm shield item.
         */
        val BLACKARM_SHIELD = Item(Items.BROKEN_SHIELD_765)

        /**
         * Represents the blackarm certificate item.
         */
        val BLACKARM_CERTIFICATE = Item(Items.HALF_CERTIFICATE_11174, 2)

        /**
         * Represents the phoenix certificate item.
         */
        val PHOENIX_CERTIFICATE = Item(Items.HALF_CERTIFICATE_11173, 2)

        /**
         * Sets the phoenix gang.
         * @param player the player.
         */
        fun setPhoenix(player: Player) {
            setAttribute(player, "/save:phoenix-gang", true)
        }

        /**
         * Sets the black arm gang.
         * @param player the player.
         */
        fun setBlackArm(player: Player) {
            player.setAttribute("/save:black-arm-gang", true)
        }

        /**
         * Swaps the gang.
         * @param player the player.
         */
        fun swapGang(player: Player) {
            when {
                isPhoenix(player) -> {
                    player.setAttribute("/save:black-arm-gang", true)
                    player.setAttribute("/save:phoenix-gang", false)
                }

                isBlackArm(player) -> {
                    player.setAttribute("/save:black-arm-gang", false)
                    player.setAttribute("/save:phoenix-gang", true)
                }

                else -> player.setAttribute("/save:phoenix-gang", true)
            }
        }

        /**
         * Method used to check if the player is part of the phoenix gang.
         * @param player the player.
         * @return `true` if so.
         */
        fun isPhoenix(player: Player): Boolean = player.getAttribute("phoenix-gang", false)

        /**
         * Method used to check if the player is part of the black arm gang.
         * @param player the player.
         * @return `true` if so.
         */
        fun isBlackArm(player: Player): Boolean = player.getAttribute("black-arm-gang", false)

        /**
         * Method used to set that the player is trying to do the phoenix mission.
         * @param player the player.
         */
        fun setPhoenixMission(player: Player) {
            player.setAttribute("/save:phoenix-mission", true)
        }

        /**
         * Method used to set that the player is trying to do the black arm gang
         * mission.
         * @param player the player.
         */
        fun setBlackArmMission(player: Player) {
            setAttribute(player, "/save:blackarm-mission", true)
        }

        /**
         * Method used to check if they're doing the black arm gang mission.
         * @param player the player.
         * @return `true` if so.
         */
        fun isBlackArmMission(player: Player): Boolean = player.getAttribute("blackarm-mission", false)

        /**
         * Method used to check if they're doing the phoenix gang mission.
         * @param player the player.
         * @return `true` if so.
         */
        fun isPhoenixMission(player: Player): Boolean = player.getAttribute("phoenix-mission", false)

        /**
         * Gets the shield item.
         * @param player the player.
         * @return
         */
        fun getShield(player: Player): Item = if (isBlackArm(player)) BLACKARM_SHIELD else PHOENIX_SHIELD
    }

    override fun newInstance(arg: Any?): Quest {
        ClassScanner.definePlugins(
            JohnnyBeardNPC(),
            JonnytheBeardDialogue(),
            KatrineDialogue(),
            KingRoaldDialogue(),
            ReldoDialogue(),
            ShieldOfArravPlugin(),
            StravenDialogue(),
            WeaponsMasterDialogue(),
        )
        return this
    }

    override fun drawJournal(
        player: Player,
        stage: Int,
    ) {
        super.drawJournal(player, stage)
        when (stage) {
            0 -> {
                line(player, "$BLUE I can start this quest by speaking to $RED Reldo $BLUE in $RED Varrock's", 11)
                line(player, "$RED Palace Library$BLUE, or by speaking to $RED Charlie the Tramp$BLUE near", 12)
                line(player, "$BLUE the $RED Blue Moon Inn$BLUE in $RED Varrock.", 13)
                line(player, "$BLUE I will need a friend to help me and some combat experience", 14)
                line(player, "$BLUE may be an advantage.", 15)
            }

            10 -> {
                line(player, "$RED Reldo$BLUE says there is a $RED quest$BLUE hidden in one of the books in", 11)
                line(player, "$BLUE his $RED library$BLUE somewhere. I should look for it and see.", 12)
            }

            20 -> {
                line(player, "<str>I read about a valuable shield stolen long ago by a gang of", 11)
                line(player, "<str>thieves with an outstanding reward upon it.", 12)
                line(player, "$BLUE I should ask $RED Reldo$BLUE if he knows anything more about this.", 13)
            }

            30 -> {
                line(player, "<str>I read about a valuable shield stolen long ago by a gang of", 11)
                line(player, "<str>thieves with an outstanding reward upon it.", 12)
                line(player, "$BLUE Reldo told me that the fur trader in $RED Varrock$BLUE, named", 13)
                line(player, "$RED Baraek$BLUE, knows about the $RED Phoenix Gang$BLUE. I should speak to", 14)
                line(player, "$BLUE him next.", 15)
            }
            40 -> {
                line(player, "<str>I read about a valuable shield stolen long ago by a gang of", 4 + 7)
                line(player, "<str>thieves with an outstanding reward upon it.", 5 + 7)
                line(player, "${BLUE}Baraek told me that the $RED'Phoenix Gang' ${BLUE}have a hideout in", 6 + 7)
                line(player, "${BLUE}the ${RED}south-eastern part of Varrock$BLUE, disguising themselves", 7 + 7)
                line(player, "${BLUE}as the ${RED}VTAM Corporation$BLUE. I should find them and join.", 8 + 7)
            }
            50 -> {
                line(player, "<str>I read about a valuable shield stolen long ago by a gang of", 11)
                line(player, "<str>thieves with an outstanding reward upon it.", 12)
                line(player, "$BLUE Baraek told me that the $RED 'Phoenix Gang'$BLUE have a hideout in", 13)
                line(player, "$BLUE the $RED south-eastern part of Varrock$BLUE, disguising themselves", 14)
                line(player, "$BLUE as the $RED VTAM Corporation$BLUE. I should find them and join.", 15)
                line(player, "<str>I also spoke to Charlie the tramp in Varrock.", 16)
                line(player, "$BLUE According to him there is a criminal organisation known as", 17)
                line(player, "$BLUE the $RED 'Black Arm Gang'$BLUE down an alley near to him. I should", 18)
                line(player, "$BLUE speak to their $RED leader, Katrine$BLUE, about joining.", 19)
            }
            60 -> {
                line(player, "<str>I read about a valuable shield stolen long ago by a gang of", 4 + 7)
                line(player, "<str>thieves with an outstanding reward upon it. Baraek told me", 5 + 7)
                line(player, "<str>the location of the Phoenix Gang hideout.", 6 + 7)
                line(player, "<str>I also spoke to Charlie the tramp in Varrock.", 7 + 7)
                line(player, "<str>According to him there is a criminal organisation known as", 8 + 7)
                line(player, "<str>the $RED'Black Arm Gang'${BLUE}down the alley near to him.", 9 + 7)
                if (isPhoenixMission(player) && isBlackArmMission(player)) {
                    line(player, "<str>I read about a valuable shield stolen long ago by a gang of", 4 + 7)
                    line(player, "<str>thieves with an outstanding reward upon it. Baraek told me", 5 + 7)
                    line(player, "<str>the location of the Phoenix Gang hideout.", 6 + 7)
                    line(player, "<str>To start this quest, I spoke to Charlie the tramp in Varrock.", 7 + 7)
                    line(player, "<str>He gave me the location of the Black Arm gang HQ.", 8 + 7)
                    line(player, "<str>According to him there is a criminal organisation known as", 9 + 7)
                    line(player, "<str>the $RED'Black Arm Gang'${BLUE}down the alley near to him.", 10 + 7)
                    line(
                        player,
                        "${BLUE}If I want to join the ${RED}Phoenix Gang ${BLUE}I need to kill$RED Jonny The",
                        11 + 7,
                    )
                    line(
                        player,
                        "${RED}Beard ${BLUE}in the ${RED}Blue Moon Inn ${BLUE}and retrieve his ${RED}report.",
                        12 + 7,
                    )
                    line(
                        player,
                        "${RED}Katrine ${BLUE}said if I wanted to join the ${RED}Black Arm Gang,$BLUE I'd",
                        13 + 7,
                    )
                    line(player, "${BLUE}have to steal ${RED}two Phoenix crossbows ${BLUE}from the rival gang.", 14 + 7)
                    line(player, "${BLUE}Maybe ${RED}Charlie the tramp ${BLUE}can give me some ideas about", 15 + 7)
                    line(player, "${BLUE}how to do this.", 16 + 7)
                } else if (isPhoenixMission(player)) {
                    line(
                        player,
                        "${BLUE}If I want to join the ${RED}Phoenix Gang ${BLUE}I need to kill$RED Jonny The",
                        10 + 7,
                    )
                    line(
                        player,
                        "${RED}Beard ${BLUE}in the ${RED}Blue Moon Inn ${BLUE}and retrieve his ${RED}report.",
                        11 + 7,
                    )
                    line(
                        player,
                        "${BLUE}Alternatively, if I want to join the ${RED}Blackarm gang ${BLUE}I should",
                        12 + 7,
                    )
                    line(player, "${BLUE}speak to their ${RED}leader, Katrine, ${BLUE}about joining.", 13 + 7)
                } else if (isBlackArmMission(player)) {
                    line(
                        player,
                        "${RED}Katrine ${BLUE}said if I wanted to join the ${RED}Black Arm Gang$BLUE, I'd",
                        10 + 7,
                    )
                    line(player, "${BLUE}have to steal ${RED}two Phoenix crossbows ${BLUE}from the rival gang.", 11 + 7)
                    line(player, "${BLUE}Maybe ${RED}Charlie the tramp ${BLUE}can give me some ideas about", 12 + 7)
                    line(player, "${BLUE}how to do this.", 13 + 7)
                }
            }
            70 -> {
                if (isPhoenix(player)) {
                    line(player, "<str>I read about a valuable shield stolen long ago by a gang of", 11)
                    line(player, "<str>thieves with an outstanding reward upon it. Baraek told me", 12)
                    line(player, "<str>the location of the Phoenix Gang hideout.", 13)
                    line(player, "<str>I killed Jonny the Beard and was welcomed into the Phoenix", 14)
                    line(player, "<str>Gang. Straven gave me a key to the weapons room.", 15)

                    if (!player.inventory.containsItem(PHOENIX_SHIELD) && !player.bank.containsItem(PHOENIX_SHIELD)) {
                        line(player, "$BLUE I need to search the $RED Phoenix Gang's hideout$BLUE to find half", 17)
                        line(player, "$BLUE of the $RED Shield of Arrav.", 18)
                    } else {
                        line(
                            player,
                            "$BLUE I found half of the $RED Shield of Arrav$BLUE in the $RED Phoenix Gang's",
                            17,
                        )
                        line(player, "$RED hideout.", 18)
                    }

                    line(player, "$BLUE The second half of the $RED shield$BLUE belongs to a rival gang", 19)
                    line(
                        player,
                        "$BLUE known as the $RED Black Arm Gang$BLUE. I will need $RED a friend's help$BLUE to",
                        20,
                    )
                    line(player, "$BLUE retrieve it before claiming the $RED reward$BLUE for it.", 21)
                } else {
                    line(player, "<str>I read about a valuable shield stolen long ago by a gang of", 11)
                    line(player, "<str>thieves with an outstanding reward upon it. Baraek told me", 12)
                    line(player, "<str>the location of the Phoenix Gang hideout.", 13)
                    line(player, "<str>With the help of a friend, I managed to obtain two Phoenix", 14)
                    line(player, "<str>Crossbows from the Phoenix Gang's weapons store, and", 15)
                    line(player, "<str>Katrine welcomes me as a Black Arm Gang member.", 16)
                    line(player, "$BLUE With $RED my friend's help$BLUE, I can get both pieces of the shield", 18)
                    line(player, "$BLUE and return it to $RED King Roald$BLUE for my $RED reward.", 19)
                }
            }

            100 -> {
                line(player, "<str>I read about a valuable shield stolen long ago by a gang of", 11)
                line(player, "<str>thieves with an outstanding reward upon it. Baraek told me", 12)
                line(player, "<str>the location of the Phoenix Gang hideout.", 13)
                if (!isPhoenix(player)) {
                    line(player, "<str>With the help of a friend, I managed to obtain two Phoenix", 14)
                    line(player, "<str>Crossbows from the Phoenix Gang's weapons store, and", 15)
                    line(player, "<str>Katrine welcomes me as a Black Arm Gang member.", 16)
                    line(player, "<str>With the help of my friend in the rival gang, I was able to", 18)
                    line(player, "<str>retrieve both parts of the fabled Shield of Arrav and", 19)
                    line(player, "<str>return it to the Museum of Varrock.", 20)
                    line(player, "<col=FF0000>QUEST COMPLETE!", 22)
                    line(player, "$RED I received a reward of 600 coins and got 1 quest point.", 23)
                } else {
                    line(player, "<str>I killed Jonny the Beard and was welcomed into the Phoenix", 14)
                    line(player, "<str>Gang. Straven gave me a key to the weapons room.", 15)
                    line(player, "<str>With the help of my friend in the rival gang, I was able to", 16)
                    line(player, "<str>retrieve both parts of the fabled Shield of Arrav and", 17)
                    line(player, "<str>return it to the Museum of Varrock.", 18)
                    line(player, "<col=FF0000>QUEST COMPLETE!", 20)
                    line(player, "$RED I received a reward of 600 coins and got 1 quest point.", 21)
                }
            }
        }
    }

    override fun finish(player: Player) {
        super.finish(player)
        removeAttribute(player, "blackarm-mission")
        removeAttribute(player, "phoenix-mission")
        player.packetDispatch.sendString("1 Quest Point", 277, 10)
        player.packetDispatch.sendString("600 Coins", 277, 11)
        player.packetDispatch.sendItemZoomOnInterface(Items.PHOENIX_CROSSBOW_767, 1, 230, 277, 5)
        setVarbit(player, Vars.VARBIT_SCENERY_MUSEUM_DISPLAY_24_5394, 1, true)
    }
}
