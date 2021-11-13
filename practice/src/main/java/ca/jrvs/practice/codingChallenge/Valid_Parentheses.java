package ca.jrvs.practice.codingChallenge;

import java.util.ArrayDeque;
import java.util.Deque;

class Valid_Parentheses {

  public boolean isValid(String s) {
    //check if odd number of char, else run code
    if (s.length() % 2 != 0)
      return false;
    Deque<Character> deque = new ArrayDeque<>();
    for (char c : s.toCharArray()) {
      //if open brace, add to stack
      if (c == '[' || c == '{' || c == '(') {
        deque.push(c);
        }
      //if close brace, check if last on deque is open brace
      //and pop if so
      else if (c == ')' && !deque.isEmpty() && deque.peek() == '(') {
        deque.pop();
      } else if (c == ']' && !deque.isEmpty() && deque.peek() == '[') {
        deque.pop();
      } else if (c == '}' && !deque.isEmpty() && deque.peek() == '{') {
        deque.pop();
        }
      }
    return deque.isEmpty();
    }
  }
