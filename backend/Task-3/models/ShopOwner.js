const mongoose = require('mongoose');

const ShopOwner = new mongoose.Schema({
  Ownername: { type: String},
  shopName: { type: String},
  email: { type: String},
  password: { type: String},
  phone: { type: String},
  address: { type: String},
  state:{ type:String},
  city:{ type:String},
  area:{type : String},
  idProof: { type: String },          
  shopPhoto: { type: String },       
  ownerPhoto: { type: String } ,
  status: { type: String, default: "pending" },
  bio: { type: String, default: "" },
    ratings: { type: [Number], default: [] } ,
  EntryDate: { type: Date}
  
});

module.exports = mongoose.model('ShopOwner', ShopOwner);
