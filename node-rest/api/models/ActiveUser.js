const mongoose = require('mongoose');

const activeUserSchema = mongoose.Schema({
    uid: { type: mongoose.Schema.Types.ObjectId, ref: 'User', index: {unique: true, dropDups: true} },
    token: {type: String, required: true, index: {unique: true, dropDups: true}},
    createdAt : {type: Date, default: Date.now}
});
module.exports = mongoose.model('ActiveUser', activeUserSchema)
