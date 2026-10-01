package vn.edu.hcmute.de05.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "Product")
public class Product_23110290 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "productId")
    private Integer productId;

    @Column(name = "productName", nullable = false, length = 200)
    private String productName;

    @Column(name = "productCode", nullable = false, unique = true)
    private Long productCode;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "categoryId", nullable = false)
    private Category_23110290 category;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "price", nullable = false, precision = 15, scale = 2)
    private BigDecimal price;

    @Column(name = "amount", nullable = false)
    private Integer amount = 0;

    @Column(name = "stock", nullable = false)
    private Integer stock = 0;

    @Column(name = "images", length = 500)
    private String images;

    @Column(name = "wishlist", nullable = false)
    private Integer wishlist = 0;

    @Column(name = "status", nullable = false)
    private Integer status = 1;

    @Column(name = "createDate", nullable = false)
    private LocalDate createDate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sellerId", nullable = false)
    private Seller_23110290 seller;

    public Product_23110290() {
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Long getProductCode() {
        return productCode;
    }

    public void setProductCode(Long productCode) {
        this.productCode = productCode;
    }

    public Category_23110290 getCategory() {
        return category;
    }

    public void setCategory(Category_23110290 category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public Integer getWishlist() {
        return wishlist;
    }

    public void setWishlist(Integer wishlist) {
        this.wishlist = wishlist;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public LocalDate getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }

    public Seller_23110290 getSeller() {
        return seller;
    }

    public void setSeller(Seller_23110290 seller) {
        this.seller = seller;
    }
}