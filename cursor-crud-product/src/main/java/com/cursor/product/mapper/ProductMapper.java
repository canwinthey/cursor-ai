package com.cursor.product.mapper;

import com.cursor.product.domain.ProductDto;
import com.cursor.product.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * Mapper interface for converting between Product entity and ProductDto.
 * <p>
 * This interface uses MapStruct to generate implementation code for mapping
 * between the domain model (Product) and the DTO (ProductDto).
 * </p>
 *
 * @author Cursor Product Team
 * @version 1.0.0
 * @since 1.0.0
 */
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProductMapper {

    /**
     * Converts a Product entity to a ProductDto.
     *
     * @param product the Product entity to convert
     * @return the converted ProductDto
     */
    ProductDto toDto(Product product);

    /**
     * Converts a ProductDto to a Product entity.
     *
     * @param productDto the ProductDto to convert
     * @return the converted Product entity
     */
    Product toEntity(ProductDto productDto);

    /**
     * Updates an existing Product entity with data from ProductDto.
     * Null values in the DTO are ignored during update.
     *
     * @param productDto the ProductDto containing updated data
     * @param product the Product entity to update (modified in place)
     */
    void updateEntity(ProductDto productDto, @MappingTarget Product product);
}

