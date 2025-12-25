var express = require('express');
var router = express.Router();
const mongoose = require('mongoose');
const BookingTbl = require('../models/Booking');
const path = require('path');
const nodemailer = require('nodemailer');
/* GET users listing. */

router.get('/', async (req, res) => {
    try {
        const bookings = await BookingTbl.find();
        res.status(200).json({ Message: "All bookings", List: bookings });
    } catch (error) {
        res.status(500).json({ Message: "Something went wrong", Errors: error.message });
    }
});
// In your Booking router
router.get('/booking/:shopId', async (req, res) => {
    try {
        const shopId = req.params.shopId; 
        const bookings = await BookingTbl.aggregate([
              { $match: { ShopId: new mongoose.Types.ObjectId(shopId) }}, 
            // Join Customer
            {
                $lookup: {
                    from: "customers",          
                    localField: "CustomerId",  
                    foreignField: "_id",       
                    as: "CustomerData"
                }
            },
            { $unwind: { path: "$CustomerData" } },
            {
                $lookup: {
                    from: "shopowners",
                    localField: "ShopId",
                    foreignField: "_id",
                    as: "ShopData"
                }
            },
            { $unwind: { path: "$ShopData"} },

            // Join Service
            {
                $lookup: {
                    from: "servicetypes",
                    localField: "ServiceId",
                    foreignField: "_id",
                    as: "ServiceData"
                }
            },
            { $unwind: { path: "$ServiceData" } },

            // Join Sub Service
            {
                $lookup: {
                    from: "servicesubtypes",
                    localField: "ServiceSubID",
                    foreignField: "_id",
                    as: "SubServiceData"
                }
            },
            { $unwind: { path: "$SubServiceData" } },

            // Project only required fields
            {
                $project: {
                    _id: 1,
                    DateBook: 1,
                    TimeBook: 1,
                    status: 1,
                    Title: 1, 
                    Price: 1,  
                    "CustomerName": "$CustomerData.Customername",
                    "CustomerPhone": "$CustomerData.phone",
                     "CustomerImg": "$CustomerData.image",  
                    "ShopName": "$ShopData.shopName",
                    "ServiceName": "$ServiceData.ServiceName",
                    "SubServiceName": "$SubServiceData.SubServiceName"
                }
            }
        ]);

        res.status(200).json({ Message: "Get Data.", List: bookings });

    } catch (error) {
        res.status(500).json({ Message: "Something went wrong.", Errors: error.message });
    }
});
// Get revenue summary for all shops (Admin Side)
router.get('/revenue/all', async (req, res) => {
  try {
    // 1️⃣ Get completed bookings with joined shop data
    const bookings = await BookingTbl.aggregate([
      { $match: { status: "completed" } },
      {
        $lookup: {
          from: "shopowners",
          localField: "ShopId",
          foreignField: "_id",
          as: "ShopData"
        }
      },
      { $unwind: "$ShopData" },
      {
        $project: {
          ShopId: 1,
          Price: 1,
          DateBook: 1,
          shopName: "$ShopData.shopName",
          ownerName: "$ShopData.Ownername",
            shopImg: "$ShopData.shopPhoto"
        }
      }
    ]);

    // 2️⃣ Get current date and month (dd/MM/yyyy)
    const today = new Date();
    const dd = String(today.getDate()).padStart(2, "0");
    const mm = String(today.getMonth() + 1).padStart(2, "0");
    const yyyy = today.getFullYear();
    const currentDate = `${dd}/${mm}/${yyyy}`;
    const currentMonth = `${mm}/${yyyy}`;

    // 3️⃣ Calculate revenues
    const revenueMap = {};

    bookings.forEach(b => {
      const shopId = b.ShopId.toString();

      if (!revenueMap[shopId]) {
        revenueMap[shopId] = {
          shopId,
          shopName: b.shopName,
          ownerName: b.ownerName || "Unknown",
             shopImg: b.shopImg || null, 
          todayRevenue: 0,
          monthRevenue: 0,
          totalRevenue: 0
        };
      }

      const price = parseFloat(b.Price) || 0;
      revenueMap[shopId].totalRevenue += price;

      if (b.DateBook === currentDate) {
        revenueMap[shopId].todayRevenue += price;
      }

      if (b.DateBook && b.DateBook.endsWith(currentMonth)) {
        revenueMap[shopId].monthRevenue += price;
      }
    });

    // 4️⃣ Sort and send response
    const result = Object.values(revenueMap).sort((a, b) => b.totalRevenue - a.totalRevenue);

    res.status(200).json({
      Message: "All Shops Revenue Summary",
      List: result
    });

  } catch (error) {
    console.error("Revenue Error:", error);
    res.status(500).json({
      Message: "Something went wrong",
      Errors: error.message
    });
  }
});
router.get('/revenue/monthly-report', async (req, res) => {
  try {
    const monthlyReport = await BookingTbl.aggregate([
      { $match: { status: "completed" } },
      {
        $lookup: {
          from: "shopowners",
          localField: "ShopId",
          foreignField: "_id",
          as: "ShopData"
        }
      },
      { $unwind: "$ShopData" },
      {
        $lookup: {
          from: "customers",
          localField: "CustomerId",
          foreignField: "_id",
          as: "CustomerData"
        }
      },
      { $unwind: "$CustomerData" },

      // Convert Price to number
      { $addFields: { priceNum: { $toDouble: "$Price" } } },

      // Split date string into parts
      { $addFields: { dateParts: { $split: ["$DateBook", "/"] } } },
      { $addFields: { monthYear: { $concat: [ { $arrayElemAt: ["$dateParts", 1] }, "/", { $arrayElemAt: ["$dateParts", 2] } ] } } },

      {
        $group: {
          _id: { shopId: "$ShopId", monthYear: "$monthYear" },
          shopName: { $first: "$ShopData.shopName" },
          ownerName: { $first: "$ShopData.Ownername" },
          shopImg: { $first: "$ShopData.shopPhoto" }, 
          totalRevenue: { $sum: "$priceNum" },
          totalServices: { $sum: 1 },
          uniqueCustomers: { $addToSet: "$CustomerId" }
        }
      },
      { $addFields: { totalCustomers: { $size: "$uniqueCustomers" } } },
      { $sort: { "_id.monthYear": 1, "_id.shopId": 1 } }
    ]);

    res.status(200).json({ Message: "Monthly report per shop", List: monthlyReport });
  } catch (error) {
    console.error(error);
    res.status(500).json({ Message: "Something went wrong", Errors: error.message });
  }
});





router.get('/id/:id', async (req, res) => {
    try {
        var id = req.params.id;
        await BookingTbl.findById(id)
            .then((response) => {
                if (!response) {
                    return res.status(404).json({ Message: "Booking Not Found" });
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
// Get completed bookings for a specific customer
router.get('/history/:customerId', async (req, res) => {
  try {
    const customerId = req.params.customerId;

    const bookings = await BookingTbl.aggregate([
      { $match: { CustomerId: new mongoose.Types.ObjectId(customerId)} },
      // Join Shop
      {
        $lookup: {
          from: "shopowners",
          localField: "ShopId",
          foreignField: "_id",
          as: "ShopData"
        }
      },
      { $unwind: "$ShopData" },

      // Join Service
      {
        $lookup: {
          from: "servicetypes",
          localField: "ServiceId",
          foreignField: "_id",
          as: "ServiceData"
        }
      },
      { $unwind: "$ServiceData" },

      // Join Sub Service
      {
        $lookup: {
          from: "servicesubtypes",
          localField: "ServiceSubID",
          foreignField: "_id",
          as: "SubServiceData"
        }
      },
      { $unwind: "$SubServiceData" },

      {
        $project: {
          _id: 1,
          ShopId: 1,
          DateBook: 1,
          TimeBook: 1,
          Title: 1,
          status: 1,
          Price: 1,
          "ShopName": "$ShopData.shopName",
          "ServiceName": "$ServiceData.ServiceName",
          "SubServiceName": "$SubServiceData.SubServiceName"
          
        }
      }
    ]);

    res.status(200).json({ Message: "Completed bookings", List: bookings });
  } catch (error) {
    res.status(500).json({ Message: "Something went wrong", Errors: error.message });
  }
});

// Get all unique customers for a specific shop (Shopowner side)
router.get('/customers/:shopId', async (req, res) => {
  try {
    const shopId = req.params.shopId;

    const customers = await BookingTbl.aggregate([
      { 
        $match: { ShopId: new mongoose.Types.ObjectId(shopId),
          status: "completed"
         } 
      },
      {
        $group: {
          _id: "$CustomerId",
          totalBookings: { $sum: 1 } 
        }
      },
      {
        $lookup: {
          from: "customers",
          localField: "_id",
          foreignField: "_id",
          as: "CustomerData"
        }
      },
      { $unwind: "$CustomerData" },
      {
        $project: {
          _id: 0,
          CustomerId: "$CustomerData._id",
          CustomerName: "$CustomerData.Customername",
          CustomerPhone: "$CustomerData.phone",
          CustomerImg: "$CustomerData.image",
           CustomerAddress: "$CustomerData.address",
          totalBookings: 1
        }
      }
    ]);

    if (customers.length === 0) {
       return res.status(200).json({ Message: "No customers found for this shop.", List: [] });
    }

    res.status(200).json({
      Message: "All customers who booked this shop",
      List: customers
    });
  } catch (error) {
    res.status(500).json({
      Message: "Something went wrong",
      Errors: error.message
    });
  }
});
router.post('/Book', async (req, res, next) => {
    try {
        var Bookmodel = {
            CustomerId:req.body.CustomerId,
            ShopId:req.body.ShopId,
            ServiceId:req.body.ServiceId,
            ServiceSubID:req.body.ServiceSubID,
            DateBook:req.body.DateBook,
            TimeBook:req.body.TimeBook,
            status:req.body.status,
            Title: req.body.Title,
            Price: req.body.Price,   
            EntryDate: new Date().toUTCString()
        };

        const Data = new BookingTbl(Bookmodel);

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
router.put("/approved/:id", async (req, res) => {
  try {
    const bookingId = req.params.id;
    const { status } = req.body;

    if (!status) {
      return res.status(400).json({ Message: "Status is required" });
    }


    const updatedBooking = await BookingTbl.findByIdAndUpdate(
      bookingId,
      { status },
      { new: true }
    );

    if (!updatedBooking) {
      return res.status(404).json({ Message: "Booking not found" });
    }

    const bookingData = await BookingTbl.aggregate([
      { $match: { _id: new mongoose.Types.ObjectId(bookingId) } },
      {
        $lookup: {
          from: "customers",
          localField: "CustomerId",
          foreignField: "_id",
          as: "CustomerData"
        }
      },
      { $unwind: "$CustomerData" },
      {
        $project: {
          _id: 1,
          status: 1,
          DateBook: 1,
          TimeBook: 1,
          Title: 1,
          "CustomerName": "$CustomerData.Customername",
          "CustomerEmail": "$CustomerData.email"
        }
      },{ $limit: 1 }
    ]);

    const result = bookingData[0];

 // Setup mail transporter
    const transporter = nodemailer.createTransport({
      service: "gmail",
      auth: {
        user: "pateldiyaashokbhai@gmail.com",
        pass: "xhckfkkinufidqow" // your app password
      }
    });

    // Prepare email content based on status
    let subject = "";
    let message = "";


   if (status === "approved") {
      subject = "Appointment Approved";
      message = `Hello ${result.CustomerName},\n\nYour appointment for "${result.Title}" on ${result.DateBook} at ${result.TimeBook} has been approved.\n\nThank you for choosing our salon!`;
    } else if (status === "rejected") {
      subject = "Appointment Rejected";
      message = `Hello ${result.CustomerName},\n\nWe're sorry to inform you that your appointment for "${result.Title}" on ${result.DateBook} at ${result.TimeBook} has been rejected.\n\nPlease contact the salon for further details.`;
    }else if (status === "completed") {
    subject = "Appointment Completed - Feedback Requested";
    message = `Hello ${result.CustomerName},\n\nYour appointment for "${result.Title}" on ${result.DateBook} at ${result.TimeBook} has been completed.\n\nWe’d love your feedback! Please let us know how your experience was.`;
}

    // Send email if applicable
    if (result?.CustomerEmail && (status === "approved" || status === "rejected"|| status === "completed")) {
      await transporter.sendMail({
        from: "pateldiyaashokbhai@gmail.com",
        to: result.CustomerEmail,
        subject: subject,
        text: message
      });
    }

    res.status(200).json({
      Message: `Status updated to ${status}`,
      Data: result || updatedBooking
    });

  } catch (error) {
    console.error(error);
    res.status(500).json({
      Message: "Failed to update status",
      Errors: error.toString()
    });
  }
});
router.put('/:id', async (req, res) => {
    try {
        var id = req.params.id;
        var Bookmodel = {
             CustomerId:req.body.CustomerId,
            ShopId:req.body.ShopId,
            ServiceId:req.body.ServiceId,
            ServiceSubID:req.body.ServiceSubID,
            DateBook:req.body.DateBook,
            TimeBook:req.body.TimeBook,
             status:req.body.status,
        };

        await BookingTbl.findByIdAndUpdate(id, Bookmodel, { new: true })
            .then((response) => {
                if (!response) {
                    return res.status(404).json({ Message: "Admin Not Found" });
                }
                res.status(200).json({
                    Message: "Booking Updated Successfully",
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

        await BookingTbl.findByIdAndDelete(id)
            .then((response) => {
                if (!response) {
                    return res.status(404).json({ Message: "Admin Not Found" });
                }
                res.status(200).json({
                    Message: "Booking Deleted Successfully",
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
