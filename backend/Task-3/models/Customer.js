// models/Customer.js
const mongoose = require('mongoose');

const Customer = new mongoose.Schema({
    Customername: { type: String },
    email: { type: String },
    password: { type: String },
    phone: { type: String},
    address: { type: String},
    image: { type: String, default: "" },
    EntryDate: { type: Date } 
});

module.exports = mongoose.model('Customer', Customer);