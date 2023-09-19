from django.db import models

# Create your models here.

class Category(models.Model):
    name = models.CharField(max_length=100)         # 카데고리 이름
    description = models.TextField(blank=True)      # 카테고리 설명

    def __str__(self):
        return self.name

class Product(models.Model):
    sku = models.CharField(max_length=50, unique=True, verbose_name='Stock Keeping Unit')                   # 고유번호
    name = models.CharField(max_length=200)                                                                 # 제품 이름
    description = models.TextField(blank=True)                                                              # 제품 설명
    price = models.DecimalField(max_digits=10, decimal_places=2)                                            # 제품 가격
    stock = models.PositiveIntegerField(default=0)                                                          # 제품 재고
    category = models.ForeignKey(Category, on_delete=models.SET_NULL, null=True, related_name='products')   # 카테고리
    image = models.ImageField(upload_to='products/', blank=True, null=True)                                 # 제품 이미지
    created_at = models.DateTimeField(auto_now_add=True)                                                    # 제품 등록일
    updated_at = models.DateTimeField(auto_now=True)                                                        # 업데이트 일시

    def __str__(self):
        return self.name
    
