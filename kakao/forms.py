from django import forms
import json
import requests

class KaKaoTalkForm(forms.Form):
    text = forms.CharField(label='Message', max_length = 300)
    web_url = forms.CharField(label = 'Web URL', max_length = 300,
                              initial = 'http://192.168.35.245:8000/iot/mjpeg')
    mobile_web_url = forms.CharField(label = 'Mobile Url', max_length = 300,
                                     initial = 'http://192.168.35.245:8000/iot/mjpeg')


    def send_talk(self):
        talk_url = "https://kapi.kakao.com/v2/api/talk/memo/default/send"
        with open("access_token.txt", "r") as f:
            token = f.read()
        header = {"Authorization": f"Bearer {token}"}

        # 문자열 하나, 링크 하나로 구성되는 카톡 메세지 구성
        text_template = {
            'object_type': 'text',
            'text': self.cleaned_data['text'],
            'link': {
                'web_url': self.cleaned_data['web_url'],
                'mobile_web_url' : self.cleaned_data['mobile_web_url']
            }
        }
        print(text_template)
        payload = {'template_object': json.dumps(text_template)}
        res = requests.post(talk_url, data = payload, headers = header)
        return res, self.cleaned_data['text']


