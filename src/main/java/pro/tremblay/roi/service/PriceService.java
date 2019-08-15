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
package pro.tremblay.roi.service;

import org.apache.commons.collections4.map.MultiKeyMap;
import org.apache.commons.lang3.tuple.Pair;
import pro.tremblay.roi.domain.Currency;
import pro.tremblay.roi.domain.Position;
import pro.tremblay.roi.domain.PricedSecurity;
import pro.tremblay.roi.domain.Security;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class PriceService extends DependencyService {

    private final Collection<String> listOfErrors = new HashSet<>();
    private final Map<Security, BigDecimal> prices = new HashMap<>();

    //
    // Methods to call during test to record a fake behavior
    //

    public void addError(String symbol) {
        listOfErrors.add(symbol);
    }

    public void addPrice(Security security, BigDecimal price) {
        prices.put(security, price);
    }

    public void addPrice(PricedSecurity security) {
        prices.put(security.getSecurity(), security.getPrice());
    }

    ///////////////////////

    /**
     * All the prices for every position in the date range.
     *
     * @param positions list of positions we want prices for
     * @param firstDayOfPeriod first day for which we want a price
     * @param lastDay last day for which we want a price
     * @return a pair with a multimap of (date, symbol, currency) as the key and the price as the value,
     * and a list of error messages for every position we haven't found a price for
     */
    public Pair<MultiKeyMap<Object, BigDecimal>, Collection<String>> getPricesForPositions(List<Position> positions, LocalDate firstDayOfPeriod, LocalDate lastDay) {

        MultiKeyMap<Object, BigDecimal> allPrices = new MultiKeyMap<>();

        positions.forEach(position  -> {
            Security security = position.getSecurity();
            Currency currency = security.getCurrency();
            String symbol = security.getSymbol();

            BigDecimal price = prices.get(security);

            LocalDate workDate = firstDayOfPeriod;
            while (workDate.isBefore(lastDay.plusDays(1))) {
                allPrices.put(workDate, symbol, currency, price);
                workDate = workDate.plusDays(1);
            }
        });

        return Pair.of(allPrices, listOfErrors);
    }
}
