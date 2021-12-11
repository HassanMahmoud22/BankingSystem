package com.example.BankingSystem;

import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Random;

@WebServlet(name = "CheckTransfer", value = "/check-transfer")
public class CheckTransfer extends HttpServlet {

    public void init() {

    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        String amount = request.getParameter("amount");
        String destination = request.getParameter("destination");
        String sourceID = request.getSession().getAttribute("accountID").toString();
        String date = java.time.LocalDate.now().toString();
        PrintWriter out = response.getWriter();
        try {
            int amountInt = Integer.parseInt(amount);
            if (amountInt > 0) {
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Bank", "mohanad", "7526819");
                Statement stmt = con.createStatement();
                String query = "SELECT * FROM BankAccount WHERE BankAccountID = '" + destination + "'";
                ResultSet rs = stmt.executeQuery(query);
                // check destination exists
                if (rs.next()) {
                    query = "SELECT * FROM BankAccount WHERE BankAccountID = '" + sourceID + "'";
                    rs = stmt.executeQuery(query);
                    rs.next();
                    int sourceBalance = rs.getInt("BACurrentBalance");
                    if (sourceBalance >= amountInt) {
                        // update source balance
                        int newSource = sourceBalance - amountInt;
                        query = "UPDATE BankAccount SET BACurrentBalance = '" + newSource + "' WHERE BankAccountID = '" + sourceID + "'";
                        stmt.executeUpdate(query);
                        // Update destination balance
                        query = "SELECT * FROM BankAccount WHERE BankAccountID = '" + destination + "'";
                        rs = stmt.executeQuery(query);
                        rs.next();
                        int newBalance = rs.getInt("BACurrentBalance") + amountInt;
                        query = "UPDATE BankAccount SET BACurrentBalance = '" + newBalance + "' WHERE BankAccountID = '" + destination + "'";
                        stmt.executeUpdate(query);
                        // Record transaction
                        Random rand = new Random();
                        int newTransaction = rand.nextInt(10000000);
                        query = "SELECT * FROM BankTransaction where BankTransactionID = '" + newTransaction + "'";
                        rs = stmt.executeQuery(query);
                        while (rs.next()) {
                            newTransaction = rand.nextInt(10000000);
                            rs = stmt.executeQuery(query);
                        }
                        query = "INSERT INTO BankTransaction (BankTransactionID, BTCreationDate, BTAmount, BTFromAccount, BTToAccount) VALUES " +
                                "(" + newTransaction + ", '" + date + "'," + amountInt + ", " + sourceID + ", " + destination + ")";
                        stmt.executeUpdate(query);
                    } else out.println("Insufficient Balance");
                } else out.println("Invalid Destination");
                rs.close();
                con.close();
                stmt.close();
            } else out.print("Invalid transfer amount");
            response.sendRedirect("CustomerHome.jsp");
        } catch (Exception ex) {
            out.print(ex);
        }
    }

    public void destroy() {
    }
}