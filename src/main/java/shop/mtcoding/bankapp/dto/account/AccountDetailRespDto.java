package shop.mtcoding.bankapp.dto.account;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AccountDetailRespDto {
    private Integer id;
    private String number;
    private Long balance;
    private Integer userId;
    private String fullname;
}
