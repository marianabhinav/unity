package com.amcrest.unity.accounting.subscription;

import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static com.amcrest.unity.accounting.subscription.Permissions.ALERTS_SEND;
import static com.amcrest.unity.accounting.subscription.Permissions.CLOUD_STORAGE;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum Tiers {

    FREE(Sets.newHashSet()),
    SILVER(Sets.newHashSet(CLOUD_STORAGE)),
    GOLD(Sets.newHashSet(CLOUD_STORAGE, ALERTS_SEND));

    private Set<Permissions> permissions;

    public Set<SimpleGrantedAuthority> getGrantedAuthorities() {
        Set<SimpleGrantedAuthority> permissions = getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
        permissions.add(new SimpleGrantedAuthority(this.name() + "_TIER"));
        return permissions;
    }
}
