const mongoose = require('mongoose');

const ShopService = new mongoose.Schema({
    ShopId: { type: mongoose.Schema.ObjectId, ref: "ShopOwner" },
    ServiceTypeID:{type: mongoose.Schema.ObjectId , ref:"ServiceType"},
    ServiceSubTypeId :{type:mongoose.Schema.ObjectId,ref:"servicesubType"},
    Title:{type : String},
    ServiceSubPhoto : {type:String},
    Price : {type:String},
    EntryDate: { type: Date } 
});

module.exports = mongoose.model('ShopService',ShopService );