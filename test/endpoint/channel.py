import requests
from requests import Response
from typing import Any, Optional

from endpoint.config import Config


def meta(jwt: str) -> Optional[Any]:
    headers = {
        'Authorization': f'Bearer {jwt}'
    }
    response: Response = requests.get(f"{Config.openmind}/channel/meta", headers=headers)
    if response.status_code != 200:
        return None
    data = response.json()
    return data['data']
