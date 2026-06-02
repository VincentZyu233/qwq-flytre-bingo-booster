package qwq.zyu.qwqFlytreBingoBooster.teamcolor

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.scheduler.BukkitRunnable
import qwq.zyu.qwqFlytreBingoBooster.config.PluginLogger

class TeamColorTask : BukkitRunnable() {
    @Volatile
    var enabled = false

    override fun run() {
        if (!enabled) return

        val scoreboard = Bukkit.getScoreboardManager()?.mainScoreboard
        if (scoreboard == null) {
            PluginLogger.warn("TeamColor: 无法获取主计分板")
            return
        }
        val objective = scoreboard.getObjective("teamDisplay")
        if (objective == null) {
            PluginLogger.warn("TeamColor: 主计分板未找到 objective 'teamDisplay'")
            return
        }

        for (player in Bukkit.getOnlinePlayers()) {
            val score = objective.getScore(player.name)
            val value = if (score.isScoreSet) score.score else 0

            PluginLogger.debug("TeamColor: ${player.name} 队伍分数 = $value")

            val (color, prefix) = when (value) {
                1 -> ChatColor.RED to "[红] "
                2 -> ChatColor.YELLOW to "[黄] "
                3 -> ChatColor.GREEN to "[绿] "
                4 -> ChatColor.BLUE to "[蓝] "
                else -> ChatColor.WHITE to ""
            }

            player.setDisplayName("$color$prefix${player.name}${ChatColor.RESET}")
            player.setPlayerListName("$color$prefix${player.name}${ChatColor.RESET}")
        }
    }
}
