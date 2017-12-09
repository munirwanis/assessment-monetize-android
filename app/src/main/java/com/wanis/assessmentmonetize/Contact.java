package com.wanis.assessmentmonetize;

import java.io.Serializable;

/**
 * Created by munirwanis on 09/12/17.
 */

class Contact implements Serializable {
    private String name;
    private String email;
    private String password;
    private String passwordConfirmation;
    private String cpf;

    public Contact(String name, String email, String password, String passwordConfirmation, String cpf){
        this.name = name;
        this.email = email;
        this.password = password;
        this.passwordConfirmation = passwordConfirmation;
        this.cpf = cpf;
    }
}
