<%@ page import="java.sql.Connection" %>
<%@ page import="java.sql.DriverManager" %>
<%@ page import="java.sql.Statement" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Home</title>
</head>
<body>
<h1>
    <%
        String customerName = request.getSession().getAttribute("customerName").toString();
        out.print("Welcome " + customerName + "!");
    %>
</h1>
<%
    String customerIDString = request.getSession().getAttribute("currentSessionUser").toString();
    try {
        int customerID = Integer.parseInt(customerIDString);
        Class.forName("com.mysql.jdbc.Driver");
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Bank", "mohanad", "7526819");
        Statement stmt = con.createStatement();
        String query = "SELECT * FROM BankAccount where CustomerID = '" + customerID + "'";
        ResultSet rs = stmt.executeQuery(query);
        if (rs.next()) {
            String accountNumber = rs.getString(1);
            String balance = rs.getString(3);
            String buttonEnabled = "<td> <button type=\"submit\" name=\"submit\" id=\"Transactions.jsp\" formaction=\"Transactions.jsp\">View Transactions</button> </td>";
            String buttonDisabled = "<td> <button type=\"submit\" name=\"submit\" id=\"Transactions.jsp\" disabled=\"disabled\">View Transactions</button> </td>";
            query = "SELECT * FROM BankTransaction where BTFromAccount = '" + accountNumber + "'" +
                    "OR BTToAccount = '" + accountNumber + "'";
            rs = stmt.executeQuery(query);
            boolean flag = rs.next();
            String button = (flag) ? buttonEnabled : buttonDisabled;
            request.getSession().setAttribute("accountID", accountNumber);
            out.print("<form method=\"get\">");
            out.print("<table>");
            out.print("<tr>" +
                    "<td> Your Account Number: </td>" +
                    "<td> " + accountNumber + " </td>" +
                    "</tr>" +
                    "<tr>" +
                    "<td> Your Current Balance:  </td>" +
                    "<td>" + balance + "</td>" +
                    "</tr>" +
                    "<tr>" +
                    button +
                    "<td> <button type=\"submit\" name=\"submit\" id=\"Transactions.jsp\" formaction=\"NewTransaction.jsp\">Make Transaction</button> </td>" +
                    "</tr>");

            out.print("</table>");
            out.print("</form");
        } else {
            out.print("<form action=\"add-account\" method=\"get\">" +
                    "<table>" +
                    "<tr> <td> You have no bank accounts yet </td> </tr>" +
                    "<tr> <td> <button type=\"submit\" name=\"submit\" id=\"addAccount\">Add Account</button> </td> </tr>" +
                    "</table>" +
                    "</form>");
        }
        rs.close();
        con.close();
        stmt.close();
    } catch (Exception ex) {
        out.print(ex);
    }

%>
</body>
</html>
