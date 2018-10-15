const express = require('express');
const router = express.Router();
const mongoose = require('mongoose');

const User = require('../models/User');

'use strict';
const crypto = require('crypto');

// Handles GET requests (Retrieve all Users)
router.get('/', (req, res, next) => {
    User.find()
            .select('_id name username email contact role password salt friendship cards')
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
                            salt: doc.salt,
                            friendship: doc.friendship,
                            cards: doc.cards,
                            request: {
                                type: 'GET',
                                url: 'http://localhost:3000/users/' + doc._id
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
});

// Handles POST requests (Register a user)
router.post('/register', (req, res, next) => {
    const salt = getSalt(64);
    const passwordHash = sha512(req.body.password, salt).passwordHash;
    const user = new User({
        _id: new mongoose.Types.ObjectId(),
        name: req.body.name,
        username: req.body.username,
        email: req.body.email,
        contact: req.body.contact,
        role: req.body.role,
        salt: salt,
        password: passwordHash,
        friendship: req.body.friendship,
        cards: req.body.cards
    });
    user.save()
            .then(result => {
                console.log(result);
                // Success response
                res.status(201).json({
                    message: 'User created successfully',
                    success: true,
                    createdUser: {
                        _id: result._id,
                        name: result.name,
                        email: result.email,
                        contact: result.contact,
                        role: result.role,
                        password: result.password,
                        salt: result.salt,
                        friendship: result.friendship,
                        cards: result.cards,
                        request: {
                            type: 'GET',
                            url: 'http://localhost:3000/users/' + result._id
                        }
                    }
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
                        success:false
                    });
                }

            });
});

// Handles GET requests for specific users (Retrieve info from one user)
router.get('/:uid', (req, res, next) => {
    const id = req.params.uid;
    User.findById(id)
            .select('_id name username email contact role password salt friendship cards')
            .exec()
            .then(doc => {
                console.log("From database", doc);
                if (doc) {
                    // Success response
                    res.status(200).json({doc});
                } else {
                    // ID does not exist
                    res.status(404).json({message: 'No valid entry found for provided ID'});
                }
            }).catch(err => {
        console.log(err);
        // Failure response
        res.status(500).json({error: err});
    });
});

// Handles DELETE requests (Deletes one user)
router.delete('/:uid', (req, res, next) => {
    const id = req.params.uid;
    User.deleteOne({_id: id})
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
});

// Handles PATCH requests (Update)
router.patch("/:uid", (req, res, next) => {
    const id = req.params.uid;
    const updateOps = {};
    for (const ops of req.body) {
        // Obtain an object of the update operations we want to perform
        updateOps[ops.propName] = ops.value;
    }
    User.update({_id: id}, {$set: updateOps})
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
});

/**
 * generates random string of characters i.e salt
 * @function
 * @param {number} length - Length of the random string.
 */
var getSalt = function (length) {
    return crypto.randomBytes(Math.ceil(length / 2))
            .toString('hex') /** convert to hexadecimal format */
            .slice(0, length);   /** return required number of characters */
};

/**
 * hash password with sha512.
 * @function
 * @param {string} password - List of required fields.
 * @param {string} salt - Data to be validated.
 */
var sha512 = function (password, salt) {
    var hash = crypto.createHmac('sha512', salt); /** Hashing algorithm sha512 */
    hash.update(password);
    var value = hash.digest('hex');
    return {
        salt: salt,
        passwordHash: value
    };
};


module.exports = router;