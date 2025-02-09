/*
https://leetcode.com/problems/length-of-last-word/

Constraints:

    1 <= s.length <= 104
    s consists of only English letters and spaces ' '.
    There will be at least one word in s.

*/

function lengthOfLastWord(s: string): number {
    let allTokens: string[] = s.split(/\s+/);
    //console.log("allTokens: " + allTokens);

    let words = allTokens.filter(token => /\w+/.test(token));
    //console.log("words: " + words);

    return words[words.length-1].length;
};

let input1 = "Hello World";
console.log(lengthOfLastWord(input1));

let input2 = "   fly me   to   the moon  ";
console.log(lengthOfLastWord(input2));

let input3 = "luffy is still joyboy";
console.log(lengthOfLastWord(input3));
