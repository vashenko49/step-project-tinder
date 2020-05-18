package com.tinder.oauth;

import com.tinder.exception.ConfigFileException;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class UtilJWTTest {


    @Test
    public void getJWT() throws ConfigFileException {
        //given
        String userID = "1234naskd";
        final UtilJWT instance = UtilJWT.getInstance();
        //when
        final String singToken = instance.createSingToken(userID);

        final boolean b = instance.verifyToken(singToken);
        assertThat(b).isTrue();

        assertThat(instance.getUserFromToken(singToken)).isEqualTo(userID);
    }
}