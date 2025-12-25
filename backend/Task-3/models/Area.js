const mongoose = require('mongoose');

const Area = new mongoose.Schema({
    CityId:{type: mongoose.Schema.ObjectId , ref:"City"},
    AreaName:{type : String},
    EntryDate: { type: Date } 
});

module.exports = mongoose.model('Area',Area );