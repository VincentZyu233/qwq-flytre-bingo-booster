package qwq.zyu.qwqFlytreBingoBooster.sidebar

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.plugin.Plugin
import qwq.zyu.qwqFlytreBingoBooster.bingo_config.BingoPluginLogger
import qwq.zyu.qwqFlytreBingoBooster.bingo_config.BingoTeamDetector

class BingoSidebarCommand(
    private val plugin: Plugin,
    private val teamDetector: BingoTeamDetector,
    private val commandName: String,
    private val refreshIntervalTicks: Long
) : CommandExecutor {
    private var task: BingoSidebarTask? = null

    fun isEnabled(): Boolean = task != null

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (args.size != 1) {
            sender.sendMessage("用法: /$commandName <enable? true/false>")
            return false
        }

        return when (args[0].lowercase()) {
            "true" -> enable(sender)
            "false" -> disable(sender)
            else -> {
                sender.sendMessage("用法: /$commandName <enable? true/false>")
                false
            }
        }
    }

    fun enable(sender: CommandSender? = null): Boolean {
        if (task != null) {
            sender?.sendMessage("sidebar的更新任务已经在运行。")
            return false
        }
        task = BingoSidebarTask(teamDetector).apply {
            runTaskTimer(plugin, 0L, refreshIntervalTicks)
        }
        BingoPluginLogger.info("BingoSidebar: 更新任务已开启")
        sender?.sendMessage("已经开启sidebar的更新任务")
        return true
    }

    fun disable(sender: CommandSender? = null): Boolean {
        if (task == null) {
            sender?.sendMessage("没有sidebar更新任务！停什喵！")
            return false
        }
        task!!.cancel()
        task = null
        BingoPluginLogger.info("BingoSidebar: 更新任务已停止")
        sender?.sendMessage("已经停止sidebar更新")
        return true
    }
}
