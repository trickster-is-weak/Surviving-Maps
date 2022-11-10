import $ from 'jquery';

global.$ = $;
global.jQuery = $;

// If you want to mock bootstrap
global.$.fn.modal = jest.fn(() => $());
global.$.fn.carousel = jest.fn(() => $());
global.$.fn.tooltip = jest.fn(() => $());
const matchers = require('jest-extended');
expect.extend(matchers);

module.exports = {
    setupFilesAfterEnv: ['<rootDir>/setup-jest.js'],
};