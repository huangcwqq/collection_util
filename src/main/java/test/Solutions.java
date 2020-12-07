package test;

public class Solutions {

  public static void main(String[] args) {
      int[] nums = {0,0,1,1,1,2,2,3,3,4};
      System.out.println(removeDuplicates(nums));
    //
  }

    public static int removeDuplicates(int[] nums) {
        if(nums.length <= 1){
            return nums.length;
        }
        int i = 0;
        int j = 1;
        for(;j < nums.length;j++){
            if(nums[j] != nums[i]){
                i++;
                nums[i] = nums[j];
            }
        }
        return i+1;
    }
}
