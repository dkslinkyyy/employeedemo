CREATE TABLE IF NOT EXISTS Occupation (
                            roleID INT AUTO_INCREMENT PRIMARY KEY,
                            title VARCHAR(255) NOT NULL,
                            description TEXT,
                            salary INT
);

CREATE TABLE IF NOT EXISTS Employee (
                          employeeID INT AUTO_INCREMENT PRIMARY KEY,
                          firstName VARCHAR(255) NOT NULL,
                          lastName VARCHAR(255) NOT NULL,
                          roleID INT,
                          FOREIGN KEY (roleID) REFERENCES Occupation(roleID) ON DELETE SET NULL ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS Account (
                         accountID INT AUTO_INCREMENT PRIMARY KEY,
                         username VARCHAR(255) UNIQUE NOT NULL,
                         password VARCHAR(255) NOT NULL,
                         adminPrivilege BOOLEAN NOT NULL,
                         employeeID INT,
                         FOREIGN KEY (employeeID) REFERENCES Employee(employeeID) ON DELETE CASCADE ON UPDATE CASCADE
);