const express = require('express');
const router = express.Router();
const mongoose = require('mongoose');
const ServiceSubType = require('../models/ServiceSubType');
const ServiceType = require('../models/ServiceType');


router.get('/', async (req, res) => {
  try {
    const result = await ServiceSubType.aggregate([

      
      {
        $lookup: {
          from: 'servicetypes',           
          localField: 'ServiceTypeId',    
          foreignField: '_id',            
          as: 'ServiceTypeData'
        }
      }, { $unwind: '$ServiceTypeData' }, 
           {
        $project: {
          _id: 1,
          SubServiceName: 1,
          Status: 1,
          EntryDate: 1,
          ServiceTypeId: 1,
          ServiceTypeName: '$ServiceTypeData.ServiceName'
        }
      }
    ]);

    res.status(200).json({
      Message: 'Get Data.',
      List: result
    });
  } catch (error) {
    res.status(500).json({ Message: "Something went wrong", Error: error.message });
  }
});


// router.get('/:id', async (req, res) => {
//   try {
//     const id = req.params.id;
//     const subService = await ServiceSubType.findById(id).populate('ServiceTypeId', 'ServiceName');
//     if (!subService) {
//       return res.status(404).json({ Message: "SubService Not Found" });
//     }
//     res.status(200).json({
//       Message: "Get Data By ID.",
//       Data: subService
//     });
//   } catch (error) {
//     res.status(500).json({ Message: "Something went wrong", Error: error.message });
//   }
// });

router.get('/:id', async (req, res) => {
  try {
    const id = req.params.id; // this is your serviceTypeId

    if (!id) {
      return res.status(400).json({ Message: "ServiceTypeId required" });
    }

    // Aggregation with lookup
    const subServices = await ServiceSubType.aggregate([
      { $match: { ServiceTypeId: new mongoose.Types.ObjectId(id) } },
      {
        $lookup: {
          from: 'servicetypes',        // collection name
          localField: 'ServiceTypeId', // field in ServiceSubType
          foreignField: '_id',         // field in ServiceType
          as: 'serviceData'
        }
      },
      { $unwind: '$serviceData' },      // flatten the array
      {
        $project: {
          _id: 1,
          SubServiceName: 1,
          Status: 1,
          EntryDate: 1,
          ServiceTypeId: 1,
          ServiceTypeName: '$serviceData.ServiceName'
        }
      }
    ]);

    res.status(200).json({
      Message: "Filtered sub-services with service name",
      List: subServices
    });
  } catch (error) {
    console.error(error);
    res.status(500).json({ Message: "Server Error", Error: error.message });
  }
});

router.post('/', async (req, res) => {
  try {
    // validate ServiceTypeId exists
    const serviceType = await ServiceType.findById(req.body.ServiceTypeId);
    if (!serviceType) {
      return res.status(400).json({ Message: "Invalid ServiceTypeId" });
    }

    const subService = new ServiceSubType({
      ServiceTypeId: req.body.ServiceTypeId,
      SubServiceName: req.body.SubServiceName,
      Status: req.body.Status,
      EntryDate: req.body.EntryDate || new Date()
    });

    await subService.save();

    // populate service name in response
    await subService.populate('ServiceTypeId', 'ServiceName');

    res.status(201).json({
      Message: "SubService created successfully",
      Data: subService
    });
  } catch (error) {
    res.status(500).json({ Message: "Something went wrong", Error: error.message });
  }
});


router.put('/:id', async (req, res) => {
  try {
    const id = req.params.id;
    const updateData = {
      ServiceTypeId: req.body.ServiceTypeId,
      SubServiceName: req.body.SubServiceName,
      Status: req.body.Status
    };

    const updated = await ServiceSubType.findByIdAndUpdate(id, updateData, { new: true }).populate('ServiceTypeId', 'ServiceName');
    if (!updated) {
      return res.status(404).json({ Message: "SubService Not Found" });
    }

    res.status(200).json({
      Message: "SubService Updated Successfully",
      Data: updated
    });
  } catch (error) {
    res.status(500).json({ Message: "Something went wrong", Error: error.message });
  }
});

router.delete('/:id', async (req, res) => {
  try {
    const id = req.params.id;
    const deleted = await ServiceSubType.findByIdAndDelete(id);
    if (!deleted) {
      return res.status(404).json({ Message: "SubService Not Found" });
    }
    res.status(200).json({
      Message: "SubService Deleted Successfully",
      Data: deleted
    });
  } catch (error) {
    res.status(500).json({ Message: "Something went wrong", Error: error.message });
  }
});

module.exports = router;
