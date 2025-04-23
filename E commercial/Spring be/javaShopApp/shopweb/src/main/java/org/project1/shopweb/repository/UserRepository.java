package org.project1.shopweb.repository;

import org.project1.shopweb.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    boolean existsByPhoneNumber(String phonenumber);
    Optional<User> findByPhoneNumber(String phonenumber);

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    Optional<User> findByGoogleAccountId(String id);

    Optional<User> findByFacebookAccountId(String id);


    @Query("SELECT o FROM User o WHERE o.active = true AND (:keyword IS NULL OR :keyword = '' OR" +
        " o.fullName LIKE %:keyword% " +
        " OR o.address LIKE %:keyword% " +
        " OR o.phoneNumber LIKE %:keyword%) " +
        "And LOWER(o.role.name) = 'user' "
    )

    Page<User> findAll(@Param("keyword") String keyword, Pageable pageable);
}
