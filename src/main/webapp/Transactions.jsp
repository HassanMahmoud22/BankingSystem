<%@ page import="java.sql.DriverManager" %>
<%@ page import="java.sql.Statement" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title> Your Transactions </title>
</head>
<body>
<h3>Your Bank Transactions</h3>
<form action="check-transaction" method="get">
    <table>
        <%
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Bank", "mohanad", "7526819");
                Statement stmt = con.createStatement();
                String accountID = request.getSession().getAttribute("accountID").toString();
                String query = "SELECT * FROM BankTransaction where BTFromAccount = '" + accountID + "'" +
                        "OR BTToAccount = '" + accountID + "'";
                ResultSet rs = stmt.executeQuery(query);
                if (rs.next()) {
                    out.print("<tr>\n" +
                            "<td></td>\n" +
                            "<td>ID</td>\n" +
                            "<td>Date</td>\n" +
                            "<td>Amount</td>\n" +
                            "<td>Destination</td>\n" +
                            "<td>Source</td>\n" +
                            "</tr>");

                    do {
                        String transactionID = rs.getString("BankTransactionID");
                        String date = rs.getString("BTCreationDate");
                        String amount = rs.getString("BTAmount");
                        String to = rs.getString("BTToAccount");
                        String from = rs.getString("BTFromAccount");

                        out.print("<tr>");
                        out.print("<td> <input type=\"radio\" name=\"transactionID\" value=\"" + transactionID + "\"> </td>");
                        out.print("<td> " + transactionID + " </td>");
                        out.print("<td> " + date + " </td>");
                        out.print("<td> " + amount + " </td>");
                        out.print("<td> " + to + " </td>");
                        out.print("<td> " + from + " </td>");
                        out.print("</tr>");
                    } while (rs.next());
                }
                rs.close();
                con.close();
                stmt.close();
            } catch (Exception e) {
                out.print(e);
            }
        %>
        <tr>
            <td colspan="5">
                <button type="submit" name="submit" id="login">Cancel Transaction</button>
            </td>
        </tr>
    </table>
</form>
</body>
</html>
