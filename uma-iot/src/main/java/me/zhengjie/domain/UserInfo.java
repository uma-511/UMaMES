package me.zhengjie.domain;

import lombok.Data;

import javax.persistence.Entity;

@Data
public class UserInfo {
    String userName;
    String password;
    String banci;
    String jitai;
}