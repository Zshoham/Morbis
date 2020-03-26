package com.morbis.model.member;

import lombok.*;

import javax.persistence.Entity;

@Entity
@Data
@NoArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = true)
public class Admin extends Member {

}
