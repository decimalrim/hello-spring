package com.hello.forum.bbs.vo;

public class SearchBoardVO {
	
	/**
	 * 검색할 페이지(블록)번호
	 */
	private int pageNo;
	
	/**
	 * 한 목록(블록)에 노출시킬 게시글의 개수
	 */
	private int listSize;
	
	/**
	 * 생성할 최대 페이지의 수
	 */
	private int pageCount;

	
	public SearchBoardVO() {
		this.listSize = 10;
	}
	
	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getListSize() {
		return listSize;
	}

	public void setListSize(int listSize) {
		this.listSize = listSize;
	}

	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int listCount) {
		this.pageCount = (int) Math.ceil((double) listCount / this.listSize);
	}
	
	

}
