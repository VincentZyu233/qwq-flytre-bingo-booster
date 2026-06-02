package qwq.zyu.qwqFlytreBingoBooster.config

import org.bukkit.Bukkit

object PluginLogger {
    var level: LogLevel = LogLevel.INFO

    private val logger get() = Bukkit.getLogger()

    fun debug(msg: String) {
        if (level.priority <= LogLevel.DEBUG.priority) {
            logger.info("[qwq-booster][DEBUG] $msg")
        }
    }

    fun info(msg: String) {
        if (level.priority <= LogLevel.INFO.priority) {
            logger.info("[qwq-booster][INFO] $msg")
        }
    }

    fun warn(msg: String) {
        if (level.priority <= LogLevel.WARN.priority) {
            logger.warning("[qwq-booster][WARN] $msg")
        }
    }

    fun error(msg: String) {
        if (level.priority <= LogLevel.ERROR.priority) {
            logger.severe("[qwq-booster][ERROR] $msg")
        }
    }
}
