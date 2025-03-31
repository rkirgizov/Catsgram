package ru.yandex.practicum.catsgram;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
class Person {
    private String firstName;
    private String lastName;
    private int age;
    @EqualsAndHashCode.Exclude
    private String phone;

    public Person(String firstName, String lastName, int age, String phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.phone = phone;
    }
}

public class Practicum {
    public static void main(String[] args) {
        Person roman1 = new Person("Roman", "Igorev", 38, "+78889991234");
        Person roman2 = new Person("Roman", "Igorev", 38, "");
        if(roman1.equals(roman2)) {
            System.out.println("Это один и тот же человек");
        } else {
            System.out.println("Это разные люди");
        }
    }
}