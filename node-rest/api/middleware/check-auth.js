const jwt = require('jsonwebtoken');
const config = require('../../config/config');

module.exports = (req, res, next) => {
    try {
        const token = req.headers.authorization;
        console.log(token);
        console.log(config.secret);
        const decoded = jwt.verify(token, config.secret);
        req.userData = decoded; 
        next();
    } catch (error) {
        console.log(error);
        return res.status(201).json({
            message: 'Auth Failed',
            success: false
        });
    }
};