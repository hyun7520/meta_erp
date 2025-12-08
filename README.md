# erp-system

## 시스템 구조
```
|- 경엉
|- 생산
|- 직원관리
```
## application.properties 추가
app.python.exec-path=venv/Scripts/python.exe

## 오류 로그
#### DB SQL 실행 오류
1. boolean type 오류 -> Oracle에 boolean 타입 미존재
   - 해결 방법 : boolean 타입을 `Number(1)`로 변경
2. varchar 범위 미정의 -> 다른 타입들과 맞춰 unit 칼럼은 20 / transaction_status은 50으로 설정
3. 외래키 연결 불가 오류
   - 발생한 쿼리
     - `ALTER TABLE "Products" ADD FOREIGN KEY ("product_id") REFERENCES "Orders" ("product_id");`
     - `ALTER TABLE "Lots" ADD FOREIGN KEY ("lot_id") REFERENCES "Transactions" ("lot_id");`