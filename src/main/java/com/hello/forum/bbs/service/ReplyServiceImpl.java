package com.hello.forum.bbs.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hello.forum.bbs.dao.ReplyDao;
import com.hello.forum.bbs.vo.ReplyVO;
import com.hello.forum.exceptions.PageNotFoundException;

@Service
public class ReplyServiceImpl implements ReplyService {

	@Autowired
	private ReplyDao replyDao;
	
	@Override
	public List<ReplyVO> getAllReplies(int boardId) {
		return replyDao.getAllReplies(boardId);
	}
	
	@Transactional
	@Override
	public boolean createNewReply(ReplyVO replyVO) {
		return this.replyDao.createNewReply(replyVO) > 0;
	}
	
	@Transactional
	@Override
	public boolean deleteOneReply(int replyId, String email) {
		ReplyVO replyVO = replyDao.getOneReply(replyId);
//		if(!email.equals(replyVO.getEmail())) {
//			throw new PageNotFoundException("잘못된 접근입니다.");
//		}
		return this.replyDao.deleteOneReply(replyId) > 0;
	} 
	
	@Transactional
	@Override
	public boolean modifyOneReply(ReplyVO replyVO) {
		ReplyVO originReplyVO = replyDao.getOneReply(replyVO.getReplyId());
//		if(!replyVO.getEmail().equals(originReplyVO.getEmail())) {
//			throw new PageNotFoundException("잘못된 접근입니다.");
//		}
		return this.replyDao.modifyOneReply(replyVO) > 0;
	}

	@Override
	public boolean recommendOneReply(int replyId, String email) {
		ReplyVO replyVO = replyDao.getOneReply(replyId);
//		if(!email.equals(replyVO.getEmail())) {
//			throw new PageNotFoundException("잘못된 접근입니다.");
//		}
		return this.replyDao.recommendOneReply(replyId) > 0;
	}
	
	

}
