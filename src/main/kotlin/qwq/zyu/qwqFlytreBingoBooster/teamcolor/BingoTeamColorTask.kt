package qwq.zyu.qwqFlytreBingoBooster.bingo_teamcolor

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.scheduler.BukkitRunnable
import qwq.zyu.qwqFlytreBingoBooster.bingo_config.BingoPluginLogger
import qwq.zyu.qwqFlytreBingoBooster.bingo_config.BingoTeamDetector
import qwq.zyu.qwqFlytreBingoBooster.bingo_type.BingoTeamVisual

class BingoTeamColorTask(
    private val teamDetector: BingoTeamDetector
) : BukkitRunnable() {
    @Volatile
    var enabled = false

    override fun run() {
        if (!enabled) return

        for (player in Bukkit.getOnlinePlayers()) {
            val teamVisual = BingoTeamVisual.fromScoreValue(teamDetector.getTeamValue(player))

            BingoPluginLogger.debug("TeamColor: ${player.name} 队伍值 = ${teamVisual.scoreValue}")
            player.setDisplayName("${teamVisual.color}${teamVisual.chatPrefix}${player.name}${ChatColor.RESET}")
            player.setPlayerListName("${teamVisual.color}${teamVisual.chatPrefix}${player.name}${ChatColor.RESET}")
        }
    }
}
