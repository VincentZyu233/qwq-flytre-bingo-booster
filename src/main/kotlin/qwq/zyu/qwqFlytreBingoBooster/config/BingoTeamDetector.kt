package qwq.zyu.qwqFlytreBingoBooster.bingo_config

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import qwq.zyu.qwqFlytreBingoBooster.bingo_type.BingoTeamVisual

enum class BingoTeamDetectionMethod { TEAM, SCOREBOARD }

class BingoTeamDetector(
    val method: BingoTeamDetectionMethod,
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
            BingoTeamDetectionMethod.TEAM -> {
                val teamName = resolveTeamName(player)
                BingoTeamVisual.fromTeamKey(teamName).scoreValue
            }
            BingoTeamDetectionMethod.SCOREBOARD -> {
                val scoreboard = mainScoreboard ?: return 0
                val objective = scoreboard.getObjective(scoreboardName) ?: return 0
                val score = objective.getScore(player.name)
                if (score.isScoreSet) score.score else 0
            }
        }
    }

    fun getTeamName(player: Player): String {
        return when (method) {
            BingoTeamDetectionMethod.TEAM -> resolveTeamName(player).ifBlank { "none" }
            BingoTeamDetectionMethod.SCOREBOARD -> {
                val scoreboard = mainScoreboard ?: return "none"
                scoreboard.getEntryTeam(player.name)?.name ?: "none"
            }
        }
    }
}
