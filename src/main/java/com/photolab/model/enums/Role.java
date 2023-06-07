package com.photolab.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

@AllArgsConstructor
@Getter
public enum Role implements GrantedAuthority {
    ADMIN("Директор"),
    MASTER("Дизайнер"),
    CLIENT("Заказчик");
    private final String name;

    @Override
    public String getAuthority() {
        return name();
    }
}

