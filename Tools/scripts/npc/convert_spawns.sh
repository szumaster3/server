#!/usr/bin/env bash

INPUT="npc_spawns.txt"
OUTPUT="npc_spawns_converted.txt"

awk '
BEGIN {
    plane = 0
    print "[" > "'"$OUTPUT"'"
}
/plane=/ {
    if (match($0, /plane=([0-9]+)/, m))
        plane = m[1]
}
{
    while (match($0, /npcid:([0-9]+),x:([0-9]+),y:([0-9]+)/, m)) {
        id = m[1]
        x = m[2]
        y = m[3]
        npc[id] = npc[id] sprintf("{%s,%s,%s,1,0}-", x, y, plane)
        $0 = substr($0, RSTART + RLENGTH)
    }
}
END {
    n = 0
    PROCINFO["sorted_in"] = "@ind_num_asc"
    for (id in npc) {
        if (n > 0)
            print "," >> "'"$OUTPUT"'"
        print "  {" >> "'"$OUTPUT"'"
        print "    \"npc_id\": \"" id "\"," >> "'"$OUTPUT"'"
        print "    \"loc_data\": \"" npc[id] "\"" >> "'"$OUTPUT"'"
        printf "  }" >> "'"$OUTPUT"'"
        n++
    }
    print "\n]" >> "'"$OUTPUT"'"
}
' "$INPUT"
