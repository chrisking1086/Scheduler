# Scheduler

## Application overview
The scheduler program uses a Model View Controller (MVC) architecture which consists of object Models for each object type:

- Appointments
- Tasks
- Employees
- Customers
  
Each of these objects has a service class that handles interaction with the database. They are responsible for the Create, Read, Update and Delete (CRUD) functionality:

- Appointment Service
- Task Service
- Employee Service
- Customer Service
- DatabaseHelper - For creating the tables upon initial use.
- Login Service - For authentication

The View(GUI) for the application was built using Java Swing and consists of:

Main Screen - Consists of a list of current appointments and the main menu items for navigation.
Management Dialogs - Each object type has a corresponding Management Dialog that allows the user to view the records in the database, choose one and open the edit screen for that object.
Add Item Dialogs - The different item types (Appointment, Customer, Employee and Tasks) have add screens unique to each type.
Edit Item Dialogs - Along with the add item dialogs, each item type has an edit dialog.
