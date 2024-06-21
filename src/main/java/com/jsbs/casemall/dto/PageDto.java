package com.jsbs.casemall.dto;

import lombok.Data;

import java.util.List;

@Data
public class PageDto<T> {
    private List<T> content;
    private int totalRecordCount;
    private int page;
    private int pageSize;
    private int startPage;
    private int lastPage;
    private int latest;
    private boolean hasPreviousPage;
    private boolean hasNextPage;
    private Criteria criteria;
    private ProductCriteria productCriteria;

    public PageDto(){}

    public PageDto(List<T> content, int totalRecordCount, int page, int pageSize){
        this.content = content;
        this.totalRecordCount = totalRecordCount;
        this.page = page > 0 ? page : 1;
        this.pageSize = pageSize > 0 ? pageSize : 10;

//        this.criteria = criteria;
//        this.lastPage = (int) (Math.ceil(criteria.getCurrentPageNo() * 1.0 / pageSize)) * pageSize;
//        //: ex. 현재 페이지 번호가 7, 페이지 크기가 5 -> 7 * 1.0 / 5 = 1.4 -> Math.ceil(1.4) = 2 -> (int) 2 * 5 = 10
//        //-> 해당 페이지 그룹의 마지막 페이지 번호: 10, ceil 내 매개변수가 double 형태임을 암시하기 위해 1.0을 곱함.
//        this.startPage = lastPage - (pageSize - 1);
//        this.latest = (int) (Math.ceil(totalRecordCount * 1.0 / criteria.getRecordsPerPage()));
//        //: 마지막 페이지 수(총 페이지 수) 계산
//        if(lastPage > latest){
//            this.lastPage = latest == 0 ? 1 : latest;
//            //: 마지막 페이지 수가 0인 경우 해당 그룹 마지막 페이지 번호를 1로 설정, 그렇지 않으면 그대로 설정
//        }
//
//        this.hasPreviousPage = startPage > 1;
//        this.hasNextPage = lastPage < latest;
    }
    //상품 페이징 처리용 메서드
    public PageDto(int totalRecordCount, int pageSize, ProductCriteria productCriteria){
        this.totalRecordCount = totalRecordCount;
        this.pageSize = pageSize;
        this.productCriteria = productCriteria;
        this.lastPage = (int) (Math.ceil(productCriteria.getPage() * 1.0 / pageSize)) * pageSize;
        this.startPage = lastPage - (pageSize - 1);
        this.latest = (int) (Math.ceil(totalRecordCount * 1.0 / productCriteria.getProducts()));

        if(lastPage > latest){
            this.lastPage = latest == 0 ? 1 : latest;
        }

        this.hasPreviousPage = startPage > 1;
        this.hasNextPage = lastPage < latest;
    }
} //: 실제 페이징 처리를 위해선 service와 레포지토리에 추가적인 메서드 구현 필요.