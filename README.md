No main method exists for this project. Code is tested via Junit in Intellij.

Design notes 
1. Normally I would load the tools and tools types into a database table and then maintain them like that, however for the sake of ease for this project, I am instead loading during the creation of the RentalAgreementService. I am reading from csv files, so that in theory if we wanted to, we could expand upon the given data provided we follow the file patterns.
2. In terms of I am counting rental days, I do not count the checkout date per the following statement for Charge Days - "Count of chargeable days, from day after checkout through and including due
date, excluding “no charge” days as specified by the tool type.". This means I am not counting the initial checkout date as part of the charging period. The due date is built off of the checkout date though.

In the JUnit Tests, the tests are named test[0-9] to match the given scenario from the document. I added 3 additional tests for argument verification.
