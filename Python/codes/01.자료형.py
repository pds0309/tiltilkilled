## 기본 자료형

# 정수
print(20, 0, -10)

# 실수
print(3.14, -4.5)

# 논리
print(True, False)

# None
print(None)

# Function
print(print)

# 메모리가 허용된다면 정수 자리수 제한 없다
print(123212346464456781232213124712849265890157209109515092)
print(123_456_789)

## 집합형 자료형

# 문자열
print("HELL")
print('HELL')
print("""HELL""")
print('''HELL''')

# 리스트
a = [1, 2, 3, 4, 5]
print(a)
print(a[0])
a[0] = 5
print(a[0])

# 튜플
b = (1, 2, 3, 4, 5)
print(b)
print(b[0])

# Tuple Doesn't support item assignment
# b[0] = 99


# Set
c = {1, 2, 3, 4, 5, 5}
print(c)
c.add(7)
print(c)

# Dictionary
d = {"name": "김갑환", "age": 46}
print(d)
print(d["name"])
d["hell"] = 5
print(d)

## Escape

print("Hell\"World")
print("Hell\nWorld")
print("Hell\tWorld")
print("Hell\\World")

## Escape 무시

print(r"Hell\\World")

## Identifier

# 모든 변수가 참조형임

hi = 25
print(hi)
print(id(hi))

hell = 26
print(id(hell))

hell = 25
print(id(hi) == id(hell))

# 변수 여러개에 같은 값 할당

n = n2 = n3 = 100
print(n, n2, n3)

# 변수 여러개에 여러 값 할당 - 양쪽 갯수 일치해야 해

nn, nn2 = 100, 200
print(nn, nn2)

# 괄호 생략 시 튜플 처럼 동작한다.
# => x1,x2,x3 = (100,200,300)

tuple1 = (100, 200, 300, 400)
x1, x2, _, _ = tuple1

# list 분해

list1 = ['김갑환', 100, True, [1, 2]]
[k1, k2, k3, [kk1, kk2]] = list1

print(list1)

## 표준 입출력


# 표준 출력
# 가변인자 때문에 여러값 넣기 가능, 자동 개행됨
# print(...)
#    print(value, ..., sep=' ', end='\n', file=sys.stdout, flush=False)
print('a', 1, 5, True)

print('a', 5, '배고파', end="zz", sep='중간마다 넣을꼬얌')
