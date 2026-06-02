package qwq.zyu.qwqFlytreBingoBooster

import org.bukkit.plugin.java.JavaPlugin
import qwq.zyu.qwqFlytreBingoBooster.bingosidebar.BingoSidebarCommand
import qwq.zyu.qwqFlytreBingoBooster.config.DetectionMethod
import qwq.zyu.qwqFlytreBingoBooster.config.LogLevel
import qwq.zyu.qwqFlytreBingoBooster.config.PluginLogger
import qwq.zyu.qwqFlytreBingoBooster.config.TeamDetector
import qwq.zyu.qwqFlytreBingoBooster.teamcolor.TeamColorCommand
import qwq.zyu.qwqFlytreBingoBooster.teamcolor.TeamColorTask

class QwqFlytreBingoBooster : JavaPlugin() {

    private lateinit var teamColorTask: TeamColorTask

    override fun onEnable() {
        saveDefaultConfig()
        reloadConfig()

        val logLevelStr = config.getString("log_level", "info") ?: "info"
        PluginLogger.level = LogLevel.fromString(logLevelStr)
        PluginLogger.info("配置加载完成，日志级别: ${PluginLogger.level.name.lowercase()}")

        val methodStr = config.getString("team_detection.method", "team") ?: "team"
        val sbName = config.getString("team_detection.scoreboard_name", "teamScore") ?: "teamScore"
        val method = DetectionMethod.entries.firstOrNull { it.name.equals(methodStr, true) } ?: DetectionMethod.TEAM
        val teamDetector = TeamDetector(method, sbName)
        PluginLogger.info("队伍检测方式: ${method.name.lowercase()}, 计分板名: $sbName")

        teamColorTask = TeamColorTask(teamDetector)
        teamColorTask.runTaskTimer(this, 0L, 20L)

        getCommand("qwq_set_scheduled_team_color_dye")?.setExecutor(TeamColorCommand(teamColorTask))
        getCommand("qwq_bingo_sidebar")?.setExecutor(BingoSidebarCommand(this, teamDetector))

        PluginLogger.info("qwq-flytre-bingo-booster 已启用")
    }

    override fun onDisable() {
        if (::teamColorTask.isInitialized) {
            teamColorTask.cancel()
        }
        PluginLogger.info("qwq-flytre-bingo-booster 已禁用")
    }
}
