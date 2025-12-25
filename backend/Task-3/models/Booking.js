const mongoose = require('mongoose');
const Booking = new mongoose.Schema({
    CustomerId :{type:mongoose.Schema.ObjectId, ref:'Customer'},
    ShopId : {type:mongoose.Schema.ObjectId, ref:'ShopOwner'},
    ServiceId : {type:mongoose.Schema.ObjectId,ref:'ServiceType'},
    ServiceSubID :{type:mongoose.Schema.ObjectId,ref:'ServiceSubType'},
    DateBook : {type:String},
     Title: { type: String },   
    TimeBook : {type:String},
     Price: {type:String},  
    status:{type:String, default: "Pending"},
    
    EntryDate: { type: Date, default: Date.now } 

});

module.exports = mongoose.model('Booking',Booking);