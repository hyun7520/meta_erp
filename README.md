# 📦 환경요인에 따른 원자재 로스율 분석기반 재고관리 최적화 시스템
**AI-CLOUD-DATA 연계 플랫폼 개발자 양성 과정 2기 팀 프로젝트**

> **"경험"이 아닌 "데이터" 기반의 선제적 재고 관리 모델**
> [cite_start]머신러닝을 활용하여 기상 정보 등 외부 환경 요인에 따른 미래 로스율과 수요를 예측하고, 최적의 재고 상태를 유지하는 스마트 워크플로우 프로그램입니다. [cite: 1, 18]

---

## 🚀 1. 프로젝트 기획 의도 및 목적
* [cite_start]**기존 방식의 한계:** 대부분 '경험'이나 '고정된 평균값'에 의존하여 재고를 관리하는 방식의 비효율성 [cite: 17, 31]
* [cite_start]**해결 방안:** 머신러닝을 결합하여 '미래의 로스율'을 예측하고, 효율적이고 정확한 '필요 재고'를 산출하는 선제적 관리 시스템 구축 [cite: 18, 32]
* [cite_start]**목표:** 중소규모 기업 보급을 목적으로 시스템 자동화를 통한 기회비용 최소화 [cite: 30, 845]

---

## 🛠 2. 사용 기술 (Tech Stack)

### **Backend / Infrastructure**
- [cite_start]**Language:** Java, Python [cite: 42, 46]
- [cite_start]**Framework:** Spring Boot, MyBatis, JPA [cite: 42, 45, 51]
- [cite_start]**DB:** Oracle Database, Amazon RDS [cite: 44, 47]
- [cite_start]**Cloud:** AWS (EC2, RDS) [cite: 50, 94]

### **AI / Data Science**
- [cite_start]**Library:** Scikit-learn, Prophet [cite: 48, 49]
- [cite_start]**Algorithm:** Random Forest Regressor (로스율 및 가격 예측), Facebook Prophet (수요량 예측) [cite: 553, 591, 819]
- [cite_start]**Data Source:** 기상청 날씨 데이터, KADX 농식품 빅데이터, 축산물 안전관리시스템(LPSMS), 한국농수산식품유통공사(aT) [cite: 497, 499, 502, 504]

### **Frontend**
- [cite_start]**Library:** ECharts (데이터 시각화 그래프) [cite: 52]

---

## ✨ 3. 핵심 기능 (Key Features)

### 📊 메인 대시보드 및 모니터링
- [cite_start]**공정 가시화:** 공장 생산 진행도, 완제품 출하 진행도 실시간 모니터링 [cite: 112, 336, 337]
- [cite_start]**데이터 분석:** 최근 5년간 제품 수요량, 날짜별 제품 로스율, 원자재 보유량 시각화 [cite: 338, 339, 340]

### 🤖 AI 기반 선제적 관리
- [cite_start]**품질 위험 경보:** 실시간 기상 데이터(습도 등)를 분석하여 로스율 상승 가능성이 높을 시 자동 품질 위험 경보 발령 [cite: 292, 301, 772]
- [cite_start]**수요 및 수급 예측:** 과거 10~15년치 데이터를 학습하여 미래 2년치 제품 수요량 및 가격 예측 [cite: 571, 671, 817, 820]
- [cite_start]**정밀 생산 계획:** AI 예측 로스율을 생산 목표 수량에 반영하여 실제 필요한 원재료 양을 정확히 파악 [cite: 306, 484, 490]

### 🏭 워크플로우 자동화
- [cite_start]**통합 프로세스:** 주문 접수 → 생산 요청 확인 및 재고 파악 → 원재료 부족 시 발주 요청 및 경영진 승인 → 생산 및 출하 [cite: 250, 262, 266, 269]
- [cite_start]**실시간 반영:** 미승인/승인/반려 상태를 실시간으로 반영하여 부서 간 갈등 최소화 및 인력 낭비 방지 [cite: 359, 360, 843, 845]

---

## 📋 4. 시스템 프로세스


[Image of supply chain workflow diagram]

`![Process Flow](./images/process_flow.png)` 
[cite_start]*(PPT 10페이지의 프로세스 플로우 이미지를 여기에 넣어주세요)* [cite: 250]

---

## 👨‍💻 5. 팀원 및 역할 (Team)
- [cite_start]**고상현 (Back-end Developer):** - 생산, 재료 발주, 제품 출하 기능 구현 및 DB 설계 [cite: 93]
    - [cite_start]AWS EC2 서버 및 RDS 인프라 구축 [cite: 94]
- [cite_start]**김00:** AI 모델 개발, 공공데이터 수집 및 예측치 산출, 인사 기능 구현 [cite: 77, 78, 80]
- [cite_start]**김00:** 백엔드 개발, 메인 대시보드 화면 및 AI 활용 그래프 산출, UI 리팩토링 [cite: 82, 83, 85]
- [cite_start]**윤00:** 백엔드 개발, 경영 페이지 화면 담당, UI/UX 설계 및 발표 자료 제작 [cite: 88, 89, 90]
- [cite_start]**허00:** AI 모델 개발, 데이터 가공 및 예측치 산출, 프로젝트 발표 [cite: 96, 98, 99]

---

## 📈 6. 기대 효과 (Expected Outcomes)
- [cite_start]**자동화 시스템:** 실시간 데이터 기반 AI 모델을 활용한 정확한 로스율 도출 및 효율적 재고 관리 [cite: 838, 840]
- [cite_start]**기회비용 최소화:** 정확한 계산을 통한 원가 절감 및 부서 간 업무 효율 증대 [cite: 844, 845]
- [cite_start]**발전 가능성:** 저가형 보급 가능성 및 식품 외 다양한 제조 분야로의 확장 기대 [cite: 846, 847]
