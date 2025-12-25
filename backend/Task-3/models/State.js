const mongoose = require('mongoose');

const State = new mongoose.Schema({

   StateName:{type : String},
   EntryDate: { type: Date } 
});

module.exports = mongoose.model('state',State );