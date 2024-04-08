package com.hello.forum.member.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;

import com.hello.forum.member.vo.MemberVO;

@MybatisTest // Mybatis framework 테스트를 위해 설정.
// 실제 Database에서 테스트 하기 위한 설정.
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
// MemberDaoImpl 테스트를 위해 MemberDaoImpl를 Import
@Import(MemberDaoImpl.class)
public class MemberDaoImplTest {
	
	@Autowired
	private MemberDao memberDao;
	
	
	@Test
	@DisplayName("이메일 개수 확인 테스트")
	public void getEmailCountTest() {
		
		int count = this.memberDao.getEmailCount("testuser@testuser.com");
		Assertions.assertEquals(count, 0);
		
		count = this.memberDao.getEmailCount("aaa@aaa.com");
		Assertions.assertEquals(count, 1);
		
	}
	
	@Test
	@DisplayName("회원가입 실패 테스트")
	public void creatNewMemberFailTest() {
		
		MemberVO memberVO = new MemberVO();
//		int insertedCount = this.memberDao.creatNewMember(memberVO);
		
		DataIntegrityViolationException exception = Assertions.assertThrows(DataIntegrityViolationException.class,
														() -> this.memberDao.creatNewMember(memberVO));
		
		Assertions.assertNotNull(exception);
	}
	
	@Test
	@DisplayName("회원가입 실패 테스트 - 중복된 이메일이 있을 경우")
	public void creatNewMemberFailDupTest() {
		
		MemberVO memberVO = new MemberVO();
		memberVO.setEmail("aaa@aaa.com");
		memberVO.setName("junit테스트");
		memberVO.setPassword("testpassword for junit");
		memberVO.setSalt("qwerqwerqwer");
		
		DuplicateKeyException exception = Assertions.assertThrows(DuplicateKeyException.class,
											() -> this.memberDao.creatNewMember(memberVO));
		
		Assertions.assertNotNull(exception);
	}
	
	@Test
	@DisplayName("회원가입 성공 테스트")
	public void creatNewMemberSuccessTest() {
		
		MemberVO memberVO = new MemberVO();
		memberVO.setEmail("aaa@google.com");
		memberVO.setName("junit테스트");
		memberVO.setPassword("testpassword for junit");
		memberVO.setSalt("qwerqwerqwer");
		
		int insertedCount = this.memberDao.creatNewMember(memberVO); // 롤백 된다.
		Assertions.assertEquals(insertedCount, 1);
	}
	
	
	// MemberDaoImpl 에 있는 나머지도 해보기

}






















