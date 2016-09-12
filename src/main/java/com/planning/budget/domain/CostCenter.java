package com.planning.budget.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A CostCenter.
 */
@Entity
@Table(name = "cost_center")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class CostCenter implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "is_generate_revenue", nullable = false)
    private Boolean isGenerateRevenue;

    @ManyToOne
    private Budget budget;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public CostCenter name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public CostCenter description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean isIsGenerateRevenue() {
        return isGenerateRevenue;
    }

    public CostCenter isGenerateRevenue(Boolean isGenerateRevenue) {
        this.isGenerateRevenue = isGenerateRevenue;
        return this;
    }

    public void setIsGenerateRevenue(Boolean isGenerateRevenue) {
        this.isGenerateRevenue = isGenerateRevenue;
    }

    public Budget getBudget() {
        return budget;
    }

    public CostCenter budget(Budget budget) {
        this.budget = budget;
        return this;
    }

    public void setBudget(Budget budget) {
        this.budget = budget;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CostCenter costCenter = (CostCenter) o;
        if(costCenter.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, costCenter.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "CostCenter{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", description='" + description + "'" +
            ", isGenerateRevenue='" + isGenerateRevenue + "'" +
            '}';
    }
}
