USE master
GO

IF NOT EXISTS (
    SELECT [name]
        FROM sys.databases
        WHERE [name] = N'HotelBooking'
)
CREATE DATABASE HotelBooking
GO