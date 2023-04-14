from dataclasses import dataclass
import sys
from typing import List

import matplotlib.pyplot as plt


@dataclass
class Record:
    left: int
    right: int
    coordinate: int


records: List[Record] = []

for line in sys.stdin:
    line = line.rstrip()
    try:
        record: Record = eval(line)
    except SyntaxError:
        continue
    records.append(record)

plt.plot(
    list(map(lambda r: r.left, records)),
    range(len(records)),
    color="red",
)
plt.plot(
    list(map(lambda r: r.right, records)),
    range(len(records)),
    color="red",
)
plt.plot(
    list(map(lambda r: r.coordinate, records)),
    range(len(records)),
    color="blue",
)
plt.show()
