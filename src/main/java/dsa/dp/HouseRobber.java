package dsa.dp;

public class HouseRobber {

    public   int houseRobber(int[] nums) {

        if(nums.length==0) return 0;

        int[] sums = new int[nums.length+1];
        sums[0] = 0;
        sums[1] = nums[0];
        if(nums.length==1) return sums[1];

        sums[2] = nums[1]>nums[0] ? nums[1] : nums[0];
        if(nums.length==2)  return  sums[2];

        for (int i = 3 ; i < sums.length;i++){
            sums[i] = sums[i-3]+ nums[i-1] > sums[i-2] + nums[i-1] ? sums[i-3]+ nums[i-1] : sums[i-2]+ nums[i-1];
        }

        return  sums[sums.length-1] > sums[sums.length-2] ? sums[sums.length-1] :sums[sums.length-2];

    }
}
