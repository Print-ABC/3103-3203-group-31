const express = require('express');
const router = express.Router();
const mongoose = require('mongoose');

const checkAuth = require('../middleware/check-auth');
const Friend = require('../models/Friend');
const User = require('../models/User');
const config = require('../../config/config');

const FriendController = require('../controllers/friend-controller');

// Handles GET requests (Retrieve all requests with a given requester uid)
router.get('/req/:uid', FriendController.friend_get_requests_requester);

// Handles GET requests (Retrieve all requests with a given recipient uid)
router.get('/recp/:uid', FriendController.friend_get_requests_recipient);

// Handles POST requests (Create a new friend request)
router.post('/', FriendController.friend_post_request);

// Handles DELETE requests (Delete a request with a given recipient uid)
router.delete('/:uid', FriendController.friend_delete_request);

module.exports = router;
