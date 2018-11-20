const express = require('express');
const router = express.Router();

const checkAuth = require('../middleware/check-auth');

const FriendController = require('../controllers/friend-controller');

// Handles GET requests (Retrieve all requests with a given requester uid)
router.get('/req/:uid', checkAuth, FriendController.friend_get_requests_requester);

// Handles GET requests (Retrieve all requests with a given recipient uid)
router.get('/recp/:uid', checkAuth, FriendController.friend_get_requests_recipient);

// Handles POST requests (Create a new friend request)
router.post('/:uid', checkAuth, FriendController.friend_post_request);

// Handles POST requests (Adds a new friend to friendlist)
router.post("/add", checkAuth, FriendController.update_friendlist_add);

// Handles POST requests (Removes an existing friend from friendlist)
router.post("/remove",checkAuth, FriendController.update_friendlist_remove);

// Handles DELETE requests (Delete a request with a given recipient uid)
router.delete('/:uid/:id', checkAuth, FriendController.friend_delete_request);

module.exports = router;
