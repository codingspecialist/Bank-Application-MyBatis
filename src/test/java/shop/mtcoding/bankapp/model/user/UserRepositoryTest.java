package shop.mtcoding.bankapp.model.user;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@MybatisTest // (F - DS - C - S) X - (R - MyBatis) O
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    /**
     * BDD 패턴 (본코드를 먼저 작성하고, 그 코드를 테스트)
     * TDD 패턴 (테스트코드를 먼저 작성하고, 본코드를 짠다)
     */
    @Test
    public void findById_test(){
        // given
        int id = 1;

        // when
        User user = userRepository.findById(id);

        // then (눈으로 검증)
        System.out.println(user.getUsername());
        Assertions.assertThat(user.getUsername()).isEqualTo("ssar");
    }

    @Test
    public void findAll_test(){
        // given

        // when
        List<User> userList = userRepository.findAll();

        // then
        Assertions.assertThat(userList.get(0).getUsername()).isEqualTo("ssar");
        Assertions.assertThat(userList.get(1).getUsername()).isEqualTo("cos");
    }

//    @Test
//    public void findByUsernameAndPassword_test(){
//        // given
//        String username = "ssar";
//        String password = "1234";
//
//        // when
//        User user = userRepository.findByUsernameAndPassword(username, password);
//
//        // then
//        Assertions.assertThat(user.getUsername()).isEqualTo("ssar");
//    }

    @Test
    public void insert_fail_test(){
        // given
        String username = "ssar";
        String password = "1234";
        String fullname = "쌀";

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setFullname(fullname);

        // when
        int checkError = 0;
        try {
            int result = userRepository.insert(user);
        }catch (Exception e){
            checkError++;
        }

        // then
        Assertions.assertThat(checkError).isEqualTo(1);
    }

    @Test
    public void insert_success_test(){
        // given
        String username = "love";
        String password = "1234";
        String fullname = "러브";

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setFullname(fullname);

        // when
        int result = userRepository.insert(user);

        // then
        Assertions.assertThat(result).isEqualTo(1);
    }


    @Test
    public void delete_test(){
        // given
        int id = 1;

        // when
        int result = userRepository.deleteById(id);

        // then
        Assertions.assertThat(result).isEqualTo(1);
    }

    @Test
    public void update_test(){
        // given
        int id = 1;
        String username = "ssar";
        String password = "1234";
        String fullname = "쌀만고";

        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setPassword(password);
        user.setFullname(fullname);

        // when
        int result = userRepository.updateById(user);

        // then
        Assertions.assertThat(result).isEqualTo(1);
    }
}
