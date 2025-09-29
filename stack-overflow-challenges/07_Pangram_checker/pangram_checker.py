# https://stackoverflow.com/beta/challenges/79767716/code-challenge-7-pangram-checker

import os
import requests
from pathlib import Path
import re
import json


def download_file(url, filename):
    if not os.path.exists(filename):
        response = requests.get(url)
        with open(filename, "wb") as f:
            f.write(response.content)
    return filename


if __name__ == "__main__":
    data_file = download_file(
        "https://drive.usercontent.google.com/u/0/uc?id=1nDVLzUkwbKwuPqZocVQnqkYFKwTu5mFQ&export=download",
        Path(__file__).parent / "pangram.json",
    )

    with open(data_file, "r") as f:
        lines = json.loads(f.read())

    pangram_count = 0
    pure_pangram_count = 0
    everything_else_then_lowercase_regex = re.compile(r"[^a-z]")
    for s in lines:
        only_alphabets = re.sub(everything_else_then_lowercase_regex, "", s.lower())
        unique_alphabets = set(only_alphabets)
        is_pangram = len(unique_alphabets) == 26
        is_pure = is_pangram and (len(only_alphabets) == 26)
        pangram_count += is_pangram
        pure_pangram_count += is_pure

    print("Number of non pangrams:", len(lines) - pangram_count)
    print("Number of pangrams:", pangram_count)
    print("Number of pure pangrams:", pure_pangram_count)
