package qwq.zyu.qwqFlytreBingoBooster.bingosidebar

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scoreboard.DisplaySlot
import org.bukkit.scoreboard.Scoreboard
import qwq.zyu.qwqFlytreBingoBooster.config.PluginLogger
import qwq.zyu.qwqFlytreBingoBooster.config.TeamDetector
import qwq.zyu.qwqFlytreBingoBooster.type.TeamVisual

class BingoSidebarTask(
    private val teamDetector: TeamDetector
) : BukkitRunnable() {
    private val lastEntriesByScoreboard = mutableMapOf<Scoreboard, Set<String>>()

    override fun run() {
        updateSidebar()
    }

    private fun updateSidebar() {
        val teamMemberMap = mutableMapOf<TeamVisual, MutableList<String>>()
        val teamScoresMap = mutableMapOf<TeamVisual, Int>()
        val teamValueMap = mutableMapOf<String, TeamVisual>()

        for (player in Bukkit.getOnlinePlayers()) {
            val teamVisual = TeamVisual.fromScoreValue(teamDetector.getTeamValue(player))
            PluginLogger.debug("BingoSidebar: ${player.name} -> team=${teamVisual.shortName}, teamValue=${teamVisual.scoreValue}")
            teamMemberMap.getOrPut(teamVisual) { mutableListOf() }.add(player.name)
            teamScoresMap[teamVisual] = teamScoresMap.getOrDefault(teamVisual, 0) + 1
            teamValueMap[player.name] = teamVisual
        }

        PluginLogger.debug("BingoSidebar: 队伍统计 -> $teamScoresMap")

        val teamOrder = listOf(TeamVisual.RED, TeamVisual.YELLOW, TeamVisual.GREEN, TeamVisual.BLUE)
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
            for (teamVisual in teamOrder) {
                val teamLine = "${teamVisual.sidebarLabel}队"
                objective.getScore(teamLine).score = teamVisual.scoreValue
                currentEntries.add(teamLine)

                val players = teamMemberMap.getOrDefault(teamVisual, mutableListOf()).sorted()
                for (playerName in players) {
                    val playerTeam = teamValueMap[playerName] ?: TeamVisual.NONE
                    val entry = "${playerTeam.color}${playerTeam.sidebarLabel}$playerName${ChatColor.RESET}"
                    objective.getScore(entry).score = playerTeam.scoreValue
                    currentEntries.add(entry)
                }
            }

            lastEntriesByScoreboard[scoreboard] = currentEntries.toSet()
        }
    }
}
