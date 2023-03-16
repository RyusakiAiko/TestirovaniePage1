package com.example;

import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.sql.*;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class Zadaniya {


    private Connection connection;

    @Before
    public void setUp() throws SQLException {
        // установление соединения с базой данных
        String url = "jdbc:postgresql://localhost/Test";
        String user = "postgres";
        String password = "Tanya228";
        connection = DriverManager.getConnection(url, user, password);
    }
    @After
    public void tearDown() throws SQLException {
        // закрытие соединения с базой данных
        connection.close();
    }

    //Валидация задание 1
    @Test
    public void testValidLogin() {
        Validate validator = new Validate();
        boolean result = validator.isLoginValid("user");
        assertTrue(result);
    }

    @Test
    public void testInvalidLogin() {
        Validate validator = new Validate();
        boolean result = validator.isLoginValid("qwe!123");
        assertFalse(result);
    }

    @Test
    public void testValidPassword() {
        Validate validator = new Validate();
        boolean result = validator.isPasswordValid("mypassword123");
        assertTrue(result);
    }

    @Test
    public void testInvalidPassword() {
        Validate validator = new Validate();
        boolean result = validator.isPasswordValid("qwe#132");
        assertFalse(result);
    }


    //Проверка существования юзера задание 2
    @Test
    public void testUserExistence() throws SQLException {
        // имя пользователя для проверки
        String username = "Ton";

        // выполнение запроса на выборку пользователя из базы данных
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE login = ?");
        statement.setString(1, username);
        ResultSet result = statement.executeQuery();

        // проверка, что хотя бы один пользователь с указанным именем существует
        assertTrue(result.next());
    }

    @Test
    public void testUserNonExistence() throws SQLException {
        // имя несуществующего пользователя для проверки
        String username = "neTon";

        // выполнение запроса на выборку пользователя из базы данных
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE login = ?");
        statement.setString(1, username);
        ResultSet result = statement.executeQuery();

        // проверка, что пользователь не найден
        assertFalse(result.next());
    }

  //Проверка коннекта с бд задание 3
    @Test
    public void testDatabaseConnection() {
        // проверка, что соединение установлено успешно
        assertNotNull(connection);
    }
    @Autowired
    private MockMvc mockMvc;

   //Добавление юзера в базу задание 4
    @Test
    public void testRegistration() throws Exception {
        mockMvc.perform(post("/register")
                        .param("login", "testuser")
                        .param("pass", "testpassword"))
                .andExpect(status().isOk());
    }
    //заход и выход из аккаунта, задание 5
    @Test
    public void shouldReturnAccessGranted() throws Exception {
        this.mockMvc.perform(post("/").param("login", "Ton").param("pass", "123")).
                andDo(print()).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(containsString("Hello, Ton")));
        this.mockMvc.perform(get("/logout")).
                andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("Logged out")));
    }

    @Test
    public void shouldReturnAccessDenied() throws Exception {
        this.mockMvc.perform(post("/").param("login", "Ton").param("pass", "234")).
                andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("Wrong pass, 123")));

    }
}
