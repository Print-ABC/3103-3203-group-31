const express = require('express');
const router = express.Router();
const mongoose = require('mongoose');

const checkAuth = require('../middleware/check-auth');
const Organization = require('../models/Organization');
const User = require('../models/User');
const config = require('../../config/config');

