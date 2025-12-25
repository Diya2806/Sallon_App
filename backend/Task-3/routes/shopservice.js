var express = require('express');
var router = express.Router();
var ShopServiceTbl = require("../models/shopservice");
const multer = require('multer');
const path = require("path");
const mongoose = require('mongoose');



const storage = multer.diskStorage({
  destination: (req, file, cb) => cb(null, path.join(__dirname, '../uploads')),
  filename: (req, file, cb) =>
    cb(null, file.fieldname + '_' + Date.now() + path.extname(file.originalname))
});
const upload = multer({ storage });




router.get('/customer/shop/:shopId', async (req, res) => {
  try {
    const { shopId } = req.params;

    const result = await ShopServiceTbl.aggregate([
      { $match: { ShopId: new mongoose.Types.ObjectId(shopId) } },
      {
        $lookup: {
          from: "servicetypes",
          localField: "ServiceTypeID",
          foreignField: "_id",
          as: "ServiceType"
        }
      },
      { $unwind: "$ServiceType" },
      {
        $lookup: {
          from: "servicesubtypes",
          localField: "ServiceSubTypeId",
          foreignField: "_id",
          as: "SubService"
        }
      },
      { $unwind: "$SubService" },
      {
        $group: {
          _id: "$ServiceType._id",
          ServiceTypeName: { $first: "$ServiceType.ServiceName" },
          SubServices: {
            $push: {
              ServiceTypeID: "$ServiceTypeID",
              SubServiceId: "$SubService._id",
              SubServiceName: "$SubService.SubServiceName",
              Title: "$Title",
              Price: "$Price",
              ServiceSubPhoto: "$ServiceSubPhoto"
            }
          }
        }
      },
      { $sort: { ServiceTypeName: 1 } }
    ]);

    if (result.length === 0) {
      return res.status(404).json({
        Message: "This shop has not added any services yet.",
        List: []
      });
    }

    res.status(200).json({
      Message: "Customer view: Services & SubServices added by shop owner.",
      List: result
    });

  } catch (error) {
    console.error(error);
    res.status(500).json({
      Message: "Server Error",
      Error: error.message
    });
  }
});


// GET services for a specific shop (filter by ShopId)
router.get('/shop/:shopId', async (req, res) => {
  try {
    const { shopId } = req.params;

    if (!shopId) {
      return res.status(400).json({ Message: "ShopId is required" });
    }

    // Convert to ObjectId
    const filter = { ShopId: new mongoose.Types.ObjectId(shopId) };

    const data = await ShopServiceTbl.aggregate([
      { $match: filter },
      {
        $lookup: {
          from: "servicetypes",
          localField: "ServiceTypeID",
          foreignField: "_id",
          as: "serviceType"
        }
      },
      {
        $lookup: {
          from: "servicesubtypes",
          localField: "ServiceSubTypeId",
          foreignField: "_id",
          as: "subService"
        }
      },
      { $unwind: { path: "$serviceType", preserveNullAndEmptyArrays: true } },
      { $unwind: { path: "$subService", preserveNullAndEmptyArrays: true } },
      {
        $project: {
          _id: 1,
          ShopId: 1,
          ServiceTypeID: 1,
          ServiceTypeName: "$serviceType.ServiceName",
          ServiceSubTypeId: 1,
          ServiceSubTypeName: "$subService.SubServiceName",
          Title: 1,
          Price: 1,
          ServiceSubPhoto: 1,
          EntryDate: 1
        }
      }
    ]);

    if (data.length === 0) {
      return res.status(404).json({ Message: "No services found for this shop", List: [] });
    }

    res.status(200).json({
      Message: "Services for this shop",
      List: data
    });

  } catch (error) {
    console.error(error);
    res.status(500).json({ Message: "Server error", Errors: error.toString() });
  }
});
router.get('/getPrice/:ShopId/:ServiceTypeID/:ServiceSubTypeId', async (req, res) => {
  try {
    const { ShopId, ServiceTypeID, ServiceSubTypeId } = req.params;

    const service = await ShopServiceTbl.findOne({
      ShopId:new mongoose.Types.ObjectId(ShopId),
      ServiceTypeID:new  mongoose.Types.ObjectId(ServiceTypeID),
      ServiceSubTypeId:new  mongoose.Types.ObjectId(ServiceSubTypeId)
    }).select("Title Price ServiceSubPhoto");

    if (!service) {
      return res.status(404).json({ Message: "No matching service found" });
    }

    res.json({
      Title: service.Title,
      Price: service.Price,
      ServiceSubPhoto: service.ServiceSubPhoto || ""
    });
  } catch (error) {
    console.error(error);
    res.status(500).json({ Message: "Server error", Error: error.message });
  }
});
//  GET admin by ID
router.get('/:id', async (req, res) => {
    try {
        var id = req.params.id;
        await ShopServiceTbl.findById(id)
            .then((response) => {
                if (!response) {
                    return res.status(404).json({ Message: "Admin Not Found" });
                }
                res.status(200).json({
                    Message: "Get Data By ID.",
                    Data: response
                });
            })
            .catch((error) => {
                res.status(500).json({
                    Message: "1 Something Went Wrong.",
                    Errors: error
                });
                console.log(error);
            });
    } catch (error) {
        res.status(500).json({
            Message: "2 Something Went Wrong.",
            Errors: error
        });
        console.log(error);
    }
});
//  POST create admin
router.post('/service', upload.single('ServiceSubPhoto'), async (req, res) => {
  try {
 
    var ServiceTypeID = req.body.ServiceTypeID;
    var ServiceSubTypeId = req.body.ServiceSubTypeId;
    var Title = req.body.Title;
    var Price = req.body.Price;

    if (!ServiceTypeID || !ServiceSubTypeId || !Title || !Price) {
      return res.status(400).json({ Message: "Required fields missing" });
    }

    const baseUrl = req.protocol + '://' + req.get('host');

    const newService = new ShopServiceTbl({
      ShopId: req.body.ShopId, 
      ServiceTypeID,
      ServiceSubTypeId,
      Title,
      Price,
      ServiceSubPhoto: req.file ? baseUrl + "/uploads/" + req.file.filename : "",
      EntryDate: new Date()
    });

    const savedService = await newService.save();

    res.status(200).json({ Message: "Service added successfully", Data: savedService });
  } catch (error) {
    console.error(error);
    res.status(500).json({ Message: "Something went wrong", Errors: error.toString() });
  }
});
/* GET home page. */
router.get('/', async (req, res, next) => {
    try {
        const filter = {};
        if (req.query.ShopId) filter.ShopId = req.query.ShopId;

        const data = await ShopServiceTbl.aggregate([
         
            {
                $lookup: {
                    from: "servicetypes", 
                    localField: "ServiceTypeID",
                    foreignField: "_id",
                    as: "serviceType"
                }
            },
            
            {
                $lookup: {
                    from: "servicesubtypes", 
                    localField: "ServiceSubTypeId",
                    foreignField: "_id",
                    as: "subService"
                }
            },
           
            { $unwind: { path: "$serviceType", preserveNullAndEmptyArrays: true } },
            { $unwind: { path: "$subService", preserveNullAndEmptyArrays: true } },
         
            {
                $project: {
                    _id: 1,
                    ShopId: 1, 
                    ServiceTypeID: 1,
                    ServiceTypeName: "$serviceType.ServiceName",
                    ServiceSubTypeId: 1,
                    ServiceSubTypeName: "$subService.SubServiceName",
                    Title: 1,
                    Price: 1,
                    ServiceSubPhoto: 1,
                    EntryDate: 1
                }
            }
        ]);

        res.status(200).json({
            Message: "Get Data.",
            List: data
        });

    } catch (error) {
        console.log(error);
        res.status(500).json({
            Message: "Something went wrong",
            Errors: error
        });
    }
});

//  PUT update admin
router.put('/:id', async (req, res) => {
    try {
        var id = req.params.id;
        var model = {
            ShopId: req.body.ShopId, 
            ServiceTypeID: req.body.ServiceTypeID,
            ServiceSubTypeId: req.body.ServiceSubTypeId ,
            Title: req.body.Title,
            ServiceSubPhoto:req.body.ServiceSubPhoto,
            Price:req.body.Price
        };

        await ShopServiceTbl.findByIdAndUpdate(id, model, { new: true })
            .then((response) => {
                if (!response) {
                    return res.status(404).json({ Message: "Admin Not Found" });
                }
                res.status(200).json({
                    Message: "Shop Service Updated Successfully",
                    Data: response
                });
            })
            .catch((error) => {
                res.status(500).json({
                    Message: "1 Something Went Wrong.",
                    Errors: error
                });
                console.log(error);
            });
    } catch (error) {
        res.status(500).json({
            Message: "2 Something Went Wrong.",
            Errors: error
        });
        console.log(error);
    }
});
router.delete('/:id', async (req, res) => {
    try {
        var id = req.params.id;

        await ShopServiceTbl.findByIdAndDelete(id)
            .then((response) => {
                if (!response) {
                    return res.status(404).json({ Message: "Admin Not Found" });
                }
                res.status(200).json({
                    Message: "Shop Service Deleted Successfully",
                    Data: response
                });
            })
            .catch((error) => {
                res.status(500).json({
                    Message: "1 Something Went Wrong.",
                    Errors: error
                });
                console.log(error);
            });
    } catch (error) {
        res.status(500).json({
            Message: "2 Something Went Wrong.",
            Errors: error
        });
        console.log(error);
    }
});





module.exports = router;
