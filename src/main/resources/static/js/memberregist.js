$().ready(function () {
  $("#btn-login").on("click", function () {
    $(".error").remove();
    $("div.grid").removeAttr("style");

    $.post(
      "/ajax/member/login",
      {
        email: $("#email").val(),
        password: $("#password").val(),
        nextUrl: $("#next").val(),
      },
      function (response) {
        var errors = response.data.errors;
        var errorMessage = response.data.errorMessage;
        var next = response.data.next;

        // 파라미터 유효성 검사에 실패했을 경우.
        if (errors) {
          console.log(errors);

          for (var key in errors) {
            // 객체리터럴(errors)의 키가 할당.
            var errorDiv = $("<div></div>");
            errorDiv.addClass("error");
            // <div class="error"></div> 이것과 같음.

            var values = errors[key];
            for (var i in values) {
              var errorValue = values[i];
              console.log(errorValue);

              var error = $("<div></div>");
              error.text(errorValue);

              errorDiv.append(error);
            }

            $("input[name=" + key + "]").after(errorDiv);
          }

          if (errors.email && errors.password) {
            // 클래스 지정만
            // $("div.grid").addClass("validator-fail-both");
            var emailFailCount = errors.email.length;
            var passwordFailCount = errors.password.length;

            // if (emailFailCount > 1 || passwordFailCount > 1) {
            // Inline-Style 지정
            $("div.grid").css({
              "grid-template-rows":
                "28px " +
                21 * emailFailCount +
                "px 28px " +
                21 * passwordFailCount +
                "px 1fr",
            });
            // }
          } else if (errors.email) {
            // $("div.grid").addClass("validator-fail-email");
            var emailFailCount = errors.email.length;
            $("div.grid").css({
              "grid-template-rows":
                "28px " + 21 * emailFailCount + "px 28px 1fr",
            });
          } else if (errors.password) {
            // $("div.grid").addClass("validator-fail-password");
            $("div.grid").css({
              "grid-template-rows":
                "28px 28px " + 21 * passwordFailCount + "px 1fr",
            });
          }
        }

        // 파라미터 유효성 검사는 패스
        // 이메일이나 패스워드가 잘못된 경우.
        if (errorMessage) {
          var errorDiv = $("<div></div>");
          errorDiv.addClass("error");
          errorDiv.text(errorMessage);

          $("#loginForm").after(errorDiv);
        }

        // 정상적으로 로그인에 성공한 경우.
        if (next) {
          location.href = next;
        }
        /*
        response = {
          data: {
            errors: {
              "email": []
            },
            errorMessage: "",
            next: "/board/search"
          }
        } */
      }
    );
  });
});
