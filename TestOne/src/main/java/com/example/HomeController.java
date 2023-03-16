package com.example;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.*;

import java.util.Map;
import java.util.Objects;

@Controller
public class HomeController {

//    private final String dbUrl = "User ID=postgres;Password=1234;Host=localhost;Port=5432;Database=SoftwareTest;";
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
    @GetMapping("/")
    public @ResponseBody String greeting() {
        return "<form action=\"\" method=\"POST\">" +
                "<label for=\"login\">Login:</label><br>" +
                "<input type=\"text\" id=\"login\" name=\"login\"/><br>" +
                "<label for=\"pass\">Password:</label><br>" +
                "<input type=\"password\" id=\"pass\" name=\"pass\"/><br>" +
                "<input type=\"submit\" value=\"Submit\">" +
                "</form>";
    }

    @GetMapping("/logout")
    public @ResponseBody String logout() {
        return "Logged out";
    }

    @PostMapping("/")
    public @ResponseBody String greetingPost(@RequestParam Map<String, String> body) {

        String login = body.get("login");
        String pass = body.get("pass");

        Connection con;
        try {
            con = connect();
        } catch (SQLException e) {
            return "<h1>DB Error</h1>";
        }

        try (Statement stmt = con.createStatement()) {
            String query = String.format("SELECT login, pass " +
                                         "FROM public.\"users\" " +
                                         "Where \"login\" = '%s';", login);
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                String loginDB = rs.getString("login");
                String passDB = rs.getString("pass");
                if (pass.equals(passDB)) {
                    return String.format("<h1>Hello, %s</h1>. <a href=\"logout\">logout</a>", login);
                }
                else {
                    return String.format("<h1>Wrong pass, %s</h1>", login);
                }
            }
            return "<h1>Such user does not exist</h1>";
        } catch (SQLException e) {
            System.out.println(e.getMessage());;
        }
        return "";
    }

}