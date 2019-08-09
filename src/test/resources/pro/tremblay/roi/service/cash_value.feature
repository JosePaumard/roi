Feature: The cash value of a collection of positions can be computed

    Scenario: The cash value of an empty position is 0 CAD
        Given an empty position
        When the cash value is computed in CAD
        Then the resulting cash value is 0

    Scenario: The cash value of an empty position is 0 USD
        Given an empty position
        When the cash value is computed in USD
        Then the resulting cash value is 0

    Scenario: The cash value of a single position in CAD can be computed in CAD
        Given the following position
            | amount | currency |
            | 10     | CAD      |
        When the cash value is computed in CAD
        Then the resulting cash value is 10

    Scenario: The cash value of several positions in CAD can be computed in CAD
        Given the following position
            | amount | currency |
            | 10     | CAD      |
            | 20     | CAD      |
        When the cash value is computed in CAD
        Then the resulting cash value is 30

    Scenario: The cash value of a single position in USD can be computed in USD
        Given the following position
            | amount | currency |
            | 10     | USD      |
        When the cash value is computed in USD
        Then the resulting cash value is 10

    Scenario: The cash value of several positions in USD can be computed in USD
        Given the following position
            | amount | currency |
            | 10     | USD      |
            | 20     | USD      |
        When the cash value is computed in USD
        Then the resulting cash value is 30

    Scenario: The cash value of several positions in USD and in CAD can be computed in CAD
        Given the following position
            | amount | currency |
            | 10     | CAD      |
            | 10     | USD      |
        When the cash value is computed in CAD
        Then the resulting cash value is 25

    Scenario: The cash value of several positions in USD and in CAD can be computed in USD
        Given the following position
            | amount | currency |
            | 10     | CAD      |
            | 10     | USD      |
        When the cash value is computed in USD
        Then the resulting cash value is 16.6667
