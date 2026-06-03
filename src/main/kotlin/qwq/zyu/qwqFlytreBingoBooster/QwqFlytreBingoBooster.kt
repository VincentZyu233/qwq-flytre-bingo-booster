package qwq.zyu.qwqFlytreBingoBooster

import org.bukkit.command.CommandExecutor
import org.bukkit.command.PluginCommand
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
    private lateinit var bingoSidebarCommand: BingoSidebarCommand
    private val registeredConfigCommands = mutableListOf<String>()

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
        teamColorTask.runTaskTimer(this, 0L, 10L)

        val teamColorCommandName = config.getString("commands.team_color_dye", "qwq_set_scheduled_team_color_dye")
            ?: "qwq_set_scheduled_team_color_dye"
        val bingoSidebarCommandName = config.getString("commands.bingo_sidebar", "qwq_bingo_sidebar")
            ?: "qwq_bingo_sidebar"
        val enableTeamColorOnLoad = config.getBoolean("features.team_color_dye.enabled_on_load", true)
        val enableBingoSidebarOnLoad = config.getBoolean("features.bingo_sidebar.enabled_on_load", true)

        val teamColorExecutor = TeamColorCommand(teamColorTask, teamColorCommandName)
        bingoSidebarCommand = BingoSidebarCommand(this, teamDetector, bingoSidebarCommandName)

        bindConfiguredCommand(
            defaultName = "qwq_set_scheduled_team_color_dye",
            configuredName = teamColorCommandName,
            executor = teamColorExecutor
        )
        bindConfiguredCommand(
            defaultName = "qwq_bingo_sidebar",
            configuredName = bingoSidebarCommandName,
            executor = bingoSidebarCommand
        )

        teamColorTask.enabled = enableTeamColorOnLoad
        if (enableBingoSidebarOnLoad) {
            bingoSidebarCommand.enable()
        }

        PluginLogger.info("命令名: team_color_dye=/$teamColorCommandName, bingo_sidebar=/$bingoSidebarCommandName")
        PluginLogger.info("默认启用: team_color_dye=$enableTeamColorOnLoad, bingo_sidebar=$enableBingoSidebarOnLoad")

        PluginLogger.info("qwq-flytre-bingo-booster 已启用")
    }

    override fun onDisable() {
        if (::teamColorTask.isInitialized) {
            teamColorTask.cancel()
        }
        if (::bingoSidebarCommand.isInitialized) {
            bingoSidebarCommand.disable()
        }
        registeredConfigCommands.forEach { name ->
            try {
                val knownCommands = getKnownCommands()
                knownCommands.remove(name)
                knownCommands.remove("${description.name.lowercase()}:$name")
            } catch (_: Exception) {
            }
        }
        PluginLogger.info("qwq-flytre-bingo-booster 已禁用")
    }

    private fun bindConfiguredCommand(defaultName: String, configuredName: String, executor: CommandExecutor) {
        getCommand(defaultName)?.setExecutor(executor)

        val normalizedName = configuredName.trim()
        if (normalizedName.isEmpty() || normalizedName.equals(defaultName, ignoreCase = true)) {
            return
        }

        if (getKnownCommands().containsKey(normalizedName)) {
            PluginLogger.warn("命令 '/$normalizedName' 已存在，跳过配置化注册")
            return
        }

        try {
            val pluginCommand = createPluginCommand(normalizedName)
            pluginCommand.executor = executor
            pluginCommand.usage = "/$normalizedName <true/false>"
            getCommandMap().register(description.name.lowercase(), pluginCommand)
            registeredConfigCommands.add(normalizedName)
        } catch (e: Exception) {
            PluginLogger.error("注册配置命令 '/$normalizedName' 失败: ${e.message}")
        }
    }

    private fun createPluginCommand(name: String): PluginCommand {
        val constructor = PluginCommand::class.java.getDeclaredConstructor(String::class.java, org.bukkit.plugin.Plugin::class.java)
        constructor.isAccessible = true
        return constructor.newInstance(name, this)
    }

    @Suppress("UNCHECKED_CAST")
    private fun getCommandMap(): org.bukkit.command.SimpleCommandMap {
        val field = server.javaClass.getDeclaredField("commandMap")
        field.isAccessible = true
        return field.get(server) as org.bukkit.command.SimpleCommandMap
    }

    @Suppress("UNCHECKED_CAST")
    private fun getKnownCommands(): MutableMap<String, org.bukkit.command.Command> {
        val field = org.bukkit.command.SimpleCommandMap::class.java.getDeclaredField("knownCommands")
        field.isAccessible = true
        return field.get(getCommandMap()) as MutableMap<String, org.bukkit.command.Command>
    }
}
