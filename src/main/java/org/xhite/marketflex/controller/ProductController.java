package org.xhite.marketflex.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.xhite.marketflex.dto.ProductDto;
import org.xhite.marketflex.service.CategoryService;
import org.xhite.marketflex.service.ProductService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private static final String PRODUCT_ATTRIBUTE = "product";
    private final ProductService productService;
    private final CategoryService categoryService;
    
    @GetMapping
    public String listProducts(@RequestParam(required = false) Long category, Model model) {
        List<ProductDto> products;
        if (category != null) {
            products = productService.getProductsByCategory(category);
        } else {
            products = productService.getAllProducts();
        }
        model.addAttribute("products", products);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "product/list";
    }
    @GetMapping("/{id}")
    public String getProduct(@PathVariable Long id, Model model) {
        productService.getProductById(id).ifPresent(product -> model.addAttribute(PRODUCT_ATTRIBUTE, product));
        return "product/detail";
    }
    
    
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/create")
    public String createProductForm(Model model) {
        model.addAttribute("product", new ProductDto());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "product/form";
    }
    
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping
    public String createProduct(@Valid @ModelAttribute("product") ProductDto productDto, 
                              BindingResult result, 
                              Model model) {
        if (result.hasErrors()) {
            model.addAttribute("categories", categoryService.getAllCategories());
            return "product/form";
        }
        productService.createProduct(productDto);
        return "redirect:/products";
    }
    
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/{id}/edit")
    public String editProductForm(@PathVariable("id") Long id, Model model) {
        productService.getProductById(id).ifPresent(product -> {
            model.addAttribute("product", product);
            model.addAttribute("categories", categoryService.getAllCategories());
        });
        return "product/form";
    }
    
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping("/{id}")
    public String updateProduct(@PathVariable("id") Long id, 
                              @Valid @ModelAttribute("product") ProductDto productDto,
                              BindingResult result,
                              Model model) {
        if (result.hasErrors()) {
            model.addAttribute("categories", categoryService.getAllCategories());
            return "product/form";
        }
        productService.updateProduct(id, productDto);
        return "redirect:/products";
    }
    
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable("id") Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok().build();
    }

}