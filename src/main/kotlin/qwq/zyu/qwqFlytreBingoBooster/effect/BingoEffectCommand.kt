package qwq.zyu.qwqFlytreBingoBooster.effect

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.NamespacedKey
import org.bukkit.Registry
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.plugin.java.JavaPlugin

class BingoEffectCommand(
    private val plugin: JavaPlugin,
    private val task: BingoEffectTask,
    private val commandName: String
) : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (args.size != 4) {
            sender.sendMessage("${ChatColor.RED}【qwq】用法: /$commandName <true/false> <effect> <amplifier> <true/false>")
            return false
        }

        val enabled = parseBoolean(args[0]) ?: run {
            sender.sendMessage("${ChatColor.RED}【qwq】第1个参数必须是 true/false")
            return false
        }
        val effectType = args[1].trim()
        if (!isValidEffect(effectType)) {
            sender.sendMessage("${ChatColor.RED}【qwq】无效药水效果: $effectType")
            return false
        }

        val amplifier = args[2].toIntOrNull()
        if (amplifier == null || amplifier < 0) {
            sender.sendMessage("${ChatColor.RED}【qwq】amplifier 必须是大于等于 0 的整数")
            return false
        }

        val hideParticles = parseBoolean(args[3]) ?: run {
            sender.sendMessage("${ChatColor.RED}【qwq】第4个参数必须是 true/false")
            return false
        }

        saveOrUpdateEntry(effectType, enabled, amplifier, hideParticles)
        task.updateEntries(loadEntries(plugin.config))

        if (!enabled) {
            val key = NamespacedKey.fromString(effectType)
            val type = key?.let { Registry.EFFECT.get(it) }
            if (type != null) {
                Bukkit.getOnlinePlayers().forEach { it.removePotionEffect(type) }
            }
        }

        sender.sendMessage(
            "${ChatColor.GREEN}【qwq】已更新 bingo effect: enabled=$enabled, type=$effectType, amplifier=$amplifier, hide_particles=$hideParticles"
        )
        return true
    }

    private fun saveOrUpdateEntry(type: String, enabled: Boolean, amplifier: Int, hideParticles: Boolean) {
        val entries = loadEntries(plugin.config).toMutableList()
        val index = entries.indexOfFirst { it.type.equals(type, ignoreCase = true) }
        val updated = BingoEffectEntry(enabled, type, amplifier, hideParticles)
        if (index >= 0) {
            entries[index] = updated
        } else {
            entries.add(updated)
        }

        plugin.config.set(
            "bingo_effects",
            entries.map {
                mapOf(
                    "enabled" to it.enabled,
                    "type" to it.type,
                    "amplifier" to it.amplifier,
                    "hide_particles" to it.hideParticles
                )
            }
        )
        plugin.saveConfig()
    }

    private fun parseBoolean(value: String): Boolean? {
        return when (value.lowercase()) {
            "true" -> true
            "false" -> false
            else -> null
        }
    }

    private fun isValidEffect(type: String): Boolean {
        val key = NamespacedKey.fromString(type) ?: return false
        return Registry.EFFECT.get(key) != null
    }

    private fun loadEntries(config: FileConfiguration): List<BingoEffectEntry> {
        return config.getMapList("bingo_effects").mapNotNull { raw ->
            val type = raw["type"]?.toString()?.trim().orEmpty()
            if (type.isEmpty()) return@mapNotNull null
            BingoEffectEntry(
                enabled = raw["enabled"] as? Boolean ?: true,
                type = type,
                amplifier = ((raw["amplifier"] as? Number)?.toInt() ?: 0).coerceAtLeast(0),
                hideParticles = raw["hide_particles"] as? Boolean ?: true
            )
        }
    }
}
