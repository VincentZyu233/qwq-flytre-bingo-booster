package qwq.zyu.qwqFlytreBingoBooster.config

import org.bukkit.Bukkit
import org.bukkit.entity.Player

enum class DetectionMethod { TEAM, SCOREBOARD }

class TeamDetector(
    val method: DetectionMethod,
    val scoreboardName: String
) {
    private val mainScoreboard get() = Bukkit.getScoreboardManager()?.mainScoreboard

    fun getTeamValue(player: Player): Int {
        return when (method) {
            DetectionMethod.TEAM -> {
                val teamName = mainScoreboard?.getEntryTeam(player.name)?.name ?: ""
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
            DetectionMethod.TEAM -> mainScoreboard?.getEntryTeam(player.name)?.name ?: "none"
            DetectionMethod.SCOREBOARD -> {
                val scoreboard = mainScoreboard ?: return "none"
                scoreboard.getEntryTeam(player.name)?.name ?: "none"
            }
        }
    }
}
