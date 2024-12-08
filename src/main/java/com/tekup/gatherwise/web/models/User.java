package com.tekup.gatherwise.web.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class User {
    private String  name;
    private String role;
   /*  public User(String name, String role) {
        this.name = name;
        this.role = role;
    } */
    
}