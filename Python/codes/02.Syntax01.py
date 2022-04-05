'''
Python Format 출력

# 포맷에 대한 정보는
# help('FORMATTING')


1. "{idx1}{idx0}".format(v1,v2)

'''

print("이름: {}, 나이: {}".format("김갑환", 46))
print("이름: {1}, 나이: {0}".format("김갑환", 46))
print("이름: {1}, 나이: {0} 값을 못해 {0}".format("김갑환", 46))

format_str = "이름:{}, 나이{}".format("김갑환", 46)

print(format_str)

# 인덱스와 Key 혼합 가능하나 Key:Value 형태는 맨 뒤에 와야함
print("나이:{age}, 이름{}".format("김갑환", age=44))

# 쪼개기
print("{1}, {2}, {0}".format(*"김갑환짱"))

dict_str = {"member": "김갑환", "age": 44}
print("{}".format(dict_str))
print("{member} {age}".format(**dict_str))

# 정렬 출력

print("정렬{0:5}".format("김갑환"))
print("정렬{0:*>8}".format("김갑환"))
print("정렬{0:*<8}".format("김갑환"))
print("정렬{0:*^8}".format("김갑환"))

'''
정렬김갑환  
정렬*****김갑환
정렬김갑환*****
정렬**김갑환***
'''

# 숫자 포맷

print("{0}".format(12341251516))
print("{0:f}".format(12341251516))
print("{0:.2f}".format(12341251516))
print("{0:d}".format(12341251516))
print("{0:20d}".format(12341251516))
print("{0:,}".format(123456777))

'''
12341251516
12341251516.000000
12341251516.00
12341251516
         12341251516
123,456,777
'''

## 날짜

from datetime import datetime

# 현재 시간
current_date = datetime.now()
print(current_date)

# 년
print("{:%Y}".format(current_date))

print("{:%Y-%m-%d, %H:%M:%S}".format(current_date))

## Format String

name = '김갑환'
age = 44

# f
print(f"이름:{name}, 나이:{age}")  # 이름:김갑환, 나이:44

# %
print("이름: %s, 나이: %d , 키: %f" % (name, age, 187.5444))

