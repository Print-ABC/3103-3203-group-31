const express = require('express');
const router = express.Router();

const checkAuth = require('../middleware/check-auth');

const FriendController = require('../controllers/friend-controller');

// Handles GET requests (Retrieve all requests with a given requester uid)
router.get('/req/:uid', checkAuth, FriendController.friend_get_requests_requester);

// Handles GET requests (Retrieve all requests with a given recipient uid)
router.get('/recp/:uid', checkAuth, FriendController.friend_get_requests_recipient);

// Handles POST requests (Create a new friend request)
router.post('/', checkAuth, FriendController.friend_post_request);

// Handles PUT requests (Adds a new friend to friendlist)
router.post("/:uid/add/:friend", checkAuth, FriendController.update_friendlist_add);

// Handles PUT requests (Removes an existing friend from friendlist)
router.post("/:uid/del/:friend", checkAuth, FriendController.update_friendlist_remove);

// Handles DELETE requests (Delete a request with a given recipient uid)
router.post('/:id', checkAuth, FriendController.friend_delete_request);

module.exports = router;
