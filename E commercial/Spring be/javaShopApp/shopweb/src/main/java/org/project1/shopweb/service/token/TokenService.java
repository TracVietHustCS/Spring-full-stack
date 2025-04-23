package org.project1.shopweb.service.token;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.project1.shopweb.component.JwtTokenUtil;
import org.project1.shopweb.model.Token;
import org.project1.shopweb.model.User;
import org.project1.shopweb.repository.TokenRepository;
import org.project1.shopweb.exception.NotFoundException;
import org.project1.shopweb.service.token.ITokenService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TokenService implements ITokenService {
    private static final int MAX_TOKENS = 3;
    @Value("${jwt.expiration}")
    private int expiration; //save to an environment variable

    @Value("${jwt.expiration-refresh-token}")
    private int expirationRefreshToken;

    private final TokenRepository tokenRepository;
    private final JwtTokenUtil jwtTokenUtil;
    @Transactional
    @Override
    public Token addToken(User user,String token, boolean isMobileDevice) {
        List<Token> userTokens = tokenRepository.findByUser(user);
        int tokenCount = userTokens.size();
        // Số lượng token vượt quá giới hạn, xóa một token cũ
        if (tokenCount >= MAX_TOKENS) {

            boolean hasNonMobileToken = !userTokens.stream().allMatch(Token::isMobile);
            Token tokenToDelete;
            if (hasNonMobileToken) {
                tokenToDelete = userTokens.stream()
                        .filter(userToken -> !userToken.isMobile())
                        .findFirst()
                        .orElse(userTokens.get(0));
            } else {

                tokenToDelete = userTokens.get(0);
            }
            tokenRepository.delete(tokenToDelete);
        }
        long expirationInSeconds = expiration;
        LocalDateTime expirationDateTime = LocalDateTime.now().plusSeconds(expirationInSeconds);
        // Tạo mới một token cho người dùng
        Token newToken = Token.builder()
                .user(user)
                .token(token)
                .revoked(false)
                .expired(false)
                .tokenType("Bearer")
                .expirationDate(expirationDateTime)
                .isMobile(isMobileDevice)
                .build();
        newToken.setRefreshToken(UUID.randomUUID().toString());
        newToken.setRefreshExpirationDate(LocalDateTime.now().plusSeconds(expirationRefreshToken));
        tokenRepository.save(newToken);
        return newToken;
    }


    public Token refreshToken ( String refreshToken,User user) throws Exception {
        Token existToken = tokenRepository.findByRefreshToken(refreshToken);
        if(existToken == null){
            throw  new NotFoundException("Refresh Token does not exist");
        }
        if(existToken.getRefreshExpirationDate().compareTo(LocalDateTime.now()) < 0){
            throw new NotFoundException("refresh Token is expired");
        }

        String token = jwtTokenUtil.generateToken(user);
        LocalDateTime expirateionDateTime = LocalDateTime.now().plusMinutes(expiration);

        existToken.setToken(token);
        existToken.setExpirationDate(expirateionDateTime);
        existToken.setToken(token);
        existToken.setRefreshToken(UUID.randomUUID().toString());
        existToken.setRefreshExpirationDate(LocalDateTime.now().plusSeconds(expirationRefreshToken));
        tokenRepository.save(existToken);
        return existToken;



    }

}
