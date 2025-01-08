SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[InsertBooking]
  @customerId INT,
    @roomId INT,
    @dateBooking DATE,
    @bookedDate DATE,
    @amountDay INT,
    @statusId INT
AS
BEGIN
INSERT INTO dbo.BookingTable (customerId, roomId, dateBooking, timeBooking, bookedDate, amountDay, statusId)
    VALUES (@customerId, @roomId, @dateBooking, GETDATE(), @bookedDate, @amountDay, @statusId);

    SELECT SCOPE_IDENTITY() AS NewBookingId;
END
GO


SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[UpdateBooking]
     @bookingId INT,
    @roomId INT,
    @bookedDate DATE,
    @amountDay INT,
    @statusId INT
AS
BEGIN
    UPDATE dbo.BookingTable
    SET
        roomId = @roomId,
        bookedDate = @bookedDate,
        amountDay = @amountDay,
        statusId = @statusId
    WHERE bookingId = @bookingId;

    SELECT @bookingId AS UpdatedBookingId;
END;
GO


SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[DeleteBooking]
    @bookingId INT
AS
BEGIN
    DELETE FROM dbo.BookingTable
    WHERE bookingId = @bookingId;

    SELECT @@ROWCOUNT AS RowsDeleted;
END;
GO
