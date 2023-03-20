import json
import requests

from endpoint import login
from endpoint import meta

loginResponse = login("user1", "user1")
assert loginResponse is not None
_, token = loginResponse

metaResponse = meta(token)
assert metaResponse is not None

print(metaResponse)
