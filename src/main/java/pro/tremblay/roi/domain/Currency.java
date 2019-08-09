/*
 * Copyright 2019-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package pro.tremblay.roi.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * All supported currencies
 */
public enum Currency {
    CAD("1"),
    USD("1.5");

    /** X-rate. Of course in real life this will not be hardcoded */
    private final BigDecimal rate;

    Currency(String rate) {
        this.rate = new BigDecimal(rate).setScale(4, RoundingMode.HALF_UP);
    }

    public BigDecimal getRate() {
        return rate;
    }

    public BigDecimal convertTo(Currency destination) {
        BigDecimal divide = rate.divide(destination.rate, 12, RoundingMode.HALF_UP);
        return divide;
    }
}
