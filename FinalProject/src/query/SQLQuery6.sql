use school
go 



CREATE PROCEDURE [dbo].[Authentication](
    @username Varchar(50),
    @password VARCHAR(255),
    @role VARCHAR(20)
)
AS
BEGIN
    IF EXISTS(
        SELECT *
        FROM Users u
        WHERE Username = @username
        AND Password_Hash = @password
        AND Role = @role
    )
    BEGIN 
        select 1 ok;
    END
    else
    begin
        select 0 ok;
    end
END;
GO



