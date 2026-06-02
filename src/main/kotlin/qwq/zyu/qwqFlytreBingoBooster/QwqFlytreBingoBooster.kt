package qwq.zyu.qwqFlytreBingoBooster

import org.bukkit.plugin.java.JavaPlugin
import qwq.zyu.qwqFlytreBingoBooster.bingosidebar.BingoSidebarCommand
import qwq.zyu.qwqFlytreBingoBooster.config.LogLevel
import qwq.zyu.qwqFlytreBingoBooster.config.PluginLogger
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

        teamColorTask = TeamColorTask()
        teamColorTask.runTaskTimer(this, 0L, 1L)

        getCommand("qwq_set_scheduled_team_color_dye")?.setExecutor(TeamColorCommand(teamColorTask))
        getCommand("qwq_bingo_sidebar")?.setExecutor(BingoSidebarCommand(this))

        PluginLogger.info("qwq-flytre-bingo-booster 已启用")
    }

    override fun onDisable() {
        if (::teamColorTask.isInitialized) {
            teamColorTask.cancel()
        }
        PluginLogger.info("qwq-flytre-bingo-booster 已禁用")
    }
}
