    var express = require('express');
    var router = express.Router();
    var FeedbackTbl = require('../models/FeedBack');
    var mongoose = require('mongoose');     
    /* GET users listing. */
    router.get('/',async (req, res, next) =>{
    try {
            const FeedBack = await FeedbackTbl.find();
            res.status(200).json({ Message: "All bookings", List: FeedBack });
        } catch (error) {
            res.status(500).json({ Message: "Something went wrong", Errors: error.message });
        }
    });

router.get('/shop/:shopId', async (req, res) => {
  try {
    const shopId = req.params.shopId;

    const feedbackList = await FeedbackTbl.aggregate([
      {
        $match: { ShopId: new mongoose.Types.ObjectId(shopId) }
      },
      {
        $lookup: {
          from: 'customers', 
          localField: 'CustomerId',
          foreignField: '_id',
          as: 'CustomerData'
        }
      },
      {
        $unwind: '$CustomerData'
      },
      {
        $project: {
          _id: 1,
          rating: 1,
          comment: 1,
          EntryDate: 1,
          'CustomerData._id': 1,
           'CustomerData.Customername': 1, 
          'CustomerData.image': 1
        }
      }
    ]);

    res.status(200).json({
      success: true,
      count: feedbackList.length,
      data: feedbackList
    });

  } catch (error) {
    console.error('Lookup Error:', error);
    res.status(500).json({ success: false, message: error.message });
  }
});
    //  GET by ID
    router.get('/:id', async (req, res) => {
        try {
            var id = req.params.id;
            await FeedbackTbl.findById(id)
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
    //  POST
    router.post('/', async (req, res, next) => {
        try {
            var FeedBackModel = {
                CustomerId:req.body.CustomerId,
                ShopId:req.body.ShopId,
                rating : req.body.rating,
                comment:req.body.comment,
                EntryDate: new Date().toUTCString()
            };

            const Data = new FeedbackTbl(FeedBackModel);

            await Data.save()
                .then((response) => {
                    res.status(200).json({
                        Message: "Post Data",
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
    //  PUT
    router.put('/:id', async (req, res) => {
        try {
            var id = req.params.id;
             var FeedBackModel = {
                CustomerId:req.body.CustomerId,
                ShopId:req.body.ShopId,
                rating : req.body.rating,
                comment:req.body.comment,
                EntryDate: new Date().toUTCString()
            };
    
            await FeedbackTbl.findByIdAndUpdate(id, FeedBackModel, { new: true })
                .then((response) => {
                    if (!response) {
                        return res.status(404).json({ Message: "Admin Not Found" });
                    }
                    res.status(200).json({
                        Message: "Updated Successfully",
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

    //  DELETE 
    router.delete('/:id', async (req, res) => {
        try {
            var id = req.params.id;

            await FeedbackTbl.findByIdAndDelete(id)
                .then((response) => {
                    if (!response) {
                        return res.status(404).json({ Message: "Admin Not Found" });
                    }
                    res.status(200).json({
                        Message: "Deleted Successfully",
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
