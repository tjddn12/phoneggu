package com.jsbs.casemall.repository;

import com.jsbs.casemall.dto.MainProductDto;
import com.jsbs.casemall.dto.ProductSearchDto;
import com.jsbs.casemall.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepositoryCustom {
    Page<Product> getAdminProductPage(ProductSearchDto productSearchDto, Pageable pageable);
    Page<MainProductDto> getMainProductPage(ProductSearchDto productSearchDto, Pageable pageable);
}
