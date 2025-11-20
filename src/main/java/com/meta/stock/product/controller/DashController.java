package com.meta.stock.product.controller;

import com.meta.stock.materials.dto.MaterialCountsBean;
import com.meta.stock.materials.service.MaterialService;
import com.meta.stock.order.dto.DashFlowBean;
import com.meta.stock.order.service.DashService;
import com.meta.stock.product.dto.ProductDemandBean;
import com.meta.stock.product.dto.ProductsAmountListBean;
import com.meta.stock.product.service.GraphService;
import com.meta.stock.product.service.ProductsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class DashController {
    @Autowired
    private ProductsService productsService;
    @Autowired
    private MaterialService materialService;
    @Autowired
    private GraphService graphService; // graph 값 입력 service 생성시 대체 예정
    @Autowired
    private DashService dashService;

    private final int limit = 5;

    @GetMapping("/")
    private String dashboard(
//            Model model
//            @RequestParam(required = false) String column,
//            @RequestParam(required = false) String search,
//            @RequestParam(value = "start_date", required = false) String date,
//            @RequestParam(defaultValue = "productId") String sort,
//            @RequestParam(defaultValue = "1") int page
    ) {
//        int offset = (page - 1) * limit;
//
//        Map<String, Object> param = new HashMap<>();
//        param.put("column", column);
//        param.put("search", search);
//        param.put("date", date);
//        param.put("sort", sort);
//
//        param.put("offset", offset);
//        param.put("limit", limit);
//        param.put("page", page);
//
//        List<ProductsAmountListBean> list = productsService.getDashTable(param);
//        int totalCount = productsService.getDashTableTotal(param);
//        int totalPage = (int) Math.ceil((double) totalCount / limit);
//
//        model.addAttribute("products", list);
//        model.addAttribute("page", page);
//        model.addAttribute("totalPage", totalPage);
        return "dashboard/dashboard";
    }

    @GetMapping("/dash/table")
    private ResponseEntity<Map<String, Object>> dashTable(
            @RequestParam(required = false) String column,
            @RequestParam(required = false) String search,
            @RequestParam(value = "start_date", required = false) String date,
            @RequestParam(defaultValue = "product_id") String sort,
            @RequestParam(defaultValue = "1") int page
    ) {
        int offset = (page - 1) * limit;

        Map<String, Object> param = new HashMap<>();
        param.put("column", column);
        param.put("search", search);
        param.put("date", date);
        param.put("sort", sort);

        param.put("page", page);
        param.put("limit", limit);
        param.put("offset", offset);

        Map<String, Object> pageData = new HashMap<>();

        List<ProductsAmountListBean> list = productsService.getDashTable(param);
        int totalCount = productsService.getDashTableTotal(param);
        int totalPage = (int) Math.ceil((double) totalCount / limit);

        pageData.put("list", list);
        pageData.put("page", page);
        pageData.put("totalPage", totalPage);

        pageData.put("column", column);
        pageData.put("search", search);
        pageData.put("date", date);
        pageData.put("sort", sort);

        return ResponseEntity.ok(pageData);
    }

    @GetMapping("/dash/flow")
    public ResponseEntity<DashFlowBean> dashFlow() {
        return ResponseEntity.ok(dashService.getDashFlowBean());
    }

    @GetMapping("/dash/materials")
    @ResponseBody
    public ResponseEntity<List<MaterialCountsBean>> materialCounts(@RequestParam(required = false) String serialCode) {
        return ResponseEntity.ok(materialService.getDateMaterialTotals(serialCode));
    }

    @GetMapping("/dash/demands")
    @ResponseBody
    public ResponseEntity<List<ProductDemandBean>> productDemands() {
        return ResponseEntity.ok(graphService.getList());
    }
}
