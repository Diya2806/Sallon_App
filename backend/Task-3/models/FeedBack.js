const mongoose = require('mongoose');
const Feedback = new mongoose.Schema({
    CustomerId :{type:mongoose.Schema.ObjectId, ref:'Customer'},
    ShopId : {type:mongoose.Schema.ObjectId, ref:'ShopOwner'},
    rating: { type: Number, required: true },
    comment: { type: String },
    EntryDate: { type: Date, default: Date.now } 

});

module.exports = mongoose.model('Feedback',Feedback);