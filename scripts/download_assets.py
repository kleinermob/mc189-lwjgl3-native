#!/usr/bin/env python3
"""
Downloads Minecraft 1.8 assets from Mojang servers into test_run/assets/.
Requires Python 3.6+ (no external dependencies).

Usage:
    python scripts/download_assets.py
"""

import json
import os
import hashlib
import urllib.request
import sys

ASSET_INDEX_URL = "https://launchermeta.mojang.com/v1/packages/4e21a4d227289242902f7e3bfe6908ee04e476b3/1.8.json"
ASSET_BASE_URL = "https://resources.download.minecraft.com"
OUTPUT_DIR = os.path.join(os.path.dirname(os.path.dirname(os.path.abspath(__file__))), "test_run", "assets")


def sha1_file(path):
    h = hashlib.sha1()
    with open(path, "rb") as f:
        for chunk in iter(lambda: f.read(65536), b""):
            h.update(chunk)
    return h.hexdigest()


def download_file(url, dest):
    os.makedirs(os.path.dirname(dest), exist_ok=True)
    urllib.request.urlretrieve(url, dest)


def main():
    index_dir = os.path.join(OUTPUT_DIR, "indexes")
    objects_dir = os.path.join(OUTPUT_DIR, "objects")
    os.makedirs(index_dir, exist_ok=True)
    os.makedirs(objects_dir, exist_ok=True)

    # Download asset index
    index_path = os.path.join(index_dir, "1.8.json")
    if not os.path.exists(index_path):
        print(f"Downloading asset index -> {index_path}")
        download_file(ASSET_INDEX_URL, index_path)
    else:
        print(f"Asset index already exists: {index_path}")

    with open(index_path, "r") as f:
        index = json.load(f)

    objects = index.get("objects", {})
    total = len(objects)
    downloaded = 0
    skipped = 0

    print(f"Processing {total} assets...")

    for name, info in objects.items():
        sha = info["hash"]
        size = info["size"]
        prefix = sha[:2]
        dest = os.path.join(objects_dir, prefix, sha)

        if os.path.exists(dest) and os.path.getsize(dest) == size:
            skipped += 1
            continue

        url = f"{ASSET_BASE_URL}/{prefix}/{sha}"
        try:
            download_file(url, dest)
            downloaded += 1
            if downloaded % 50 == 0:
                print(f"  Downloaded {downloaded}/{total - skipped} new assets...")
        except Exception as e:
            print(f"  FAILED: {name} ({url}): {e}", file=sys.stderr)

    print(f"\nDone! Downloaded {downloaded} new, skipped {skipped} existing ({total} total).")


if __name__ == "__main__":
    main()
