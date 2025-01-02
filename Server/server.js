const express = require('express');
const bcrypt = require('bcryptjs');
const bodyParser = require('body-parser');
const sql = require('mssql');
const jwt = require('jsonwebtoken');

const app = express();
app.use(bodyParser.json());
const saltRounds = 10;
const JWT_SECRET = 'your_jwt_secret_key';

const dbConfig = {
   user: 'SA', // Your SQL Server username
  password: 'C0mp2001!', // Your SQL Server password
  server: 'localhost', // Use 'localhost' if running on the same machine
  port: 1433, // Default SQL Server port
  database: 'HotelBooking', // Your database name
  options: {
    encrypt: true, // Use true for Azure SQL; set false for local Docker
    trustServerCertificate: true, // Use for self-signed certificates
  },
};

let pool;
sql.connect(dbConfig)
  .then((p) => {
    pool = p;
    console.log('Connected to Database');
  })
  .catch((err) => console.error('Database Connection Failed:', err));



  
app.post('/register', async (req, res) => {
    const { firstName, lastName, email, phoneNumber, password } = req.body;
  
    if (!firstName || !lastName || !email || !phoneNumber || !password) {
      return res.status(400).send('All fields are required');
    }
  
    try {
      // Hash the password
      const hashedPassword = await bcrypt.hash(password, saltRounds);
  
      // Insert user into database
      const result = await pool.request()
        .input('firstName', sql.NVarChar, firstName)
        .input('lastName', sql.NVarChar, lastName)
        .input('email', sql.NVarChar, email)
        .input('phoneNumber', sql.NVarChar, phoneNumber)
        .input('password', sql.NVarChar, hashedPassword)
        .query(`
          INSERT INTO dbo.CustomerTable (firstName, lastName, email, phoneNumber, password)
          VALUES (@firstName, @lastName, @email, @phoneNumber, @password);
        `);
  
      res.status(201).send('User registered successfully');
    } catch (err) {
      console.error('Error registering user:', err);
      res.status(500).send('Error registering user');
    }
  });
  
  app.post('/login', async (req, res) => {
    const { email, password } = req.body;
  
    if (!email || !password) {
      return res.status(400).send('Email and password are required');
    }
  
    try {
      // Get the user from the database
      const result = await pool.request()
        .input('email', sql.NVarChar, email)
        .query('SELECT * FROM dbo.CustomerTable WHERE email = @email');
  
      const user = result.recordset[0];
  
      if (!user) {
        return res.status(400).send('Invalid credentials');
      }
  
      // Compare the provided password with the hashed password in the database
      const isMatch = await bcrypt.compare(password, user.password);
      
      if (!isMatch) {
        return res.status(400).send('Invalid credentials');
      }
  
      // Generate JWT token
      const token = jwt.sign({ customerId: user.customerId }, JWT_SECRET, { expiresIn: '1h' });
  
      res.status(200).json({ token });
    } catch (err) {
      console.error('Error logging in user:', err);
      res.status(500).send('Error logging in user');
    }
  });
  
  app.get('/customer', async (req, res) => {
    const token = req.headers['authorization'];
  
    if (!token) {
      return res.status(401).send('Access denied, token required');
    }
  
    try {
      // Verify the token
      const decoded = jwt.verify(token, JWT_SECRET);
      
      const result = await pool.request()
        .input('customerId', sql.Int, decoded.customerId)
        .query('SELECT customerId, firstName, lastName, email, phoneNumber FROM dbo.CustomerTable WHERE customerId = @customerId');
      
      const user = result.recordset[0];
  
      if (!user) {
        return res.status(404).send('User not found');
      }
  
      res.status(200).json(user);
    } catch (err) {
      console.error('Error fetching customer data:', err);
      res.status(500).send('Error fetching customer data');
    }
  });





  app.get('/getBookingDetails', async (req, res) => {
    try {
      const result = await pool.request()
        .query('SELECT * FROM BookingDetailsView');
  
      res.status(200).json(result.recordset); // Send the booking details in the response
    } catch (err) {
      console.error('Error executing SELECT on BookingDetailsView:', err);
      res.status(500).send('Error fetching booking details');
    }
  });

  app.post('/insertBooking', async (req, res) => {
    const {
        customerId,
        roomId,
        dateBooking,
        bookedDate,
        amountDay,
        statusId,
    } = req.body;

    try {
        const result = await pool.request()
            .input('customerId', sql.Int, customerId)
            .input('roomId', sql.Int, roomId)
            .input('dateBooking', sql.Date, dateBooking)
            .input('bookedDate', sql.Date, bookedDate)
            .input('amountDay', sql.Int, amountDay)
            .input('statusId', sql.Int, statusId)
            .execute('InsertBooking');

        res.status(201).json({ newBookingId: result.recordset[0].NewBookingId });
    } catch (err) {
        console.error('Error executing InsertBooking procedure:', err);
        res.status(500).send('Error inserting booking');
    }
});

app.get('/getBookingDetails/:bookingId', async (req, res) => {
    const { bookingId } = req.params; // Extract bookingId from the URL
  
    try {
      const result = await pool.request()
        .input('bookingId', sql.Int, bookingId) // Pass bookingId as a parameter to SQL query
        .query('SELECT * FROM BookingDetailsView WHERE bookingId = @bookingId');
      
      // Check if data is found
      if (result.recordset.length === 0) {
        return res.status(404).send('Booking not found');
      }
      
      res.status(200).json(result.recordset[0]); // Return the specific booking details
    } catch (err) {
      console.error('Error executing SELECT on BookingDetailsView:', err);
      res.status(500).send('Error fetching booking details');
    }
  });
  

app.put('/updateBooking/:bookingId', async (req, res) => {
    const {
        roomId,
        bookedDate,
        amountDay,
        statusId,
    } = req.body;
    
    const { bookingId } = req.params; // Get bookingId from URL

    try {
        const result = await pool.request()
            .input('bookingId', sql.Int, bookingId) // bookingId from URL
            .input('roomId', sql.Int, roomId)
            .input('bookedDate', sql.Date, bookedDate)
            .input('amountDay', sql.Int, amountDay)
            .input('statusId', sql.Int, statusId)
            .execute('UpdateBooking');

        res.status(200).json({ updatedBookingId: result.recordset[0].UpdatedBookingId });
    } catch (err) {
        console.error('Error executing UpdateBooking procedure:', err);
        res.status(500).send('Error updating booking');
    }
});

  app.delete('/deleteBooking/:id', async (req, res) => {
    const { id } = req.params;
  
    try {
      const result = await pool.request()
        .input('bookingId', sql.Int, id)
        .execute('DeleteBooking');
  
      const rowsDeleted = result.recordset[0]?.RowsDeleted || 0;
      res.status(200).json({ rowsDeleted });
    } catch (err) {
      console.error('Error executing DeleteBooking procedure:', err);
      res.status(500).send('Error deleting booking');
    }
  });



const PORT = 3000;
app.listen(PORT, () => {
  console.log(`Server running at http://localhost:${PORT}`);
});
