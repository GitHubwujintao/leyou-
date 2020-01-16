package com.leyou.api;

import com.sun.javafx.collections.MappingChange;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("category")
public interface CategoryApi {

    @GetMapping("names")
    List<String> queryNameByCids(@RequestParam("ids") List<Long> ids);
}