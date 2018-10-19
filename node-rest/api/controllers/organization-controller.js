const mongoose = require('mongoose');

const User = require('../models/User');
const Organization = require('../models/Organization');
const config = require('../../config/config');

exports.org_create_card = (req, res, next) => {
    // Check if uid exists
    User.findById(req.body.uid)
        .then(user => {
            if (!user) {
                return res.status(201).json({
                    message: "User does not exists",
                    success: false
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
            return res.status(201).json({
                message: "Name card successfully created",
                cardId: result._id,
                success: true
            });
        })
        .catch(err => {
            console.log(err);
            if (err.errmsg.includes("duplicate")) {
                return res.status(201).json({
                    success: false,
                    message: "Name card already exists"
                });
            }            
            return res.status(201).json({
                success: false,
                message: "Failed to create name card"
            });
        });

}

exports.org_get_all = (req, res, next) => {
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
                            url: 'http://localhost:' + config.port + '/organizations/' + doc._id
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

exports.org_get_one = (req, res, next) => {
    Organization.findById(req.params.cardId)
        .select('_id uid name organization email contact jobTitle')
        .exec()
        .then(organization => {
            if (!organization) {
                return res.status(404).json({
                    message: 'Name card not found'
                });
            }
            res.status(200).json({
                organization: organization
            });
        })
        .catch(err => {
            res.status(500).json({
                error: err
            });
        });
}

exports.org_delete_one = (req, res, next) => {
    const id = req.params.cardId;
    Organization.deleteOne({ _id: id })
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