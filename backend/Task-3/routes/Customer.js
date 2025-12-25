    var express = require('express');
    var router = express.Router();
    const nodemailer = require("nodemailer");
    var CustomerTbl = require('../models/Customer');
    const multer = require('multer');

    // Multer setup to store images in 'uploads/' folder
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



    router.get('/login/:email/:password', async (req, res) => {
        try {
            const { email, password } = req.params; 

            if (!email || !password) {
                return res.status(400).json({ success: false, Message: "Email and Password required" });
            }

            const customer = await CustomerTbl.findOne({ email });
            if (!customer) return res.status(404).json({ success: false, Message: "Email not found" });

            if (customer.password !== password) { 
                return res.status(401).json({ success: false, Message: "Invalid Password" });
            }

            res.status(200).json({
                success: true,
                Message: "Login Successful",
                userId: customer._id,
                Customername: customer.Customername,
                email: customer.email
            });

        } catch (error) {
            console.error(error);
            res.status(500).json({ success: false, Message: "Server Error", Errors: error.toString() });
        }
    });
    /* GET  Customer List. */
    router.get('/', async(req, res, next)=> {
    try{
        await CustomerTbl.find()
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


    //  GET Customer by ID
    router.get('/:id', async (req, res) => {
        try {
            var id = req.params.id;
            await CustomerTbl.findById(id)
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



    // POST create Customer
    router.post('/', async (req, res, next) => {
        try {
            const { Customername, email, phone, password, address } = req.body;

       
        const trimmedEmail = email.trim().toLowerCase();

        const existingCustomer = await CustomerTbl.findOne({ email: { $regex: new RegExp(`^${trimmedEmail}$`, 'i') } });
        if (existingCustomer) {
            return res.status(400).json({
                Message: "Email already registered",
            });
        }
            const customerData = {
                Customername: req.body.Customername,
                email: req.body.email,
                phone: req.body.phone,
                password: req.body.password,
                address: req.body.address,
                EntryDate: new Date().toUTCString()
            };

            const customer = new CustomerTbl(customerData);
            await customer.save();

            // Send email
            const transporter = nodemailer.createTransport({
                service: 'gmail', // you can use any email service
                auth: {
                    user: 'pateldiyaashokbhai@gmail.com', // replace with your email
                    pass: 'xhckfkkinufidqow'   // replace with your app password
                }
            });

            const mailOptions = {
                from: 'pateldiyaashokbhai@gmail.com',
                to: customerData.email,
                subject: 'Registration Successful',
                text: `Hello ${customerData.Customername},\n\nYou have successfully registered. Now you are eligible to log in.\n\nThank you!`
            };

            transporter.sendMail(mailOptions, (error, info) => {
                if (error) {
                    console.log("Email error: ", error);
                } else {
                    console.log('Email sent: ' + info.response);
                }
            });

            res.status(200).json({
                Message: "Customer registered successfully",
                Data: customer
            });

        } catch (error) {
            console.log(error);
            res.status(500).json({
                Message: "Something went wrong",
                Errors: error
            });
        }
    });
// put img
router.put('/profile/:customerId/image', upload.single('image'), async (req, res) => {
    try {
        const customerId = req.params.customerId;

        // If no file uploaded, return error
        if (!req.file) {
            return res.status(400).json({ message: "No image uploaded" });
        }

        const baseUrl = req.protocol + "://" + req.get('host');
        const imageUrl = baseUrl + "/uploads/" + req.file.filename;

        // Update customer image field
        const updatedCustomer = await CustomerTbl.findByIdAndUpdate(
            customerId,
            { image: imageUrl }, 
            { new: true }
        );

        if (!updatedCustomer) {
            return res.status(404).json({ message: "Customer not found" });
        }

        res.status(200).json({
            message: "Profile image updated successfully",
            data: updatedCustomer
        });
    } catch (error) {
        res.status(500).json({ message: "Error updating profile image", error: error.message });
    }
});


    //  PUT update Customer
    router.put('/:id', async (req, res) => {
        try {
            var id = req.params.id;
            var Customermodel = {
                Customername: req.body.Customername,
                email: req.body.email,
                phone: req.body.phone,
                password:req.body.password,
                address:req.body.address
            };

            await CustomerTbl.findByIdAndUpdate(id, Customermodel, { new: true })
                .then((response) => {
                    if (!response) {
                        return res.status(404).json({ Message: "Admin Not Found" });
                    }
                    res.status(200).json({
                        Message: "Customer Updated Successfully",
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

            await CustomerTbl.findByIdAndDelete(id)
                .then((response) => {
                    if (!response) {
                        return res.status(404).json({ Message: "Admin Not Found" });
                    }
                    res.status(200).json({
                        Message: "Customer Deleted Successfully",
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