package com.gtchoi.todolistbackend.model;

import com.gtchoi.todolistbackend.validator.FieldMatch;
import com.gtchoi.todolistbackend.validator.VariableNamingPolicy;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@FieldMatch.List({
    @FieldMatch(first = "password", second = "confirmPassword", message = "두 비밀번호가 일치 해야합니다.")
})
public class SignUpDTO {

    @NotBlank
    @Length(min = 4, max = 16, message = "ID는 최소 4자, 최대 16자")
    @VariableNamingPolicy
    private String userId;
    @NotBlank
    @Length(min = 8, max = 20, message = "패스워드는 최소 8자, 최대 20자")
    private String password;
    @NotBlank
    @Length(min = 8, max = 20, message = "패스워드는 최소 8자, 최대 20자")
    private String confirmPassword;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
