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
        List<FixedProductDto> fixedProducts = productMapper.getFixedProductWithStockQty(); // 각 제품 종류 출력
        Map<String, Map<String, String>> demands = parser.getDemandsContents();

        List<ProductDemandBean> list = new ArrayList<>();

        for (String date : demands.keySet()) {
            Map<String, String> demand = demands.get(date);
            String parseDate = date.substring(0, 4) + "-" + date.substring(5, 7);
            for (FixedProductDto fp : fixedProducts) {
                float demandAmount = Float.parseFloat(demand.get(fp.getName()));
                list.add(new ProductDemandBean(fp.getName(), parseDate, demandAmount));
            }
        }
        list.sort(Comparator.comparing(ProductDemandBean::getRequestDate));

        // 5년보다 더 전일 경우 skip
        List<ProductDemandBean> result = new ArrayList<>();
        LocalDate startDate = LocalDate.now().minusYears(5);
        String startKey = DateTimeFormatter.ofPattern("yyyy-MM").format(startDate);
        Boolean isSkip = true;
        for (ProductDemandBean bean : list) {
            if (bean.getRequestDate().equals(startKey)) isSkip = false;
            if (isSkip) continue;

            result.add(bean);
        }

        return result;
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
}
