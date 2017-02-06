package com.cs499.myapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Buyer.
 */
@Entity
@Table(name = "buyer")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Buyer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "address")
    private String address;

    @OneToOne
    @JoinColumn(unique = true)
    private Item buys;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "buyer_transacts",
               joinColumns = @JoinColumn(name="buyers_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="transacts_id", referencedColumnName="ID"))
    private Set<Seller> transacts = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Buyer name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public Buyer address(String address) {
        this.address = address;
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Item getBuys() {
        return buys;
    }

    public Buyer buys(Item item) {
        this.buys = item;
        return this;
    }

    public void setBuys(Item item) {
        this.buys = item;
    }

    public Set<Seller> getTransacts() {
        return transacts;
    }

    public Buyer transacts(Set<Seller> sellers) {
        this.transacts = sellers;
        return this;
    }

    public Buyer addTransacts(Seller seller) {
        transacts.add(seller);
        return this;
    }

    public Buyer removeTransacts(Seller seller) {
        transacts.remove(seller);
        return this;
    }

    public void setTransacts(Set<Seller> sellers) {
        this.transacts = sellers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Buyer buyer = (Buyer) o;
        if (buyer.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, buyer.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Buyer{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", address='" + address + "'" +
            '}';
    }
}
