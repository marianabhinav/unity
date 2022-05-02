package com.amcrest.unity.accounting.email.token.domain;

import com.amcrest.unity.accounting.user.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ConfirmationToken {

    @Schema(hidden = true)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @NotNull
    @NonNull
    private Integer otp;

    @NotNull
    @NonNull
    private LocalDateTime createdAt;

    @NotNull
    @NonNull
    private LocalDateTime expiresAt;

    private LocalDateTime confirmedAt;

    @NotNull
    @NonNull
    @ManyToOne
    @JoinColumn(
            nullable = false,
            name = "user_id"
    )
    private User user;
}
