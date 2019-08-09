package pro.tremblay.roi.service.util;

import cucumber.api.Transformer;

import java.math.BigDecimal;

public class BigDecimalTransformer extends Transformer<BigDecimal> {
    @Override
    public BigDecimal transform(String number) {
        BigDecimal converted = new BigDecimal(number).setScale(4);
        return converted;
    }
}
