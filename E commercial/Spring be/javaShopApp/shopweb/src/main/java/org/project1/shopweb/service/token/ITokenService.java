package org.project1.shopweb.service.token;

import org.project1.shopweb.model.Token;
import org.project1.shopweb.model.User;

public interface ITokenService {
    Token addToken(User user, String token,boolean isMobileDevice);

    public Token refreshToken ( String refreshToken,User user) throws Exception;
}
