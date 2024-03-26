package com.hello.forum.member.dao;

import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import com.hello.forum.member.vo.MemberVO;

@Repository
public class MemberDaoImpl extends SqlSessionDaoSupport implements 	MemberDao {
	
	@Autowired
	@Override
	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		super.setSqlSessionTemplate(sqlSessionTemplate);
	}
	
	@Override
	public int getEmailCount(String email) {
		return getSqlSession().selectOne(MemberDao.NAME_SPACE + ".getEmailCount", email);
	}
	
	@Override
	public int creatNewMember(MemberVO memberVO) {
		return getSqlSession().insert(MemberDao.NAME_SPACE + ".creatNewMember", memberVO);
	}

	@Override
	public String selectSalt(String email) {
		return getSqlSession().selectOne(MemberDao.NAME_SPACE + ".selectSalt", email);
	}

	@Override
	public MemberVO selectMemberByEmailAndPassword() {
		return getSqlSession().selectOne(MemberDao.NAME_SPACE + ".selectMemberByEmailAndPassword");
	}

	


}
