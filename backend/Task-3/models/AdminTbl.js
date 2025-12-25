const mongoose = require('mongoose');

const AdminTbl = new mongoose.Schema({
    Name: { type: String },
    Email: { type: String},
    Mobile: { type: String },
    Password:{type:String},
    Image: { type: String },
    EntryDate: { type: Date } 
});

module.exports = mongoose.model('Admin', AdminTbl);