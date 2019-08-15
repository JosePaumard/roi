Feature: The security value of security positions can be calculated


    Scenario Template: The security value of an empty list of positions is 0
        Given an empty collection of positions
        When the security value is calculated for currency <security value currency>
        Then the security value is 0
        Examples:
            | security value currency |
            | CAD                     |
            | USD                     |


    Scenario Template: The security value of a position with no corresponding price is 0
        Given a position without a price and a currency of <position currency>
        When the security value is calculated for currency <security value currency>
        Then the security value is 0
        Examples:
            | position currency | security value currency |
            | CAD               | CAD                     |
            | USD               | USD                     |
            | CAD               | USD                     |
            | USD               | CAD                     |


    Scenario Template: The security value of a position and a corresponding price can be computed
        Given a position with a price <price> a currency of <position currency> a quantity of <quantity> at date <date>
        When the security value is calculated for currency <security value currency>
        Then the security value is <expected security value>
        Examples:
            | price | position currency | quantity | date       | security value currency | expected security value |
            | 0     | CAD               | 1        | 2019-07-13 | CAD                     | 10                      |
            | 10    | CAD               | 1        | 2019-07-13 | CAD                     | 10                      |
            | 0     | USD               | 1        | 2019-07-13 | USD                     | 10                      |
            | 10    | USD               | 1        | 2019-07-13 | USD                     | 10                      |
            | 0     | CAD               | 1        | 2019-07-13 | USD                     | 6.6667                  |
            | 10    | CAD               | 1        | 2019-07-13 | USD                     | 6.6667                  |
            | 0     | USD               | 1        | 2019-07-13 | CAD                     | 15                      |
            | 10    | USD               | 1        | 2019-07-13 | CAD                     | 15                      |
