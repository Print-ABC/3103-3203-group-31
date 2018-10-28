const express = require('express');
const router = express.Router();

const checkAuth = require('../middleware/check-auth');

const UserController = require('../controllers/user-controller');

// Handles POST requests (Register a user)
router.post('/register', UserController.users_register_user);

//Retrieve all namecards of a user, return username, name and organization/school
router.post('/cards', checkAuth, UserController.users_get_cards_info);

// User Login
router.post('/login', UserController.users_login);

// Gets username of a specific user
router.get("/username/:username", UserController.users_get_username);

// Check if card exists for that user, if not then update to array
router.get("/findcards/:uid/:cardtocheck", checkAuth, UserController.users_find_cards);

// Check for 2fa
router.get("/check2fa/:fatoken", UserController.users_2fa);

module.exports = router;
