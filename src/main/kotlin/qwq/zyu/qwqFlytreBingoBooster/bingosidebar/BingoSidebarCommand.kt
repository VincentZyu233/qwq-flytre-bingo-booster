package qwq.zyu.qwqFlytreBingoBooster.bingosidebar

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scoreboard.DisplaySlot
import org.bukkit.scoreboard.Scoreboard
import qwq.zyu.qwqFlytreBingoBooster.config.PluginLogger
import qwq.zyu.qwqFlytreBingoBooster.config.TeamDetector

class BingoSidebarCommand(
    private val plugin: Plugin,
    private val teamDetector: TeamDetector,
    private val commandName: String
) : CommandExecutor {
    private var task: BukkitRunnable? = null
    private val lastEntriesByScoreboard = mutableMapOf<Scoreboard, Set<String>>()

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (args.size != 1) {
            sender.sendMessage("用法: /$commandName <enable? true/false>")
            return false
        }

        return when (args[0].lowercase()) {
            "true" -> enable(sender)
            "false" -> disable(sender)
            else -> {
                sender.sendMessage("用法: /$commandName <enable? true/false>")
                false
            }
        }
    }

    fun enable(sender: CommandSender? = null): Boolean {
        if (task != null) {
            sender?.sendMessage("sidebar的更新任务已经在运行。")
            return false
        }
        task = object : BukkitRunnable() {
            override fun run() = updateSidebar()
        }.apply {
            runTaskTimer(plugin, 0L, 10L)
        }
        PluginLogger.info("BingoSidebar: 更新任务已开启")
        sender?.sendMessage("已经开启sidebar的更新任务")
        return true
    }

    fun disable(sender: CommandSender? = null): Boolean {
        if (task == null) {
            sender?.sendMessage("没有sidebar更新任务！停什喵！")
            return false
        }
        task!!.cancel()
        task = null
        PluginLogger.info("BingoSidebar: 更新任务已停止")
        sender?.sendMessage("已经停止sidebar更新")
        return true
    }

    private fun updateSidebar() {
        val teamMemberMap = mutableMapOf<String, MutableList<String>>()
        val teamScoresMap = mutableMapOf<String, Int>()
        val teamValueMap = mutableMapOf<String, Int>()
        val colorNames = arrayOf("无", "红", "黄", "绿", "蓝")

        for (player in Bukkit.getOnlinePlayers()) {
            val teamValue = teamDetector.getTeamValue(player)
            val teamName = colorNames[teamValue]
            PluginLogger.debug("BingoSidebar: ${player.name} -> teamName=$teamName, teamValue=$teamValue")
            teamMemberMap.getOrPut(teamName) { mutableListOf() }.add(player.name)
            teamScoresMap[teamName] = teamScoresMap.getOrDefault(teamName, 0) + 1
            teamValueMap[player.name] = teamValue
        }

        PluginLogger.debug("BingoSidebar: 队伍统计 -> $teamScoresMap")

        val teamOrder = listOf("红", "黄", "绿", "蓝")
        val teamLabels = mapOf(
            "红" to "【Red红】",
            "黄" to "【Yellow黄】",
            "绿" to "【Green绿】",
            "蓝" to "【Blue蓝】"
        )
        val teamColors = mapOf(
            "红" to ChatColor.RED,
            "黄" to ChatColor.YELLOW,
            "绿" to ChatColor.GREEN,
            "蓝" to ChatColor.BLUE
        )
        val currentEntries = linkedSetOf<String>()

        val uniqueScoreboards = Bukkit.getOnlinePlayers()
            .map { it.scoreboard }
            .toSet()

        for (scoreboard in uniqueScoreboards) {
            var objective = scoreboard.getObjective("qwqBingoSidebar")
            if (objective == null) {
                objective = scoreboard.registerNewObjective("qwqBingoSidebar", "dummy", "---Teams队伍---")
            }
            objective.displaySlot = DisplaySlot.SIDEBAR

            lastEntriesByScoreboard[scoreboard].orEmpty().forEach { scoreboard.resetScores(it) }

            currentEntries.clear()
            for (teamName in teamOrder) {
                val teamLabel = teamLabels[teamName] ?: teamName
                val teamLine = "${teamLabel}队"
                objective.getScore(teamLine).score = teamScoresMap.getOrDefault(teamName, 0)
                currentEntries.add(teamLine)

                val players = teamMemberMap.getOrDefault(teamName, mutableListOf()).sorted()
                for (playerName in players) {
                    val color = teamColors[teamName] ?: ChatColor.WHITE
                    val entry = "$color${teamLabel}$playerName${ChatColor.RESET}"
                    objective.getScore(entry).score = teamValueMap[playerName] ?: 0
                    currentEntries.add(entry)
                }
            }

            lastEntriesByScoreboard[scoreboard] = currentEntries.toSet()
        }
    }
}
