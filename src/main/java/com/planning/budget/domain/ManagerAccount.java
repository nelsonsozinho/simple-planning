package com.planning.budget.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A ManagerAccount.
 */
@Entity
@Table(name = "manager_account")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ManagerAccount implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "value", nullable = false)
    private Double value;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "maturity")
    private LocalDate maturity;

    @Column(name = "is_paid")
    private Boolean isPaid;

    @Column(name = "name")
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getValue() {
        return value;
    }

    public ManagerAccount value(Double value) {
        this.value = value;
        return this;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public ManagerAccount description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getMaturity() {
        return maturity;
    }

    public ManagerAccount maturity(LocalDate maturity) {
        this.maturity = maturity;
        return this;
    }

    public void setMaturity(LocalDate maturity) {
        this.maturity = maturity;
    }

    public Boolean isIsPaid() {
        return isPaid;
    }

    public ManagerAccount isPaid(Boolean isPaid) {
        this.isPaid = isPaid;
        return this;
    }

    public void setIsPaid(Boolean isPaid) {
        this.isPaid = isPaid;
    }

    public String getName() {
        return name;
    }

    public ManagerAccount name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ManagerAccount managerAccount = (ManagerAccount) o;
        if(managerAccount.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, managerAccount.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ManagerAccount{" +
            "id=" + id +
            ", value='" + value + "'" +
            ", description='" + description + "'" +
            ", maturity='" + maturity + "'" +
            ", isPaid='" + isPaid + "'" +
            ", name='" + name + "'" +
            '}';
    }
}
