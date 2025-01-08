SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE VIEW [dbo].[BookingDetailsView] AS
SELECT 
    b.bookingId,
    b.customerId,
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
