# Generated by Django 4.1.7 on 2023-06-24 02:20

import common.models
from django.db import migrations, models
import django.utils.timezone


class Migration(migrations.Migration):

    initial = True

    dependencies = [
        ("auth", "0012_alter_user_first_name_max_length"),
    ]

    operations = [
        migrations.CreateModel(
            name="CustomUser",
            fields=[
                (
                    "id",
                    models.BigAutoField(
                        auto_created=True,
                        primary_key=True,
                        serialize=False,
                        verbose_name="ID",
                    ),
                ),
                ("password", models.CharField(max_length=128, verbose_name="password")),
                ("userid", models.CharField(max_length=15, unique=True)),
                ("email", models.EmailField(default="", max_length=254, unique=True)),
                ("name", models.CharField(max_length=15)),
                ("apartment", models.CharField(default="아파트명", max_length=40)),
                (
                    "dong",
                    models.CharField(
                        choices=[
                            ("101", "101동"),
                            ("102", "102동"),
                            ("103", "103동"),
                            ("104", "104동"),
                            ("105", "105동"),
                        ],
                        default="101",
                        max_length=5,
                    ),
                ),
                (
                    "ho",
                    models.CharField(
                        choices=[
                            ("101", "101호"),
                            ("102", "102호"),
                            ("201", "201호"),
                            ("202", "202호"),
                            ("301", "301호"),
                            ("302", "302호"),
                            ("401", "401호"),
                            ("402", "402호"),
                            ("501", "501호"),
                            ("502", "502호"),
                            ("601", "601호"),
                            ("602", "602호"),
                            ("701", "701호"),
                            ("702", "702호"),
                            ("801", "801호"),
                            ("802", "802호"),
                            ("901", "901호"),
                            ("902", "902호"),
                            ("1001", "1001호"),
                            ("1002", "1002호"),
                        ],
                        default="101",
                        max_length=4,
                    ),
                ),
                ("is_active", models.BooleanField(default=True)),
                ("is_superuser", models.BooleanField(default=False)),
                ("is_staff", models.BooleanField(default=False)),
                (
                    "date_joined",
                    models.DateTimeField(default=django.utils.timezone.now),
                ),
                ("last_login", models.DateTimeField(blank=True, null=True)),
                (
                    "groups",
                    models.ManyToManyField(
                        blank=True,
                        help_text="The groups this user belongs to. A user will get all permissions granted to each of their groups.",
                        related_name="user_set",
                        related_query_name="user",
                        to="auth.group",
                        verbose_name="groups",
                    ),
                ),
                (
                    "user_permissions",
                    models.ManyToManyField(
                        blank=True,
                        help_text="Specific permissions for this user.",
                        related_name="user_set",
                        related_query_name="user",
                        to="auth.permission",
                        verbose_name="user permissions",
                    ),
                ),
            ],
            options={
                "verbose_name": "User",
                "verbose_name_plural": "Users",
                "ordering": ["-date_joined"],
            },
            managers=[
                ("objects", common.models.CustomUserManager()),
            ],
        ),
    ]
