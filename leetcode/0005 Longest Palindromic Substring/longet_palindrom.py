class Solution:
    def longestPalindrome(self, s: str) -> str:
        longest_yet = s[0]
        longest_yet_len = 1
        
        center = len(s)/2 - 0.5 #1.5
        for i in range(0, math.ceil(len(s)/2)):
            left =  center - i     #1.5
            right = center + i    #1.5
            longest_possible = len(s) - i
            
            if(longest_yet_len >= longest_possible):
                return longest_yet
            
            left_longest = self.longest_palindrom_around(s, left)
            # print(left, left_longest)
            if i == 0:
                right_longest = left_longest
            else:
                right_longest = self.longest_palindrom_around(s, right)
                # print(right, right_longest)

            left_of_left_longest = self.longest_palindrom_around(s, left - 0.5)
            right_of_right_longest = self.longest_palindrom_around(s, right + 0.5)

            all_palindroms = [left_longest, right_longest, left_of_left_longest, right_of_right_longest]
            longest_palindrom = longest = max(all_palindroms, key=len)
            longest_palindrom_len = len(longest_palindrom)

            if(longest_palindrom_len > longest_yet_len):
                longest_yet_len = longest_palindrom_len
                longest_yet = longest_palindrom

        return longest_yet
    
    def longest_palindrom_around(self, s, index):
        is_index_whole_num = index % 1 == 0
        if is_index_whole_num:
            index = int(index)
            i = index - 1
            j = index + 1
            ret = s[index]
        else:
            i = math.floor(index)
            j = math.ceil(index)
            ret = ''
        
        if (i<0) or (j>len(s)):
            return ret
        
        while i >= 0 and j<len(s):
            if s[i] != s[j]:
                break
            ret = s[i:j+1]    
            i -= 1
            j += 1

        return ret

