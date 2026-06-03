![qwq-flytre-bingo-booster](https://socialify.git.ci/VincentZyu233/qwq-flytre-bingo-booster/image?custom_description=%F0%9F%8E%AF%F0%9F%91%A5%F0%9F%8E%A8%F0%9F%93%8A%E2%9A%A1+Flytre+Bingo+booster%2C+server+side+plugin%EF%BC%9A+team+name+dyeing+%2B+scoreboard+sidebar&description=1&font=JetBrains+Mono&forks=1&issues=1&language=1&logo=https%3A%2F%2Fassets.streamlinehq.com%2Fimage%2Fprivate%2Fw_300%2Ch_300%2Car_1%2Ff_auto%2Fv1%2Ficons%2Flogos%2Fspigotmc-6n76dhb21bm15t2i8wr3rei.png%2Fspigotmc-feqcixjzc5qm0dm8in5erj.png%3F_a%3DDATAiZAAZAA0&name=1&owner=1&pulls=1&stargazers=1&theme=Light)

# 🎯👥🎨📊⚡ qwq-flytre-bingo-booster

> 🧩 专为 [Flytre Bingo](https://www.flytre.net/bingo) 地图打造的 Spigot 辅助插件：队伍染色 + 侧边栏显示

[![GitHub](https://img.shields.io/badge/GitHub-181717?style=for-the-badge&logo=github&logoColor=white)](https://github.com/VincentZyu233/qwq-flytre-bingo-booster)
[![Gitee](https://img.shields.io/badge/Gitee-C71D23?style=for-the-badge&logo=gitee&logoColor=white)](https://gitee.com/vincent-zyu/qwq-flytre-bingo-booster)

[![Paper](https://img.shields.io/badge/Paper-1.21.5-1F93FF?style=for-the-badge&logoColor=white)](https://papermc.io)
[![Spigot](https://img.shields.io/badge/Spigot-1.21.5-ED8106?style=for-the-badge&logo=spigotmc&logoColor=white)](https://www.spigotmc.org/)

[![Kotlin](https://img.shields.io/badge/Kotlin-2.1.10-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)](https://kotlinlang.org)
[![Gradle](https://img.shields.io/badge/Gradle-8.8-02303A?style=for-the-badge&logo=gradle&logoColor=white)](https://gradle.org)

[![QQ群](https://img.shields.io/badge/QQ群-1085190201-12B7F5?style=flat-square&logo=qq&logoColor=white)](https://qm.qq.com/q/4vjto4V7Di)

---

## 🌟 功能特性

| 功能 | 命令 | 说明 |
|------|------|------|
| 🎨 **队伍染色** | `/qwq_set_scheduled_team_color_dye <true/false>` | 根据 `team_detection` 配置读取原生 Team 或计分板分数，为玩家名添加对应队伍颜色与前缀（🔴红/🟡黄/🟢绿/🔵蓝） |
| 📊 **侧边栏** | `/qwq_bingo_sidebar <true/false>` | 在屏幕右侧显示各队伍成员列表，每 0.5 秒自动刷新 |

> 💡 本插件专为 [Flytre Bingo](https://www.flytre.net/bingo) 地图设计，可按配置读取 Minecraft 原生 Team 或主计分板 objective 来判断队伍，并与地图原生数据联动。

---

## 🛠 技术栈

| | |
|---|---|
| 🧱 **服务端** | [![Paper](https://img.shields.io/badge/Paper-1.21.5-1F93FF?style=for-the-badge&logoColor=white)](https://fill-ui.papermc.io/projects/paper/family/1.21) [![Spigot](https://img.shields.io/badge/Spigot-1.21.5-ED8106?style=for-the-badge&logo=spigotmc&logoColor=white)](https://getbukkit.org/download/spigot) |
| 📝 **语言** | [![Kotlin](https://img.shields.io/badge/Kotlin-2.1.10-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)](https://kotlinlang.org) |
| 🏗 **构建** | [![Gradle](https://img.shields.io/badge/Gradle-8.8-02303A?style=for-the-badge&logo=gradle&logoColor=white)](https://gradle.org) |

---

## 📦 下载与安装

[![Download](https://img.shields.io/badge/Download-GitHub_Releases-ED8106?style=for-the-badge&logo=spigotmc&logoColor=white)](https://github.com/VincentZyu233/qwq-flytre-bingo-booster/releases)

将 `.jar` 文件放入服务器的 `plugins/` 目录后重启即可。

默认配置如下：

```yml
log_level: info

team_detection:
  method: team
  scoreboard_name: teamScore
```

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

## 💬 交流反馈

<p>💬 插件使用问题 / 🐛 Bug反馈 / 👨‍💻 插件开发交流，欢迎加入QQ群：<b>1085190201</b> 🎉</p>
<p>💡 在群里直接艾特我，回复的更快哦 ~ ✨</p>
