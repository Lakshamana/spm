const readline = require('readline');
const fs = require('fs');

const args = process.argv.slice(2);

const methodName = args[0]
const oldfile = args[1]
const newfile = args[2]

console.log(oldfile)

// create instance of readline
// each instance is associated with single input stream
let rl = readline.createInterface({
    input: fs.createReadStream(oldfile)
});

let line_no = 0;

// event is emitted after each line

// end
rl.on('close', function(line) {
    console.log('Total lines : ' + line_no);
});

methodLines = ''

/**
 *
 * @param {String} methodName
 * @param {Array} line
 */
function extractMethod(methodName) {
  rl.on('line', line => {
    let openCurly = 0
    if (/public (.*) (.*).*\((.*)\).*\{/.test(line)) {
      openCurly++
    } else {
      if (line.includes('{')) openCurly++
      if (line.includes('}')) openCurly--;
    }
  });
}
