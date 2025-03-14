package content.global.handlers.item.book

import content.global.handlers.iface.BookInterface
import content.global.handlers.iface.BookLine
import content.global.handlers.iface.Page
import content.global.handlers.iface.PageSet
import core.api.setAttribute
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import org.rs.consts.Items

class GnomeTranslationBook : InteractionListener {
    companion object {
        private const val TITLE = "Gnome-English Translation"
        private val CONTENTS =
            arrayOf(
                PageSet(
                    Page(
                        BookLine("Gnome-English", 55),
                        BookLine("Translation", 56),
                        BookLine("", 57),
                        BookLine("written by Anita", 58),
                        BookLine("", 59),
                        BookLine("This text contains the", 60),
                        BookLine("the ancient gnome words I", 61),
                        BookLine("have managed to translate", 62),
                        BookLine("thus far.", 63),
                    ),
                    Page(
                        BookLine("-A-", 66),
                        BookLine("arpos: rocks", 67),
                        BookLine("ando: gate", 68),
                        BookLine("andra: city", 69),
                        BookLine("ataris: cow", 70),
                        BookLine("", 71),
                        BookLine("-C-", 72),
                        BookLine("cef: threat", 73),
                        BookLine("cheray: lazy", 74),
                        BookLine("Cinqo: King", 75),
                        BookLine("cretor: bucket", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("-E-", 55),
                        BookLine("eis: me", 56),
                        BookLine("es: a", 57),
                        BookLine("et: and", 58),
                        BookLine("eto: will", 59),
                        BookLine("", 60),
                        BookLine("-G-", 61),
                        BookLine("gandius: jungle", 62),
                        BookLine("Gal: all", 63),
                        BookLine("gentis: leaf", 64),
                        BookLine("gutus: banana", 65),
                    ),
                    Page(
                        BookLine("gomondo: branch", 66),
                        BookLine("", 67),
                        BookLine("-H-", 68),
                        BookLine("har: old", 69),
                        BookLine("harij: harpoon", 70),
                        BookLine("hewo: grass", 71),
                        BookLine("", 72),
                        BookLine("-I-", 73),
                        BookLine("ip: you", 74),
                        BookLine("imindus: quest", 75),
                        BookLine("irno: translate", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("", 55),
                        BookLine("-K-", 56),
                        BookLine("kar: no", 57),
                        BookLine("kai: boat", 58),
                        BookLine("ko: sail", 59),
                        BookLine("", 60),
                        BookLine("-L-", 61),
                        BookLine("lauf: eye", 62),
                        BookLine("laquinay: common sense", 63),
                        BookLine("lemanto: man", 64),
                        BookLine("lemantolly: stupid man", 65),
                    ),
                    Page(
                        BookLine("lovos: gave", 66),
                        BookLine("", 67),
                        BookLine("-M-", 68),
                        BookLine("meso: came", 69),
                        BookLine("meriz: kill", 70),
                        BookLine("mina: time(s)", 71),
                        BookLine("mos: coin", 72),
                        BookLine("mi: I", 73),
                        BookLine("mond: seal", 74),
                        BookLine("", 75),
                        BookLine("-O-", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("o: for", 55),
                        BookLine("", 56),
                        BookLine("-P-", 57),
                        BookLine("por: long", 58),
                        BookLine("prit: with", 59),
                        BookLine("priw: tree", 60),
                        BookLine("pro: to", 61),
                        BookLine("", 62),
                        BookLine("-Q-", 63),
                        BookLine("Qui: guard", 64),
                        BookLine("Quir: guardian", 65),
                    ),
                    Page(
                        BookLine("", 66),
                        BookLine("-R-", 67),
                        BookLine("rentos: agility", 68),
                        BookLine("", 69),
                        BookLine("-S-", 70),
                        BookLine("sarko: begone", 71),
                        BookLine("sind: big", 72),
                        BookLine("", 67),
                        BookLine("-T-", 68),
                        BookLine("ta: the", 69),
                        BookLine("tuzo: open", 70),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("-U-", 55),
                        BookLine("Undri: lands", 56),
                        BookLine("Umesco: Soul", 57),
                    ),
                ),
            )

        @Suppress("UNUSED_PARAMETER")
        private fun display(
            player: Player,
            pageNum: Int,
            buttonID: Int,
        ): Boolean {
            BookInterface.pageSetup(
                player,
                BookInterface.FANCY_BOOK_3_49,
                TITLE,
                CONTENTS,
            )
            return true
        }
    }

    override fun defineListeners() {
        on(Items.TRANSLATION_BOOK_784, IntType.ITEM, "read") { player, _ ->
            setAttribute(player, "bookInterfaceCallback", Companion::display)
            setAttribute(player, "bookInterfaceCurrentPage", 0)
            display(player, 0, 0)
            return@on true
        }
    }
}
