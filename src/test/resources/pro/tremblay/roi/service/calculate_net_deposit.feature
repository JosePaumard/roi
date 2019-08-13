Feature: the net deposit of a collection of transactions can be calculated

    A transaction fee has four parameters:
    - an amount
    - a fee
    - a currency
    - a date


    Scenario Template: The net deposit of an empty collection of transactions is 0
        Given an empty collection of transactions
        When the net deposit is calculated in <currency>
        Then the calculated net deposit is 0
        Examples:
            | currency |
            | CAD      |
            | USD      |


    Scenario Template: The net deposit is 0 when the transaction has no amount
        Given the following list of transactions
            | amount | currency | date       | type       |
            | null   | CAD      | 2019-07-13 | deposit    |
            | null   | CAD      | 2019-07-13 | withdrawal |
            | null   | CAD      | 2019-07-13 | sell       |
            | null   | CAD      | 2019-07-13 | buy        |
            | null   | USD      | 2019-07-13 | deposit    |
            | null   | USD      | 2019-07-13 | withdrawal |
            | null   | USD      | 2019-07-13 | sell       |
            | null   | USD      | 2019-07-13 | buy        |
        When the net deposit is calculated in <commission currency> for each transaction
        Then the calculated net deposit is 0 for all the transactions
        Examples:
            | commission currency |
            | CAD                 |
            | USD                 |


    Scenario Template: The net deposit of a single deposit transaction can be calculated
        Given a transaction of amount <amount> <transaction currency> of type deposit at date <date>
        When the net deposit is calculated in <currency>
        Then the calculated net deposit is <expected net deposit>
        Examples:
            | amount | transaction currency | currency | date       | expected net deposit |
            | 0      | CAD                  | CAD      | 2019-07-13 | 0                    |
            | 10     | CAD                  | CAD      | 2019-07-13 | 10                   |
            | 0      | USD                  | USD      | 2019-07-13 | 0                    |
            | 10     | USD                  | USD      | 2019-07-13 | 10                   |
            | 0      | CAD                  | USD      | 2019-07-13 | 0                    |
            | 10     | CAD                  | USD      | 2019-07-13 | 6.6667               |
            | 0      | USD                  | CAD      | 2019-07-13 | 0                    |
            | 10     | USD                  | CAD      | 2019-07-13 | 15                   |


    Scenario Template: The net deposit of a single withdrawal transaction can be calculated
        Given a transaction of amount <amount> <transaction currency> of type deposit at date <date>
        When the net deposit is calculated in <currency>
        Then the calculated net deposit is <expected net deposit>
        Examples:
            | amount | transaction currency | currency | date       | expected net deposit |
            | 0      | CAD                  | CAD      | 2019-07-13 | 0                    |
            | 10     | CAD                  | CAD      | 2019-07-13 | 10                   |
            | 0      | USD                  | USD      | 2019-07-13 | 0                    |
            | 10     | USD                  | USD      | 2019-07-13 | 10                   |
            | 0      | CAD                  | USD      | 2019-07-13 | 0                    |
            | 10     | CAD                  | USD      | 2019-07-13 | 6.6667               |
            | 0      | USD                  | CAD      | 2019-07-13 | 0                    |
            | 10     | USD                  | CAD      | 2019-07-13 | 15                   |


    Scenario Template: The net deposit of a single sell transaction can be calculated
        Given a transaction of amount <amount> <transaction currency> of type deposit at date <date>
        When the net deposit is calculated in <currency>
        Then the calculated net deposit is <expected net deposit>
        Examples:
            | amount | transaction currency | currency | date       | expected net deposit |
            | 0      | CAD                  | CAD      | 2019-07-13 | 0                    |
            | 10     | CAD                  | CAD      | 2019-07-13 | 10                   |
            | 0      | USD                  | USD      | 2019-07-13 | 0                    |
            | 10     | USD                  | USD      | 2019-07-13 | 10                   |
            | 0      | CAD                  | USD      | 2019-07-13 | 0                    |
            | 10     | CAD                  | USD      | 2019-07-13 | 6.6667               |
            | 0      | USD                  | CAD      | 2019-07-13 | 0                    |
            | 10     | USD                  | CAD      | 2019-07-13 | 15                   |


    Scenario Template: The net deposit of a single buy transaction can be calculated
        Given a transaction of amount <amount> <transaction currency> of type deposit at date <date>
        When the net deposit is calculated in <currency>
        Then the calculated net deposit is <expected net deposit>
        Examples:
            | amount | transaction currency | currency | date       | expected net deposit |
            | 0      | CAD                  | CAD      | 2019-07-13 | 0                    |
            | 10     | CAD                  | CAD      | 2019-07-13 | 10                   |
            | 0      | USD                  | USD      | 2019-07-13 | 0                    |
            | 10     | USD                  | USD      | 2019-07-13 | 10                   |
            | 0      | CAD                  | USD      | 2019-07-13 | 0                    |
            | 10     | CAD                  | USD      | 2019-07-13 | 6.6667               |
            | 0      | USD                  | CAD      | 2019-07-13 | 0                    |
            | 10     | USD                  | CAD      | 2019-07-13 | 15                   |


    Scenario: The net deposit of a collection of transactions can be calculated
        Given the following list of transactions of different amounts and types
            | amount | currency | type       | date       |
            | 500    | CAD      | deposit    | 2019-07-13 |
            | -200   | CAD      | withdrawal | 2019-07-13 |
            | 50     | CAD      | sell       | 2019-07-13 |
            | 75     | CAD      | buy        | 2019-07-13 |
        When the net deposit is calculated in CAD
        Then the calculated net deposit is 300


    Scenario: The net deposit of a collection of transactions can be calculated
        Given the following list of transactions of different amounts and types
            | amount | currency | type       | date       |
            | 500    | CAD      | deposit    | 2019-07-13 |
            | -200   | CAD      | withdrawal | 2019-07-13 |
        When the net deposit is calculated in CAD
        Then the calculated net deposit is 300


    Scenario: The net deposit of a collection of transactions can be calculated
        Given the following list of transactions of different amounts and types
            | amount | currency | type       | date       |
            | 50     | CAD      | sell       | 2019-07-13 |
            | 75     | CAD      | buy        | 2019-07-13 |
        When the net deposit is calculated in CAD
        Then the calculated net deposit is 0
