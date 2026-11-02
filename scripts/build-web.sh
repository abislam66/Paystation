#!/usr/bin/env bash
# Rebuild web/paystation.jar (Java 8 bytecode, required by the CheerpJ browser runtime).
# Run this after changing anything in src/main, then commit web/paystation.jar.
set -euo pipefail
cd "$(dirname "$0")/.."

OUT=$(mktemp -d)
javac --release 8 -nowarn -d "$OUT" src/main/java/edu/temple/cis/paystation/*.java
printf 'Main-Class: edu.temple.cis.paystation.PayStationMain\n' > "$OUT/MANIFEST.MF"
jar cfm web/paystation.jar "$OUT/MANIFEST.MF" -C "$OUT" edu
rm -rf "$OUT"
echo "Built web/paystation.jar"
