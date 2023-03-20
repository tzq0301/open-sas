import json
import requests
from requests import Response
from typing import Optional, Tuple

from endpoint.config import Config


def login(username: str, password: str) -> Optional[Tuple[int, str]]:
    data = json.dumps({
        "username": username,
        "password": password,
    })
    headers = {
        'Content-type': 'application/json'
    }
    response: Response = requests.post(f"{Config.openmind}/user/login", data, headers=headers)
    if response.status_code != 200:
        return None
    data = response.json()
    return data['data']['id'], data['data']['token']
