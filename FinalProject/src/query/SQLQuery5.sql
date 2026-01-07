use school
go 
CREATE TABLE Users (
    User_ID INT PRIMARY KEY identity(1,1),
    Username VARCHAR(50) UNIQUE NOT NULL,
    Password_Hash VARCHAR(255) NOT NULL, -- Store hashed passwords only
    Role VARCHAR(20)
);

