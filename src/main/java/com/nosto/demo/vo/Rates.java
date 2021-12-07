package com.nosto.demo.vo;

import java.util.Map;
import java.util.Objects;

import lombok.Data;

@Data
public class Rates {

    private Map<String, String> rates;

    @Override
    public int hashCode() {
        return Objects.hash(rates);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Rates)) {
            return false;
        }

        return Objects.equals(((Rates) obj).getRates(), this.getRates());
    }
}
