<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%> <%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
    <title>회원가입</title>
    <jsp:include page="../commonheader.jsp"></jsp:include>
    <style type="text/css">
      div.grid {
        display: grid;
        grid-template-columns: 120px 1fr;
        grid-template-rows: 28px 28px 28px 28px 1fr;
        row-gap: 10px;
      }
      div.grid > div.btn-group {
        display: grid;
        grid-column: 1 / 3;
      }
      div.grid div.right-align {
        text-align: right;
      }
      label {
        padding-left: 10px;
      }
      button,
      input {
        padding: 10px;
      }
      input[type="file"] {
        padding: 10px;
      }

      .available {
        background-color: #0f03;
      }

      .unusable {
        background-color: #f003;
      }
    </style>
  </head>
  <script type="text/javaScript">
    $().ready(function() {
        var alertDialog = $(".alert-dialog");

        // if (undefined) {
        //     // 실행되지 않음
        // }
        // if (null) {
        //     // 실행되지 않음
        // }
        // if (0) {
        //     // 실행되지 않음
        // }
        // if ("") {
        //     // 실행되지 않음
        // }
        // if (false) {
        //     // 실행되지 않음
        // }


        // 배열.
        if (alertDialog && alertDialog.length > 0) {
            alertDialog[0].showModal();
        }

        $("#email").on("keyup", function () {
            // 서버에게 사용할 수 있는 이메일인지 확인 받는다.
            $.get(
                "/member/regist/available",
                {email: $(this).val()},
                function (response) {
                    var available = response.available;
                    if (available) {
                        $("#email").addClass("available");
                        $("#email").removeClass("unusable");
                        $("#btn-regist").removeAttr("disabled");
                    } else {
                        $("#email").addClass("unusable");
                        $("#email").removeClass("available");
                        $("#btn-regist").attr("disabled", "disabled");
                    }
                });
        });
    });
  </script>
  <body>
    <c:if test="${not empty errorMessage}">
      <dialog class="alert-dialog">
        <h1>${errorMessage}</h1>
      </dialog>
    </c:if>

    <h1>회원가입</h1>
    <form action="/member/regist" method="post">
      <div class="grid">
        <label for="email">이메일</label>
        <input type="email" type="email" id="email" name="email" />

        <label for="name">이름</label>
        <input type="name" type="text" id="name" name="name" />

        <label for="password">비밀번호</label>
        <input type="password" type="password" id="password" name="password" />

        <label for="confirmPassword">비밀번호 확인</label>
        <input
          type="confirmPassword"
          type="password"
          id="confirmPassword"
          name="confirmPassword"
        />

        <div class="btn-group">
          <div class="right-align">
            <input id="btn-regist" type="submit" value="등록" />
          </div>
        </div>
      </div>
    </form>
  </body>
</html>
