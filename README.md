# Bank 애플리케이션

## 테스트 커버리지 실행방법

### 1. 플러그인 추가
```gradle
plugins {
	// 생략
	id 'jacoco'
}

jacoco {
	toolVersion = "0.8.7"
}
```

### 2. 전체 테스트 실행
- 테스트를 실행하면 /build/jacoco/test.exec 파일이 생성된다.

### 3. 리포트 설정
```gradle
jacocoTestReport {
	reports {
		xml.enabled true
		csv.enabled true
		html.enabled true

		xml.destination file("${buildDir}/jacoco/index.xml")
		csv.destination file("${buildDir}/jacoco/index.csv")
		html.destination file("${buildDir}/jacoco/index.html")
	}
	finalizedBy 'jacocoTestCoverageVerification' // 테스트 기준 미달시 실패
}
```

### 4. 테스트 기준 설정
```gradle
jacocoTestCoverageVerification {
	violationRules {
		rule {
			enabled = true
			element = 'CLASS'

			limit {
				counter = 'LINE'
				value = 'COVEREDRATIO'
				minimum = 0.1
			}

			// 제외할 검증 대상
			// 패키지 + 클래스명을 적어주어야 합니다. 와일드 카드로 * (여러 글자) 와 ? (한 글자) 를 사용할 수 있습니다.
			excludes = [
					'*.*Application',
					'*.*Exception',
					'*.*Dto',
			]
		}
		rule {
			// 규칙을 여러개 추가할 수 있습니다.
		}
	}
}
```


### 5. 커버리지 보고서 생성 (test.exec 파일이 생성된 뒤 터미널에서 실행)
./gradlew jacocoTestReport

```text
counter
BRANCH : 조건문 등의 분기 수

CLASS : 클래스 수, 내부 메서드가 한 번이라도 실행된다면 실행된 것으로 간주한다.

COMPLEXITY : 복잡도

INSTRUCTION : Java 바이트코드 명령 수

METHOD : 메서드 수, 메서드가 한 번이라도 실행된다면 실행된 것으로 간주한다.

LINE : 빈 줄을 제외한 실제 코드의 라인 수, 라인이 한 번이라도 실행되면 실행된 것으로 간주한다.
```

```text
value
COVEREDCOUNT : 커버된 개수

COVEREDRATIO : 커버된 비율, 0부터 1사이의 숫자로 1이 100%이다.

MISSEDCOUNT : 커버되지 않은 개수

MISSEDRATIO : 커버되지 않은 비율, 0부터 1사이의 숫자로 1이 100%이다.

TOTALCOUNT : 전체 개수
```

### 6. 생성된 보고서 확인
- /build/jacoco
- index.html
- index.csv
- index.xml

### 참고
https://hudi.blog/dallog-jacoco/