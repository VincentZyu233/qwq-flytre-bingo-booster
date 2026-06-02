package qwq.zyu.qwqFlytreBingoBooster

import org.bukkit.plugin.java.JavaPlugin
import qwq.zyu.qwqFlytreBingoBooster.bingosidebar.BingoSidebarCommand
import qwq.zyu.qwqFlytreBingoBooster.teamcolor.TeamColorCommand
import qwq.zyu.qwqFlytreBingoBooster.teamcolor.TeamColorTask

class QwqFlytreBingoBooster : JavaPlugin() {

    private lateinit var teamColorTask: TeamColorTask

    override fun onEnable() {
        teamColorTask = TeamColorTask()
        teamColorTask.runTaskTimer(this, 0L, 1L)

        getCommand("qwq_set_scheduled_team_color_dye")?.setExecutor(TeamColorCommand(teamColorTask))
        getCommand("qwq_bingo_sidebar")?.setExecutor(BingoSidebarCommand(this))
    }

    override fun onDisable() {
        if (::teamColorTask.isInitialized) {
            teamColorTask.cancel()
        }
    }
}
