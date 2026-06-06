package qwq.zyu.qwqFlytreBingoBooster.bingo_config

import org.bukkit.Bukkit

object BingoPluginLogger {
    var level: BingoLogLevel = BingoLogLevel.INFO

    private val logger get() = Bukkit.getLogger()

    fun debug(msg: String) {
        if (level.priority <= BingoLogLevel.DEBUG.priority) {
            logger.info("[qwq-booster][DEBUG] $msg")
        }
    }

    fun info(msg: String) {
        if (level.priority <= BingoLogLevel.INFO.priority) {
            logger.info("[qwq-booster][INFO] $msg")
        }
    }

    fun warn(msg: String) {
        if (level.priority <= BingoLogLevel.WARN.priority) {
            logger.warning("[qwq-booster][WARN] $msg")
        }
    }

    fun error(msg: String) {
        if (level.priority <= BingoLogLevel.ERROR.priority) {
            logger.severe("[qwq-booster][ERROR] $msg")
        }
    }
}
