import sys
from pathlib import Path

PROJECT_ROOT = Path(__file__).resolve().parent
README = PROJECT_ROOT / "README.md"
CONFIG_YML = PROJECT_ROOT / "src" / "main" / "resources" / "config.yml"

CONFIG_MARKER = "默认配置如下："


def main():
    if not CONFIG_YML.exists():
        print(f"❌ 找不到 {CONFIG_YML}", file=sys.stderr)
        sys.exit(1)
    if not README.exists():
        print(f"❌ 找不到 {README}", file=sys.stderr)
        sys.exit(1)

    config_text = CONFIG_YML.read_text(encoding="utf-8").strip()
    readme_text = README.read_text(encoding="utf-8")

    marker_idx = readme_text.index(CONFIG_MARKER)
    after_marker = readme_text[marker_idx:]

    fence_start = after_marker.index("```yml")
    fence_end = after_marker.index("```", fence_start + 6)

    old_block = after_marker[fence_start : fence_end + 3]
    new_block = f"```yml\n{config_text}\n```"

    new_readme = (
        readme_text[: marker_idx + fence_start]
        + new_block
        + readme_text[marker_idx + fence_end + 3 :]
    )

    README.write_text(new_readme, encoding="utf-8")
    print(f"✅ README.md 已同步 {CONFIG_YML.relative_to(PROJECT_ROOT)}")


if __name__ == "__main__":
    main()
