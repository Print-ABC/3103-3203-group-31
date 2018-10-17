const mongoose = require('mongoose');

const User = require('../models/User');
const Friend = require('../models/Friend');
const config = require('../../config/config');

//GET: Retrieve list of friends [username, name, school] when given a uid
exports.users_get_friends = (req, res, next) => {
  const id = req.params.uid;
  Friend.find( {$or: [{ requester: id, status: 3 }, { recipient: id, status: 3 }]})
        .populate('requester', 'name username')
        .populate('recipient', 'name username')
        .exec()
        .then(docs => {
          const response = {
            count: docs.length,
            friends: docs.map(doc => {
              return {
                _id: doc._id,
                name: doc.name,
                username: doc.username
              }
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

//TODO: Friends services
exports.friend_get_friends = (req, res, next) => {
  // Friend.findById()
}
