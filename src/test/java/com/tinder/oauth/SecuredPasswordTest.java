package com.tinder.oauth;

import org.junit.Test;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import static org.assertj.core.api.Assertions.assertThat;

public class SecuredPasswordTest {

    @Test
    public void getHashPassword() throws InvalidKeySpecException, NoSuchAlgorithmException {
        //given
        String password = "stsongPasswor";
        //when
        final String s = SecuredPassword.generateStrongPasswordHash(password);
        //than
        assertThat(s).isNotEmpty();
    }

    @Test
    public void getHashAndCheckMatch() throws InvalidKeySpecException, NoSuchAlgorithmException {
        //given
        String password = "stsongPasswor";
        //when
        final String s = SecuredPassword.generateStrongPasswordHash(password);
        final boolean b = SecuredPassword.validatePassword(password, s);
        //than
        assertThat(b).isTrue();
    }

}