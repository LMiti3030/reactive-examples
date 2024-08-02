package mititelu_laura.sfg_reactive_brewery.domain;

import jakarta.persistence.*;
import jdk.jfr.DataAmount;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mititelu_laura.sfg_reactive_brewery.web.model.BeerStyleEnum;
import org.hibernate.annotations.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

@DataAmount
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Entity
public class Beer {
    @Id
    @GeneratedValue(generator = "UUID")
    @UuidGenerator(style = UuidGenerator.Style.AUTO)
    @Column(length = 36, columnDefinition = "varchar", updatable = false, nullable = false)
    private UUID id;

    @Version
    private Long version;

    private String beerName;
    private BeerStyleEnum beerStyle;
    private String upc;

    private Integer quantityOnHand;
    private BigDecimal price;

    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp createdDate;

    @UpdateTimestamp
    private Timestamp lastModifiedDate;

}
