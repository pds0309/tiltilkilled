


### 로봇 메타 태그

HTML 문서의 <head> 태그내에 있는 <meta> 태그를 활용함

```html
<head>
<meta name="robots" content="명령어">
</head>
```

**noindex**

- 해당 페이지는 검색로봇의 수집에서 제외됨

**nofollow**

- 해당 페이지 내의 링크를 수집하지 않도록 설정됨


**예시**

```html
<head>
<meta name="robots" content="index,nofollow">
</head>
```

> 색인 대상이지만 페이지 내 링크는 수집하지 않는다.


특별한 제약조건이 없다면 웹 페이지가 검색 결과에서 제외되지 않도록 

로봇 메타 태그를 삭제하거나 기본 설정인 index, follow로 설정하는 것이 권장됨

```html
<head>
<meta name="robots" content="index,follow">
</head>
```


