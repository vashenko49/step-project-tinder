package com.tinder.dataSource;

import com.tinder.exception.ConfigFileException;
import com.tinder.start.ConfigFile;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ConfigFileTest {
    @Test
    public void uploadConfigFile() throws ConfigFileException {
        //given
        ConfigFile configFile = ConfigFile.getInstance();
        //when
        //than
        assertThat(configFile).isInstanceOf(ConfigFile.class);
    }

    @Test
    public void  verificationOfTheKeyToTheData() throws ConfigFileException {
        //given
        ConfigFile configFile = ConfigFile.getInstance();
        //when
        //than
        assertThat(configFile.getValueByKey("db.user")).isEqualTo("vashchenko");
    }
}