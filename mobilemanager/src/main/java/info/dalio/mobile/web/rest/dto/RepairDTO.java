package info.dalio.mobile.web.rest.dto;

import org.joda.time.DateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import info.dalio.mobile.domain.enumeration.Brand;

/**
 * A DTO for the Repair entity.
 */
public class RepairDTO implements Serializable {

    private Long id;

    @NotNull
    private String imei;

    @NotNull
    private String serialno;

    private Brand brand;

    private DateTime entryDate;

    private Boolean closed;

    private String comment;

    private BigDecimal price;

    private Long clientId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getSerialno() {
        return serialno;
    }

    public void setSerialno(String serialno) {
        this.serialno = serialno;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public DateTime getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(DateTime entryDate) {
        this.entryDate = entryDate;
    }

    public Boolean getClosed() {
        return closed;
    }

    public void setClosed(Boolean closed) {
        this.closed = closed;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        RepairDTO repairDTO = (RepairDTO) o;

        if ( ! Objects.equals(id, repairDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "RepairDTO{" +
                "id=" + id +
                ", imei='" + imei + "'" +
                ", serialno='" + serialno + "'" +
                ", brand='" + brand + "'" +
                ", entryDate='" + entryDate + "'" +
                ", closed='" + closed + "'" +
                ", comment='" + comment + "'" +
                ", price='" + price + "'" +
                '}';
    }
}
