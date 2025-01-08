# MAL2018-Information-Management-and-Retrieval


**Project Overview**

This project provides the robust backend infrastructure for the TSC Hotel and Tourism mobile application (Android). It focuses on secure data storage, efficient processing, and seamless API interaction for user authentication, booking management, and other functionalities.

**Setup Flow**

1.  **Docker Environment:**

    *   **Command:**

        ```bash
        docker run -e 'ACCEPT_EULA=1' -e 'MSSQL_SA_PASSWORD=C0mp2001!' -p 1433:1433 --name COMP2001sqlserv -d mcr.microsoft.com/azure-sql-edge [invalid URL removed]
        ```

    *   **Explanation:**

        This command launches a Docker container pre-configured with Microsoft Azure SQL Edge. The environment variables `ACCEPT_EULA=1` and `MSSQL_SA_PASSWORD=C0mp2001!` (replace with your desired password) ensure acceptance of the license agreement and provide a secure password for the SQL Server Administrator account. The port mapping `1433:1433` exposes the database service on port 1433 for accessibility.

2.  **Database:**

    *   **Azure Data Studio:** Download and install Azure Data Studio ([https://learn.microsoft.com/en-us/azure-data-studio/download-azure-data-studio](https://learn.microsoft.com/en-us/azure-data-studio/download-azure-data-studio)).
    *   **SQL Script Execution (Order-Specific):** The project includes SQL scripts for database schema creation and initialization. Execute them in Azure Data Studio in the **exact order** specified by their filenames (e.g., `01_create_tables.sql` before `02_insert_initial_data.sql`) to ensure proper database structure and data setup.

3.  **Server Setup:**

    *   **Command:**

        ```bash
        node server.js
        ```

    *   **Explanation:**

        Run this command in the terminal within the project directory. It starts the Express.js server, which acts as the core of your backend application, handling API requests, processing data, and interacting with the database.

4.  **Mobile Development:**

    *   **Android Studio:** This project primarily focuses on the backend, but use Android Studio ([https://developer.android.com/](https://developer.android.com/)) to build the connected mobile application (not included in this repository).

**Authentication and Booking Management**

The mobile app requires users to register and log in before performing Create, Read, Update, and Delete (CRUD) operations on booking data. This ensures data security and controlled access to booking functionalities. The backend implements secure authentication mechanisms to validate user credentials and manage access permissions.

**Technologies and Libraries:**

*   **Backend:**
    *   Node.js ([https://nodejs.org/en](https://nodejs.org/en)): JavaScript runtime environment for server-side development.
    *   Express.js ([https://expressjs.com/](https://expressjs.com/)): Lightweight web framework for building APIs and web applications.
    *   mssql ([https://www.npmjs.com/package/mssql](https://www.npmjs.com/package/mssql)): Node.js driver for interacting with Microsoft SQL Server databases.
*   **Development Tools:**
    *   Docker ([https://www.docker.com/](https://www.docker.com/)): Containerization platform for creating and deploying applications in a standardized environment.
    *   Azure Data Studio: Graphical interface for managing and interacting with Microsoft SQL databases.

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

**Additional Notes**

*   Replace `C0mp2001!` with a strong password for the SQL Server Administrator account.
*   Consider implementing environment variables for sensitive information like database credentials to enhance security.
*   Consult the documentation for each technology used for further details and best practices.
*   This README provides a high-level overview. Further documentation within the codebase might be present to guide specific functionalities and implementation details.