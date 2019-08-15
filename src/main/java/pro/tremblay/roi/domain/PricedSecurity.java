package pro.tremblay.roi.domain;

import java.math.BigDecimal;

public class PricedSecurity {

    private Security security;
    private BigDecimal price;

    public PricedSecurity(Security security, BigDecimal price) {
        this.security = security;
        this.price = price;
    }

    public Security getSecurity() {
        return security;
    }

    public void setSecurity(Security security) {
        this.security = security;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
