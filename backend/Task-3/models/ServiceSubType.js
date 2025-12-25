const mongoose = require('mongoose');

const SubServiceTypeSchema = new mongoose.Schema({
  ServiceTypeId: { type: mongoose.Schema.ObjectId, ref: 'ServiceType' },
  SubServiceName: { type: String },
  Status: { type: String },
  EntryDate: { type: Date }
});

module.exports = mongoose.model('ServiceSubType', SubServiceTypeSchema);
