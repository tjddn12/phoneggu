package com.jsbs.casemall.dto;

import lombok.Data;
import org.springframework.web.util.UriComponentsBuilder;

//데이터 검색 및 페이징 처리를 위한 DTO
@Data
public class Criteria {
    private int currentPageNo; //: 현재 페이지 번호
    private int recordsPerPage; //: 페이지별 항목 수
    private String condition; //: 검색 조건
    private String keyword; //: 검색어

    public Criteria(){
        this(1, 10); //: 페이징 처리를 위한 초기값
    }
    public Criteria(int currentPageNo, int recordsPerPage){
        this.currentPageNo = currentPageNo;
        this.recordsPerPage = recordsPerPage;
    } //: 필드에 값을 전달
    public String getListLink(){
        UriComponentsBuilder builder = UriComponentsBuilder.fromPath("")
                .queryParam("currentPageNo", currentPageNo)
                .queryParam("recordsPerPage", recordsPerPage)
                .queryParam("category", this.getCondition())
                .queryParam("keyword", this.keyword);
        //: URI(Uniform Resource Identifier)를 동적으로 생성하는 스프링 프레임워크 클래스,
        //queryParam() 메서드로 사용하여 쿼리 파라미터 추가
        return builder.toUriString();
    }
}