Feature: A report can be generated


    Scenario: An empty report is empty
        Given a starting date of 2018-07-13 and a current date of 2019-07-13
        When an empty report is generated in CAD
        Then the report has the following values
            | initial | current | fee  | net deposits | net variation amount | net variation percentage |
            | 0.00    | 0.00    | 0.00 | 0.00         | 0.00                 | 0.0000                   |
        And there are 366 values per day
        And there is no anomaly
        And the price is 0 for the day 0 2018-07-13
        And the price is 0 for the day 365 2019-07-13


    Scenario: A report does not modify its initial entities
        Given the following securities
            | symbol | price | currency |
            | AAA    | 2     | CAD      |
        And the following accounts
            | name | cash | currency |
            | 123  | 10   | CAD      |
        And the following positions
            | security | quantity |
            | AAA      | 100      |
        And the following transactions
            | account_name | transaction_type | amount | currency | fee | quantity | trade_date | security |
            | 123          | sell             | 1      | CAD      | 2   | -10      | 2019-01-13 | AAA      |
        When the report is generated in CAD between 2018-07-13 and 2019-07-13
        Then the report has the following securities
            | symbol | price | currency |
            | AAA    | 2     | CAD      |
        And the report has the following accounts
            | name | cash | currency |
            | 123  | 10   | CAD      |
        And the report has the following positions
            | security | quantity |
            | AAA      | 100      |
        And the report has the following transactions
            | account_name | transaction_type | amount | currency | fee | quantity | trade_date | security |
            | 123          | sell             | 1      | CAD      | 2   | -10      | 2019-01-13 | AAA      |


    Scenario: A report guarantees that the money at the beginning is still there at the end of period
        Given the following accounts
            | name | cash | currency |
            | 123  | 10   | CAD      |
        When the report is generated in CAD between 2018-07-13 and 2019-07-13
        Then the report has the following values
            | initial | current | fee  | net deposits | net variation amount | net variation percentage |
            | 10.00   | 10.00   | 0.00 | 0.00         | 0.00                 | 0.0000                   |
        And there are 366 values per day
        And the price is 0 for the day 0 2018-07-13
        And the price is 0 for the day 365 2019-07-13


    Scenario: Some cash can be added
        Given the following accounts
            | name | cash | currency |
            | 123  | 10   | CAD      |
        And the following transactions
            | account_name | transaction_type | amount | currency | fee | quantity | trade_date | security |
            | 123          | deposit          | 1      | CAD      | 0   | 0        | 2019-01-13 | null     |
        When the report is generated in CAD between 2018-07-13 and 2019-07-13
        Then the report has the following values
            | initial | current | fee  | net deposits | net variation amount | net variation percentage |
            | 9.00    | 10.00   | 0.00 | 1.00         | 1.00                 | 0.1111                   |
        And there are 366 values per day
        And there is no anomaly
        And the price is 9 for the day 0 2018-07-13
        And the price is 9 for the day 184 2019-01-13
        And the price is 10 for the day 185 2019-01-14
        And the price is 10 for the day 365 2019-07-13


    Scenario: A position can be added
        Given the following securities
            | symbol | price | currency |
            | AAA    | 2     | CAD      |
        And the following accounts
            | name | cash | currency |
            | 123  | 10   | CAD      |
        And the following positions
            | security | quantity |
            | AAA      | 100      |
        And the following transactions
            | account_name | transaction_type | amount | currency | fee | quantity | trade_date | security |
            | 123          | buy              | -5     | CAD      | 0   | 100      | 2019-01-13 | AAA      |
        When the report is generated in CAD between 2018-07-13 and 2019-07-13
        Then the report has the following values
            | initial | current | fee  | net deposits | net variation amount | net variation percentage |
            | 15.00   | 210.00  | 0.00 | 0.00         | 195.00               | 13.0000                  |
        And there are 366 values per day
        And there is no anomaly
        And the price is 15 for the day 0 2018-07-13
        And the price is 15 for the day 184 2019-01-13
        And the price is 210 for the day 185 2019-01-14
        And the price is 210 for the day 365 2019-07-13

    Scenario: A transaction with fees can be added
        Given the following securities
            | symbol | price | currency |
            | AAA    | 2     | CAD      |
        And the following accounts
            | name | cash | currency |
            | 123  | 1000 | CAD      |
        And the following transactions
            | account_name | transaction_type | amount | currency | fee | quantity | trade_date | security |
            | 123          | buy              | -5     | CAD      | 5   | 100      | 2019-01-13 | AAA      |
            | 123          | buy              | -5     | CAD      | 15  | 100      | 2019-01-13 | AAA      |
            | 123          | buy              | -5     | CAD      | 20  | 100      | 2019-01-13 | AAA      |
        When the report is generated in CAD between 2018-07-13 and 2019-07-13
        Then the report has the following values
            | initial | current | fee   | net deposits | net variation amount | net variation percentage |
            | 1015.00 | 1000.00 | 40.00 | 0.00         | -15.00               | -0.0148                  |
