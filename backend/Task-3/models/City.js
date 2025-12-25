const mongoose = require('mongoose');

const City = new mongoose.Schema({
    StateId:{type: mongoose.Schema.ObjectId , ref:"state"},
    CityName:{type : String},
    EntryDate: { type: Date } 
});

module.exports = mongoose.model('City',City );