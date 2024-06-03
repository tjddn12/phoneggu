package com.jsbs.casemall.repository;

import com.jsbs.casemall.constant.ProductCategory;
import com.jsbs.casemall.constant.ProductSellStatus;
import com.jsbs.casemall.constant.ProductType;
import com.jsbs.casemall.dto.MainProductDto;
import com.jsbs.casemall.dto.ProductSearchDto;
import com.jsbs.casemall.dto.QMainProductDto;
import com.jsbs.casemall.entity.Product;
import com.jsbs.casemall.entity.QProduct;
import com.jsbs.casemall.entity.QProductImg;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

public class ProductRepositoryCustomImpl implements ProductRepositoryCustom{

    private JPAQueryFactory queryFactory;

    public ProductRepositoryCustomImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);
    }

    //판매 상태에 따라 검색 조건을 반환합니다.
    private BooleanExpression searchSellStatusEq(ProductSellStatus searchSellStatus) {
        // 검색 매개변수 searchSellStatus가 null이면 검색 조건을 적용하지 않음
        // 그렇지 않으면 QProduct.product.productSellStatus.eq(searchSellStatus)를 반환하여
        // 해당 상태와 일치하는 상품을 검색
        return searchSellStatus == null ? null : QProduct.product.productSellStatus.eq(searchSellStatus);
    }

    //카테고리에 따라 검색 조건을 반환합니다.
    private BooleanExpression searchCategoryEq(ProductCategory searchCategory) {
        return searchCategory == null ? null : QProduct.product.productCategory.eq(searchCategory);
    }

    //주어진 상품 종류에 따라 검색 조건을 반환합니다.
    private BooleanExpression searchPrTypeEq(ProductType searchPrType) {
        return searchPrType == null ? null : QProduct.product.productType.eq(searchPrType);
    }

    //날짜 유형에 따라 등록 날짜 기준으로 필터링 조건을 반환합니다.
    private BooleanExpression regDtsAfter(String searchDateType) {
        if (StringUtils.isEmpty(searchDateType)) {
            return null;
        }
        LocalDateTime dateTime = LocalDateTime.now();
        if ("1d".equals(searchDateType)) {
            dateTime = dateTime.minusDays(1);
        } else if ("1w".equals(searchDateType)) {
            dateTime = dateTime.minusWeeks(1);
        } else if ("1m".equals(searchDateType)) {
            dateTime = dateTime.minusMonths(1);
        } else if ("6m".equals(searchDateType)) {
            dateTime = dateTime.minusMonths(6);
        }
        return QProduct.product.regTime.after(dateTime);
    }

    //주어진 검색 기준과 검색어에 따라 필터링 조건을 반환합니다.
    // 검색 기준 ("prName" 또는 "createBy")
    private BooleanExpression searchByLike(String searchBy, String searchQuery) {
        // 검색 기준이 "prName"이면 상품 이름을 필터링
        if (StringUtils.equals("prName", searchBy)) {
            return QProduct.product.prName.like("%" + searchQuery + "%");
            // 검색 기준이 "createBy"이면 작성자를 필터링
        } else if (StringUtils.equals("createBy", searchBy)) {
            return QProduct.product.createdBy.like("%" + searchQuery + "%");
        }
        // 기준이 맞지 않으면 null 반환
        return null;
    }

    @Override
    public Page<Product> getAdminProductPage(ProductSearchDto productSearchDto, Pageable pageable) {
        // 검색 조건에 맞는 상품 목록을 가져옵니다.
        List<Product> content = queryFactory
                .selectFrom(QProduct.product) // QProduct.product 엔티티를 기준으로 쿼리를 시작합니다.
                .where(
                        regDtsAfter(productSearchDto.getSearchDateType()), // 등록 날짜 기준으로 필터링
                        searchSellStatusEq(productSearchDto.getSearchSellStatus()), // 판매 상태 기준으로 필터링
                        searchByLike(productSearchDto.getSearchBy(), productSearchDto.getSearchQuery()), // 검색 조건으로 필터링
                        searchCategoryEq(productSearchDto.getSearchCategory()), // 카테고리 기준으로 필터링
                        searchPrTypeEq(productSearchDto.getSearchPrType()) // 상품 종류 기준으로 필터링
                )
                .orderBy(QProduct.product.id.desc()) // ID 기준 내림차순 정렬
                .offset(pageable.getOffset()) // 결과의 시작 위치 설정
                .limit(pageable.getPageSize()) // 페이지 크기만큼 결과 제한
                .fetch(); // 쿼리 실행 및 결과 가져오기

        // 검색 조건에 맞는 총 상품 수를 계산합니다.
        long total = queryFactory
                .select(Wildcard.count) // 총 결과 수를 선택합니다.
                .from(QProduct.product) // QProduct.product 엔티티 기준
                .where(
                        regDtsAfter(productSearchDto.getSearchDateType()), // 등록 날짜 기준으로 필터링
                        searchSellStatusEq(productSearchDto.getSearchSellStatus()), // 판매 상태 기준으로 필터링
                        searchByLike(productSearchDto.getSearchBy(), productSearchDto.getSearchQuery()), // 검색 조건으로 필터링
                        searchCategoryEq(productSearchDto.getSearchCategory()), // 카테고리 기준으로 필터링
                        searchPrTypeEq(productSearchDto.getSearchPrType()) // 상품 종류 기준으로 필터링
                )
                .fetchOne(); // 카운트 쿼리 실행 및 결과 가져오기

        // 페이지 정보를 포함한 PageImpl 객체를 생성하여 반환합니다.
        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<MainProductDto> getMainProductPage(ProductSearchDto productSearchDto, Pageable pageable) {
        QProduct product = QProduct.product;
        QProductImg productImg = QProductImg.productImg;

        // 검색 조건에 맞는 MainProductDto 목록을 가져옵니다.
        List<MainProductDto> content = queryFactory
                .select(
                        new QMainProductDto(
                                product.id,
                                product.prName,
                                product.prDetail,
                                productImg.imgUrl,
                                product.prPrice
                        )
                )
                .from(productImg)
                .join(productImg.product, product)
                .where(productImg.prMainImg.eq("Y")) // 메인 이미지가 'Y'인 경우만 필터링
                .where(searchByLike(productSearchDto.getSearchBy(), productSearchDto.getSearchQuery())) // 검색 조건 필터링
                .orderBy(product.id.desc()) // ID 기준 내림차순 정렬
                .offset(pageable.getOffset()) // 가져올 데이터의 시작 오프셋 설정
                .limit(pageable.getPageSize()) // 한 페이지당 가져올 데이터 수 설정
                .fetch(); // 쿼리 실행 및 결과 가져오기

        // 검색 조건에 맞는 총 상품 수를 계산합니다.
        long total = queryFactory
                .select(Wildcard.count) // 총 결과 수를 선택합니다.
                .from(productImg)
                .join(productImg.product, product)
                .where(productImg.prMainImg.eq("Y")) // 메인 이미지가 'Y'인 경우만 필터링
                .where(searchByLike(productSearchDto.getSearchBy(), productSearchDto.getSearchQuery())) // 검색 조건 필터링
                .fetchOne(); // 카운트 쿼리 실행 및 결과 가져오기

        // 페이지 정보를 포함한 PageImpl 객체를 생성하여 반환합니다.
        return new PageImpl<>(content, pageable, total);
    }
}
