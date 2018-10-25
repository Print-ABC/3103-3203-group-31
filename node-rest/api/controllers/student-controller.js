const mongoose = require('mongoose');

const User = require('../models/User');
const Student = require('../models/Student');
const config = require('../../config/config');

exports.stu_create_card = (req, res, next) => {
    // Check if uid exists
    User.findById(req.body.uid)
        .then(user => {
            if (!user) {
                return res.status(201).json({
                    message: "User does not exists"
                });
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
            console.log(result);
            User.update(
                { "_id": req.body.uid },
                { $push: { "cards": result._id } },
                function (err, docs) {
                    if (err) {
                        console.log(err);
                        res.status(400);
                    }
                    return res.status(201);
                })
            
        })
        .catch(err => {
            console.log(err);
            if (err.errmsg.includes("duplicate")) {
                return res.status(406);
            } else {
                return res.status(400);
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

exports.stu_get_one = (req, res, next) => {
    Student.findById(req.params.cardId)
        .select('_id uid name course email contact')
        .exec()
        .then(student => {
            if (!student) {
                return res.status(404).json({
                    message: 'Name card not found'
                });
            }
            res.status(200).json({
                student: student
            });
        })
        .catch(err => {
            res.status(500).json({
                error: err
            });
        });
}

exports.stu_delete_one = (req, res, next) => {
    const id = req.params.cardId;
    Student.deleteOne({ _id: id })
        .exec()
        .then(result => {
            res.status(200).json({
                message: 'Name card deleted'
            });
        })
        .catch(err => {
            console.log(err);
            res.status(500).json({
                error: err
            });
        });
}