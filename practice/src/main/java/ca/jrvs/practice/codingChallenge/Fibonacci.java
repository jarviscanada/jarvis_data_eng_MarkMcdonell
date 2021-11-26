package ca.jrvs.practice.codingChallenge;
public class Fibonacci {
  //bottom up approach has best performance
  public int fib(int n) {
    if (n == 0) {
      return 0;}
    if (n == 1 || n == 2) {
      return 1;}
    int[] bottom_up = new int[n+1];
    bottom_up[0]=0;
    bottom_up[1]=1;
    bottom_up[2]=1;
    for (int i = 3 ; i <= n ; i++){
      bottom_up[i] = bottom_up[i-1] + bottom_up[i-2];
    }
    return bottom_up[n];
  }
}
