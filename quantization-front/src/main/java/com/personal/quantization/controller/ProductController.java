package com.personal.quantization.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.personal.quantization.model.QuantizationResult;


@RestController
@RequestMapping(value = "/quantization/product")
public class ProductController {

	@RequestMapping(value = "/list", method = RequestMethod.GET)
//    @PreAuthorize("hasAuthority('product')")
    public QuantizationResult list(Principal principal) {
        List<String> list = new ArrayList<>();
        list.add("眼镜");
        list.add("格子衬衣");
        list.add("双肩包");
        list.add(principal.getName());
        return QuantizationResult.ok(list);
    }

}
