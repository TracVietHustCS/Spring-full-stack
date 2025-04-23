package org.project1.shopweb.service.user;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.project1.shopweb.component.JwtTokenUtil;
import org.project1.shopweb.component.LocalizationUtils;
import org.project1.shopweb.dto.UpdateUserDTO;
import org.project1.shopweb.dto.UserDTOLogin;
import org.project1.shopweb.exception.DemoI18nException;
import org.project1.shopweb.exception.ErrorCode;
import org.project1.shopweb.model.Role;
import org.project1.shopweb.model.Token;
import org.project1.shopweb.model.User;
import org.project1.shopweb.dto.UserDTO;
import org.project1.shopweb.repository.RoleRepository;
import org.project1.shopweb.repository.TokenRepository;
import org.project1.shopweb.repository.UserRepository;
import org.project1.shopweb.exception.NotFoundException;
import org.project1.shopweb.utils.MessageKeys;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final LocalizationUtils localizationUtils;
    private final TokenRepository  tokenRepository;

    @Override
    public User createUser(UserDTO userDTO) throws Exception {

        //register user
        String phoneNumber = userDTO.getPhoneNumber();
        // Kiểm tra xem số điện thoại đã tồn tại hay chưa
        if( !phoneNumber.isBlank()   && userRepository.existsByPhoneNumber(phoneNumber)) {
            throw new DemoI18nException(ErrorCode.REGISTER_FAILED,"Phone number already exists");
        }
        if(!userDTO.getEmail().isBlank() && userRepository.existsByEmail(userDTO.getEmail())){
            throw new NotFoundException("email is already exist");

        }



        Role role =roleRepository.findById(userDTO.getRoleId())
                .orElseThrow(() ->
                        new DemoI18nException(ErrorCode.REGISTER_FAILED,localizationUtils.getLocalizationMessages(MessageKeys.ROLE_NOT_FOUND)));
        if(role.getName().toUpperCase().equals(Role.ADMIN)) {
            throw new DemoI18nException(ErrorCode.REGISTER_FAILED,"You cannot register an admin account");
        }
        //convert from userDTO => user
        User newUser = User.builder()
                .fullName(userDTO.getFullName())
                .phoneNumber(userDTO.getPhoneNumber())
                .password(userDTO.getPassword())
                .address(userDTO.getAddress())
                .dateOfBirth(userDTO.getDateOfBirth())
                .facebookAccountId(userDTO.getFacebookAccountId())
                .googleAccountId(userDTO.getGoogleAccountId())
                .active(true)
                .email(userDTO.getEmail())
                .build();

        newUser.setRole(role);
        // Kiểm tra nếu có accountId, không yêu cầu password
        if (!userDTO.isSocialLogin()) {
            String password = userDTO.getPassword();
            String encodedPassword = passwordEncoder.encode(password);
            newUser.setPassword(encodedPassword);
        }
        return userRepository.save(newUser);
    }


    @Override
    public String login(UserDTOLogin userDTOLogin  ) throws  Exception{

        Optional<User> optionalUser = Optional.empty();
        String subject = null;
        String phone = userDTOLogin.getPhoneNumber();
        String email = userDTOLogin.getEmail();

        if (phone != null && !phone.isBlank()) {
            optionalUser = userRepository.findByPhoneNumber(phone);
            subject = phone;
        }

        if (optionalUser.isEmpty() && email != null && !email.isBlank()) {
            optionalUser = userRepository.findByEmail(email);
            subject = email;
        }

        if(optionalUser.isEmpty()){
            throw new DemoI18nException(ErrorCode.LOGIN_FAILED,localizationUtils.getLocalizationMessages(MessageKeys.WRONG_USER_PASS));
        }


        User existUser = optionalUser.get();
        if(!passwordEncoder.matches(userDTOLogin.getPassword(),existUser.getPassword())){
                throw new DemoI18nException(ErrorCode.LOGIN_FAILED,localizationUtils.getLocalizationMessages(MessageKeys.WRONG_USER_PASS));

        }

        Optional<Role> optionalRole = roleRepository.findById(userDTOLogin.getRoleId());
        if(optionalRole.isEmpty() || !userDTOLogin.getRoleId().equals(existUser.getRole().getId())) {
            throw new DemoI18nException(ErrorCode.LOGIN_FAILED,localizationUtils.getLocalizationMessages(MessageKeys.ROLE_NOT_FOUND));
        }
        if(!optionalUser.get().getActive()) {
            throw new DemoI18nException(ErrorCode.LOGIN_FAILED,localizationUtils.getLocalizationMessages(MessageKeys.USER_IS_lOCKED));
        }



        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                subject,userDTOLogin.getPassword(),existUser.getAuthorities()
        );
        authenticationManager.authenticate(token); // hasrole trong securityfilterchain check role tu cai object Authenticaion tra ve
        return jwtTokenUtil.generateToken(existUser);
    }

    @Override
    public User getUserDetailFromToken(String token) {

        if(jwtTokenUtil.isTokenExpired(token)){
            throw new RuntimeException("this token is expired");

        }
        String phoneNumber = jwtTokenUtil.extractPhoneNumber(token);
        Optional<User> user = userRepository.findByPhoneNumber(phoneNumber);

     if(user.isPresent()){
         log.info("tra ve user");
         return user.get();
     } else {
        log.info("not found");
         throw new RuntimeException("User Not found");
     }



    }

    @Override
    public User updateUser(Long userId, UpdateUserDTO userDTO) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("user Not Found"));

        String newPhoneNumber = userDTO.getPhoneNumber();
        if(userRepository.existsByPhoneNumber(userDTO.getPhoneNumber())
         && !newPhoneNumber.equals(existingUser.getPhoneNumber())){

            throw new NotFoundException("this phone number is already exist");
        }


        if (userDTO.getFullName() != null) {
            existingUser.setFullName(userDTO.getFullName());
        }
        if (newPhoneNumber != null) {
            existingUser.setPhoneNumber(newPhoneNumber);
        }
        if (userDTO.getAddress() != null) {
            existingUser.setAddress(userDTO.getAddress());
        }
        if (userDTO.getDateOfBirth() != null) {
            existingUser.setDateOfBirth(userDTO.getDateOfBirth());
        }
        if (userDTO.isFacebookAccountIdValid()) {
            existingUser.setFacebookAccountId(userDTO.getFacebookAccountId());
        }
        if (userDTO.isGoogleAccountIdValid()) {
            existingUser.setGoogleAccountId(userDTO.getGoogleAccountId());
        }
        if(userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()){
            if(!userDTO.getPassword().equals(userDTO.getRetypePassword())){
                throw new NotFoundException("Password does not match");
            }
            String newPassword = userDTO.getPassword();
            String encodePassword = passwordEncoder.encode(newPassword);
            existingUser.setPassword(encodePassword);

        } else {
            throw new  NotFoundException("bad password"); // actually valid from fe so do have to worry
        }
        return userRepository.save(existingUser);


    }

    @Override
    public User getUserDetailsFromRefreshToken(String refreshToken) throws Exception {
        Token existingToken = tokenRepository.findByRefreshToken(refreshToken);
        if(jwtTokenUtil.isTokenExpired(existingToken.getToken())){
            log.info(existingToken.getToken());

            throw new NotFoundException("token not valid");
        }
        return existingToken.getUser();

    }

    @Override
    public Page<User> findAll(String keyword, Pageable pageable) throws Exception {
        return userRepository.findAll(keyword,pageable);
    }

    @Override
    @Transactional
    public void resetPwd(String newPass, Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("not found user"));
        String encodedPassword = passwordEncoder.encode(newPass);
        user.setPassword(encodedPassword);
        userRepository.save(user);
        List<Token> tokenList = tokenRepository.findByUser(user);
        for(Token t : tokenList){
            tokenRepository.delete(t);
        }
    }

    @Override
    public void blockOrEnable(Long id, Boolean active) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("not found user"));
        user.setActive(active);
        userRepository.save(user);
    }


    @Override
    public String loginSocial(UserDTOLogin userLoginDTO) throws Exception {
        Optional<User> optionalUser = Optional.empty();
        Role roleUser = roleRepository.findByName(Role.USER)
                .orElseThrow(() -> new NotFoundException(
                        localizationUtils.getLocalizationMessages(MessageKeys.ROLE_NOT_FOUND)));

        // Kiểm tra Google Account ID
        if (userLoginDTO.isGoogleAccountIdValid()) {
            optionalUser = userRepository.findByGoogleAccountId(userLoginDTO.getGoogleAccountId());

            // Tạo người dùng mới nếu không tìm thấy
            if (optionalUser.isEmpty()) {
                User newUser = User.builder()
                        .fullName(Optional.ofNullable(userLoginDTO.getFullname()).orElse(""))
                        .email(Optional.ofNullable(userLoginDTO.getEmail()).orElse(""))
                        .profileImage(Optional.ofNullable(userLoginDTO.getProfileImage()).orElse(""))
                        .role(roleUser)
                        .googleAccountId(userLoginDTO.getGoogleAccountId())
                        .password("") // Mật khẩu trống cho đăng nhập mạng xã hội
                        .active(true)
                        .build();

                // Lưu người dùng mới
                newUser = userRepository.save(newUser);
                optionalUser = Optional.of(newUser);
            }
        }
        // Kiểm tra Facebook Account ID
        else if (userLoginDTO.isFacebookAccountIdValid()) {
            optionalUser = userRepository.findByFacebookAccountId(userLoginDTO.getFacebookAccountId());

            // Tạo người dùng mới nếu không tìm thấy
            if (optionalUser.isEmpty()) {
                User newUser = User.builder()
                        .fullName(Optional.ofNullable(userLoginDTO.getFullname()).orElse(""))
                        .email(Optional.ofNullable(userLoginDTO.getEmail()).orElse(""))
                        .profileImage(Optional.ofNullable(userLoginDTO.getProfileImage()).orElse(""))
                        .role(roleUser)
                        .facebookAccountId(userLoginDTO.getFacebookAccountId())
                        .password("") // Mật khẩu trống cho đăng nhập mạng xã hội
                        .active(true)
                        .build();

                // Lưu người dùng mới
                newUser = userRepository.save(newUser);
                optionalUser = Optional.of(newUser);
            }
        } else {
            throw new IllegalArgumentException("Invalid social account information.");
        }

        User user = optionalUser.get();

        // Kiểm tra nếu tài khoản bị khóa
        if (!user.getActive()) {
            throw new NotFoundException(localizationUtils.getLocalizationMessages(MessageKeys.USER_IS_lOCKED));
        }

        // Tạo JWT token cho người dùng
        return jwtTokenUtil.generateToken(user);
    }


}
