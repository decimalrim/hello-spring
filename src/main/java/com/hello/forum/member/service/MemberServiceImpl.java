package com.hello.forum.member.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.hello.forum.beans.SHA;
import com.hello.forum.member.dao.MemberDao;
import com.hello.forum.member.vo.MemberVO;
import com.hello.forum.utils.StringUtils;

/*
 * 회원가입 업무 로직 작성
 */
@Service
public class MemberServiceImpl implements MemberService {
	
	@Autowired
	private SHA sha;
	
	@Autowired
	private MemberDao memberDao;

	@Override
	public boolean createNewMember(MemberVO memberVO) {
		int emailCount = memberDao.getEmailCount(memberVO.getEmail());
		
		if(emailCount > 0) {
			throw new IllegalArgumentException("Email이 이미 사용중입니다.");
		}
		
		String password = memberVO.getPassword();
		String salt = this.sha.generateSalt();
		password = this.sha.getEncrypt(password, salt);
		
		memberVO.setPassword(password);
		memberVO.setSalt(salt);
		
		int insertCount = memberDao.creatNewMember(memberVO);
		return insertCount > 0;
	}
	
	// 해당 이메일이 한건도 없다면 == 0 사용가능.
	@Override
	public boolean checkAvailableEmail(String email) {
		return this.memberDao.getEmailCount(email) == 0;
	}
	
	
	@Override
	public MemberVO getMember(MemberVO memberVO) {
		// 1. 이메일로 저장되어 있는 salt를 조회한다.
		String storedSalt = this.memberDao.selectSalt(memberVO.getEmail());
		
		// 만약, salt값이 null 이라면, 회원정보가 없는 것이므로 사용자에게 예외를 전달한다.
		if (StringUtils.isEmpty(storedSalt)) {
			throw new IllegalArgumentException("이메일 또는 비밀번호가 잘못되었습니다.");
		}
	
		// 2. salt 값이 있을경우, salt를 이용해 sha 암호화 한다.
		String password = memberVO.getPassword();
		password = this.sha.getEncrypt(password, storedSalt);
		memberVO.setPassword(password);
		
		// 3. DB 에서 암호화된 비밀번호와 이메일을 비교해 회원 정보를 가져온다.
		MemberVO member = this.memberDao.selectMemberByEmailAndPassword(memberVO);
		
		// 만약, 회원 정보가 null 이라면 회원 정보가 없는 것이므로 사용자에게 예외를 전달한다.
		if (member == null) {
			throw new IllegalArgumentException("이메일 또는 비밀번호가 잘못되었습니다.");
		}
		
		return member;
	}

	@Override
	public boolean deleteMe(String email) {
		int updateCount = this.memberDao.deleteMemberByEmail(email);
		return updateCount > 0;
	}
	
	
	
	
	
	
	
	
}
