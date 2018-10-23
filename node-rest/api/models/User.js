const mongoose = require('mongoose');
mongoose.set('useCreateIndex', true);
const bcrypt = require('bcrypt-nodejs');

const userSchema = mongoose.Schema({
    _id: mongoose.Schema.Types.ObjectId,
    name: {type: String, required: true},
    username: {type: String, required: true, index: {unique: true, dropDups: true}},
    email: {type: String, required: true, index: {unique: true, dropDups: true}},
    contact: {type: String, required: true},
    role: {type: Number, required: true},
    password: {type: String, required: true},
    friendship: {type: [String], required: true, default: []},
    cards: {type: [String], required: true, default: []}
});

// Create encrypted password
userSchema.pre('save', function (next) {
    var user = this;
    if (this.isModified('password') || this.isNew) {
        bcrypt.genSalt(10, function (err, salt) {
            if (err) {
                return next(err);
            }
            bcrypt.hash(user.password, salt, null, function (err, hash) {
                if (err) {
                    return next(err);
                }
                user.password = hash;
                next();
            });
        });
    } else {
        return next();
    }
});
module.exports = mongoose.model('User', userSchema);
