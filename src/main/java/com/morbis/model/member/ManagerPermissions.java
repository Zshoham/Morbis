package com.morbis.model.member;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class ManagerPermissions {
    @Id
    @GeneratedValue
    private int id;
}
