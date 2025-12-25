var express = require('express');
var router = express.Router();
var StateTbl = require('../models/State');

/* GET State. */
router.get('/', async(req, res, next)=> {
   try{
    await StateTbl.find()
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

//  GET State by ID
router.get('/:id', async (req, res) => {
    try {
        var id = req.params.id;
        await StateTbl.findById(id)
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

//  POST create State
router.post('/', async (req, res, next) => {
    try {
        var Statemodel = {
            StateName:req.body.StateName,
            EntryDate: new Date().toUTCString()
        };

        const Data = new StateTbl(Statemodel);

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
        var Statemodel = {
            StateName:req.body.StateName
        };

        await StateTbl.findByIdAndUpdate(id, Statemodel, { new: true })
            .then((response) => {
                if (!response) {
                    return res.status(404).json({ Message: "Admin Not Found" });
                }
                res.status(200).json({
                    Message: "State Updated Successfully",
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

        await StateTbl.findByIdAndDelete(id)
            .then((response) => {
                if (!response) {
                    return res.status(404).json({ Message: "Admin Not Found" });
                }
                res.status(200).json({
                    Message: "State Deleted Successfully",
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
