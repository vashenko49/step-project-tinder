package com.tinder.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class AccountUser {
    private String first_name;
    private int age;
    private String interests;
    private String gender;
    private String genderpartner;
    private String aboutMe;
    private int max_distance;
    private int min_age;
    private int max_age;
    @JsonIgnore
    private String password;
    private List<String> imagesList;
    private String img_url;
    @JsonIgnore
    private UUID userId;
}
