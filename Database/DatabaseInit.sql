USE master
GO

IF NOT EXISTS (
    SELECT [name]
    FROM sys.databases
    WHERE [name] = N'HotelBookingSystem'
)
CREATE DATABASE HotelBookingSystem
GO

USE HotelBookingSystem
GO

IF OBJECT_ID('[dbo].[BookingTable]', 'U') IS NOT NULL
    DROP TABLE [dbo].[BookingTable];

IF OBJECT_ID('[dbo].[RoomTable]', 'U') IS NOT NULL
    DROP TABLE [dbo].[RoomTable];

IF OBJECT_ID('[dbo].[RoomTypeTable]', 'U') IS NOT NULL
    DROP TABLE [dbo].[RoomTypeTable'];

IF OBJECT_ID('[dbo].[StatusTable]', 'U') IS NOT NULL
    DROP TABLE [dbo].[StatusTable];

IF OBJECT_ID('[dbo].[CustomerTable]', 'U') IS NOT NULL
    DROP TABLE [dbo].[CustomerTable'];

IF OBJECT_ID('[dbo].[HotelTable]', 'U') IS NOT NULL
    DROP TABLE [dbo].[HotelTable'];

GO
CREATE TABLE [dbo].[CustomerTable]
(
    [customerId] INT NOT NULL PRIMARY KEY IDENTITY(1,1),
    [firstName] NVARCHAR(50) NOT NULL,
    [lastNAme] NVARCHAR(50) NOT NULL,
    [email] NVARCHAR(50) NOT NULL,
    [phoneNumber] NVARCHAR(50) NOT NULL,
    [password] NVARCHAR(128) NOT NULL
);

CREATE TABLE [dbo].[HotelTable]
(
    [hotelId] INT NOT NULL PRIMARY KEY IDENTITY(1,1),
    [hotelName] NVARCHAR(50) NOT NULL
);

CREATE TABLE [dbo].[StatusTable]
(
    [statusId] INT NOT NULL PRIMARY KEY IDENTITY(1,1),
    [statusType] NVARCHAR(50) NOT NULL
);

CREATE TABLE [dbo].[RoomTypeTable]
(
    [roomTypeId] INT NOT NULL PRIMARY KEY IDENTITY(1,1), 
    [roomName] NVARCHAR(50) NOT NULL,
    [roomPrice] FLOAT NOT NULL,
    [hotelId] INT NOT NULL,
    [totalRoom] INT NOT NULL,
    CONSTRAINT fkHotelId FOREIGN KEY (hotelId) REFERENCES HotelTable (hotelId)
);

CREATE TABLE [dbo].[RoomTable]
(
    [roomId] INT NOT NULL PRIMARY KEY IDENTITY(1,1),
    [roomTypeId] INT NOT NULL,
    CONSTRAINT fkRoomTypeId FOREIGN KEY (roomTypeId) REFERENCES RoomTypeTable (roomTypeId)
);

CREATE TABLE [dbo].[BookingTable]
(
    [bookingId] INT NOT NULL PRIMARY KEY IDENTITY(1,1), 
    [customerId] INT NOT NULL,
    [roomId] INT NOT NULL,
    [createdAtDate] DATE NOT NULL,
    [createdAtTime] TIME NOT NULL,
    [bookedDate] DATE NOT NULL,
    [amountDay] INT NOT NULL,
    [statusId] INT NOT NULL,
    CONSTRAINT fkCustomerId FOREIGN KEY (customerId) REFERENCES CustomerTable (customerId),
    CONSTRAINT fkRoomId FOREIGN KEY (roomId) REFERENCES RoomTable (roomId),
    CONSTRAINT fkStatusId FOREIGN KEY (statusId) REFERENCES StatusTable (statusId)
);

GO

CREATE VIEW BookingDetailsView AS
SELECT 
    b.bookingId,
    c.firstName + ' ' + c.lastName AS customerName,
    c.email AS customerEmail,
    r.roomId,
    rt.roomName,
    rt.roomPrice,
    h.hotelName,
    b.bookedDate,
    b.amountDay,
    s.statusType AS bookingStatus
FROM 
    BookingTable b
INNER JOIN CustomerTable c ON b.customerId = c.customerId
INNER JOIN RoomTable r ON b.roomId = r.roomId
INNER JOIN RoomTypeTable rt ON r.roomTypeId = rt.roomTypeId
INNER JOIN HotelTable h ON rt.hotelId = h.hotelId
INNER JOIN StatusTable s ON b.statusId = s.statusId;

GO

INSERT INTO [dbo].[CustomerTable] ([firstName], [lastName], [email], [phoneNumber], [password])
VALUES
('John', 'Doe', 'johndoe1@gmail.com', '123456789', 'Password123'),
('Jim', 'Heart', 'heart.j134@gmail.com', '198765432', 'Password123'),
('Jack', 'Sparrow', 'captainblackpearl@gmail.com', '1123456789', 'Password123');

INSERT INTO [dbo].[HotelTable] ([hotelName])
VALUES
('Blue Ocean Hotel');

INSERT INTO [dbo].[StatusTable] ([statusType])
VALUES
('Pending'),
('Confirmed'),
('AwaitingPayment'),
('Cancelled'),
('Completed'),
('On Hold'),
('Checked In'),
('Checked Out'),
('Rescheduled'),
('No Show');

INSERT INTO [dbo].[RoomTypeTable] ([roomName], [roomPrice], [hotelId], [totalRoom])
VALUES
('Family Room', 250, 1, 3),
('Suites', 350, 1, 3),
('Standard Deluxe', 350, 1, 3);

INSERT INTO [dbo].[RoomTable] ([roomTypeId])
VALUES
(1),
(1),
(1),
(2),
(3),
(3),
(3);

INSERT INTO [dbo].[BookingTable] ([customerId], [roomId], [createdAtDate], [createdAtTime], [bookedDate], [amountDay], [statusId])
VALUES
(1, 1, '2024-11-10', '12:12:00', '2024-11-12', 2, 2),
(2, 1, '2024-11-12', '14:46:00', '2024-11-13', 2, 2),
(3, 6, '2024-11-12', '20:09:00', '2024-11-24', 4, 2),
(1, 1, '2024-11-14', '08:36:00', '2024-11-20', 2, 1);

GO









-- Stored Procedure: Update Booking
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[UpdateBooking]
    @BookingId INT,
    @RoomId INT,
    @BookedDate DATE,
    @AmountDay INT,
    @StatusId INT
AS
BEGIN
    BEGIN TRY
        UPDATE dbo.BookingTable
        SET
            RoomId = @RoomId,
            BookedDate = @BookedDate,
            AmountDay = @AmountDay,
            StatusId = @StatusId
        WHERE BookingId = @BookingId;

        SELECT @BookingId AS UpdatedBookingId;
    END TRY
    BEGIN CATCH
        -- Handle errors
        SELECT ERROR_MESSAGE() AS ErrorMessage;
    END CATCH
END;
GO

-- Stored Procedure: Insert Booking
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[InsertBooking]
    @CustomerId INT,
    @RoomId INT,
    @createdAtDate DATE,
    @BookedDate DATE,
    @AmountDay INT,
    @StatusId INT
AS
BEGIN
    BEGIN TRY
        INSERT INTO dbo.BookingTable (CustomerId, RoomId, createdAtDate, createdAtTime, BookedDate, AmountDay, StatusId)
        VALUES (@CustomerId, @RoomId, @createdAtDate, GETDATE(), @BookedDate, @AmountDay, @StatusId);

        SELECT SCOPE_IDENTITY() AS NewBookingId;
    END TRY
    BEGIN CATCH
        -- Handle errors
        SELECT ERROR_MESSAGE() AS ErrorMessage;
    END CATCH
END;
GO

-- Stored Procedure: Delete Booking
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[DeleteBooking]
    @BookingId INT
AS
BEGIN
    BEGIN TRY
        DELETE FROM dbo.BookingTable
        WHERE BookingId = @BookingId;

        SELECT @@ROWCOUNT AS RowsDeleted;
    END TRY
    BEGIN CATCH
        -- Handle errors
        SELECT ERROR_MESSAGE() AS ErrorMessage;
    END CATCH
END;
GO

-- Trigger: Update Room Availability on Insert
CREATE TRIGGER trg_UpdateRoomAvailabilityOnInsert
ON dbo.BookingTable
AFTER INSERT
AS
BEGIN
    UPDATE dbo.RoomTypeTable
    SET TotalRoom = TotalRoom - 1
    FROM dbo.RoomTypeTable RT
    INNER JOIN dbo.RoomTable R ON RT.RoomTypeId = R.RoomTypeId
    INNER JOIN Inserted I ON R.RoomId = I.RoomId;
END;
GO

-- Trigger: Update Room Availability on Delete
CREATE TRIGGER trg_UpdateRoomAvailabilityOnDelete
ON dbo.BookingTable
AFTER DELETE
AS
BEGIN
    UPDATE dbo.RoomTypeTable
    SET TotalRoom = TotalRoom + 1
    FROM dbo.RoomTypeTable RT
    INNER JOIN dbo.RoomTable R ON RT.RoomTypeId = R.RoomTypeId
    INNER JOIN Deleted D ON R.RoomId = D.RoomId;
END;
GO


-- Trigger: Auto-Complete Booking
CREATE TRIGGER trg_AutoCompleteBooking
ON dbo.BookingTable
AFTER UPDATE
AS
BEGIN
    DECLARE @CompletedStatusId INT;

    SELECT @CompletedStatusId = StatusId
    FROM dbo.StatusTable
    WHERE StatusType = 'Completed';

    UPDATE dbo.BookingTable
    SET StatusId = @CompletedStatusId
    WHERE GETDATE() > DATEADD(DAY, AmountDay, DateBooked)
      AND StatusId <> @CompletedStatusId;
END;
GO
