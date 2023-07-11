from django.contrib import admin
from .models import Club, Board, Reply



# class BoardAdmin(admin.ModelAdmin):
#     search_fields = ['category']

admin.site.register(Club)
admin.site.register(Board)
admin.site.register(Reply)