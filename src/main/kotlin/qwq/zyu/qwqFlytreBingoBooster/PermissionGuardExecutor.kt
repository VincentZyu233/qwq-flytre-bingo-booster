package qwq.zyu.qwqFlytreBingoBooster

import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender

class PermissionGuardExecutor(
    private val inner: CommandExecutor,
    private val mode: String
) : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        return when (mode) {
            "everyone" -> inner.onCommand(sender, command, label, args)
            "console" -> {
                if (sender is ConsoleCommandSender) {
                    inner.onCommand(sender, command, label, args)
                } else {
                    sender.sendMessage("${ChatColor.RED}【qwq】仅支持控制台执行此命令")
                    true
                }
            }
            "op" -> {
                if (sender is ConsoleCommandSender || sender.isOp()) {
                    inner.onCommand(sender, command, label, args)
                } else {
                    sender.sendMessage("${ChatColor.RED}【qwq】你没有权限执行此命令")
                    true
                }
            }
            else -> {
                if (sender.hasPermission(mode)) {
                    inner.onCommand(sender, command, label, args)
                } else {
                    sender.sendMessage("${ChatColor.RED}【qwq】你没有权限执行此命令（需要权限节点: $mode）")
                    true
                }
            }
        }
    }
}
