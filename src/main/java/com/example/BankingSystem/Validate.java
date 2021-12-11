package com.example.BankingSystem;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet(name = "Validate", value = "/validate")
public class Validate extends HttpServlet {

    public void init() {

    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Bank", "mohanad", "7526819");
            Statement stmt = con.createStatement();
            String query = "SELECT * FROM Customer where Username = '" + username + "' and Password = '"
                    + password + "'";
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                HttpSession session = request.getSession(true);
                session.setAttribute("currentSessionUser", rs.getInt("CustomerID"));
                session.setAttribute("customerName", rs.getString("CustomerName"));
                response.sendRedirect("CustomerHome.jsp");
            } else {
                out.print("Credentials incorrect");
                RequestDispatcher rd = request.getRequestDispatcher("Login.html");
                rd.include(request, response);
            }
            rs.close();
            con.close();
            stmt.close();
        } catch (Exception ex) {
            out.println(ex);
        }
    }

    public void destroy() {
    }
}