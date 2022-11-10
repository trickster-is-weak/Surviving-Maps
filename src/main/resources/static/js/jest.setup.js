const matchers = require('jest-extended');
const $ = require("jquery");
const multiselect = import("bootstrap-multiselect");
// const bootstrap = import("bootstrap")
expect.extend(matchers);
global.$ = $;
global.jQuery = $;
global.$.multiselect = multiselect;
global.multiselect = multiselect;


module.exports = {
    setupFilesAfterEnv: ['./setup-jest.js'],
};
