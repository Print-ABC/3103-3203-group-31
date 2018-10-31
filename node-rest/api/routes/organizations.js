const express = require('express');
const router = express.Router();

const checkAuth = require('../middleware/check-auth');

const OrgController = require('../controllers/organization-controller');

// Handles GET requests (Retrieve all Organization cards)
router.get('/', checkAuth, OrgController.org_get_all);

// Handles POST requests (Create an organization name card)
router.post('/create', checkAuth, OrgController.org_create_card);

// Handles GET requests for an organization card(Retrieve info from one card)
router.get('/:cardId', checkAuth, OrgController.org_get_one);

module.exports = router;
