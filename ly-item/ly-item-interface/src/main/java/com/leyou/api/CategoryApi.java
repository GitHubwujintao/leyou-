package com.leyou.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping("category")
public interface CategoryApi {

    @GetMapping("names")
    ResponseEntity<List<String>> queryNameByIds(@RequestParam("ids") List<Long> ids);
}