package com.example;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

@Controller
public class RegistrationController {

    static final String DB_URL = "jdbc:postgresql://127.0.0.1:5432/TestyUnit";
    static final String USER = "postgres";
    static final String PASS = "timur2014";

    public Connection connect() throws SQLException {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return conn;
    }

    @GetMapping("/register")
    public @ResponseBody String registerForm() {
        return "<form action=\"\" method=\"POST\">" +
                "<label for=\"login\">Login:</label><br>" +
                "<input type=\"text\" id=\"login\" name=\"login\"/><br>" +
                "<label for=\"pass\">Password:</label><br>" +
                "<input type=\"password\" id=\"pass\" name=\"pass\"/><br>" +
                "<input type=\"submit\" value=\"Register\">" +
                "</form>";
    }

    @PostMapping("/register")
    public @ResponseBody String registerUser(@RequestParam Map<String, String> body) {

        String login = body.get("login");
        String pass = body.get("pass");

        Connection con;
        try {
            con = connect();
        } catch (SQLException e) {
            return "<h1>DB Error</h1>";
        }

        try (Statement stmt = con.createStatement()) {
            String query = String.format("INSERT INTO public.\"users\" (login, pass) " +
                    "VALUES ('%s', '%s')", login, pass);
            stmt.executeUpdate(query);
            return String.format("<h1>Registration successful, %s</h1>", login);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return String.format("<h1>Registration failed, %s</h1>", login);
        }
    }
}
