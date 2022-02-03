package com.amcrest.unity.accounting.user.domain;

import com.amcrest.unity.accounting.email.validation.ValidEmail;
import com.amcrest.unity.accounting.user.validation.ValidPassword;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Objects;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user")
public class User implements UserDetails {

    @Schema(hidden = true)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    private String firstName;
    private String lastName;

    @NotNull
    @NotEmpty
    @ValidPassword
    private String password;

    @NotNull
    @NotEmpty
    @ValidEmail
    @Column(unique = true)
    private String email;

    @Schema(hidden = true)
    private Boolean isAccountNonLocked = true;

    @Schema(hidden = true)
    private Boolean isEnabled = false;

    @Schema(hidden = true)
    private Boolean isAccountNonExpired = true;

    @Schema(hidden = true)
    private Boolean isCredentialsNonExpired = true;

    @Transient
    private Collection<? extends GrantedAuthority> authorities;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User) o;
        return id != null && Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    @Schema(hidden = true)
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    @Schema(hidden = true)
    public String getUsername() {
        return email;
    }

    @Override
    @Schema(hidden = true)
    public boolean isAccountNonExpired() {
        return isAccountNonExpired;
    }

    @Override
    @Schema(hidden = true)
    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    @Override
    @Schema(hidden = true)
    public boolean isCredentialsNonExpired() {
        return isCredentialsNonExpired;
    }

    @Override
    @Schema(hidden = true)
    public boolean isEnabled() {
        return isEnabled;
    }
}
