package org.project1.shopweb.service.auth;

import java.io.IOException;
import java.util.Map;

public interface IAuthService {
    String generateAuthUrl(String loginType);

    Map<String, Object> authenticateAndFetchProfile(String code,String loginType) throws IOException;
}
