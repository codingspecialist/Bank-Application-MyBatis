package shop.mtcoding.bankapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import shop.mtcoding.bankapp.dto.account.AccountDepositReqDto;
import shop.mtcoding.bankapp.dto.account.AccountSaveReqDto;
import shop.mtcoding.bankapp.dto.account.AccountWithdrawReqDto;
import shop.mtcoding.bankapp.handler.ex.CustomException;
import shop.mtcoding.bankapp.model.account.Account;
import shop.mtcoding.bankapp.model.account.AccountRepository;
import shop.mtcoding.bankapp.model.history.History;
import shop.mtcoding.bankapp.model.history.HistoryRepository;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private HistoryRepository historyRepository;

    @Transactional
    public void 계좌생성(AccountSaveReqDto accountSaveReqDto, int principalId) {
        Account account = accountSaveReqDto.toModel(principalId);
        accountRepository.insert(account);
    }

    @Transactional
    public int 계좌출금(AccountWithdrawReqDto accountWithdrawReqDto) {
        // 1. 계좌존재 여부
        Account accountPS = accountRepository.findByNumber(accountWithdrawReqDto.getWAccountNumber());
        if (accountPS == null) {
            throw new CustomException("계좌가 없는데?", HttpStatus.BAD_REQUEST);
        }

        // 2. 계좌패스워드 확인
        accountPS.checkPassword(accountWithdrawReqDto.getWAccountPassword());

        // 3. 잔액확인
        accountPS.checkBalance(accountWithdrawReqDto.getAmount());

        // 4. 출금(balance - 마이너스)
        accountPS.withdraw(accountWithdrawReqDto.getAmount());
        accountRepository.updateById(accountPS);

        // 5. 히스토리 (거래내역)
        History history = new History();
        history.setAmount(accountWithdrawReqDto.getAmount());
        history.setWAccountId(accountPS.getId());
        history.setDAccountId(null);
        history.setWBalance(accountPS.getBalance());
        history.setDBalance(null);

        historyRepository.insert(history);

        // 6. 해당 계좌의 id를 return
        return accountPS.getId();
    }

    @Transactional
    public void 입금하기(AccountDepositReqDto accountDepositReqDto) {
        // 1. 입금계좌 존재 여부
        Account accountPS = accountRepository.findByNumber(accountDepositReqDto.getDAccountNumber());
        if (accountPS == null) {
            throw new CustomException("계좌가 없는데?", HttpStatus.BAD_REQUEST);
        }

        // 2. 입금하기 (의미 있는 메서드를 호출)
        accountPS.deposit(accountDepositReqDto.getAmount()); // 모델에 상태 변경
        accountRepository.updateById(accountPS); // 디비에 commit

        // 3. 입금 트랜잭션 만들기 (히스토리)
        History history = new History();
        history.setAmount(accountDepositReqDto.getAmount());
        history.setWAccountId(null);
        history.setDAccountId(accountPS.getId());
        history.setWBalance(null);
        history.setDBalance(accountPS.getBalance());

        historyRepository.insert(history);
    }
}
