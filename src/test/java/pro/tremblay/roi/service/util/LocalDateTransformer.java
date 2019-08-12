package pro.tremblay.roi.service.util;

import cucumber.api.Transformer;

import java.time.LocalDate;

public class LocalDateTransformer extends Transformer<LocalDate> {

    @Override
    public LocalDate transform(String localDate) {
        return LocalDate.parse(localDate);
    }
}
