<%--
  Created by IntelliJ IDEA.
  User: wangxinji
  Date: 2020/1/6
  Time: 0:45
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>text</title>
</head>
<body>

<form action="/login" method="post">
    <table>
        <tr>
            <td>用户名:</td>
            <td><input type="text" name="username"></td>
        </tr>
        <tr>
            <td>密码:</td>
            <td><input type="password" name="password"></td>
        </tr>
        <tr>
            <td colspan="4"><button type="submit">登录</button></td>
        </tr>
    </table>
</form>
</body>
</html>
