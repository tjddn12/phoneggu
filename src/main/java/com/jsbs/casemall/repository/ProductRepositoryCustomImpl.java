package com.jsbs.casemall.repository;

import com.jsbs.casemall.constant.ProductSellStatus;
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

    private BooleanExpression searchSellStatusEq(ProductSellStatus searchSellStatus){
        return searchSellStatus == null ? null : QProduct.product.productSellStatus.eq(searchSellStatus);
    }
    //판매상태를 검색 매개변수 searchSellStatus 가 null 검색조건을 적용하지 않는다.
    // 그렇지않으면   QItem.product.productSellStatus.eq(searchSellStatus) 를 반환하여
    // 해당 상태와 일치하는 상품을 검색

    private BooleanExpression regDtsAfter(String searchDateType) {

        LocalDateTime dateTime = LocalDateTime.now();

        if (StringUtils.equals("all", searchDateType) || searchDateType == null) {
            return null;
        } else if (StringUtils.equals("1d", searchDateType)) {
            dateTime = dateTime.minusDays(1); //1일이내
        } else if (StringUtils.equals("1w", searchDateType)) {
            dateTime = dateTime.minusWeeks(1); //일주일
        } else if (StringUtils.equals("1m", searchDateType)) {
            dateTime = dateTime.minusMonths(1);//한달
        } else if (StringUtils.equals("6m", searchDateType)) {
            dateTime = dateTime.minusMonths(6);//6개월
        }

        return QProduct.product.regTime.after(dateTime);
    }

    private BooleanExpression searchByLike(String searchBy, String searchQuery){
        if(StringUtils.equals("productNm", searchBy)){
            return QProduct.product.prName.like("%" + searchQuery + "%");
        }else if(StringUtils.equals("createBy", searchBy)){
            return QProduct.product.createdBy.like("%" + searchQuery + "%");
        }
        return null;
    }



    @Override
    public Page<Product> getAdminProductPage(ProductSearchDto productSearchDto, Pageable pageable) {
        List<Product> content = queryFactory
                .selectFrom(QProduct.product) //QItem.product을 기반으로 쿼리를 시작
                .where(regDtsAfter(productSearchDto.getSearchDateType()),
                        searchSellStatusEq(productSearchDto.getSearchSellStatus()),
                        searchByLike(productSearchDto.getSearchBy(),
                                productSearchDto.getSearchQuery()))
                .orderBy(QProduct.product.id.desc())// 아이템을 아이디 기준으로 내림차순
                .offset(pageable.getOffset())
                //페이지 번호와 페이지 크기를 고려하여 결과를 가져올 시작 오프셋을 설정합니다.
                .limit(pageable.getPageSize()) //: 한 페이지에 표시될 아이템 수를 제한합니다.
                .fetch();//쿼리를 실행하고 결과를 가져온다.
        long total = queryFactory.select(Wildcard.count).from(QProduct.product)
                .where(regDtsAfter(productSearchDto.getSearchDateType()),
                        searchSellStatusEq(productSearchDto.getSearchSellStatus()),
                        searchByLike(productSearchDto.getSearchBy(), productSearchDto.getSearchQuery()))
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }

    private BooleanExpression productPrNameLike(String searchQuery){
        return StringUtils.isEmpty(searchQuery) ? null : QProduct.product.prName.like("%" + searchQuery + "%");
    }

    //productSearchDto - 검색조건을 담고있는 DTO객체
    // pageable: 페이징 정보

    @Override
    public Page<MainProductDto> getMainProductPage(ProductSearchDto productSearchDto, Pageable pageable) {
        QProduct product = QProduct.product;
        QProductImg productImg = QProductImg.productImg;
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
                .where(productImg.prMainImg.eq("Y"))
                .where(productPrNameLike(productSearchDto.getSearchQuery()))
                .orderBy(product.id.desc())
                .offset(pageable.getOffset()) //가져올 데이터의 시작 오프셋
                .limit(pageable.getPageSize()) //한페이지당 가져올 데이터의 갯수
                .fetch();
        //페이지 크기가 5이고 현재페이지가 2이면 pageable.getOffset() 5를 반환
        // limit(pageable.getPageSize() 는 5를 반환하여 6번째부터 10번째가지 데이터를 가져옴

        long total = queryFactory
                .select(Wildcard.count)
                .from(productImg)
                .join(productImg.product, product)
                .where(productImg.prMainImg.eq("Y"))
                .where(productPrNameLike(productSearchDto.getSearchQuery()))
                .fetchOne()
                ;


        return  new PageImpl<>(content, pageable, total);
    }
}
