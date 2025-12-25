var express = require('express');
var router = express.Router();
var AdminTbl = require('../models/AdminTbl');
const multer = require('multer');


 const storage = multer.diskStorage({
            destination: function (req, file, cb) {
                cb(null, 'uploads/');
            },
            filename: function (req, file, cb) {
                const uniqueSuffix = Date.now() + '-' + Math.round(Math.random() * 1E9);
                cb(null, uniqueSuffix + '-' + file.originalname);
            }
        });
        const upload = multer({ storage: storage });


/* GET home page. */
router.get('/', async(req, res, next)=> {
   try{
    await AdminTbl.find()
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
// PUT /admin/:id/image
router.put('/:id/image', upload.single('image'), async (req, res) => {
    try {
        res.set('Cache-Control', 'no-store');
        const id = req.params.id;
        if (!req.file) return res.status(400).json({ Message: "No file uploaded" });

        const baseUrl = req.protocol + "://" + req.get('host');
        const imageUrl = baseUrl + "/uploads/" + req.file.filename;


        const updatedAdmin = await AdminTbl.findByIdAndUpdate(id, 
            { Image: imageUrl }, 
            { new: true }
        );
        
        if (!updatedAdmin) {
            return res.status(404).json({ message: "admin not found" });
        }

        res.status(200).json({ Message: "Image Updated Successfully", Data: updatedAdmin });
    } catch (error) {
        console.log(error);
        res.status(500).json({ Message: "Error uploading image", Errors: error });
    }
});

//  GET admin by ID
router.get('/:id', async (req, res) => {
    try {
        var id = req.params.id;
        await AdminTbl.findById(id)
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
        var model = {
            Name: req.body.Name,
            Email: req.body.Email,
            Mobile: req.body.Mobile,
            Password:req.body.Password,
            EntryDate: new Date().toUTCString()
        };

        const Data = new AdminTbl(model);

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
        var model = {
            Name: req.body.Name,
            Email: req.body.Email,
            Mobile: req.body.Mobile,
            Password:req.body.Password
        };

        await AdminTbl.findByIdAndUpdate(id, model, { new: true })
            .then((response) => {
                if (!response) {
                    return res.status(404).json({ Message: "Admin Not Found" });
                }
                res.status(200).json({
                    Message: "Admin Updated Successfully",
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

        await AdminTbl.findByIdAndDelete(id)
            .then((response) => {
                if (!response) {
                    return res.status(404).json({ Message: "Admin Not Found" });
                }
                res.status(200).json({
                    Message: "Admin Deleted Successfully",
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
