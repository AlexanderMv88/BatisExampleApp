package org.BatisExampleApp.entity;


import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Owner extends NamedEntity {


    private List<Pet> pets;


    public Owner addPet(Pet pet){
        if (pets==null) pets = new ArrayList<>();
        pets.add(pet);
        return this;
    }

    public Owner(String name) {
        this.name = name;
    }

    public Owner(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Owner() {

    }

    @Override
    public String toString() {
        return "Owner{" +
                "pets=" + pets +
                ", id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
