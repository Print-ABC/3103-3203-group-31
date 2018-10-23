const express = require('express');
const router = express.Router();

const checkAuth = require('../middleware/check-auth');

const UserController = require('../controllers/user-controller');

// Handles GET requests (Retrieve all Users)
router.get('/', UserController.users_get_all);

// Handles POST requests (Register a user)
router.post('/register', UserController.users_register_user);

//Retrieve all namecards of a user, return username, name and organization/school
router.post('/cards', checkAuth, UserController.users_get_cards_info);

// User Login
router.post('/login', UserController.users_login);

// Handles GET requests for specific users (Retrieve info from one user)
router.get('/:uid', UserController.users_get_one);

// Handles DELETE requests (Deletes one user)
router.delete('/:uid', checkAuth, UserController.users_delete_one);

// Handles PATCH requests (Update)
router.patch("/:uid", checkAuth, UserController.users_update_one);

// Gets username of a specific user
router.get("/username/:uid", checkAuth, UserController.users_get_username);

// Gets name of a specific user
router.get('/name/:uid', UserController.users_get_name);

// Check if card exists for that user, if not then update to array
router.get("/findcards/:uid/:cardtocheck", UserController.users_find_cards);



module.exports = router;
