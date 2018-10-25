const mongoose = require('mongoose');

const User = require('../models/User');
const Organization = require('../models/Organization');
const config = require('../../config/config');

exports.org_create_card = (req, res, next) => {
    // Check if uid exists
    User.findById(req.body.uid)
        .then(user => {
            if (!user) {
                return res.status(400).json({});
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
            User.update(
              { "_id": req.body.uid },
              { $push: {"cards": result._id} },
              function(err, docs) {
                if (err){
                  console.log(err);
                  return res.status(400).json({});
                }
                return res.status(201).json({
                    cardId: result._id
                });
              }
            )
        })
        .catch(err => {
            console.log(err);
            if (err.errmsg.includes("duplicate")) {
                return res.status(406).json({});
            }
            return res.status(400).json({});
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
                message: 'From collections',
                success: true,
                organization: organization.organization,
                _id: organization._id,
                uid: organization.uid,
                name: organization.name,
                email: organization.email,
                contact: organization.contact,
                jobTitle: organization.jobTitle
            });
        })
        .catch(err => {
            res.status(500).json({
                success : false,
                message: 'error retrieving from collections',
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
