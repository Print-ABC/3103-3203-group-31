const mongoose = require('mongoose');

const User = require('../models/User');
const Friend = require('../models/Friend');
const config = require('../../config/config');

//GET: Retrieve requests with the given requester uid
exports.friend_get_requests_requester = (req, res, next) => {
  Friend.find( {'requester': req.params.uid}, function (err, docs){
    if(err) {
      console.log(err);
      res.status(500).json( {error: err} );
    }
    res.status(200).json(docs);
  });
}

//GET: Retrieve requests with the given recipient uid
exports.friend_get_requests_recipient = (req, res, next) => {
  Friend.find( {'recipient': req.params.uid}, function (err, docs){
    if(err) {
      console.log(err);
      res.status(500).json( {error: err} );
    }
    res.status(200).json(docs);
  });
}

//POST: Make a new friend request
exports.friend_post_request = (req, res, next) => {
  const request = new Friend({
      _id: new mongoose.Types.ObjectId(),
      requester: req.body.requester,
      recipient: req.body.recipient
    }
  );
  request.save()
        .then(result => {
          console.log(result);
          res.status(201).json({
            message: 'User created successfully',
            success: true});
        })
        .catch(err => {
          console.log(err);
          res.status(201).json({
            error: err,
            message: "Failed to create user",
            success: false
          });
        });
}

//UPDATE: Add uid to list of friends of an existing user
exports.update_friendlist_add = (req, res, next) => {
  User.update(
    { "_id": req.params.uid },
    { $push: {"friendship": req.params.fuid} },
    function (err, docs) {
      if(err) {
        console.log(err);
        res.status(500).json( {error: err} );
      }
      res.status(200).json({
        message: 'Update: New friend added to friendlist'
      });
    }
  );
}

//UPDATE: Remove a uid from list of friends of an existing user
exports.update_friendlist_remove = (req, res, next) => {
  User.update(
    { "_id": req.params.uid },
    { $pull: {"friendship": req.params.fuid} },
    function (err, docs) {
      if(err) {
        console.log(err);
        res.status(500).json( {error: err} );
      }
      res.status(200).json({
        message: 'Update: Friend was removed from friendlist'
      });
    }
  );
}

//DELETE: Delete an existing friend request
exports.friend_delete_request = (req, res, next) => {
    Friend.deleteOne({ recipient: req.params.uid })
        .exec()
        .then(result => {
            res.status(200).json({
                message: 'Friend request deleted'
            });
        })
        .catch(err => {
            console.log(err);
            res.status(500).json({
                error: err
            });
        });
}
