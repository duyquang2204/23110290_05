package vn.edu.hcmute.de05.entity;

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
@Table(name = "Users")
public class User_23110290 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userId")
    private Integer userId;

    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String username;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "fullname", nullable = false, length = 50)
    private String fullname;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Column(name = "images", length = 500)
    private String images;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "status", nullable = false)
    private Integer status = 0;

    @Column(name = "code", length = 50)
    private String code;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "roleId", nullable = false)
    private UserRole_23110290 role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sellerId")
    private Seller_23110290 seller;

    public User_23110290() {
    }

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getFullname() { return fullname; }
    public void setFullname(String fullname) { this.fullname = fullname; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getImages() { return images; }
    public void setImages(String images) { this.images = images; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public UserRole_23110290 getRole() { return role; }
    public void setRole(UserRole_23110290 role) { this.role = role; }

    public Seller_23110290 getSeller() { return seller; }
    public void setSeller(Seller_23110290 seller) { this.seller = seller; }
}