SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE TRIGGER [dbo].[trg_AutoCompleteBooking]
ON [dbo].[BookingTable]
AFTER UPDATE
AS
BEGIN
    UPDATE dbo.BookingTable
    SET statusId = (SELECT statusId FROM dbo.StatusTable WHERE statusType = 'Completed')
    WHERE GETDATE() > DATEADD(DAY, amountDay, dateBooking)
      AND statusId <> (SELECT statusId FROM dbo.StatusTable WHERE statusType = 'Completed');
END;
GO
ALTER TABLE [dbo].[BookingTable] ENABLE TRIGGER [trg_AutoCompleteBooking]
GO





SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE TRIGGER [dbo].[trg_UpdateRoomAvailabilityOnDelete]
ON [dbo].[BookingTable]
AFTER DELETE
AS
BEGIN
    UPDATE dbo.RoomTypeTable
    SET totalRoom = totalRoom + 1
    FROM dbo.RoomTypeTable RT
    INNER JOIN dbo.RoomTable R ON RT.roomTypeId = R.roomTypeId
    INNER JOIN Deleted D ON R.roomId = D.roomId;
END;
GO
ALTER TABLE [dbo].[BookingTable] ENABLE TRIGGER [trg_UpdateRoomAvailabilityOnDelete]
GO





SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE TRIGGER [dbo].[trg_UpdateRoomAvailabilityOnInsert]
ON [dbo].[BookingTable]
AFTER INSERT
AS
BEGIN
    UPDATE dbo.RoomTypeTable
    SET totalRoom = totalRoom - 1
    FROM dbo.RoomTypeTable RT
    INNER JOIN dbo.RoomTable R ON RT.roomTypeId = R.roomTypeId
    INNER JOIN Inserted I ON R.roomId = I.roomId;
END;
GO
ALTER TABLE [dbo].[BookingTable] ENABLE TRIGGER [trg_UpdateRoomAvailabilityOnInsert]
GO
