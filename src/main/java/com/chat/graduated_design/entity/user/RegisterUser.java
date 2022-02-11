package com.chat.graduated_design.entity.user;

import com.chat.graduated_design.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterUser extends User {
    private String passwordConfirm;
    private String code;
}
