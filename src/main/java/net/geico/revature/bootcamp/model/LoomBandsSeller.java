package net.geico.revature.bootcamp.model;

import java.util.Objects;

public class LoomBandsSeller {

    public String name;
    public int id;

    //deleted the arguments from the below constructor -- int i, String brown, float v, String john

    public LoomBandsSeller(){

    }

    public LoomBandsSeller(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoomBandsSeller that = (LoomBandsSeller) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "LoomBandsSeller{" +
                "sellerName='" + name + '\'' +
                '}';
    }
}
