package com.tinder.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AccountUser {
    private final String first_name;
    private final int age;
    private final String interests;
    private final String gender;
    private final String genderpartner;
    private final String aboutMe;
    private final int max_distance;
    private final int min_age;
    private final int max_age;
    @JsonIgnore private final String password;
    private List<String> imagesList;
}
