import argparse
import json
import sys
import urllib.error
import urllib.parse
import urllib.request


API_BASE = "https://minecraft.curseforge.com/api/game"
FOCUS_TERMS = (
    "1.21.5",
    "Java 21",
    "Paper",
    "Spigot",
    "Bukkit",
    "java",
    "minecraft",
    "bukkit",
)


def fetch_json(path: str, token: str):
    url = f"{API_BASE}/{path}?token={urllib.parse.quote(token)}"
    req = urllib.request.Request(url, headers={"X-Api-Token": token})
    with urllib.request.urlopen(req, timeout=30) as resp:
        return json.loads(resp.read().decode("utf-8"))


def matches_focus(item) -> bool:
    text = " ".join(str(item.get(k, "")) for k in ("name", "slug", "gameVersionTypeID"))
    return any(term.lower() in text.lower() for term in FOCUS_TERMS)


def parse_args():
    parser = argparse.ArgumentParser()
    parser.add_argument("token", help="Legacy CurseForge token from curseforge.com/account/api-tokens")
    parser.add_argument("--proxy", help="Optional HTTP/HTTPS proxy URL, e.g. http://192.168.31.233:7890")
    return parser.parse_args()


def main():
    args = parse_args()
    token = args.token.strip()

    if args.proxy:
        proxy_handler = urllib.request.ProxyHandler(
            {
                "http": args.proxy,
                "https": args.proxy,
            }
        )
        opener = urllib.request.build_opener(proxy_handler)
        urllib.request.install_opener(opener)

    try:
        version_types = fetch_json("version-types", token)
        versions = fetch_json("versions", token)
    except urllib.error.HTTPError as exc:
        body = exc.read().decode("utf-8", errors="replace")
        print(f"HTTP {exc.code}: {exc.reason}")
        print(body)
        sys.exit(1)
    except Exception as exc:
        print(f"Request failed: {exc}")
        sys.exit(1)

    print("=== VERSION TYPES ===")
    for item in version_types:
        if matches_focus(item):
            print(
                json.dumps(
                    {
                        "id": item.get("id"),
                        "name": item.get("name"),
                        "slug": item.get("slug"),
                    },
                    ensure_ascii=False,
                )
            )

    print("\n=== VERSIONS ===")
    for item in versions:
        if matches_focus(item):
            print(
                json.dumps(
                    {
                        "id": item.get("id"),
                        "name": item.get("name"),
                        "slug": item.get("slug"),
                        "gameVersionTypeID": item.get("gameVersionTypeID"),
                    },
                    ensure_ascii=False,
                )
            )


if __name__ == "__main__":
    main()
