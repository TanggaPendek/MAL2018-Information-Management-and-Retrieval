# MAL2018-Information-Management-and-Retrieval


**Project Overview**

This project provides the robust backend infrastructure for the TSC Hotel and Tourism mobile application (Android). It focuses on secure data storage, efficient processing, and seamless API interaction for user authentication, booking management, and other functionalities.


---


**Setup Flow**

1.  **Docker Environment:**

    *   **Command:**

        ```bash
        docker run -e 'ACCEPT_EULA=1' -e 'MSSQL_SA_PASSWORD=C0mp2001!' -p 1433:1433 --name COMP2001sqlserv -d mcr.microsoft.com/azure-sql-edge
        ```


2.  **Database:**

    *   **Azure Data Studio:** Download and install Azure Data Studio ([https://learn.microsoft.com/en-us/azure-data-studio/download-azure-data-studio](https://learn.microsoft.com/en-us/azure-data-studio/download-azure-data-studio)).
    *   **SQL Script Execution (Order-Specific):** The project includes SQL scripts for database schema creation and initialization. Execute them in Azure Data Studio in the **exact order** specified by their filenames to ensure proper database structure and data setup.

3.  **Server Setup:**

    *   **Command:**

        ```bash
        node server.js
        ```

    *   **Explanation:**

        Run this command in the terminal within the project server directory. It starts the Express.js server, which acts as the core of your backend application, handling API requests, processing data, and interacting with the database.

4.  **Mobile Development:**

    *   **Android Studio:** This project primarily focuses on the backend, but use Android Studio ([https://developer.android.com/](https://developer.android.com/)) to build the connected mobile application (not included in this repository).

**Authentication and Booking Management**

The mobile app requires users to register and log in before performing Create, Read, Update, and Delete (CRUD) operations on booking data. This ensures data security and controlled access to booking functionalities. The backend implements secure authentication mechanisms to validate user credentials and manage access permissions.


---


**Technologies and Libraries:**

*   **Backend:**
    *   Node.js ([https://nodejs.org/en](https://nodejs.org/en)): JavaScript runtime environment for server-side development.
    *   Express.js ([https://expressjs.com/](https://expressjs.com/)): Lightweight web framework for building APIs and web applications.
    *   mssql ([https://www.npmjs.com/package/mssql](https://www.npmjs.com/package/mssql)): Node.js driver for interacting with Microsoft SQL Server databases.
*   **Development Tools:***    
    *   Docker ([https://www.docker.com/](https://www.docker.com/)): Containerization platform for creating and deploying applications in a standardized environment.
    *   Azure Data Studio: Graphical interface for managing and interacting with Microsoft SQL databases.
    *   Android Studio ([https://developer.android.com/studio/](https://developer.android.com/studio/)): Integrated Development Environment (IDE) for Android app development.
    *   Retrofit2 ([https://square.github.io/retrofit/](https://square.github.io/retrofit/)): Type-safe HTTP client for Android and Java.


---


**Credits**

*   Android Studio
*   Retrofit2
*   Docker
*   Azure Data Studio
*   Node.js
*   Express.js
*   mssql

**Assets**

*   [Image Asset](https://img.freepik.com/free-photo/close-up-white-marble-texture-background_53876-63512.jpg?t=st=1736217481~exp=1736221081~hmac=aedb0dac7)


---
**Additional Notes**

*  Ensure Docker is running before starting the SQL server container.
*  Run the necessary SQL scripts in Azure Data Studio before starting the app and server.
*  For detailed guides on setting up Android Studio, refer to the official documentation [here](https://developer.android.com/studio).
