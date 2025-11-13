package com.cursor.product.repository;

import com.cursor.product.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for Product entity.
 * <p>
 * This interface extends JpaRepository to provide CRUD operations
 * and standard query methods for Product entities.
 * </p>
 *
 * @author Cursor Product Team
 * @version 1.0.0
 * @since 1.0.0
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}

