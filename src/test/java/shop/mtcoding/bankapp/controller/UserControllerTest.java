package shop.mtcoding.bankapp.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import shop.mtcoding.bankapp.dto.user.LoginReqDto;
import shop.mtcoding.bankapp.model.account.AccountRepository;
import shop.mtcoding.bankapp.model.user.User;
import shop.mtcoding.bankapp.model.user.UserRepository;
import shop.mtcoding.bankapp.service.UserService;

import javax.servlet.http.HttpSession;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;

@WebMvcTest(UserController.class) // F -> DS(request, session, response) -> UserController
public class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private UserService userService;

    @Test
    public void login() throws Exception {
        // given
        String requestBody = "username=ssar&password=1234";

        // stub
        User user = new User();
        user.setId(1);
        user.setUsername("ssar");
        user.setPassword("1234");
        user.setFullname("쌀");
        user.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        Mockito.when(userRepository.findByUsernameAndPassword(ArgumentMatchers.any())).thenReturn(user);

        // when
        ResultActions resultActions = mvc.perform(MockMvcRequestBuilders.post("/login").content(requestBody).contentType(MediaType.APPLICATION_FORM_URLENCODED));

        // then
        HttpSession session = resultActions.andReturn().getRequest().getSession();
        User sessionUser = (User) session.getAttribute("principal");

        System.out.println(sessionUser.getUsername());
    }
}
