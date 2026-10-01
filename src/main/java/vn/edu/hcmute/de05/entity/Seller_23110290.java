package vn.edu.hcmute.de05.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Seller")
public class Seller_23110290 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sellerId")
    private Integer sellerId;

    @Column(name = "sellername", nullable = false, length = 50)
    private String sellername;

    @Column(name = "images", length = 500)
    private String images;

    @Column(name = "status", nullable = false)
    private Integer status = 1;

    public Seller_23110290() {
    }

    public Integer getSellerId() {
        return sellerId;
    }

    public void setSellerId(Integer sellerId) {
        this.sellerId = sellerId;
    }

    public String getSellername() {
        return sellername;
    }

    public void setSellername(String sellername) {
        this.sellername = sellername;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}