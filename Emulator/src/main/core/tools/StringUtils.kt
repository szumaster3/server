package core.tools

import core.net.packet.IoBuffer
import java.text.DecimalFormat
import java.util.*

object StringUtils {
    @JvmStatic
    private val VALID_CHARS =
        charArrayOf(
            '_',
            'a',
            'b',
            'c',
            'd',
            'e',
            'f',
            'g',
            'h',
            'i',
            'j',
            'k',
            'l',
            'm',
            'n',
            'o',
            'p',
            'q',
            'r',
            's',
            't',
            'u',
            'v',
            'w',
            'x',
            'y',
            'z',
            '0',
            '1',
            '2',
            '3',
            '4',
            '5',
            '6',
            '7',
            '8',
            '9',
        )

    @JvmStatic
    private val anIntArray241 =
        intArrayOf(
            215,
            203,
            83,
            158,
            104,
            101,
            93,
            84,
            107,
            103,
            109,
            95,
            94,
            98,
            89,
            86,
            70,
            41,
            32,
            27,
            24,
            23,
            -1,
            -2,
            26,
            -3,
            -4,
            31,
            30,
            -5,
            -6,
            -7,
            37,
            38,
            36,
            -8,
            -9,
            -10,
            40,
            -11,
            -12,
            55,
            48,
            46,
            47,
            -13,
            -14,
            -15,
            52,
            51,
            -16,
            -17,
            54,
            -18,
            -19,
            63,
            60,
            59,
            -20,
            -21,
            62,
            -22,
            -23,
            67,
            66,
            -24,
            -25,
            69,
            -26,
            -27,
            199,
            132,
            80,
            77,
            76,
            -28,
            -29,
            79,
            -30,
            -31,
            87,
            85,
            -32,
            -33,
            -34,
            -35,
            -36,
            197,
            -37,
            91,
            -38,
            134,
            -39,
            -40,
            -41,
            97,
            -42,
            -43,
            133,
            106,
            -44,
            117,
            -45,
            -46,
            139,
            -47,
            -48,
            110,
            -49,
            -50,
            114,
            113,
            -51,
            -52,
            116,
            -53,
            -54,
            135,
            138,
            136,
            129,
            125,
            124,
            -55,
            -56,
            130,
            128,
            -57,
            -58,
            -59,
            183,
            -60,
            -61,
            -62,
            -63,
            -64,
            148,
            -65,
            -66,
            153,
            149,
            145,
            144,
            -67,
            -68,
            147,
            -69,
            -70,
            -71,
            152,
            154,
            -72,
            -73,
            -74,
            157,
            171,
            -75,
            -76,
            207,
            184,
            174,
            167,
            166,
            165,
            -77,
            -78,
            -79,
            172,
            170,
            -80,
            -81,
            -82,
            178,
            -83,
            177,
            182,
            -84,
            -85,
            187,
            181,
            -86,
            -87,
            -88,
            -89,
            206,
            221,
            -90,
            189,
            -91,
            198,
            254,
            262,
            195,
            196,
            -92,
            -93,
            -94,
            -95,
            -96,
            252,
            255,
            250,
            -97,
            211,
            209,
            -98,
            -99,
            212,
            -100,
            213,
            -101,
            -102,
            -103,
            224,
            -104,
            232,
            227,
            220,
            226,
            -105,
            -106,
            246,
            236,
            -107,
            243,
            -108,
            -109,
            231,
            237,
            235,
            -110,
            -111,
            239,
            238,
            -112,
            -113,
            -114,
            -115,
            -116,
            241,
            -117,
            244,
            -118,
            -119,
            248,
            -120,
            249,
            -121,
            -122,
            -123,
            253,
            -124,
            -125,
            -126,
            -127,
            259,
            258,
            -128,
            -129,
            261,
            -130,
            -131,
            390,
            327,
            296,
            281,
            274,
            271,
            270,
            -132,
            -133,
            273,
            -134,
            -135,
            278,
            277,
            -136,
            -137,
            280,
            -138,
            -139,
            289,
            286,
            285,
            -140,
            -141,
            288,
            -142,
            -143,
            293,
            292,
            -144,
            -145,
            295,
            -146,
            -147,
            312,
            305,
            302,
            301,
            -148,
            -149,
            304,
            -150,
            -151,
            309,
            308,
            -152,
            -153,
            311,
            -154,
            -155,
            320,
            317,
            316,
            -156,
            -157,
            319,
            -158,
            -159,
            324,
            323,
            -160,
            -161,
            326,
            -162,
            -163,
            359,
            344,
            337,
            334,
            333,
            -164,
            -165,
            336,
            -166,
            -167,
            341,
            340,
            -168,
            -169,
            343,
            -170,
            -171,
            352,
            349,
            348,
            -172,
            -173,
            351,
            -174,
            -175,
            356,
            355,
            -176,
            -177,
            358,
            -178,
            -179,
            375,
            368,
            365,
            364,
            -180,
            -181,
            367,
            -182,
            -183,
            372,
            371,
            -184,
            -185,
            374,
            -186,
            -187,
            383,
            380,
            379,
            -188,
            -189,
            382,
            -190,
            -191,
            387,
            386,
            -192,
            -193,
            389,
            -194,
            -195,
            454,
            423,
            408,
            401,
            398,
            397,
            -196,
            -197,
            400,
            -198,
            -199,
            405,
            404,
            -200,
            -201,
            407,
            -202,
            -203,
            416,
            413,
            412,
            -204,
            -205,
            415,
            -206,
            -207,
            420,
            419,
            -208,
            -209,
            422,
            -210,
            -211,
            439,
            432,
            429,
            428,
            -212,
            -213,
            431,
            -214,
            -215,
            436,
            435,
            -216,
            -217,
            438,
            -218,
            -219,
            447,
            444,
            443,
            -220,
            -221,
            446,
            -222,
            -223,
            451,
            450,
            -224,
            -225,
            453,
            -226,
            -227,
            486,
            471,
            464,
            461,
            460,
            -228,
            -229,
            463,
            -230,
            -231,
            468,
            467,
            -232,
            -233,
            470,
            -234,
            -235,
            479,
            476,
            475,
            -236,
            -237,
            478,
            -238,
            -239,
            483,
            482,
            -240,
            -241,
            485,
            -242,
            -243,
            499,
            495,
            492,
            491,
            -244,
            -245,
            494,
            -246,
            -247,
            497,
            -248,
            502,
            -249,
            506,
            503,
            -250,
            -251,
            505,
            -252,
            -253,
            508,
            -254,
            510,
            -255,
            -256,
            0,
        )

    @JvmStatic
    fun getFormattedNumber(amount: Int): String {
        return DecimalFormat("#,###,##0").format(amount.toLong()).toString()
    }

    @JvmStatic
    fun containsInvalidCharacter(name: String): Boolean {
        for (c in name.toCharArray()) {
            var pass = false
            for (vc in VALID_CHARS) {
                if (vc == c) {
                    pass = true
                    break
                }
            }
            if (!pass) return true
        }
        return false
    }

    @JvmStatic
    fun plusS(word: String): String {
        if (word.endsWith("s")) {
            return word
        }
        if (word.endsWith("y")) {
            return word.substring(0, word.length - 1) + "ies"
        }
        return word + "s"
    }

    @JvmStatic
    fun isPlusN(word: String?): Boolean {
        if (word == null) return false
        val s = word.lowercase(Locale.getDefault())
        return s[0] == 'a' ||
            s[0] == 'e' ||
            s[0] == 'i' ||
            s[0] == 'o' ||
            s[0] == 'u' ||
            (s[0] == 'h' && s.length > 1 && s[1] != 'e')
    }

    @JvmStatic
    fun getPlayerNameLong(s: String): Long {
        var l = 0L
        var i = 0
        while (i < s.length && i < 12) {
            val c = s[i]
            l *= 37L
            if (c >= 'A' && c <= 'Z') {
                l += ((1 + c.code) - 65).toLong()
            } else if (c >= 'a' && c <= 'z') {
                l += ((1 + c.code) - 97).toLong()
            } else if (c >= '0' && c <= '9') {
                l += ((27 + c.code) - 48).toLong()
            }
            i++
        }
        while (l % 37L == 0L && l != 0L) l /= 37L
        return l
    }

    @JvmStatic
    fun convertStringToLong(s: String): Long {
        require(s.length <= 20) { "String is too long: $s" }
        var out = 0L
        for (i in 0 until s.length) {
            var m = reducedMapping(s.codePointAt(i))
            require(m != -1L) { "Unmapped Character in String: $s" }
            m = m shl ((9 - i) * 6) + 4
            out = out or m
        }
        return out
    }

    @JvmStatic
    fun formatDisplayName(name: String): String {
        var name = name
        name = name.replace("_".toRegex(), " ")
        name = name.lowercase(Locale.getDefault())
        val newName = StringBuilder()
        var wasSpace = true
        for (i in 0 until name.length) {
            if (wasSpace) {
                newName.append(("" + name[i]).uppercase(Locale.getDefault()))
                wasSpace = false
            } else {
                newName.append(name[i])
            }
            if (name[i] == ' ') {
                wasSpace = true
            }
        }
        return newName.toString()
    }

    @JvmStatic
    private fun getByte(c: Char): Byte {
        val charByte =
            if (c.code > 0 && c < '\u0080' || c >= '\u00a0' && c <= '\u00ff') {
                c.code.toByte()
            } else if (c != '\u20AC') {
                if (c != '\u201A') {
                    if (c != '\u0192') {
                        if (c == '\u201E') {
                            -124
                        } else if (c != '\u2026') {
                            if (c != '\u2020') {
                                if (c == '\u2021') {
                                    -121
                                } else if (c == '\u02C6') {
                                    -120
                                } else if (c == '\u2030') {
                                    -119
                                } else if (c == '\u0160') {
                                    -118
                                } else if (c == '\u2039') {
                                    -117
                                } else if (c == '\u0152') {
                                    -116
                                } else if (c != '\u017D') {
                                    if (c == '\u2018') {
                                        -111
                                    } else if (c != '\u2019') {
                                        if (c != '\u201C') {
                                            if (c == '\u201D') {
                                                -108
                                            } else if (c != '\u2022') {
                                                if (c == '\u2013') {
                                                    -106
                                                } else if (c == '\u2014') {
                                                    -105
                                                } else if (c == '\u02DC') {
                                                    -104
                                                } else if (c == '\u2122') {
                                                    -103
                                                } else if (c != '\u0161') {
                                                    if (c == '\u203A') {
                                                        -101
                                                    } else if (c != '\u0153') {
                                                        if (c == '\u017E') {
                                                            -98
                                                        } else if (c != '\u0178') {
                                                            63
                                                        } else {
                                                            -97
                                                        }
                                                    } else {
                                                        -100
                                                    }
                                                } else {
                                                    -102
                                                }
                                            } else {
                                                -107
                                            }
                                        } else {
                                            -109
                                        }
                                    } else {
                                        -110
                                    }
                                } else {
                                    -114
                                }
                            } else {
                                -122
                            }
                        } else {
                            -123
                        }
                    } else {
                        -125
                    }
                } else {
                    -126
                }
            } else {
                -128
            }
        return charByte
    }

    @JvmStatic
    fun getDouble(s: String): Double {
        var s = s
        s = s.replace(", ".toRegex(), "").replace(",".toRegex(), "")
        val sb = StringBuilder()
        var c: Char
        var foundStart = false
        for (i in 0 until s.length) {
            c = s[i]
            if (Character.isDigit(c) || c == '-' || c == '.') {
                sb.append(c)
                foundStart = true
            } else if (foundStart) {
                break
            }
        }
        try {
            val amount = sb.toString().toDouble()
            return amount
        } catch (e: NumberFormatException) {
            return 0.0
        }
    }

    @JvmStatic
    fun getNameHash(str: String): Int {
        var str = str
        str = str.lowercase(Locale.getDefault())
        var hash = 0
        for (index in 0 until str.length) hash = getByte(str[index]) + ((hash shl 5) - hash)
        return hash
    }

    @JvmStatic
    fun getString(s: String): String {
        return s
            .replace("\\<.*?>".toRegex(), "")
            .replace("&#160;".toRegex(), "")
            .replace("Discontinued Item:".toRegex(), "")
    }

    @JvmStatic
    fun splitIntoLine(
        input: String,
        maxCharInLine: Int,
    ): Array<String> {
        val tok = StringTokenizer(input, " ")
        val output = java.lang.StringBuilder(input.length)
        var lineLen = 0
        while (tok.hasMoreTokens()) {
            var word = tok.nextToken()

            while (word.length > maxCharInLine) {
                output.append(word.substring(0, maxCharInLine - lineLen) + "\n")
                word = word.substring(maxCharInLine - lineLen)
                lineLen = 0
            }

            if (lineLen + word.length > maxCharInLine) {
                output.append("\n")
                lineLen = 0
            }
            output.append("$word ")

            lineLen += word.length + 1
        }
        // output.split();
        // return output.toString();
        return output
            .toString()
            .split("\n".toRegex())
            .dropLastWhile { it.isEmpty() }
            .toTypedArray()
    }

    @JvmStatic
    private val anIntArray233 =
        intArrayOf(
            0,
            1024,
            2048,
            3072,
            4096,
            5120,
            6144,
            8192,
            9216,
            12288,
            10240,
            11264,
            16384,
            18432,
            17408,
            20480,
            21504,
            22528,
            23552,
            24576,
            25600,
            26624,
            27648,
            28672,
            29696,
            30720,
            31744,
            32768,
            33792,
            34816,
            35840,
            36864,
            536870912,
            16777216,
            37888,
            65536,
            38912,
            131072,
            196608,
            33554432,
            524288,
            1048576,
            1572864,
            262144,
            67108864,
            4194304,
            134217728,
            327680,
            8388608,
            2097152,
            12582912,
            13631488,
            14680064,
            15728640,
            100663296,
            101187584,
            101711872,
            101974016,
            102760448,
            102236160,
            40960,
            393216,
            229376,
            117440512,
            104857600,
            109051904,
            201326592,
            205520896,
            209715200,
            213909504,
            106954752,
            218103808,
            226492416,
            234881024,
            222298112,
            224395264,
            268435456,
            272629760,
            276824064,
            285212672,
            289406976,
            223346688,
            293601280,
            301989888,
            318767104,
            297795584,
            298844160,
            310378496,
            102498304,
            335544320,
            299892736,
            300941312,
            301006848,
            300974080,
            39936,
            301465600,
            49152,
            1073741824,
            369098752,
            402653184,
            1342177280,
            1610612736,
            469762048,
            1476395008,
            -2147483648,
            -1879048192,
            352321536,
            1543503872,
            -2013265920,
            -1610612736,
            -1342177280,
            -1073741824,
            -1543503872,
            356515840,
            -1476395008,
            -805306368,
            -536870912,
            -268435456,
            1577058304,
            -134217728,
            360710144,
            -67108864,
            364904448,
            51200,
            57344,
            52224,
            301203456,
            53248,
            54272,
            55296,
            56320,
            301072384,
            301073408,
            301074432,
            301075456,
            301076480,
            301077504,
            301078528,
            301079552,
            301080576,
            301081600,
            301082624,
            301083648,
            301084672,
            301085696,
            301086720,
            301087744,
            301088768,
            301089792,
            301090816,
            301091840,
            301092864,
            301093888,
            301094912,
            301095936,
            301096960,
            301097984,
            301099008,
            301100032,
            301101056,
            301102080,
            301103104,
            301104128,
            301105152,
            301106176,
            301107200,
            301108224,
            301109248,
            301110272,
            301111296,
            301112320,
            301113344,
            301114368,
            301115392,
            301116416,
            301117440,
            301118464,
            301119488,
            301120512,
            301121536,
            301122560,
            301123584,
            301124608,
            301125632,
            301126656,
            301127680,
            301128704,
            301129728,
            301130752,
            301131776,
            301132800,
            301133824,
            301134848,
            301135872,
            301136896,
            301137920,
            301138944,
            301139968,
            301140992,
            301142016,
            301143040,
            301144064,
            301145088,
            301146112,
            301147136,
            301148160,
            301149184,
            301150208,
            301151232,
            301152256,
            301153280,
            301154304,
            301155328,
            301156352,
            301157376,
            301158400,
            301159424,
            301160448,
            301161472,
            301162496,
            301163520,
            301164544,
            301165568,
            301166592,
            301167616,
            301168640,
            301169664,
            301170688,
            301171712,
            301172736,
            301173760,
            301174784,
            301175808,
            301176832,
            301177856,
            301178880,
            301179904,
            301180928,
            301181952,
            301182976,
            301184000,
            301185024,
            301186048,
            301187072,
            301188096,
            301189120,
            301190144,
            301191168,
            301193216,
            301195264,
            301194240,
            301197312,
            301198336,
            301199360,
            301201408,
            301202432,
        )

    @JvmStatic
    private val aByteArray235 =
        byteArrayOf(
            22,
            22,
            22,
            22,
            22,
            22,
            21,
            22,
            22,
            20,
            22,
            22,
            22,
            21,
            22,
            22,
            22,
            22,
            22,
            22,
            22,
            22,
            22,
            22,
            22,
            22,
            22,
            22,
            22,
            22,
            22,
            22,
            3,
            8,
            22,
            16,
            22,
            16,
            17,
            7,
            13,
            13,
            13,
            16,
            7,
            10,
            6,
            16,
            10,
            11,
            12,
            12,
            12,
            12,
            13,
            13,
            14,
            14,
            11,
            14,
            19,
            15,
            17,
            8,
            11,
            9,
            10,
            10,
            10,
            10,
            11,
            10,
            9,
            7,
            12,
            11,
            10,
            10,
            9,
            10,
            10,
            12,
            10,
            9,
            8,
            12,
            12,
            9,
            14,
            8,
            12,
            17,
            16,
            17,
            22,
            13,
            21,
            4,
            7,
            6,
            5,
            3,
            6,
            6,
            5,
            4,
            10,
            7,
            5,
            6,
            4,
            4,
            6,
            10,
            5,
            4,
            4,
            5,
            7,
            6,
            10,
            6,
            10,
            22,
            19,
            22,
            14,
            22,
            22,
            22,
            22,
            22,
            22,
            22,
            22,
            22,
            22,
            22,
            22,
            22,
            22,
            22,
            22,
            22,
            22,
            22,
            22,
            22,
            22,
            22,
            22,
            22,
            22,
            22,
            22,
            22,
            22,
            22,
            22,
            22,
            22,
            22,
            22,
            22,
            22,
            22,
            22,
            22,
            22,
            22,
            22,
            22,
            22,
            22,
            22,
            22,
            22,
            22,
            22,
            22,
            22,
            22,
            22,
            22,
            22,
            22,
            22,
            22,
            22,
            22,
            22,
            22,
            22,
            22,
            22,
            22,
            22,
            22,
            22,
            22,
            22,
            22,
            22,
            22,
            22,
            22,
            22,
            22,
            22,
            22,
            22,
            22,
            22,
            22,
            22,
            22,
            22,
            22,
            22,
            22,
            22,
            22,
            22,
            22,
            22,
            22,
            22,
            22,
            22,
            22,
            22,
            22,
            22,
            22,
            22,
            22,
            22,
            22,
            22,
            22,
            22,
            22,
            22,
            22,
            22,
            22,
            22,
            21,
            22,
            21,
            22,
            22,
            22,
            21,
            22,
            22,
        )

    @JvmStatic
    fun encryptPlayerChat(
        `is`: ByteArray,
        i_25_: Int,
        i_26_: Int,
        i_27_: Int,
        is_28_: ByteArray,
    ): Int {
        var i_25_ = i_25_
        var i_27_ = i_27_
        try {
            i_27_ += i_25_
            var i_29_ = 0
            var i_30_ = i_26_ shl 3
            while (i_27_ > i_25_) {
                val i_31_ = 0xff and is_28_[i_25_].toInt()
                val i_32_ = anIntArray233[i_31_]
                val i_33_ = aByteArray235[i_31_].toInt()
                var i_34_ = i_30_ shr 3
                var i_35_ = i_30_ and 0x7
                i_29_ = i_29_ and (-i_35_ shr 31)
                i_30_ += i_33_
                val i_36_ = ((-1 + (i_35_ - -i_33_)) shr 3) + i_34_
                i_35_ += 24
                `is`[i_34_] = ((i_29_ or (i_32_ ushr i_35_)).also { i_29_ = it }).toByte()
                if ((i_36_ xor -0x1) < (i_34_ xor -0x1)) {
                    i_34_++
                    i_35_ -= 8
                    `is`[i_34_] = ((i_32_ ushr i_35_).also { i_29_ = it }).toByte()
                    if (i_36_ > i_34_) {
                        i_34_++
                        i_35_ -= 8
                        `is`[i_34_] = ((i_32_ ushr i_35_).also { i_29_ = it }).toByte()
                        if (i_36_ > i_34_) {
                            i_35_ -= 8
                            i_34_++
                            `is`[i_34_] = ((i_32_ ushr i_35_).also { i_29_ = it }).toByte()
                            if (i_34_ < i_36_) {
                                i_35_ -= 8
                                i_34_++
                                `is`[i_34_] = ((i_32_ shl -i_35_).also { i_29_ = it }).toByte()
                            }
                        }
                    }
                }
                i_25_++
            }
            return -i_26_ + ((7 + i_30_) shr 3)
        } catch (runtimeexception: RuntimeException) {
            runtimeexception.printStackTrace()
        }
        return 0
    }

    @JvmStatic
    fun getValue(s: String): Int {
        var s = s
        s = s.replace(", ".toRegex(), "").replace(",".toRegex(), "")
        val sb = StringBuilder()
        var c: Char
        var foundStart = false
        for (i in 0 until s.length) {
            c = s[i]
            if (Character.isDigit(c) || c == '-') {
                sb.append(c)
                foundStart = true
            } else if (foundStart) {
                break
            }
        }
        try {
            val amount = sb.toString().toInt()
            return amount
        } catch (e: NumberFormatException) {
            return 0
        }
    }

    @JvmStatic
    fun invalidAccountName(name: String): Boolean {
        return name.length < 2 ||
            name.length > 12 ||
            name.startsWith("_") ||
            name.endsWith("_") ||
            name.contains("__") ||
            containsInvalidCharacter(
                name,
            )
    }

    @JvmStatic
    fun longToString(l: Long): String {
        var l = l
        var i = 0
        val ac = CharArray(32)
        while (l != 0L) {
            val l1 = l
            l /= 37L
            ac[11 - i++] = VALID_CHARS[(l1 - l * 37L).toInt()]
        }
        return String(ac, 12 - i, i)
    }

    @JvmStatic
    fun packGJString2(
        position: Int,
        buffer: ByteArray,
        str: String,
    ): Int {
        val length = str.length
        var offset = position
        var index = 0
        while (length > index) {
            val character = str[index].code
            if (character > 127) {
                if (character > 2047) {
                    buffer[offset++] = ((character or 919275) shr 12).toByte()
                    buffer[offset++] = (128 or ((character shr 6) and 63)).toByte()
                    buffer[offset++] = (128 or (character and 63)).toByte()
                } else {
                    buffer[offset++] = ((character or 12309) shr 6).toByte()
                    buffer[offset++] = (128 or (character and 63)).toByte()
                }
            } else {
                buffer[offset++] = character.toByte()
            }
            index++
        }
        return offset - position
    }

    @JvmStatic
    fun reducedMapping(x: Int): Long {
        var out: Long = -1
        if (x >= 97 && x <= 122) {
            out = (x - 96).toLong()
        } else if (x >= 65 && x <= 90) {
            out = (x - 37).toLong()
        } else if (x >= 48 && x <= 57) {
            out = (x - +5).toLong()
        } else if (x == 32) {
            out = 63L
        }
        return out
    }

    @JvmStatic
    fun stringToLong(s: String): Long {
        var l = 0L
        var i = 0
        while (i < s.length && i < 12) {
            val c = s[i]
            l *= 37L
            if (c >= 'A' && c <= 'Z') {
                l += ((1 + c.code) - 65).toLong()
            } else if (c >= 'a' && c <= 'z') {
                l += ((1 + c.code) - 97).toLong()
            } else if (c >= '0' && c <= '9') {
                l += ((27 + c.code) - 48).toLong()
            }
            i++
        }
        while (l % 37L == 0L && l != 0L) l /= 37L
        return l
    }

    @JvmStatic
    fun decryptPlayerChat(
        buffer: IoBuffer,
        totalChars: Int,
    ): String {
        try {
            if (totalChars == 0) return ""
            var charsDecoded = 0
            var i_4_ = 0
            var s = ""
            while (true) {
                val i_7_ = buffer.get().toByte()
                if (i_7_ >= 0) {
                    i_4_++
                } else {
                    i_4_ = anIntArray241[i_4_]
                }
                var i_8_: Int
                if ((anIntArray241[i_4_].also { i_8_ = it }) < 0) {
                    s += Char((i_8_ xor -0x1).toByte().toUShort())
                    if (totalChars <= ++charsDecoded) break
                    i_4_ = 0
                }
                if (((i_7_.toInt() and 0x40) xor -0x1) != -1) {
                    i_4_ = anIntArray241[i_4_]
                } else {
                    i_4_++
                }
                if ((anIntArray241[i_4_].also { i_8_ = it }) < 0) {
                    s += Char((i_8_ xor -0x1).toByte().toUShort())
                    if (++charsDecoded >= totalChars) break
                    i_4_ = 0
                }
                if ((0x20 and i_7_.toInt()) == 0) {
                    i_4_++
                } else {
                    i_4_ = anIntArray241[i_4_]
                }
                if ((anIntArray241[i_4_].also { i_8_ = it }) < 0) {
                    s += Char((i_8_ xor -0x1).toByte().toUShort())
                    if (totalChars <= ++charsDecoded) break
                    i_4_ = 0
                }
                if (((0x10 and i_7_.toInt()) xor -0x1) == -1) {
                    i_4_++
                } else {
                    i_4_ = anIntArray241[i_4_]
                }
                if ((anIntArray241[i_4_].also { i_8_ = it }) < 0) {
                    s += Char((i_8_ xor -0x1).toByte().toUShort())
                    if (totalChars <= ++charsDecoded) break

                    i_4_ = 0
                }
                if (((0x8 and i_7_.toInt()) xor -0x1) != -1) {
                    i_4_ = anIntArray241[i_4_]
                } else {
                    i_4_++
                }
                if ((anIntArray241[i_4_].also { i_8_ = it }) < 0) {
                    s += Char((i_8_ xor -0x1).toByte().toUShort())
                    if (++charsDecoded >= totalChars) break
                    i_4_ = 0
                }
                if ((0x4 and i_7_.toInt()) == 0) {
                    i_4_++
                } else {
                    i_4_ = anIntArray241[i_4_]
                }
                if ((anIntArray241[i_4_].also { i_8_ = it }) < 0) {
                    s += Char((i_8_ xor -0x1).toByte().toUShort())
                    if (totalChars <= ++charsDecoded) break
                    i_4_ = 0
                }
                if (((i_7_.toInt() and 0x2) xor -0x1) != -1) {
                    i_4_ = anIntArray241[i_4_]
                } else {
                    i_4_++
                }
                if ((anIntArray241[i_4_].also { i_8_ = it }) < 0) {
                    s += Char((i_8_ xor -0x1).toByte().toUShort())
                    if (totalChars <= ++charsDecoded) break
                    i_4_ = 0
                }
                if (((i_7_.toInt() and 0x1) xor -0x1) != -1) {
                    i_4_ = anIntArray241[i_4_]
                } else {
                    i_4_++
                }
                if ((anIntArray241[i_4_].also { i_8_ = it }) < 0) {
                    s += Char((i_8_ xor -0x1).toByte().toUShort())
                    if (++charsDecoded >= totalChars) break
                    i_4_ = 0
                }
            }
            return s
        } catch (runtimeexception: RuntimeException) {
            runtimeexception.printStackTrace()
        }
        return ""
    }
}
