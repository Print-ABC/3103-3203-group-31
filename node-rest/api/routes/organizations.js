const express = require('express');
const router = express.Router();

const checkAuth = require('../middleware/check-auth');

const OrgController = require('../controllers/organization-controller');

// Handles GET requests (Retrieve all Organization cards)
router.get('/', checkAuth, OrgController.org_get_all);

// Handles POST requests (Create an organization name card)
router.post('/create', checkAuth, OrgController.org_create_card);

module.exports = router;
