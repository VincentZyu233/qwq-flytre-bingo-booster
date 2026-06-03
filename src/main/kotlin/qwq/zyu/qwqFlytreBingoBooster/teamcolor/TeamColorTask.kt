package qwq.zyu.qwqFlytreBingoBooster.teamcolor

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.scheduler.BukkitRunnable
import qwq.zyu.qwqFlytreBingoBooster.config.PluginLogger
import qwq.zyu.qwqFlytreBingoBooster.config.TeamDetector

class TeamColorTask(
    private val teamDetector: TeamDetector
) : BukkitRunnable() {
    @Volatile
    var enabled = false

    override fun run() {
        if (!enabled) return

        for (player in Bukkit.getOnlinePlayers()) {
            val value = teamDetector.getTeamValue(player)

            PluginLogger.debug("TeamColor: ${player.name} 队伍值 = $value")

            val (color, prefix) = when (value) {
                1 -> ChatColor.RED to       "【--Red-红--】 "
                2 -> ChatColor.YELLOW to    "【--Yellow-黄--】 "
                3 -> ChatColor.GREEN to     "【--Green-绿--】 "
                // 4 -> ChatColor.BLUE to      "【--Blue-蓝--】 "
                4 -> ChatColor.AQUA to      "【--Blue-蓝--】 "
                else -> ChatColor.WHITE to ""
            }

            player.setDisplayName("$color$prefix${player.name}${ChatColor.RESET}")
            player.setPlayerListName("$color$prefix${player.name}${ChatColor.RESET}")
        }
    }
}
