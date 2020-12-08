package jpabook.jpashop.domain;

import lombok.Getter;

import javax.persistence.Embeddable;

@Embeddable //값타입클래스임을 선언.
@Getter //값타입은 기본적으로 데이터가 변경이 되면 안된다.
// 그래서 게터만 제공하고, 생성자에서 모두 값을 초기화해서 변경 부가능한 클래스를 만들자. 굳이 만들려면 복사를 하거나 똑같은 객체를 새로 만들어야 함.
public class Address {

    private String city;
    private String street;
    private String zipcode;

    //public보다는 protected가 더 안전하다.
    protected Address(){}

    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
}
