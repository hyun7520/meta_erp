package com.meta.stock.product.service;

import com.meta.stock.config.PythonParser;
import com.meta.stock.product.dto.FixedProductDto;
import com.meta.stock.product.dto.ProductDemandBean;
import com.meta.stock.product.dto.ProductLossBean;
import com.meta.stock.product.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
public class GraphService {
    // 해당 파일은 Graph들을 그리기 위한 Class 파일
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private PythonParser parser;

    private String DEFAULT_ROOT = "src/main/resources/static/file/";

    public Map<String, String> getNotification() {
        LocalDate today = LocalDate.now();
        String date = DateTimeFormatter.ofPattern("yyyyMM").format(today);
        String day = today.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.KOREAN);

        List<String> weather = parser.getProductLossPerYearMonth(date).get("감자"); // 기본 데이터 get
        Map<String, String> result = new HashMap<>();
        result.put("date", DateTimeFormatter.ofPattern("yyyy-MM-dd ").format(today) + day);
        result.put("humidity", weather.get(1));
        result.put("status", weather.get(2));

        return result;
    }

    public List<ProductDemandBean> getList() {
        List<ProductDemandBean> list = new ArrayList<>();
        File file = new File(DEFAULT_ROOT + "가상의 제품 수요량.csv");
        BufferedReader br;

        try {
            br = new BufferedReader(new FileReader(file));
            String line = br.readLine();
            String[] products = line.split(",");
            while ((line = br.readLine()) != null) {
                String[] arr = line.split(",");
                String date = arr[0].replace("\uFEFF", "").trim();

                for (int idx = 1; idx < arr.length; idx++) {
                    list.add(new ProductDemandBean(products[idx], date, Float.parseFloat(arr[idx])));
                }
            }
            br.close(); // 한번 끊기

            Map<String, Map<String, Integer>> map = getSampleData();
            for (String key : map.keySet()) {
                Map<String, Integer> data = map.get(key);
                for (String date : data.keySet()) {
                    list.add(new ProductDemandBean(key, date, data.get(date)));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        list.sort(Comparator.comparing(ProductDemandBean::getRequestDate));
        return list;
    }

    public List<ProductLossBean> getLossPerHumidity() {
        List<FixedProductDto> fixedProducts = productMapper.getFixedProductWithStockQty(); // 각 제품 종류 출력
        List<String> dates = parser.getLossDates();
        List<ProductLossBean> list = new ArrayList<>();

        Collections.sort(dates);

        for (String date : dates) {
            Map<String, List<String>> data = parser.getProductLossPerYearMonth(date);
            String parseDate = date.substring(0, 4) + "-" + date.substring(4);
            for (FixedProductDto fp : fixedProducts) {
                float lossPercent = Float.parseFloat(data.get(fp.getName()).get(0));
                list.add(new ProductLossBean(fp.getName(), "loss", parseDate, lossPercent));
            }
            float humidityPercent = Float.parseFloat(data.get(fixedProducts.get(0).getName()).get(1)); // 어느 값이나 모두 같기에 일괄 저장
            list.add(new ProductLossBean("습도", "humidity", parseDate, humidityPercent));
        }
        return list;
    }

    private static Map<String, Map<String, Integer>> getSampleData() {
        List<String> months = Arrays.asList(
                "2025.11", "2025.12", "2026.01", "2026.02", "2026.03", "2026.04",
                "2026.05", "2026.06", "2026.07", "2026.08", "2026.09", "2026.10",
                "2026.11", "2026.12", "2027.01", "2027.02", "2027.03", "2027.04",
                "2027.05", "2027.06", "2027.07", "2027.08", "2027.09", "2027.10",
                "2027.11", "2027.12"
        );

        Map<String, List<Integer>> echartsData = new LinkedHashMap<>();
        echartsData.put("김치찌개", Arrays.asList(1200,1350,1400,1300,1150,1100,1050,1000,1080,1150,1250,1450,1500,1600,1650,1550,1400,1300,1250,1200,1300,1380,1500,1700,1750,1850));
        echartsData.put("된장찌개", Arrays.asList(1000,1050,1100,1150,1200,1250,1300,1350,1300,1250,1200,1150,1100,1150,1200,1250,1300,1350,1400,1450,1400,1350,1300,1250,1200,1150));
        echartsData.put("부대찌개", Arrays.asList(1100,1150,1200,1250,1200,1150,1100,1050,1100,1150,1200,1250,1300,1350,1400,1450,1400,1350,1300,1250,1300,1350,1400,1450,1500,1550));
        echartsData.put("닭볶음탕", Arrays.asList(950,980,1050,1100,1150,1200,1250,1300,1350,1400,1450,1500,1550,1600,1650,1700,1750,1800,1750,1700,1650,1600,1550,1500,1450,1400));

        Map<String, Map<String, Integer>> result = new LinkedHashMap<>();

        for (String product : echartsData.keySet()) {
            Map<String, Integer> dateMap = new LinkedHashMap<>();
            List<Integer> demands = echartsData.get(product);

            for (int i = 0; i < months.size(); i++) {
                String month = months.get(i).replace(".", "-"); // "2026.01" → "2026-01"
                Integer demand = demands.get(i);
                dateMap.put(month, demand);
            }

            result.put(product, dateMap);
        }

        return result;
    }
}
