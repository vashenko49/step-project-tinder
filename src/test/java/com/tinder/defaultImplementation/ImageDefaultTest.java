package com.tinder.defaultImplementation;

import com.tinder.exception.ConfigFileException;
import com.tinder.exception.ErrorConnectionToDataBase;
import com.tinder.exception.ImageException;
import com.tinder.exception.UserException;
import org.junit.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


public class ImageDefaultTest {
    String email = "vashchenko@gmail.com";
    String firstName = "vashchenko";

    @Test
    public void uploadImgToCloudinaryUseUri() throws ConfigFileException, ImageException, ErrorConnectionToDataBase, UserException {
        //given
        String imgUrl = "https://i.pinimg.com/236x/f4/d2/96/f4d2961b652880be432fb9580891ed62.jpg";
        final ImageDefault IMAGE_DEFAULT = ImageDefault.getInstance();
        final UserDefault USER_DEFAULT = UserDefault.getInstance();
        //when
        final UUID userFromGoogle = USER_DEFAULT.createUserFromGoogle(firstName, email);
        final String publicId = IMAGE_DEFAULT.uploadImgUseUri(imgUrl);
        assertThat(publicId).isNotNull().isNotEmpty();

        final boolean isSavePublicId = IMAGE_DEFAULT.saveImgUrlInDataBaseByUserID(userFromGoogle, publicId);
        assertThat(isSavePublicId).isTrue();
        final boolean isDropPublicId = IMAGE_DEFAULT.dropImgUrlFromDataBaseByUserID(publicId);
        assertThat(isDropPublicId).isTrue();

        final boolean b = IMAGE_DEFAULT.dropImgByPublicId(publicId);
        assertThat(b).isTrue();
        //than
    }

}