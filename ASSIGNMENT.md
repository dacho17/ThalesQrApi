# Fullstack Engineer - Backend Focused Assessment 

The goal of this test is to assess your backend engineering skills. 
Please use Java or kotlin to build the backend. Feel free to use any third party libraries.

## Requirements

You are to develop a backend API service that allows the upload of static QR codes files for importing certain data into the main database. To do this, the backend must be able to receive the QR code files, process it and insert the data into a database of your choice. The backend should also be able to allow for searching and deletion of the uploaded data

You are free to create QR codes of the following types for this exercise. The complexity of the qr code does not add any bonus point.
- Text
- URL
- vCard

You should also implement an authentication mechanism of your choice for the API usage along with an RBAC implementation to ensure the following
- only authenticated users can upload the data
- search functionality is available for everyone regardless of authentication
- only admin users can delete the uploaded data. 

You don't have to implement any frontend. A swagger or any other tool to list the requests will be good as soon as you have documented it.

# Deliverables
- A working set of code along with any initialisation and seeding instructions for the database
- Sample QR codes of different types to validate the exercise
- An API documentation on how to use the API functionalities, including examples (curl, python, swagger, etc) on how to trigger the APIs and to get authentication

## Evaluation criteria
- Clean code and clean architecture
- Tests
- Meets the requirements
- Explain the assumptions and choices made in the System design

The following earn you bonus points:
- Clear commits
- User friendly README
- Clear logic separation between API and Database
- Advance architecture paterns like SOLID, layer architecture ...

# Submission Guidelines
- Please include a Readme.md file with instructions to run
- Submit as a zip/tarball