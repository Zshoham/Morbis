package com.morbis.model.member.entity;

import lombok.*;

import javax.persistence.Entity;

@Entity
@Data
@NoArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = true)
public class Fan extends Member {
}
