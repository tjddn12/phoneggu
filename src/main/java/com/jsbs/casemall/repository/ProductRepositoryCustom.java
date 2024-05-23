package com.jsbs.casemall.repository;

import com.jsbs.casemall.dto.MainProductDto;
import com.jsbs.casemall.dto.ProductSearchDto;
import com.jsbs.casemall.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepositoryCustom {
    Page<Product> getAdminItemPage(ProductSearchDto productSearchDto, Pageable pageable);
    Page<MainProductDto> getMainItemPage(ProductSearchDto productSearchDto, Pageable pageable);
}
