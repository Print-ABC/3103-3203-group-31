const mongoose = require('mongoose');

const User = require('../models/User');
const Student = require('../models/Student');
const config = require('../../config/config');

exports.stu_create_card = (req, res, next) => {
    // Check if uid exists
    User.findById(req.body.uid)
        .then(user => {
            if (!user) {
                return res.status(400).json({});
            }
            const student = new Student({
                _id: new mongoose.Types.ObjectId(),
                uid: req.body.uid,
                name: req.body.name,
                course: req.body.course,
                email: req.body.email,
                contact: req.body.contact,
            });
            return student.save();
        })
        .then(result => {
            User.updateOne(
                { "_id": req.body.uid },
                { $push: { "cards": result._id } },
                function (err, docs) {
                    if (err) {
                        console.log(err);
                        return res.status(400).json({});
                    }
                    return res.status(201).json({});
                })

        })
        .catch(err => {
            if (err.errmsg.includes("duplicate")) {
                return res.status(406).json({})
            } else {
                return res.status(400).json({});
            }


        });

}

exports.stu_get_all = (req, res, next) => {
    Student.find()
        .select('_id uid name email contact course')
        .exec()
        .then(docs => {
            res.status(200).json({
                count: docs.length,
                stu_cards: docs.map(doc => {
                    return {
                        _id: doc._id,
                        name: doc.name,
                        uid: doc.ud,
                        course: doc.course,
                        email: doc.email,
                        contact: doc.contact,
                        request: {
                            type: 'GET',
                            url: 'http://localhost:' + config.port + '/students/' + doc._id
                        }
                    };
                })
            });
        }
        )
        .catch(err => {
            res.status(500).json({
                error: err
            });
        });
}