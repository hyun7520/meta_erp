package com.meta.stock.config;

import com.meta.stock.python.PythonService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PythonParser {
    @Autowired
    private PythonService pythonService;

    private Map<String, Map<String, List<String>>> content;
    private String projectRoot = System.getProperty("user.dir");

    @PostConstruct
    public void init() {
        pythonService.runLossPython(); // 로스율 저장
        readCsv();
    }

    public Map<String, Map<String, List<String>>> getLossContents() {
        return content;
    }

    public List<String> getLossDates() {
        return new ArrayList<>(content.keySet());
    }

    public Map<String, List<String>> getProductLossPerYearMonth(String yearMonth) {
        return content.get(yearMonth);
    }

    public int getLossPerProductAndYearMonth(String name, int qty) {
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMM");
        String todayYM = today.format(formatter);
        double lossRate = Double.parseDouble(content.get(todayYM).get(name).get(0)); // 로스율 %
        return (int) Math.ceil((double) qty * lossRate / 50);
    }

    // Loss & 습도 & 기후_분류 데이터 파일 읽어와서 Map에 저장
    private void readCsv() {
        content = new HashMap<>();
        try {
            readLossCsv(); // 로스율 연산 결과 저장된 CSV 읽기
            readPastWeatherCsv(); // 과거 날씨 데이터 Get
            readFutureWeatherCsv(); // 미래 날씨 데이터 Get
        } catch(FileNotFoundException e) {
            System.out.println("파일을 찾을 수 없었습니다.");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("파일 읽기 오류가 발생했습니다.");
            e.printStackTrace();
        }
    }

    private void readLossCsv() throws IOException {
        File csv = new File(projectRoot + "/src/main/resources/file/final_result.csv");
        BufferedReader br = null;
        String line = "";

        br = new BufferedReader(new FileReader(csv));
        // 첫 번째 줄(헤더)
        br.readLine();
        String[] headers = br.readLine().split(","); // 품목명
        while((line = br.readLine()) != null) {
            // CSV는 쉼표로 구분
            String[] cols = line.split(",");
            if(cols.length < headers.length) continue; // 데이터 검증
            String yearMonth = cols[0].trim();  // 연월

            for (int index = 1; index < headers.length; index++) {
                String itemName = headers[index];   // 품목명
                List<String> values = Arrays.asList(cols[index].trim(), "0", "0");

                content.computeIfAbsent(yearMonth, k -> new HashMap<>());
                content.get(yearMonth).put(itemName, values);
            }
        }

        try {
            br.close();
        } catch (IOException e) {
            System.out.println("파일 닫기 오류가 발생했습니다.");
        }
    }

    // 과거 값 insert
    private void readPastWeatherCsv() throws IOException {
        File past = new File(projectRoot + "/src/main/resources/file/past_material_date.csv");
        BufferedReader br = null;
        String line = "";
        int count = 0;
        br = new BufferedReader(new FileReader(past));
        // 첫 번째 줄(헤더) 건너뛰기
        br.readLine();
        while((line = br.readLine()) != null) {
            // CSV는 쉼표로 구분
            String[] cols = line.split(",");
            if (count > content.size()) break;
            if(cols.length < 7) continue; // 데이터 검증
            String yearMonth = cols[1].trim();  // 연월

            if (!content.containsKey(yearMonth)) continue;

            Map<String, List<String>> data = content.get(yearMonth);

            for (String itemName : data.keySet()) {
                List<String> values = data.get(itemName);
                values.set(1, cols[5].trim()); // 상대습도
                values.set(2, cols[9].trim()); // 기후_분류
                content.get(yearMonth).put(itemName, values);
            }
            count++;
        }

        try {
            br.close();
        } catch (IOException e) {
            System.out.println("파일 닫기 오류가 발생했습니다.");
        }
    }

    // 미래 값 insert
    private void readFutureWeatherCsv() throws IOException {
        File future = new File(projectRoot + "/src/main/resources/file/future_material_date.csv");
        BufferedReader br = null;
        String line = "";
        br = new BufferedReader(new FileReader(future));
        // 첫 번째 줄(헤더) 건너뛰기
        br.readLine();
        while((line = br.readLine()) != null) {
            // CSV는 쉼표로 구분
            String[] cols = line.split(",");
            String yearMonth = cols[0].trim();  // 연월

            if (!content.containsKey(yearMonth)) continue;

            Map<String, List<String>> data = content.get(yearMonth);

            for (String itemName : data.keySet()) {
                List<String> values = data.get(itemName);
                values.set(1, cols[3].trim()); // 상대습도
                values.set(2, cols[6].trim()); // 기후_분류
                content.get(yearMonth).put(itemName, values);
            }
        }

        try {
            br.close();
        } catch (IOException e) {
            System.out.println("파일 닫기 오류가 발생했습니다.");
        }
    }
}
