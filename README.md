# 🚗 고속도로 차량 관리 시스템

> **고속도로를 주행하는 차량을 효율적으로 관리하고 통행료를 계산하는 시스템**  
> **'객체지향프로그래밍 및 실습'** 수업에서 Java와 Eclipse IDE를 활용하여 개발한 프로젝트입니다.

---

## 📌 시스템 개요
고속도로 차량 관리 시스템은 **고속도로를 통행하는 차량을 관리하고 통행료를 계산하는 시스템**입니다.  
시스템은 다음과 같은 두 개의 데이터 파일을 사용합니다:  

- **`vehicles.txt`** : 차량 데이터를 미리 정의  
- **`rates.txt`** : 차량 종류별 기본 요금 및 거리 요율을 정의  

---

## 🎯 기술 스택
- **언어:** Java  
- **개발 환경:** Eclipse IDE  
- **데이터 저장:** `vehicles.txt`, `rates.txt`  

---

## 📂 파일 구조
```shell
📁 HighwayManagementSystem
 ┣ 📜 Main.java
 ┣ 📜 Vehicle.java
 ┣ 📜 TollCalculator.java
 ┣ 📜 Highway.java
 ┣ 📜 CommandProcessor.java
 ┣ 📜 vehicles.txt
 ┗ 📜 rates.txt
```

---

## ⚙ 기능 및 명령어

### 1️⃣ 시간 설정 (`t`)
현재 시간을 설정합니다. 새로운 시간이 설정되면 **모든 차량의 위치가 갱신**되며,  
차량의 **진출 시간이 현재 시간을 초과하면 통행료가 자동으로 계산**됩니다.



### 2️⃣ 차량 진입 (`n`)
차량을 고속도로에 진입시키는 명령어입니다.  
차량 번호, 진입 장소, 진출 장소, 속도를 입력하면 차량이 등록됩니다.  



### 3️⃣ 고속도로상의 모든 차량 보기 (`o`)
현재 고속도로를 주행 중인 **모든 차량 정보를 출력**합니다.  
출력 정보에는 다음이 포함됩니다:  
- 차량 종류  
- 차량 번호  
- 속성 정보 (배기량 등)  
- 진입 시간 및 장소  
- 진출 장소  
- 속도 및 현재 위치  



### 4️⃣ 고속도로를 진출한 모든 차량 정보 보기 (`x`)
현재 시간 기준으로 **고속도로를 빠져나간 차량의 정보를 출력**합니다.  
출력 정보에는 다음이 포함됩니다:  
- 차량 종류  
- 차량 번호  
- 속성 정보  
- 진입 시간 및 장소  
- 진출 시간 및 장소  
- **통행료**  



### 5️⃣ 현재 시간 보기 (`c`)
현재 설정된 시스템 시간을 출력합니다.



### 6️⃣ 시스템 종료 (`q`)
프로그램을 종료합니다.

---

## 📝 명령어 예시

### 🕒 1. 시간 설정 (`t`)
**명령 형식:**  
```shell
t 연 월 일 시 분
```
예시 입력:
```shell
t 2023 5 10 14 30
```
예시 출력:
```shell
현재시간: 2023/05/10, 14:30
```

### 🚗 2. 차량 진입 ('n')
**명령 형식:**  
```shell
n 차량번호 진입장소 진출장소 속도
```
예시 입력:
```shell
n 1234 서울 대전 100
```
예시 출력:
```shell
승용차 1234 진입  
진입시간: 2023/05/10:14:30  
진입장소: 서울  
진출장소: 대전  
시속: 100km
```
### 🚙 3. 고속도로상의 모든 차량 보기 ('o')
**명령 형식:**  
```shell
o
```
예시 입력:
```shell
o
```
예시 출력:
```shell
1. car 1234 2000cc 2023/05/10:14:30 서울->대전 시속:100km 위치:100km  
2. hybrid car 2345 1500cc 2023/05/10:15:00 수원->부산 시속:120km 위치:100km  
3. bus 3456 45인승 2023/05/10:15:00 부산->서울 시속:120km 위치:330km
```
### 🏁 4. 고속도로를 진출한 모든 차량 보기 ('x')
**명령 형식:**  
```shell
x
```
예시 입력:
```shell
x
```
예시 출력 (현재시간: 16:30 가정):
```shell
1. car 1234 2000cc 2023/05/10:14:30 서울->대전 2023/05/10:16:06 6800원  
2. ...
```

### ⏳ 5. 현재시간 보기 ('c')
**명령 형식:**  
```shell
c
```
예시 입력:
```shell
c
```
예시 출력 (현재시간: 16:30 가정):
```shell
2023/05/10:16:30
```
