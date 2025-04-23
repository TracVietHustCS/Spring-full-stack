package org.project1.shopweb.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.project1.shopweb.component.JwtTokenUtil;
import org.project1.shopweb.dto.RefreshTokenDTO;
import org.project1.shopweb.dto.UpdateUserDTO;
import org.project1.shopweb.dto.UserDTO;
import org.project1.shopweb.dto.UserDTOLogin;
import org.project1.shopweb.exception.NotFoundException;
import org.project1.shopweb.model.Token;
import org.project1.shopweb.model.User;
import org.project1.shopweb.service.auth.AuthService;
import org.project1.shopweb.service.auth.IAuthService;
import org.project1.shopweb.service.token.TokenService;
import org.project1.shopweb.service.user.UserService;
import org.project1.shopweb.respon.LoginRespon;
import org.project1.shopweb.respon.RegisterRespon;
import org.project1.shopweb.respon.UserListRespon;
import org.project1.shopweb.respon.UserRespon;
import org.project1.shopweb.utils.MessageKeys;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;
import org.project1.shopweb.component.LocalizationUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("${api.prefix}/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    private final LocalizationUtils localizationUtils;
    private final JwtTokenUtil jwtTokenUtil;
    private final TokenService tokenService;
    private final IAuthService iAuthService;

    @PostMapping("/register")

    public ResponseEntity<RegisterRespon> creatUser(@Valid @RequestBody UserDTO userDto) throws Exception {

            if(!userDto.getPassword().equals(userDto.getRetypePassword())){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(RegisterRespon.builder()
                        .message(localizationUtils.getLocalizationMessages(MessageKeys.PASSWORD_NOT_MATCH)).build());
            }
            User user = userService.createUser(userDto);
            return ResponseEntity.ok(RegisterRespon.builder()
                            .user(user)
                            .message(localizationUtils.getLocalizationMessages(MessageKeys.REGISTER_SUCCESSFULLY)).build());


    }
    private boolean isMobileDevice(String userAgent) {
        // Kiểm tra User-Agent header để xác định thiết bị di động
        // Ví dụ đơn giản:
        return userAgent.toLowerCase().contains("mobile");

    }
    @PostMapping("/login")
        public ResponseEntity<?> login(@Valid @RequestBody UserDTOLogin userlogin,HttpServletRequest request) throws Exception {


        String token = userService.login(userlogin);
        String userAgent = request.getHeader("User-Agent");
        User user = userService.getUserDetailFromToken(token);
        Token jwtToken = tokenService.addToken(user, token, isMobileDevice(userAgent));

        return ResponseEntity.ok(LoginRespon.builder()
                .message(localizationUtils.getLocalizationMessages(MessageKeys.LOGIN_SUCCESSFULLY))
                .token(jwtToken.getToken())
                .tokenType(jwtToken.getTokenType())
                .refreshToken(jwtToken.getRefreshToken())
                .username(user.getUsername())
                .roles(user.getAuthorities().stream().map(item -> item.getAuthority()).toList())
                .id(user.getId())
                .build());

    }
        @PostMapping("/details")
        public ResponseEntity<UserRespon> getDetailFromToken(@RequestHeader("Authorization") String token ){


                    String realToken = token.substring(7);
                    User user = userService.getUserDetailFromToken(realToken);
                    log.info("ok");

                    return ResponseEntity.ok(UserRespon.fromUser(user));



        }
        @PutMapping("/details/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id,
                                                 @RequestBody UpdateUserDTO userDTO,
                                                 @RequestHeader("Authorization") String authorizationHeader){

            log.info("da nahn requst");
            String token = authorizationHeader.substring(7);
            User user = userService.getUserDetailFromToken(token);
            if(user.getId() != id){
                log.info(user.getId().toString());

                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            User updateUser = userService.updateUser(id,userDTO);
            log.info("ok");
            return ResponseEntity.ok(UserRespon.fromUser(updateUser));


        }


    @PostMapping("/refreshtoken")
    public ResponseEntity<LoginRespon> refreshToken(
            @Valid @RequestBody RefreshTokenDTO refreshTokenDTO
    ) throws Exception {

            User userDetail = userService.getUserDetailsFromRefreshToken(refreshTokenDTO.getRefreshToken());
            Token jwtToken = tokenService.refreshToken(refreshTokenDTO.getRefreshToken(), userDetail);
            return ResponseEntity.ok(LoginRespon.builder()
                    .message("Refresh token successfully")
                    .token(jwtToken.getToken())
                    .tokenType(jwtToken.getTokenType())
                    .refreshToken(jwtToken.getRefreshToken())
                    .username(userDetail.getUsername())
                    .roles(userDetail.getAuthorities().stream().map(item -> item.getAuthority()).toList())
                    .id(userDetail.getId())
                    .build());

    }

    @GetMapping("")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getAllUser(
            @RequestParam(defaultValue = "", required = false ) String keyword,
            @RequestParam(defaultValue = "0", required = false) int pageNum,
            @RequestParam(defaultValue = "id",required = false) String sortField,
            @RequestParam(defaultValue = "asc", required = false) String sortDir
    ) throws Exception {

            PageRequest pageRequest = PageRequest.of(pageNum - 1, 5,
                    sortDir.equals("asc") ? Sort.by(sortField).ascending() : Sort.by(sortField).descending());
            Page<UserRespon> users = userService.findAll(keyword, pageRequest).map(UserRespon::fromUser);
            int totalPages = users.getTotalPages();
            List<UserRespon> userList = users.getContent();

            return ResponseEntity.ok(
                    UserListRespon.builder()
                            .totalPage(totalPages)
                            .users(userList)
                            .build()
            );

    }

    @PutMapping("/refresh-password/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> resetPassword(@PathVariable Long id){

            String newPass = UUID.randomUUID().toString().substring(0,5);
            userService.resetPwd(newPass,id);
            return ResponseEntity.ok(newPass);



    }

    @GetMapping("/auth/social-login")
    public ResponseEntity<String> socialAuth(@RequestParam("login_type") String loginType){
        String url = iAuthService.generateAuthUrl(loginType);
        return ResponseEntity.ok(url);

    }

    @GetMapping("/auth/social/callback")
    public ResponseEntity<?> callback(
            @RequestParam("code") String code,
            @RequestParam("login_type") String loginType,
            HttpServletRequest request
    ) throws Exception {
        // Call the AuthService to get user info
        Map<String, Object> userInfo = iAuthService.authenticateAndFetchProfile(code, loginType);

        if (userInfo == null) {
             throw new NotFoundException("fail to authenticate");
        }

        // Extract user information from userInfo map
        String accountId = "";
        String name = "";
        String picture = "";
        String email = "";

        if (loginType.trim().equals("google")) {
            accountId = (String) Objects.requireNonNullElse(userInfo.get("sub"), "");
            name = (String) Objects.requireNonNullElse(userInfo.get("name"), "");
            picture = (String) Objects.requireNonNullElse(userInfo.get("picture"), "");
            email = (String) Objects.requireNonNullElse(userInfo.get("email"), "");
        } else if (loginType.trim().equals("facebook")) {
          accountId = (String) Objects.requireNonNullElse(userInfo.get("id"), "");           name = (String) Objects.requireNonNullElse(userInfo.get("name"), "");
            email = (String) Objects.requireNonNullElse(userInfo.get("email"), "");
            // Lấy URL ảnh từ cấu trúc dữ liệu của Facebook
            Object pictureObj = userInfo.get("picture");
            if (pictureObj instanceof Map) {
                Map<?, ?> pictureData = (Map<?, ?>) pictureObj;
                Object dataObj = pictureData.get("data");
                if (dataObj instanceof Map) {
                    Map<?, ?> dataMap = (Map<?, ?>) dataObj;
                    Object urlObj = dataMap.get("url");
                    if (urlObj instanceof String) {
                        picture = (String) urlObj;
                    }
                }
            }
        }

        // Tạo đối tượng UserLoginDTO
        UserDTOLogin userLoginDTO = UserDTOLogin.builder()
                .email(email)
                .fullname(name)
                .password("")
                .phoneNumber("")
                .profileImage(picture)
                .build();

        if (loginType.trim().equals("google")) {
            userLoginDTO.setGoogleAccountId(accountId);
            //userLoginDTO.setFacebookAccountId("");
        } else if (loginType.trim().equals("facebook")) {
            userLoginDTO.setFacebookAccountId(accountId);
            //userLoginDTO.setGoogleAccountId("");
        }

        return this.loginSocial(userLoginDTO, request);
    }

    private ResponseEntity<?> loginSocial(
            @Valid @RequestBody UserDTOLogin userLoginDTO,
            HttpServletRequest request
    ) throws Exception {
        // Gọi hàm loginSocial từ UserService cho đăng nhập mạng xã hội
        String token = userService.loginSocial(userLoginDTO);

        // Xử lý token và thông tin người dùng
        String userAgent = request.getHeader("User-Agent");
        User userDetail = userService.getUserDetailFromToken(token);
        Token jwtToken = tokenService.addToken(userDetail, token, isMobileDevice(userAgent));

        // Tạo đối tượng LoginResponse
        LoginRespon loginResponse = LoginRespon.builder()
                .message(localizationUtils.getLocalizationMessages(MessageKeys.LOGIN_SUCCESSFULLY))
                .token(jwtToken.getToken())
                .tokenType(jwtToken.getTokenType())
                .refreshToken(jwtToken.getRefreshToken())
                .username(userDetail.getUsername())
                .roles(userDetail.getAuthorities().stream().map(GrantedAuthority ::getAuthority).toList())
                .id(userDetail.getId())
                .build();

        // Trả về phản hồi
        return ResponseEntity.ok().body(loginResponse);
    }


    @PutMapping("/block/{id}/{active}")
    @PostAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> block(@PathVariable Long id,@PathVariable int active){

            userService.blockOrEnable(id,active > 0);
            String message = active > 0 ? "Successful enable" : "block done";
            return ResponseEntity.ok(message);


    }
    @GetMapping("/login/oauth2/code/google")
    public String handleGoogleCallback(@RequestParam("code") String code) {
        // In mã code nhận được từ Google
        System.out.println("Received authorization code: " + code);

        // Trả về code để kiểm tra
        return "Authorization code: " + code;
    }






}
