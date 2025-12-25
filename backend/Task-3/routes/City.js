var express = require('express');
var router = express.Router();
var CityTbl = require('../models/City');

/* GET City. */
router.get('/', async(req, res, next)=> {
   try{
    await CityTbl.find()
    .then((response)=>{
        res.status(200).json({
            Message:"Get Data.",
            List:response
        });
    })
    .catch((error)=>{
        res.status(500).json({
            Message:"2 Somthing Went Wrong.",
            Errors:error
        });
        console.log(error);
    });

   }catch(error){
     res.status(500).json({
      Message:"2 Somthing Went Wrong.",
      Errors:error
    });
    console.log(error);
   }
});


//  GET City by ID
router.get('/:id', async (req, res) => {
    try {
        var id = req.params.id;
        await CityTbl.findById(id)
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


//  POST create City
router.post('/', async (req, res, next) => {
    try {
        var Citymodel = {
            StateId:req.body.StateId,
            CityName:req.body.CityName,
            EntryDate: new Date().toUTCString()
        };

        const Data = new CityTbl(Citymodel);

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

//  PUT update City
router.put('/:id', async (req, res) => {
    try {
        var id = req.params.id;
        var Citymodel = {
            StateId:req.body.StateId,
            CityName:req.body.CityName
        };

        await CityTbl.findByIdAndUpdate(id, Citymodel, { new: true })
            .then((response) => {
                if (!response) {
                    return res.status(404).json({ Message: "Admin Not Found" });
                }
                res.status(200).json({
                    Message: "City Updated Successfully",
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

//  DELETE City
router.delete('/:id', async (req, res) => {
    try {
        var id = req.params.id;

        await CityTbl.findByIdAndDelete(id)
            .then((response) => {
                if (!response) {
                    return res.status(404).json({ Message: "Admin Not Found" });
                }
                res.status(200).json({
                    Message: "City Deleted Successfully",
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