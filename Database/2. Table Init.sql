
IF OBJECT_ID('[dbo].[BookingTable]', 'U') IS NOT NULL
    DROP TABLE [dbo].[BookingTable];

IF OBJECT_ID('[dbo].[RoomTable]', 'U') IS NOT NULL
    DROP TABLE [dbo].[RoomTable];

IF OBJECT_ID('[dbo].[RoomTypeTable]', 'U') IS NOT NULL
    DROP TABLE [dbo].[RoomTypeTable];

IF OBJECT_ID('[dbo].[StatusTable]', 'U') IS NOT NULL
    DROP TABLE [dbo].[StatusTable];

IF OBJECT_ID('[dbo].[CustomerTable]', 'U') IS NOT NULL
    DROP TABLE [dbo].[CustomerTable];

IF OBJECT_ID('[dbo].[HotelTable]', 'U') IS NOT NULL
    DROP TABLE [dbo].[HotelTable];


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
    [dateBooking] DATE NOT NULL,
    [timeBooking] TIME NOT NULL,
    [bookedDate] DATE NOT NULL,
    [amountDay] INT NOT NULL,
    [statusId] INT NOT NULL,
    CONSTRAINT fkCustomerId FOREIGN KEY (customerId) REFERENCES CustomerTable (customerId),
    CONSTRAINT fkRoomId FOREIGN KEY (roomId) REFERENCES RoomTable (roomId),
    CONSTRAINT fkStatusId FOREIGN KEY (statusId) REFERENCES StatusTable (statusId),
);
GO