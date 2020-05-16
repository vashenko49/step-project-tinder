package com.tinder.model;

import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
@Builder
public class AccountTinder {
    private final UUID userid;
    private final String firstname;
    private final int age;
    private final  String interests;
    private final String gender;
    private final String aboutme;
    private final int maxdistance;
    private final int maxage;
    private final String imgurl;
    private final Date dateregistration;
    private final  Date lastvisit;
    private final float locationlatitudex;
    private final float locationlongitudey;
}
