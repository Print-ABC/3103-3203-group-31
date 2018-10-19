const mongoose = require('mongoose');
const jwt = require('jsonwebtoken');
const bcrypt = require('bcrypt-nodejs');

const User = require('../models/User');
const Organization = require('../models/Organization');
const config = require('../../config/config');

exports.users_get_all = (req, res, next) => {
    User.find()
        .select('_id name username email contact role password friendship cards')
        .exec()
        .then(docs => {
            const response = {
                count: docs.length,
                users: docs.map(doc => {
                    return {
                        _id: doc._id,
                        name: doc.name,
                        username: doc.username,
                        email: doc.email,
                        contact: doc.contact,
                        role: doc.role,
                        password: doc.password,
                        friendship: doc.friendship,
                        cards: doc.cards,
                        request: {
                            type: 'GET',
                            url: 'http://localhost:+' + config.port + '/users/' + doc._id
                        }
                    };
                })
            };
            // May return empty array if no data, uncomment if need a response
            //        if (docs.length >= 0){
            res.status(200).json(response);
            //        } else {
            //            res.status(404).json({
            //               message: 'No entries found' 
            //            });
            //        }
        })
        .catch(err => {
            console.log(err);
            res.status(500).json({
                error: err
            });
        });
}

exports.users_get_username = (req, res, next) => {
    const id = req.params.uid;
    User.findById(id)
        .select('username')
        .exec()
        .then(doc => {
            console.log("From database", doc);
            if (doc) {
                // Success response
                res.status(200).json({
                    username: doc.username,
                    success: true
                });
            } else {
                // ID does not exist
                res.status(200).json({ success: false });
            }
        }).catch(err => {
            console.log(err);
            // Failure response
            res.status(200).json({ success: false });
        });
}

exports.users_register_user = (req, res, next) => {
    const user = new User({
        _id: new mongoose.Types.ObjectId(),
        name: req.body.name,
        username: req.body.username,
        email: req.body.email,
        contact: req.body.contact,
        role: req.body.role,
        password: req.body.password,
        friendship: req.body.friendship,
        cards: req.body.cards
    });
    user.save()
        .then(result => {
            console.log(result);
            // Success response
            res.status(201).json({
                message: 'User created successfully',
                success: true
                //createdUser: {
                //    _id: result._id,
                //    name: result.name,
                //    email: result.email,
                //    contact: result.contact,
                //    role: result.role,
                //    password: result.password,
                //    friendship: result.friendship,
                //    cards: result.cards,
                //    request: {
                //        type: 'GET',
                //        url: 'http://localhost:' + config.port + '/users/' + result._id
                //    }
                //}
            });
        })
        .catch(err => {
            console.log(err);
            if (err.errmsg.includes("duplicate")) {
                // Anything other than 201 will crash the client
                res.status(201).json({
                    message: 'Username/email already exists',
                    success: false
                });
            } else {
                res.status(201).json({
                    error: err,
                    message: "Failed to create user",
                    success: false
                });
            }

        });
}

exports.users_login = (req, res, next) => {
    User.find({ username: req.body.username })
        .exec()
        .then(user => {
            if (user.length < 1) {
                return res.status(201).json({
                    messsage: 'Login failed',
                    success: false
                });
            }
            bcrypt.compare(req.body.password, user[0].password, (err, result) => {
                if (err) {
                    console.log(err);
                    return res.status(201).json({
                        messsage: 'Login failedadsa',
                        success: false
                    });
                }
                if (result) {
                    console.log(result);
                    const token = jwt.sign({
                        username: user[0].username,
                        uid: user[0]._id
                    }, config.secret, {
                            expiresIn: "1h"
                        });
                    // Get card ID of user if available
                    const cardId = getCardIdByUid(user[0].role, user[0]._id, function (cardId) {
                        res.status(200).json({
                            message: 'Login successful',
                            token: token,
                            cardId: cardId,
                            username: user[0].username,
                            friendship: user[0].friendship,
                            cards: user[0].cards,
                            uid: user[0]._id,
                            role: user[0].role,
                            success: true
                        });
                    })
                    return;
                }
                res.status(201).json({
                    message: 'Login failed',
                    success: false
                });
            });
        })
        .catch(err => {
            console.log(err);
            res.status(201).json({
                message: "Login failed",
                success: false,
                error: err
            });
        });
}

exports.users_get_one = (req, res, next) => {
    const id = req.params.uid;
    User.findById(id)
        .select('_id name username email contact role password friendship cards')
        .exec()
        .then(doc => {
            console.log("From database", doc);
            if (doc) {
                // Success response
                res.status(200).json({ doc });
            } else {
                // ID does not exist
                res.status(404).json({ message: 'No valid entry found for provided ID' });
            }
        }).catch(err => {
            console.log(err);
            // Failure response
            res.status(500).json({ error: err });
        });
}

exports.users_delete_one = (req, res, next) => {
    const id = req.params.uid;
    User.deleteOne({ _id: id })
        .exec()
        .then(result => {
            res.status(200).json({
                message: 'User deleted'
            });
        })
        .catch(err => {
            console.log(err);
            res.status(500).json({
                error: err
            });
        });
}

exports.users_update_one = (req, res, next) => {
    const id = req.params.uid;
    const updateOps = {};
    for (const ops of req.body) {
        // Obtain an object of the update operations we want to perform
        updateOps[ops.propName] = ops.value;
    }
    User.update({ _id: id }, { $set: updateOps })
        .exec()
        .then(result => {
            res.status(200).json({
                message: 'User updated',
                request: result
            });
        })
        .catch(err => {
            console.log(err);
            res.status(500).json({
                error: err
            });
        });
}

function getCardIdByUid(role, uid, callback) {
    // If organization
    if (role == 1) {
        Organization.findOne({ uid: uid })
            .select('_id')
            .exec()
            .then(result => {
                if (result) {
                    callback(result._id);
                } else {
                    return callback("none");
                }
            })
            .catch(err => {
                return callback("none");
        })
    }
}