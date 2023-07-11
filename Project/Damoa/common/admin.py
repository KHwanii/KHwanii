from django.contrib import admin
from django.contrib.auth.admin import UserAdmin
from .models import CustomUser
from django.contrib.auth.models import User


class CustomUserAdmin(UserAdmin) :
    fieldsets = (
        (None, {'fields': ('username', 'name', 'email', 'password')}),
        (('Permissions'), {'fields': ('is_active', 'is_staff', 'is_superuser', 'groups', 'user_permissions')}),
        (('Important dates'), {'fields': ('last_login', 'date_joined')}),
    )
    list_filter = ('is_staff', 'is_superuser', 'is_active', 'groups')
    search_fields = ('email', 'name')
    ordering = ('email',)
    list_display = ('username', 'name', 'email', 'age', 'ad', 'is_staff')


admin.site.register(CustomUser, CustomUserAdmin)


    