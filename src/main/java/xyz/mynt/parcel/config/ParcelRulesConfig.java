package xyz.mynt.parcel.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Julian Jupiter
 */
@Configuration
@ConfigurationProperties(prefix = "parcel.rules")
public class ParcelRulesConfig {
    private Double reject = 50.0;
    private Double heavyParcel = 10.0;
    private Double smallParcel = 1500.0;
    private Double mediumParcel = 2500.0;

    public Double getReject() {
        return reject;
    }

    public void setReject(Double reject) {
        this.reject = reject;
    }

    public Double getHeavyParcel() {
        return heavyParcel;
    }

    public void setHeavyParcel(Double heavyParcel) {
        this.heavyParcel = heavyParcel;
    }

    public Double getSmallParcel() {
        return smallParcel;
    }

    public void setSmallParcel(Double smallParcel) {
        this.smallParcel = smallParcel;
    }

    public Double getMediumParcel() {
        return mediumParcel;
    }

    public void setMediumParcel(Double mediumParcel) {
        this.mediumParcel = mediumParcel;
    }
}
