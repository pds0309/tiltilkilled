## 산술 , 대입, 비교

'''
+
-
*
/ : 소숫점 포함한 나누기
% : 나머지
// : 몫
** : 지수승

'''

n1 = 10
n2 = 3

print(10 / 3)  # 3.3333333333333335
print(10 % 3)  # 1
print(10 // 3)  # 3
print(10 ** 3)  # 1000

# 몫 , 나머지 반환

result = divmod(n1, n2)
print(result)

x, y = divmod(n1, n2)

print(x, y)
print(f"몫{x}, 나머지{y}")

# 대입연산자

n, *n2 = 1, 2, 3
print(n2)

*n1, n2, n3 = 1, 2, 3, 4, 5, 6, 7
print(n1, n2, n3)  # n1: [1,2,3,4,5]

k1, k2 = {"A": 1, "B": "HI"}
print(k1, k2)  # A B

# 비교 연산자

a = 1
b = 1

# 값만 비교한다
print(a == b)

# id를 비교한다
print(a is b)

c = [1, 2, 3]
d = [1, 2, 3]
print(c == d)  # True
print(c is d)  # False
print(id(c))
print(id(d))

## 복사하기

# 복사안하면?

a = [1, 2, 3]
b = a

print(id(a) == id(b))  # True

a = [1, 2, 3]
b = list(a)

print(id(a) == id(b))  # False

a = [1, 2, 3]
b = a.copy()

print(id(a) == id(b))  # False

a = [1, 2, 3]
# slicing
b = a[:]

print(id(a) == id(b))  # False

print(a == b)  # True
print(a is b)  # False

## 논리

# js 와 마찬가지로 빈 값, None 등은 False 로 처리 가능

# Empty Dict

a = {}
print(not a)  # True
print(bool(a))  # False

b = {"A": "HI"}
print(not b)  # False
print(bool(b))  # True

c = None
print(c is None)  # True

## 비트 연산

'''

&
|
^
<<
>>

'''

a = 11
b = 8
print(bin(a))

print(a & b, bin(a & b))
print(a | b, bin(a | b))
print(a ^ b, bin(a ^ b))
print(a << 2, bin(a << 2))

## 값이 있는가?

li = [1, 2, 3, 4]
print(2 in li)  # True

dic = {"name": "김갑환", "age": 25}
print("age" in dic)  # True
print(25 in dic)  # False



## Type

print(type(2))
print(type(2.5))
print(type("HELLO"))
print(type([1,2,3]))
print(type({1,2,3}))
print(type((1,2,3)))
print(type({}))
print(type(None))
print(type(True))
print(type(print))

'''
<class 'int'>
<class 'float'>
<class 'str'>
<class 'list'>
<class 'set'>
<class 'tuple'>
<class 'dict'>
<class 'NoneType'>
<class 'bool'>
<class 'builtin_function_or_method'>
'''