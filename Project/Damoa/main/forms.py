from django import forms
from main.models import Board, Club, Reply
from django_summernote.widgets import SummernoteWidget
from common.models import CustomUser

class BoardForm(forms.ModelForm):
    content = forms.CharField(widget=SummernoteWidget())
 
    class Meta:
        model = Board
        fields = ['club', 'subject','content','event_date']
        widgets = {
            'subject': forms.TextInput(attrs={'placeholder': '제목을 입력하세요.'}),
            'content': forms.Textarea(attrs={'placeholder': '내용을 입력하세요.'}),
            'event_date': forms.DateTimeInput(attrs={'type': 'datetime-local', 'placeholder': 'YYYY-MM-DDTHH:MM', 'required':True}),
            'club':forms.Select(attrs={'class':'form-control', 'required':True}),
        }

    def __init__(self, *args, **kwargs):
        user = kwargs.pop('user', None)
        super().__init__(*args, **kwargs)
        self.fields['club'].queryset = user.author_club.all()
        
  
    
   
class ClubForm(forms.ModelForm):
    class Meta:
        model = Club
        fields = ['category','name']
        labels={
            'category':'카테고리',
            'name':'이름',
        }   

class ReplyForm(forms.ModelForm):
    class Meta:
        model = Reply
        fields = ['content']
        labels={
            'content':'답변내용',
        }   