package com.amcrest.unity.subscription.domain;

import com.amcrest.unity.accounting.email.validation.ValidEmail;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(name = "user_subscription_mapping")
public class Subscription {

    @Schema(hidden = true)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @NotNull
    @NotEmpty
    private String customer_id;

    @NotNull
    @NotEmpty
    private String subscription_status;

    @NotNull
    @NotEmpty
    @ValidEmail
    @Column(unique = true)
    private String email;

//    @Override
//    @Schema(hidden = true)
//    public String getUsername() {
//        return email;
//    }
}
