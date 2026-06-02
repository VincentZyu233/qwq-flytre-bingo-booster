package qwq.zyu.qwqFlytreBingoBooster.config

import org.bukkit.Bukkit
import org.bukkit.entity.Player

enum class DetectionMethod { TEAM, SCOREBOARD }

class TeamDetector(
    val method: DetectionMethod,
    val scoreboardName: String
) {
    fun getTeamValue(player: Player): Int {
        return when (method) {
            DetectionMethod.TEAM -> when (player.team?.name) {
                "red" -> 1
                "yellow" -> 2
                "green" -> 3
                "blue" -> 4
                else -> 0
            }
            DetectionMethod.SCOREBOARD -> {
                val scoreboard = Bukkit.getScoreboardManager()?.mainScoreboard ?: return 0
                val objective = scoreboard.getObjective(scoreboardName) ?: return 0
                val score = objective.getScore(player.name)
                if (score.isScoreSet) score.score else 0
            }
        }
    }

    fun getTeamName(player: Player): String {
        return when (method) {
            DetectionMethod.TEAM -> player.team?.name ?: "none"
            DetectionMethod.SCOREBOARD -> {
                val scoreboard = Bukkit.getScoreboardManager()?.mainScoreboard ?: return "none"
                scoreboard.getEntryTeam(player.name)?.name ?: "none"
            }
        }
    }
}
