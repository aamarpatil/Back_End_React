package net.geico.revature.bootcamp.model;

import java.util.Objects;

public class LoomBands {

    public int loomId;

    public String loomName; //title

    public float loomPrice;

    public String loomSellerName;



    public LoomBands(){


    }

    public LoomBands(int loomId, String loomName, float loomPrice, String loomSellerName) {
        this.loomId = loomId;
        this.loomName = loomName;
        this.loomPrice = loomPrice;
        this.loomSellerName = loomSellerName;
    }

    public int getLoomId() {
        return loomId;
    }

    public void setLoomId(int loomId) {
        this.loomId = loomId;
    }

    public String getLoomName() {
        return loomName;
    }

    public void setLoomName(String loomName) {
        this.loomName = loomName;
    }

    public float getLoomPrice() {
        return loomPrice;
    }

    public void setLoomPrice(float loomPrice) {
        this.loomPrice = loomPrice;
    }

    public String getLoomSellerName() {
        return loomSellerName;
    }

    public void setLoomSellerName(String loomSellerName) {
        this.loomSellerName= loomSellerName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoomBands loomBands = (LoomBands) o;
        return loomId == loomBands.loomId && Float.compare(loomPrice, loomBands.loomPrice) == 0 && Objects.equals(loomName, loomBands.loomName) && Objects.equals(loomSellerName, loomBands.loomSellerName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(loomId, loomName, loomPrice, loomSellerName);
    }

    @Override
    public String toString() {
        return "LoomBands{" +
                "loomId=" + loomId +
                ", loomName='" + loomName + '\'' +
                ", loomPrice=" + loomPrice +
                ", loomSellerName='" + loomSellerName + '\'' +
                '}';
    }
}
