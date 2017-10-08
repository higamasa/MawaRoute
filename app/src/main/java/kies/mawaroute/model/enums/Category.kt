package kies.mawaroute.model.enums

/**
 * おっきめのジャンル
 */
enum class Category(val code: String) {

    // 居酒屋
    IZAKAYA("RSFST09000"),

    // 日本料理・郷土料理
    TRADITIONAL_JAPANESE("RSFST02000"),

    // 🍣・魚料理・シーフード
    SUSHI_SEAFOOD("RSFST03000"),

    // 🍲
    NABE("RSFST04000"),

    // 焼肉・ホルモン
    YAKINIKU_HORUMON("RSFST05000"),

    // 焼き鳥・肉料理・串料理
    YAKITORI("RSFST06000"),

    // 和食
    JAPANESE_FOOD("RSFST01000"),

    // お好み焼き・粉物
    OKONOMIYAKI("RSFST07000"),

    // 🍜
    NOODLES("RSFST08000"),

    // 中華
    CHINESE("RSFST14000"),

    // イタリアン・フレンチ
    ITALIAN_FRENCH("RSFST11000"),

    // 洋食
    WESTERN_EUROPEAN("RSFST13000"),

    // 欧米・各国料理
    WESTERN_VARIOUS("RSFST12000"),

    // 🍛
    CURRY("RSFST16000"),

    // アジア・エスニック料理
    SOUTHEAST_ASIAN("RSFST15000"),

    // オーガニック・創作料理
    ORGANIC_FUSION("RSFST17000"),

    // 🍻ダイニングバー・バー・ビアホール🍻
    BARS("RSFST10000"),

    // 🍺
    ALCOHOL("RSFST21000"),

    // カフェ☕・スイーツ🍰
    CAFE("RSFST18000"),

    // 🎉宴会・カラオケ・エンターテイメント🎉
    ENTERTAINMENT("RSFST19000"),

    // ファミレス・ファーストフード
    FAST_FOOD("RSFST20000"),

    // その他全部
    OTHER("RSFST90000");

    companion object {
        fun of(code: String): Category = values().firstOrNull { it.code == code } ?: OTHER
    }
}
