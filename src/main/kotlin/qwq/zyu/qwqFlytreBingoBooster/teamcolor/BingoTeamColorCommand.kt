package qwq.zyu.qwqFlytreBingoBooster.bingo_teamcolor

import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class BingoTeamColorCommand(
    private val task: BingoTeamColorTask,
    private val commandName: String
) : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (args.size != 1) {
            sender.sendMessage("${ChatColor.RED}【qwq】用法: /$commandName <true/false>")
            return false
        }

        return when (args[0].lowercase()) {
            "true" -> {
                task.enabled = true
                sender.sendMessage("${ChatColor.GREEN}【qwq】Team color dyeing enabled.")
                true
            }
            "false" -> {
                task.enabled = false
                sender.sendMessage("${ChatColor.RED}【qwq】Team color dyeing disabled.")
                true
            }
            else -> {
                sender.sendMessage("${ChatColor.RED}【qwq】非法参数！只有 true/false 可用.")
                false
            }
        }
    }
}
