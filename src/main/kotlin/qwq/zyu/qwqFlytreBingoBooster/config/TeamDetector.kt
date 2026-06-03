package qwq.zyu.qwqFlytreBingoBooster.config

import org.bukkit.Bukkit
import org.bukkit.entity.Player

enum class DetectionMethod { TEAM, SCOREBOARD }

class TeamDetector(
    val method: DetectionMethod,
    val scoreboardName: String
) {
    private val mainScoreboard get() = Bukkit.getScoreboardManager()?.mainScoreboard

    private fun resolveTeamName(player: Player): String {
        val playerTeam = player.scoreboard.getEntryTeam(player.name)?.name
        if (!playerTeam.isNullOrBlank()) return playerTeam

        val mainTeam = mainScoreboard?.getEntryTeam(player.name)?.name
        if (!mainTeam.isNullOrBlank()) return mainTeam

        return ""
    }

    fun getTeamValue(player: Player): Int {
        return when (method) {
            DetectionMethod.TEAM -> {
                val teamName = resolveTeamName(player)
                when (teamName) {
                    "red" -> 1
                    "yellow" -> 2
                    "green" -> 3
                    "blue" -> 4
                    else -> 0
                }
            }
            DetectionMethod.SCOREBOARD -> {
                val scoreboard = mainScoreboard ?: return 0
                val objective = scoreboard.getObjective(scoreboardName) ?: return 0
                val score = objective.getScore(player.name)
                if (score.isScoreSet) score.score else 0
            }
        }
    }

    fun getTeamName(player: Player): String {
        return when (method) {
            DetectionMethod.TEAM -> resolveTeamName(player).ifBlank { "none" }
            DetectionMethod.SCOREBOARD -> {
                val scoreboard = mainScoreboard ?: return "none"
                scoreboard.getEntryTeam(player.name)?.name ?: "none"
            }
        }
    }
}
