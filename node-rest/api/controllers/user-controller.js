const mongoose = require('mongoose');
const jwt = require('jsonwebtoken');
const bcrypt = require('bcrypt-nodejs');
const nodemailer = require('nodemailer');
const rand = require('secure-random-string');

const User = require('../models/User');
const Organization = require('../models/Organization');
const Student = require('../models/Student');
const config = require('../../config/config');


exports.users_get_cards_info = (req, res) => {
    if (req.body.cards) {
        //Loop through each card that a user owns
        console.log(req.body.cards.length);
        const p1 = Organization.find({ _id: { $in: req.body.cards } }).select("name organization jobTitle contact email ").exec();
        const p2 = Student.find({ _id: { $in: req.body.cards } }).select("name course email contact").exec();
        Promise.all([p1, p2])
            .then(result => {
                console.log(result);
                res.status(200).json({
                    success: true,
                    orgCards: result[0],
                    stuCards:result[1]
                })
            }
            )
            .catch(err => {
                console.log(err);
                res.status(200).json({
                    success: false
                })
            })
    } else {
        res.status(200).json({
            success: false
        })
    }
}
function checkOrgCard(cardId) {
    const promise = Organization.findById(cardId).exec();
    return promise;
}

function checkStudentCard(cardId) {
    const promise = Student.findById(cardId).exec();
    return promise;
}

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
            res.status(200).json(response);
        })
        .catch(err => {
            console.log(err);
            res.status(500).json({
                error: err
            });
        });
}

exports.users_get_username = (req, res, next) => {
    User.findOne({ username: new RegExp('^' + req.params.username + '$', "i") }, function(err, doc) {
      if(err) {
        console.log(err);
        res.status(500).json( {error: err} );
      }
      res.status(200).json({
		  uid: doc._id,
		  name: doc.name,
		  username: doc.username,
		  role: doc.role,
		  email: doc.email
		  });
    });
}

exports.users_get_name = (req, res, next) => {
    User.findById(req.params.uid)
        .select('_id name')
        .exec()
        .then(doc => {
            console.log("From database", doc);
            if (doc) {
                // Success response
                res.status(200).json({
                    _id: doc._id,
                    username: doc.name,
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
            });
        })
        .catch(err => {
            console.log(err);
            if (err.errmsg.includes("duplicate")) {
                // Anything other than 201 will crash the client
                res.status(201).json({
                    message: 'Username/email already exists',
                    success: falsec
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
                    message: 'Login failed',
                    success: false
                });
            }
            bcrypt.compare(req.body.password, user[0].password, (err, result) => {
                if (err) {
                    console.log(err);
                    return res.status(201).json({
                        message: 'Login failed',
                        success: false
                    });
                } else {
                  const transporter = nodemailer.createTransport({
                      service: 'gmail',
                      auth: {
                        user: 'tyrraelizz95@gmail.com',
                        pass: '02021995lol'
                      }
                    });
                    rand({alphanumeric: true, length: 10}, function(error , TwoFA){
                      if (error){
                        console.log(error);
                      }
                      console.log('Generated 2FA:', TwoFA);
                      const mailOptions = {
                        from: 'admin@mydomain.com',
                        // to: user[0].email,
                        to: 'tyrraskye@hotmail.com',
                        subject: '2FA OTP Verificator (Do Not Reply)',
                        text: 'Your OTP verificator is: ' + TwoFA
                      };

                      transporter.sendMail(mailOptions, function(error, info){
                        if (error) {
                          console.log(error);
                        } else {
                          console.log('Email sent: ' + info.response);
                          return res.status(200).json({
                              message: 'Verificator Sent!',
                              success: true
                          });
                        }
                      });
                    });
                }
                // TODO After keyed in correct 2FA
                 console.log(user[0]);
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
						              name: user[0].name,
                          username: user[0].username,
                          friendship: user[0].friendship,
                          cards: user[0].cards,
                          uid: user[0]._id,
                          role: user[0].role,
                          success: true
                      });
                  }) 
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

exports.users_2fa = (req, res, next) => {
  const userInputToken = req.params.TwoFaToken;
  bcrypt.compare(userInputToken, TwoFA, (err, result) => {
      console.log(TwoFA);
      console.log(userInputToken);
      if (err) {
          console.log(err);
          return res.status(201).json({
              messsage: 'Two FA Failed',
              success: false
          });
      }
      if (result) {
        console.log(user[0]);
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
    } else {
        Student.findOne({ uid: uid })
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

exports.users_find_cards = (req, res, next) => {
  const id = req.params.uid;
  const cardToCheck = req.params.cardtocheck;
  console.log(id);
  User.findById(id)
  .select('cards')
  .exec()
  .then(doc => {
      console.log("From database", doc);
      if (doc) {
          // Success response
        //  res.status(200).json({ doc });
          console.log(doc.cards);
          if (doc.cards.indexOf(cardToCheck) > -1) {
            //Card already exist in collection, end.
            console.log("EXIST!");
            res.status(200).json({ message: 'Card already exist in collection', success: true });
          } else {
            //Card does not exist, proceed to add into database
            console.log("NOT EXIST!");
            User.updateOne(
               { "_id": id },
               { $push: { "cards": cardToCheck } },
               function (err, docs) {
                 if(err) {
                   console.log(err);
                   res.status(500).json( {error: err, success: false} );
                 }
                 res.status(200).json({
                   message: 'New card added to collection!',
                   success: true
                 });
               }
            );
          }
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
