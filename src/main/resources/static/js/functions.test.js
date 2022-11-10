const fns = require('./functions')

test('adds 1 + 2 to equal 3', () => {
    expect(fns.sum(1, 2)).toBe(3);
});


test('checkEnable', () => {
    fns.checkEnable();
});