package shop.mtcoding.bankapp.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import shop.mtcoding.bankapp.dto.user.JoinReqDto;
import shop.mtcoding.bankapp.dto.user.LoginReqDto;
import shop.mtcoding.bankapp.handler.ex.CustomException;
import shop.mtcoding.bankapp.model.user.User;
import shop.mtcoding.bankapp.model.user.UserRepository;
import shop.mtcoding.bankapp.service.UserService;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private HttpSession session;

    @GetMapping("/logout")
    public String logout() {
        session.invalidate();
        return "redirect:/loginForm";
    }

    // SELECT 요청이지만 로그인만 post로 한다. (예외임!!)
    @PostMapping("/login")
    public String login(LoginReqDto loginReqDto) {
        System.out.println("실서버");
        System.out.println(loginReqDto.getUsername());
        System.out.println(loginReqDto.getPassword());
        if (loginReqDto.getUsername() == null || loginReqDto.getUsername().isEmpty()) {
            throw new CustomException("username을 입력해주세요", HttpStatus.BAD_REQUEST);
        }
        if (loginReqDto.getPassword() == null || loginReqDto.getPassword().isEmpty()) {
            throw new CustomException("password를 입력해주세요", HttpStatus.BAD_REQUEST);
        }

        // 레파지토리 호출 (조회)
        User principal = userRepository.findByUsernameAndPassword(loginReqDto);

        if (principal == null) {
            throw new CustomException("아이디 혹은 비번이 틀렸습니다", HttpStatus.BAD_REQUEST);
        }

        // ------ HTTP 최초 로직!!!!!!!!
        // HTTP로 만들어진 서버는 stateless 이다.
        // stateless 상태를 저장하지 않는 서버!!
        // 클라이언트가 request(get,post,put,delete) 요청을 하면!!
        // 서버는 거기에 해당하는 처리를 하고, response 응답을 한다.
        // 서버는 클라이언트의 정보를 저장하지 않는다.
        // 서버는 멀티쓰레드 프로세스이다. 서버는 다수의 클라이언트의 요청 동시에 받을 수 있다.
        // 부하를 방지하기 위해서 클라이언트 정보를 기억안함.

        // 클라이언트의 정보를 기억해야하는 stateful 한 서버가 필요해진다.
        // 그 저장공간에 session이다.
        // 그래서 세션에 저장해야한다.

        // 아파치/톰켓의 저장 영역
        // (request - 응답이 되는 순간 사라짐, session - 브라우저가 켜져 있는 동안)
        session.setAttribute("principal", principal);

        return "redirect:/";
    }

    /**
     * POST요청과 PUT요청시에만 BODY 데이터가 있다.
     * 해당 BODY데이터는 컨트롤러 메서드의 매개변수에 주입된다.(DS)
     * 스프링은 x-www-form-urlencoded가 기본 파싱전략
     * key=value&key=value (form태그의 기본 전송 전략)
     * 컨트롤러의 메서드는 매개변수에서 두가지 방식으로 데이터를 받는다.
     * 1. 그냥 변수, 2. DTO(Object)
     * 주의 : key이름과 변수이름이 동일해야 한다.
     */
    @PostMapping("/join")
    public String join(JoinReqDto joinReqDto) { // DTO로 받는 것이 좋다.
        // 1. POST, PUT일 때만 유효성 검사 (이것보다 우선되는 것이 인증 검사이다)
        if (joinReqDto.getUsername() == null || joinReqDto.getUsername().isEmpty()) {
            throw new CustomException("username을 입력해주세요", HttpStatus.BAD_REQUEST);
        }
        if (joinReqDto.getPassword() == null || joinReqDto.getPassword().isEmpty()) {
            throw new CustomException("password를 입력해주세요", HttpStatus.BAD_REQUEST);
        }
        if (joinReqDto.getFullname() == null || joinReqDto.getFullname().isEmpty()) {
            throw new CustomException("fullname을 입력해주세요", HttpStatus.BAD_REQUEST);
        }

        // 컨벤션 : post, put, delete 할때만 하기
        // 서비스 호출 => 회원가입();
        userService.회원가입(joinReqDto);

        return "redirect:/loginForm";
    }

    @GetMapping("/joinForm")
    public String joinForm() {
        return "user/joinForm";
    }

    @GetMapping("/loginForm")
    public String loginForm() {
        return "user/loginForm";
    }
}
