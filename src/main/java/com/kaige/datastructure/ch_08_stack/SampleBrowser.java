package com.kaige.datastructure.ch_08_stack;

/**
 * 8-3 实现浏览器的前进与后退功能
 */
public class SampleBrowser {
  
  /**
   * 存储后退页面的栈
   */
  private final StackBasedOnLinkeList<String> backStack = new StackBasedOnLinkeList<>();
  
  /**
   * 存储前进页面的栈
   */
  private final StackBasedOnLinkeList<String> forwardStack = new StackBasedOnLinkeList<>();
  
  /**
   * 当前页面
   */
  private String currentPage = null;
  
  public static void main(String[] args) {
    SampleBrowser browser = new SampleBrowser();
    browser.open("www.baidu.com");
    browser.open("www.xinlang.com");
    browser.open("www.google.com");
    browser.goForward();
    browser.goBack();
    browser.goBack();
    browser.goBack();
    browser.goForward();
    browser.open("www.qq.com");
    browser.goBack();
    browser.open("www.360.com");
    browser.goBack();
    browser.checkCurrentPage();
    browser.goForward();
  }
  
  /**
   * 进入一个URL 页面
   *
   * @param url 页面 url
   */
  private void open(String url) {
    if (currentPage != null) {
      // 将当前页面压入 backStack 栈
      backStack.push(currentPage);
      // 清空 forwardStack 栈，即不允许浏览器前进跳转
      cleanStack(forwardStack);
    }
    showUrl(url, "Open");
  }
  
  /**
   * 是否可以前进页面
   *
   * @return 是或否
   */
  private boolean canGoForward() {
    return !forwardStack.isEmpty();
  }
  
  /**
   * 是否可以回退页面
   *
   * @return 是或否
   */
  private boolean canGoBack() {
    return !backStack.isEmpty();
  }
  
  /**
   * 显示当前页面
   *
   * @param url    页面 URL
   * @param prefix 页面前缀
   */
  private void showUrl(String url, String prefix) {
    currentPage = url;
    System.out.println(prefix + " page ==> " + url);
  }
  
  /**
   * 检查当前页面
   */
  private void checkCurrentPage() {
    System.out.println("Current Page is: " + currentPage);
  }

  /**
   * 清空栈元素
   *
   * @param stack 栈
   */
  private void cleanStack(StackBasedOnLinkeList<String> stack) {
    while (!stack.isEmpty()) {
      stack.pop();
    }
  }
  
  /**
   * 回退
   */
  private String goBack() {
    if (!canGoBack()) {
      System.out.println("** Cannot go back, no pages behead.");
      return null;
    }
    // 将当前页面压入到 forwardStack 栈中
    forwardStack.push(currentPage);
    // 弹出 backStack 栈栈顶的页面，并赋值给当前页面
    String backUrl = backStack.pop();
    showUrl(backUrl, "Back");
    return backUrl;
  }
  
  /**
   * 前进
   */
  private String goForward() {
    if (!canGoForward()) {
      System.out.println("** Cannot go forward, no pages ahead.");
      return null;
    }
    // 将当前页面压入 backStack 栈
    backStack.push(currentPage);
    // 弹出 forwardStack 栈顶的页面，并设置为当前页面
    String forwardUrl = forwardStack.pop();
    showUrl(forwardUrl, "Forward");
    return forwardUrl;
  }

}
