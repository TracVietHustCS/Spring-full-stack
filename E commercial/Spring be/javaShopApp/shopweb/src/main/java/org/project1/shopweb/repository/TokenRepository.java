package org.project1.shopweb.repository;

import org.project1.shopweb.model.Token;
import org.project1.shopweb.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TokenRepository extends JpaRepository<Token,Long> {
    List<Token> findByUser(User user);
    Token findByToken(String token);

    Token findByRefreshToken(String token);
}
