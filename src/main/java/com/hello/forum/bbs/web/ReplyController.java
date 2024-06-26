package com.hello.forum.bbs.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.hello.forum.bbs.service.ReplyService;
import com.hello.forum.bbs.vo.ReplyVO;
import com.hello.forum.common.vo.PaginateVO;
import com.hello.forum.member.vo.MemberVO;
import com.hello.forum.utils.AjaxResponse;

@RestController
public class ReplyController {
	
	@Autowired
	private ReplyService replyService;
	
	@GetMapping("/ajax/board/reply/{boardId}")
	public AjaxResponse getAllReplies (@PathVariable int boardId) {
		
		List<ReplyVO> replyList = this.replyService.getAllReplies(boardId);
		
		return new AjaxResponse().append("count", replyList.size()).append("replies", replyList);
	}
	
	
	@PostMapping("/ajax/board/reply/{boardId}")
	public AjaxResponse doCreateNewReplies(@PathVariable int boardId,
								ReplyVO replyVO,
								@SessionAttribute("_LOGIN_USER_") MemberVO memberVO) {
		replyVO.setBoardId(boardId);
		replyVO.setEmail(memberVO.getEmail());
		
		boolean isSuccess = this.replyService.createNewReply(replyVO);
		
		return new AjaxResponse().append("result", isSuccess);
	}
	
	@GetMapping("/ajax/board/reply/delete/{replyId}")
	public AjaxResponse doDeleteReplies(@PathVariable int replyId,
									@SessionAttribute ("_LOGIN_USER_") MemberVO memberVO) {
		boolean isSuccess = this.replyService.deleteOneReply(replyId, memberVO.getEmail());
		
		return new AjaxResponse().append("result", isSuccess);
	}
	
	@PostMapping("/ajax/board/reply/modify/{replyId}")
	public AjaxResponse doModifyReplies(@PathVariable int replyId,
									ReplyVO replyVO,
									@SessionAttribute ("_LOGIN_USER_") MemberVO memberVO) {
		replyVO.setReplyId(replyId);
		replyVO.setEmail(memberVO.getEmail());
		boolean isSuccess = replyService.modifyOneReply(replyVO);

		return new AjaxResponse().append("result", isSuccess);
	}
	
	
	@GetMapping("/ajax/board/reply/recommend/{replyId}")
	public AjaxResponse doRecommendReplies (@PathVariable int replyId, 
									@SessionAttribute ("_LOGIN_USER_") MemberVO memberVO) {
		boolean isSuccess = replyService.recommendOneReply(replyId, memberVO.getEmail());

		return new AjaxResponse().append("result", isSuccess);
	}
	
	

}
