package com.example.BankingSystem;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

@WebServlet(name = "CheckTransaction", value = "/check-transaction")
public class CheckTransaction extends HttpServlet {
    public void init() {

    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String transactionID = request.getParameter("transactionID");
        PrintWriter out = response.getWriter();
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Bank", "mohanad", "7526819");
            Statement stmt = con.createStatement();
            String query = "SELECT * FROM BankTransaction WHERE BankTransactionID = '" + transactionID + "'";
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                String currentDate = java.time.LocalDate.now().toString();
                String date = rs.getString("BTCreationDate");
                if (currentDate.substring(0, currentDate.length() - 2).equals(date.substring(0, date.length() - 2))) {
                    String currentDay = currentDate.substring(currentDate.length() - 2);
                    String day = date.substring(date.length() - 2);
                    int currentInt = Integer.parseInt(currentDay);
                    int cancelInt = Integer.parseInt(day);
                    if (currentInt - cancelInt <= 1) {
                        int amount = rs.getInt("BTAmount");
                        String from = rs.getString("BTFromAccount");
                        String to = rs.getString("BTToAccount");
                        query = "SELECT * FROM BankAccount WHERE BankAccountID = '" + to + "'";
                        rs = stmt.executeQuery(query);
                        rs.next();
                        int destBalance = rs.getInt("BACurrentBalance");
                        if (destBalance > amount) {
                            destBalance -= amount;
                            query = "UPDATE BankAccount SET BACurrentBalance = '" + destBalance + "' WHERE BankAccountID = '" + to + "'";
                            stmt.executeUpdate(query);

                            query = "SELECT * FROM BankAccount WHERE BankAccountID = '" + from + "'";
                            rs = stmt.executeQuery(query);
                            rs.next();
                            int srcBalance = rs.getInt("BACurrentBalance");
                            srcBalance += amount;
                            query = "UPDATE BankAccount SET BACurrentBalance = '" + srcBalance + "' WHERE BankAccountID = '" + from + "'";
                            stmt.executeUpdate(query);

                            query = "DELETE FROM BankTransaction WHERE BankTransactionID = '" + transactionID + "'";
                            stmt.executeUpdate(query);
                        }
                        rs.close();
                        con.close();
                        stmt.close();
                    }
                }
            }
            response.sendRedirect("CustomerHome.jsp");
        } catch (Exception e) {
            out.print(e);
        }
    }

    public void destroy() {
    }
}