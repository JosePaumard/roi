Feature: The exchange rate can be calculated for a valid transaction


    Scenario Template: The exchange rate is 0 for a transaction without a currency
        Given a transaction of amount <amount> with no defined currency and a trade date <trade date>
        When the exchange rate is computed in <exchange rate currency>
        Then the calculated exchange rate is <calculated exchange rate>
        Examples:
            | amount | trade date | exchange rate currency | calculated exchange rate |
            | 0      | 2019-07-13 | CAD                    | 0                        |
            | 10     | 2019-07-13 | CAD                    | 0                        |
            | 0      | 2019-07-13 | USD                    | 0                        |
            | 10     | 2019-07-13 | USD                    | 0                        |


    Scenario Template: The exchange rate is 0 for a transaction without a trade date
        Given a transaction of amount <amount> <currency> and no trade date
        When the exchange rate is computed in <exchange rate currency>
        Then the calculated exchange rate is <calculated exchange rate>
        Examples:
            | amount | currency | exchange rate currency | calculated exchange rate |
            | 0      | CAD      | CAD                    | 0                        |
            | 10     | CAD      | CAD                    | 0                        |
            | 0      | USD      | USD                    | 0                        |
            | 10     | USD      | USD                    | 0                        |


    Scenario Template: The exchange rate can be computed for a valid transaction
        Given a transaction of amount <amount> <currency> and a trade date <trade date>
        When the exchange rate is computed in <exchange rate currency>
        Then the calculated exchange rate is <calculated exchange rate>
        Examples:
            | amount | currency | trade date | exchange rate currency | calculated exchange rate |
            | 0      | CAD      | 2019-07-13 | CAD                    | 1                        |
            | 10     | CAD      | 2019-07-13 | CAD                    | 1                        |
            | 0      | USD      | 2019-07-13 | USD                    | 1                        |
            | 10     | USD      | 2019-07-13 | USD                    | 1                        |
            | 0      | CAD      | 2019-07-13 | USD                    | 0.6667                   |
            | 10     | CAD      | 2019-07-13 | USD                    | 0.6667                   |
            | 0      | USD      | 2019-07-13 | CAD                    | 1.5                      |
            | 10     | USD      | 2019-07-13 | CAD                    | 1.5                      |

