Feature: The commission of a collection of transactions can be computed

    A transaction fee has four parameters:
    - an amount
    - a fee
    - a currency
    - a date


    Scenario Template: The commission of an empty collection of transaction is 0
        Given an empty collection of transactions
        When the commission is calculated in <commission currency>
        Then the calculated commission is 0 for this transaction
        Examples:
            | commission currency |
            | CAD                 |
            | USD                 |


    Scenario Template: The commission is 0 when the fee of a transaction is null
        Given the following list of transaction fees
            | fee | currency | date       |
            | 0   | null     | 2019-07-13 |
            | 10  | null     | 2019-07-13 |
        When the commission is calculated in <commission currency> for each transaction
        Then the calculated commission is 0 for all the transactions
        Examples:
            | commission currency |
            | CAD                 |
            | USD                 |


    Scenario Template: The commission is 0 when the currency of the fee is null
        Given the following list of transaction fees
            | fee  | currency | date       |
            | null | CAD      | 2019-07-13 |
            | null | USD      | 2019-07-13 |
        When the commission is calculated in <commission currency> for each transaction
        Then the calculated commission is 0 for all the transactions
        Examples:
            | commission currency |
            | CAD                 |
            | USD                 |


    Scenario Template: The commission is 0 when the date of the transaction is null
        Given the following list of transaction fees
            | fee | currency | date |
            | 0   | CAD      | null |
            | 10  | CAD      | null |
            | 0   | USD      | null |
            | 10  | USD      | null |
        When the commission is calculated in <commission currency> for each transaction
        Then the calculated commission is 0 for all the transactions
        Examples:
            | commission currency |
            | CAD                 |
            | USD                 |


    Scenario: The commission corresponds to the fee when it is defined in the same currency
        Given the following transaction fee
            | fee | currency | date       |
            | 10  | CAD      | 2019-07-13 |
        When the commission is calculated in CAD
        Then the calculated commission is 10 for this transaction


    Scenario Template: The commission corresponds to the fee when it is defined in the same currency
        Given a transaction of fee <fee> <fee currency> dated from <trade date>
        When the commission is calculated in <commission currency>
        Then the calculated commission is <expected commission> for this transaction
        Examples:
            | fee | fee currency | trade date | commission currency | expected commission |
            | 0   | CAD          | 2019-07-13 | CAD                 | 0                   |
            | 10  | CAD          | 2019-07-13 | CAD                 | 10                  |
            | 0   | USD          | 2019-07-13 | USD                 | 0                   |
            | 10  | USD          | 2019-07-13 | USD                 | 10                  |


    Scenario Template: The commission corresponds to the converted fee when it is defined in another currency
        Given a transaction of fee <fee> <fee currency> dated from <trade date>
        When the commission is calculated in <commission currency>
        Then the calculated commission is <expected commission> for this transaction
        Examples:
            | fee | fee currency | trade date | commission currency | expected commission |
            | 0   | USD          | 2019-07-13 | CAD                 | 0                   |
            | 10  | USD          | 2019-07-13 | CAD                 | 15                  |
            | 0   | CAD          | 2019-07-13 | USD                 | 0                   |
            | 10  | CAD          | 2019-07-13 | USD                 | 6.6667              |


    Scenario Template: The commission of a deposit transaction can be computed
        Given a transaction of type deposit of fee <fee> <fee currency> dated from <trade date>
        When the commission is calculated in <commission currency>
        Then the calculated commission is <expected commission> for this transaction
        Examples:
            | fee | fee currency | trade date | commission currency | expected commission |
            | 0   | USD          | 2019-07-13 | CAD                 | 0                   |
            | 10  | USD          | 2019-07-13 | CAD                 | 15                  |
            | 0   | CAD          | 2019-07-13 | USD                 | 0                   |
            | 10  | CAD          | 2019-07-13 | USD                 | 6.6667              |


    Scenario Template: The commission of a withdrawal transaction can be computed
        Given a transaction of type deposit of fee <fee> <fee currency> dated from <trade date>
        When the commission is calculated in <commission currency>
        Then the calculated commission is <expected commission> for this transaction
        Examples:
            | fee | fee currency | trade date | commission currency | expected commission |
            | 0   | USD          | 2019-07-13 | CAD                 | 0                   |
            | 10  | USD          | 2019-07-13 | CAD                 | 15                  |
            | 0   | CAD          | 2019-07-13 | USD                 | 0                   |
            | 10  | CAD          | 2019-07-13 | USD                 | 6.6667              |


    Scenario Template: The commission of a sell transaction can be computed
        Given a transaction of type sell of fee <fee> <fee currency> dated from <trade date>
        When the commission is calculated in <commission currency>
        Then the calculated commission is <expected commission> for this transaction
        Examples:
            | fee | fee currency | trade date | commission currency | expected commission |
            | 0   | USD          | 2019-07-13 | CAD                 | 0                   |
            | 10  | USD          | 2019-07-13 | CAD                 | 15                  |
            | 0   | CAD          | 2019-07-13 | USD                 | 0                   |
            | 10  | CAD          | 2019-07-13 | USD                 | 6.6667              |


    Scenario Template: The commission of a buy transaction can be computed
        Given a transaction of type buy of fee <fee> <fee currency> dated from <trade date>
        When the commission is calculated in <commission currency>
        Then the calculated commission is <expected commission> for this transaction
        Examples:
            | fee | fee currency | trade date | commission currency | expected commission |
            | 0   | USD          | 2019-07-13 | CAD                 | 0                   |
            | 10  | USD          | 2019-07-13 | CAD                 | 15                  |
            | 0   | CAD          | 2019-07-13 | USD                 | 0                   |
            | 10  | CAD          | 2019-07-13 | USD                 | 6.6667              |


    Scenario: The commission of a collection of different transaction types can be computed
        Given the following list of transaction fees of different types
            | fee | currency | type       | date       |
            | 10  | CAD      | withdrawal | 2019-07-13 |
            | 10  | CAD      | deposit    | 2019-07-13 |
            | 10  | CAD      | sell       | 2019-07-13 |
            | 10  | CAD      | buy        | 2019-07-13 |
        When the commission is calculated in CAD
        Then the calculated commission is 40 for this transaction


    Scenario: The commission of a collection of different transaction types can be computed
        Given the following list of transaction fees of different types
            | fee | currency | type       | date       |
            | 10  | USD      | withdrawal | 2019-07-13 |
            | 10  | USD      | deposit    | 2019-07-13 |
            | 10  | USD      | sell       | 2019-07-13 |
            | 10  | USD      | buy        | 2019-07-13 |
        When the commission is calculated in USD
        Then the calculated commission is 40 for this transaction


    Scenario: The commission of a collection of different transaction types can be computed
        Given the following list of transaction fees of different types
            | fee | currency | type       | date       |
            | 10  | CAD      | withdrawal | 2019-07-13 |
            | 10  | CAD      | deposit    | 2019-07-13 |
            | 10  | CAD      | sell       | 2019-07-13 |
            | 10  | CAD      | buy        | 2019-07-13 |
        When the commission is calculated in USD
        Then the calculated commission is 26.6667 for this transaction


    Scenario: The commission of a collection of different transaction types can be computed
        Given the following list of transaction fees of different types
            | fee | currency | type       | date       |
            | 10  | USD      | withdrawal | 2019-07-13 |
            | 10  | USD      | deposit    | 2019-07-13 |
            | 10  | USD      | sell       | 2019-07-13 |
            | 10  | USD      | buy        | 2019-07-13 |
        When the commission is calculated in CAD
        Then the calculated commission is 60.0000 for this transaction

