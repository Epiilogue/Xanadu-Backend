package com.ruoyi.auth.form;

import java.io.Serializable;

public class EmailBody implements Serializable {
    /**
     * 用户名
     */
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    /**
     * 用户密码
     */
    private String code;

}