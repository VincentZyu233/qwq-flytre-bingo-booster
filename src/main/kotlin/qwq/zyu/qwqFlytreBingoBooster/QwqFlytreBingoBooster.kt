package qwq.zyu.qwqFlytreBingoBooster

import org.bukkit.command.CommandExecutor
import org.bukkit.command.PluginCommand
import org.bukkit.plugin.java.JavaPlugin
import qwq.zyu.qwqFlytreBingoBooster.bingo_config.BingoLogLevel
import qwq.zyu.qwqFlytreBingoBooster.bingo_config.BingoPluginLogger
import qwq.zyu.qwqFlytreBingoBooster.bingo_config.BingoTeamDetectionMethod
import qwq.zyu.qwqFlytreBingoBooster.bingo_config.BingoTeamDetector
import qwq.zyu.qwqFlytreBingoBooster.bingo_effect.BingoEffectCommand
import qwq.zyu.qwqFlytreBingoBooster.bingo_effect.BingoEffectEntry
import qwq.zyu.qwqFlytreBingoBooster.bingo_effect.BingoEffectTask
import qwq.zyu.qwqFlytreBingoBooster.sidebar.BingoSidebarCommand
import qwq.zyu.qwqFlytreBingoBooster.bingo_teamcolor.BingoTeamColorCommand
import qwq.zyu.qwqFlytreBingoBooster.bingo_teamcolor.BingoTeamColorTask

class QwqFlytreBingoBooster : JavaPlugin() {

    private lateinit var teamColorTask: BingoTeamColorTask
    private lateinit var bingoSidebarCommand: BingoSidebarCommand
    private lateinit var bingoEffectTask: BingoEffectTask
    private val registeredConfigCommands = mutableListOf<String>()

    override fun onEnable() {
        saveDefaultConfig()
        reloadConfig()

        val logLevelStr = config.getString("log_level", "info") ?: "info"
        BingoPluginLogger.level = BingoLogLevel.fromString(logLevelStr)
        BingoPluginLogger.info("配置加载完成，日志级别: ${BingoPluginLogger.level.name.lowercase()}")

        val methodStr = config.getString("team_detection.method", "team") ?: "team"
        val sbName = config.getString("team_detection.scoreboard_name", "teamScore") ?: "teamScore"
        val method = BingoTeamDetectionMethod.entries.firstOrNull { it.name.equals(methodStr, true) } ?: BingoTeamDetectionMethod.TEAM
        val teamDetector = BingoTeamDetector(method, sbName)
        BingoPluginLogger.info("队伍检测方式: ${method.name.lowercase()}, 计分板名: $sbName")

        val teamColorRefreshTicks = getPositiveTicks("features.team_color_dye.refresh_interval_ticks", 10L)
        val bingoSidebarRefreshTicks = getPositiveTicks("features.bingo_sidebar.refresh_interval_ticks", 10L)
        val bingoEffectRefreshTicks = getPositiveTicks("features.bingo_effect.refresh_interval_ticks", 10L)
        val bingoEffectDurationTicks = getPositiveTicks("features.bingo_effect.apply_duration_ticks", 114514L)

        teamColorTask = BingoTeamColorTask(teamDetector)
        teamColorTask.runTaskTimer(this, 0L, teamColorRefreshTicks)
        bingoEffectTask = BingoEffectTask { bingoEffectDurationTicks.toInt() }
        bingoEffectTask.updateEntries(loadBingoEffectEntries())
        bingoEffectTask.runTaskTimer(this, 0L, bingoEffectRefreshTicks)

        val teamColorCommandName = config.getString("commands.team_color_dye", "qwq_bingo_team_color_dye")
            ?: "qwq_bingo_team_color_dye"
        val bingoSidebarCommandName = config.getString("commands.bingo_sidebar", "qwq_bingo_sidebar")
            ?: "qwq_bingo_sidebar"
        val bingoEffectCommandName = config.getString("commands.bingo_effect", "qwq_bingo_effect")
            ?: "qwq_bingo_effect"
        val enableTeamColorOnLoad = config.getBoolean("features.team_color_dye.enabled_on_load", true)
        val enableBingoSidebarOnLoad = config.getBoolean("features.bingo_sidebar.enabled_on_load", true)
        val enableBingoEffectOnLoad = config.getBoolean("features.bingo_effect.enabled_on_load", true)

        val permissionMode = config.getString("permission_mode", "op") ?: "op"
        BingoPluginLogger.info("权限模式: $permissionMode")

        val teamColorInner = BingoTeamColorCommand(teamColorTask, teamColorCommandName)
        val teamColorExecutor = PermissionGuardExecutor(teamColorInner, permissionMode)

        bingoSidebarCommand = BingoSidebarCommand(this, teamDetector, bingoSidebarCommandName, bingoSidebarRefreshTicks)
        val bingoSidebarExecutor = PermissionGuardExecutor(bingoSidebarCommand, permissionMode)

        val bingoEffectInner = BingoEffectCommand(this, bingoEffectTask, bingoEffectCommandName)
        val bingoEffectExecutor = PermissionGuardExecutor(bingoEffectInner, permissionMode)

        bindConfiguredCommand(
            defaultName = "qwq_bingo_team_color_dye",
            configuredName = teamColorCommandName,
            executor = teamColorExecutor,
            usage = "/$teamColorCommandName <true/false>"
        )
        bindConfiguredCommand(
            defaultName = "qwq_bingo_sidebar",
            configuredName = bingoSidebarCommandName,
            executor = bingoSidebarExecutor,
            usage = "/$bingoSidebarCommandName <true/false>"
        )
        bindConfiguredCommand(
            defaultName = "qwq_bingo_effect",
            configuredName = bingoEffectCommandName,
            executor = bingoEffectExecutor,
            usage = "/$bingoEffectCommandName <true/false> <effect> <amplifier> <true/false>"
        )

        teamColorTask.enabled = enableTeamColorOnLoad
        if (enableBingoSidebarOnLoad) {
            bingoSidebarCommand.enable()
        }
        bingoEffectTask.enabled = enableBingoEffectOnLoad

        server.pluginManager.registerEvents(
            BingoStatusBroadcastListener(this, teamColorTask, bingoSidebarCommand, bingoEffectTask),
            this
        )

        BingoPluginLogger.info("命令名: team_color_dye=/$teamColorCommandName, bingo_sidebar=/$bingoSidebarCommandName, bingo_effect=/$bingoEffectCommandName")
        BingoPluginLogger.info("默认启用: team_color_dye=$enableTeamColorOnLoad, bingo_sidebar=$enableBingoSidebarOnLoad, bingo_effect=$enableBingoEffectOnLoad")

        BingoPluginLogger.info("qwq-flytre-bingo-booster 已启用")
    }

    override fun onDisable() {
        if (::teamColorTask.isInitialized) {
            teamColorTask.cancel()
        }
        if (::bingoSidebarCommand.isInitialized) {
            bingoSidebarCommand.disable()
        }
        if (::bingoEffectTask.isInitialized) {
            bingoEffectTask.removeManagedEffectsFromOnlinePlayers()
            bingoEffectTask.cancel()
        }
        registeredConfigCommands.forEach { name ->
            try {
                val knownCommands = getKnownCommands()
                knownCommands.remove(name)
                knownCommands.remove("${description.name.lowercase()}:$name")
            } catch (_: Exception) {
            }
        }
        BingoPluginLogger.info("qwq-flytre-bingo-booster 已禁用")
    }

    private fun getPositiveTicks(path: String, defaultValue: Long): Long {
        val value = config.getLong(path, defaultValue)
        return if (value >= 1L) value else defaultValue
    }

    private fun loadBingoEffectEntries(): List<BingoEffectEntry> {
        return config.getMapList("bingo_effects").mapNotNull { raw ->
            val type = raw["type"]?.toString()?.trim().orEmpty()
            if (type.isEmpty()) {
                BingoPluginLogger.warn("检测到空的 bingo_effects.type，已跳过")
                return@mapNotNull null
            }
            BingoEffectEntry(
                enabled = raw["enabled"] as? Boolean ?: true,
                type = type,
                amplifier = ((raw["amplifier"] as? Number)?.toInt() ?: 0).coerceAtLeast(0),
                hideParticles = raw["hide_particles"] as? Boolean ?: true
            )
        }
    }

    private fun bindConfiguredCommand(
        defaultName: String,
        configuredName: String,
        executor: CommandExecutor,
        usage: String
    ) {
        getCommand(defaultName)?.setExecutor(executor)
        getCommand(defaultName)?.usage = usage

        val normalizedName = configuredName.trim()
        if (normalizedName.isEmpty() || normalizedName.equals(defaultName, ignoreCase = true)) {
            return
        }

        if (getKnownCommands().containsKey(normalizedName)) {
            BingoPluginLogger.warn("命令 '/$normalizedName' 已存在，跳过配置化注册")
            return
        }

        try {
            val pluginCommand = createPluginCommand(normalizedName)
            pluginCommand.setExecutor(executor)
            pluginCommand.usage = usage.replace(configuredName, normalizedName)
            getCommandMap().register(description.name.lowercase(), pluginCommand)
            registeredConfigCommands.add(normalizedName)
        } catch (e: Exception) {
            BingoPluginLogger.error("注册配置命令 '/$normalizedName' 失败: ${e.message}")
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
