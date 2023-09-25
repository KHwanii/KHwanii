from django.db import models
from common.models import CustomUser
from django.core.validators import MinValueValidator, MaxValueValidator
from django.core.exceptions import ValidationError

# Create your models here.

# 카테고리
class Category(models.Model):
    name = models.CharField(max_length=100)         # 카데고리 이름
    description = models.TextField(blank=True)      # 카테고리 설명

    def __str__(self):
        return self.name

# 제품
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
    
# 제품 리뷰
class ProductReview(models.Model):
    product = models.ForeignKey(Product, on_delete=models.CASCADE)      # 제품명
    user = models.ForeignKey(CustomUser, on_delete=models.CASCADE)      # 사용자
    STAR_CHOICES = [
        (1, '1 - 매우 나쁨'),
        (2, '2 - 나쁨'),
        (3, '3 - 평범'),
        (4, '4 - 좋음'),
        (5, '5 - 매우 좋음'),
    ]
    star = models.PositiveIntegerField(choices=STAR_CHOICES)            # 별점
    comment = models.TextField()                                        # 댓글
    created_at = models.DateTimeField(auto_now_add=True)                # 생성일시

# 장바구니
class Cart(models.Model):
    user = models.OneToOneField(CustomUser, on_delete=models.CASCADE)   # 장바구니 유저

# 장바구니 아이템
class CartItem(models.Model):
    cart = models.ForeignKey(Cart, on_delete=models.CASCADE)            # 장바구니
    product = models.ForeignKey(Product, on_delete=models.CASCADE)      # 제품
    # 수량, 최소 1 ~ 최대 10
    quantity = models.PositiveIntegerField(validators=[MinValueValidator(1), MaxValueValidator(10)])

# 주문
class Order(models.Model):
    user = models.ForeignKey(CustomUser, on_delete=models.CASCADE)      # 주문자
    created_at = models.DateTimeField(auto_now_add=True)                # 주문일시


# 주문 아이템
class OrderItem(models.Model):
    order = models.ForeignKey(Order, on_delete=models.CASCADE)          # 주문 내용
    product = models.ForeignKey(Product, on_delete=models.CASCADE)      # 제품
    # 수량, 최소 1 ~ 최대 10
    quantity = models.PositiveIntegerField(validators=[MinValueValidator(1), MaxValueValidator(10)])

# 할인 코드
class DiscountCode(models.Model):
    code = models.CharField(max_length=50, unique=True)                     # 할인 코드
    discount_amount = models.PositiveIntegerField(null=True, blank=True)    # 할인금액
    discount_rate = models.PositiveIntegerField(null=True, blank=True)      # 할인율

    def clean(self):
    # 할인 금액과 할인율 중 하나만 적용하도록 검사
        if self.discount_amount and self.discount_rate:
            raise ValidationError("할인 금액과 할인율 중 하나만 적용해야 합니다.")
    
    # 할인 적용 유효성 검사를 위해 save 메소드 재정의
    def save(self, *args, **kwargs):
        self.clean()
        super().save(*args, **kwargs)


# 결제
class Payment(models.Model):
    order = models.OneToOneField(Order, on_delete=models.CASCADE)       # 주문
    price = models.IntegerField(null=False, blank=False)                # 가격


# 배송
class Shipping(models.Model):
    order = models.OneToOneField(Order, on_delete=models.CASCADE)       # 주문
    address = models.TextField()                                        # 주소
    method = models.CharField(max_length=50)                            # 배송 방법
    SHIPPING_STATUS = (
        ('pending', '배송 준비 중'),
        ('shipped', '배송 중'),
        ('delivered', '배송 완료'),
    )
    status = models.CharField(max_length=50, choices=SHIPPING_STATUS)   # 배송 현황
