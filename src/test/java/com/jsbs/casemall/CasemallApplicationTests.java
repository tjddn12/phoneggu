package com.jsbs.casemall;

import com.jsbs.casemall.service.ArticleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CasemallApplicationTests {

	@Autowired
	private ArticleService articleService;

	@Test
	void testJpa(){
		for(int i = 1; i < 301; i++){
			String subject = String.format("테스트 데이터입니다.:[%03d]", i);
			String content = "흠좀무";

			this.articleService.creation(subject, content);
		}
	}
	@Test
	void contextLoads() {
	}

}
