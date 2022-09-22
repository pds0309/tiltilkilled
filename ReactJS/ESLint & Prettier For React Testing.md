

## ESLint & Prettier for React Testing

<br>

### ESLint - Linting

- 정적 코드 분석 및 체킹


### Prettier

- 코드 포맷 자동화


<br>



### For Testing

**설치**

```shell
npm install eslint-plugin-testing-library
npm install eslint-plugin-jest-dom
```


**.eslintrc.json 설정 추가**

- `package.json` 의 `eslintConfig` 사항을 지워준다.

```json
{
	"plugins": [
		"testing-library",
		"jest-dom",
	],
	"extends": [
		"react-app",
		"react-app/jest",
		"plugin:testing-library/recommended",
		"plugin:testing-library/react",
		"plugin:jest-dom/recommended"
	]
}
```

**vscode 프로젝트 자동 교정 알림 기능 추가**

- .vscode 디렉터리 생성후 `settings.json` 파일 추가

- 다음과 같은 설정 추가

```json
{
	"eslint.options": {
	  "configFile": ".eslintrc.json"
	},
	"eslint.validate": ["javascript", "javascriptreact"],
	"editor.codeActionsOnSave": {
	  "source.fixAll.eslint": true
	}
}
```

- extension 설치하기

- 해당 추가사항 버전관리에서 제거하기

`.vscode`, `.eslintcache`


<br>


**Prettier 적용하기**

- extension 설치하기

- `settings.json` 에 기본 포매터로 설정하기

```
"editor.defaultFormatter": "esbenp.prettier-vscode",
"editor.formatOnSave": true,
```


