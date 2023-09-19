from django.contrib import admin
from .models import CustomUser

class CustomUserAdmin(admin.ModelAdmin) :
    fieldsets = (
        (None, {'fields': ('userid', 'password', 'email', 'name', 'gender', 'nationality')}),
        (('Permissions'), {'fields': ('is_active', 'is_staff', 'is_superuser', 'groups', 'user_permissions')}),
        (('Important dates'), {'fields': ('last_login', 'date_joined')}),
    )
    add_fieldsets = (
        (None, {
            'classes': ('wide',),
            'fields': ('userid', 'password1', 'password2', 'email', 'name', 'gender', 'nationality'),
        }),
    )
    list_filter = ('is_staff', 'is_superuser', 'is_active', 'groups')
    search_fields = ('userid', 'email', 'name', 'gender', 'nationality')
    ordering = ('userid', 'email', 'name', 'gender', 'nationality')
    list_display = ('userid', 'email', 'name', 'gender', 'nationality', 'is_staff')


admin.site.register(CustomUser, CustomUserAdmin)