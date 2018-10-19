const express = require('express');
const router = express.Router();

const checkAuth = require('../middleware/check-auth');

const StudentController = require('../controllers/student-controller');

// Handles GET requests (Retrieve all Student cards)
router.get('/', checkAuth, StudentController.stu_get_all);

// Handles POST requests (Create a student name card)
router.post('/create', checkAuth, StudentController.stu_create_card);

// Handles GET requests for an student card(Retrieve info from one card)
router.get('/cid/:cardId', StudentController.stu_get_one);

// Handles DELETE requests (Deletes one student card)
router.delete('/:cardId', StudentController.stu_delete_one);

module.exports = router;
