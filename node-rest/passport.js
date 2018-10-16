const LocalStrategy = require('passport-local').Strategy;
const passport = require('passport');
const User = require('./api/models/User');

passport.use(new LocalStrategy({
    username: 'username',
    password: 'password'
}, function (username, password, cb) {
    return User.findOne({username, password})
            .then(user=>{
                if (!user){
                    return cb(null, false, {message:'Login failed', success: false});
                }
                return cb(null, user, {message: 'Log in successfully'});
    })
            .catch(err=>cb(err));
}));