package shop.mtcoding.bankapp.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import shop.mtcoding.bankapp.dto.account.AccountWithdrawReqDto;
import shop.mtcoding.bankapp.model.account.Account;
import shop.mtcoding.bankapp.model.account.AccountRepository;
import shop.mtcoding.bankapp.model.history.History;
import shop.mtcoding.bankapp.model.history.HistoryRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private HistoryRepository historyRepository;

    @InjectMocks
    private AccountService accountService;

    @Test
    public void 계좌출금_tdd_test() throws  Exception{

        // given
        Long amount = 100L;
        String wAccountNumber = "1111";
        String wAccountPassword = "1234";

        // when
        // 1. 계좌존재 여부
        Account accountPS = new Account();
        accountPS.setId(1);
        accountPS.setNumber("1111");
        accountPS.setPassword("1234");
        accountPS.setBalance(1000L);
        accountPS.setUserId(1);
        accountPS.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));

        // 2. 계좌패스워드 확인
        if (!wAccountPassword.equals(accountPS.getPassword())) {
            throw new RuntimeException("출금계좌 비밀번호 틀렸는데?");
        }

        // 3. 잔액확인
        if (accountPS.getBalance() < amount) {
            throw new RuntimeException("잔액이 부족한데?");
        }

        // 4. 출금(balance - 마이너스)
        accountPS.setBalance(accountPS.getBalance() - amount);

        // 5. 히스토리 (거래내역)
        History history = new History();
        history.setAmount(amount);
        history.setWAccountId(accountPS.getId());
        history.setDAccountId(null);
        history.setWBalance(accountPS.getBalance());
        history.setDBalance(null);

        // then
        System.out.println(history.getWBalance());
        Assertions.assertThat(history.getWBalance()).isEqualTo(900);
    }

    @Test
    public void 계좌출금_bdd_test() throws  Exception{
        // given
        AccountWithdrawReqDto accountWithdrawReqDto = new AccountWithdrawReqDto();
        accountWithdrawReqDto.setAmount(100L);
        accountWithdrawReqDto.setWAccountNumber("1111");
        accountWithdrawReqDto.setWAccountPassword("1234");

        // stub
        Account accountPS = new Account();
        accountPS.setId(1);
        accountPS.setNumber("1111");
        accountPS.setPassword("1234");
        accountPS.setBalance(1000L);
        accountPS.setUserId(1);
        accountPS.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));

        Mockito.when(accountRepository.findByNumber(any())).thenReturn(accountPS);
        Mockito.when(accountRepository.updateById(any())).thenReturn(1);
        Mockito.when(historyRepository.insert(any())).thenReturn(1);

        // when
        int result =  accountService.계좌출금(accountWithdrawReqDto);

        // then
        System.out.println(result); // 1이기를 기대함.


    }
}
