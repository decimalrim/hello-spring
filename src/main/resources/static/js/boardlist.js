$().ready(function () {
  $("#deleteMassiveBoard").on("click", function () {
    // 선택된 체크박스만 가져온다.
    var checkedItems = $(".target-board-id:checked");

    // 선택된 체크박스만 반복하여 서버로 보낼 파라미터를 생성한다.
    var itemsArray = [];
    checkedItems.each(function (index, data) {
      itemsArray.push($(data).val());
    });

    // 서버로 전송한다. (ajax)
    $.post(
      "/ajax/board/delete/massive",
      { deleteItems: itemsArray },
      function (response) {
        var result = response.data.result;
        if (result) {
          // 삭제가 완료되면 현재페이지를 새로고침한다.
          location.reload();
        }
      }
    );
  });

  // list-size 바뀌면 맨 처음 페이지로 가라
  $("#list-size").on("change", function () {
    search(0);
  });

  $("#search-btn").on("click", function () {
    search(0);
  });

  $("#cancel-search-btn").on("click", function () {
    location.href = "/board/search";
  });

  $("#uploadExcelfile").on("click", function () {
    $("#excelfile").click();
  });

  /** 파일이 선택되면 수행해라~ */
  $("#excelfile").on("change", function () {
    // 선택된 파일의 정보를 출력.
    var file = $(this)[0].files[0];
    var filename = file.name;

    if (!filename.endsWith(".xlsx")) {
      alert("엑셀 파일을 선택해주세요!");
      // 엑셀파일을 선택하지 않았을     경우
      // 함수실행을 종료시킨다.
      return;
    }

    // 파일을 서버로 전송시킨다.
    var formData = new FormData();
    // formData에 파일 정보를 첨부시킨다.
    formData.append("excelFile", file);

    // 파일 전송은 $.post로 할 수 없다.
    // $.post();
    $.ajax({
      url: "/ajax/board/excel/write", // 요청을 보낼 주소
      method: "POST", // 요청을 보낼 HttpMethod
      data: formData, // 요청을 보낼 데이터 (FormData)
      processData: false,
      contentType: false,
      success: function (response) {
        var data = response.data;
        if (data.result && data.next) {
          location.href = data.next;
        }
      },
    });
  });
});
