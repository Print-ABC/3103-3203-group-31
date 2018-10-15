const mongoose = require('mongoose');
mongoose.set('useCreateIndex', true);

const userSchema = mongoose.Schema({
    _id: mongoose.Schema.Types.ObjectId,
    name: {type: String, required: true},
    username: {type: String, required: true, index: {unique: true, dropDups: true}},
    email: {type: String, required: true, index: {unique: true, dropDups: true}},
    contact: {type: String, required: true},
    role: {type: Number, required: true},
    password: {type: String, required: true},
    salt: {type: String, required: true},
    friendship: {type: [String], required: true, default: []},
    cards: {type: [String], required: true, default: []}
});
module.exports = mongoose.model('User', userSchema);

