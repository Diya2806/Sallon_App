var express = require('express');
var router = express.Router();
const ServiceTbl = require('../models/ServiceType')

/* GET home page. */
router.get('/', async(req, res, next)=> {
   try{
    await ServiceTbl.find()
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

//  GET admin by ID
router.get('/:id', async (req, res) => {
    try {
        var id = req.params.id;
        await ServiceTbl.findById(id)
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
router.post('/', async (req, res, next) => {
    try {
        var Servicemodel = {
            ServiceName: req.body.ServiceName,
            status:req.body.status,
            EntryDate: new Date().toUTCString()
        };

        const Data = new ServiceTbl(Servicemodel);

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

//  PUT update admin
router.put('/:id', async (req, res) => {
    try {
        var id = req.params.id;
        var Servicemodel = {
           ServiceName: req.body.ServiceName,
           status:req.body.status
        };

        await ServiceTbl    .findByIdAndUpdate(id, Servicemodel, { new: true })
            .then((response) => {
                if (!response) {
                    return res.status(404).json({ Message: "Admin Not Found" });
                }
                res.status(200).json({
                    Message: "ServiceType Updated Successfully",
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

//  DELETE admin
router.delete('/:id', async (req, res) => {
    try {
        var id = req.params.id;

        await ServiceTbl.findByIdAndDelete(id)
            .then((response) => {
                if (!response) {
                    return res.status(404).json({ Message: "Admin Not Found" });
                }
                res.status(200).json({
                    Message: "ServiceType Deleted Successfully",
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