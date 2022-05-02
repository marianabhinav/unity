package com.amcrest.unity.accounting.user;

import com.amcrest.unity.accounting.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.lastLoginDate = ?2 WHERE u.email = ?1")
    int updateLoginDate(String email, LocalDateTime lastLoginDate);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.isEnabled = TRUE WHERE u.email = ?1")
    int enableAppUser(String email);
}
