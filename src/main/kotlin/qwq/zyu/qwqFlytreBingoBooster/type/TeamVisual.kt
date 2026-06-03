package qwq.zyu.qwqFlytreBingoBooster.type

import org.bukkit.ChatColor

enum class TeamVisual(
    val scoreValue: Int,
    val teamKey: String,
    val shortName: String,
    val color: ChatColor,
    val chatPrefix: String,
    val sidebarLabel: String,
) {
    NONE(
        scoreValue = 0,
        teamKey = "none",
        shortName = "无",
        color = ChatColor.WHITE,
        chatPrefix = "",
        sidebarLabel = "【--None-无--】",
    ),
    RED(
        scoreValue = 1,
        teamKey = "red",
        shortName = "红",
        color = ChatColor.RED,
        chatPrefix = "【--Red-红--】 ",
        sidebarLabel = "【--Red-红--】",
    ),
    YELLOW(
        scoreValue = 2,
        teamKey = "yellow",
        shortName = "黄",
        color = ChatColor.YELLOW,
        chatPrefix = "【--Yellow-黄--】 ",
        sidebarLabel = "【--Yellow-黄--】",
    ),
    GREEN(
        scoreValue = 3,
        teamKey = "green",
        shortName = "绿",
        color = ChatColor.GREEN,
        chatPrefix = "【--Green-绿--】 ",
        sidebarLabel = "【--Green-绿--】",
    ),
    BLUE(
        scoreValue = 4,
        teamKey = "blue",
        shortName = "蓝",
        // ChatColor.BLUE,
        color = ChatColor.AQUA,
        // "【--Blue-蓝--】 ",
        chatPrefix = "【--Blue-蓝--】 ",
        sidebarLabel = "【--Blue-蓝--】",
    );

    companion object {
        fun fromScoreValue(scoreValue: Int): TeamVisual {
            return entries.firstOrNull { it.scoreValue == scoreValue } ?: NONE
        }

        fun fromTeamKey(teamKey: String): TeamVisual {
            return entries.firstOrNull { it.teamKey.equals(teamKey, ignoreCase = true) } ?: NONE
        }
    }
}
