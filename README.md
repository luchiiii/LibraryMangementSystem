# Library Management System

## Overview
This is a **Library Management System** built in **Java** using **PostgreSQL** as the database. The system follows the **DAO (Data Access Object) pattern** and provides a **console-based interface** for both **librarians** and **members** to manage books, members, and borrowing records.

The project is designed to be **simple, efficient, and easy to use**, with features like:
- Adding, deleting, and viewing books 📚
- Managing library members 👥
- Borrowing and returning books
- Keeping track of borrowed books
- A loyalty program for members
- Logging all activities for better tracking
- Exporting data to CSV files

---

## Setting Up the Project

### Requirements
Before running the project, make sure you have the following installed:
✅ **Java 17 or later**  
✅ **PostgreSQL (or Docker for easy setup)**  
✅ **Maven for dependency management**  
✅ **IntelliJ IDEA (or any Java IDE)**

### Cloning the Project
If you’re using **Git**, you can clone the repository like this:
```sh
git clone https://github.com/your-repo/library-management-system.git
cd library-management-system
```
If you don’t use Git, simply **download the project folder** and extract it.

### Setting Up the Database
If you're using **Docker**, you can start PostgreSQL with:
```sh
docker-compose up -d
```
If you want to set up the database manually, open **PostgreSQL** and create a new database:
```sql
CREATE DATABASE library_management;
```
Make sure to update the database credentials in the `DatabaseConnection.java` file:
```java
private static final String URL = "jdbc:postgresql://localhost:5432/library_management";
private static final String USER = "your_db_username";
private static final String PASSWORD = "your_db_password";
```

---

## Running the Project

### Using IntelliJ IDEA
1. Open **IntelliJ IDEA** and load the project.
2. Navigate to the **Main.java** file in the `libraryManagement.ui` package.
3. Click **Run** ▶ to start the program.
4. Follow the prompts to interact with the system.

### Using Command Line
If you prefer to run the project manually:
```sh
mvn clean install
java -jar target/library-management.jar
```
This will compile and run the application.

---

## How to Use the Library System

### Choosing a Role
When you start the system, you will be asked whether you are a **Librarian** or a **Library Member**. Based on your selection, you’ll get different options.

### Librarian Options
Librarians can:
- **Manage Books** (Add, View, Delete)
- **Manage Members** (Register new members, View members)

### Member Options
Library members can:
- **Borrow a book** (Search by title and borrow if available)
- **Return a book** (Mark a borrowed book as returned)
- **View borrowed books** (Check their current loans)

To borrow or return a book, members need to provide their **Member ID** and the **Book Title**.

---

## Troubleshooting

**Problem:** Cannot connect to PostgreSQL.  
➡ **Solution:** Make sure PostgreSQL is running (`docker ps` or `pgAdmin`).

**Problem:** Foreign key constraint errors when borrowing books.  
➡ **Solution:** Ensure that the **member exists** before borrowing a book.

**Problem:** "Method not found" errors.  
➡ **Solution:** Try rebuilding the project using `mvn clean install`.

---

## Notes
This project was built with a **focus on simplicity and usability**. If needed, you can modify the code to add new features like **GUI support** or **advanced reports**.

If you face any issues, feel free to ask! 😊

