# qwq-flytre-bingo-booster

为 Flytre Bingo 地图提供队伍染色与侧边栏显示的 Spigot 辅助插件。

## 命令

| 命令 | 说明 |
|------|------|
| `/qwq_set_scheduled_team_color_dye <true/false>` | 根据计分板值给玩家名染色（红/黄/绿/蓝） |
| `/qwq_bingo_sidebar <true/false>` | 显示或隐藏队伍侧边栏 |

## 构建

### 本地构建

```bash
./gradlew build
```

产物在 `build/libs/` 目录下。

### GitHub Actions

push 到 `main` 分支时，commit message 包含特定关键字可触发 CI：

| 关键字 | 行为 |
|--------|------|
| `build action` | 自动构建并上传 artifact |
| `build release` | 自动构建 + 创建 GitHub Release |

示例：

```
git commit -m "fix color bug; build action"
git commit -m "release v1.0; build release"
```

PR 到 `main` 时也会触发构建（但不发布）。

## 依赖

- Paper / Spigot 1.21.x
- Kotlin 运行时（已 shade 进 jar）
