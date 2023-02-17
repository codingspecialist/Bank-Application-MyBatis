<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@include file="../layout/header.jsp" %>

        <h1>계좌상세보기</h1>
        <hr />
        <div class="user-box">
            ${aDto.fullname}님 계좌<br />
            계좌번호 : ${aDto.number}<br />
            잔액 : ${aDto.balance}원
        </div>
        <div class="list-box">
            <a href="/account/${aDto.id}?gubun=all">전체</a>
            <a href="/account/${aDto.id}?gubun=deposit">입금</a>
            <a href="/account/${aDto.id}?gubun=withdraw">출금</a>
            <br />
            <table border="1">
                <thead>
                    <tr>
                        <th>날짜</th>
                        <th>보낸이</th>
                        <th>받은이</th>
                        <th>입출금금액</th>
                        <th>계좌잔액</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${hDtoList}" var="history">
                        <tr>
                            <td>${history.createdAt}</td>
                            <td>${history.sender}</td>
                            <td>${history.receiver}</td>
                            <td>${history.amount}원</td>
                            <td>${history.balance}원</td>
                        </tr>
                    </c:forEach>
  
                </tbody>
            </table>
        </div>

        </body>

        </html>