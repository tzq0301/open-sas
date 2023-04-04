import json
import requests

from endpoint import login
from endpoint import meta


def meta_info():
    login_response = login("user1", "user1")
    assert login_response is not None

    _, token = login_response

    meta_response = meta(token)
    assert meta_response is not None

    return meta_response


if __name__ == '__main__':
    print(meta_info())
