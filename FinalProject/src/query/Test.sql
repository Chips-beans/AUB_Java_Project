USE SchoolMGT
GO

DECLARE @return_value int;
EXEC dbo.Authentication @username = 'Admin', @password = 'dgplq456', @role = 'Admin', @IsValid = @return_value OUTPUT;
SELECT 'Return Value' = @return_value;