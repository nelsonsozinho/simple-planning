package com.planning.budget.service.dto;

import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;


/**
 * A DTO for the ManagerAccount entity.
 */
public class ManagerAccountDTO implements Serializable {

    private Long id;

    @NotNull
    private Double value;

    @NotNull
    private String description;

    private LocalDate maturity;

    private Boolean isPaid;

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

    public void setValue(Double value) {
        this.value = value;
    }
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public LocalDate getMaturity() {
        return maturity;
    }

    public void setMaturity(LocalDate maturity) {
        this.maturity = maturity;
    }
    public Boolean getIsPaid() {
        return isPaid;
    }

    public void setIsPaid(Boolean isPaid) {
        this.isPaid = isPaid;
    }
    public String getName() {
        return name;
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

        ManagerAccountDTO managerAccountDTO = (ManagerAccountDTO) o;

        if ( ! Objects.equals(id, managerAccountDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ManagerAccountDTO{" +
            "id=" + id +
            ", value='" + value + "'" +
            ", description='" + description + "'" +
            ", maturity='" + maturity + "'" +
            ", isPaid='" + isPaid + "'" +
            ", name='" + name + "'" +
            '}';
    }
}
