const readline = require('readline')
const fs = require('fs')

const args = process.argv.slice(2)

const methodText = args[0]
const oldfile = args[1]

// create instance of readline
// each instance is associated with single input stream
let rl = readline.createInterface({
    input: fs.createReadStream(oldfile),
});

// const out = fs.createWriteStream(oldfile, {
//   flags: 'a'
// })

// event is emitted after each line

// end
rl.on('close', _ => {})

const methodLines = methodText.split('\n')
const trigger = 'jhipster-needle-entity-add-field'

rl.on('line', line => {
  console.log(line)
  if (line.includes(trigger)) {
    methodLines.forEach(l => console.log(l))
  }
})
