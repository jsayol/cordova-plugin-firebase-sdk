/**
 * The characters used to generate the push IDs.
 * See: https://gist.github.com/mikelehen/3596a30bd69384624c11
 */
const PUSH_CHARS = '-0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ_abcdefghijklmnopqrstuvwxyz';

let lastTime = 0;
let lastRandChars: number[] = [];

/**
 * Based on the code provided by Michael Lehenbauer: https://gist.github.com/mikelehen/3596a30bd69384624c11
 * Also see: https://firebase.googleblog.com/2015/02/the-2120-ways-to-ensure-unique_68.html
 *
 * @param timeDiff
 * @returns {string}
 * @internal
 */
export function generatePushKey(timeDiff = 0): string {
  let now = Date.now() - timeDiff;//this.db.timeDiff;
  const isDuplicateTime = (now === lastTime);

  lastTime = now;

  const timeStampChars = new Array(8);

  for (let i = 7; i >= 0; i--) {
    timeStampChars[i] = PUSH_CHARS.charAt(now % 64);

    // NOTE: Can't use << here because javascript will convert to int and lose the upper bits.
    now = Math.floor(now / 64);
  }

  if (now !== 0)
    throw new Error('generatePushId: We should have converted the entire timestamp.');

  let id = timeStampChars.join('');

  if (!isDuplicateTime) {
    for (let i = 0; i < 12; i++) {
      lastRandChars[i] = Math.floor(Math.random() * 64);
    }
  } else {
    // If the timestamp hasn't changed since last push, use the same random number, except incremented by 1.
    let i;
    for (i = 11; (i >= 0) && (lastRandChars[i] === 63); i--) {
      lastRandChars[i] = 0;
    }

    lastRandChars[i]++;
  }

  for (let i = 0; i < 12; i++) {
    id += PUSH_CHARS.charAt(lastRandChars[i]);
  }

  if (id.length != 20)
    throw new Error('generatePushKey: Length should be 20.');

  return id;
}
