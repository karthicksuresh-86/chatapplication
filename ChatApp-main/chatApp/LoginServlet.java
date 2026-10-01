package com.chat;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final String URL =
        "jdbc:mysql://db01.dbhost.dev:5051/db_454krc7z2"
        + "?useSSL=false"
        + "&allowPublicKeyRetrieval=true"
        + "&serverTimezone=UTC";

    private static final String USER =
        "user_454krc7z2";

    private static final String PASSWORD =
        "p454krc7z2";


    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        String username =
            request.getParameter("username");


        // Check username
        if (username == null ||
            username.trim().isEmpty()) {

            response.sendRedirect(
                request.getContextPath() + "/login.jsp"
            );

            return;
        }


        username = username.trim();


        try {

            // Load MySQL driver
            Class.forName(
                "com.mysql.cj.jdbc.Driver"
            );


            // Connect to database
            Connection con =
                DriverManager.getConnection(
                    URL,
                    USER,
                    PASSWORD
                );


            // Add username to users table
            String sql =
                "INSERT IGNORE INTO users (username) "
                + "VALUES (?)";


            PreparedStatement ps =
                con.prepareStatement(sql);


            ps.setString(1, username);

            ps.executeUpdate();


            ps.close();
            con.close();


            // Create session
            HttpSession session =
                request.getSession(true);


            // Store username
            session.setAttribute(
                "username",
                username
            );


            // Redirect to chat page
            response.sendRedirect(
                request.getContextPath()
                + "/chat.jsp"
            );


        } catch (Exception e) {

            e.printStackTrace();

            // Show database error
            response.setContentType(
                "text/html;charset=UTF-8"
            );

            response.getWriter().println(
                "<h2>Database Connection Error</h2>"
            );

            response.getWriter().println(
                "<p>" + e.getMessage() + "</p>"
            );
        }
    }
}