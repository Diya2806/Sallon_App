const express = require('express');
const router = express.Router();
const multer = require('multer');
const path = require('path');
const nodemailer = require('nodemailer');
const ShopOwnerTbl = require('../models/ShopOwner');


// Multer setup
const storage = multer.diskStorage({
  destination: (req, file, cb) => cb(null, path.join(__dirname, '../uploads')),
  filename: (req, file, cb) => cb(null, file.fieldname + '_' + Date.now() + path.extname(file.originalname))
});
const upload = multer({ storage });

// Accept 3 image fields
const cpUpload = upload.fields([
  { name: 'idProof', maxCount: 1 },
  { name: 'shopPhoto', maxCount: 1 },
  { name: 'ownerPhoto', maxCount: 1 }
]);





// POST: Shop owner register
router.post('/register', cpUpload, async (req, res) => {
  try {
    console.log("req.body:", req.body);
    console.log("req.files:", req.files);
    
const Ownername = req.body?.Ownername;
const shopName = req.body?.shopName;
const email = req.body?.email;
const password = req.body?.password;
const phone = req.body?.phone;
const address = req.body?.address;
const state = req.body?.state;
const city = req.body?.city;
const area = req.body?.area;


    if (!Ownername || !shopName || !email || !password || !phone) {
      return res.status(400).json({ Message: "Required fields missing" });
    }
      const existingShop = await ShopOwnerTbl.findOne({ email: email });
    if (existingShop) {
      return res.status(400).json({ Message: "Email already registered" });
    }

    const baseUrl = req.protocol + '://' + req.get('host');
    const newShop = new ShopOwnerTbl({
      Ownername ,
      shopName,
      email,
      password,
      phone,
      address,
      state,
      city,
      area,
      idProof: req.files?.idProof ? baseUrl + "/uploads/" + req.files.idProof[0].filename : "",
      shopPhoto: req.files?.shopPhoto ? baseUrl + "/uploads/" + req.files.shopPhoto[0].filename : "",
      ownerPhoto: req.files?.ownerPhoto ? baseUrl + "/uploads/" + req.files.ownerPhoto[0].filename : "",
      status: "pending",
      EntryDate: new Date()
    });

    const savedShop = await newShop.save();

    // Send email: under review
    const transporter = nodemailer.createTransport({
      service: "gmail",
      auth: { user: "pateldiyaashokbhai@gmail.com", pass: "xhckfkkinufidqow" }
    });

    await transporter.sendMail({
      from: "pateldiyaashokbhai@gmail.com",
      to: savedShop.email,
      subject: "Shop Registration: Under Review",
      text: `Hello ${savedShop.Ownername},\n\nYour shop "${savedShop.shopName}" registration is under review. You will be notified once admin approves it.\n\nThank you!`
    });

    res.status(200).json({ Message: "Shop registered successfully", Data: savedShop });

  } catch (error) {
    console.error(error);
    res.status(500).json({ Message: "Something went wrong", Errors: error.toString() });
  }
});

// PUT: Admin approves shop
router.put('/approve/:id', async (req, res) => {
  try {
    const { status } = req.body;
    if (!status) return res.status(400).json({ Message: "Status is required" });

    const updatedShop = await ShopOwnerTbl.findByIdAndUpdate(
      req.params.id,
      { status },
      { new: true }
    );

    if (!updatedShop) return res.status(404).json({ Message: "Shop owner not found" });

    if (status === "approved") {
      const transporter = nodemailer.createTransport({
        service: "gmail",
        auth: { user: "pateldiyaashokbhai@gmail.com", pass: "xhckfkkinufidqow" }
      });

      await transporter.sendMail({
        from: "pateldiyaashokbhai@gmail.com",
        to: updatedShop.email,
        subject: "Shop Approved",
        text: `Hello ${updatedShop.Ownername},\n\nYour shop "${updatedShop.shopName}" has been approved. You can now log in.\n\nThank you!`
      });
    }

    res.status(200).json({ Message: `Shop status updated to ${status}`, Data: updatedShop });

  } catch (error) {
    res.status(500).json({ Message: "Failed to update status", Errors: error.toString() });
  }
});


// GET approved shops for customers
router.get('/shoplist', async (req, res) => {
  try {
    const shops = await ShopOwnerTbl.find(
      { status: "approved" },      // filter only approved shops
      "_id shopName Ownername"     // select only necessary fields
    );

    res.status(200).json({
      Message: "Get Approved Shops",
      List: shops
    });
  } catch (error) {
    res.status(500).json({
      Message: "Something went wrong",
      Errors: error
    });
  }
});
router.get('/allshops', async (req, res) => {
  try {
    const shops = await ShopOwnerTbl.find({ status: "approved" });

    const result = shops.map(shop => {
      const ratings = Array.isArray(shop.ratings) ? shop.ratings : [];
      const avgRating = ratings.length
        ? ratings.reduce((a, b) => a + b, 0) / ratings.length
        : 0;

      return {
        shopId: shop._id || "",
        shopName: shop.shopName || "Unnamed Shop",
        Ownername: shop.Ownername || "Unknown",
        rating: Number(avgRating.toFixed(1)),
        shopPhoto: shop.shopPhoto || "",
        bio: shop.bio || ""
      };
    });

    res.status(200).json({
      Message: "Approved shops with ratings",
      List: result
    });

  } catch (err) {
    console.error(err);
    res.status(500).json({
      Message: "Something went wrong",
      Errors: err.message || err.toString()
    });
  }
});




router.post('/send-otp', async (req, res) => {
  try {
    const { email } = req.body;
    if (!email) return res.status(400).json({ Message: "Email is required" });

    // Check if email exists
    const shop = await ShopOwnerTbl.findOne({ email });
    if (!shop) return res.status(404).json({ Message: "Email not found" });

    // Generate 6-digit OTP
    const otp = Math.floor(100000 + Math.random() * 900000);

    // Send OTP via email
    const transporter = nodemailer.createTransport({
      service: "gmail",
      auth: { user: "pateldiyaashokbhai@gmail.com", pass: "xhckfkkinufidqow" }
    });

    await transporter.sendMail({
      from: "pateldiyaashokbhai@gmail.com",
      to: email,
      subject: "Your OTP Code",
      text: `Hello ${shop.Ownername},\n\nYour OTP for password reset is: ${otp}. It will expire in 5 minutes.`
    });

    res.status(200).json({
      Message: "OTP sent successfully",
      OTP: otp,        
      userId: shop._id 
    });
  } catch (error) {
    console.error(error);
    res.status(500).json({ Message: "Failed to send OTP", Errors: error.toString() });
  }
});


router.put("/newpass/:id", async (req, res) => {
  try {
    const { password } = req.body;

    if (!password) {
      return res.status(400).json({ message: "Password is required" });
    }


    const user = await ShopOwnerTbl.findByIdAndUpdate(
      req.params.id,
       { password }, 
      { new: true }
    );

    if (!user) {
      return res.status(404).json({ message: "User not found" });
    }
    res.status(200).json({ message: "Password updated successfully" });
  } catch (err) {
    console.error(err);
    res.status(500).json({ message: "Server error" });
  }
});
// PUT: Update bio
router.put('/update-bio/:id', async (req, res) => {
    try {
        const { bio } = req.body;
        const updatedShop = await ShopOwnerTbl.findByIdAndUpdate(
            req.params.id,
            { bio },
            { new: true }
        );
        res.status(200).json({ Message: "Bio updated", Data: updatedShop });
    } catch (error) {
        res.status(500).json({ Message: "Failed to update bio", Errors: error.toString() });
    }
});

router.get('/login/:email/:password', async (req, res) => {
    try {
        const { email, password } = req.params; 

        if (!email || !password) {
            return res.status(400).json({ success: false, Message: "Email and Password required" });
        }

        const shop = await ShopOwnerTbl.findOne({ email });
        if (!shop) return res.status(404).json({ success: false, Message: "Email not found" });

        if (shop.password !== password) { 
            return res.status(401).json({ success: false, Message: "Invalid Password" });
        }

        if (shop.status !== "approved") {
            return res.status(403).json({ success: false, Message: "Shop not approved yet" });
        }

        res.status(200).json({
            success: true,
            Message: "Login Successful",
            userId: shop._id,
            Ownername: shop.Ownername,
            shopName: shop.shopName
        });

    } catch (error) {
        console.error(error);
        res.status(500).json({ success: false, Message: "Server Error", Errors: error.toString() });
    }
});


// ✅ DELETE: Remove shop owner
router.delete('/:id', async (req, res) => {
  try {
    const deletedShop = await ShopOwnerTbl.findByIdAndDelete(req.params.id);
    if (!deletedShop) return res.status(404).json({ Message: "Shop owner not found" });
    res.status(200).json({ Message: "Shop deleted successfully", Data: deletedShop });
  } catch (error) {
    res.status(500).json({ Message: "Failed to delete shop", Errors: error.toString() });
  }
});

/* GET home page. */
router.get('/', async(req, res, next)=> {
   try{
    await ShopOwnerTbl.find()
    .then((response)=>{
        res.status(200).json({
            Message:"Get Data.",
            List:response
        });
    })
    .catch((error)=>{
        res.status(500).json({
            Message:"1 Somthing Went Wrong.",
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
        await ShopOwnerTbl.findById(id)
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





module.exports = router;
