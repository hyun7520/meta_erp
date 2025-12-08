import pandas as pd
from prophet import Prophet
import os
import sys

# 1. 데이터 로드 및 전처리
# 가상의 제품 수요량.csv 파일이 현재 디렉토리에 있다고 가정합니다.
# NOTE: 실행을 위해서는 '가상의 제품 수요량.csv' 파일이 반드시 필요합니다.
FILE_PATH_PRODUCT = sys.argv[1] # product_demands.csv

OUTPUT_FILENAME = "result_demand.csv"

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

df = load_data(FILE_PATH_PRODUCT)

if df is None:
    print("error: 파일 로드 실패")
else:
    df['연월'] = pd.to_datetime(df['연월'])

    # 2. 예측 기간 설정
    # 2025년 11월부터 26개월 (2027년 12월까지)
    future_dates = pd.date_range(start='2025-11-01', periods=26, freq='MS')
    future_df = pd.DataFrame({'ds': future_dates})

    results = {}
    items = ['제육볶음','닭볶음탕','카레','야채수프']

    print("--- 예측 과정 시작 (Prophet) ---")

    # 3. Prophet 모델 학습 및 예측
    for item in items:
        df_item = df[['연월',f'{item}']].copy()
        df_item.columns = ['ds', 'y']

        # NOTE: Prophet 모델은 내부적으로 로그 변환 등을 수행할 수 있습니다.
        model = Prophet(yearly_seasonality=True)
        model.fit(df_item)

        forecast = model.predict(future_df)

        results[item] = forecast[['ds', 'yhat']].rename(columns={'yhat': f'{item}'})
        # yhat (예측값)을 반올림하여 정수로 변환
        results[item][f'{item}'] = results[item][f'{item}'].round().astype(int)

    print("--- 예측 과정 종료 ---")

    # 4. 모든 예측 결과를 하나의 DataFrame으로 결합
    final_forecast_df = future_df.copy()
    for item in results:
        final_forecast_df = pd.merge(final_forecast_df, results[item], on='ds', how='left')

    # 5. 결과 출력
    try:
        # 파일 저장 (선택 사항: 임시 폴더에 저장됨)
        # df_final.to_csv(OUTPUT_FILENAME, index=False, encoding='utf-8-sig')

        # [핵심] Java가 결과를 가져갈 수 있도록 CSV 포맷 문자열로 출력 (stdout)
        # index=False를 해야 '0, 2023-01...' 같은 인덱스 숫자가 안 붙음
        print(final_forecast_df.to_csv(index=False, encoding='utf-8-sig'))

    except Exception as e:
        print(f"error: {e}")