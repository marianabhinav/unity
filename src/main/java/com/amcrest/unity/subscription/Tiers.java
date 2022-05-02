package com.amcrest.unity.subscription;

import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static com.amcrest.unity.subscription.Permissions.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum Tiers {

    FREE(Sets.newHashSet()),
    SILVER(Sets.newHashSet(CLOUD_STORAGE)),
    GOLD(Sets.newHashSet(CLOUD_STORAGE, ALERTS_SEND)),
    ADMIN(Sets.newHashSet(CLOUD_STORAGE, ALERTS_SEND, ADMIN_ROLE));

    private Set<Permissions> permissions;

    public Set<SimpleGrantedAuthority> getGrantedAuthorities() {
        Set<SimpleGrantedAuthority> permissions = getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
        return permissions;
    }
}
