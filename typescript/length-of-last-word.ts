// https://leetcode.com/problems/length-of-last-word/

function lengthOfLastWord(s: string): number {
    let allTokens: string[] = s.split(/\s+/);
    //console.log("allTokens: " + allTokens);

    let words = allTokens.filter(token => /\w+/.test(token));
    //console.log("words: " + words);

    return words[words.length-1].length;
};

let input1 = "Hello World";
console.log(lengthOfLastWord(input1));
