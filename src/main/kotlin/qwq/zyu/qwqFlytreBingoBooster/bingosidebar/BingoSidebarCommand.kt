package qwq.zyu.qwqFlytreBingoBooster.bingosidebar

import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scoreboard.DisplaySlot
import org.bukkit.scoreboard.Scoreboard
import qwq.zyu.qwqFlytreBingoBooster.config.PluginLogger

class BingoSidebarCommand(private val plugin: Plugin) : CommandExecutor {

    private val scoreboard: Scoreboard = Bukkit.getScoreboardManager()!!.newScoreboard
    private var task: BukkitRunnable? = null

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (args.size != 1) {
            sender.sendMessage("用法: /qwq_bingo_sidebar <enable? true/false>")
            return false
        }

        return when (args[0].lowercase()) {
            "true" -> enable(sender)
            "false" -> disable(sender)
            else -> {
                sender.sendMessage("用法: /qwq_bingo_sidebar <enable? true/false>")
                false
            }
        }
    }

    private fun enable(sender: CommandSender): Boolean {
        if (task != null) {
            sender.sendMessage("sidebar的更新任务已经在运行。")
            return false
        }
        task = object : BukkitRunnable() {
            override fun run() = updateSidebar()
        }.apply {
            runTaskTimer(plugin, 0L, 20 * 10L)
        }
        PluginLogger.info("BingoSidebar: 更新任务已开启")
        sender.sendMessage("已经开启sidebar的更新任务")
        return true
    }

    private fun disable(sender: CommandSender): Boolean {
        if (task == null) {
            sender.sendMessage("没有sidebar更新任务！停什喵！")
            return false
        }
        task!!.cancel()
        task = null
        PluginLogger.info("BingoSidebar: 更新任务已停止")
        sender.sendMessage("已经停止sidebar更新")
        return true
    }

    private fun updateSidebar() {
        var objective = scoreboard.getObjective("teamDisplay")
        if (objective == null) {
            objective = scoreboard.registerNewObjective("teamDisplay", "dummy", "---队伍---")
        }
        objective.displaySlot = DisplaySlot.SIDEBAR

        val teamMemberMap = mutableMapOf<String, MutableList<String>>()
        val teamScoresMap = mutableMapOf<String, Int>()
        val colorNames = arrayOf("无", "红", "黄", "绿", "蓝")

        for (player in Bukkit.getOnlinePlayers()) {
            val teamValue = getTeamValue(player)
            val teamName = colorNames[teamValue]
            PluginLogger.debug("BingoSidebar: ${player.name} -> teamName=$teamName, teamValue=$teamValue")
            teamMemberMap.getOrPut(teamName) { mutableListOf() }.add(player.name)
            teamScoresMap[teamName] = teamScoresMap.getOrDefault(teamName, 0) + 1
        }

        PluginLogger.debug("BingoSidebar: 队伍统计 -> $teamScoresMap")

        val teamOrder = listOf("红", "黄", "绿", "蓝")
        for (teamName in teamOrder) {
            val teamLine = "${teamName}队"
            objective.getScore(teamLine).score = teamScoresMap.getOrDefault(teamName, 0)

            val players = teamMemberMap.getOrDefault(teamName, mutableListOf()).sorted()
            for (playerName in players) {
                val player = Bukkit.getPlayer(playerName) ?: continue
                objective.getScore("$teamName【$playerName】").score = getTeamValue(player)
            }
        }

        Bukkit.getOnlinePlayers().forEach { it.scoreboard = scoreboard }
    }

    private val mainScoreboard get() = Bukkit.getScoreboardManager()!!.mainScoreboard

    private fun getTeamName(player: Player): String {
        val team = mainScoreboard.getEntryTeam(player.name)
        PluginLogger.debug("BingoSidebar: ${player.name} 在主计分板中的队伍 = ${team?.name ?: "null"}")
        return team?.name ?: "none"
    }

    private fun getTeamValue(player: Player): Int {
        return when (getTeamName(player)) {
            "red" -> 1
            "yellow" -> 2
            "green" -> 3
            "blue" -> 4
            else -> 0
        }
    }
}
