package com.dowit.backend.dto;

import com.dowit.backend.entity.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserResponseDTO {
        private Long id;
        private String name;
        private String email;
        private UserRole role;

}
