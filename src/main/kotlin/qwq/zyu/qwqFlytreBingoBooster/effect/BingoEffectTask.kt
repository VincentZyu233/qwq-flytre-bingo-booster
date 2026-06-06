package qwq.zyu.qwqFlytreBingoBooster.bingo_effect

import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.Registry
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable
import qwq.zyu.qwqFlytreBingoBooster.bingo_config.BingoPluginLogger

class BingoEffectTask(
    private val applyDurationTicksProvider: () -> Int
) : BukkitRunnable() {
    @Volatile
    var enabled = false

    @Volatile
    private var entries: List<BingoEffectEntry> = emptyList()

    fun updateEntries(newEntries: List<BingoEffectEntry>) {
        entries = newEntries
    }

    fun getEntriesSnapshot(): List<BingoEffectEntry> = entries

    fun removeManagedEffectsFromOnlinePlayers() {
        Bukkit.getOnlinePlayers().forEach { removeManagedEffects(it) }
    }

    fun removeManagedEffects(player: Player) {
        entries.mapNotNull { resolveType(it.type) }.forEach(player::removePotionEffect)
    }

    override fun run() {
        if (!enabled) return

        val duration = applyDurationTicksProvider().coerceAtLeast(1)
        val currentEntries = entries
        for (player in Bukkit.getOnlinePlayers()) {
            for (entry in currentEntries) {
                if (!entry.enabled) continue
                val type = resolveType(entry.type) ?: continue
                player.addPotionEffect(
                    PotionEffect(type, duration, entry.amplifier.coerceAtLeast(0), false, !entry.hideParticles, true)
                )
            }
        }
    }

    private fun resolveType(typeName: String): PotionEffectType? {
        val key = NamespacedKey.fromString(typeName)
        if (key == null) {
            BingoPluginLogger.warn("无效药水效果ID: $typeName")
            return null
        }
        val type = Registry.EFFECT.get(key)
        if (type == null) {
            BingoPluginLogger.warn("未找到药水效果: $typeName")
        }
        return type
    }
}
