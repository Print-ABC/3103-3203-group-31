const express = require('express');
const router = express.Router();

const checkAuth = require('../middleware/check-auth');

const StudentController = require('../controllers/student-controller');

// Handles GET requests (Retrieve all Student cards)
router.get('/', checkAuth, StudentController.stu_get_all);

// Handles POST requests (Create a student name card)
router.post('/create', checkAuth, StudentController.stu_create_card);

module.exports = router;
