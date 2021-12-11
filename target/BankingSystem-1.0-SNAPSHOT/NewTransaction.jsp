<%--
  Created by IntelliJ IDEA.
  User: mohanad
  Date: 12/20/20
  Time: 8:20 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>New Transaction</title>
</head>
<body>
<form action="check-transfer" method="get">
    <table>
        <tr>
            <td>Amount</td>
            <td><label id="amount">
                <input type="text" name="amount">
            </label></td>
        </tr>
        <tr>
            <td>Destination</td>
            <td><label id="destination">
                <input type="text" name="destination">
            </label></td>
        </tr>
        <tr>
            <td>
                <button type="submit" name="submit" id="login">Transfer</button>
            </td>
        </tr>
    </table>
</form>
</body>
</html>
