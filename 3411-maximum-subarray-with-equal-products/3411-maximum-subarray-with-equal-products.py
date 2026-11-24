class Solution:
    def gcd(self, a, b):
        while b != 0:
            a, b = b, a % b
        return a

    def maxLength(self, nums):
        left = 0
        product = 1
        max_len = 2

        for right in range(len(nums)):
            while self.gcd(product, nums[right]) > 1:
                product //= nums[left]
                left += 1

            product *= nums[right]

            max_len = max(max_len, right - left + 1)

        return max_len