package com.tinder.defaultImplementation;

import com.tinder.exception.ErrorConnectionToDataBase;
import com.tinder.exception.UserException;
import org.junit.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


public class UserDefaultTest {
    String email = "vashchenko@gmail.com";
    String firstName = "vashchenko";

    @Test
    public void createUserFromGoogleAndDrop() throws ErrorConnectionToDataBase, UserException {
        //given
        final UserDefault instance = UserDefault.getInstance();

        //when
        //than
        final UUID userFromGoogle = instance.createUserFromGoogle(firstName, email);

        assertThat(userFromGoogle).isNotNull();
        final boolean b = instance.dropUser(userFromGoogle);
        assertThat(b).isTrue();
    }

    @Test
    public void checkUserInDataBaseByEmail() throws ErrorConnectionToDataBase, UserException {
        //given
        final UserDefault instance = UserDefault.getInstance();
        //when
        final boolean b = instance.userExistByEmail(email);
        //than
        assertThat(b).isFalse();
    }

}