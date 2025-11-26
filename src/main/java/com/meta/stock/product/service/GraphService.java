package com.meta.stock.product.service;

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
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class GraphService {
    // 해당 파일은 Graph들을 그리기 위한 Class 파일
    @Autowired
    private ProductMapper productMapper;

    private String DEFAULT_ROOT = "src/main/resources/static/file/";

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
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        list.sort(Comparator.comparing(ProductDemandBean::getRequestDate));
        return list;
    }

    public List<ProductLossBean> getLossPerHumidity() {
       List<FixedProductDto> fixedProducts = productMapper.getFixedProductWithStockQty();
       List<ProductLossBean> list = new ArrayList<>();

        YearMonth end = YearMonth.now();                 // 현재 년/월
        YearMonth start = end.minusYears(1);             // 현재로부터 5년 전

        YearMonth current = start;

        while (!current.isAfter(end)) {
            String ym = current.toString();              // "YYYY-MM"
            for (FixedProductDto fp : fixedProducts) {
                String caseName = fp.getName();

                float percent = (float) (5 + (Math.random() * 45));
                list.add(new ProductLossBean(caseName, "loss", ym, percent));
            }
            list.add(new ProductLossBean("습도", "humidity", ym, (float) (5 + (Math.random() * 45))));

            current = current.plusMonths(1);// 다음 달
        }
        return list;
    }
}
