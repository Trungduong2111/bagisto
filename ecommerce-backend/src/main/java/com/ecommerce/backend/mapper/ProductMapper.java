package com.ecommerce.backend.mapper;

import com.ecommerce.backend.dto.request.ProductRequest;
import com.ecommerce.backend.dto.response.ProductResponse;
import com.ecommerce.backend.entity.Category;
import com.ecommerce.backend.entity.Product;
import com.ecommerce.backend.entity.ProductImage;
import com.ecommerce.backend.entity.ProductVariant;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "category", source = "categoryId", qualifiedByName = "categoryIdToCategory")
    @Mapping(target = "images", ignore = true)
    @Mapping(target = "variants", ignore = true)
    @Mapping(target = "reviews", ignore = true)
    @Mapping(target = "cartItems", ignore = true)
    Product toEntity(ProductRequest request);

    @Mapping(target = "isInStock", expression = "java(product.isInStock())")
    @Mapping(target = "isLowStock", expression = "java(product.isLowStock())")
    @Mapping(target = "isAvailable", expression = "java(product.isAvailable())")
    @Mapping(target = "discountAmount", expression = "java(product.getDiscountAmount())")
    @Mapping(target = "discountPercentage", expression = "java(product.getDiscountPercentage())")
    ProductResponse toResponse(Product product);

    @Mapping(target = "isInStock", expression = "java(variant.isInStock())")
    @Mapping(target = "isAvailable", expression = "java(variant.isAvailable())")
    @Mapping(target = "displayName", expression = "java(variant.getDisplayName())")
    ProductResponse.ProductVariantResponse toVariantResponse(ProductVariant variant);

    ProductResponse.ProductImageResponse toImageResponse(ProductImage image);

    @Mapping(target = "id", source = "category.id")
    @Mapping(target = "name", source = "category.name")
    @Mapping(target = "slug", source = "category.slug")
    @Mapping(target = "imageUrl", source = "category.imageUrl")
    ProductResponse.CategoryResponse toCategoryResponse(Category category);

    @Named("categoryIdToCategory")
    default Category categoryIdToCategory(Long categoryId) {
        if (categoryId == null) {
            return null;
        }
        Category category = new Category();
        category.setId(categoryId);
        return category;
    }
}