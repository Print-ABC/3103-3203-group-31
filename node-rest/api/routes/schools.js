const express = require('express');
const router = express.Router();

const checkAuth = require('../middleware/check-auth');

const SchController = require('../controllers/school-controller');

// Handles GET requests (Retrieve all Organization cards)
router.get('/', checkAuth, SchController.sch_get_all);

// Handles POST requests (Create an organization name card)
router.post('/', checkAuth, SchController.sch_create_card);

// Handles GET requests for an organization card(Retrieve info from one card)
router.get('/:cardId', SchController.sch_get_one);

// Handles DELETE requests (Deletes one organization card)
router.delete('/:cardId', SchController.sch_delete_one);

module.exports = router;
