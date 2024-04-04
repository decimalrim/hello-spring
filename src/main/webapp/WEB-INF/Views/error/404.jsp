<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%> <%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>페이지를 찾을 수 없습니다.</title>
  </head>
  <body>
    <!-- <h3>찾으시는 페이지는 존재하지 않습니다.</h3> -->
    <h3>${message}</h3>
    <a href="/board/search">게시글 목록으로 가기</a>
  </body>
</html>
