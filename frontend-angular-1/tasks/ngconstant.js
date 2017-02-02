var grunt = require('grunt');

module.exports = {
  dist: {
    options: {
      name: 'app.constants',
      dest: 'public/app/app.constants.js',
      values: {
        CONST: grunt.file.readJSON('.env.hmg')
      }
    }
  },
  server: {
    options: {
      name: 'app.constants',
      dest: 'public/app/app.constants.js',
      values: {
        CONST: grunt.file.readJSON('.env.dev')
      }
    }
  }
};
