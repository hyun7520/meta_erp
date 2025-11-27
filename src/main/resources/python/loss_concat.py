import pandas as pd
import os
import sys

# ----------------- 설정 -----------------

# 파일 경로 (실제 업로드된 파일 이름을 사용)
#erp-system\src\main\resources\static\file
if len(sys.argv) < 4:
    print("Error: 필요한 CSV 파일 경로가 모두 전달되지 않았습니다.")
    sys.exit(1)

# 변수명을 헷갈리지 않게 명확히 매핑합니다.
FILE_PATH_FUTURE = sys.argv[1]   # Java의 csvFile1 (future_material_date.csv)
FILE_PATH_PAST = sys.argv[2]     # Java의 csvFile2 (past_material_date.csv)
FILE_PATH_PRODUCT = sys.argv[3]
# FILE_PATH_1 = "input_data_past_material_date.csv"  # 과거 품목 로스율
# FILE_PATH_2 = "input_data_future_material_date.csv"  # 미래 품목 로스율
# FILE_PATH_3 = "input_data_product_loss_predict.csv"  # 전체 제품 로스율

# 최종 결과 파일 이름
#erp-system\src\main\resources\static\file
OUTPUT_FILENAME = "total_loss.csv"

# 최종적으로 필요한 칼럼 목록 (총 14개)
FINAL_COLUMNS = [
    '연월', '감자', '당근', '대파', '배추', '양파', '고추', '호박',
    '돼지고기', '닭고기', # 추가된 품목 로스율
    '카레', '닭볶음탕', '제육볶음', '야채수프' # 제품 로스율
]

# 품목별 로스율 칼럼 (피벗팅 시 사용)
ITEM_LOSS_COLUMNS = ['감자', '당근', '대파', '배추', '양파', '고추', '호박', '돼지고기', '닭고기']


# ----------------- 함수 정의 -----------------

def load_data(file_path):
    """파일이 존재하는지 확인하고 불러오는 함수"""
    # 사용자가 업로드한 파일은 이미 접근 가능하므로 존재 여부 확인 로직은 단순화
    try:
        # 인코딩 시도 (utf-8, cp949)
        return pd.read_csv(file_path, encoding='utf-8')
    except UnicodeDecodeError:
        try:
            return pd.read_csv(file_path, encoding='cp949')
        except Exception as e:
            print(f"오류: {file_path} 파일 로드 중 오류 발생 - {e}")
            return None


def preprocess_item_loss(df_past, df_future):
    """과거/미래 품목별 로스율 데이터를 통합 및 피벗팅하는 함수 (9개 품목 포함)"""

    # 과거 데이터 처리 (식자재 로스율 과거 데이터.csv)
    # 필요한 칼럼: '연월', '품목명', '로스율_가상_%'
    df_past_proc = df_past[['연월', '품목명', '로스율_가상_%']].copy()
    df_past_proc['연월'] = df_past_proc['연월'].astype(str)
    df_past_proc['품목명'] = df_past_proc['품목명'].replace('풋고추', '고추')  # '풋고추'를 '고추'로 통일

    df_past_pivot = df_past_proc.pivot_table(
        index='연월', columns='품목명', values='로스율_가상_%', aggfunc='first'
    ).reset_index()

    # 미래 데이터 처리 (식자재 로스율 예측 파일.csv)
    # 필요한 칼럼: '연월', '품목명', '예상_로스율_%'
    df_future_proc = df_future[['연월', '품목명', '예상_로스율_%']].copy()
    df_future_proc['연월'] = df_future_proc['연월'].astype(str)
    df_future_proc['품목명'] = df_future_proc['품목명'].replace('풋고추', '고추')  # '풋고추'를 '고추'로 통일

    df_future_pivot = df_future_proc.pivot_table(
        index='연월', columns='품목명', values='예상_로스율_%', aggfunc='first'
    ).reset_index()

    # 데이터 통합
    df_item_loss_all = pd.concat([df_past_pivot, df_future_pivot], ignore_index=True)

    # 최종 품목 칼럼에 맞게 정리 및 누락 칼럼 추가
    df_item_loss_final = df_item_loss_all[['연월'] + [col for col in ITEM_LOSS_COLUMNS if col in df_item_loss_all.columns]]
    for col in ITEM_LOSS_COLUMNS:
        if col not in df_item_loss_final.columns:
            df_item_loss_final[col] = pd.NA

    return df_item_loss_final[['연월'] + ITEM_LOSS_COLUMNS]


def preprocess_product_loss(df_past):
    """과거 제품별 로스율 데이터를 정리하는 함수"""

    # 과거 데이터 처리 (제품 로스율(카레,닭볶,제육,수프)(종합).csv)
    df_past = df_past.copy()
    # 칼럼명 변경 (기존: 연월,제육볶음_로스율(%),닭볶음탕_로스율(%),카레_로스율(%),야채수프_로스율(%) 가정)
    df_past.columns = ['연월', '제육볶음', '닭볶음탕', '카레', '야채수프']
    df_past['연월'] = df_past['연월'].astype(str)

    # 요청하신 순서에 맞게 재정렬 (카레, 닭볶음탕, 제육볶음, 야채수프)
    df_past = df_past[['연월', '카레', '닭볶음탕', '제육볶음', '야채수프']]

    # (이 파일은 로스율 값이 이미 float 형식이거나 바로 변환 가능하다고 가정)
    for col in ['카레', '닭볶음탕', '제육볶음', '야채수프']:
        if df_past[col].dtype == 'object':
             # 값에 '%'가 있다면 제거하고 숫자로 변환
            df_past[col] = (
                df_past[col]
                .astype(str)
                .str.replace('%', '')
                .astype(float)
            )

    return df_past


# ----------------- 즉시 실행 코드 -----------------

print("--- start concat csv ---")

# 1. 데이터 로드
df_future_data = load_data(FILE_PATH_FUTURE) # 미래
df_past_data = load_data(FILE_PATH_PAST)     # 과거
df_product_data = load_data(FILE_PATH_PRODUCT) # 제품

if not all([df_future_data is not None, df_past_data is not None, df_product_data is not None]):
    print("error: 파일 로드 실패")
else:
    # 2. 품목별 로스율 데이터 통합 및 정리
    # [주의] 함수 정의가 preprocess_item_loss(df_past, df_future) 순서임
    df_item_loss = preprocess_item_loss(df_past_data, df_future_data)

    # 3. 제품별 로스율 데이터 정리
    df_product_loss = preprocess_product_loss(df_product_data)

    # 4. 최종 병합
    df_final = pd.merge(df_item_loss, df_product_loss, on='연월', how='outer')

    # 5. 최종 칼럼 순서 및 정렬
    df_final = df_final[FINAL_COLUMNS]
    df_final = df_final.sort_values(by='연월').reset_index(drop=True)

    # 6. 결과 출력
    try:
        # 파일 저장 (선택 사항: 임시 폴더에 저장됨)
        # df_final.to_csv(OUTPUT_FILENAME, index=False, encoding='utf-8-sig')

        # [핵심] Java가 결과를 가져갈 수 있도록 CSV 포맷 문자열로 출력 (stdout)
        # index=False를 해야 '0, 2023-01...' 같은 인덱스 숫자가 안 붙음
        print(df_final.to_csv(index=False, encoding='utf-8-sig'))

    except Exception as e:
        print(f"error: {e}")