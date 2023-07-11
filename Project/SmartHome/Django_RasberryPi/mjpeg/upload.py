import requests

def upload(image_name):
    url = 'http://172.30.1.78:8000/iot/upload_image/'

    files = {'image_file': open(image_name, 'rb')}     # 이미지 데이터 파일
    data = {'file_name': image_name}                 # 파일명
    response = requests.post(url, files=files, data=data)

    return response

'''
    files = {'image_file': open(image_name, 'rb')}
    data = {'file_name': image_name}
    response = requests.post(url, files=files, data=data)
'''