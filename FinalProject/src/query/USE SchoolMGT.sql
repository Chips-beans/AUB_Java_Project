USE SchoolMGT
GO

CREATE PROCEDURE Authentication
    @username Varchar(50),
    @password VARCHAR(255),
    @role VARCHAR(20),
    @IsValid INT OUTPUT
AS
BEGIN
    SET @IsValid = 0;
    SET NOCOUNT ON;
    IF EXISTS(
        SELECT *
        FROM Users u  
        WHERE Username = @username
        AND Password_Hash = @password
        AND Role = @role
    )
    BEGIN 
        SET @IsValid = 1;
        RETURN @IsValid
    END
END;
GO

