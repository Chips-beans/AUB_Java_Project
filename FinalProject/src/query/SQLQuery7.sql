use SchoolMGT
go 

Create Procedure [dbo].[ChangePassword] 
(@UserName varchar(50),
 @OldPassword varchar(255),
 @NewPassword varchar(20)) 
AS
Begin
  If Exists(Select USER_ID From Users Where Username = @UserName and Password_Hash = @OldPassword) 
  Begin
    Update Users set Password_Hash = @NewPassword
    Where Username = @UserName;
    select 1 ok;
  End;
  Else
    select 0 ok;
End;
