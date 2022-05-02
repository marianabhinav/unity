package com.amcrest.unity.subscription;

import com.amcrest.unity.subscription.domain.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    Optional<Subscription> findByEmail(String email);
}
