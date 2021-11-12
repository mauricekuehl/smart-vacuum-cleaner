# smart-vacuum-cleaner
#### This was a school project to create a smart grid based vacuum cleaner. 
*it is pure bad slow spagetti code*
## How the program works 
First, the robot makes a circuit where the first priority is to go right, the second to go 
straight ahead, then left and backwards. (This is after testing the best combination I could achieve).  
After each movement it scans the distance to the right, to the front and to the left. So he creates a 
map.  
If there are still unknown fields like here:

![image](https://user-images.githubusercontent.com/63231445/141530433-041a3b8e-e96a-4509-bf13-f919f9e79f63.png)

He drives to them. 
Finally he makes a brute force (recursive backtracking),                                                                 
to find the best way to drive through each field. 
