from dataclasses import dataclass


@dataclass
class Config:
    openmind: str = "http://127.0.0.1:8080"
