package qwq.zyu.qwqFlytreBingoBooster.teamcolor

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.scheduler.BukkitRunnable
import qwq.zyu.qwqFlytreBingoBooster.config.PluginLogger
import qwq.zyu.qwqFlytreBingoBooster.config.TeamDetector
import qwq.zyu.qwqFlytreBingoBooster.type.TeamVisual

class TeamColorTask(
    private val teamDetector: TeamDetector
) : BukkitRunnable() {
    @Volatile
    var enabled = false

    override fun run() {
        if (!enabled) return

        for (player in Bukkit.getOnlinePlayers()) {
            val teamVisual = TeamVisual.fromScoreValue(teamDetector.getTeamValue(player))

            PluginLogger.debug("TeamColor: ${player.name} 队伍值 = ${teamVisual.scoreValue}")
            player.setDisplayName("${teamVisual.color}${teamVisual.chatPrefix}${player.name}${ChatColor.RESET}")
            player.setPlayerListName("${teamVisual.color}${teamVisual.chatPrefix}${player.name}${ChatColor.RESET}")
        }
    }
}
