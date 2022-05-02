package com.amcrest.unity.accounting.adminUser;

import com.amcrest.unity.accounting.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional(readOnly = true)
public interface AdminUserRepository extends JpaRepository<User, Long> {

    List<User> findAll();

    @Query("SELECT u FROM User u WHERE u.firstName like %?1% OR u.lastName like %?1% OR u.email like %?1%")
    List<User> findAllByEmailOrFirstNameOrLastName(String keyword);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.firstName = ?2,  u.lastName = ?3 WHERE u.email = ?1")
    int updateUser(String email, String firstName, String lastName);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.isAccountNonLocked = ?2 WHERE u.email = ?1")
    int lockUser(String email, boolean isAccountNonLocked);
}
