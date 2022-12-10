package com.example.mobilele.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "brands")
public class BrandEntity extends BaseEntity{

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "brand",
    fetch = FetchType.EAGER,
    cascade = CascadeType.ALL)
    private List<ModelEntity> models = new ArrayList<>();

    @Override
    public String toString() {
        return "BrandEntity{" +
                "name='" + name + '\'' +
                ", models=" + models +
                '}';
    }
}
