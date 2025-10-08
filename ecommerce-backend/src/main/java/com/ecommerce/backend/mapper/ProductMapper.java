package com.ecommerce.backend.mapper;

import com.ecommerce.backend.dto.request.ProductRequest;
import com.ecommerce.backend.dto.response.ProductResponse;
import com.ecommerce.backend.entity.Category;
import com.ecommerce.backend.entity.Product;
import com.ecommerce.backend.entity.ProductImage;
import com.ecommerce.backend.entity.ProductVariant;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductMapper {

    public Product toEntity(ProductRequest request) {
        if (request == null) {
            return null;
        }

        Product product = new Product();
        
        // Map basic fields
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setShortDescription(request.getShortDescription());
        product.setSku(request.getSku());
        product.setPrice(request.getPrice());
        product.setComparePrice(request.getComparePrice());
        product.setCostPrice(request.getCostPrice());
        product.setStockQuantity(request.getStockQuantity());
        product.setMinStockLevel(request.getMinStockLevel());
        product.setWeight(request.getWeight());
        product.setDimensions(request.getDimensions());
        product.setStatus(request.getStatus());
        product.setIsFeatured(request.getIsFeatured());
        product.setIsDigital(request.getIsDigital());
        product.setRequiresShipping(request.getRequiresShipping());
        product.setMetaTitle(request.getMetaTitle());
        product.setMetaDescription(request.getMetaDescription());
        product.setSlug(request.getSlug());
        
        // Map category
        if (request.getCategoryId() != null) {
            Category category = new Category();
            category.setId(request.getCategoryId());
            product.setCategory(category);
        }
        
        return product;
    }

    public ProductResponse toResponse(Product product) {
        if (product == null) {
            return null;
        }

        ProductResponse response = new ProductResponse();
        
        // Map basic fields
        response.setId(product.getId());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setShortDescription(product.getShortDescription());
        response.setSku(product.getSku());
        response.setPrice(product.getPrice());
        response.setComparePrice(product.getComparePrice());
        response.setCostPrice(product.getCostPrice());
        response.setStockQuantity(product.getStockQuantity());
        response.setMinStockLevel(product.getMinStockLevel());
        response.setWeight(product.getWeight());
        response.setDimensions(product.getDimensions());
        response.setStatus(product.getStatus());
        response.setIsFeatured(product.getIsFeatured());
        response.setIsDigital(product.getIsDigital());
        response.setRequiresShipping(product.getRequiresShipping());
        response.setMetaTitle(product.getMetaTitle());
        response.setMetaDescription(product.getMetaDescription());
        response.setSlug(product.getSlug());
        response.setCreatedAt(product.getCreatedAt());
        response.setUpdatedAt(product.getUpdatedAt());
        
        // Map calculated fields
        response.setIsInStock(product.isInStock());
        response.setIsLowStock(product.isLowStock());
        response.setIsAvailable(product.isAvailable());
        response.setDiscountAmount(product.getDiscountAmount());
        response.setDiscountPercentage(product.getDiscountPercentage());
        
        // Map category
        if (product.getCategory() != null) {
            response.setCategory(toCategoryResponse(product.getCategory()));
        }
        
        // Map images
        if (product.getImages() != null) {
            response.setImages(product.getImages().stream()
                    .map(this::toImageResponse)
                    .collect(Collectors.toList()));
        }
        
        // Map variants
        if (product.getVariants() != null) {
            response.setVariants(product.getVariants().stream()
                    .map(this::toVariantResponse)
                    .collect(Collectors.toList()));
        }
        
        return response;
    }

    public ProductResponse.ProductVariantResponse toVariantResponse(ProductVariant variant) {
        if (variant == null) {
            return null;
        }

        ProductResponse.ProductVariantResponse response = new ProductResponse.ProductVariantResponse();
        response.setId(variant.getId());
        response.setName(variant.getName());
        response.setSku(variant.getSku());
        response.setPrice(variant.getPrice());
        response.setComparePrice(variant.getComparePrice());
        response.setStockQuantity(variant.getStockQuantity());
        response.setImageUrl(variant.getImageUrl());
        response.setIsActive(variant.getIsActive());
        response.setAttribute1Name(variant.getAttribute1Name());
        response.setAttribute1Value(variant.getAttribute1Value());
        response.setAttribute2Name(variant.getAttribute2Name());
        response.setAttribute2Value(variant.getAttribute2Value());
        response.setAttribute3Name(variant.getAttribute3Name());
        response.setAttribute3Value(variant.getAttribute3Value());
        response.setIsInStock(variant.isInStock());
        response.setIsAvailable(variant.isAvailable());
        response.setDisplayName(variant.getDisplayName());
        
        return response;
    }

    public ProductResponse.ProductImageResponse toImageResponse(ProductImage image) {
        if (image == null) {
            return null;
        }

        ProductResponse.ProductImageResponse response = new ProductResponse.ProductImageResponse();
        response.setId(image.getId());
        response.setImageUrl(image.getImageUrl());
        response.setAltText(image.getAltText());
        response.setIsPrimary(image.getIsPrimary());
        response.setSortOrder(image.getSortOrder());
        
        return response;
    }

    public ProductResponse.CategoryResponse toCategoryResponse(Category category) {
        if (category == null) {
            return null;
        }

        ProductResponse.CategoryResponse response = new ProductResponse.CategoryResponse();
        response.setId(category.getId());
        response.setName(category.getName());
        response.setSlug(category.getSlug());
        response.setImageUrl(category.getImageUrl());
        
        return response;
    }
}