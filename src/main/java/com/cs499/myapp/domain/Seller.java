package com.cs499.myapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Seller.
 */
@Entity
@Table(name = "seller")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Seller implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "account")
    private Integer account;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "seller_sells",
               joinColumns = @JoinColumn(name="sellers_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="sells_id", referencedColumnName="ID"))
    private Set<Item> sells = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Seller name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAccount() {
        return account;
    }

    public Seller account(Integer account) {
        this.account = account;
        return this;
    }

    public void setAccount(Integer account) {
        this.account = account;
    }

    public Set<Item> getSells() {
        return sells;
    }

    public Seller sells(Set<Item> items) {
        this.sells = items;
        return this;
    }

    public Seller addSells(Item item) {
        sells.add(item);
        return this;
    }

    public Seller removeSells(Item item) {
        sells.remove(item);
        return this;
    }

    public void setSells(Set<Item> items) {
        this.sells = items;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Seller seller = (Seller) o;
        if (seller.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, seller.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Seller{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", account='" + account + "'" +
            '}';
    }
}
