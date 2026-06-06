![qwq-flytre-bingo-booster](https://socialify.git.ci/VincentZyu233/qwq-flytre-bingo-booster/image?custom_description=%F0%9F%8E%AF%F0%9F%91%A5%F0%9F%8E%A8%F0%9F%93%8A%E2%9A%A1+Flytre+Bingo+booster%2C+server+side+plugin%EF%BC%9A+team+name+dyeing+%2B+scoreboard+sidebar&description=1&font=JetBrains+Mono&forks=1&issues=1&language=1&logo=https%3A%2F%2Fassets.streamlinehq.com%2Fimage%2Fprivate%2Fw_300%2Ch_300%2Car_1%2Ff_auto%2Fv1%2Ficons%2Flogos%2Fspigotmc-6n76dhb21bm15t2i8wr3rei.png%2Fspigotmc-feqcixjzc5qm0dm8in5erj.png%3F_a%3DDATAiZAAZAA0&name=1&owner=1&pulls=1&stargazers=1&theme=Light)

> **[📖 English](README.md)**
> **[📖 中文](README-zh.cn.md)**

# 🎯👥🎨📊⚡ qwq-flytre-bingo-booster

> 🧩 专为 [Flytre Bingo](https://www.flytre.net/bingo) 地图打造的 Spigot 辅助插件：队伍染色 + 侧边栏显示

[![GitHub](https://img.shields.io/badge/GitHub-181717?style=for-the-badge&logo=github&logoColor=white)](https://github.com/VincentZyu233/qwq-flytre-bingo-booster)
[![Gitee](https://img.shields.io/badge/Gitee-C71D23?style=for-the-badge&logo=gitee&logoColor=white)](https://gitee.com/vincent-zyu/qwq-flytre-bingo-booster)

[![Paper](./doc/images/badge/paper.svg)](https://papermc.io)
[![Spigot](./doc/images/badge/spigot.svg)](https://www.spigotmc.org/)

[![Kotlin](https://img.shields.io/badge/Kotlin-2.1.10-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)](https://kotlinlang.org)
[![Gradle](https://img.shields.io/badge/Gradle-8.8-02303A?style=for-the-badge&logo=gradle&logoColor=white)](https://gradle.org)

[![QQ群](https://img.shields.io/badge/QQ群-1085190201-12B7F5?style=flat-square&logo=qq&logoColor=white)](https://qm.qq.com/q/4vjto4V7Di)

<p><del>💬 插件使用问题 / 🐛 Bug反馈 / 👨‍💻 插件开发交流，欢迎加入QQ群：<b>259248174</b>   🎉（这个群G了</del> </p>
<p>💬 插件使用问题 / 🐛 Bug反馈 / 👨‍💻 插件开发交流，欢迎加入QQ群：<b>1085190201</b> 🎉</p>
<p>💡 在群里直接艾特我，回复的更快哦~ ✨</p>

---

## 🌟 功能特性

> 💾 上面这三个指令的效果都可以持久化到配置文件。

| 功能 | 命令 | 说明 |
|------|------|------|
| 🎨 **队伍染色** | `/qwq_bingo_team_color_dye <true/false>` | 根据 `team_detection` 配置读取原生 Team 或计分板分数，为玩家名添加对应队伍颜色与前缀（🔴红/🟡黄/🟢绿/🔵蓝） |
| 📊 **侧边栏** | `/qwq_bingo_sidebar <true/false>` | 在屏幕右侧显示各队伍成员列表，按配置 tick 间隔自动刷新 |
| 💊 **常驻效果** | `/qwq_bingo_effect <true/false> <effect> <amplifier> <true/false>` | 给所有在线玩家持续补药水效果 |

> 💡 本插件专为 [Flytre Bingo地图](https://www.flytre.net/bingo) 设计，这里有[Flytre Bingo地图的下载链接](https://www.flytre.net/bingo)。 
> 本插件可按配置读取 Minecraft 原生 Team 或主计分板 objective 来判断队伍，并与地图原生datapack联动。

### 🖼️ 效果预览

![Preview: sidebar and chat color in Minecraft client](./doc/images/preview/preview-sidebar-chatcolor-minecraft-client-pov.png)

### 🗺️ 版本支持

> 截止2026年6月3日，目前本插件支持的情况如下:

| | |
|---|---|
| 🎯 **适配地图** | [![Flytre Bingo](./doc/images/badge/minecraft.svg)](https://www.flytre.net/bingo) |
| 🌎 **地图支持版本** | 1.16.x · 1.17.x · 1.18.x · 1.19.x · 1.20.2-4 · **1.21.5** · **1.21.10** |
| 📦 **插件支持版本** | [![Paper](./doc/images/badge/paper.svg)](https://fill-ui.papermc.io/projects/paper/family/1.21) [![Spigot](./doc/images/badge/spigot.svg)](https://getbukkit.org/download/spigot) |

---

## 🛠 技术栈

| | |
|---|---|
| 🧱 **服务端** | [![Spigot API](https://img.shields.io/badge/Spigot_API_1.21.5-ED8106?style=for-the-badge&logo=spigotmc&logoColor=white)](https://getbukkit.org/download/spigot) |
| 📝 **语言** | [![Kotlin](https://img.shields.io/badge/Kotlin-2.1.10-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)](https://kotlinlang.org) |
| 🏗 **构建** | [![Gradle](https://img.shields.io/badge/Gradle-8.8-02303A?style=for-the-badge&logo=gradle&logoColor=white)](https://gradle.org) |

---

## 📦 下载与安装

[![Download](https://img.shields.io/badge/Download-GitHub_Releases-ED8106?style=for-the-badge&logo=spigotmc&logoColor=white)](https://github.com/VincentZyu233/qwq-flytre-bingo-booster/releases)

将 `.jar` 文件放入服务器的 `plugins/` 目录后重启即可。

默认配置如下：

```yml
# 🎯 qwq-flytre-bingo-booster 配置

# 📢 日志级别 (Log Level): debug, info, warn, error, silent
# 🐛 debug  - 输出所有日志（调试用 🔧）
# ℹ️  info   - 输出 info 及以上（默认 ✅）
# ⚠️  warn   - 只输出警告和错误 🟡
# ❌ error  - 只输出错误 🔴
# 🔇 silent - 关闭所有日志 🤫
log_level: info

# 🧾 命令名配置
# `team_color_dye` - 开关队伍染色，为玩家名添加对应队伍颜色与前缀
# `bingo_sidebar`  - 开关右侧队伍成员侧边栏显示
# `bingo_effect`   - 设置或关闭常驻药水效果
commands:
  team_color_dye: qwq_bingo_team_color_dye
  bingo_sidebar: qwq_bingo_sidebar
  bingo_effect: qwq_bingo_effect

# 🚀 功能默认启用配置
features:
  team_color_dye:
    enabled_on_load: true
    refresh_interval_ticks: 10
  bingo_sidebar:
    enabled_on_load: true
    refresh_interval_ticks: 10
  bingo_effect:
    enabled_on_load: true
    refresh_interval_ticks: 10
    apply_duration_ticks: 114514

# 💊 Bingo 常驻药水效果列表
# 每个对象表示一条受插件管理的常驻药水效果配置
# `enabled`         - 是否启用该效果
# `type`            - 药水效果 ID，例如 minecraft:night_vision
# `amplifier`       - Bukkit 内部等级值，0=1级，1=2级，2=3级，以此类推
# `hide_particles`  - 是否隐藏粒子效果
bingo_effects:
  # 默认效果：急迫 4，隐藏粒子，默认启用
  - enabled: true
    type: minecraft:haste
    amplifier: 3
    hide_particles: true
  # 默认效果：迅捷 3，隐藏粒子，默认启用
  - enabled: true
    type: minecraft:speed
    amplifier: 2
    hide_particles: true
  # 默认效果：夜视 2，隐藏粒子，默认启用
  - enabled: true
    type: minecraft:night_vision
    amplifier: 1
    hide_particles: true

# 👥 队伍检测配置
team_detection:
  # 🔍 检测方式: team 或 scoreboard
  # team       - 优先读玩家当前 scoreboard 上的原生 team，读不到时回退主 scoreboard（推荐 ✅）
  # scoreboard - 读主计分板某个 objective 的分数值
  method: team
  # 📊 如果用 scoreboard 方式，指定主计分板 objective 名
  scoreboard_name: teamScore
```

`commands` 支持自定义指令名：

- `team_color_dye`: 队伍染色开关命令名
- `bingo_sidebar`: bingo 侧边栏开关命令名
- `bingo_effect`: bingo 常驻药水命令名

`features.*.enabled_on_load` 控制插件加载后是否默认自动启用功能，`refresh_interval_ticks` 控制对应定时任务的 tick 间隔：

- `true`: 开服后自动开启
- `false`: 保持旧行为，等手动输入指令后再开启

`team_detection.method` 支持两种模式：

- `team`: 优先读取玩家当前 scoreboard 上的原生 Team，读不到时回退到主 scoreboard
- `scoreboard`: 读取主 scoreboard 上指定 objective 的分数值，默认使用 `teamScore`

---

## 🔧 构建

### 本地构建

```bash
./gradlew build
```

产物在 `build/libs/` 目录下（`*-all.jar` 为完整的 fat jar）。

### GitHub Actions 自动构建

[![CI Status](https://img.shields.io/github/actions/workflow/status/VincentZyu233/qwq-flytre-bingo-booster/build.yml?branch=for-spigot-1.21.5&style=for-the-badge&logo=githubactions&logoColor=white)](https://github.com/VincentZyu233/qwq-flytre-bingo-booster/actions)


push 到 `main` 或 `for-*` 分支时，commit message 包含特定关键字可触发 CI：

| 关键字 | 行为 |
|--------|------|
| `build action` | 🏗 自动构建并上传 artifact |
| `build release` | 🏗 自动构建 + 🚀 创建 GitHub Release |

示例：

```bash
git commit -m "aaa: some commit messages...; build action"
git commit -m "bbb: yet other commit messages...; build release"
```

PR 到 `main` 或 `for-*` 分支时也会触发构建（但不发布）。

---
