import requests

str_urls = "http://www.w3schools.com/"
# print(dir(requests))

# GET Html
response = requests.get(str_urls)

print(response)
print(response.headers)
print(response.encoding)
print(response.content)
print(response.text)  # == response.content.decoding("utf-8")
print(response.status_code)

# GET JSON
# http://www.kobis.or.kr/kobisopenapi/webservice/rest/boxoffice/searchDailyBoxOfficeList.json?key=3d430a039fb1bae3fe5f0bc48df64e46&targetDt=20120101
req_url = "http://www.kobis.or.kr/kobisopenapi/webservice/rest/boxoffice/searchDailyBoxOfficeList.json?key=3d430a039fb1bae3fe5f0bc48df64e46&targetDt=20210101"

response = requests.get(req_url)

print(response.headers)
print(response.text)  # type: string

response_json = response.json()  # type: dict

from pprint import pprint as pp

pp(response_json)

for dailyBox in response_json['boxOfficeResult']['dailyBoxOfficeList']:
    print('순위:{} 제목{}'.format(dailyBox['rank'], dailyBox['movieNm']))
