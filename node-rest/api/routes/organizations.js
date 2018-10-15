const express = require('express');
const router = express.Router();
const mongoose = require('mongoose');

const Organization = require('../models/Organization');
const User = require('../models/User');

// Handles GET requests (Retrieve all Organization cards)
router.get('/', (req, res, next) => {
    Organization.find()
            .select('_id uid name organization email contact jobTitle')
            .exec()
            .then(docs => {
                res.status(200).json({
                    count: docs.length,
                    org_cards: docs.map(doc => {
                        return {
                            _id: doc._id,
                            name: doc.name,
                            uid: doc.uid,
                            organization: doc.organization,
                            email: doc.email,
                            contact: doc.contact,
                            jobTitle: doc.jobTitle,
                            request: {
                                type: 'GET',
                                url: 'http://localhost:3000/organizations/' + doc._id
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
});
// Handles POST requests (Create an organization name card)
router.post('/', (req, res, next) => {
    // Check if uid exists
    User.findById(req.body.uid)
            .then(user => {
                if (!user){
                    return res.status(404).json({
                        message: "User does not exists"
                    });
                }
                const organization = new Organization({
                    _id: new mongoose.Types.ObjectId(),
                    uid: req.body.uid,
                    name: req.body.name,
                    organization: req.body.organization,
                    email: req.body.email,
                    contact: req.body.contact,
                    jobTitle: req.body.jobTitle
                });
                return organization.save();
            })
            .then(result => {
                console.log(result);
                res.status(201).json({
                    message: "Name card successfully created",
                    result: result
                });
            })
            .catch(err => {
                console.log(err);
                res.status(500).json({
                    error: err
                });
            });

});

// Handles GET requests for an organization card(Retrieve info from one card)
router.get('/:cardId', (req, res, next) => {
    Organization.findById(req.params.cardId)
            .select('_id uid name organization email contact jobTitle')
            .exec()
            .then(organization=>{
                if(!organization){
                    return res.status(404).json({
                       message: 'Name card not found'
                    });
                }
                res.status(200).json({
                   organization: organization
                });
    })
            .catch(err=>{
                res.status(500).json({
                    error: err
                });
    });
});

// Handles DELETE requests (Deletes one organization card)
router.delete('/:cardId', (req, res, next) => {
    const id = req.params.cardId;
    Organization.deleteOne({_id: id})
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
});
module.exports = router;
