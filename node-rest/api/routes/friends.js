const express = require('express');
const router = express.Router();
const mongoose = require('mongoose');

const checkAuth = require('../middleware/check-auth');
const Friend = require('../models/Friend');
const User = require('../models/User');
const config = require('../../config/config');

const FriendController = require('../controllers/friend-controller');


//TODO: friends API calls
// Retrieve all friends of a specified user
router.get('/:uid', FriendController.users_get_friends);

module.exports = router;
