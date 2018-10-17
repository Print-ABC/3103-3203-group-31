const express = require('express');
const morgan = require('morgan');
const bodyParser = require('body-parser');
const mongoose = require('mongoose');
const passport = require('passport');
const config = require('./config/config');
require('./passport');

// Init App
const app = express();

// Append headers to response
app.use((req, res, next) => {
    res.header('Access-Control-Allow-Origin', '*');
    res.header('Access-Control-Allow-Headers',
            'Origin, X-Requested-With, Content-Type, Accept, Authorization');

    if (req.method === 'OPTIONS') {
        res.header('Access-Control-Allow-Methods', 'PUT,POST,PATCH,DELETE,GET');
        return res.status(200).json({});
    }
    next();
});

//Init passport
app.use(passport.initialize());

const usersRoutes = require('./api/routes/users');
const organizationsRoutes = require('./api/routes/organizations');
const schoolsRoutes = require('./api/routes/schools');
const friendsRoutes = require('./api/routes/friends');

// DB connection
mongoose.connect('mongodb://xjustus:' + process.env.MONGO_ATLAS_PW +
        '@ncshare-shard-00-00-ng4qy.mongodb.net:27017,ncshare-shard-00-01-ng4qy.mongodb.net:27017,ncshare-shard-00-02-ng4qy.mongodb.net:27017/test?ssl=true&replicaSet=ncshare-shard-0&authSource=admin&retryWrites=true',
        {
            useNewUrlParser: true
        });

app.use(morgan('dev'));
app.use(bodyParser.urlencoded({extended: false}));
app.use(bodyParser.json());

// Forwards requests with different endpoints to different files
app.use('/users', usersRoutes);
app.use('/organizations', organizationsRoutes);
app.use('/schools', schoolsRoutes);
// TODO: uncomment and implement friends
//app.use('/friends', friendsRoutes);

// Requests error handling
app.use((req, res, next) => {
    const error = new Error('Not found');
    error.status = 404;
    next(error);
});

app.use((error, req, res, next) => {
    res.status(error.status || 500);
    res.json({
        error: {
            message: error.message,
            sucess: false
        }
    });
});

module.exports = app;

