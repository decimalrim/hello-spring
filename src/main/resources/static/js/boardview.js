// 페이지 렌더링이 모두 끝났을 때
$().ready(function () {
  $(".delete-board").on("click", function () {
    var chooseValue = confirm(
      "이 게시글을 정말 삭제하시겠습니까?\n삭제작업은 복구할 수 없습니다."
    );

    var id = $(this).closest(".grid").data("id");

    if (chooseValue) {
      location.href = "/board/delete/" + id;
    }
  });

  var modifyReply = function (event) {
    console.log("수정을 클릭함", event);
    var target = event.currentTarget;
    var reply = $(target).closest(".reply");
    var replyId = reply.data("reply-id");
    console.log("replyId", replyId);
  };
  var deleteReply = function (event) {
    console.log("삭제를 클릭함", event);
    var target = event.currentTarget;
    var reply = $(target).closest(".reply");
    var replyId = reply.data("reply-id");
    console.log("replyId", replyId);
  };
  var reReply = function (event) {
    console.log("답글달기를 클릭함", event);
    var target = event.currentTarget;
    var reply = $(target).closest(".reply");
    var replyId = reply.data("reply-id");
    console.log("replyId", replyId);
  };
  var recommendReply = function (event) {
    console.log("추천하기를 클릭함", event);
    var target = event.currentTarget;
    var reply = $(target).closest(".reply");
    var replyId = reply.data("reply-id");
    console.log("replyId", replyId);
  };
  var loadReplies = function (boardId) {
    $(".reply-items").html("");

    $.get("/ajax/board/reply/" + boardId, function (response) {
      var count = response.data.count;
      var replies = response.data.replies;

      for (var i in replies) {
        var reply = replies[i];

        // <div class="reply" data-reply-id="댓글번호" style="padding-left: (level - 1) * 40px"></div>
        var replyDom = $("<div></div>");
        replyDom.addClass("reply");
        replyDom.data("reply-id", reply.replyId);
        replyDom.css({
          "padding-left": (reply.level - 1) * 40 + "px",
        });

        // <div class="author">사용자명 (사용자이메일)</div>
        var authorDom = $("<div></div>");
        authorDom.addClass("author");
        authorDom.text(reply.memberVO.name + "(" + reply.email + ")");
        replyDom.append(authorDom);

        // <div class="recommend-count">추천수: 실제추천수</div>
        var recommendCountDom = $("<div></div>");
        recommendCountDom.addClass("recommend-count");
        recommendCountDom.text("추천수: " + reply.recommendCnt);
        replyDom.append(recommendCountDom);

        // <div class="datatime"></div>
        var datetimeDom = $("<div></div>");
        datetimeDom.addClass("datetime");

        // <span class="crtdt">등록: 등록날짜</span>
        var crtDtDom = $("<span></span>");
        crtDtDom.addClass("crtdt");
        crtDtDom.text("등록:" + reply.crtDt);
        datetimeDom.append(crtDtDom);

        if (reply.crtDt !== reply.mdfyDt) {
          // 수정 되었을때만 보여야 하니까
          // <span class="mdfydt">(수정: 수정날짜)</span>
          var mdfyDtDom = $("<span></span>");
          mdfyDtDom.addClass("mdfydt");
          mdfyDtDom.text("(수정:" + reply.mdfyDt + ")");
          datetimeDom.append(mdfyDtDom);
        }
        replyDom.append(datetimeDom);

        // <pre class="content">댓글 내용</pre>
        var contentDom = $("<pre></pre>");
        contentDom.addClass("content");
        contentDom.text(reply.content);
        replyDom.append(contentDom);

        var loginEmail = $("#login-email").text();
        // <div>
        var controlDom = $("<div></div>");

        // 내가 작성한 이메일이라면
        if (reply.email == loginEmail) {
          // <span class="modify-reply">수정</span>
          var modifyReplyDom = $("<span></span>");
          modifyReplyDom.addClass("modify-reply");
          modifyReplyDom.text("수정");
          modifyReplyDom.on("click", modifyReply);
          controlDom.append(modifyReplyDom);

          // <span class="delete-reply">삭제</span>
          var deleteReplyDom = $("<span></span>");
          deleteReplyDom.addClass("delete-reply");
          deleteReplyDom.text("삭제");
          deleteReplyDom.on("click", deleteReply);
          controlDom.append(deleteReplyDom);

          // <span class="re-reply">답변하기</span>
          var reReplyDom = $("<span></span>");
          reReplyDom.addClass("re-reply");
          reReplyDom.text("답변하기");
          reReplyDom.on("click", reReply);
          controlDom.append(reReplyDom);
        } else {
          // <span class="re-reply">추천하기</span>
          var recommendReplyDom = $("<span></span>");
          recommendReplyDom.addClass("recommend-reply");
          recommendReplyDom.text("추천하기");
          recommendReplyDom.on("click", recommendReply);
          controlDom.append(recommendReplyDom);

          // <span class="re-reply">답변하기</span>
          var reReplyDom = $("<span></span>");
          reReplyDom.addClass("re-reply");
          reReplyDom.text("답변하기");
          reReplyDom.on("click", reReply);
          controlDom.append(reReplyDom);
        }
        replyDom.append(controlDom);

        $(".reply-items").append(replyDom);

        /*
        <div
          class="reply"
          data-reply-id="댓글번호"
          style="padding-left: (level - 1) * 40px"
        >
          <div class="author">사용자명 (사용자이메일)</div>
          <div class="recommend-count">추천수: 실제추천수</div>
          <div class="datatime">
            <span class="crtdt">등록: 등록날짜</span>
            <span class="mdfydt">(수정: 수정날짜)</span>
          </div>
          <pre class="content">댓글 내용</pre>
          <div>
            <span class="modify-reply">수정</span>
            <span class="delete-reply">삭제</span>
            <span class="re-reply">답변하기</span>

            <span class="re-reply">추천하기</span>
            <span class="re-reply">답변하기</span>
          </div>
        </div>;
        */
      }
    });
  };

  var boardId = $(".grid").data("id");
  loadReplies(boardId);

  $("#btn-save-reply").on("click", function () {
    $.post(
      "/ajax/board/reply/" + boardId,
      {
        content: $("#txt-reply").val(),
      },
      function (response) {
        var result = response.data.result;
        if (result) {
          loadReplies(boardId);
          $("#txt-reply").val("");
        } else {
          alert("댓글을 등록할 수 없습니다. 잠시 후 시도해주세요.");
        }
      }
    );
  });
  $("#btn-cancel-reply").on("click", function () {});
});
