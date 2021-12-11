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
import java.util.Random;

@WebServlet(name = "AddAccount", value = "/add-account")
public class AddAccount extends HttpServlet {
    public void init() {

    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String customerIDString = request.getSession().getAttribute("currentSessionUser").toString();
        try {
            int customerID = Integer.parseInt(customerIDString);
            String date = java.time.LocalDate.now().toString();
            int defaultBalance = 1000;
            Random rand = new Random();
            int newAccount = rand.nextInt(10000000);
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Bank", "mohanad", "7526819");
            Statement stmt = con.createStatement();
            String query = "SELECT * FROM BankAccount where BankAccountID = '" + newAccount + "'";
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                newAccount = rand.nextInt(10000000);
                rs = stmt.executeQuery(query);
            }
            query = "INSERT INTO BankAccount (BankAccountID, BACreationDate, BACurrentBalance, CustomerID)" +
                    " values (" + newAccount + ", '" + date + "' ," + defaultBalance + ", " + customerID + ");";
            stmt.executeUpdate(query);
            rs.close();
            stmt.close();
            con.close();
            response.sendRedirect("CustomerHome.jsp");
        } catch (Exception ex) {
            PrintWriter out = response.getWriter();
            out.print(ex);
        }
    }

    public void destroy() {
    }
}