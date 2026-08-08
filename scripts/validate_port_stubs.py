#!/usr/bin/env python3
"""Inventory of code paths that are disabled by a constant.

Backporting Voxy means repeatedly replacing calls that do not exist on this MC/Iris/Embeddium
version with `if (false)` or `&& !false`. Those compile silently and produce no warning, so they
survive long after the real API becomes available again - every one of them is upstream behaviour
that is currently switched off.

Three shipped bugs came from exactly this:
  - MixinDefaultChunkRenderer: `if (false)` made setupViewport run twice per frame with shaders
  - VoxyRenderSystem:          `&&!false` ran the chunk bound renderer during Iris' shadow pass
  - ViewportSelector:          the shadow pass shared the main viewport

Convention: annotate every constant-disabled branch with one of
  PORT-STUB  - disabled by this port, revisit when the API is available
  UPSTREAM   - came from upstream in this shape, not ours to fix

Everything already present when this check was introduced is recorded in port_stubs_baseline.txt
(all of it upstream debug code and upstream TODOs). Only branches that are neither annotated nor
in the baseline are reported, so a newly introduced stub stands out instead of drowning in noise.

Refresh the baseline deliberately, never casually:  python3 scripts/validate_port_stubs.py --bless

Exits 0 always; this is an inventory, not a gate.
"""
import re
import sys
from pathlib import Path

SRC = Path(__file__).resolve().parent.parent / "src" / "main" / "java"
BASELINE = Path(__file__).resolve().parent / "port_stubs_baseline.txt"

# Constant-folded conditions: if (false), if (true), && !false, &&!false, || true ...
PATTERNS = [
    re.compile(r"\bif\s*\(\s*(?:true|false)\s*\)"),
    re.compile(r"(?:&&|\|\|)\s*!?\s*(?:true|false)\b"),
]
MARKERS = ("PORT-STUB", "UPSTREAM")
CONTEXT = 3  # lines above a hit that may carry the marker


def main() -> int:
    if not SRC.is_dir():
        print(f"source dir not found: {SRC}")
        return 0

    bless = "--bless" in sys.argv
    baseline = set()
    if BASELINE.is_file():
        baseline = {
            ln.strip() for ln in BASELINE.read_text(encoding="utf-8").splitlines()
            if ln.strip() and not ln.startswith("#")
        }

    unannotated, annotated = [], 0

    for path in sorted(SRC.rglob("*.java")):
        lines = path.read_text(encoding="utf-8", errors="replace").splitlines()
        for i, line in enumerate(lines):
            code = line.split("//", 1)[0]
            if not any(p.search(code) for p in PATTERNS):
                continue
            window = "\n".join(lines[max(0, i - CONTEXT): i + 1])
            if any(m in window for m in MARKERS):
                annotated += 1
                continue
            rel = path.relative_to(SRC.parent.parent.parent).as_posix()
            # Keyed on file + code text, not line number, so unrelated edits don't churn it
            unannotated.append((f"{rel}::{line.strip()}", i + 1))

    if bless:
        BASELINE.write_text(
            "# Constant-disabled branches present when this check was introduced (all upstream).\n"
            "# Key: <path>::<code>. Regenerate only when you have reviewed the diff.\n"
            + "".join(f"{key}\n" for key, _ in sorted(unannotated)),
            encoding="utf-8",
        )
        print(f"baseline written: {len(unannotated)} entries -> {BASELINE.name}")
        return 0

    new = [(key, lineno) for key, lineno in unannotated if key not in baseline]

    print("=== PORT STUB INVENTORY ===")
    print(f"annotated: {annotated}   baselined: {len(unannotated) - len(new)}   new: {len(new)}")
    if new:
        print("\nNew constant-disabled branches - is this an upstream code path you switched off?")
        print("Mark each PORT-STUB (revisit later) or UPSTREAM (not ours):")
        for key, lineno in new:
            path_part, code = key.split("::", 1)
            print(f"  {path_part}:{lineno}: {code}")
    print()
    return 0


if __name__ == "__main__":
    sys.exit(main())
