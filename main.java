import java.util.Random;
import java.util.*;
public class SAUGSTROBOT_makeGrid{

    /*
        Es gibt 2 wichtige Funktionen:  1. robo: scannt die Umgebung
                                            Die Funktion nimmt noch ein int als größe des Testbereiches
                                            Es wird zurzeit noch ein Bereich zufällig erstellt (zum testen)
                                            Es werden alle informationen im Terminal mehr oder weniger verständlich angezeit (schön anzusehen ist wie der Roboter Infos dazugewinnt)

                                        2. make_clean_path: erstellt den besten Weg und dieser kann mit "clean()" ausgeführt werden, jedoch wird hier nicht viel im Terminal ausgegeben (nur Zeit + Route). 
                                           (kann nur mit der robo funktion ausgeführt werden) 

        Die namen der Variablen sind sehr fragwürdig (ändere ich vielleicht noch später) -> außer mir ist es schwer durchzusehen allerdings habe ich probiert so gut es geht schnell zu erklären was was macht   
    */
    

    public static void main(String[] args) {
        robo(8); // robo(6) bis 8 wenn nach absehbarer Zeit der burte force in make_clean_path passieren soll | robo(10) bis 25 wenn nur die robo Methode veranschaulicht werden soll  
        
        make_clean_path();
    }




    public static void robo(int l) { // makes a map 
        if(create_random_grid){create_random_grid(l);}
        basicStartGridSetup(l);
        printgrid(grid);
        int i = 0;
        do {
            scan();
            if (get_char_right()==' '){
                turnRight();
                moveForward();
                System.out.println("Move right");
            } else if (get_char_forward()==' '){
                moveForward();
                System.out.println("Move forward");
            } else if (get_char_left()==' '){
                turnLeft();
                moveForward();
                System.out.println("Move left");
            } else {
                System.out.println("Move back");
                turn180();
            }
            System.out.println("posX: " + posX + " posY: "+ posY + " | diX: " + diX + " diy: " + diY);
            printgrid(grid);
            i++;
        } while(!(posX == grid.length/2 && posY == 1) && i<100);
        System.out.println("-------------FINAL---------------");
        printgrid(grid);
        System.out.println("posX: " + posX + " posY: "+ posY + "in " + i +" moves");
        System.out.println("");
        clearUnkownSquares();
        System.out.println("With "+movecount+" times moveing forward and moveTo: "+moveToCount);
    }

    public static void make_clean_path(){ // creates a path for cleaning 
        System.out.println("clean x: "+posX+" y: "+ posY);
        printgrid(grid);
        cleanpath = new int[grid.length][grid[0].length];
        while(bestlist == null){
            for(int i = 0; i<grid[0].length ;i++){
                for(int u = 0;u<grid.length;u++){
                    // path[u][i] = 0;
                    if(grid[u][i]=='#'){
                        cleanpath[u][i] = doublesquare;
                    }else{
                        cleanpath[u][i] = 0;
                    }
                }
            }
            backtrack(posX, posY, 0, 0,new ArrayList<Integer>());
            if(xtimesondoublesquare < (grid.length-2)*(grid[0].length-2)*(doublesquare-1)){
                xtimesondoublesquare*=2;
                System.out.println(xtimesondoublesquare+" = xtimes... last try with trys: "+ durchgang);
            }else{
                xtimesondoublesquare=1;
                doublesquare++;
                printgridInt(cleanpath);
                System.out.println(xtimesondoublesquare+" = xtimes..."+ doublesquare+" = doublesquare last try with trys: "+ durchgang);
            }
        }
        System.out.println("the best path to clean:");
        for (Integer i : bestlist) {
            System.out.print(i +" | ");
        }
    }

    public static void clean(){ //drives the created path
        System.out.println("follow the created path");
        for (Integer i : bestlist) {
             turnTo(i);
             moveForward(); 
        }
    }

//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------    
               
    /*variables*/

    public static boolean create_random_grid = true;
    public static char[][] realgrid =  {{'#','#','#','#','#','#','#','#'}, //30 sek
                                        {'#',' ',' ',' ',' ','#','#','#'},
                                        {'#',' ',' ','#',' ',' ','#','#'},
                                        {'#',' ',' ',' ','#',' ','#','#'},
                                        {'#',' ','#','#',' ',' ',' ','#'},
                                        {'#',' ','#',' ',' ',' ',' ','#'},
                                        {'#',' ','#',' ',' ',' ','#','#'},
                                        {'#','#','#','#','#','#','#','#'}};                  
    public static char[][] grid;
    public static int[][] path;
    public static int posX = 4;
    public static int posY = 1;
    public static int diX = 0;
    public static int diY = 1;
    public static int movecount=0;
    public static int moveToCount=0;
    public static int[][] cleanpath;
    public static int bestMove = 999999;
    public static int durchgang = 0;
    public static int doublesquare = 1;
    public static int xtimesondoublesquare = 1;
    public static int recusion= 0;
    public static ArrayList<Integer> bestlist;
    public static ArrayList<Integer> list;
    public static int movetimeTurn = 10;

//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public static void moveForward(){ 
        movecount++;
        posX += diX;
        posY += diY;
        
    }
    public static void turnRight(){ 
        int diXz;
        diXz = diX;
        diX = diY;
        diY = -1*diXz;        
    }
    public static void turnLeft(){ 
        int diXz;
        diXz = diX;
        diX = -1*diY;
        diY = diXz;
    }
    public static void turn180(){
        turnRight();
        turnRight();
    }
    public static void backtrack(int x, int y, int move, int timesdouble ,ArrayList<Integer> list){
        recusion++;
        if(recusion%1000000000==0){
            if(durchgang == 99){
                return;
            }
            durchgang++;
            System.out.println(durchgang+"durchgang");
        }
        if(x==posX&&y==posY&&move>0){
            if(filled()){
                // System.out.println(move+":"+bestMove);
                if(move < bestMove){
                    System.out.println("new best"+move+" timesdouble: "+ timesdouble);
                    // printgridInt(cleanpath);
                    bestMove = move;
                    bestlist = new ArrayList<Integer>(list);
                    if(timesdouble-1 == xtimesondoublesquare){
                        durchgang = 99;
                    }
                }
                return;
            }
        }
        if(cleanpath.length>x+1){
            if (cleanpath[x + 1][y + 0] < doublesquare && timesdouble < xtimesondoublesquare){
                cleanpath[x + 1][y + 0]+=1;
                list.add(90);
                // System.out.println("x + 1, y + 0,move++"+move);
                if(cleanpath[x + 1][y + 0]==1){backtrack(x + 1, y + 0,move+1,timesdouble,list);}
                else{backtrack(x + 1, y + 0,move+1,timesdouble+1,list);}
                cleanpath[x + 1][y + 0]-=1;
                list.remove(list.size()-1);
            }
        }
        if(0<=x-1){
            if (cleanpath[x - 1][y + 0] < doublesquare && timesdouble < xtimesondoublesquare){
                cleanpath[x - 1][y + 0]+=1;
                list.add(270);
                // System.out.println("x - 1, y + 0,move++"+move);
                if(cleanpath[x - 1][y + 0]==1){backtrack(x - 1, y + 0,move+1,timesdouble,list);}
                else{backtrack(x - 1, y + 0,move+1,timesdouble+1,list);}
                cleanpath[x - 1][y + 0]-=1;
                list.remove(list.size()-1);
            }
        }
        if(cleanpath[0].length>y+1){
            if (cleanpath[x + 0][y + 1] < doublesquare && timesdouble < xtimesondoublesquare){
                cleanpath[x + 0][y + 1]+=1;
                list.add(0);
                // System.out.println("x + 0, y + 1,move++"+move);
                if(cleanpath[x + 0][y + 1]==1){backtrack(x + 0, y + 1,move+1,timesdouble,list);}
                else{backtrack(x + 0, y + 1,move+1,timesdouble+1,list);}
                cleanpath[x + 0][y + 1]-=1;
                list.remove(list.size()-1);
            }
        }
        if(0<=y-1){
            
            if (cleanpath[x + 0][y - 1] < doublesquare && timesdouble < xtimesondoublesquare){
                cleanpath[x + 0][y - 1]+=1;
                list.add(180);
                // System.out.println("x + 0, y - 1,move++"+move);
                if(cleanpath[x + 0][y - 1]==1){backtrack(x + 0, y - 1,move+1,timesdouble,list);}
                else{backtrack(x + 0, y - 1,move+1,timesdouble+1,list);}
                cleanpath[x + 0][y - 1]-=1;
                list.remove(list.size()-1);
            }
        }
    }
    public static boolean filled() {
        // System.out.println("try filled");
        for (int[] is : cleanpath) {
            for (int i : is) {
                if(i==0){
                    // System.out.println("false");
                    return false;
                }
            }
        }
        // System.out.println("true");
        return true;
    }
    public static void create_random_grid(int length){
        Random rand = new Random();
        realgrid = new char[length][length];
        for(int i = 0; i<realgrid.length;i++){
            for(int u = 0;u<realgrid.length;u++){
                if(i!=realgrid.length-1 && u != realgrid[0].length-1 && i!=0 && u!=0){
                    if(rand.nextDouble()<0.3){
                        realgrid[i][u] = '#';
                    }else{
                        realgrid[i][u] = ' ';
                    }
                }else{
                    realgrid[i][u] = '#';
                }
            }
        }
        System.out.println("This is the random grid");
        printgrid(realgrid); 
    }
    public static void basicStartGridSetup(int length){
        //setting up all grids etc. 
        grid =  new char[length][length];
        path = new int[length][length];
        // posX = grid.length/2;
        // posY = 1;
        // diX = 0;
        // diY = 1;
        realgrid[posX][posY] = ' ';
        //replaceing all unkown squares with a "?" in the final gird which the robo can see
        for(int c = 0; c<grid.length;c++){
            for(int cc = 0; cc<grid[0].length;cc++){
                if(c!=grid.length-1 && cc != grid[0].length-1 && c!=0 && cc!=0){
                    grid[c][cc] = '?';
                }else{
                    grid[c][cc] = '#';
                }
            }
        }
    }
    public static void printgrid(char[][] cArr){
        //printing out the grid like in math (x goes up to the left and y goes up upwards)
        System.out.println("print a grid");
        for(int i = cArr[0].length-1; i>=0;i--){
            System.out.print(i);
            for(int u = 0;u<cArr.length;u++){
                System.out.print(":" + cArr[u][i]);
            }
            System.out.println(":");
        }
        System.out.println("x:0:1:2:3:4:5:6:7:8:9:0:1:2:3:4:5:6:7:8:9");
    }
    public static void printgridInt(int[][] cArr){
        //printing out the grid like in math (x goes up to the left and y goes up upwards)
        System.out.println("printgridInt");
        for(int i = cArr[0].length-1; i>=0;i--){
            System.out.print(i);
            for(int u = 0;u<cArr.length;u++){
                if(cArr[u][i]<10 && cArr[u][i]>=0){
                    System.out.print(":" + cArr[u][i]+"0");
                }else{
                    System.out.print(":" + cArr[u][i]);
                }
            }
            System.out.println(":");
        }
    }
    public static void scan() {
        //Scaning arount a posX and posY   |   It gets the info where the next wall in all diactions are
        System.out.println("Scan at x: " + posX + " y: " + posY);
        for(int l = 0; realgrid[posX+l][posY] != '#';l++){
            grid[posX+l+1][posY] = realgrid[posX+l+1][posY]; 
            System.out.print(grid[posX+l+1][posY]+"1 | ");
        }
        for(int l = 0; realgrid[posX-l][posY] != '#';l++){
            grid[posX-l-1][posY] = realgrid[posX-l-1][posY]; 
            System.out.print(grid[posX-l-1][posY]+"2 | ");
        }
        for(int l = 0; realgrid[posX][posY+l] != '#';l++){
            grid[posX][posY+l+1] = realgrid[posX][posY+l+1]; 
            System.out.print (grid[posX][posY+l+1]+"3 | ");
        }
        for(int l = 0; realgrid[posX][posY-l] != '#';l++){
            grid[posX][posY-l-1] = realgrid[posX][posY-l-1]; 
            System.out.print (grid[posX][posY-l-1]+"4 | ");
        }
        System.out.println("");
        /*servo soll drehen 
            muss noch wenn ich weiß wie man die ansteuert etc. und werte abließt
        */

    }
    public static char get_char_forward(){return grid[diX+posX][diY+posY];}
    public static char get_char_right(){return grid[diY+posX][-1*diX+posY];}
    public static char get_char_left(){return grid[-1*diY+posX][diX+posY];}
    public static void turnTo(int degree) {
        if(degree==0){
            if(diX==1){
                turnLeft();
            }else if(diX==-1){
                turnRight();
            }else if(diY==-1){
                turn180();
            }
            if(diX!=0||diY!=1){
                System.out.println("ERROR turnTo1");
                System.exit(0);
            } 
        }else if(degree==90 || degree==-270){
            if(diY==1){
                turnRight();
            }else if(diY==-1){
                turnLeft();
            }else if(diX==-1){
                turn180();
            }
            if(diX!=1||diY!=0){
                System.out.println("ERROR turnTo2");
                System.exit(0);
            }
        }else if(degree==180 || degree==-180){
            if(diX==-1){
                turnLeft();
            }else if(diX==1){
                turnRight();
            }else if(diY==1){
                turn180();
            }if(diX!=0||diY!=-1){
                System.out.println("ERROR turnTo3");
                System.exit(0);
            }
            
        }else if(degree==270 || degree==-90){
            if(diY==-1){
                turnRight();
            }else if(diY==1){
                turnLeft();
            }else if(diX==1){
                turn180();
            }
            if(diX!=-1||diY!=0){
                System.out.println("ERROR turnTo4");
                System.exit(0);
            }   
        }
    }
    public static void clearUnkownSquares() {
        // when the robo is done it round it checks if some squares are not reachable if he doesnt kown he goes to the point
        for(int i = 0; i<grid[0].length ;i++){
            for(int u = 0;u<grid.length;u++){
                if(grid[u][i] == '?'){
                    if(check_if_square_isnt_reachable(u,i)){
                        grid[u][i] = '/';
                        clearUnkownSquares();
                        return;
                    }
                }
            }
        }
        System.out.println("----------------------------------");
        System.out.println("Now going to unkown sqaures");
        for(int i = 0; i<grid[0].length ;i++){
            for(int u = 0;u<grid.length;u++){
                if(grid[u][i] == '?'){
                    grid[u][i] = '#';
                }
            }
        }
        go_to_still_unkown_Squares();
        System.out.println("last bit");
        for(int i = 0; i<grid[0].length ;i++){
            for(int u = 0;u<grid.length;u++){
                if(grid[u][i] == '/'){
                    grid[u][i] = '#';
                }
            }
        }
        System.out.println("done with clean-Up");
        System.out.println("--------------------Final----------------------");
        printgrid(grid);
    }    
    public static boolean check_if_square_isnt_reachable(int x, int y) {
        if(grid.length>x+1)
            {if (grid[x + 1][y + 0] == ' '|| grid[x + 1][y + 0] == '/'){return true;}
        }else{return false;}
        if(0<=x-1)
            {if (grid[x - 1][y + 0] == ' '|| grid[x - 1][y + 0] == '/'){return true;}
        }else{return false;}
        if(grid[0].length>y+1)
            {if (grid[x + 0][y + 1] == ' ' || grid[x + 0][y + 1] == '/'){return true;}
        }else{return false;}
        if(0<=y-1)
            {if (grid[x + 0][y - 1] == ' '|| grid[x + 0][y - 1] == '/'){return true;}
        }else{return false;}
        return false;
    }
    public static void go_to_still_unkown_Squares(){
        //goes to reachable but unkown squares
        int[][] mostValueMove = new int[grid.length][grid[0].length];
        moveToCount++;
        int min = 99;
        for(int y = 0; y<grid[0].length ;y++){
            for(int x = 0;x<grid.length;x++){
                mostValueMove[x][y] = 99;
            }
        }
        //make a table with the distance
        for(int y = grid[0].length-1; y>=0 ;y--){
            for(int x = 0;x<grid.length-1;x++){
                if(grid[x][y] == '/'){
                    if (grid[x + 1][y + 0] == ' '){
                        mostValueMove[x+1][y] = Math.abs((posX-x+1)+(posY-y));
                    }
                    if (grid[x - 1][y + 0] == ' '){
                        mostValueMove[x-1][y] = Math.abs((posX-x-1)+(posY-y));
                    }
                    if (grid[x + 0][y + 1] == ' '){
                        mostValueMove[x][y+1] = Math.abs((posX-x)+(posY-y+1));
                    }
                    if (grid[x + 0][y - 1] == ' '){
                        mostValueMove[x][y-1] = Math.abs((posX-x)+(posY-y-1));
                    }
                }
            }
        }
        for(int y = grid[0].length-1; y>=0 ;y--){
            for(int x = 0;x<grid.length-1;x++){
                if(mostValueMove[x][y]<min){
                    min = mostValueMove[x][y];
                }
            }
        }
        //scan and if find square with smallest distance -> go to it
        for(int y = grid[0].length-1; y>=0 ;y--){
            for(int x = 0;x<grid.length-1;x++){
                if(mostValueMove[x][y]==min&&min!=99){
                    goToAndScan(x,y);
                    go_to_still_unkown_Squares();
                    return;
                }
            }
        }
    }
    public static void goToAndScan(int x, int y){
        System.out.println("go to x: " + x + " y:" + y+" from: x: "+posX+" y: "+posY);
        System.out.println("pathFinder");
        //preparing the grid for the pathfinder

        
        for(int i = 0; i<grid[0].length ;i++){
            for(int u = 0;u<grid.length;u++){
                if(grid[u][i] == ' '){
                    path[u][i] = 999;
                } else {
                    path[u][i] = -9;
                }
            }
        }
        //printgridInt(path);
        pathFinder(x, y, posX, posY, 1);
        list = new ArrayList<Integer>();
        markPath(x, y, path[x][y]);
        followPathAndScan();
        list.clear();
    }
    public static void pathFinder(int xfinal, int yfinal, int x, int y, int count) {
        path[x][y] = count;
        if(!(x == xfinal && y == yfinal)){
            if (path[x + 1][y + 0] > count+1){
                pathFinder(xfinal, yfinal, x + 1, y, count+1);
            }
            if (path[x - 1][y + 0] > count+1){
                pathFinder(xfinal, yfinal, x - 1, y, count+1);
            }
            if (path[x + 0][y + 1] > count+1){
                pathFinder(xfinal, yfinal, x, y +1, count+1);
            }
            if (path[x + 0][y - 1] > count+1){
                pathFinder(xfinal, yfinal, x, y - 1, count+1);
            }
            return;
        }
    }
    public static void followPathAndScan(){
        for(int i = list.size()-1; i>=0 ; i--){
            turnTo(list.get(i));
            moveForward();
        }
        scan();
    }
    public static void markPath(int x, int y, int count) {
        if(!(x == posX && y == posY)){
            if (path[x + 1][y + 0] == count - 1){
                list.add(270);
                markPath(x+1,y,count - 1);
                return;
            }
            if (path[x - 1][y + 0] == count - 1){
                list.add(90);
                markPath(x-1,y,count - 1);
                return;
            }
            if (path[x + 0][y + 1] == count - 1){
                list.add(180);
                markPath(x,y + 1 ,count - 1);
                return;
            }
            if (path[x + 0][y - 1] == count - 1){
                list.add(0);
                markPath(x,y -1,count - 1);
                return;
            }
        }
    }
}   