No main method exists for this project. Code is tested via Junit in Intellij.

Design notes - Normally I would load the tools and tools types into a database table and then maintain them like that, however for the sake of ease for this project, I am instead loading during the creation of the RentalAgreementService. I am reading from csv files, so that in theory if we wanted to, we could expand upon the given data provided we follow the file patterns.

In the JUnit Tests, the tests are named test[0-9] to match the given scenario from the document. I added 3 additional tests for argument verification.
