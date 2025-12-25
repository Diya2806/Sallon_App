const createError = require('http-errors');
const express = require('express');
const path = require('path');
const cookieParser = require('cookie-parser');
const logger = require('morgan');
const cors = require('cors');
const mongoose = require('mongoose');

const indexRouter = require('./routes/index');
const usersRouter = require('./routes/users');
const AdminRouter = require('./routes/Admin');
const CustomerRouter = require('./routes/Customer');
const ShopOwnerRouter = require('./routes/ShopOwner');
const ServiceTypeRouter = require('./routes/ServiceType');
const ServiceSubTypeRouter = require('./routes/ServiceSubType');
const StateRouter = require('./routes/State');
const CityRouter = require('./routes/City');
const AreaRouter = require('./routes/Area');
const ShopServiceRouter = require('./routes/shopservice');
const BookingRouter = require('./routes/Booking');
const FeedBackRouter = require('./routes/FeedBack');

const app = express();

// View engine
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'ejs');

// Middlewares
app.use(logger('dev'));
app.use(cookieParser());
app.use(cors());
app.use(express.static(path.join(__dirname, 'public')));
app.use(express.json());          // for parsing JSON
app.use(express.urlencoded({ extended: true }));  // for parsing URL-encoded
app.use((req, res, next) => {
  res.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, private");
  res.setHeader("Pragma", "no-cache");
  res.setHeader("Expires", "0");
  next();
});

// Serve uploads
app.use('/uploads', express.static(path.join(__dirname, 'uploads')));

// Routes
app.use('/', indexRouter);
app.use('/users', usersRouter);
app.use('/admin', AdminRouter);
app.use('/serviceType', ServiceTypeRouter);
app.use('/customer', CustomerRouter);
app.use('/shopowner', ShopOwnerRouter);
app.use('/ServiceSubType', ServiceSubTypeRouter);
app.use('/State', StateRouter);
app.use('/City', CityRouter);
app.use('/Area', AreaRouter);
app.use('/shopservice', ShopServiceRouter);
app.use('/Booking',BookingRouter);
app.use('/FeedBack',FeedBackRouter);
// Catch 404
app.use((req, res, next) => {
  res.status(404).json({ success: false, message: "Not Found" });
});

// Error handler (return JSON instead of HTML)
app.use((err, req, res, next) => {
  console.error(err);
  res.status(err.status || 500).json({
    success: false,
    message: err.message || "Internal Server Error",
    error: req.app.get('env') === 'development' ? err : {}
  });
});


// MongoDB connection
const ConnectionString = "mongodb://localhost:27017/PannalAdmin";
mongoose.connect(ConnectionString)
  .then(() => console.log("Successfully Connected to Database."))
  .catch(error => console.log("Database connection error:", error));

module.exports = app;
