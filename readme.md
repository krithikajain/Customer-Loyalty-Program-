## Customer Loyalty Program

### Overview

LoyaltyFirst is an end-to-end Android application that integrates with a server-side database to manage customer loyalty programs. The project implements a 3-tier architecture comprising the client (Android app), server-side servlets, and database layers. It enables users to log in, view transactions, redeem rewards, and manage family points effectively.

## Features
- **User Authentication**
  - Login with username and password.
  - Verifies credentials via a server-side database.
- **Dashboard**
  - View user profile, reward points, and access app features.
- **Transactions**
  - Display a list of transactions with detailed information.
- **Rewards Redemption**
  - Browse available prizes and view the points required for redemption.
- **Family Point Management**
  - Add points from transactions to a family account based on predefined percentages.
- **Server-Side Integration**
  - Communicates with servlets and a database to fetch and update data dynamically.
## Database & Server-Side Details

### 1. Database Integration
- **Backend Database** stores:
  - User credentials for authentication.
  - Transaction details.
  - Prize information.
  - Family points and percentages.
- **JDBC Connection** connects the servlets to the database for CRUD operations.

### 2. Java Servlets
The project uses servlets to handle server-side logic. Key servlets include:
- **Login Servlet**:  
  Validates user credentials by checking the database. Returns:
  - `Yes:<customer_id>` for valid credentials.
  - `No` for invalid credentials.
- **Transaction Servlet**:  
  Fetches a list of transactions for a given customer ID.
- **Redemption Details Servlet**:  
  Retrieves details about prizes available for redemption.
- **Family Points Servlet**:  
  Handles logic to calculate and update family points based on transactions.

### 3. Server-Side Pages
- `Transactions.jsp`: Retrieves transaction details.
- `RedemptionDetails.jsp`: Fetches prize information.
- `SupportFamilyIncrease.jsp`: Manages family point updates.

---

## Screens & Functionality

### 1. Login Screen (`MainActivity`)
- Users log in with their credentials.
- Server validates the credentials against the database.

### 2. User Dashboard (`MainActivity2`)
- Displays user profile info (name, reward points).
- Offers navigation to transactions, rewards, and family management.

### 3. Transactions Screen (`MainActivity3`)
- Lists all user transactions.
- Formats and displays details like ID, description, amount, and date.

### 4. Transaction Details (`MainActivity4`)
- Allows the user to select a transaction.
- Displays detailed info like total points, date, and product details.

### 5. Rewards Redemption (`MainActivity5`)
- Lists prizes for redemption.
- Displays prize details, including description, points needed, and location.

### 6. Family Point Management (`MainActivity6`)
- Allows users to allocate points from transactions to a family account.
- Calculates points based on family percentage and updates the database.
