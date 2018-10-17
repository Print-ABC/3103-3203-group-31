const express = require('express');
const router = express.Router();

const checkAuth = require('../middleware/check-auth');

const UserController = require('../controllers/user-controller');

// Handles GET requests (Retrieve all Users)
router.get('/', UserController.users_get_all);

// Handles POST requests (Register a user)
router.post('/register', UserController.users_register_user);

// User Login
router.post('/login', UserController.users_login);

// Handles GET requests for specific users (Retrieve info from one user)
router.get('/:uid', UserController.users_get_one);

// Handles DELETE requests (Deletes one user)
router.delete('/:uid', checkAuth, UserController.users_delete_one);

// Handles PATCH requests (Update)
router.patch("/:uid", checkAuth, UserController.users_update_one);

module.exports = router;