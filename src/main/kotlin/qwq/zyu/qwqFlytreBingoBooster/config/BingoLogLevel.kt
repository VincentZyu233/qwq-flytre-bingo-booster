package qwq.zyu.qwqFlytreBingoBooster.bingo_config

enum class BingoLogLevel(val priority: Int) {
    DEBUG(0),
    INFO(1),
    WARN(2),
    ERROR(3),
    SILENT(4);

    companion object {
        fun fromString(s: String): BingoLogLevel {
            return entries.firstOrNull { it.name.equals(s, ignoreCase = true) } ?: INFO
        }
    }
}
