package qwq.zyu.qwqFlytreBingoBooster

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import qwq.zyu.qwqFlytreBingoBooster.bingo_effect.BingoEffectTask
import qwq.zyu.qwqFlytreBingoBooster.bingo_sidebar.BingoSidebarCommand
import qwq.zyu.qwqFlytreBingoBooster.bingo_teamcolor.BingoTeamColorTask

class BingoStatusBroadcastListener(
    private val plugin: QwqFlytreBingoBooster,
    private val teamColorTask: BingoTeamColorTask,
    private val bingoSidebarCommand: BingoSidebarCommand,
    private val bingoEffectTask: BingoEffectTask
) : Listener {

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val playerName = event.player.name
        val teamColorConfigured = plugin.config.getBoolean("features.team_color_dye.enabled_on_load", true)
        val teamColorInterval = plugin.config.getLong("features.team_color_dye.refresh_interval_ticks", 10L)
        val sidebarConfigured = plugin.config.getBoolean("features.bingo_sidebar.enabled_on_load", true)
        val sidebarInterval = plugin.config.getLong("features.bingo_sidebar.refresh_interval_ticks", 10L)
        val effectConfigured = plugin.config.getBoolean("features.bingo_effect.enabled_on_load", true)
        val effectInterval = plugin.config.getLong("features.bingo_effect.refresh_interval_ticks", 10L)
        val effectDuration = plugin.config.getLong("features.bingo_effect.apply_duration_ticks", 30L)
        val effectEntries = bingoEffectTask.getEntriesSnapshot()
        val activeEffects = effectEntries.count { it.enabled }

        val lines = listOf(
            "${ChatColor.GOLD}【qwq】${ChatColor.YELLOW}$playerName 进入服务器，当前功能状态如下：",
            "${ChatColor.AQUA}队伍染色${ChatColor.GRAY} 配置默认=$teamColorConfigured, 当前生效=${teamColorTask.enabled}, 间隔=${teamColorInterval}tick",
            "${ChatColor.AQUA}侧边栏${ChatColor.GRAY} 配置默认=$sidebarConfigured, 当前生效=${bingoSidebarCommand.isEnabled()}, 间隔=${sidebarInterval}tick",
            "${ChatColor.AQUA}常驻效果${ChatColor.GRAY} 配置默认=$effectConfigured, 当前生效=${bingoEffectTask.enabled}, 间隔=${effectInterval}tick, 持续=${effectDuration}tick, 已启用效果=${activeEffects}/${effectEntries.size}"
        )

        lines.forEach(Bukkit::broadcastMessage)
    }
}
