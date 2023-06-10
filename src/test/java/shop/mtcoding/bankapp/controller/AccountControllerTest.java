package shop.mtcoding.bankapp.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
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
import shop.mtcoding.bankapp.model.account.AccountRepository;
import shop.mtcoding.bankapp.model.history.HistoryRepository;
import shop.mtcoding.bankapp.model.user.User;
import shop.mtcoding.bankapp.service.AccountService;
import shop.mtcoding.bankapp.service.AccountServiceTest;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@WebMvcTest(AccountController.class) // F -> DS(request, session, response) -> UserController
public class AccountControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AccountRepository accountRepository;

    @MockBean
    private HistoryRepository historyRepository;

    @MockBean
    private AccountService accountService;

    private MockHttpSession mockSession;

    @BeforeEach
    public void setUp(){
        User user = new User();
        user.setId(1);
        user.setUsername("ssar");
        user.setPassword("1234");
        user.setFullname("ssar@nate.com");
        user.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));

        mockSession = new MockHttpSession();
        mockSession.setAttribute("principal", user);
    }


    @Test
    public void transfer() throws Exception {
        // given
        String requestBody = "amount=100&wAccountNumber=1111&dAccountNumber=2222&wAccountPassword=1234";

        // stub
        Mockito.when(accountService.이체하기(ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(1);

        // when
        ResultActions resultActions = mvc.perform(MockMvcRequestBuilders.post("/account/transfer").content(requestBody).contentType(MediaType.APPLICATION_FORM_URLENCODED).session(mockSession));

        // then
        resultActions.andExpect(MockMvcResultMatchers.redirectedUrl("/account/"+1));
    }
}
