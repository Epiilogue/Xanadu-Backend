package com.ruoyi.auth.form;

import java.io.Serializable;

public class EmailBody implements Serializable {
    /**
     * 用户名
     */
    private String email;
    private String userName;
    /**
     * 用户密码
     */
    private String code;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

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


}