package com.hello.forum.member.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.hello.forum.beans.SHA;
import com.hello.forum.exceptions.AlreadyUseException;
import com.hello.forum.exceptions.UserIdentifyNotMatchException;
import com.hello.forum.member.dao.MemberDao;
import com.hello.forum.member.dao.MemberDaoImpl;
import com.hello.forum.member.vo.MemberVO;

@SpringBootTest // jUnit 테스트를 위해 필요한 Bean을 가져오기 위한 에노테이션
@ExtendWith(SpringExtension.class) // Spring Test를 위해서 JUnit5를 사용하기 위한 설정.
// MemberServiceImpl을 테스트 하기 위해 Import
// MemberServiceImpl이 동작하기 위해 MemberDaoImpl을 Import
// MemberServiceImpl이 동작하기 위해 SHA를 Import
@Import({MemberServiceImpl.class, MemberDaoImpl.class, SHA.class})
public class MemberServiceImplTest {
	
	/**
	 * Import 해온 MemberServiceImpl을 주입한다.
	 */
	@Autowired
	private MemberService memberService;
	
	/**
	 * MemberServiceImpl에 DI 해주기 위한 MemberDao 선언
	 */
	@MockBean // 위 MemberService 테스트 하기위해 필요한 MemberDao를 임시?가짜? 객체
	private MemberDao memberDao;
	
	/**
	 * MemberServiceImpl에 DI 해주기 위한 SHA 선언
	 */
//	@MockBean
//	private SHA sha;
	
	@Test
	@DisplayName("회원 ID 중복체크 테스트") // ()에는 test의 설명
	public void checkAvailableEmailTest() {
		
		// MemberServiceImpl의 checkAvailableEmail(email) 함수를 테스트 하기 위해서
		// MemberDaoImpl의 getEmailCount(email) 가 동작해야하는 방법을 작성한다.
		
		// 1. Given
		BDDMockito.given(this.memberDao.getEmailCount("user01@gmail.com")).willReturn(0);
		
		// 2. Given
		BDDMockito.given(this.memberDao.getEmailCount("user02@gmail.com")).willReturn(1);
		
		// 3. when
		boolean isAvailableEmail = this.memberService.checkAvailableEmail("user01@gmail.com"); // true
		
		// 4. then
		// isAvailableEmail의 값이 true 면 성공!
		// 아니라면 실패!
		Assertions.assertTrue(isAvailableEmail);
		
		// 5. when -  user02도 같은 방법으로 테스트
		isAvailableEmail = this.memberService.checkAvailableEmail("user02@gmail.com");
		
		Assertions.assertFalse(isAvailableEmail);
		
		// 6. Verify (검증)
		Mockito.verify(this.memberDao).getEmailCount("user01@gmail.com");
		Mockito.verify(this.memberDao).getEmailCount("user02@gmail.com");
		
	}
	
	@Test
	@DisplayName("회원가입 실패 테스트")
	public void createNewMemberFailTest() {
		// 1. Given
		BDDMockito.given(this.memberDao.getEmailCount("user01@gmail.com")).willReturn(1);
		
		// 2. when
		MemberVO memberVO = new MemberVO();
		memberVO.setEmail("user01@gmail.com");
		
		// 작성된 테스트 코드는 AlreadyUseException이 발생해야한다!
//		boolean isSuccess = this.memberService.createNewMember(memberVO); // 이것보다는 아래처럼
		AlreadyUseException exception = Assertions.assertThrows(AlreadyUseException.class, () -> 
			this.memberService.createNewMember(memberVO)); // 예외가 발생 해야 true이다 라는 코드
		
		// 3. then
		// 예상되는 예외 메세지와 실제 발생된 예외의 메세지가 같은지 비교.
		String message = "이미 사용중인 이메일입니다.";
		Assertions.assertEquals(message, exception.getMessage());
		
		// 4. Verify
		Mockito.verify(this.memberDao).getEmailCount("user01@gmail.com");
	}
	
	@Test
	@DisplayName("회원가입 성공 테스트")
	public void createNewMemberSuccessTest() {
		
		// 1. Given
		BDDMockito.given(this.memberDao.getEmailCount("user01@gmail.com")).willReturn(0);
		
		MemberVO memberVO = new MemberVO();
		memberVO.setEmail("user01@gmail.com");
		memberVO.setName("테스트 사용자");
		memberVO.setPassword("testpassword");
		memberVO.setConfirmPassword("testpassword");
		
		BDDMockito.given(this.memberDao.creatNewMember(memberVO)).willReturn(1);
		
		// 2. when
		boolean isSuccess = this.memberService.createNewMember(memberVO);
		// isSuccess ==> true;
		
		// 3. then
		Assertions.assertTrue(isSuccess); // 회원가입의 결과가 true인지 검증.
		Assertions.assertNotNull(memberVO.getSalt()); // 비밀번호 암호화를 위한 salt가 생성되었는지 검증.
		
		// 비밀번호가 암호화되어 getConfirmPassword와 다른지 검증.
		Assertions.assertNotEquals(memberVO.getPassword(), memberVO.getConfirmPassword()); 
		
		// 4. Verify
		Mockito.verify(this.memberDao).getEmailCount("user01@gmail.com");
		Mockito.verify(this.memberDao).creatNewMember(memberVO);
		
	}
	
	
	@Test
	@DisplayName("회원가입 실패 테스트 1 - salt 없는 케이스")
	public void getMemberFailTest() {
		
		// 1. Given
		BDDMockito.given(this.memberDao.selectSalt("user01@gmail.com")).willReturn(null);
		
		// 2. when
		MemberVO memberVO = new MemberVO();
		memberVO.setEmail("user01@gmail.com");
		memberVO.setPassword("testpassword");
		
		UserIdentifyNotMatchException exception = Assertions.assertThrows(UserIdentifyNotMatchException.class,
																	() -> this.memberService.getMember(memberVO));
		// 3. then
		String message = "";
		Assertions.assertEquals(message, exception.getMessage());
		
		// 4. Verify
		Mockito.verify(this.memberDao).selectSalt("user01@gmail.com");
	}
	
//	@Test
//	@DisplayName("회원가입 실패 테스트 2 - 비밀번호 이메일 비교 실패 테스트")
//	public void getMemberFailTest2() {
//		// 1. Given
//		BDDMockito.given(this.memberDao.selectSalt("user01@gmail.com")).willReturn(" ");
//		
//		// 2. when
//		MemberVO memberVO = new MemberVO();
//		this.memberDao.selectMemberByEmailAndPassword(memberVO);
//		
//		// 3. then
//		
//		
//		// 4. Verify
//		
//	}
	
	
	@Test
	@DisplayName("회원가입 성공 테스트")
	public void getMemberSuccessTest() {
		
		// 1. Given
		BDDMockito.given(this.memberDao.selectSalt("user01@gmail.com")).willReturn("abcdefg");
		
		MemberVO memberVO = new MemberVO();
		memberVO.setEmail("user01@gmail.com");
		memberVO.setPassword("testpassword");
		
		BDDMockito.given(this.memberDao.selectMemberByEmailAndPassword(memberVO)).willReturn(memberVO);
		
		// 2. when
		MemberVO loginMemberVO = this.memberService.getMember(memberVO);
			
		// 3. then
		Assertions.assertNotNull(loginMemberVO);
		Assertions.assertEquals(memberVO.getEmail(), loginMemberVO.getEmail());
		Assertions.assertNotEquals(loginMemberVO.getPassword(), "testpassword");
		
		// 4. Verify
		Mockito.verify(this.memberDao).selectSalt("user01@gmail.com");
		Mockito.verify(this.memberDao).selectMemberByEmailAndPassword(loginMemberVO);
	}
	
	@Test
	@DisplayName("탈퇴 실패 테스트")
	public void deleteFailMe() {
		
		// 1. Given
		BDDMockito.given(this.memberDao.deleteMemberByEmail("user01@gmail.com")).willReturn(0);
		
		// 2. when
		boolean isSuccess = this.memberService.deleteMe("user01@gmail.com");
		
		// 3. then
		Assertions.assertFalse(isSuccess);
		
		// 4. Verify
		Mockito.verify(this.memberDao).deleteMemberByEmail("user01@gmail.com");
	}
	
	@Test
	@DisplayName("탈퇴 성공 테스트")
	public void deleteSuccessMe() {
		
		// 1. Given
		BDDMockito.given(this.memberDao.deleteMemberByEmail("user01@gmail.com")).willReturn(1);
				
		// 2. when
		boolean isSuccess = this.memberService.deleteMe("user01@gmail.com");
		
		// 3. then
		Assertions.assertTrue(isSuccess);
		
		// 4. Verify
		Mockito.verify(this.memberDao).deleteMemberByEmail("user01@gmail.com");
		
	}
}




























