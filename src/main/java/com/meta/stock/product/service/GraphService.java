package com.meta.stock.product.service;

import com.meta.stock.product.dto.ProductDemandBean;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class GraphService {
    // 해당 파일은 Graph들을 그리기 위한 임시 데이터 CSV 파일들을 parse 하기 위한 Class 파일
    private String DEFAULT_ROOT = "C:\\Users\\saiiy\\Downloads\\";

    public List<ProductDemandBean> getList() {
        List<ProductDemandBean> list = new ArrayList<>();
        File file = new File(DEFAULT_ROOT + "demand_pivot.csv");
        BufferedReader br;
        String line;

        try {
            br = new BufferedReader(new FileReader(file));
            String[] dates = br.readLine().split(",");
            while ((line = br.readLine()) != null) {
                String[] arr = line.split(",");
                String product = arr[0].replace("\uFEFF", "").trim();

                for (int idx = 1; idx < arr.length; idx++) {
                    list.add(new ProductDemandBean(product, dates[idx].trim(), Float.parseFloat(arr[idx])));
                }
            }
            br.close(); // 한번 끊기
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        list.sort(Comparator.comparing(ProductDemandBean::getDate));
        return list;
    }
}
