package com.meta.stock.product.controller;

import com.meta.stock.product.dto.ProductsAmountListBean;
import com.meta.stock.product.service.ProductsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

@Controller
public class DashController {
    @Autowired
    private ProductsService productsService;

    private final int limit = 5;

    @GetMapping("/")
    private String dashboard(
            Model model,
            @RequestParam(required = false) String column,
            @RequestParam(required = false) String search,
            @RequestParam(value = "start_date", required = false) String date,
            @RequestParam(defaultValue = "productId") String sort,
            @RequestParam(defaultValue = "1") int page
    ) {
        int offset = (page - 1) * limit;

        Map<String, Object> param = new HashMap<>();
        param.put("column", column);
        param.put("search", search);
        param.put("date", date);
        param.put("sort", sort);

        param.put("offset", offset);
        param.put("limit", limit);
        param.put("page", page);

        Page<ProductsAmountListBean> list = productsService.getList(param);

        model.addAttribute("products", list);
        return "dashboard/dashboard";
    }
}
