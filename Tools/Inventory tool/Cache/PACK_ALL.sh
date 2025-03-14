#!/bin/bash

folder_path="index1"

if [ ! -d "$folder_path" ]; then
    echo "index1 $folder_path not exist."
    exit 1
fi

for file in "$folder_path"/*; do
    if [ -f "$file" ]; then
        gzip -c "$file" > "$file.gz"
        echo "File $file packed into $file.gz"
    fi
done
