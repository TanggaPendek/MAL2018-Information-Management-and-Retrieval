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


INSERT INTO [dbo].[BookingTable] ([customerId], [roomId], [dateBooking], [timeBooking], [bookedDate], [amountDay], [statusId])
VALUES
(1, 1, '2024-11-10', '12:12:00', '2024-11-12', 2, 2),
(2, 1, '2024-11-12', '14:46:00', '2024-11-13', 2, 2),
(3, 6, '2024-11-12', '20:09:00', '2024-11-24', 4, 2),
(1, 1, '2024-11-14', '08:36:00', '2024-11-20', 2, 1);
