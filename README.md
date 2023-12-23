# Implementation Notes for the Debt Collection System Integration

## Overview
This document provides implementation details for the Intrum Debt Collection System Integration project. The project's goal is to automate the processing of government-published CSV files for pandemic relief payouts and integrate this data into Intrum's debt collection system.

## Architecture
- **Language and Frameworks:** The project is implemented in Java 17, Spring Boot, OpenCSV, Maven, JUnit, Mockito, RESTful services.
- **Data Processing:** A scheduled task picks up CSV files from a designated folder (simulating an FTP server) and processes them.
- **Integration Points:** The system integrates with the debt collection system using a REST API.

## File Processing
- **Directory Structure:** The CSV files are stored in `DIRECTORY_PATH`, which is configurable through `application.properties`.
- **File Naming Convention:** Files follow the `WK_payouts_yyyymmdd_hhmmss.csv` format.
- **File Format:** CSV files use ';' as a field delimiter and '"' as a text delimiter, with the character set ISO-8859-1.

## CSV File Parsing
- **Data Extraction:** The `readAndParseCSVFile` method is responsible for parsing the CSV files and extracting relevant data.
- **Data Model:** Extracted data is mapped to the `WakandaPayoutData` class.

## Data Submission
- **API Integration:** Parsed data is submitted to the Intrum debt collection system via the `/payout` API endpoint.
- **RestTemplate Usage:** The `submitPayoutData` method uses Spring's `RestTemplate` for API communication.

## Testing
- **Unit Tests:** 
- One-to-One Class Mapping: For each main class, there is a corresponding test class in the test suite.
- Method-Level Testing: Each public method in a class is tested by one or more test methods.
- Independence: Each test is independent and can run standalone.
- **Frameworks and Libraries:** JUnit, Mockito, PowerMock.

## Future Extensibility
- The application is designed to easily accommodate different file formats and data submission mechanisms for other countries like Gondor.

## Build and Deployment
- To build and deploy the application, follow these steps:

1. Clone the project from the Git repository.
2. Open the project in your preferred IDE.
3. Build the project using Maven.
4. Deploy the application to a server.

## Contact Information
- Maintainer: Krutika Parate
- Email: krutikaparate24@gmail.com