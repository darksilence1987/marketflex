package org.xhite.marketflex.service;

import java.util.List;
import java.util.Optional;

import org.xhite.marketflex.dto.ProductDto;

public interface ProductService {
    List<ProductDto> getAllProducts();
    List<ProductDto> getProductsByCategory(Long categoryId);
    Optional<ProductDto> getProductById(Long id);
    ProductDto createProduct(ProductDto productDto);
    ProductDto updateProduct(Long id, ProductDto productDto);
    void deleteProduct(Long id);
    List<ProductDto> getFeaturedProducts(int limit);
    public boolean isProductAvailable(Long id, int quantity);
    public void updateStock(Long id, int quantity);
}