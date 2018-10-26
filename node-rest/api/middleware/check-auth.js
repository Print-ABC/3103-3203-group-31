const jwt = require('jsonwebtoken');
const config = require('../../config/config');

module.exports = (req, res, next) => {
    try {
        //TODO: check if token exists in db, return a message to logout user
        const token = req.headers.authorization;
        const decoded = jwt.verify(token, config.secret);
        req.userData = decoded; 
        next();
    } catch (error) {
        console.log(error);
        return res.status(403).json({
            message: 'Auth Failed',
            success: false
        });
    }
};