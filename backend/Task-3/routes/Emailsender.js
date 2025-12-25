var express = require('express');
var router = express.Router();
const nodemailer = require('nodemailer');
const bodyParser = require('body-parser');
const Shop = require('../models/ShopOwner'); // correct

/* GET home page. */
router.get('/', function(req, res, next) {
  res.render('index', { title: 'Express' });
});

// Approve shop - separate route
router.post('/approve', async (req, res) => {
    const { shopId } = req.body;
    if (!shopId) return res.status(400).json({ Message: "shopId is required" });

    try {
        const shop = await Shop.findById(shopId);
        if (!shop) return res.status(404).json({ Message: "Shop not found" });

        // if (shop.status === "approved") {
        //     return res.status(200).json({ Message: "Shop is already approved" });
        // }

        // Update status to approved
        shop.status = "approved";
        await shop.save();

        // Send email
        const transporter = nodemailer.createTransport({
            service: 'gmail',
            auth: { 
                user: 'pateldiyaashokbhai@gmail.com', 
                pass: 'xhckfkkinufidqow' 
            }
        });

        const mailOptions = {
            from: 'pateldiyaashokbhai@gmail.com',
            to: shop.email,
            subject: 'Your Shop is Approved!',
            text: `Hello ${shop.Ownername},\n\nYour shop "${shop.shopName}" has been approved. You can now log in.\n\nThank you!`
        };

        transporter.sendMail(mailOptions, (error, info) => {
            if (error) {
                console.error(error);
                return res.status(500).json({ Message: "Email failed", Error: error });
            }
            res.status(200).json({ Message: "Shop approved and email sent" });
        });

    } catch (err) {
        console.error(err);
        res.status(500).json({ Message: "Server error", Error: err });
    }
});






module.exports = router;
