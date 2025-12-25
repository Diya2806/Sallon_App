const mongoose = require('mongoose');

const Service = new mongoose.Schema({
    ServiceName: { type: String },  
    status: { type: String},
    EntryDate: { type: Date } 
});

module.exports = mongoose.model('ServiceType', Service);