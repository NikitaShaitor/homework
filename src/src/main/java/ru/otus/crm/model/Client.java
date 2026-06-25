package ru.otus.crm.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "client")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    @Version
    private int version;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id")
    private Address address;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Phone> phones;

    protected Client() {
        this.phones = new ArrayList<>();
    }

    public Client(String name) {
        this();
        this.name = name;
    }

    public Client(long id, String name) {
        this(name);
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        if (address == null) {
            if (this.address != null) {
                this.address.setClient(null);
            }
            this.address = null;
            return;
        }
        this.address = address;
        address.setClient(this);
    }

    public List<Phone> getPhones() {
        return phones;
    }

    public void addPhone(Phone phone) {
        Objects.requireNonNull(phone);
        phones.add(phone);
        phone.setClient(this);
    }

    public void removePhone(Phone phone) {
        Objects.requireNonNull(phone);
        phones.remove(phone);
        phone.setClient(null); // Разрываем обратную связь!
    }


    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address=" + address +
                ", phones=" + phones +
                '}';
    }
}