package com.jpabook.jpamarket.domain;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
public class Address { // 생성자를 통해 초기화하고 불변한 클래스를 위해 Setter 추가안함

    private String city;
    private String street;
    private String zipcode;

    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }

    protected Address() { // JPA 스펙상 엔티티나 임베디드 타입은 기본 생성자 생성해야함
    }
}
